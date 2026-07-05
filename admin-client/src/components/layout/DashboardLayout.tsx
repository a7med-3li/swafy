import { Outlet } from "react-router-dom"
import { Sidebar } from "./Sidebar"

export function DashboardLayout() {
  return (
    <div className="flex h-screen">
      <Sidebar />
      <main className="flex-1 overflow-auto bg-background p-6">
        <Outlet />
      </main>
    </div>
  )
}
