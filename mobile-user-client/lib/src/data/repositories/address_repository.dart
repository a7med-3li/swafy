import '../../core/constants/api_constants.dart';
import '../../core/network/api_client.dart';
import '../models/address_result.dart';

/// Handles address autocomplete + search used in the Book-a-Ride flow.
///
/// - [autoComplete] hits `GET /api/v1/address/autoComplete` which returns
///   `{source, addresseList: [Address...]}` (local database / quick match).
/// - [search] hits `GET /api/v1/address/search` which returns a bare
///   `[Address...]` list (geocoded via the maps API).
class AddressRepository {
  AddressRepository({required ApiClient apiClient}) : _api = apiClient;

  final ApiClient _api;

  /// Fetches autocomplete matches for [query]. Returns an empty list on
  /// empty input or when the autocomplete source returns nothing.
  Future<List<AddressResult>> autoComplete(String query) async {
    final trimmed = query.trim();
    if (trimmed.isEmpty) return const [];

    final data = await _api.get(
      ApiConstants.addressAutoComplete,
      queryParams: {'address': trimmed},
    );

    return _parseList(data);
  }

  /// Performs a full geocoding search for [query] against the maps API.
  Future<List<AddressResult>> search(String query) async {
    final trimmed = query.trim();
    if (trimmed.isEmpty) return const [];

    final data = await _api.get(
      ApiConstants.addressSearch,
      queryParams: {'address': trimmed},
    );

    return _parseList(data);
  }

  /// Extracts the address list from either a bare list or the autocomplete
  /// wrapper `{source, addresseList: [...]}`.
  List<AddressResult> _parseList(dynamic data) {
    List<dynamic> raw;
    if (data is List) {
      raw = data;
    } else if (data is Map<String, dynamic>) {
      final list = data['addresseList'] ?? data['addresses'];
      raw = list is List ? list : const [];
    } else {
      return const [];
    }

    return raw
        .whereType<Map<String, dynamic>>()
        .map(AddressResult.fromJson)
        .toList(growable: false);
  }
}
