import { useState } from "react"
import { Eye, EyeOff, Loader2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Checkbox } from "@/components/ui/checkbox"
import { cn } from "@/lib/utils"
import type { UserRole, Gender } from "@/lib/auth"
import logo from "/assets/logo.jpg"

// ---------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------

export interface SignupFormData {
  firstName: string
  lastName: string
  email: string
  phoneNumber: string
  password: string
  role: UserRole
  gender: Gender
  agreedToTerms: boolean
}

export interface SignupPageProps {
  /** Called when the user submits the form. Re-throw to show the error banner. */
  onSignup?: (data: SignupFormData) => Promise<void>
  onLoginClick?: () => void
  onTermsClick?: () => void
  onPrivacyClick?: () => void
}

// ---------------------------------------------------------------------------
// Validation
// ---------------------------------------------------------------------------

type FieldErrors = Partial<Record<keyof SignupFormData, string>>

function validate(data: SignupFormData): FieldErrors {
  const errors: FieldErrors = {}
  if (!data.firstName.trim()) errors.firstName = "First name is required."
  if (!data.lastName.trim()) errors.lastName = "Last name is required."

  const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!data.email.trim()) errors.email = "Email address is required."
  else if (!emailRe.test(data.email)) errors.email = "Please enter a valid email address."

  if (!data.phoneNumber.trim()) errors.phoneNumber = "Phone number is required."
  else if (!/^\+?[\d\s\-()]{7,}$/.test(data.phoneNumber))
    errors.phoneNumber = "Please enter a valid phone number."

  if (!data.password) errors.password = "Password is required."
  else if (data.password.length < 8) errors.password = "Password must be at least 8 characters."

  if (!data.role) errors.role = "Please select your role."
  if (!data.gender) errors.gender = "Please select your gender."

  if (!data.agreedToTerms) errors.agreedToTerms = "You must agree to the Terms and Conditions."

  return errors
}

// ---------------------------------------------------------------------------
// Sub-components
// ---------------------------------------------------------------------------

/** Segmented toggle for a fixed set of string options */
function SegmentedControl<T extends string>({
  options,
  value,
  onChange,
  hasError,
}: {
  options: { label: string; value: T }[]
  value: T
  onChange: (v: T) => void
  hasError?: boolean
}) {
  return (
    <div
      className={cn(
        "flex overflow-hidden rounded-2xl border border-border bg-card p-1 gap-1",
        hasError && "border-destructive"
      )}
    >
      {options.map((opt) => (
        <button
          key={opt.value}
          type="button"
          onClick={() => onChange(opt.value)}
          className={cn(
            "flex-1 rounded-xl py-2.5 text-sm font-medium transition-all duration-200",
            value === opt.value
              ? "bg-primary text-primary-foreground shadow-sm"
              : "text-muted-foreground hover:text-foreground"
          )}
        >
          {opt.label}
        </button>
      ))}
    </div>
  )
}

// ---------------------------------------------------------------------------
// Component
// ---------------------------------------------------------------------------

const ROLE_OPTIONS: { label: string; value: UserRole }[] = [
  { label: "🚗  Driver", value: "DRIVER" },
  { label: "🧑  Rider", value: "RIDER" },
]

const GENDER_OPTIONS: { label: string; value: Gender }[] = [
  { label: "Male", value: "MALE" },
  { label: "Female", value: "FEMALE" },
  { label: "Other", value: "OTHER" },
]

