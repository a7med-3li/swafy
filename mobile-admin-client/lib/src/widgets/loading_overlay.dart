import 'package:flutter/material.dart';

/// A semi-transparent full-screen overlay with a centered spinner.
///
/// Wrap around content that should be blocked during loading:
/// ```dart
/// LoadingOverlay(
///   isLoading: provider.isLoading,
///   child: MyContent(),
/// )
/// ```
class LoadingOverlay extends StatelessWidget {
  const LoadingOverlay({
    super.key,
    required this.isLoading,
    required this.child,
  });

  final bool isLoading;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        child,
        if (isLoading)
          Positioned.fill(
            child: Container(
              color: Colors.black54,
              child: const Center(
                child: CircularProgressIndicator(
                  color: Color(0xFF4ADE80),
                  strokeWidth: 3,
                ),
              ),
            ),
          ),
      ],
    );
  }
}
