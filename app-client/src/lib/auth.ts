// ---------------------------------------------------------------------------
// Auth API — /api/v1/auth
//
// Base URL is read from the VITE_API_BASE_URL env variable so you can
// point to different backends per environment without code changes.
//
// .env.development  →  VITE_API_BASE_URL=http://localhost:8080
// .env.production   →  VITE_API_BASE_URL=https://api.swafy.com
// ---------------------------------------------------------------------------

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? ""

// ---------------------------------------------------------------------------
// Request / Response types
// ---------------------------------------------------------------------------

/**
 * Mirrors com.vamo.common.enums.UserRole
 * Add/remove values here as the backend enum evolves.
 */
export type UserRole = "RIDER" | "DRIVER"

/**
 * Mirrors com.vamo.common.enums.Gender
 */
export type Gender = "MALE" | "FEMALE" | "OTHER"

/**
 * Exactly mirrors com.vamo.auth.dto.UserRegistrationRequest.
 * Field names must stay in sync with the Java class.
 */
export interface UserRegistrationRequest {
  firstName: string
  lastName: string
  phoneNumber: string
  email: string
  password: string
  role: UserRole
  gender: Gender
}

/**
 * Maps to the Spring Boot `UserResponse` returned by `registerUser`.
 * Extend with additional fields as the backend evolves.
 */
export interface UserResponse {
  id: number | string
  firstName: string
  lastName: string
  email: string
  phoneNumber: string
  role: UserRole
  gender: Gender
  [key: string]: unknown   // forward-compatible with extra fields
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

/** Generic API error — carries the HTTP status + server message. */
export class ApiError extends Error {
  constructor(
    public readonly status: number,
    message: string
  ) {
    super(message)
    this.name = "ApiError"
  }
}

async function post<TBody, TResponse>(
  path: string,
  body: TBody
): Promise<TResponse> {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  })

  if (!res.ok) {
    // Try to read a server-provided error message
    let message = `Request failed with status ${res.status}`
    try {
      const data = await res.json()
      // Spring Boot usually sends { message: "..." } or { error: "..." }
      message = data?.message ?? data?.error ?? message
    } catch {
      // response body wasn't JSON — keep the default message
    }
    throw new ApiError(res.status, message)
  }

  return res.json() as Promise<TResponse>
}

// ---------------------------------------------------------------------------
// Auth endpoints
// ---------------------------------------------------------------------------

/**
 * POST /api/v1/auth/register/{role}
 *
 * Registers a new user and returns their profile.
 * The endpoint mirrors the backend split: riders use `/register/passenger`,
 * drivers use `/register/driver`.
 * Throws an `ApiError` on any non-2xx response.
 */
export async function registerUser(
  payload: UserRegistrationRequest
): Promise<UserResponse> {
  const endpoint =
    payload.role === "DRIVER"
      ? "/api/v1/auth/register/driver"
      : "/api/v1/auth/register/passenger"

  return post<UserRegistrationRequest, UserResponse>(endpoint, payload)
}
