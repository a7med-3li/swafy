import { useEffect, useRef } from "react"
import { Button } from "@/components/ui/button"
import logo from "/assets/logo.jpg"

// ---------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------

export interface WelcomePageProps {
  /** First name (or full name) to greet the user with */
  userName?: string
  /** Called when the user taps "Get Started" */
  onContinue?: () => void
}

// ---------------------------------------------------------------------------
// Confetti particle — pure CSS-driven, no library needed
// ---------------------------------------------------------------------------

interface Particle {
  x: number      // % from left
  delay: number  // s
  duration: number // s
  size: number   // px
  color: string
  rotation: number
  drift: number  // px drift left/right
}

const COLORS = [
  "oklch(0.58 0.13 220)",   // Swafy teal
  "oklch(0.68 0.18 45)",    // Swafy orange
  "oklch(0.75 0.15 290)",   // violet
  "oklch(0.80 0.12 160)",   // mint
  "oklch(0.90 0.10 60)",    // yellow
]

function makeParticles(count: number): Particle[] {
  return Array.from({ length: count }, (_, i) => ({
    x: Math.random() * 100,
    delay: Math.random() * 1.2,
    duration: 1.6 + Math.random() * 1.6,
    size: 5 + Math.random() * 7,
    color: COLORS[i % COLORS.length],
    rotation: Math.random() * 720 - 360,
    drift: (Math.random() - 0.5) * 80,
  }))
}

const PARTICLES = makeParticles(40)

// ---------------------------------------------------------------------------
// Component
// ---------------------------------------------------------------------------

export function WelcomePage({ userName, onContinue }: WelcomePageProps) {
  const firstName = userName?.split(" ")[0] ?? "there"

  // Trigger CSS animation replay on mount
  const checkRef = useRef<SVGCircleElement>(null)
  useEffect(() => {
    const el = checkRef.current
    if (!el) return
    el.style.animation = "none"
    void el.getBoundingClientRect()
    el.style.animation = ""
  }, [])

  return (
    <div className="relative flex h-full flex-col items-center overflow-hidden bg-background px-6 pb-8 pt-14">
      {/* ── Confetti particles ── */}
      <div aria-hidden className="pointer-events-none absolute inset-0 overflow-hidden">
        {PARTICLES.map((p, i) => (
          <span
            key={i}
            className="absolute top-0 block rounded-sm opacity-0"
            style={{
              left: `${p.x}%`,
              width: p.size,
              height: p.size * (Math.random() > 0.5 ? 1 : 0.45),
              background: p.color,
              animationName: "confettiFall",
              animationDuration: `${p.duration}s`,
              animationDelay: `${p.delay}s`,
              animationTimingFunction: "ease-in",
              animationFillMode: "forwards",
              "--drift": `${p.drift}px`,
              "--rotate": `${p.rotation}deg`,
            } as React.CSSProperties}
          />
        ))}
      </div>

      {/* ── Top: logo + check ring ── */}
      <div className="flex flex-col items-center gap-6">
        {/* Swafy logo */}
        <div
          className="size-20 overflow-hidden rounded-full ring-2 ring-border shadow-lg"
          style={{ animation: "fadeSlideDown 0.5s ease both" }}
        >
          <img src={logo} alt="Swafy logo" className="size-full object-cover" />
        </div>

        {/* Animated checkmark circle */}
        <div
          className="relative flex items-center justify-center"
          style={{ animation: "scaleIn 0.5s 0.2s cubic-bezier(0.34,1.56,0.64,1) both" }}
        >
          {/* Glow ring */}
          <span
            className="absolute rounded-full"
            style={{
              inset: -8,
              background: "oklch(0.58 0.13 220 / 0.15)",
              animation: "pulseRing 2s 0.8s ease-out infinite",
            }}
          />

          <svg width="100" height="100" viewBox="0 0 100 100" fill="none">
            {/* Track */}
            <circle cx="50" cy="50" r="44" stroke="oklch(1 0 0 / 0.06)" strokeWidth="5" />
            {/* Animated fill ring */}
            <circle
              ref={checkRef}
              cx="50"
              cy="50"
              r="44"
              stroke="oklch(0.58 0.13 220)"
              strokeWidth="5"
              strokeLinecap="round"
              strokeDasharray="276.46"
              strokeDashoffset="276.46"
              transform="rotate(-90 50 50)"
              style={{ animation: "drawCircle 0.7s 0.35s ease forwards" }}
            />
            {/* Checkmark */}
            <polyline
              points="30,52 44,66 70,36"
              stroke="oklch(0.58 0.13 220)"
              strokeWidth="5.5"
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeDasharray="60"
              strokeDashoffset="60"
              style={{ animation: "drawCheck 0.4s 0.9s ease forwards" }}
            />
          </svg>
        </div>

        {/* Text */}
        <div className="flex flex-col items-center gap-2 text-center">
          <h1
            className="text-3xl font-bold tracking-tight text-foreground"
            style={{ animation: "fadeSlideUp 0.5s 1s ease both" }}
          >
            Welcome,{" "}
            <span className="text-primary">{firstName}!</span>
          </h1>
          <p
            className="max-w-[260px] text-sm leading-relaxed text-muted-foreground"
            style={{ animation: "fadeSlideUp 0.5s 1.15s ease both" }}
          >
            Your account is ready. Start exploring effortless urban travel with Swafy.
          </p>
        </div>

        {/* Feature pills */}
        <div
          className="flex flex-wrap justify-center gap-2"
          style={{ animation: "fadeSlideUp 0.5s 1.3s ease both" }}
        >
          {["🚗 Ride", "📍 Track", "💳 Pay", "⭐ Rate"].map((label) => (
            <span
              key={label}
              className="rounded-full border border-border bg-card px-3.5 py-1.5 text-xs font-medium text-foreground"
            >
              {label}
            </span>
          ))}
        </div>
      </div>

      {/* ── Bottom: CTA ── */}
      <div
        className="mt-auto w-full"
        style={{ animation: "fadeSlideUp 0.5s 1.45s ease both" }}
      >
        <Button
          id="welcome-continue"
          onClick={onContinue}
          className="h-14 w-full rounded-2xl text-base font-semibold tracking-wide"
        >
          Get Started
        </Button>
        <p className="mt-3 text-center text-xs text-muted-foreground">
          You can always update your profile later.
        </p>
      </div>
    </div>
  )
}

export default WelcomePage
