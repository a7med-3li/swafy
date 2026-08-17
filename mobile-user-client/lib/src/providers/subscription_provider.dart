import 'package:flutter/foundation.dart';

import '../core/network/api_exception.dart';
import '../data/models/corridor_response.dart';
import '../data/models/subscription_response.dart';
import '../data/repositories/subscription_repository.dart';

/// Manages subscription state (active, pending, history) with caching
/// to prevent redundant API calls.
class SubscriptionProvider extends ChangeNotifier {
  SubscriptionProvider({required SubscriptionRepository subscriptionRepository})
      : _repo = subscriptionRepository;

  final SubscriptionRepository _repo;

  // ── State ───────────────────────────────────────────────────────────

  List<SubscriptionResponse> _active = [];
  List<SubscriptionResponse> _pending = [];
  List<SubscriptionResponse> _history = [];
  bool _isLoading = false;
  String? _error;
  String? _successMessage;
  DateTime? _activeLastFetch;
  DateTime? _pendingLastFetch;
  DateTime? _historyLastFetch;

  /// Cache TTL for subscription data.
  static const _cacheDuration = Duration(minutes: 3);

  List<SubscriptionResponse> get active => List.unmodifiable(_active);
  List<SubscriptionResponse> get pending => List.unmodifiable(_pending);
  List<SubscriptionResponse> get history => List.unmodifiable(_history);
  bool get isLoading => _isLoading;
  String? get error => _error;
  String? get successMessage => _successMessage;

  bool get _isActiveCacheValid =>
      _activeLastFetch != null &&
      DateTime.now().difference(_activeLastFetch!) < _cacheDuration;

  bool get _isPendingCacheValid =>
      _pendingLastFetch != null &&
      DateTime.now().difference(_pendingLastFetch!) < _cacheDuration;

  bool get _isHistoryCacheValid =>
      _historyLastFetch != null &&
      DateTime.now().difference(_historyLastFetch!) < _cacheDuration;

  // ── Load active subscriptions ──────────────────────────────────────

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

  // ── Load pending subscriptions ─────────────────────────────────────

  Future<void> loadPending() async {
    if (_isPendingCacheValid) return;

    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _pending = await _repo.getPending();
      _pendingLastFetch = DateTime.now();
    } on ApiException catch (e) {
      _error = e.message;
      _pending = [];
      _pendingLastFetch = null;
    } catch (_) {
      _pending = [];
      _pendingLastFetch = null;
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

  /// Forces a fresh fetch of active, pending, and history.
  Future<void> forceRefresh() async {
    _activeLastFetch = null;
    _pendingLastFetch = null;
    _historyLastFetch = null;
    await Future.wait([loadActive(), loadPending(), loadHistory()]);
  }

  // ── Purchase ───────────────────────────────────────────────────────

  /// Subscribes to a [corridor]. Returns the new subscription on success.
  Future<SubscriptionResponse?> purchase(CorridorResponse corridor) async {
    _isLoading = true;
    _error = null;
    _successMessage = null;
    notifyListeners();

    try {
      final newSub = await _repo.purchase(corridor);

      // Add to pending since purchase creates a request awaiting approval
      _pending = [newSub, ..._pending];

      _successMessage = 'تم تقديم طلب الاشتراك بنجاح!';

      // Invalidate caches so next load fetches fresh data from the server
      _activeLastFetch = null;
      _pendingLastFetch = null;
      _historyLastFetch = null;

      _isLoading = false;
      notifyListeners();
      return newSub;
    } on ApiException catch (e) {
      _error = e.message;
      _isLoading = false;
      notifyListeners();
      return null;
    } catch (_) {
      _error = 'حدث خطأ أثناء الاشتراك.';
      _isLoading = false;
      notifyListeners();
      return null;
    }
  }

  // ── Cancel ─────────────────────────────────────────────────────────

  Future<bool> cancel(int id) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      await _repo.cancel(id);
      _active = _active.where((s) => s.id != id).toList();
      _pending = _pending.where((s) => s.id != id).toList();
      _successMessage = 'تم إلغاء الاشتراك.';
      _activeLastFetch = null;
      _pendingLastFetch = null;
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
