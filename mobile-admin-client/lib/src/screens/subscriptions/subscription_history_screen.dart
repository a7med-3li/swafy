import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/subscription_provider.dart';
import '../../theme/theme.dart';

/// Screen displaying the user's subscription history in a dedicated page.
class SubscriptionHistoryScreen extends StatefulWidget {
  const SubscriptionHistoryScreen({super.key});

  @override
  State<SubscriptionHistoryScreen> createState() => _SubscriptionHistoryScreenState();
}

class _SubscriptionHistoryScreenState extends State<SubscriptionHistoryScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<SubscriptionProvider>().loadHistory();
    });
  }

  @override
  Widget build(BuildContext context) {
    final subProvider = context.watch<SubscriptionProvider>();

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: VamoTheme.background,
        appBar: AppBar(
          title: const Text('سجل الاشتراكات'),
          backgroundColor: Colors.transparent,
          elevation: 0,
        ),
        body: SafeArea(
          child: subProvider.isLoading
              ? const Center(
                  child: CircularProgressIndicator(
                    color: Color(0xFF4ADE80),
                    strokeWidth: 3,
                  ),
                )
              : subProvider.history.isEmpty
                  ? _buildEmptyState(context)
                  : ListView.builder(
                      padding: const EdgeInsets.all(24),
                      physics: const BouncingScrollPhysics(),
                      itemCount: subProvider.history.length,
                      itemBuilder: (context, index) {
                        final sub = subProvider.history[index];
                        return Container(
                          margin: const EdgeInsets.only(bottom: 14),
                          padding: const EdgeInsets.all(18),
                          decoration: BoxDecoration(
                            color: VamoTheme.card,
                            borderRadius: BorderRadius.circular(18),
                            border: Border.all(color: const Color(0xFF2A2A2A)),
                          ),
                          child: Row(
                            children: [
                              Container(
                                width: 48,
                                height: 48,
                                decoration: BoxDecoration(
                                  color: VamoTheme.primary.withValues(alpha: 0.2),
                                  borderRadius: BorderRadius.circular(16),
                                ),
                                child: const Icon(
                                  Icons.card_membership_rounded,
                                  color: Color(0xFF4ADE80),
                                  size: 24,
                                ),
                              ),
                              const SizedBox(width: 16),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      '${sub.price.toStringAsFixed(0)} ج.م',
                                      style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                                            fontWeight: FontWeight.w800,
                                            fontSize: 16,
                                          ),
                                    ),
                                    const SizedBox(height: 6),
                                    Text(
                                      '${sub.startDate} — ${sub.endDate}',
                                      style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                            color: VamoTheme.subtitle,
                                          ),
                                    ),
                                  ],
                                ),
                              ),
                              Container(
                                padding: const EdgeInsets.symmetric(
                                  horizontal: 12,
                                  vertical: 6,
                                ),
                                decoration: BoxDecoration(
                                  color: const Color(0xFF1A1A1A),
                                  borderRadius: BorderRadius.circular(10),
                                ),
                                child: Text(
                                  sub.status.label,
                                  style: const TextStyle(
                                    fontSize: 12,
                                    fontWeight: FontWeight.w700,
                                    color: Color(0xFF9E9E9E),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        );
                      },
                    ),
        ),
      ),
    );
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
                color: VamoTheme.card,
                borderRadius: BorderRadius.circular(24),
              ),
              child: const Icon(
                Icons.history_rounded,
                color: VamoTheme.subtitle,
                size: 38,
              ),
            ),
            const SizedBox(height: 20),
            Text(
              'لا يوجد سجل اشتراكات بعد',
              style: Theme.of(context).textTheme.titleMedium?.copyWith(
                    fontWeight: FontWeight.w800,
                  ),
            ),
            const SizedBox(height: 8),
            Text(
              'ستظهر هنا جميع اشتراكاتك السابقة والحالية.',
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                    color: VamoTheme.subtitle,
                  ),
            ),
          ],
        ),
      ),
    );
  }
}
