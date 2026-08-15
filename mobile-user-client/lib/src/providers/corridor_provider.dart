import 'package:flutter/foundation.dart';

import '../core/network/api_exception.dart';
import '../data/models/corridor_response.dart';
import '../data/repositories/corridor_repository.dart';

/// Manages corridor data state with a 5-minute cache to avoid redundant
/// network requests on tab switches.
class CorridorProvider extends ChangeNotifier {
  CorridorProvider({required CorridorRepository corridorRepository})
      : _repo = corridorRepository;

  final CorridorRepository _repo;

  // ── State ───────────────────────────────────────────────────────────

  List<CorridorResponse> _corridors = [];
  bool _isLoading = false;
  String? _error;
  DateTime? _lastFetchTime;

  /// Cache TTL — corridors rarely change, 5 minutes is safe.
  static const _cacheDuration = Duration(minutes: 5);

  List<CorridorResponse> get corridors => List.unmodifiable(_corridors);
  bool get isLoading => _isLoading;
  String? get error => _error;

  bool get _isCacheValid =>
      _lastFetchTime != null &&
      DateTime.now().difference(_lastFetchTime!) < _cacheDuration &&
      _corridors.isNotEmpty;

  // ── Load corridors ─────────────────────────────────────────────────

  /// Fetches all corridors from the backend.
  /// Skips the request if cached data is still fresh.
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

  /// Forces a fresh fetch from the backend, ignoring the cache.
  Future<void> forceRefresh() async {
    _lastFetchTime = null;
    await loadCorridors();
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
}
