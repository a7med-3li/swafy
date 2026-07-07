import { NavLink } from "react-router-dom"
import {
  LayoutDashboard,
  Bus,
  Users,
  Banknote,
  Ticket,
  AlertTriangle,
  Route,
  LogOut,
} from "lucide-react"
import { clearToken } from "@/lib/api"
import { useNavigate } from "react-router-dom"

const navItems = [
  { to: "/", icon: LayoutDashboard, label: "Dashboard" },
  { to: "/rides", icon: Bus, label: "Rides" },
  { to: "/drivers", icon: Users, label: "Drivers" },
  { to: "/deposits", icon: Banknote, label: "Deposits" },
  { to: "/subscriptions", icon: Ticket, label: "Subscriptions" },
  { to: "/no-shows", icon: AlertTriangle, label: "No-Shows" },
  { to: "/corridors", icon: Route, label: "Corridors" },
]

export function Sidebar() {
  const navigate = useNavigate()

  const handleLogout = () => {
    clearToken()
    navigate("/login")
  }

  return (
    <aside className="flex h-screen w-56 flex-col border-r border-border bg-sidebar">
      <div className="flex h-14 items-center gap-2 border-b border-border px-4">
        <Bus className="size-5 text-primary" />
        <span className="text-lg font-bold text-sidebar-foreground">Swafy Admin</span>
      </div>
      <nav className="flex-1 space-y-1 p-3">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            end={item.to === "/"}
            className={({ isActive }) =>
              `flex items-center gap-2.5 rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
                isActive
                  ? "bg-sidebar-accent text-sidebar-accent-foreground"
                  : "text-sidebar-foreground/70 hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
              }`
            }
          >
            <item.icon className="size-4" />
            {item.label}
          </NavLink>
        ))}
      </nav>
      <div className="border-t border-border p-3">
        <button
          onClick={handleLogout}
          className="flex w-full items-center gap-2.5 rounded-lg px-3 py-2 text-sm font-medium text-sidebar-foreground/70 transition-colors hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
        >
          <LogOut className="size-4" />
          Logout
        </button>
      </div>
    </aside>
  )
}
