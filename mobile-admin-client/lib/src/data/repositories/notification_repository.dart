import '../../core/constants/api_constants.dart';
import '../../core/network/api_client.dart';
import '../models/notification_page_response.dart';
import '../models/notification_response.dart';

/// Handles notification-related API calls.
class NotificationRepository {
  NotificationRepository({required ApiClient apiClient}) : _api = apiClient;

  final ApiClient _api;

  /// Fetches notifications as a backend page.
  Future<NotificationPageResponse> getAllNotifications({
    int page = 0,
    int size = 20,
  }) async {
    final data = await _api.get(
      ApiConstants.allNotifications,
      queryParams: {
        'page': page.toString(),
        'size': size.toString(),
      },
    );

    if (data is Map<String, dynamic>) {
      return NotificationPageResponse.fromJson(data);
    }

    if (data is List) {
      final content = data
          .whereType<Map<String, dynamic>>()
          .map(NotificationResponse.fromJson)
          .toList(growable: false);

      return NotificationPageResponse(
        content: content,
        number: page,
        size: size,
        totalPages: content.isEmpty ? 0 : 1,
        totalElements: content.length,
        first: true,
        last: true,
        empty: content.isEmpty,
      );
    }

    return NotificationPageResponse.fromJson({
      'content': const [],
      'number': page,
      'size': size,
      'totalPages': 0,
      'totalElements': 0,
      'first': true,
      'last': true,
      'empty': true,
    });
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
