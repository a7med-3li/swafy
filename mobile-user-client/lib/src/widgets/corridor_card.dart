import 'package:flutter/material.dart';

import '../data/models/corridor_response.dart';
import '../theme/theme.dart';

/// Card displaying a corridor's name, price, and stop count.
///
/// Tapping the card triggers [onTap].
class CorridorCard extends StatelessWidget {
  const CorridorCard({
    super.key,
    required this.corridor,
    required this.onTap,
    this.isSelected = false,
  });

  final CorridorResponse corridor;
  final VoidCallback onTap;
  final bool isSelected;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 250),
        curve: Curves.easeInOut,
        padding: const EdgeInsets.all(20),
        decoration: BoxDecoration(
          color: isSelected
              ? VamoTheme.primary.withValues(alpha: 0.15)
              : context.cardColor,
          borderRadius: BorderRadius.circular(20),
          border: Border.all(
            color: isSelected ? VamoTheme.primary : context.cardBorderColor,
            width: isSelected ? 1.5 : 1,
          ),
        ),
        child: Row(
          children: [
            // ── Route icon ─────────────────────────────
            Container(
              width: 52,
              height: 52,
              decoration: BoxDecoration(
                color: VamoTheme.primary.withValues(alpha: 0.2),
                borderRadius: BorderRadius.circular(16),
              ),
              child: const Icon(
                Icons.route_rounded,
                color: Color(0xFF4ADE80),
                size: 26,
              ),
            ),
            const SizedBox(width: 16),

            // ── Info ────────────────────────────────────
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    corridor.name,
                    style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                          fontWeight: FontWeight.w800,
                          fontSize: 16,
                        ),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 6),
                  Row(
                    children: [
                      _buildChip(
                        context,
                        icon: Icons.place_outlined,
                        text: '${corridor.stops.length} محطة',
                      ),
                      const SizedBox(width: 12),
                      _buildChip(
                        context,
                        icon: Icons.payments_outlined,
                        text: '${corridor.price.toStringAsFixed(0)} ج.م',
                      ),
                    ],
                  ),
                ],
              ),
            ),

            // ── Arrow ───────────────────────────────────
            Icon(
              Icons.chevron_left_rounded,
              color: context.subtitleColor,
              size: 28,
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildChip(BuildContext context, {required IconData icon, required String text}) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Icon(icon, size: 14, color: context.subtitleColor),
        const SizedBox(width: 4),
        Text(
          text,
          style: Theme.of(context).textTheme.bodySmall?.copyWith(
                color: context.subtitleColor,
                fontWeight: FontWeight.w500,
              ),
        ),
      ],
    );
  }
}
