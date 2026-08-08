import 'package:flutter/foundation.dart';

import '../core/network/api_exception.dart';
import '../data/models/notification_page_response.dart';
import '../data/models/notification_response.dart';
import '../data/repositories/notification_repository.dart';

/// Manages notification data state.
class NotificationProvider extends ChangeNotifier {
  NotificationProvider({required NotificationRepository notificationRepository})
      : _repo = notificationRepository;

  final NotificationRepository _repo;

  // ── State ───────────────────────────────────────────────────────────

  List<NotificationResponse> _notifications = [];
  NotificationPageResponse? _page;
  bool _isLoading = false;
  String? _error;

  List<NotificationResponse> get notifications => List.unmodifiable(_notifications);
  NotificationPageResponse? get page => _page;
  bool get isLoading => _isLoading;
  String? get error => _error;

  // ── Load notifications ─────────────────────────────────────────────

  /// Fetches all notifications from the backend.
  Future<void> loadNotifications() async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _page = await _repo.getAllNotifications();
      _notifications = _page?.content ?? const [];
    } on ApiException catch (e) {
      _error = e.message;
      _notifications = [];
      _page = null;
    } catch (_) {
      _error = 'تعذر تحميل الإشعارات.';
      _notifications = [];
      _page = null;
    }

    _isLoading = false;
    notifyListeners();
  }

  /// Finds a notification by [id], or returns null.
  NotificationResponse? findById(int id) {
    try {
      return _notifications.firstWhere((n) => n.id == id);
    } catch (_) {
      return null;
    }
  }

  void clearError() {
    _error = null;
    notifyListeners();
  }
}
