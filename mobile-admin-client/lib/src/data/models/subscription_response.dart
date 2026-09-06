/// Subscription status values from the backend.
enum SubscriptionStatus {
  pending,
  active,
  expired,
  cancelled,
  suspended;

  static SubscriptionStatus fromString(String? value) {
    switch (value?.toUpperCase()) {
      case 'ACTIVE':
        return SubscriptionStatus.active;
      case 'EXPIRED':
        return SubscriptionStatus.expired;
      case 'CANCELLED':
        return SubscriptionStatus.cancelled;
      case 'SUSPENDED':
        return SubscriptionStatus.suspended;
      default:
        return SubscriptionStatus.pending;
    }
  }

  String get label {
    switch (this) {
      case SubscriptionStatus.pending:
        return 'قيد الانتظار';
      case SubscriptionStatus.active:
        return 'نشط';
      case SubscriptionStatus.expired:
        return 'منتهي';
      case SubscriptionStatus.cancelled:
        return 'ملغى';
      case SubscriptionStatus.suspended:
        return 'موقوف';
    }
  }
}

/// A passenger subscription to a corridor.
class SubscriptionResponse {
  const SubscriptionResponse({
    required this.id,
    required this.price,
    required this.startDate,
    required this.endDate,
    required this.status,
    this.corridorTitle = '',
    this.passengerName = '',
    this.passengerPhone = '',
  });

  final int id;
  final double price;
  final String startDate;
  final String endDate;
  final SubscriptionStatus status;

  /// Corridor label (populated by admin listing endpoints).
  final String corridorTitle;

  /// Passenger display name (populated by admin listing endpoints).
  final String passengerName;

  /// Passenger phone number (populated by admin listing endpoints).
  final String passengerPhone;

  factory SubscriptionResponse.fromJson(Map<String, dynamic> json) {
    return SubscriptionResponse(
      id: (json['id'] as num?)?.toInt() ?? 0,
      price: (json['price'] as num?)?.toDouble() ?? 0.0,
      startDate: json['startDate'] as String? ?? '',
      endDate: json['endDate'] as String? ?? '',
      status: SubscriptionStatus.fromString(json['status'] as String?),
      corridorTitle: json['corridorTitle'] as String? ?? '',
      passengerName: json['passengerName'] as String? ?? '',
      passengerPhone: json['passengerPhone'] as String? ?? '',
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'price': price,
        'startDate': startDate,
        'endDate': endDate,
        'status': status.name.toUpperCase(),
      };
}
