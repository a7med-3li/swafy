import 'package:flutter/foundation.dart';

import '../core/network/api_exception.dart';
import '../data/models/corridor_response.dart';
import '../data/repositories/corridor_repository.dart';

/// Manages corridor data state.
class CorridorProvider extends ChangeNotifier {
  CorridorProvider({required CorridorRepository corridorRepository})
      : _repo = corridorRepository;

  final CorridorRepository _repo;

  // ── State ───────────────────────────────────────────────────────────

  List<CorridorResponse> _corridors = [];
  bool _isLoading = false;
  String? _error;

  List<CorridorResponse> get corridors => List.unmodifiable(_corridors);
  bool get isLoading => _isLoading;
  String? get error => _error;

  // ── Load corridors ─────────────────────────────────────────────────

  /// Fetches all corridors from the backend.
  Future<void> loadCorridors() async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _corridors = await _repo.getAllCorridors();
    } on ApiException catch (e) {
      _error = e.message;
      _corridors = [];
    } catch (_) {
      _error = 'تعذر تحميل المسارات.';
      _corridors = [];
    }

    _isLoading = false;
    notifyListeners();
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
