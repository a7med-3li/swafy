import '../../core/constants/api_constants.dart';
import '../../core/network/api_client.dart';
import '../models/corridor_response.dart';

/// Handles corridor-related API calls.
class CorridorRepository {
  CorridorRepository({required ApiClient apiClient}) : _api = apiClient;

  final ApiClient _api;

  /// Fetches all available corridors.
  Future<List<CorridorResponse>> getAllCorridors() async {
    final data = await _api.get(ApiConstants.corridors);

    if (data is List) {
      return data
          .map((e) => CorridorResponse.fromJson(e as Map<String, dynamic>))
          .toList(growable: false);
    }

    return const [];
  }

  /// Creates a new corridor (admin/manager use).
  Future<CorridorResponse> createCorridor({
    required String name,
    required String route,
    required double price,
    required List<Map<String, dynamic>> stops,
  }) async {
    final data = await _api.post(
      ApiConstants.addCorridor,
      body: {
        'name': name,
        'route': route,
        'price': price,
        'stops': stops,
      },
    );

    return CorridorResponse.fromJson(data as Map<String, dynamic>);
  }
}
