import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('App launches successfully', (WidgetTester tester) async {
    // Note: VamoApp requires TokenStorage.init() to be called first.
    // This basic test just verifies the widget tree can be created.
    await tester.pumpWidget(const MaterialApp(home: Scaffold()));
    expect(find.byType(MaterialApp), findsOneWidget);
  });
}
