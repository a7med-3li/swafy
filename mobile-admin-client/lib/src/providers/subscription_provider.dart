import 'package:flutter/foundation.dart';

import '../core/network/api_exception.dart';
import '../data/models/corridor_response.dart';
import '../data/models/subscription_response.dart';
import '../data/repositories/subscription_repository.dart';

/// Manages subscription state (active, history, purchase).
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

  SubscriptionResponse? get active => _active;
  List<SubscriptionResponse> get history => List.unmodifiable(_history);
  bool get isLoading => _isLoading;
  String? get error => _error;
  String? get successMessage => _successMessage;

  // ── Load active subscription ───────────────────────────────────────

  Future<void> loadActive() async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _active = await _repo.getActive();
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
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _history = await _repo.getHistory();
    } on ApiException catch (e) {
      _error = e.message;
      _history = [];
    } catch (_) {
      _error = 'تعذر تحميل سجل الاشتراكات.';
      _history = [];
    }

    _isLoading = false;
    notifyListeners();
  }

  // ── Purchase ───────────────────────────────────────────────────────

  /// Subscribes to a [corridor]. Shows success message on completion.
  Future<bool> purchase(CorridorResponse corridor) async {
    _isLoading = true;
    _error = null;
    _successMessage = null;
    notifyListeners();

    try {
      _active = await _repo.purchase(corridor);
      _successMessage = 'تم الاشتراك بنجاح!';
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
