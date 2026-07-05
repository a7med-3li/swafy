import { useEffect, useState } from "react"
import { get } from "@/lib/api"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import type { NoShowPassengerItem, RideAdminItem } from "@/lib/types"

export function NoShowsPage() {
  const [rides, setRides] = useState<RideAdminItem[]>([])
  const [passengers, setPassengers] = useState<NoShowPassengerItem[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      get<RideAdminItem[]>("/api/v1/admin/no-shows/rides"),
      get<NoShowPassengerItem[]>("/api/v1/admin/no-shows/passengers"),
    ])
      .then(([r, p]) => { setRides(r); setPassengers(p) })
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [])

  if (loading) {
    return <div className="text-center text-muted-foreground py-8">Loading...</div>
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">No-Show Tracking</h1>

      <div>
        <h2 className="mb-3 text-lg font-semibold">No-Show Rides ({rides.length})</h2>
        {rides.length === 0 ? (
          <p className="text-sm text-muted-foreground">No no-show rides.</p>
        ) : (
          <Card>
            <CardContent className="p-0">
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="px-4 py-2 text-left font-medium text-muted-foreground">Ride ID</th>
                      <th className="px-4 py-2 text-left font-medium text-muted-foreground">Passenger</th>
                      <th className="px-4 py-2 text-left font-medium text-muted-foreground">Driver</th>
                      <th className="px-4 py-2 text-left font-medium text-muted-foreground">Status</th>
                      <th className="px-4 py-2 text-left font-medium text-muted-foreground">Departed</th>
                      <th className="px-4 py-2 text-left font-medium text-muted-foreground">Marked No-Show</th>
                    </tr>
                  </thead>
                  <tbody>
                    {rides.map((r) => (
                      <tr key={r.id} className="border-b border-border hover:bg-muted/50">
                        <td className="px-4 py-2 font-mono text-xs">{r.id.slice(0, 8)}...</td>
                        <td className="px-4 py-2 font-mono text-xs">{r.passengerId.slice(0, 8)}...</td>
                        <td className="px-4 py-2 font-mono text-xs">
                          {r.driverId ? `${r.driverId.slice(0, 8)}...` : "—"}
                        </td>
                        <td className="px-4 py-2">
                          <Badge variant="destructive">NO_SHOW</Badge>
                        </td>
                        <td className="px-4 py-2 text-xs">
                          {r.departureTime ? new Date(r.departureTime).toLocaleString() : "—"}
                        </td>
                        <td className="px-4 py-2 text-xs">
                          {r.noShowMarkedAt ? new Date(r.noShowMarkedAt).toLocaleString() : "—"}
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

      <Separator />

      <div>
        <h2 className="mb-3 text-lg font-semibold">Passenger No-Show Ranking</h2>
        {passengers.length === 0 ? (
          <p className="text-sm text-muted-foreground">No passengers with no-shows.</p>
        ) : (
          <Card>
            <CardContent className="p-0">
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="px-4 py-2 text-left font-medium text-muted-foreground">#</th>
                      <th className="px-4 py-2 text-left font-medium text-muted-foreground">Passenger</th>
                      <th className="px-4 py-2 text-left font-medium text-muted-foreground">No-Shows</th>
                    </tr>
                  </thead>
                  <tbody>
                    {passengers.map((p, i) => (
                      <tr key={p.passengerId} className="border-b border-border hover:bg-muted/50">
                        <td className="px-4 py-2">{i + 1}</td>
                        <td className="px-4 py-2">{p.passengerName}</td>
                        <td className="px-4 py-2">
                          <Badge variant={p.noShowCount >= 5 ? "destructive" : p.noShowCount >= 3 ? "warning" : "outline"}>
                            {p.noShowCount}
                          </Badge>
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
    </div>
  )
}
