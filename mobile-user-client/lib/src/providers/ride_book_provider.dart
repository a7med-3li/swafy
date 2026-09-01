import 'package:flutter/foundation.dart';
import 'package:geolocator/geolocator.dart';

import '../core/network/api_exception.dart';
import '../data/models/address_result.dart';
import '../data/repositories/address_repository.dart';

/// Encapsulates the result of resolving the passenger's current location.
class PickupLocation {
  const PickupLocation({required this.latitude, required this.longitude});

  final double latitude;
  final double longitude;
}

/// Search state for a single address lane (pickup or dropoff): query results
/// from autocomplete / full search, plus the address the user pinned.
class AddressSearchState {
  int _token = 0;
  bool _isSearching = false;
  bool _resultsFromSearch = false;
  String? _searchError;
  List<AddressResult> _results = const [];
  AddressResult? _selected;

  int get token => _token;
  bool get isSearching => _isSearching;
  bool get resultsFromSearch => _resultsFromSearch;
  String? get searchError => _searchError;
  List<AddressResult> get results => _results;
  AddressResult? get selected => _selected;
  bool get hasResults => _results.isNotEmpty;
  bool get hasSelection => _selected != null;

  /// Marks the start of a new query and invalidates older in-flight replies.
  void beginSearch() {
    _token++;
    _isSearching = true;
    _resultsFromSearch = false;
    _searchError = null;
  }

  void finishSearch() {
    _isSearching = false;
  }

  void setResults(List<AddressResult> list, {required bool fromFullSearch}) {
    _results = list;
    _resultsFromSearch = fromFullSearch;
  }

  void setError(String message) {
    _results = const [];
    _resultsFromSearch = false;
    _searchError = message;
  }

  void select(AddressResult address) {
    _selected = address;
    _results = const [];
    _resultsFromSearch = false;
    _searchError = null;
    _isSearching = false;
    _token++;
  }

  void clearSelection() {
    _selected = null;
  }

  void clearResults() {
    _token++;
    _results = const [];
    _resultsFromSearch = false;
    _searchError = null;
    _isSearching = false;
  }
}

/// Manages the Book-a-Ride flow: two independent search lanes (pickup and
/// dropoff), each backed by autocomplete + full-search fallback, with the
/// device's current location offered as the pickup default when available.
class RideBookProvider extends ChangeNotifier {
  RideBookProvider({required AddressRepository addressRepository})
      : _addressRepo = addressRepository;

  final AddressRepository _addressRepo;

  // ── Device location state (pickup default) ──────────────────────────
  bool _isLocating = false;
  PickupLocation? _deviceLocation;
  String? _locationError;

  // ── Search lanes ────────────────────────────────────────────────────
  final AddressSearchState _pickup = AddressSearchState();
  final AddressSearchState _dropoff = AddressSearchState();

  bool get isLocating => _isLocating;
  PickupLocation? get deviceLocation => _deviceLocation;
  String? get locationError => _locationError;
  bool get hasDeviceLocation => _deviceLocation != null;

  AddressSearchState get pickupSearch => _pickup;
  AddressSearchState get dropoffSearch => _dropoff;
  AddressResult? get pickupSelected => _pickup.selected;
  AddressResult? get dropoffSelected => _dropoff.selected;

  bool get hasPickup => _pickup.hasSelection || hasDeviceLocation;
  bool get hasDropoff => _dropoff.hasSelection;
  bool get canRequestRide => hasPickup && hasDropoff;

  /// Coordinates to use as the ride pickup: the pinned address when the user
  /// searched for one, otherwise the device's current location.
  PickupLocation? get pickupCoordinates {
    final addr = _pickup.selected;
    if (addr != null && addr.lat != null && addr.lng != null) {
      return PickupLocation(latitude: addr.lat!, longitude: addr.lng!);
    }
    return _deviceLocation;
  }

