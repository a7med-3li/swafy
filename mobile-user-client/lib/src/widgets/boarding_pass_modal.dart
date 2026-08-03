import 'dart:async';
import 'package:flutter/material.dart';
import '../data/models/subscription_response.dart';
import '../theme/theme.dart';

/// Modal dialog displaying the passenger's Digital Boarding Pass QR Code.
///
/// Includes an animated security refresh timer and trip verification details
/// for the driver to scan upon boarding.
class BoardingPassModal extends StatefulWidget {
  final SubscriptionResponse subscription;
  final String passengerName;

  const BoardingPassModal({
    super.key,
    required this.subscription,
    required this.passengerName,
  });

  static void show(
    BuildContext context, {
    required SubscriptionResponse subscription,
    required String passengerName,
  }) {
    showModalBottomSheet(
      context: context,
      backgroundColor: Colors.transparent,
      isScrollControlled: true,
      builder: (_) => BoardingPassModal(
        subscription: subscription,
        passengerName: passengerName,
      ),
    );
  }

  @override
  State<BoardingPassModal> createState() => _BoardingPassModalState();
}

class _BoardingPassModalState extends State<BoardingPassModal> {
  int _secondsRemaining = 60;
  Timer? _timer;

  @override
  void initState() {
    super.initState();
    _startTimer();
  }

  void _startTimer() {
    _timer?.cancel();
    _secondsRemaining = 60;
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (!mounted) {
        timer.cancel();
        return;
      }
      if (_secondsRemaining > 0) {
        setState(() => _secondsRemaining--);
      } else {
        // Reset timer to simulate rotating security token
        setState(() => _secondsRemaining = 60);
      }
    });
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Container(
        margin: const EdgeInsets.all(16),
        padding: const EdgeInsets.all(28),
        decoration: BoxDecoration(
          color: VamoTheme.card,
          borderRadius: BorderRadius.circular(28),
          border: Border.all(color: VamoTheme.cardBorder, width: 1.5),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.6),
              blurRadius: 30,
              offset: const Offset(0, 10),
            ),
          ],
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            // Modal handle bar
            Container(
              width: 48,
              height: 5,
              decoration: BoxDecoration(
                color: const Color(0xFF373737),
                borderRadius: BorderRadius.circular(10),
              ),
            ),
            const SizedBox(height: 20),

            // Header badge
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
              decoration: BoxDecoration(
                color: VamoTheme.accentDark,
                borderRadius: BorderRadius.circular(12),
              ),
              child: const Text(
                'تذكرة الصعود الإلكترونية 🎟️',
                style: TextStyle(
                  color: VamoTheme.accent,
                  fontWeight: FontWeight.w800,
                  fontSize: 13,
                ),
              ),
            ),
            const SizedBox(height: 18),

            // Passenger Name & Corridor
            Text(
              widget.passengerName,
              style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                    fontWeight: FontWeight.w900,
                  ),
            ),
            const SizedBox(height: 4),
            Text(
              'اشتراك فعال حتى: ${widget.subscription.endDate}',
              style: Theme.of(context).textTheme.bodySmall?.copyWith(
                    color: VamoTheme.subtitle,
                  ),
            ),
            const SizedBox(height: 24),

            // Simulated High-Contrast QR Code Card
            Container(
              padding: const EdgeInsets.all(22),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(24),
                boxShadow: [
                  BoxShadow(
                    color: VamoTheme.accent.withValues(alpha: 0.2),
                    blurRadius: 20,
                    offset: const Offset(0, 6),
                  ),
                ],
              ),
              child: Column(
                children: [
                  const Icon(
                    Icons.qr_code_2_rounded,
                    color: Colors.black,
                    size: 190,
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'PASS-${widget.subscription.id}-${DateTime.now().minute}',
                    style: const TextStyle(
                      color: Color(0xFF333333),
                      fontWeight: FontWeight.w800,
                      letterSpacing: 2,
                      fontSize: 14,
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 24),

            // Security countdown timer
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Icon(
                  Icons.verified_user_outlined,
                  color: VamoTheme.accent,
                  size: 20,
                ),
                const SizedBox(width: 8),
                Text(
                  'يتغير الرمز تلقائياً بعد: $_secondsRemaining ثانية',
                  style: const TextStyle(
                    color: VamoTheme.accent,
                    fontWeight: FontWeight.w700,
                    fontSize: 13,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 20),

            // Close button
            SizedBox(
              width: double.infinity,
              child: OutlinedButton(
                onPressed: () => Navigator.pop(context),
                style: OutlinedButton.styleFrom(
                  side: const BorderSide(color: VamoTheme.cardBorder),
                  padding: const EdgeInsets.symmetric(vertical: 14),
                ),
                child: const Text(
                  'إغلاق',
                  style: TextStyle(fontWeight: FontWeight.w800),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
