import 'dart:io';

import 'package:flutter_test/flutter_test.dart';

void main() {
  test('Android manifest allows cleartext traffic for local backend', () {
    final manifestPath = File('android/app/src/main/AndroidManifest.xml');

    expect(manifestPath.existsSync(), isTrue, reason: 'Android manifest should exist');

    final manifest = manifestPath.readAsStringSync();
    expect(
      manifest.contains('android:usesCleartextTraffic="true"'),
      isTrue,
      reason: 'The app must allow cleartext HTTP traffic for local backend testing',
    );
  });
}
