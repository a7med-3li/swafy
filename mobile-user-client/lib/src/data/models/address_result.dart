/// A searchable address result from the backend.
///
/// Mirrors the backend `Address` entity: `{id, title, description,
/// latitude, longitude}`. Latitude/longitude come back as strings from the
/// backend, so we expose parsed doubles via [lat]/[lng].
class AddressResult {
  const AddressResult({
    required this.id,
    required this.title,
    required this.description,
    required this.latitude,
    required this.longitude,
  });

  final int id;
  final String title;
  final String description;
  final String latitude;
  final String longitude;

  double? get lat => double.tryParse(latitude);
  double? get lng => double.tryParse(longitude);

  /// A human-friendly display title (falls back to description).
  String get displayTitle => title.isNotEmpty ? title : (description.isNotEmpty ? description : 'موقع غير معروف');

  factory AddressResult.fromJson(Map<String, dynamic> json) {
    return AddressResult(
      id: (json['id'] as num?)?.toInt() ?? 0,
      title: json['title'] as String? ?? '',
      description: json['description'] as String? ?? '',
      latitude: json['latitude'] as String? ?? '',
      longitude: json['longitude'] as String? ?? '',
    );
  }
}
