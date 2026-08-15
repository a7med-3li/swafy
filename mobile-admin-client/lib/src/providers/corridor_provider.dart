import 'package:flutter/foundation.dart';

import '../core/network/api_exception.dart';
import '../data/models/corridor_response.dart';
import '../data/repositories/corridor_repository.dart';

/// Manages corridor data state for the admin app.
/// Includes corridor creation capability and caching.
class CorridorProvider extends ChangeNotifier {
  CorridorProvider({required CorridorRepository corridorRepository})
      : _repo = corridorRepository;

  final CorridorRepository _repo;

  // ── State ───────────────────────────────────────────────────────────

  List<CorridorResponse> _corridors = [];
  bool _isLoading = false;
  bool _isCreating = false;
  String? _error;
  String? _successMessage;
  DateTime? _lastFetchTime;

  static const _cacheDuration = Duration(minutes: 5);

  List<CorridorResponse> get corridors => List.unmodifiable(_corridors);
  bool get isLoading => _isLoading;
  bool get isCreating => _isCreating;
  String? get error => _error;
  String? get successMessage => _successMessage;

  bool get _isCacheValid =>
      _lastFetchTime != null &&
      DateTime.now().difference(_lastFetchTime!) < _cacheDuration &&
      _corridors.isNotEmpty;

  // ── Load corridors ─────────────────────────────────────────────────

  /// Fetches all corridors from the backend.
  Future<void> loadCorridors() async {
    if (_isCacheValid) return;

    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _corridors = await _repo.getAllCorridors();
      _lastFetchTime = DateTime.now();
    } on ApiException catch (e) {
      _error = e.message;
      _corridors = [];
      _lastFetchTime = null;
    } catch (_) {
      _error = 'تعذر تحميل المسارات.';
      _corridors = [];
      _lastFetchTime = null;
    }

    _isLoading = false;
    notifyListeners();
  }

  /// Forces a fresh fetch ignoring cache.
  Future<void> forceRefresh() async {
    _lastFetchTime = null;
    await loadCorridors();
  }

  // ── Create corridor ────────────────────────────────────────────────

  /// Creates a new corridor and refreshes the list on success.
  Future<bool> createCorridor({
    required String name,
    required String route,
    required double price,
    required List<Map<String, dynamic>> stops,
  }) async {
    _isCreating = true;
    _error = null;
    _successMessage = null;
    notifyListeners();

    try {
      await _repo.createCorridor(
        name: name,
        route: route,
        price: price,
        stops: stops,
      );
      _successMessage = 'تم إنشاء المسار "$name" بنجاح!';
      _isCreating = false;
      notifyListeners();

      // Refresh the list to include the new corridor.
      _lastFetchTime = null;
      await loadCorridors();

      return true;
    } on ApiException catch (e) {
      _error = e.message;
      _isCreating = false;
      notifyListeners();
      return false;
    } catch (_) {
      _error = 'حدث خطأ أثناء إنشاء المسار.';
      _isCreating = false;
      notifyListeners();
      return false;
    }
  }

  /// Finds a corridor by [id], or returns null.
  CorridorResponse? findById(int id) {
    try {
      return _corridors.firstWhere((c) => c.id == id);
    } catch (_) {
      return null;
    }
  }

  void clearError() {
    _error = null;
    notifyListeners();
  }

  void clearMessages() {
    _error = null;
    _successMessage = null;
    notifyListeners();
  }
}
