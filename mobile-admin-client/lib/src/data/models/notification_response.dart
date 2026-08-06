import 'stop_response.dart';

/// A transit corridor with its stops and pricing.
class NotificationResponse {
  const NotificationResponse({
    required this.id,
    required this.title,
    required this.shortDescription,
    required this.message,
  });

  final int id;
  final String title;
  final String shortDescription;
  final String message;

  factory NotificationResponse.fromJson(Map<String, dynamic> json) {
    return NotificationResponse(
      id: (json['id'] as num?)?.toInt() ?? 0,
      title: json['title'] as String? ?? '',
      shortDescription: json['shortDescription'] as String? ?? '',
      message: json['message'] as String? ?? '',
    );
  }

  Map<String, dynamic> toJson() => {
    'id': id,
    'title': title,
    'shortDescription': shortDescription,
    'message': message,
  };
}
