/// Structured error returned by the backend.
class ErrorResponse {
  const ErrorResponse({
    required this.status,
    required this.message,
    this.timestamp,
    this.errors = const [],
  });

  final int status;
  final String message;
  final String? timestamp;
  final List<String> errors;

  factory ErrorResponse.fromJson(Map<String, dynamic> json) {
    return ErrorResponse(
      status: (json['status'] as num?)?.toInt() ?? 0,
      message: json['message'] as String? ?? '',
      timestamp: json['timestamp'] as String?,
      errors: (json['errors'] as List<dynamic>?)
              ?.map((e) => e.toString())
              .toList(growable: false) ??
          const [],
    );
  }
}
