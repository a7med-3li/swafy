import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

/// Vamo App "Sleek Mobility" Theme Design System.
///
/// Combines deep emerald/black gradients, high-contrast typography,
/// and subtle glassmorphic card borders for a modern transit aesthetic.
class VamoTheme {
  VamoTheme._();

  // Core Palette
  static const Color background = Color(0xFF0A0A0A);
  static const Color card = Color(0xFF141414);
  static const Color cardElevated = Color(0xFF1A1A1A);
  static const Color field = Color(0xFF1F1F1F);
  static const Color cardBorder = Color(0xFF2E2E2E);

  // Emerald Accent & Status
  static const Color primary = Color(0xFF05472A);
  static const Color accent = Color(0xFF4ADE80);
  static const Color accentDark = Color(0xFF166534);
  static const Color success = Color(0xFF4ADE80);
  static const Color alert = Color(0xFFF87171);
  static const Color warning = Color(0xFFFACC15);

  // Typography Tokens
  static const Color title = Colors.white;
  static const Color subtitle = Color(0xFFA0A0A0);

  static ThemeData dark() {
    final base = ThemeData.dark(useMaterial3: true);
    final textTheme = GoogleFonts.tajawalTextTheme(base.textTheme).apply(
      bodyColor: title,
      displayColor: title,
    );

    final inputBorder = OutlineInputBorder(
      borderRadius: BorderRadius.circular(16),
      borderSide: const BorderSide(color: cardBorder),
    );

    return base.copyWith(
      scaffoldBackgroundColor: background,
      canvasColor: background,
      primaryColor: accent,
      colorScheme: const ColorScheme.dark(
        brightness: Brightness.dark,
        primary: accent,
        secondary: primary,
        surface: card,
        onPrimary: Colors.black,
        onSecondary: title,
        onSurface: title,
        error: alert,
        onError: Colors.white,
      ),
      textTheme: textTheme,
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: field,
        labelStyle: const TextStyle(color: subtitle, fontWeight: FontWeight.w600),
        hintStyle: const TextStyle(color: subtitle),
        enabledBorder: inputBorder,
        focusedBorder: inputBorder.copyWith(
          borderSide: const BorderSide(color: accent, width: 1.5),
        ),
        errorBorder: inputBorder.copyWith(
          borderSide: const BorderSide(color: alert, width: 1.2),
        ),
        border: inputBorder,
        contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 18),
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: accent,
          foregroundColor: Colors.black,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          padding: const EdgeInsets.symmetric(vertical: 18),
          textStyle: const TextStyle(fontWeight: FontWeight.w800, fontSize: 16),
        ),
      ),
      outlinedButtonTheme: OutlinedButtonThemeData(
        style: OutlinedButton.styleFrom(
          foregroundColor: title,
          side: const BorderSide(color: cardBorder),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 20),
        ),
      ),
      filledButtonTheme: FilledButtonThemeData(
        style: FilledButton.styleFrom(
          backgroundColor: accent,
          foregroundColor: Colors.black,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          padding: const EdgeInsets.symmetric(vertical: 18),
          textStyle: const TextStyle(fontWeight: FontWeight.w800, fontSize: 16),
        ),
      ),
      cardTheme: const CardThemeData(
        color: card,
        elevation: 0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.all(Radius.circular(20)),
          side: BorderSide(color: cardBorder),
        ),
      ),
      appBarTheme: AppBarTheme(
        backgroundColor: Colors.transparent,
        elevation: 0,
        iconTheme: const IconThemeData(color: title),
        titleTextStyle: textTheme.titleLarge?.copyWith(fontWeight: FontWeight.w900),
      ),
    );
  }
}
