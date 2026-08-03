import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/auth_provider.dart';
import '../../providers/subscription_provider.dart';
import '../../theme/theme.dart';
import '../auth/login_screen.dart';

/// User profile screen showing info, subscription history, and logout.
class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<SubscriptionProvider>().loadHistory();
    });
  }

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();
    final subProvider = context.watch<SubscriptionProvider>();
    final user = auth.user;

    return SafeArea(
      child: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        physics: const BouncingScrollPhysics(),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text('حسابي', style: Theme.of(context).textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.w900)),
            const SizedBox(height: 24),

            // Profile card
            Container(
              padding: const EdgeInsets.all(22),
              decoration: BoxDecoration(color: VamoTheme.card, borderRadius: BorderRadius.circular(24), border: Border.all(color: const Color(0xFF2A2A2A))),
              child: Row(
                children: [
                  Container(
                    width: 60, height: 60,
                    decoration: BoxDecoration(gradient: const LinearGradient(colors: [Color(0xFF05472A), Color(0xFF0A6B3E)]), borderRadius: BorderRadius.circular(20)),
                    child: Center(child: Text(auth.displayName.isNotEmpty ? auth.displayName[0].toUpperCase() : 'V', style: const TextStyle(color: Colors.white, fontWeight: FontWeight.w900, fontSize: 24))),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(auth.displayName, style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w800)),
                        if (user != null) ...[
                          const SizedBox(height: 4),
                          Text(user.phoneNumber, style: Theme.of(context).textTheme.bodySmall?.copyWith(color: VamoTheme.subtitle)),
                        ],
                      ],
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),

            // User details
            if (user != null) ...[
              _infoTile(context, Icons.person_outline, 'الجنس', user.genderLabel),
              _infoTile(context, Icons.badge_outlined, 'الدور', user.roleLabel),
              if (user.email != null && user.email!.isNotEmpty) _infoTile(context, Icons.email_outlined, 'البريد', user.email!),
            ],
            const SizedBox(height: 28),

            // Subscription history
            Text('سجل الاشتراكات', style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w800)),
            const SizedBox(height: 16),
            if (subProvider.isLoading)
              const Center(child: Padding(padding: EdgeInsets.all(20), child: CircularProgressIndicator(color: Color(0xFF4ADE80), strokeWidth: 3)))
            else if (subProvider.history.isEmpty)
              Container(
                padding: const EdgeInsets.all(24),
                decoration: BoxDecoration(color: VamoTheme.card, borderRadius: BorderRadius.circular(16)),
                child: Column(
                  children: [
                    const Icon(Icons.history_rounded, color: VamoTheme.subtitle, size: 36),
                    const SizedBox(height: 12),
                    Text('لا يوجد سجل اشتراكات', style: Theme.of(context).textTheme.bodyMedium?.copyWith(color: VamoTheme.subtitle)),
                  ],
                ),
              )
            else
              ...subProvider.history.map((sub) => Container(
                margin: const EdgeInsets.only(bottom: 12),
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(color: VamoTheme.card, borderRadius: BorderRadius.circular(16), border: Border.all(color: const Color(0xFF2A2A2A))),
                child: Row(
                  children: [
                    Container(
                      width: 44, height: 44,
                      decoration: BoxDecoration(color: VamoTheme.primary.withValues(alpha: 0.2), borderRadius: BorderRadius.circular(14)),
                      child: const Icon(Icons.card_membership_rounded, color: Color(0xFF4ADE80), size: 22),
                    ),
                    const SizedBox(width: 14),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('${sub.price.toStringAsFixed(0)} ج.م', style: Theme.of(context).textTheme.bodyLarge?.copyWith(fontWeight: FontWeight.w700)),
                          const SizedBox(height: 4),
                          Text('${sub.startDate} — ${sub.endDate}', style: Theme.of(context).textTheme.bodySmall?.copyWith(color: VamoTheme.subtitle)),
                        ],
                      ),
                    ),
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                      decoration: BoxDecoration(color: const Color(0xFF1A1A1A), borderRadius: BorderRadius.circular(8)),
                      child: Text(sub.status.label, style: const TextStyle(fontSize: 11, fontWeight: FontWeight.w600, color: Color(0xFF9E9E9E))),
                    ),
                  ],
                ),
              )),
            const SizedBox(height: 28),

            // Logout
            OutlinedButton.icon(
              onPressed: () async {
                await auth.logout();
                if (context.mounted) {
                  Navigator.of(context).pushAndRemoveUntil(MaterialPageRoute(builder: (_) => const LoginScreen()), (_) => false);
                }
              },
              icon: const Icon(Icons.logout_rounded, color: Color(0xFFF87171)),
              label: const Text('تسجيل الخروج', style: TextStyle(color: Color(0xFFF87171), fontWeight: FontWeight.w700)),
              style: OutlinedButton.styleFrom(side: const BorderSide(color: Color(0xFF7F1D1D)), padding: const EdgeInsets.symmetric(vertical: 16), shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16))),
            ),
          ],
        ),
      ),
    );
  }

  Widget _infoTile(BuildContext context, IconData icon, String label, String value) {
    return Container(
      margin: const EdgeInsets.only(bottom: 8),
      padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 14),
      decoration: BoxDecoration(color: VamoTheme.card, borderRadius: BorderRadius.circular(14)),
      child: Row(
        children: [
          Icon(icon, color: VamoTheme.subtitle, size: 20),
          const SizedBox(width: 14),
          Text(label, style: Theme.of(context).textTheme.bodyMedium?.copyWith(color: VamoTheme.subtitle)),
          const Spacer(),
          Text(value, style: Theme.of(context).textTheme.bodyMedium?.copyWith(fontWeight: FontWeight.w700)),
        ],
      ),
    );
  }
}
