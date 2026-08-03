import 'package:flutter/material.dart';
import '../../theme/theme.dart';

/// Screen displaying passenger service announcements, bus delay alerts,
/// and subscription renewal notifications.
class NotificationsScreen extends StatelessWidget {
  const NotificationsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    // Simulated list of notifications for the passenger client
    final notifications = [
      _NotificationItem(
        title: 'تأخير طفيف في مسارك الصباحي',
        body: 'توجد كثافات مرورية على طريق النصر، متوقع تأخر الباص ٥ دقائق عن الموعد المعتاد.',
        time: 'منذ ١٠ دقائق',
        type: _NotificationType.delay,
        isUnread: true,
      ),
      _NotificationItem(
        title: 'تم تأكيد اشتراكك بنجاح',
        body: 'تم تفعيل اشتراكك الشهري في مسار "التجمع الخامس - المهندسين". رحلة سعيدة مع Vamo!',
        time: 'منذ يومين',
        type: _NotificationType.success,
        isUnread: false,
      ),
      _NotificationItem(
        title: 'تحديث أمني في خدمة Vamo',
        body: 'يمكنك الآن استخدام كود الـ QR الإلكتروني من شاشة الرئيسية لركوب الباص بسهولة وأمان.',
        time: 'منذ ٣ أيام',
        type: _NotificationType.info,
        isUnread: false,
      ),
    ];

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: VamoTheme.background,
        appBar: AppBar(
          title: const Text('الإشعارات والتنبيهات'),
          backgroundColor: Colors.transparent,
          elevation: 0,
        ),
        body: SafeArea(
          child: ListView.builder(
            padding: const EdgeInsets.all(24),
            physics: const BouncingScrollPhysics(),
            itemCount: notifications.length,
            itemBuilder: (context, index) {
              final item = notifications[index];
              return _buildNotificationCard(context, item);
            },
          ),
        ),
      ),
    );
  }

  Widget _buildNotificationCard(BuildContext context, _NotificationItem item) {
    Color badgeColor;
    IconData icon;

    switch (item.type) {
      case _NotificationType.delay:
        badgeColor = VamoTheme.alert;
        icon = Icons.warning_amber_rounded;
        break;
      case _NotificationType.success:
        badgeColor = VamoTheme.success;
        icon = Icons.check_circle_outline_rounded;
        break;
      case _NotificationType.info:
        badgeColor = const Color(0xFF60A5FA);
        icon = Icons.info_outline_rounded;
        break;
    }

    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: VamoTheme.card,
        borderRadius: BorderRadius.circular(22),
        border: Border.all(
          color: item.isUnread ? badgeColor.withValues(alpha: 0.5) : VamoTheme.cardBorder,
          width: item.isUnread ? 1.5 : 1,
        ),
        boxShadow: item.isUnread
            ? [
                BoxShadow(
                  color: badgeColor.withValues(alpha: 0.1),
                  blurRadius: 16,
                  offset: const Offset(0, 4),
                ),
              ]
            : [],
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: badgeColor.withValues(alpha: 0.15),
              borderRadius: BorderRadius.circular(16),
            ),
            child: Icon(icon, color: badgeColor, size: 24),
          ),
          const SizedBox(width: 16),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Expanded(
                      child: Text(
                        item.title,
                        style: Theme.of(context).textTheme.titleMedium?.copyWith(
                              fontWeight: FontWeight.w800,
                              fontSize: 15,
                            ),
                      ),
                    ),
                    if (item.isUnread)
                      Container(
                        width: 8,
                        height: 8,
                        decoration: BoxDecoration(
                          color: badgeColor,
                          shape: BoxShape.circle,
                        ),
                      ),
                  ],
                ),
                const SizedBox(height: 6),
                Text(
                  item.body,
                  style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                        color: VamoTheme.subtitle,
                        height: 1.5,
                      ),
                ),
                const SizedBox(height: 10),
                Text(
                  item.time,
                  style: TextStyle(
                    color: VamoTheme.subtitle.withValues(alpha: 0.7),
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

enum _NotificationType { delay, success, info }

class _NotificationItem {
  final String title;
  final String body;
  final String time;
  final _NotificationType type;
  final bool isUnread;

  _NotificationItem({
    required this.title,
    required this.body,
    required this.time,
    required this.type,
    required this.isUnread,
  });
}
