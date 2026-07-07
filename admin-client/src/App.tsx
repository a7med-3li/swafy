import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"
import { DashboardLayout } from "@/components/layout/DashboardLayout"
import { LoginPage } from "@/pages/LoginPage"
import { DashboardPage } from "@/pages/DashboardPage"
import { RidesPage } from "@/pages/RidesPage"
import { DriversPage } from "@/pages/DriversPage"
import { DepositsPage } from "@/pages/DepositsPage"
import { SubscriptionsPage } from "@/pages/SubscriptionsPage"
import { NoShowsPage } from "@/pages/NoShowsPage"
import { CorridorsPage } from "@/pages/CorridorsPage"
import { isAuthenticated } from "@/lib/api"

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  if (!isAuthenticated()) {
    return <Navigate to="/login" replace />
  }
  return <>{children}</>
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          element={
            <ProtectedRoute>
              <DashboardLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<DashboardPage />} />
          <Route path="rides" element={<RidesPage />} />
          <Route path="drivers" element={<DriversPage />} />
          <Route path="deposits" element={<DepositsPage />} />
          <Route path="subscriptions" element={<SubscriptionsPage />} />
          <Route path="no-shows" element={<NoShowsPage />} />
          <Route path="corridors" element={<CorridorsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}
