import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../data/models/corridor_response.dart';
import '../../providers/subscription_provider.dart';
import '../../providers/corridor_provider.dart';
import '../../theme/theme.dart';
import '../../widgets/error_banner.dart';
import '../../widgets/vamo_button.dart';
import '../home/home_screen.dart';

/// Detail view for a single corridor with stop list and subscribe action.
class CorridorDetailScreen extends StatelessWidget {
  const CorridorDetailScreen({super.key, required this.corridor});

  final CorridorResponse corridor;

  @override
  Widget build(BuildContext context) {
    final subProvider = context.watch<SubscriptionProvider>();

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: context.bg,
        appBar: AppBar(
          title: Text(corridor.name),
          backgroundColor: Colors.transparent,
        ),
        body: SafeArea(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(24),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                // Header card
                Container(
                  padding: const EdgeInsets.all(22),
                  decoration: BoxDecoration(
                    gradient: const LinearGradient(
                      colors: [Color(0xFF05472A), Color(0xFF081A19)],
                      begin: Alignment.topLeft,
                      end: Alignment.bottomRight,
                    ),
                    borderRadius: BorderRadius.circular(24),
                    border: Border.all(color: const Color(0xFF166534).withValues(alpha: 0.4)),
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        children: [
                          Container(
                            width: 56, height: 56,
                            decoration: BoxDecoration(
                              color: const Color(0xFF166534),
                              borderRadius: BorderRadius.circular(18),
                            ),
                            child: const Icon(Icons.route_rounded, color: Color(0xFF4ADE80), size: 28),
                          ),
                          const SizedBox(width: 16),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(corridor.name, style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.w900, color: Colors.white)),
                                const SizedBox(height: 4),
                                Text('${corridor.stops.length} محطة', style: Theme.of(context).textTheme.bodyMedium?.copyWith(color: const Color(0xFF9E9E9E))),
                              ],
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 20),
                      Container(
                        padding: const EdgeInsets.all(16),
                        decoration: BoxDecoration(color: const Color(0xFF0A1A14), borderRadius: BorderRadius.circular(16)),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            const Icon(Icons.payments_outlined, color: Color(0xFF4ADE80), size: 22),
                            const SizedBox(width: 10),
                            Text('${corridor.price.toStringAsFixed(0)} ج.م / شهرياً', style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w800, color: const Color(0xFF4ADE80))),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 24),

                // Stops
                Text('المحطات', style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w800)),
                const SizedBox(height: 16),
                if (corridor.stops.isEmpty)
                  Container(
                    padding: const EdgeInsets.all(20),
                    decoration: BoxDecoration(color: context.cardColor, borderRadius: BorderRadius.circular(16)),
                    child: Text('لا توجد محطات مسجلة حالياً.', style: Theme.of(context).textTheme.bodyMedium?.copyWith(color: context.subtitleColor), textAlign: TextAlign.center),
                  )
                else
                  ...List.generate(corridor.stops.length, (i) {
                    final stop = corridor.stops[i];
                    final isLast = i == corridor.stops.length - 1;
                    return Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Column(
                          children: [
                            Container(
                              width: 32, height: 32,
                              decoration: BoxDecoration(
                                color: i == 0 ? const Color(0xFF4ADE80) : isLast ? const Color(0xFFF87171) : VamoTheme.primary,
                                shape: BoxShape.circle,
                              ),
                              child: Center(child: Text('${i + 1}', style: const TextStyle(color: Colors.white, fontWeight: FontWeight.w700, fontSize: 13))),
                            ),
                            if (!isLast) Container(width: 2, height: 40, color: context.cardBorderColor),
                          ],
                        ),
                        const SizedBox(width: 14),
                        Expanded(
                          child: Container(
                            margin: const EdgeInsets.only(bottom: 12),
                            padding: const EdgeInsets.all(16),
                            decoration: BoxDecoration(color: context.cardColor, borderRadius: BorderRadius.circular(16)),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(stop.name, style: Theme.of(context).textTheme.bodyLarge?.copyWith(fontWeight: FontWeight.w700)),
                                if (stop.latitude != 0.0) ...[
                                  const SizedBox(height: 4),
                                  Text('${stop.latitude.toStringAsFixed(4)}, ${stop.longitude.toStringAsFixed(4)}', style: Theme.of(context).textTheme.bodySmall?.copyWith(color: context.subtitleColor)),
                                ],
                              ],
                            ),
                          ),
                        ),
                      ],
                    );
                  }),

                const SizedBox(height: 24),

                // Error messages
                ErrorBanner(message: subProvider.error, onDismiss: subProvider.clearMessages),
                if (subProvider.error != null) const SizedBox(height: 12),

                // Subscribe
                VamoButton(
                  label: 'اشتراك في هذا المسار',
                  icon: Icons.card_membership_rounded,
                  isLoading: subProvider.isLoading,
                  onPressed: () => _confirmSubscribe(context),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void _confirmSubscribe(BuildContext context) {
    showDialog(
      context: context,
      builder: (ctx) => Directionality(
        textDirection: TextDirection.rtl,
        child: AlertDialog(
          backgroundColor: context.cardColor,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
          title: const Text('تأكيد الاشتراك', style: TextStyle(fontWeight: FontWeight.w800)),
          content: Text('هل تريد الاشتراك في مسار "${corridor.name}" بسعر ${corridor.price.toStringAsFixed(0)} ج.م؟'),
          actions: [
            TextButton(onPressed: () => Navigator.pop(ctx), child: Text('إلغاء', style: TextStyle(color: context.subtitleColor))),
            FilledButton(
              onPressed: () async {
                Navigator.pop(ctx);
                final subProvider = context.read<SubscriptionProvider>();
                final result = await subProvider.purchase(corridor);
                if (result != null && context.mounted) {
                  _showRequestPlacedDialog(context);
                }
              },
              style: FilledButton.styleFrom(backgroundColor: VamoTheme.primary),
              child: const Text('تأكيد'),
            ),
          ],
        ),
      ),
    );
  }

  void _showRequestPlacedDialog(BuildContext context) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) => Directionality(
        textDirection: TextDirection.rtl,
        child: AlertDialog(
          backgroundColor: context.cardColor,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
          title: Row(
            children: [
              Container(
                width: 40,
                height: 40,
                decoration: BoxDecoration(
                  color: const Color(0xFF05472A).withValues(alpha: 0.15),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: const Icon(Icons.check_circle_rounded, color: Color(0xFF22C55E), size: 24),
              ),
              const SizedBox(width: 12),
              const Expanded(
                child: Text('تم تقديم الطلب', style: TextStyle(fontWeight: FontWeight.w800)),
              ),
            ],
          ),
          content: const Text(
            'تم تقديم طلب اشتراكك بنجاح! سيتم مراجعة الطلب من قبل الإدارة وتفعيله قريباً.',
            style: TextStyle(height: 1.6),
          ),
          actions: [
            FilledButton(
              onPressed: () async {
                Navigator.pop(ctx);
                if (!context.mounted) return;

                // Refresh all data from the backend
                final subProvider = context.read<SubscriptionProvider>();
                final corridorProvider = context.read<CorridorProvider>();
                await Future.wait([
                  subProvider.forceRefresh(),
                  corridorProvider.forceRefresh(),
                ]);

                if (!context.mounted) return;

                // Find HomeScreenState before popping so we can switch tabs
                final homeState = context.findAncestorStateOfType<HomeScreenState>();
                Navigator.of(context).popUntil((route) => route.isFirst);
                homeState?.switchTab(0);
              },
              style: FilledButton.styleFrom(backgroundColor: VamoTheme.primary),
              child: const Text('موافق'),
            ),
          ],
        ),
      ),
    );
  }
}
