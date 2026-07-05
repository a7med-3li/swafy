import { useEffect, useState } from "react"
import { get } from "@/lib/api"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from "@/components/ui/select"
import type { RideAdminItem } from "@/lib/types"

const statusColors: Record<string, "success" | "warning" | "default" | "destructive" | "outline"> = {
  BOOKED: "warning",
  IN_PROGRESS: "default",
  COMPLETED: "success",
  CANCELLED: "destructive",
  NO_SHOW: "destructive",
}

export function RidesPage() {
  const [rides, setRides] = useState<RideAdminItem[]>([])
  const [loading, setLoading] = useState(true)
  const [statusFilter, setStatusFilter] = useState("")
  const [passengerFilter, setPassengerFilter] = useState("")

  useEffect(() => {
    const params = new URLSearchParams()
    if (statusFilter) params.set("status", statusFilter)
    if (passengerFilter) params.set("passengerId", passengerFilter)

    const qs = params.toString()
    get<RideAdminItem[]>(`/api/v1/admin/rides${qs ? `?${qs}` : ""}`)
      .then(setRides)
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [statusFilter, passengerFilter])

  return (
    <div className="space-y-4">
      <h1 className="text-2xl font-bold">Ride Monitoring</h1>

      <div className="flex gap-3">
        <div className="w-40">
          <Select value={statusFilter} onValueChange={(v) => { setStatusFilter(v); setLoading(true) }}>
            <SelectTrigger>
              <SelectValue placeholder="All Statuses" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="">All Statuses</SelectItem>
              <SelectItem value="BOOKED">Booked</SelectItem>
              <SelectItem value="IN_PROGRESS">In Progress</SelectItem>
              <SelectItem value="COMPLETED">Completed</SelectItem>
              <SelectItem value="CANCELLED">Cancelled</SelectItem>
              <SelectItem value="NO_SHOW">No-Show</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <Input
          placeholder="Filter by Passenger ID..."
          className="w-64"
          value={passengerFilter}
          onChange={(e) => { setPassengerFilter(e.target.value); setLoading(true) }}
        />
      </div>

      {loading ? (
        <div className="text-center text-muted-foreground py-8">Loading...</div>
      ) : (
        <Card>
          <CardHeader>
            <CardTitle className="text-sm text-muted-foreground">
              {rides.length} ride{rides.length !== 1 ? "s" : ""}
            </CardTitle>
          </CardHeader>
          <CardContent className="p-0">
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">ID</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Passenger</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Driver</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Status</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">PIN</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Departed</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Requested</th>
                  </tr>
                </thead>
                <tbody>
                  {rides.map((ride) => (
                    <tr key={ride.id} className="border-b border-border hover:bg-muted/50">
                      <td className="px-4 py-2 font-mono text-xs">{ride.id.slice(0, 8)}...</td>
                      <td className="px-4 py-2 font-mono text-xs">{ride.passengerId.slice(0, 8)}...</td>
                      <td className="px-4 py-2 font-mono text-xs">
                        {ride.driverId ? `${ride.driverId.slice(0, 8)}...` : "—"}
                      </td>
                      <td className="px-4 py-2">
                        <Badge variant={statusColors[ride.status] || "outline"}>
                          {ride.status}
                        </Badge>
                      </td>
                      <td className="px-4 py-2 font-mono">{ride.pin || "—"}</td>
                      <td className="px-4 py-2 text-xs">
                        {ride.departureTime ? new Date(ride.departureTime).toLocaleString() : "—"}
                      </td>
                      <td className="px-4 py-2 text-xs">
                        {ride.requestedAt ? new Date(ride.requestedAt).toLocaleString() : "—"}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
