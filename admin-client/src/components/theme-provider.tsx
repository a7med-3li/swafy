import * as React from "react"

type Theme = "dark" | "light" | "system"

type ThemeProviderState = {
  theme: Theme
  setTheme: (theme: Theme) => void
}

const ThemeProviderContext = React.createContext<ThemeProviderState | undefined>(undefined)

export function ThemeProvider({
  children,
  defaultTheme = "dark",
  storageKey = "theme",
}: {
  children: React.ReactNode
  defaultTheme?: Theme
  storageKey?: string
}) {
  const [theme, setThemeState] = React.useState<Theme>(() => {
    const stored = localStorage.getItem(storageKey)
    if (stored === "dark" || stored === "light" || stored === "system") return stored
    return defaultTheme
  })

  const setTheme = React.useCallback(
    (next: Theme) => {
      localStorage.setItem(storageKey, next)
      setThemeState(next)
    },
    [storageKey]
  )

  React.useEffect(() => {
    const root = document.documentElement
    const resolved = theme === "system"
      ? window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light"
      : theme
    root.classList.remove("light", "dark")
    root.classList.add(resolved)
  }, [theme])

  return (
    <ThemeProviderContext.Provider value={{ theme, setTheme }}>
      {children}
    </ThemeProviderContext.Provider>
  )
}

export function useTheme() {
  const ctx = React.useContext(ThemeProviderContext)
  if (!ctx) throw new Error("useTheme must be used within ThemeProvider")
  return ctx
}
