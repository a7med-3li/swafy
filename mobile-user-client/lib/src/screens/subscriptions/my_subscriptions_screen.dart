import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../data/models/subscription_response.dart';
import '../../providers/subscription_provider.dart';
import '../../theme/theme.dart';

/// Subscriptions management screen with Active / Pending / All tabs.
///
/// Categorizes the user's subscriptions by status and displays
/// them in a clean, tabbed interface.
class MySubscriptionsScreen extends StatefulWidget {
  const MySubscriptionsScreen({super.key});

  @override
  State<MySubscriptionsScreen> createState() => _MySubscriptionsScreenState();
}

class _MySubscriptionsScreenState extends State<MySubscriptionsScreen>
    with SingleTickerProviderStateMixin {
  late final TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 3, vsync: this);
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<SubscriptionProvider>().loadHistory();
    });
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final subProvider = context.watch<SubscriptionProvider>();

    final activeList = subProvider.history
        .where((s) => s.status == SubscriptionStatus.active)
        .toList();
    final pendingList = subProvider.history
        .where((s) => s.status == SubscriptionStatus.pending)
        .toList();
    final allList = subProvider.history;

    return SafeArea(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // ── Header ──────────────────────────────────────
          Padding(
            padding: const EdgeInsets.fromLTRB(24, 24, 24, 0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'اشتراكاتي',
                  style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                        fontWeight: FontWeight.w900,
                      ),
                ),
                const SizedBox(height: 6),
                Text(
                  'إدارة ومتابعة اشتراكاتك الحالية والسابقة',
                  style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                        color: context.subtitleColor,
                      ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 20),

          // ── Tab Bar ──────────────────────────────────────
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 24),
            child: Container(
              padding: const EdgeInsets.all(4),
              decoration: BoxDecoration(
                color: context.fieldColor,
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: context.cardBorderColor),
              ),
              child: TabBar(
                controller: _tabController,
                indicator: BoxDecoration(
                  color: VamoTheme.primary,
                  borderRadius: BorderRadius.circular(12),
                  boxShadow: [
                    BoxShadow(
                      color: VamoTheme.primary.withValues(alpha: 0.3),
                      blurRadius: 8,
                      offset: const Offset(0, 2),
                    ),
                  ],
                ),
                indicatorSize: TabBarIndicatorSize.tab,
                dividerColor: Colors.transparent,
                labelColor: Colors.white,
                unselectedLabelColor: context.subtitleColor,
                labelStyle: const TextStyle(
                  fontWeight: FontWeight.w800,
                  fontSize: 13,
                ),
                unselectedLabelStyle: const TextStyle(
                  fontWeight: FontWeight.w600,
                  fontSize: 13,
                ),
                tabs: [
                  Tab(text: 'نشطة (${activeList.length})'),
                  Tab(text: 'قيد التفعيل (${pendingList.length})'),
                  Tab(text: 'الكل (${allList.length})'),
                ],
              ),
            ),
          ),
          const SizedBox(height: 16),

          // ── Tab Content ─────────────────────────────────
          Expanded(
            child: subProvider.isLoading
                ? Center(
                    child: CircularProgressIndicator(
                      color: VamoTheme.accent,
                      strokeWidth: 3,
                    ),
                  )
                : TabBarView(
                    controller: _tabController,
                    children: [
                      _SubscriptionList(
                        subscriptions: activeList,
                        emptyIcon: Icons.check_circle_outline_rounded,
                        emptyTitle: 'لا توجد اشتراكات نشطة',
                        emptySubtitle: 'اشترك في مسار لبدء رحلاتك اليومية',
                        onRefresh: () => subProvider.forceRefresh(),
                      ),
                      _SubscriptionList(
                        subscriptions: pendingList,
                        emptyIcon: Icons.hourglass_empty_rounded,
                        emptyTitle: 'لا توجد اشتراكات قيد التفعيل',
                        emptySubtitle: 'جميع اشتراكاتك مفعلة حالياً',
                        onRefresh: () => subProvider.forceRefresh(),
                      ),
                      _SubscriptionList(
                        subscriptions: allList,
                        emptyIcon: Icons.inbox_rounded,
                        emptyTitle: 'لا يوجد سجل اشتراكات',
                        emptySubtitle: 'ستظهر هنا جميع اشتراكاتك بعد التسجيل',
                        onRefresh: () => subProvider.forceRefresh(),
                      ),
                    ],
                  ),
          ),
        ],
      ),
    );
  }
}

/// Reusable subscription list with pull-to-refresh and empty state.
class _SubscriptionList extends StatelessWidget {
  const _SubscriptionList({
    required this.subscriptions,
    required this.emptyIcon,
    required this.emptyTitle,
    required this.emptySubtitle,
    required this.onRefresh,
  });

  final List<SubscriptionResponse> subscriptions;
  final IconData emptyIcon;
  final String emptyTitle;
  final String emptySubtitle;
  final Future<void> Function() onRefresh;

