import { useEffect, useState } from "react"
import { get } from "@/lib/api"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from "@/components/ui/select"
import { Ticket, DollarSign } from "lucide-react"
import type { SubscriptionSalesItem, SubscriptionSalesStats } from "@/lib/types"

const statusBadge: Record<string, "success" | "warning" | "destructive" | "outline"> = {
  ACTIVE: "success",
  EXPIRED: "outline",
  CANCELLED: "destructive",
  SUSPENDED: "warning",
}

export function SubscriptionsPage() {
  const [subs, setSubs] = useState<SubscriptionSalesItem[]>([])
  const [stats, setStats] = useState<SubscriptionSalesStats | null>(null)
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState("")

  useEffect(() => {
    const params = filter ? `?status=${filter}` : ""
    Promise.all([
      get<SubscriptionSalesItem[]>(`/api/v1/admin/subscriptions${params}`),
      get<SubscriptionSalesStats>("/api/v1/admin/subscriptions/stats"),
    ])
      .then(([s, st]) => { setSubs(s); setStats(st) })
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [filter])

  if (loading) {
    return <div className="text-center text-muted-foreground py-8">Loading...</div>
  }

  return (
    <div className="space-y-4">
      <h1 className="text-2xl font-bold">Subscription Sales</h1>

      {stats && (
        <div className="grid grid-cols-2 gap-4 sm:grid-cols-4">
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-xs text-muted-foreground">Active</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex items-center gap-2 text-lg font-bold">
                <Ticket className="size-4 text-emerald-400" />
                {stats.totalActive}
              </div>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-xs text-muted-foreground">Expired</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-lg font-bold">{stats.totalExpired}</div>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-xs text-muted-foreground">Cancelled</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-lg font-bold">{stats.totalCancelled}</div>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-xs text-muted-foreground">Total Revenue</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex items-center gap-2 text-lg font-bold">
                <DollarSign className="size-4 text-yellow-400" />
                {stats.totalRevenue} EGP
              </div>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-xs text-muted-foreground">Student Basic</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-lg font-bold">{stats.studentBasicCount}</div>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-xs text-muted-foreground">Student Plus</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-lg font-bold">{stats.studentPlusCount}</div>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-xs text-muted-foreground">Corporate Commuter</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-lg font-bold">{stats.corporateCommuterCount}</div>
            </CardContent>
          </Card>
        </div>
      )}

      <div className="w-40">
        <Select value={filter} onValueChange={setFilter}>
          <SelectTrigger>
            <SelectValue placeholder="All Statuses" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">All Statuses</SelectItem>
            <SelectItem value="ACTIVE">Active</SelectItem>
            <SelectItem value="EXPIRED">Expired</SelectItem>
            <SelectItem value="CANCELLED">Cancelled</SelectItem>
            <SelectItem value="SUSPENDED">Suspended</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-sm text-muted-foreground">
            {subs.length} subscription{subs.length !== 1 ? "s" : ""}
          </CardTitle>
        </CardHeader>
        <CardContent className="p-0">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-border">
                  <th className="px-4 py-2 text-left font-medium text-muted-foreground">ID</th>
                  <th className="px-4 py-2 text-left font-medium text-muted-foreground">Passenger</th>
                  <th className="px-4 py-2 text-left font-medium text-muted-foreground">Plan</th>
                  <th className="px-4 py-2 text-left font-medium text-muted-foreground">Rides</th>
                  <th className="px-4 py-2 text-left font-medium text-muted-foreground">Price</th>
                  <th className="px-4 py-2 text-left font-medium text-muted-foreground">Period</th>
                  <th className="px-4 py-2 text-left font-medium text-muted-foreground">Status</th>
                  <th className="px-4 py-2 text-left font-medium text-muted-foreground">Auto-Renew</th>
                </tr>
              </thead>
              <tbody>
                {subs.map((s) => (
                  <tr key={s.id} className="border-b border-border hover:bg-muted/50">
                    <td className="px-4 py-2 font-mono text-xs">{s.id}</td>
                    <td className="px-4 py-2 font-mono text-xs">{s.passengerId.slice(0, 8)}...</td>
                    <td className="px-4 py-2">
                      <Badge variant="outline">{s.plan}</Badge>
                    </td>
                    <td className="px-4 py-2">
                      {s.remainingRides}/{s.totalRides}
                    </td>
                    <td className="px-4 py-2">{s.price} EGP</td>
                    <td className="px-4 py-2 text-xs">
                      {s.startDate} – {s.endDate}
                    </td>
                    <td className="px-4 py-2">
                      <Badge variant={statusBadge[s.status] || "outline"}>{s.status}</Badge>
                    </td>
                    <td className="px-4 py-2">{s.autoRenew ? "Yes" : "No"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
