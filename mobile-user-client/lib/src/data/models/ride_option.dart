import 'package:flutter/material.dart';

/// A single ride option returned by the backend ride-request endpoint.
///
/// Mirrors the backend `RoutingResponse`: `{routePolyline, duration,
/// distance, vehicleType}`. [duration] is in seconds, [distance] in meters.
class RideOption {
  const RideOption({
    required this.routePolyline,
    required this.duration,
    required this.distance,
    required this.vehicleType,
  });

  final String routePolyline;
  final int duration;
  final int distance;
  final String vehicleType;

  factory RideOption.fromJson(Map<String, dynamic> json) {
    return RideOption(
      routePolyline: json['routePolyline'] as String? ?? '',
      duration: (json['duration'] as num?)?.toInt() ?? 0,
      distance: (json['distance'] as num?)?.toInt() ?? 0,
      vehicleType: json['vehicleType'] as String? ?? '',
    );
  }

  /// Human-readable ride-type name in Arabic.
  String get typeLabel {
    switch (vehicleType.toUpperCase()) {
      case 'TAXI':
        return 'تاكسي';
      case 'SCOOTER':
        return 'سكوتر';
      case 'BUS':
        return 'أوتوبيس';
      case 'CAR':
      default:
        return 'سيارة';
    }
  }

  /// Estimated travel time formatted as `X س Y د` or `Y د`.
  String get formattedDuration {
    final minutes = (duration / 60).ceil();
    if (minutes < 60) return '$minutes دقيقة';
    final hours = minutes ~/ 60;
    final rest = minutes % 60;
    return rest == 0 ? '$hours ساعة' : '$hours س $rest د';
  }

  /// Distance formatted as kilometres (rounded) or metres when short.
  String get formattedDistance {
    if (distance >= 1000) {
      final km = distance / 1000;
      return km >= 10 ? '${km.round()} كم' : '${km.toStringAsFixed(1)} كم';
    }
    return '$distance م';
  }

  /// Material icon representing the vehicle type.
  IconData get icon {
    switch (vehicleType.toUpperCase()) {
      case 'TAXI':
        return Icons.local_taxi_rounded;
      case 'SCOOTER':
        return Icons.two_wheeler_rounded;
      case 'BUS':
        return Icons.directions_bus_rounded;
      case 'CAR':
      default:
        return Icons.directions_car_rounded;
    }
  }
}