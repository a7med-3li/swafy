import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../data/models/notification_response.dart';
import '../../providers/notification_provider.dart';
import '../../theme/theme.dart';

/// Screen displaying all notifications with read/unread styling.
class NotificationsScreen extends StatefulWidget {
  const NotificationsScreen({super.key});

  @override
  State<NotificationsScreen> createState() => _NotificationsScreenState();
}

class _NotificationsScreenState extends State<NotificationsScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<NotificationProvider>().loadNotifications();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: context.bg,
        appBar: AppBar(
          title: const Text('الإشعارات'),
          backgroundColor: Colors.transparent,
          elevation: 0,
        ),
        body: Consumer<NotificationProvider>(
          builder: (context, provider, _) {
            if (provider.isLoading) {
              return Center(
                child: CircularProgressIndicator(color: VamoTheme.accent, strokeWidth: 3),
              );
            }

            if (provider.error != null) {
              return _buildErrorState(context, provider);
            }

            if (provider.notifications.isEmpty) {
              return _buildEmptyState(context);
            }

            return RefreshIndicator(
              onRefresh: () async {
                await provider.loadNotifications();
                await provider.loadUnreadCount();
              },
              color: VamoTheme.accent,
              child: ListView.builder(
                padding: const EdgeInsets.fromLTRB(20, 12, 20, 24),
                physics: const AlwaysScrollableScrollPhysics(
                  parent: BouncingScrollPhysics(),
                ),
                itemCount: provider.notifications.length,
                itemBuilder: (context, index) {
                  final notification = provider.notifications[index];
                  return _NotificationCard(
                    notification: notification,
                    onTap: () => _openNotification(context, notification),
                  );
                },
              ),
            );
          },
        ),
      ),
    );
  }

  void _openNotification(BuildContext context, NotificationResponse notification) {
    // Mark as read
    if (notification.isUnread) {
      context.read<NotificationProvider>().markAsRead(notification.id);
    }

    // Show detail popup
    showDialog(
      context: context,
      builder: (ctx) => _NotificationDetailDialog(notification: notification),
    );
  }

  Widget _buildErrorState(BuildContext context, NotificationProvider provider) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(32),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 72,
              height: 72,
              decoration: BoxDecoration(
                color: const Color(0xFFEF4444).withValues(alpha: 0.1),
                borderRadius: BorderRadius.circular(22),
              ),
              child: const Icon(Icons.error_outline_rounded, color: Color(0xFFEF4444), size: 36),
            ),
            const SizedBox(height: 20),
            Text(
              'تعذر تحميل الإشعارات',
              style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w800),
            ),
            const SizedBox(height: 8),
            Text(
              provider.error ?? 'حدث خطأ غير متوقع',
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(color: context.subtitleColor),
            ),
            const SizedBox(height: 24),
            FilledButton.icon(
              onPressed: () => provider.loadNotifications(),
              icon: const Icon(Icons.refresh_rounded, size: 20),
              label: const Text('إعادة المحاولة'),
              style: FilledButton.styleFrom(backgroundColor: VamoTheme.accent),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildEmptyState(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(32),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 88,
              height: 88,
              decoration: BoxDecoration(
                color: context.cardColor,
                borderRadius: BorderRadius.circular(28),
                border: Border.all(color: context.cardBorderColor),
              ),
              child: Icon(
                Icons.notifications_none_rounded,
                color: context.subtitleColor,
                size: 42,
              ),
            ),
            const SizedBox(height: 24),
            Text(
              'لا توجد إشعارات',
              style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w800),
            ),
            const SizedBox(height: 8),
            Text(
              'ستظهر هنا جميع إشعاراتك عند وصولها',
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                    color: context.subtitleColor,
                    height: 1.5,
                  ),
            ),
          ],
        ),
      ),
    );
  }
}

// ── Notification Card ───────────────────────────────────────────────

class _NotificationCard extends StatelessWidget {
  const _NotificationCard({
    required this.notification,
    required this.onTap,
  });

