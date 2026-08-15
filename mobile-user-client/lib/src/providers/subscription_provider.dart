import 'package:flutter/foundation.dart';

import '../core/network/api_exception.dart';
import '../data/models/corridor_response.dart';
import '../data/models/subscription_response.dart';
import '../data/repositories/subscription_repository.dart';

/// Manages subscription state (active, history, purchase) with caching
/// to prevent redundant API calls.
class SubscriptionProvider extends ChangeNotifier {
  SubscriptionProvider({required SubscriptionRepository subscriptionRepository})
      : _repo = subscriptionRepository;

  final SubscriptionRepository _repo;

  // ── State ───────────────────────────────────────────────────────────

  SubscriptionResponse? _active;
  List<SubscriptionResponse> _history = [];
  bool _isLoading = false;
  String? _error;
  String? _successMessage;
  DateTime? _activeLastFetch;
  DateTime? _historyLastFetch;

  /// Cache TTL for subscription data.
  static const _cacheDuration = Duration(minutes: 3);

  SubscriptionResponse? get active => _active;
  List<SubscriptionResponse> get history => List.unmodifiable(_history);
  bool get isLoading => _isLoading;
  String? get error => _error;
  String? get successMessage => _successMessage;

  bool get _isActiveCacheValid =>
      _activeLastFetch != null &&
      DateTime.now().difference(_activeLastFetch!) < _cacheDuration;

  bool get _isHistoryCacheValid =>
      _historyLastFetch != null &&
      DateTime.now().difference(_historyLastFetch!) < _cacheDuration &&
      _history.isNotEmpty;

  // ── Load active subscription ───────────────────────────────────────

  Future<void> loadActive() async {
    if (_isActiveCacheValid) return;

    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _active = await _repo.getActive();
      _activeLastFetch = DateTime.now();
    } on ApiException catch (e) {
      _error = e.message;
    } catch (_) {
      // Silently handle — it's okay if there's no active subscription.
    }

    _isLoading = false;
    notifyListeners();
  }

  // ── Load history ───────────────────────────────────────────────────

  Future<void> loadHistory() async {
    if (_isHistoryCacheValid) return;

    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _history = await _repo.getHistory();
      _historyLastFetch = DateTime.now();
    } on ApiException catch (e) {
      _error = e.message;
      _history = [];
      _historyLastFetch = null;
    } catch (_) {
      _error = 'تعذر تحميل سجل الاشتراكات.';
      _history = [];
      _historyLastFetch = null;
    }

    _isLoading = false;
    notifyListeners();
  }

  /// Forces a fresh fetch of both active subscription and history.
  Future<void> forceRefresh() async {
    _activeLastFetch = null;
    _historyLastFetch = null;
    await Future.wait([loadActive(), loadHistory()]);
  }

  // ── Purchase ───────────────────────────────────────────────────────

  /// Subscribes to a [corridor]. Shows success message on completion.
  Future<bool> purchase(CorridorResponse corridor) async {
    _isLoading = true;
    _error = null;
    _successMessage = null;
    notifyListeners();

    try {
      final newSub = await _repo.purchase(corridor);
      _active = newSub;
      
      // Update local history immediately for UI synchronization
      final existingIndex = _history.indexWhere((s) => s.id == newSub.id);
      if (existingIndex >= 0) {
        _history[existingIndex] = newSub;
      } else {
        _history = [newSub, ..._history];
      }
      
      _successMessage = 'تم الاشتراك بنجاح!';
      
      // Mark caches as valid so navigating to the tab uses this up-to-date local state
      _activeLastFetch = DateTime.now();
      _historyLastFetch = DateTime.now();
      
      _isLoading = false;
      notifyListeners();
      return true;
    } on ApiException catch (e) {
      _error = e.message;
      _isLoading = false;
      notifyListeners();
      return false;
    } catch (_) {
      _error = 'حدث خطأ أثناء الاشتراك.';
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  // ── Cancel ─────────────────────────────────────────────────────────

  Future<bool> cancel(int id) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      await _repo.cancel(id);
      _active = null;
      _successMessage = 'تم إلغاء الاشتراك.';
      _activeLastFetch = null;
      _historyLastFetch = null;
      _isLoading = false;
      notifyListeners();
      return true;
    } on ApiException catch (e) {
      _error = e.message;
      _isLoading = false;
      notifyListeners();
      return false;
    } catch (_) {
      _error = 'حدث خطأ أثناء إلغاء الاشتراك.';
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  void clearMessages() {
    _error = null;
    _successMessage = null;
    notifyListeners();
  }
}
