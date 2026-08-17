import 'package:flutter/foundation.dart';

import '../core/network/api_exception.dart';
import '../data/models/notification_page_response.dart';
import '../data/models/notification_response.dart';
import '../data/repositories/notification_repository.dart';

/// Manages notification data state with unread tracking.
class NotificationProvider extends ChangeNotifier {
  NotificationProvider({required NotificationRepository notificationRepository})
      : _repo = notificationRepository;

  final NotificationRepository _repo;

  // ── State ───────────────────────────────────────────────────────────

  List<NotificationResponse> _notifications = [];
  NotificationPageResponse? _page;
  int _unreadCount = 0;
  bool _isLoading = false;
  String? _error;

  List<NotificationResponse> get notifications => List.unmodifiable(_notifications);
  NotificationPageResponse? get page => _page;
  int get unreadCount => _unreadCount;
  bool get isLoading => _isLoading;
  String? get error => _error;

  // ── Load notifications ─────────────────────────────────────────────

  Future<void> loadNotifications() async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _page = await _repo.getAllNotifications();
      _notifications = (_page?.content ?? const []).toList()
        ..sort((a, b) {
          final aDate = a.createdAt ?? '';
          final bDate = b.createdAt ?? '';
          return bDate.compareTo(aDate); // newest first
        });
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

  // ── Unread count ───────────────────────────────────────────────────

  Future<void> loadUnreadCount() async {
    try {
      _unreadCount = await _repo.getUnreadCount();
      notifyListeners();
    } catch (_) {
      // Silently fail — badge just won't update
    }
  }

  // ── Mark as read ───────────────────────────────────────────────────

  Future<void> markAsRead(String id) async {
    try {
      await _repo.markAsRead(id);

      // Update local state
      _notifications = _notifications.map((n) {
        if (n.id == id) {
          return NotificationResponse(
            id: n.id,
            title: n.title,
            shortDescription: n.shortDescription,
            message: n.message,
            status: 'READ',
            createdAt: n.createdAt,
          );
        }
        return n;
      }).toList();

      // Decrement unread count (but not below 0)
      if (_unreadCount > 0) _unreadCount--;

      notifyListeners();
    } on ApiException catch (_) {
      // Silently fail — UI stays as-is
    } catch (_) {
      // Silently fail
    }
  }

  /// Finds a notification by [id], or returns null.
  NotificationResponse? findById(String id) {
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
