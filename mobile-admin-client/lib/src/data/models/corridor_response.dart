import 'stop_response.dart';

/// A transit corridor with its stops and pricing.
class CorridorResponse {
  const CorridorResponse({
    required this.id,
    required this.name,
    required this.price,
    required this.stops,
  });

  final int id;
  final String name;
  final double price;
  final List<StopResponse> stops;

  factory CorridorResponse.fromJson(Map<String, dynamic> json) {
    return CorridorResponse(
      id: (json['id'] as num?)?.toInt() ?? 0,
      name: json['name'] as String? ?? '',
      price: (json['price'] as num?)?.toDouble() ?? 0.0,
      stops: (json['stops'] as List<dynamic>?)
              ?.map((e) => StopResponse.fromJson(e as Map<String, dynamic>))
              .toList(growable: false) ??
          const [],
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'name': name,
        'price': price,
        'stops': stops.map((s) => s.toJson()).toList(),
      };
}
