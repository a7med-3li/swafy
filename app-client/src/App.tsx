import { useEffect, useState } from "react"
import { SignupPage } from "@/components/SignupPage"
import { WelcomePage } from "@/components/WelcomePage"
import type { SignupFormData } from "@/components/SignupPage"
import { registerUser } from "@/lib/auth"

// Simple screen state — extend this when you add routing (React Router, etc.)
type Screen = "signup" | "welcome"

export function App() {
  const [screen, setScreen] = useState<Screen>("signup")
  const [userName, setUserName] = useState<string>("")

  // Force dark mode on mount
  useEffect(() => {
    document.documentElement.classList.add("dark")
  }, [])

  /**
   * Calls POST /api/v1/auth/register.
   * Maps SignupFormData → UserRegistrationRequest.
   * On success  → transitions to the Welcome screen.
   * On failure  → re-throws so SignupPage can show the inline error banner.
   */
  async function handleSignup(data: SignupFormData) {
    await registerUser({
      firstName:   data.firstName,
      lastName:    data.lastName,
      phoneNumber: data.phoneNumber,
      email:       data.email,
      password:    data.password,
      role:        data.role,
      gender:      data.gender,
    })
    setUserName(`${data.firstName} ${data.lastName}`)
    setScreen("welcome")
  }

  function handleLoginClick() {
    // TODO: navigate("/login")
  }

  function handleTermsClick() {
    // TODO: navigate("/terms")
  }

  function handlePrivacyClick() {
    // TODO: navigate("/privacy")
  }

  function handleContinue() {
    // TODO: navigate("/home") or wherever the main app entry is
    console.log("Navigating to main app…")
  }

  // Render the correct screen inside the shared phone frame
  function renderScreen() {
    switch (screen) {
      case "welcome":
        return (
          <WelcomePage
            userName={userName}
            onContinue={handleContinue}
          />
        )
      default:
        return (
          <SignupPage
            onSignup={handleSignup}
            onLoginClick={handleLoginClick}
            onTermsClick={handleTermsClick}
            onPrivacyClick={handlePrivacyClick}
          />
        )
    }
  }

  return (
    <div
      className="flex min-h-svh items-center justify-center bg-[oklch(0.10_0_0)]"
      style={{
        backgroundImage: `radial-gradient(circle at 50% 0%, oklch(0.20 0.04 220 / 0.4) 0%, transparent 70%)`,
      }}
    >
      {/* Phone frame — 390 × 844 (iPhone 14 dimensions) */}
      <div
        className="relative w-[390px] overflow-hidden rounded-[2.5rem] bg-background shadow-2xl ring-1 ring-white/10"
        style={{ height: "844px" }}
      >
        {/* Notch indicator */}
        <div className="absolute inset-x-0 top-0 z-10 flex justify-center pt-3.5">
          <div className="h-[5px] w-[120px] rounded-full bg-white/10" />
        </div>

        {renderScreen()}
      </div>
    </div>
  )
}

export default App