  /// Fetches the device's current location as the pickup default.
  Future<void> loadCurrentLocation() async {
    _isLocating = true;
    _locationError = null;
    notifyListeners();

    try {
      var serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) {
        throw const PickupLocationException('خدمة الموقع غير مفعلة على الجهاز.');
      }

      var permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
      }
      if (permission == LocationPermission.denied) {
        throw const PickupLocationException('تم رفض الإذن بالوصول إلى الموقع.');
      }
      if (permission == LocationPermission.deniedForever) {
        throw const PickupLocationException(
            'إذن الموقع مرفوض بشكل دائم. فعّله من إعدادات الجهاز.');
      }

      final position = await Geolocator.getCurrentPosition(
        locationSettings: const LocationSettings(
          accuracy: LocationAccuracy.high,
          timeLimit: Duration(seconds: 15),
        ),
      );

      _deviceLocation = PickupLocation(
        latitude: position.latitude,
        longitude: position.longitude,
      );
    } on PickupLocationException catch (e) {
      _locationError = e.message;
    } on ApiException catch (e) {
      _locationError = e.message;
    } catch (e) {
      debugPrint('⚠️ [RideBookProvider] location error: $e');
      _locationError = 'تعذر تحديد موقعك الحالي. حاول مرة أخرى.';
    }

    _isLocating = false;
    notifyListeners();
  }

  Future<void> autoCompletePickup(String query) =>
      _autoComplete(_pickup, query);
  Future<void> autoCompleteDropoff(String query) =>
      _autoComplete(_dropoff, query);
  Future<void> searchPickup(String query) => _fullSearch(_pickup, query);
  Future<void> searchDropoff(String query) => _fullSearch(_dropoff, query);

  /// Runs autocomplete for [query]. Cheap, fast — used while typing.
  Future<void> _autoComplete(AddressSearchState search, String query) async {
    search.beginSearch();
    final token = search.token;
    notifyListeners();

    try {
      final list = await _addressRepo.autoComplete(query);
      if (token != search.token) return; // stale response
      search.setResults(list, fromFullSearch: false);
    } on ApiException catch (e) {
      if (token != search.token) return;
      search.setError(e.message);
    } catch (e) {
      if (token != search.token) return;
      debugPrint('⚠️ [RideBookProvider] autoComplete error: $e');
      search.setError('حدث خطأ أثناء البحث.');
    }

    search.finishSearch();
    notifyListeners();
  }

  /// Runs the full search for [query] (maps API). Called explicitly when
  /// autocomplete returned no results and the user taps the fallback button.
  Future<void> _fullSearch(AddressSearchState search, String query) async {
    search.beginSearch();
    final token = search.token;
    notifyListeners();

    try {
      final list = await _addressRepo.search(query);
      if (token != search.token) return;
      search.setResults(list, fromFullSearch: true);
    } on ApiException catch (e) {
      if (token != search.token) return;
      search.setError(e.message);
    } catch (e) {
      if (token != search.token) return;
      debugPrint('⚠️ [RideBookProvider] search error: $e');
      search.setError('حدث خطأ أثناء البحث.');
    }

    search.finishSearch();
    notifyListeners();
  }

  void selectPickup(AddressResult address) {
    _pickup.select(address);
    notifyListeners();
  }

  void selectDropoff(AddressResult address) {
    _dropoff.select(address);
    notifyListeners();
  }

  void clearPickupSelection() {
    _pickup.clearSelection();
    notifyListeners();
  }

  void clearDropoffSelection() {
    _dropoff.clearSelection();
    notifyListeners();
  }

  void clearPickupSearch() {
    _pickup.clearResults();
    notifyListeners();
  }

  void clearDropoffSearch() {
    _dropoff.clearResults();
    notifyListeners();
  }

  void clear() {
    _pickup.clearResults();
    _pickup.clearSelection();
    _dropoff.clearResults();
    _dropoff.clearSelection();
    _deviceLocation = null;
    _locationError = null;
    notifyListeners();
  }
}

/// A domain error used to surface user-friendly location failures.
class PickupLocationException implements Exception {
  const PickupLocationException(this.message);

  final String message;
}