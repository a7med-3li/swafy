import 'package:flutter/material.dart';
import '../theme/theme.dart';

/// Card displaying the passenger's active trip for today ("رحلتك اليومية اليوم").
///
/// Shows simulated or live driver info, bus license plate, and estimated ETA
/// on the active corridor.
class TodayRideCard extends StatelessWidget {
  final String corridorName;
  final String driverName;
  final String vehiclePlate;
  final int etaMinutes;
  final VoidCallback? onTrackPressed;

  const TodayRideCard({
    super.key,
    required this.corridorName,
    this.driverName = 'كابتن محمود حسن',
    this.vehiclePlate = 'أ ب ج ١٢٣٤',
    this.etaMinutes = 7,
    this.onTrackPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(22),
      decoration: BoxDecoration(
        color: context.cardColor,
        borderRadius: BorderRadius.circular(24),
        border: Border.all(color: context.cardBorderColor),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.1),
            blurRadius: 16,
            offset: const Offset(0, 6),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Header Row: Badge & ETA
          Row(
            children: [
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                decoration: BoxDecoration(
                  color: const Color(0xFF0D3D22),
                  borderRadius: BorderRadius.circular(10),
                ),
                child: const Row(
                  children: [
                    Icon(Icons.directions_bus_filled_rounded,
                        color: VamoTheme.accent, size: 16),
                    SizedBox(width: 6),
                    Text(
                      'رحلتك اليومية الآن',
                      style: TextStyle(
                        color: VamoTheme.accent,
                        fontSize: 12,
                        fontWeight: FontWeight.w800,
                      ),
                    ),
                  ],
                ),
              ),
              const Spacer(),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                decoration: BoxDecoration(
                  color: context.fieldColor,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Text(
                  'يصل خلال $etaMinutes دقائق',
                  style: TextStyle(
                    color: context.titleColor,
                    fontSize: 12,
                    fontWeight: FontWeight.w700,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),

          // Corridor Name
          Text(
            corridorName,
            style: Theme.of(context).textTheme.titleMedium?.copyWith(
                  fontWeight: FontWeight.w900,
                  fontSize: 16,
                ),
          ),
          const SizedBox(height: 14),

          // Driver & Bus details row
          Container(
            padding: const EdgeInsets.all(14),
            decoration: BoxDecoration(
              color: context.cardElevatedColor,
              borderRadius: BorderRadius.circular(16),
              border: Border.all(color: context.cardBorderColor),
            ),
            child: Row(
              children: [
                Container(
                  width: 44,
                  height: 44,
                  decoration: BoxDecoration(
                    color: VamoTheme.accent.withValues(alpha: 0.15),
                    borderRadius: BorderRadius.circular(14),
                  ),
                  child: const Icon(
                    Icons.person_pin_rounded,
                    color: VamoTheme.accent,
                    size: 24,
                  ),
                ),
                const SizedBox(width: 14),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        driverName,
                        style: TextStyle(
                          color: context.titleColor,
                          fontWeight: FontWeight.w800,
                          fontSize: 14,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        'رقم اللوحة: $vehiclePlate',
                        style: TextStyle(
                          color: context.subtitleColor,
                          fontSize: 12,
                        ),
                      ),
                    ],
                  ),
                ),
                Container(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 12,
                    vertical: 8,
                  ),
                  decoration: BoxDecoration(
                    color: context.fieldColor,
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: const Row(
                    children: [
                      Icon(Icons.radar_rounded, color: VamoTheme.accent, size: 16),
                      SizedBox(width: 6),
                      Text(
                        'تتبع مباشر',
                        style: TextStyle(
                          color: VamoTheme.accent,
                          fontSize: 12,
                          fontWeight: FontWeight.w800,
                        ),
                      ),
                    ],
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
