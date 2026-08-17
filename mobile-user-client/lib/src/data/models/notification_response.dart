/// Notification payload returned by the backend.
class NotificationResponse {
  const NotificationResponse({
    required this.id,
    required this.title,
    required this.shortDescription,
    required this.message,
    required this.status,
    this.createdAt,
  });

  final String id;
  final String title;
  final String shortDescription;
  final String message;
  final String status;
  final String? createdAt;

  bool get isUnread => status == 'UNREAD';

  factory NotificationResponse.fromJson(Map<String, dynamic> json) {
    return NotificationResponse(
      id: (json['id'] ?? '').toString(),
      title: json['title'] as String? ?? '',
      shortDescription: json['shortDescription'] as String? ?? '',
      message: json['message'] as String? ?? '',
      status: json['status'] as String? ?? 'UNREAD',
      createdAt: json['createdAt'] as String?,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'title': title,
        'shortDescription': shortDescription,
        'message': message,
        'status': status,
        'createdAt': createdAt,
      };
}
