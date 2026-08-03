import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:provider/provider.dart';

import '../../providers/auth_provider.dart';
import '../../providers/subscription_provider.dart';
import '../../theme/theme.dart';
import '../../widgets/vamo_button.dart';
import '../auth/login_screen.dart';
import '../corridors/corridors_screen.dart';
import '../profile/profile_screen.dart';
import '../subscriptions/subscription_history_screen.dart';

/// Main dashboard shown after login.
///
/// Contains a top action menu, bottom navigation, and a focused Home view.
class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _currentIndex = 0;

  /// Public method to switch tabs from child widgets.
  void switchTab(int index) {
    setState(() => _currentIndex = index);
  }

  @override
  void initState() {
    super.initState();
    // Load active subscription on startup.
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<SubscriptionProvider>().loadActive();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: VamoTheme.background,
        appBar: _buildAppBar(context),
        body: IndexedStack(
          index: _currentIndex,
          children: const [
            _DashboardTab(),
            CorridorsScreen(),
            ProfileScreen(),
          ],
        ),
        bottomNavigationBar: _buildBottomNav(),
      ),
    );
  }

  PreferredSizeWidget _buildAppBar(BuildContext context) {
    return AppBar(
      backgroundColor: VamoTheme.background,
      elevation: 0,
      title: Row(
        children: [
          Container(
            width: 38,
            height: 38,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(10),
              color: VamoTheme.primary.withValues(alpha: 0.15),
            ),
            child: ClipRRect(
              borderRadius: BorderRadius.circular(10),
              child: Padding(
                padding: const EdgeInsets.all(6),
                child: SvgPicture.asset(
                  'assets/images/vamo_logo.svg',
                  width: 24,
                  height: 24,
                ),
              ),
            ),
          ),
          const SizedBox(width: 10),
          Text(
            'Vamo',
            style: Theme.of(context).textTheme.titleLarge?.copyWith(
                  fontWeight: FontWeight.w900,
                  letterSpacing: 1.2,
                ),
          ),
        ],
      ),
      actions: [
        PopupMenuButton<String>(
          onSelected: (value) => _handleMenuSelection(context, value),
          icon: Container(
            width: 40,
            height: 40,
            decoration: BoxDecoration(
              color: VamoTheme.card,
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: const Color(0xFF2A2A2A)),
            ),
            child: const Icon(
              Icons.menu_rounded,
              color: Colors.white,
              size: 22,
            ),
          ),
          color: VamoTheme.card,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
            side: const BorderSide(color: Color(0xFF2A2A2A)),
          ),
          position: PopupMenuPosition.under,
          itemBuilder: (context) => [
            const PopupMenuItem(
              value: 'profile',
              child: Row(
                children: [
                  Icon(Icons.person_outline_rounded,
                      color: VamoTheme.subtitle, size: 20),
                  SizedBox(width: 12),
                  Text('الملف الشخصي',
                      style: TextStyle(fontWeight: FontWeight.w700)),
                ],
              ),
            ),
            const PopupMenuItem(
              value: 'history',
              child: Row(
                children: [
                  Icon(Icons.history_rounded,
                      color: VamoTheme.subtitle, size: 20),
                  SizedBox(width: 12),
                  Text('سجل الاشتراكات',
                      style: TextStyle(fontWeight: FontWeight.w700)),
                ],
              ),
            ),
            const PopupMenuDivider(height: 1),
            const PopupMenuItem(
              value: 'logout',
              child: Row(
                children: [
                  Icon(Icons.logout_rounded,
                      color: Color(0xFFF87171), size: 20),
                  SizedBox(width: 12),
                  Text('تسجيل الخروج',
                      style: TextStyle(
                        color: Color(0xFFF87171),
                        fontWeight: FontWeight.w700,
                      )),
                ],
              ),
            ),
          ],
        ),
        const SizedBox(width: 16),
      ],
    );
  }

  void _handleMenuSelection(BuildContext context, String value) {
    switch (value) {
      case 'profile':
        switchTab(2);
        break;
      case 'history':
        Navigator.of(context).push(
          MaterialPageRoute(
            builder: (_) => const SubscriptionHistoryScreen(),
          ),
        );
        break;
      case 'logout':
        _confirmLogout(context);
        break;
    }
  }

  void _confirmLogout(BuildContext context) {
    showDialog(
      context: context,
      builder: (ctx) => Directionality(
        textDirection: TextDirection.rtl,
        child: AlertDialog(
          backgroundColor: VamoTheme.card,
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
          title: const Text('تسجيل الخروج',
              style: TextStyle(fontWeight: FontWeight.w800)),
          content: const Text('هل أنت متأكد أنك تريد تسجيل الخروج من حسابك؟'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(ctx),
              child: const Text('إلغاء',
                  style: TextStyle(color: Color(0xFFA0A0A0))),
            ),
            FilledButton(
              onPressed: () async {
                Navigator.pop(ctx);
                final auth = context.read<AuthProvider>();
                await auth.logout();
                if (context.mounted) {
                  Navigator.of(context).pushReplacement(
                    MaterialPageRoute(builder: (_) => const LoginScreen()),
                  );
                }
              },
              style:
                  FilledButton.styleFrom(backgroundColor: const Color(0xFFB91C1C)),
              child: const Text('تسجيل الخروج'),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildBottomNav() {
    return Container(
      decoration: const BoxDecoration(
        color: Color(0xFF0D0D0D),
        border: Border(top: BorderSide(color: Color(0xFF1F1F1F))),
      ),
      child: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              _navItem(0, Icons.home_rounded, 'الرئيسية'),
              _navItem(1, Icons.route_rounded, 'المسارات'),
              _navItem(2, Icons.person_outline_rounded, 'حسابي'),
            ],
          ),
        ),
      ),
    );
  }

  Widget _navItem(int index, IconData icon, String label) {
    final isSelected = _currentIndex == index;
    return GestureDetector(
      onTap: () => setState(() => _currentIndex = index),
      behavior: HitTestBehavior.opaque,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
        decoration: BoxDecoration(
          color: isSelected
              ? VamoTheme.primary.withValues(alpha: 0.15)
              : Colors.transparent,
          borderRadius: BorderRadius.circular(16),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              icon,
              color: isSelected ? const Color(0xFF4ADE80) : VamoTheme.subtitle,
              size: 24,
            ),
            if (isSelected) ...[
              const SizedBox(width: 8),
              Text(
                label,
                style: const TextStyle(
                  color: Color(0xFF4ADE80),
                  fontWeight: FontWeight.w700,
                  fontSize: 13,
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

/// The "Home" tab content — clean, focused dashboard without scroll clutter.
class _DashboardTab extends StatelessWidget {
  const _DashboardTab();

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();
    final subProvider = context.watch<SubscriptionProvider>();

    return SafeArea(
      child: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        physics: const BouncingScrollPhysics(),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // ── Greeting Card ──────────────────────────────────
            Row(
              children: [
                Container(
                  width: 58,
                  height: 58,
                  decoration: BoxDecoration(
                    gradient: const LinearGradient(
                      colors: [Color(0xFF05472A), Color(0xFF0A6B3E)],
                    ),
                    borderRadius: BorderRadius.circular(20),
                    boxShadow: [
                      BoxShadow(
                        color: VamoTheme.primary.withValues(alpha: 0.25),
                        blurRadius: 16,
                        offset: const Offset(0, 4),
                      ),
                    ],
                  ),
                  child: Center(
                    child: Text(
                      auth.displayName.isNotEmpty
                          ? auth.displayName[0].toUpperCase()
                          : 'V',
                      style: const TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.w900,
                        fontSize: 24,
                      ),
                    ),
                  ),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'مرحبًا، ${auth.displayName} 👋',
                        style:
                            Theme.of(context).textTheme.headlineSmall?.copyWith(
                                  fontWeight: FontWeight.w900,
                                  fontSize: 22,
                                ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        'رحلتك اليومية أسهل مع Vamo',
                        style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                              color: VamoTheme.subtitle,
                            ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
            const SizedBox(height: 32),

            // ── Active Subscription Status ─────────────────────
            _buildSubscriptionSection(context, subProvider),
          ],
        ),
      ),
    );
  }

  Widget _buildSubscriptionSection(
      BuildContext context, SubscriptionProvider subProvider) {
    final active = subProvider.active;

    if (active != null) {
      final daysLeft = _calculateDaysLeft(active.endDate);

      return Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Container(
            padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              gradient: const LinearGradient(
                colors: [Color(0xFF05472A), Color(0xFF081A19)],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
              borderRadius: BorderRadius.circular(26),
              border: Border.all(
                color: const Color(0xFF166534).withValues(alpha: 0.5),
                width: 1.5,
              ),
              boxShadow: [
                BoxShadow(
                  color: VamoTheme.primary.withValues(alpha: 0.15),
                  blurRadius: 24,
                  offset: const Offset(0, 8),
                ),
              ],
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Top row: Status badge & icon
                Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.all(10),
                      decoration: BoxDecoration(
                        color: const Color(0xFF166534),
                        borderRadius: BorderRadius.circular(14),
                      ),
                      child: const Icon(
                        Icons.card_membership_rounded,
                        color: Color(0xFF4ADE80),
                        size: 22,
                      ),
                    ),
                    const SizedBox(width: 12),
                    Text(
                      'اشتراكك الحالي',
                      style: Theme.of(context).textTheme.titleLarge?.copyWith(
                            fontWeight: FontWeight.w900,
                          ),
                    ),
                    const Spacer(),
                    Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 12, vertical: 6),
                      decoration: BoxDecoration(
                        color: const Color(0xFF166534),
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: Text(
                        active.status.label,
                        style: const TextStyle(
                          color: Color(0xFF4ADE80),
                          fontSize: 12,
                          fontWeight: FontWeight.w800,
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 22),

                // Days remaining badge
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                  decoration: BoxDecoration(
                    color: const Color(0xFF092E1C),
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(
                      color: const Color(0xFF15803D).withValues(alpha: 0.6),
                    ),
                  ),
                  child: Row(
                    children: [
                      const Icon(
                        Icons.timer_outlined,
                        color: Color(0xFF4ADE80),
                        size: 22,
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Text(
                          daysLeft > 0
                              ? 'متبقي $daysLeft يوم على تجديد الاشتراك'
                              : 'الاشتراك ينتهي اليوم أو يحتاج تجديد',
                          style: const TextStyle(
                            color: Color(0xFF86EFAC),
                            fontWeight: FontWeight.w800,
                            fontSize: 14,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 24),

                // Dates and Price
                Row(
                  children: [
                    _subInfoItem(
                      context,
                      'القيمة الشهرية',
                      '${active.price.toStringAsFixed(0)} ج.م',
                      isHighlight: true,
                    ),
                    const SizedBox(width: 28),
                    _subInfoItem(context, 'تاريخ البدء', active.startDate),
                    const SizedBox(width: 28),
                    _subInfoItem(context, 'تاريخ الانتهاء', active.endDate),
                  ],
                ),
              ],
            ),
          ),
          const SizedBox(height: 20),

          // Quick Actions for active user
          Row(
            children: [
              Expanded(
                child: VamoButton(
                  label: 'تصفح مسار آخر / تجديد',
                  icon: Icons.add_circle_outline_rounded,
                  onPressed: () {
                    final homeState =
                        context.findAncestorStateOfType<_HomeScreenState>();
                    homeState?.switchTab(1);
                  },
                ),
              ),
              const SizedBox(width: 12),
              SizedBox(
                width: 58,
                height: 58,
                child: OutlinedButton(
                  onPressed: () {
                    Navigator.of(context).push(
                      MaterialPageRoute(
                        builder: (_) => const SubscriptionHistoryScreen(),
                      ),
                    );
                  },
                  style: OutlinedButton.styleFrom(
                    side: const BorderSide(color: Color(0xFF373737)),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(16),
                    ),
                    padding: EdgeInsets.zero,
                  ),
                  child: const Icon(
                    Icons.history_rounded,
                    color: VamoTheme.subtitle,
                    size: 24,
                  ),
                ),
              ),
            ],
          ),
        ],
      );
    }

    // Empty state when user has no active subscription
    return Container(
      padding: const EdgeInsets.all(28),
      decoration: BoxDecoration(
        color: VamoTheme.card,
        borderRadius: BorderRadius.circular(26),
        border: Border.all(color: const Color(0xFF2A2A2A)),
      ),
      child: Column(
        children: [
          Container(
            width: 72,
            height: 72,
            decoration: BoxDecoration(
              color: VamoTheme.primary.withValues(alpha: 0.15),
              borderRadius: BorderRadius.circular(22),
            ),
            child: const Icon(
              Icons.explore_rounded,
              color: Color(0xFF4ADE80),
              size: 36,
            ),
          ),
          const SizedBox(height: 20),
          Text(
            'لا يوجد اشتراك فعال حالياً',
            style: Theme.of(context).textTheme.titleLarge?.copyWith(
                  fontWeight: FontWeight.w900,
                ),
          ),
          const SizedBox(height: 10),
          Text(
            'اشترك الآن في مسارك اليومي واستمتع برحلات مريحة، تعرفة موحدة، وسائقين معتمدين.',
            textAlign: TextAlign.center,
            style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                  color: VamoTheme.subtitle,
                  height: 1.6,
                ),
          ),
          const SizedBox(height: 26),
          VamoButton(
            label: 'تصفح المسارات المتاحة 🧭',
            icon: Icons.search_rounded,
            onPressed: () {
              final homeState =
                  context.findAncestorStateOfType<_HomeScreenState>();
              homeState?.switchTab(1);
            },
          ),
        ],
      ),
    );
  }

  int _calculateDaysLeft(String endDateStr) {
    try {
      final endDate = DateTime.parse(endDateStr);
      final now = DateTime.now();
      final difference =
          endDate.difference(DateTime(now.year, now.month, now.day)).inDays;
      return difference > 0 ? difference : 0;
    } catch (_) {
      return 30; // Default fallback if parsing fails
    }
  }

  Widget _subInfoItem(
    BuildContext context,
    String label,
    String value, {
    bool isHighlight = false,
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          label,
          style: Theme.of(context).textTheme.bodySmall?.copyWith(
                color: const Color(0xFF9E9E9E),
              ),
        ),
        const SizedBox(height: 6),
        Text(
          value,
          style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                fontWeight: isHighlight ? FontWeight.w900 : FontWeight.w700,
                color: isHighlight ? const Color(0xFF4ADE80) : Colors.white,
                fontSize: isHighlight ? 18 : 15,
              ),
        ),
      ],
    );
  }
}