  final NotificationResponse notification;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    final isUnread = notification.isUnread;
    final timeAgo = _formatTimeAgo(notification.createdAt);

    return GestureDetector(
      onTap: onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        margin: const EdgeInsets.only(bottom: 12),
        padding: const EdgeInsets.all(18),
        decoration: BoxDecoration(
          color: isUnread
              ? VamoTheme.accent.withValues(alpha: 0.06)
              : context.cardColor,
          borderRadius: BorderRadius.circular(20),
          border: Border.all(
            color: isUnread
                ? VamoTheme.accent.withValues(alpha: 0.25)
                : context.cardBorderColor,
            width: isUnread ? 1.5 : 1,
          ),
        ),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Unread indicator or icon
            if (isUnread)
              Container(
                width: 44,
                height: 44,
                decoration: BoxDecoration(
                  color: VamoTheme.accent.withValues(alpha: 0.15),
                  borderRadius: BorderRadius.circular(14),
                ),
                child: Icon(
                  _getNotificationIcon(notification),
                  color: VamoTheme.accent,
                  size: 22,
                ),
              )
            else
              Container(
                width: 44,
                height: 44,
                decoration: BoxDecoration(
                  color: context.fieldColor,
                  borderRadius: BorderRadius.circular(14),
                ),
                child: Icon(
                  _getNotificationIcon(notification),
                  color: context.subtitleColor,
                  size: 22,
                ),
              ),
            const SizedBox(width: 14),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Title row with time
                  Row(
                    children: [
                      Expanded(
                        child: Text(
                          notification.title,
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                          style: Theme.of(context).textTheme.titleSmall?.copyWith(
                                fontWeight: isUnread ? FontWeight.w900 : FontWeight.w700,
                                fontSize: 14.5,
                                color: isUnread ? context.titleColor : context.subtitleColor,
                              ),
                        ),
                      ),
                      if (timeAgo != null) ...[
                        const SizedBox(width: 8),
                        Text(
                          timeAgo,
                          style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                color: context.subtitleColor.withValues(alpha: 0.7),
                                fontSize: 11,
                              ),
                        ),
                      ],
                    ],
                  ),
                  const SizedBox(height: 6),
                  // Short message
                  Text(
                    notification.shortDescription.isNotEmpty
                        ? notification.shortDescription
                        : notification.message,
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                    style: Theme.of(context).textTheme.bodySmall?.copyWith(
                          color: context.subtitleColor,
                          height: 1.5,
                          fontWeight: isUnread ? FontWeight.w500 : FontWeight.w400,
                        ),
                  ),
                ],
              ),
            ),
            // Unread dot
            if (isUnread) ...[
              const SizedBox(width: 10),
              Padding(
                padding: const EdgeInsets.only(top: 4),
                child: Container(
                  width: 9,
                  height: 9,
                  decoration: const BoxDecoration(
                    color: VamoTheme.accent,
                    shape: BoxShape.circle,
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }

  IconData _getNotificationIcon(NotificationResponse notification) {
    final title = notification.title.toLowerCase();
    if (title.contains('اشتراك') || title.contains('subscription')) {
      return Icons.card_membership_rounded;
    }
    if (title.contains('مرحبا') || title.contains('welcome') || title.contains('تسجيل')) {
      return Icons.person_add_rounded;
    }
    return Icons.notifications_active_rounded;
  }

  String? _formatTimeAgo(String? dateStr) {
    if (dateStr == null || dateStr.isEmpty) return null;
    try {
      final date = DateTime.parse(dateStr);
      final now = DateTime.now();
      final diff = now.difference(date);

      if (diff.inMinutes < 1) return 'الآن';
      if (diff.inMinutes < 60) return 'منذ ${diff.inMinutes} د';
      if (diff.inHours < 24) return 'منذ ${diff.inHours} س';
      if (diff.inDays < 7) return 'منذ ${diff.inDays} ي';

      // Older than a week — show the date
      return '${date.day}/${date.month}';
    } catch (_) {
      return null;
    }
  }
}

// ── Notification Detail Dialog ──────────────────────────────────────

class _NotificationDetailDialog extends StatelessWidget {
  const _NotificationDetailDialog({required this.notification});

  final NotificationResponse notification;

  @override
  Widget build(BuildContext context) {
    final timeAgo = _formatTimeAgo(notification.createdAt);

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Dialog(
        backgroundColor: context.cardColor,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(28)),
        insetPadding: const EdgeInsets.symmetric(horizontal: 24, vertical: 40),
        child: ConstrainedBox(
          constraints: BoxConstraints(
            maxHeight: MediaQuery.of(context).size.height * 0.7,
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              // ── Header ────────────────────────────────
              Container(
                width: double.infinity,
                padding: const EdgeInsets.fromLTRB(24, 24, 24, 16),
                decoration: BoxDecoration(
                  color: VamoTheme.accent.withValues(alpha: 0.06),
                  borderRadius: const BorderRadius.vertical(top: Radius.circular(28)),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Container(
                          width: 42,
                          height: 42,
                          decoration: BoxDecoration(
                            color: VamoTheme.accent.withValues(alpha: 0.15),
                            borderRadius: BorderRadius.circular(14),
                          ),
                          child: Icon(
                            _getNotificationIcon(notification),
                            color: VamoTheme.accent,
                            size: 22,
                          ),
                        ),
                        const SizedBox(width: 14),
                        Expanded(
                          child: Text(
                            notification.title,
                            style: Theme.of(context).textTheme.titleMedium?.copyWith(
                                  fontWeight: FontWeight.w900,
                                  fontSize: 17,
                                ),
                          ),
                        ),
                      ],
                    ),
                    if (timeAgo != null) ...[
                      const SizedBox(height: 10),
                      Row(
                        children: [
                          Icon(Icons.access_time_rounded,
                              size: 14, color: context.subtitleColor.withValues(alpha: 0.6)),
                          const SizedBox(width: 6),
                          Text(
                            timeAgo,
                            style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                  color: context.subtitleColor.withValues(alpha: 0.7),
                                  fontSize: 12,
                                ),
                          ),
                        ],
                      ),
                    ],
                  ],
                ),
              ),

              // ── Body ──────────────────────────────────
              Flexible(
                child: SingleChildScrollView(
                  padding: const EdgeInsets.fromLTRB(24, 20, 24, 0),
                  child: Text(
                    notification.message,
                    style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                          height: 1.75,
                          fontSize: 15,
                        ),
                  ),
                ),
              ),

              // ── Footer ────────────────────────────────
              Padding(
                padding: const EdgeInsets.fromLTRB(24, 16, 24, 24),
                child: SizedBox(
                  width: double.infinity,
                  child: FilledButton(
                    onPressed: () => Navigator.pop(context),
                    style: FilledButton.styleFrom(
                      backgroundColor: VamoTheme.accent,
                      padding: const EdgeInsets.symmetric(vertical: 14),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16),
                      ),
                    ),
                    child: const Text(
                      'تم',
                      style: TextStyle(fontWeight: FontWeight.w800),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  IconData _getNotificationIcon(NotificationResponse notification) {
    final title = notification.title.toLowerCase();
    if (title.contains('اشتراك') || title.contains('subscription')) {
      return Icons.card_membership_rounded;
    }
    if (title.contains('مرحبا') || title.contains('welcome') || title.contains('تسجيل')) {
      return Icons.person_add_rounded;
    }
    return Icons.notifications_active_rounded;
  }

  String? _formatTimeAgo(String? dateStr) {
    if (dateStr == null || dateStr.isEmpty) return null;
    try {
      final date = DateTime.parse(dateStr);
      final now = DateTime.now();
      final diff = now.difference(date);

      if (diff.inMinutes < 1) return 'الآن';
      if (diff.inMinutes < 60) return 'منذ ${diff.inMinutes} د';
      if (diff.inHours < 24) return 'منذ ${diff.inHours} س';
      if (diff.inDays < 7) return 'منذ ${diff.inDays} ي';
      return '${date.day}/${date.month}';
    } catch (_) {
      return null;
    }
  }
}
