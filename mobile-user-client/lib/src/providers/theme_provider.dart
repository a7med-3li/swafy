import 'package:flutter/material.dart';

/// Manages the application theme mode (Dark / Light).
///
/// Provides smooth toggling between [ThemeMode.dark] and [ThemeMode.light]
/// and notifies listeners across the widget tree.
class ThemeProvider extends ChangeNotifier {
  ThemeMode _themeMode = ThemeMode.dark;

  ThemeMode get themeMode => _themeMode;

  bool get isDark => _themeMode == ThemeMode.dark;

  /// Toggles between dark and light modes.
  void toggleTheme() {
    _themeMode = isDark ? ThemeMode.light : ThemeMode.dark;
    notifyListeners();
  }

  /// Explicitly sets the desired theme mode.
  void setThemeMode(ThemeMode mode) {
    if (_themeMode == mode) return;
    _themeMode = mode;
    notifyListeners();
  }
}
