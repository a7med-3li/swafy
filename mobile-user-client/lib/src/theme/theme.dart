import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class VamoTheme {
  VamoTheme._();

  static const Color background = Color(0xFF000000);
  static const Color card = Color(0xFF1E1E1E);
  static const Color field = Color(0xFF2A2A2A);
  static const Color primary = Color(0xFF05472A);
  static const Color title = Colors.white;
  static const Color subtitle = Color(0xFFA0A0A0);

  static ThemeData dark() {
    final base = ThemeData.dark(useMaterial3: true);
    final textTheme = GoogleFonts.cairoTextTheme(base.textTheme).apply(
      bodyColor: title,
      displayColor: title,
    );

    final inputBorder = OutlineInputBorder(
      borderRadius: BorderRadius.circular(12),
      borderSide: const BorderSide(color: Color(0xFF373737)),
    );

    return base.copyWith(
      scaffoldBackgroundColor: background,
      canvasColor: background,
      primaryColor: primary,
      colorScheme: const ColorScheme.dark(
        brightness: Brightness.dark,
        primary: primary,
        secondary: primary,
        surface: card,
        onPrimary: title,
        onSecondary: title,
        onSurface: title,
        error: Colors.redAccent,
        onError: Colors.white,
      ),
      textTheme: textTheme,
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: field,
        labelStyle: const TextStyle(color: subtitle),
        hintStyle: const TextStyle(color: subtitle),
        enabledBorder: inputBorder,
        focusedBorder: inputBorder.copyWith(borderSide: const BorderSide(color: primary, width: 1.5)),
        border: inputBorder,
        contentPadding: const EdgeInsets.symmetric(horizontal: 18, vertical: 18),
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: primary,
          foregroundColor: title,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          padding: const EdgeInsets.symmetric(vertical: 18),
          textStyle: const TextStyle(fontWeight: FontWeight.w700),
        ),
      ),
      outlinedButtonTheme: OutlinedButtonThemeData(
        style: OutlinedButton.styleFrom(
          foregroundColor: title,
          side: const BorderSide(color: Color(0xFF373737)),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 20),
        ),
      ),
      filledButtonTheme: FilledButtonThemeData(
        style: FilledButton.styleFrom(
          backgroundColor: primary,
          foregroundColor: title,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          padding: const EdgeInsets.symmetric(vertical: 18),
          textStyle: const TextStyle(fontWeight: FontWeight.w700, fontSize: 16),
        ),
      ),
      cardTheme: const CardThemeData(
        color: card,
        elevation: 0,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.all(Radius.circular(16))),
      ),
      appBarTheme: AppBarTheme(
        backgroundColor: Colors.transparent,
        elevation: 0,
        iconTheme: const IconThemeData(color: title),
        titleTextStyle: textTheme.titleLarge?.copyWith(fontWeight: FontWeight.w800),
      ),
    );
  }
}
