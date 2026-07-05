const BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080"

export class ApiError extends Error {
  status: number

  constructor(status: number, message: string) {
    super(message)
    this.name = "ApiError"
    this.status = status
  }
}

function getToken(): string | null {
  return localStorage.getItem("admin_token")
}

export function setToken(token: string) {
  localStorage.setItem("admin_token", token)
}

export function clearToken() {
  localStorage.removeItem("admin_token")
}

function base64UrlDecode(str: string): string {
  str = str.replace(/-/g, "+").replace(/_/g, "/")
  while (str.length % 4) str += "="
  return atob(str)
}

export function isAuthenticated(): boolean {
  const token = getToken()
  if (!token) return false
  try {
    const payload = JSON.parse(base64UrlDecode(token.split(".")[1]))
    return payload.exp * 1000 > Date.now()
  } catch {
    return false
  }
}

async function request<TResponse>(
  method: string,
  path: string,
  body?: unknown,
): Promise<TResponse> {
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  }

  const token = getToken()
  if (token) {
    headers["Authorization"] = `Bearer ${token}`
  }

  const res = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  })

  if (!res.ok) {
    let message = `Request failed with status ${res.status}`
    try {
      const err = await res.json()
      message = err.message || err.error || message
    } catch {
      // ignore parse errors
    }
    throw new ApiError(res.status, message)
  }

  if (res.status === 204) {
    return undefined as TResponse
  }

  return res.json() as Promise<TResponse>
}

export function get<TResponse>(path: string) {
  return request<TResponse>("GET", path)
}

export function post<TResponse>(path: string, body?: unknown) {
  return request<TResponse>("POST", path, body)
}
