import 'notification_response.dart';

/// Paged notifications payload returned by the backend.
class NotificationPageResponse {
  const NotificationPageResponse({
    required this.content,
    required this.number,
    required this.size,
    required this.totalPages,
    required this.totalElements,
    required this.first,
    required this.last,
    required this.empty,
  });

  final List<NotificationResponse> content;
  final int number;
  final int size;
  final int totalPages;
  final int totalElements;
  final bool first;
  final bool last;
  final bool empty;

  factory NotificationPageResponse.fromJson(Map<String, dynamic> json) {
    final content = (json['content'] as List<dynamic>? ?? const [])
        .whereType<Map<String, dynamic>>()
        .map(NotificationResponse.fromJson)
        .toList(growable: false);

    return NotificationPageResponse(
      content: content,
      number: (json['number'] as num?)?.toInt() ?? 0,
      size: (json['size'] as num?)?.toInt() ?? content.length,
      totalPages: (json['totalPages'] as num?)?.toInt() ?? 0,
      totalElements: (json['totalElements'] as num?)?.toInt() ?? content.length,
      first: json['first'] as bool? ?? false,
      last: json['last'] as bool? ?? true,
      empty: json['empty'] as bool? ?? content.isEmpty,
    );
  }
}
