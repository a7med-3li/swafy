import '../../core/constants/api_constants.dart';
import '../../core/network/api_client.dart';
import '../models/corridor_response.dart';
import '../models/subscription_response.dart';

/// Handles subscription-related API calls.
class SubscriptionRepository {
  SubscriptionRepository({required ApiClient apiClient}) : _api = apiClient;

  final ApiClient _api;

  /// Purchases a subscription for the given [corridor].
  ///
  /// The backend expects the full corridor object inside a `corridor` wrapper.
  Future<SubscriptionResponse> purchase(CorridorResponse corridor) async {
    final data = await _api.post(
      ApiConstants.purchaseSubscription,
      body: {
        'corridorID': corridor.id,
      },
    );

    return SubscriptionResponse.fromJson(data as Map<String, dynamic>);
  }

  /// Fetches the user's currently active subscription, if any.
  Future<List<SubscriptionResponse>> getActive() async {
    final data = await _api.get(ApiConstants.activeSubscription);

    if (data is List) {
      return data
          .map((e) => SubscriptionResponse.fromJson(e as Map<String, dynamic>))
          .toList(growable: false);
    }

    return const [];
  }

  /// Fetches subscriptions pending admin approval.
  Future<List<SubscriptionResponse>> getPending() async {
    final data = await _api.get(ApiConstants.pendingSubscription);

    if (data is List) {
      return data
          .map((e) => SubscriptionResponse.fromJson(e as Map<String, dynamic>))
          .toList(growable: false);
    }

    return const [];
  }

  /// Fetches the user's full subscription history.
  Future<List<SubscriptionResponse>> getHistory() async {
    final data = await _api.get(ApiConstants.subscriptionHistory);

    if (data is List) {
      return data
          .map((e) => SubscriptionResponse.fromJson(e as Map<String, dynamic>))
          .toList(growable: false);
    }

    return const [];
  }

  /// Cancels a subscription by [id].
  Future<void> cancel(int id) async {
    await _api.post(ApiConstants.cancelSubscription(id));
  }
}
