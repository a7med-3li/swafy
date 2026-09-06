import '../../core/constants/api_constants.dart';
import '../../core/network/api_client.dart';
import '../models/ride_option.dart';

/// Handles ride-request API calls.
///
/// [requestRide] hits `GET /api/v3/ride/request` (GET with a JSON body),
/// which returns a list of `RoutingResponse` ride options for the given
/// pick-up / drop-off coordinates.
class RideRepository {
  RideRepository({required ApiClient apiClient}) : _api = apiClient;

  final ApiClient _api;

  Future<List<RideOption>> requestRide({
    required double pickupLatitude,
    required double pickupLongitude,
    required double dropoffLatitude,
    required double dropoffLongitude,
  }) async {
    final data = await _api.getWithBody(
      ApiConstants.rideRequest,
      body: {
        'pickUp': {
          'latitude': pickupLatitude,
          'longitude': pickupLongitude,
        },
        'dropOff': {
          'latitude': dropoffLatitude,
          'longitude': dropoffLongitude,
        },
      },
    );

    if (data is List) {
      return data
          .whereType<Map<String, dynamic>>()
          .map(RideOption.fromJson)
          .toList(growable: false);
    }

    return const [];
  }
}