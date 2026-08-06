import '../../core/constants/api_constants.dart';
import '../../core/network/api_client.dart';
import '../models/notification_response.dart';

/// Handles notification-related API calls.
class NotificationRepository {
  NotificationRepository({required ApiClient apiClient}) : _api = apiClient;

  final ApiClient _api;

  /// Fetches all available notifications.
  Future<List<NotificationResponse>> getAllNotifications() async {
    final data = await _api.get(ApiConstants.notifications);

    if (data is List) {
      return data
          .map((e) => NotificationResponse.fromJson(e as Map<String, dynamic>))
          .toList(growable: false);
    }

    return const [];
  }

  /// Retrieves a specific notification by its ID.
  Future<NotificationResponse> getNotification({
    required String id
  }) async {
    final data = await _api.get(
      ApiConstants.getNotification(id),
    );

    return NotificationResponse.fromJson(data as Map<String, dynamic>);
  }
}
