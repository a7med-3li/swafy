import 'dart:async';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/corridor_provider.dart';
import '../../theme/theme.dart';
import '../../widgets/corridor_card.dart';
import '../../widgets/error_banner.dart';
import 'corridor_detail_screen.dart';

/// Screen displaying all available corridors with a local search bar.
class CorridorsScreen extends StatefulWidget {
  const CorridorsScreen({super.key});

  @override
  State<CorridorsScreen> createState() => _CorridorsScreenState();
}

class _CorridorsScreenState extends State<CorridorsScreen> {
  final _searchController = TextEditingController();
  String _searchQuery = '';
  Timer? _debounce;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<CorridorProvider>().loadCorridors();
    });
  }

  @override
  void dispose() {
    _searchController.dispose();
    _debounce?.cancel();
    super.dispose();
  }

  void _onSearchChanged(String value) {
    _debounce?.cancel();
    _debounce = Timer(const Duration(milliseconds: 300), () {
      if (mounted) {
        setState(() => _searchQuery = value.trim());
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<CorridorProvider>();

    // Client-side filtering
    final filteredCorridors = _searchQuery.isEmpty
        ? provider.corridors
        : provider.corridors.where((c) {
            final query = _searchQuery.toLowerCase();
            return c.name.toLowerCase().contains(query);
          }).toList();

    return SafeArea(
      child: CustomScrollView(
        physics: const BouncingScrollPhysics(),
        slivers: [
          SliverPadding(
            padding: const EdgeInsets.fromLTRB(24, 24, 24, 0),
            sliver: SliverToBoxAdapter(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'المسارات المتاحة',
                    style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                          fontWeight: FontWeight.w900,
                        ),
                  ),
                  const SizedBox(height: 6),
                  Text(
                    'اختر المسار المناسب لرحلتك اليومية',
                    style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                          color: context.subtitleColor,
                        ),
                  ),
                  const SizedBox(height: 16),

                  // ── Search Bar ─────────────────────────────────
                  Container(
                    decoration: BoxDecoration(
                      color: context.fieldColor,
                      borderRadius: BorderRadius.circular(16),
                      border: Border.all(color: context.cardBorderColor),
                    ),
                    child: TextField(
                      controller: _searchController,
                      onChanged: _onSearchChanged,
                      textDirection: TextDirection.rtl,
                      style: TextStyle(
                        color: context.titleColor,
                        fontWeight: FontWeight.w600,
                      ),
                      decoration: InputDecoration(
                        hintText: 'ابحث عن مسار...',
                        hintStyle: TextStyle(
                          color: context.subtitleColor,
                          fontWeight: FontWeight.w500,
                        ),
                        prefixIcon: Icon(
                          Icons.search_rounded,
                          color: context.subtitleColor,
                          size: 22,
                        ),
                        suffixIcon: _searchQuery.isNotEmpty
                            ? IconButton(
                                onPressed: () {
                                  _searchController.clear();
                                  setState(() => _searchQuery = '');
                                },
                                icon: Icon(
                                  Icons.close_rounded,
                                  color: context.subtitleColor,
                                  size: 20,
                                ),
                              )
                            : null,
                        border: InputBorder.none,
                        enabledBorder: InputBorder.none,
                        focusedBorder: InputBorder.none,
                        contentPadding: const EdgeInsets.symmetric(
                          horizontal: 16,
                          vertical: 14,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),

                  ErrorBanner(
                    message: provider.error,
                    onDismiss: provider.clearError,
                  ),
                  if (provider.error != null) const SizedBox(height: 12),
                ],
              ),
            ),
          ),
          if (provider.isLoading)
            SliverFillRemaining(
              child: Center(
                child: CircularProgressIndicator(
                  color: VamoTheme.accent,
                  strokeWidth: 3,
                ),
              ),
            )
          else if (provider.corridors.isEmpty)
            SliverFillRemaining(
              child: Center(
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
                        ),
                        child: Icon(
                          Icons.route_rounded,
                          color: context.subtitleColor,
                          size: 36,
                        ),
                      ),
                      const SizedBox(height: 20),
                      Text(
                        'لا توجد مسارات حالياً',
                        style: Theme.of(context).textTheme.titleMedium?.copyWith(
                              fontWeight: FontWeight.w700,
                            ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        'سيتم إضافة مسارات جديدة قريباً.',
                        style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                              color: context.subtitleColor,
                            ),
                      ),
                      const SizedBox(height: 24),
                      TextButton.icon(
                        onPressed: () => provider.forceRefresh(),
                        icon: const Icon(Icons.refresh_rounded, size: 20),
                        label: const Text('إعادة المحاولة'),
                        style: TextButton.styleFrom(
                          foregroundColor: VamoTheme.accent,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            )
          else if (filteredCorridors.isEmpty)
            SliverFillRemaining(
              child: Center(
                child: Padding(
                  padding: const EdgeInsets.all(32),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(
                        Icons.search_off_rounded,
                        color: context.subtitleColor,
                        size: 48,
                      ),
                      const SizedBox(height: 16),
                      Text(
                        'لا توجد نتائج لـ "$_searchQuery"',
                        style: Theme.of(context).textTheme.titleMedium?.copyWith(
                              fontWeight: FontWeight.w700,
                            ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        'جرب كلمة بحث مختلفة',
                        style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                              color: context.subtitleColor,
                            ),
                      ),
                    ],
                  ),
                ),
              ),
            )
          else
            SliverPadding(
              padding: const EdgeInsets.fromLTRB(24, 0, 24, 24),
              sliver: SliverList(
                delegate: SliverChildBuilderDelegate(
                  (context, index) {
                    final corridor = filteredCorridors[index];
                    return Padding(
                      padding: const EdgeInsets.only(bottom: 12),
                      child: CorridorCard(
                        corridor: corridor,
                        onTap: () {
                          Navigator.of(context).push(
                            MaterialPageRoute(
                              builder: (_) => CorridorDetailScreen(
                                corridor: corridor,
                              ),
                            ),
                          );
                        },
                      ),
                    );
                  },
                  childCount: filteredCorridors.length,
                ),
              ),
            ),
        ],
      ),
    );
  }
}