export function SignupPage({
  onSignup,
  onLoginClick,
  onTermsClick,
  onPrivacyClick,
}: SignupPageProps) {
  const [form, setForm] = useState<SignupFormData>({
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    password: "",
    role: "PASSENGER",
    gender: "MALE",
    agreedToTerms: false,
  })
  const [errors, setErrors] = useState<FieldErrors>({})
  const [showPassword, setShowPassword] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [serverError, setServerError] = useState<string | null>(null)

  function updateField<K extends keyof SignupFormData>(key: K, value: SignupFormData[K]) {
    setForm((prev) => ({ ...prev, [key]: value }))
    if (errors[key]) setErrors((prev) => ({ ...prev, [key]: undefined }))
    setServerError(null)
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    const fieldErrors = validate(form)
    if (Object.keys(fieldErrors).length > 0) {
      setErrors(fieldErrors)
      return
    }
    if (!onSignup) return
    setIsLoading(true)
    setServerError(null)
    try {
      await onSignup(form)
    } catch (err: unknown) {
      setServerError(
        err instanceof Error ? err.message : "Something went wrong. Please try again."
      )
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="relative flex h-full flex-col bg-background">
      {/* ── Scrollable content ── */}
      <div className="flex flex-1 flex-col overflow-y-auto px-6 pb-4 pt-12">

        {/* Logo + heading */}
        <div className="mb-6 flex flex-col items-center gap-4">
          <div className="size-20 overflow-hidden rounded-full ring-2 ring-border shadow-xl">
            <img src={logo} alt="Swafy logo" className="size-full object-cover" />
          </div>
          <div className="text-center">
            <h1 className="text-2xl font-bold tracking-tight text-foreground">
              Create Account
            </h1>
            <p className="mt-1.5 text-sm text-muted-foreground">
              Join Swafy for effortless urban travel.
            </p>
          </div>
        </div>

        {/* ── Form ── */}
        <form onSubmit={handleSubmit} noValidate className="flex flex-col gap-3">

          {/* Server error */}
          {serverError && (
            <div
              role="alert"
              className="rounded-xl border border-destructive/40 bg-destructive/10 px-4 py-3 text-sm text-destructive"
            >
              {serverError}
            </div>
          )}

          {/* First + Last name — side by side */}
          <div className="flex gap-3">
            <div className="flex flex-1 flex-col gap-1.5">
              <Label htmlFor="firstName" className="sr-only">First Name</Label>
              <Input
                id="firstName"
                type="text"
                placeholder="First Name"
                autoComplete="given-name"
                value={form.firstName}
                onChange={(e) => updateField("firstName", e.target.value)}
                aria-invalid={!!errors.firstName}
                className={cn(
                  "h-14 rounded-2xl bg-card px-4 text-base placeholder:text-muted-foreground",
                  errors.firstName && "border-destructive"
                )}
              />
              {errors.firstName && (
                <p className="px-1 text-xs text-destructive">{errors.firstName}</p>
              )}
            </div>
            <div className="flex flex-1 flex-col gap-1.5">
              <Label htmlFor="lastName" className="sr-only">Last Name</Label>
              <Input
                id="lastName"
                type="text"
                placeholder="Last Name"
                autoComplete="family-name"
                value={form.lastName}
                onChange={(e) => updateField("lastName", e.target.value)}
                aria-invalid={!!errors.lastName}
                className={cn(
                  "h-14 rounded-2xl bg-card px-4 text-base placeholder:text-muted-foreground",
                  errors.lastName && "border-destructive"
                )}
              />
              {errors.lastName && (
                <p className="px-1 text-xs text-destructive">{errors.lastName}</p>
              )}
            </div>
          </div>

          {/* Email */}
          <div className="flex flex-col gap-1.5">
            <Label htmlFor="email" className="sr-only">Email Address</Label>
            <Input
              id="email"
              type="email"
              placeholder="Email Address"
              autoComplete="email"
              value={form.email}
              onChange={(e) => updateField("email", e.target.value)}
              aria-invalid={!!errors.email}
              className={cn(
                "h-14 rounded-2xl bg-card px-5 text-base placeholder:text-muted-foreground",
                errors.email && "border-destructive"
              )}
            />
            {errors.email && (
              <p className="px-1 text-xs text-destructive">{errors.email}</p>
            )}
          </div>

          {/* Phone */}
          <div className="flex flex-col gap-1.5">
            <Label htmlFor="phoneNumber" className="sr-only">Phone Number</Label>
            <Input
              id="phoneNumber"
              type="tel"
              placeholder="Phone Number"
              autoComplete="tel"
              value={form.phoneNumber}
              onChange={(e) => updateField("phoneNumber", e.target.value)}
              aria-invalid={!!errors.phoneNumber}
              className={cn(
                "h-14 rounded-2xl bg-card px-5 text-base placeholder:text-muted-foreground",
                errors.phoneNumber && "border-destructive"
              )}
            />
            {errors.phoneNumber && (
              <p className="px-1 text-xs text-destructive">{errors.phoneNumber}</p>
            )}
          </div>

          {/* Password */}
          <div className="flex flex-col gap-1.5">
            <Label htmlFor="password" className="sr-only">Password</Label>
            <div className="relative">
              <Input
                id="password"
                type={showPassword ? "text" : "password"}
                placeholder="Password"
                autoComplete="new-password"
                value={form.password}
                onChange={(e) => updateField("password", e.target.value)}
                aria-invalid={!!errors.password}
                className={cn(
                  "h-14 rounded-2xl bg-card px-5 pr-12 text-base placeholder:text-muted-foreground",
                  errors.password && "border-destructive"
                )}
              />
              <button
                type="button"
                aria-label={showPassword ? "Hide password" : "Show password"}
                onClick={() => setShowPassword((v) => !v)}
                className="absolute inset-y-0 right-0 flex items-center px-4 text-muted-foreground transition-colors hover:text-foreground"
              >
                {showPassword ? <EyeOff className="size-5" /> : <Eye className="size-5" />}
              </button>
            </div>
            {errors.password && (
              <p className="px-1 text-xs text-destructive">{errors.password}</p>
            )}
          </div>

          {/* Role */}
          <div className="flex flex-col gap-1.5">
            <p className="px-1 text-xs font-medium text-muted-foreground">I am a…</p>
            <SegmentedControl
              options={ROLE_OPTIONS}
              value={form.role}
              onChange={(v) => updateField("role", v)}
              hasError={!!errors.role}
            />
            {errors.role && (
              <p className="px-1 text-xs text-destructive">{errors.role}</p>
            )}
          </div>

          {/* Gender */}
          <div className="flex flex-col gap-1.5">
            <p className="px-1 text-xs font-medium text-muted-foreground">Gender</p>
            <SegmentedControl
              options={GENDER_OPTIONS}
              value={form.gender}
              onChange={(v) => updateField("gender", v)}
              hasError={!!errors.gender}
            />
            {errors.gender && (
              <p className="px-1 text-xs text-destructive">{errors.gender}</p>
            )}
          </div>

          {/* Terms */}
          <div className="flex flex-col gap-1">
            <div className="flex items-start gap-3 py-1">
              <Checkbox
                id="terms"
                checked={form.agreedToTerms}
                onCheckedChange={(checked) => updateField("agreedToTerms", checked === true)}
                aria-invalid={!!errors.agreedToTerms}
                className="mt-0.5 shrink-0"
              />
              <Label
                htmlFor="terms"
                className="cursor-pointer text-sm leading-snug text-muted-foreground"
              >
                <span>
                  {"I agree to the "}
                  <button
                    type="button"
                    onClick={onTermsClick}
                    className="inline text-primary underline-offset-2 hover:underline"
                  >
                    Terms and Conditions
                  </button>
                  {" and "}
                  <button
                    type="button"
                    onClick={onPrivacyClick}
                    className="inline text-primary underline-offset-2 hover:underline"
                  >
                    Privacy Policy
                  </button>
                  {"."}
                </span>
              </Label>
            </div>
            {errors.agreedToTerms && (
              <p className="px-1 text-xs text-destructive">{errors.agreedToTerms}</p>
            )}
          </div>

          {/* Submit */}
          <Button
            type="submit"
            id="signup-submit"
            disabled={isLoading}
            className="mt-2 h-14 w-full rounded-2xl text-base font-semibold tracking-wide"
          >
            {isLoading ? (
              <>
                <Loader2 data-icon="inline-start" className="animate-spin" />
                Creating account…
              </>
            ) : (
              "Create Account"
            )}
          </Button>
        </form>
      </div>

      {/* ── Login link — pinned to bottom ── */}
      <div className="px-6 pb-6 pt-3 text-center">
        <p className="text-sm text-muted-foreground">
          Already have an account?{" "}
          <button
            type="button"
            id="goto-login"
            onClick={onLoginClick}
            className="font-semibold text-primary underline-offset-2 hover:underline"
          >
            Log In
          </button>
        </p>
      </div>
    </div>
  )
}

export default SignupPage
