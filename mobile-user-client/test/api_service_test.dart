import 'package:flutter_test/flutter_test.dart';

import 'package:swafy/src/core/network/api_exception.dart';
import 'package:swafy/src/data/models/auth_response.dart';
import 'package:swafy/src/data/models/corridor_response.dart';
import 'package:swafy/src/data/models/subscription_response.dart';
import 'package:swafy/src/data/models/user_info.dart';

void main() {
  group('AuthResponse', () {
    test('parses from JSON correctly', () {
      final json = {'token': 'abc123', 'refreshToken': 'def456'};
      final auth = AuthResponse.fromJson(json);
      expect(auth.token, 'abc123');
      expect(auth.refreshToken, 'def456');
    });
  });

  group('CorridorResponse', () {
    test('parses from JSON with stops', () {
      final json = {
        'id': 1,
        'name': 'Test Corridor',
        'price': 50.0,
        'stops': [
          {'id': 1, 'name': 'Stop A', 'latitude': 29.0, 'longitude': 31.0},
          {'id': 2, 'name': 'Stop B', 'latitude': 29.1, 'longitude': 31.1},
        ],
      };
      final corridor = CorridorResponse.fromJson(json);
      expect(corridor.id, 1);
      expect(corridor.name, 'Test Corridor');
      expect(corridor.price, 50.0);
      expect(corridor.stops.length, 2);
      expect(corridor.stops[0].name, 'Stop A');
    });
  });

  group('SubscriptionResponse', () {
    test('parses status correctly', () {
      final json = {
        'id': 1,
        'price': 100.0,
        'startDate': '2026-01-01',
        'endDate': '2026-02-01',
        'status': 'ACTIVE',
      };
      final sub = SubscriptionResponse.fromJson(json);
      expect(sub.status, SubscriptionStatus.active);
      expect(sub.status.label, 'نشط');
    });
  });

  group('UserInfo', () {
    test('provides Arabic labels', () {
      final json = {
        'displayName': 'Ahmed',
        'gender': 'MALE',
        'phoneNumber': '01012345678',
        'role': 'PASSENGER',
      };
      final user = UserInfo.fromJson(json);
      expect(user.genderLabel, 'ذكر');
      expect(user.roleLabel, 'راكب');
    });
  });

  group('ApiException', () {
    test('parses from error JSON', () {
      final json = {
        'status': 400,
        'message': 'Invalid request',
        'errors': ['field1 is required'],
      };
      final ex = ApiException.fromJson(json);
      expect(ex.message, 'Invalid request');
      expect(ex.statusCode, 400);
      expect(ex.errors, ['field1 is required']);
      expect(ex.isAuthError, false);
    });

    test('detects auth errors', () {
      final ex = ApiException('Forbidden', statusCode: 403);
      expect(ex.isAuthError, true);
    });
  });
}