  @override
  Widget build(BuildContext context) {
    if (subscriptions.isEmpty) {
      return _buildEmptyState(context);
    }

    return RefreshIndicator(
      onRefresh: onRefresh,
      color: VamoTheme.accent,
      child: ListView.builder(
        padding: const EdgeInsets.fromLTRB(24, 0, 24, 24),
        physics: const AlwaysScrollableScrollPhysics(
          parent: BouncingScrollPhysics(),
        ),
        itemCount: subscriptions.length,
        itemBuilder: (context, index) {
          final sub = subscriptions[index];
          return _buildSubscriptionCard(context, sub);
        },
      ),
    );
  }

  Widget _buildSubscriptionCard(BuildContext context, SubscriptionResponse sub) {
    final statusColor = _getStatusColor(sub.status);
    final statusBgColor = statusColor.withValues(alpha: 0.15);

    return Container(
      margin: const EdgeInsets.only(bottom: 14),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: context.cardColor,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: context.cardBorderColor),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Top row: icon + price + status badge
          Row(
            children: [
              Container(
                width: 50,
                height: 50,
                decoration: BoxDecoration(
                  color: statusBgColor,
                  borderRadius: BorderRadius.circular(16),
                ),
                child: Icon(
                  _getStatusIcon(sub.status),
                  color: statusColor,
                  size: 24,
                ),
              ),
              const SizedBox(width: 14),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      '${sub.price.toStringAsFixed(0)} ج.م / شهرياً',
                      style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                            fontWeight: FontWeight.w800,
                            fontSize: 16,
                          ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      'رقم الاشتراك: #${sub.id}',
                      style: Theme.of(context).textTheme.bodySmall?.copyWith(
                            color: context.subtitleColor,
                          ),
                    ),
                  ],
                ),
              ),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                decoration: BoxDecoration(
                  color: statusBgColor,
                  borderRadius: BorderRadius.circular(10),
                  border: Border.all(
                    color: statusColor.withValues(alpha: 0.3),
                  ),
                ),
                child: Text(
                  sub.status.label,
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w800,
                    color: statusColor,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),

          // Date range
          Container(
            padding: const EdgeInsets.all(14),
            decoration: BoxDecoration(
              color: context.fieldColor,
              borderRadius: BorderRadius.circular(14),
            ),
            child: Row(
              children: [
                _dateItem(
                  context,
                  icon: Icons.play_arrow_rounded,
                  label: 'البداية',
                  value: sub.startDate,
                ),
                Container(
                  width: 1,
                  height: 36,
                  margin: const EdgeInsets.symmetric(horizontal: 16),
                  color: context.cardBorderColor,
                ),
                _dateItem(
                  context,
                  icon: Icons.stop_rounded,
                  label: 'الانتهاء',
                  value: sub.endDate,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _dateItem(
    BuildContext context, {
    required IconData icon,
    required String label,
    required String value,
  }) {
    return Expanded(
      child: Row(
        children: [
          Icon(icon, color: context.subtitleColor, size: 18),
          const SizedBox(width: 8),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                label,
                style: Theme.of(context).textTheme.bodySmall?.copyWith(
                      color: context.subtitleColor,
                      fontSize: 11,
                    ),
              ),
              const SizedBox(height: 2),
              Text(
                value.isNotEmpty ? value : '-',
                style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                      fontWeight: FontWeight.w700,
                      fontSize: 13,
                    ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Color _getStatusColor(SubscriptionStatus status) {
    switch (status) {
      case SubscriptionStatus.active:
        return const Color(0xFF22C55E); // green
      case SubscriptionStatus.pending:
        return const Color(0xFFFBBF24); // amber
      case SubscriptionStatus.expired:
        return const Color(0xFF94A3B8); // slate
      case SubscriptionStatus.cancelled:
        return const Color(0xFFEF4444); // red
      case SubscriptionStatus.suspended:
        return const Color(0xFFF97316); // orange
    }
  }

  IconData _getStatusIcon(SubscriptionStatus status) {
    switch (status) {
      case SubscriptionStatus.active:
        return Icons.check_circle_rounded;
      case SubscriptionStatus.pending:
        return Icons.hourglass_top_rounded;
      case SubscriptionStatus.expired:
        return Icons.timer_off_rounded;
      case SubscriptionStatus.cancelled:
        return Icons.cancel_rounded;
      case SubscriptionStatus.suspended:
        return Icons.pause_circle_rounded;
    }
  }

  Widget _buildEmptyState(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(32),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 80,
              height: 80,
              decoration: BoxDecoration(
                color: context.cardColor,
                borderRadius: BorderRadius.circular(24),
                border: Border.all(color: context.cardBorderColor),
              ),
              child: Icon(
                emptyIcon,
                color: context.subtitleColor,
                size: 38,
              ),
            ),
            const SizedBox(height: 20),
            Text(
              emptyTitle,
              style: Theme.of(context).textTheme.titleMedium?.copyWith(
                    fontWeight: FontWeight.w800,
                  ),
            ),
            const SizedBox(height: 8),
            Text(
              emptySubtitle,
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                    color: context.subtitleColor,
                  ),
            ),
          ],
        ),
      ),
    );
  }
}
