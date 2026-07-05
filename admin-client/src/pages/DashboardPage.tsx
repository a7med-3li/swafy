import { useEffect, useState } from "react"
import { get } from "@/lib/api"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import {
  Users,
  Bus,
  UserCheck,
  Clock,
  DollarSign,
  Ticket,
  AlertTriangle,
  CheckCircle,
} from "lucide-react"
import type { AdminStats } from "@/lib/types"

export function DashboardPage() {
  const [stats, setStats] = useState<AdminStats | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    get<AdminStats>("/api/v1/admin/stats")
      .then(setStats)
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [])

  if (loading) {
    return <div className="flex items-center justify-center h-64 text-muted-foreground">Loading...</div>
  }

  if (!stats) {
    return <div className="text-destructive">Failed to load stats</div>
  }

  const statCards = [
    { title: "Total Users", value: stats.totalUsers, icon: Users, color: "text-blue-400" },
    { title: "Drivers", value: stats.totalDrivers, icon: UserCheck, color: "text-emerald-400" },
    { title: "Approved Drivers", value: stats.approvedDrivers, icon: CheckCircle, color: "text-emerald-400" },
    { title: "Pending Drivers", value: stats.pendingDrivers, icon: Clock, color: "text-amber-400" },
    { title: "Rides Today", value: stats.totalRidesToday, icon: Bus, color: "text-cyan-400" },
    { title: "Active Subscriptions", value: stats.activeSubscriptions, icon: Ticket, color: "text-violet-400" },
    { title: "Total Revenue", value: `${stats.totalSubscriptionRevenue} EGP`, icon: DollarSign, color: "text-yellow-400" },
    { title: "No-Shows", value: stats.totalRidesNoShow, icon: AlertTriangle, color: "text-red-400" },
  ]

  const rideBreakdown = [
    { label: "Booked", value: stats.totalRidesBooked, color: "bg-blue-500" },
    { label: "In Progress", value: stats.totalRidesInProgress, color: "bg-amber-500" },
    { label: "Completed", value: stats.totalRidesCompleted, color: "bg-emerald-500" },
    { label: "Cancelled", value: stats.totalRidesCancelled, color: "bg-gray-500" },
    { label: "No-Show", value: stats.totalRidesNoShow, color: "bg-red-500" },
  ]

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Dashboard</h1>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {statCards.map((card) => (
          <Card key={card.title}>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                {card.title}
              </CardTitle>
              <card.icon className={`size-4 ${card.color}`} />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{card.value}</div>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-sm text-muted-foreground">Ride Status Breakdown</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex flex-wrap gap-2">
            {rideBreakdown.map((item) => (
              <Badge
                key={item.label}
                variant="outline"
                className="flex items-center gap-1.5"
              >
                <span className={`size-2 rounded-full ${item.color}`} />
                {item.label}: {item.value}
              </Badge>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
