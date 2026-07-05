import { useEffect, useState } from "react"
import { get, post } from "@/lib/api"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from "@/components/ui/select"
import type { DriverAdminItem } from "@/lib/types"

const statusBadge: Record<string, "success" | "warning" | "destructive" | "outline"> = {
  APPROVED: "success",
  PENDING: "warning",
  REJECTED: "destructive",
}

export function DriversPage() {
  const [drivers, setDrivers] = useState<DriverAdminItem[]>([])
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState("")

  const fetchDrivers = () => {
    setLoading(true)
    const params = filter ? `?approvalStatus=${filter}` : ""
    get<DriverAdminItem[]>(`/api/v1/admin/drivers${params}`)
      .then(setDrivers)
      .catch(console.error)
      .finally(() => setLoading(false))
  }

  useEffect(() => { fetchDrivers() }, [filter])

  const handleApprove = async (id: string) => {
    await post(`/api/v1/admin/drivers/${id}/approve`)
    fetchDrivers()
  }

  const handleReject = async (id: string) => {
    await post(`/api/v1/admin/drivers/${id}/reject`)
    fetchDrivers()
  }

  return (
    <div className="space-y-4">
      <h1 className="text-2xl font-bold">Driver Management</h1>

      <div className="w-40">
        <Select value={filter} onValueChange={setFilter}>
          <SelectTrigger>
            <SelectValue placeholder="All Drivers" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">All Drivers</SelectItem>
            <SelectItem value="PENDING">Pending</SelectItem>
            <SelectItem value="APPROVED">Approved</SelectItem>
            <SelectItem value="REJECTED">Rejected</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {loading ? (
        <div className="text-center text-muted-foreground py-8">Loading...</div>
      ) : (
        <Card>
          <CardHeader>
            <CardTitle className="text-sm text-muted-foreground">
              {drivers.length} driver{drivers.length !== 1 ? "s" : ""}
            </CardTitle>
          </CardHeader>
          <CardContent className="p-0">
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Name</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Phone</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">National ID</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">License</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">On Shift</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Corridor</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Wallet</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Status</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {drivers.map((d) => (
                    <tr key={d.profileId} className="border-b border-border hover:bg-muted/50">
                      <td className="px-4 py-2">{d.name}</td>
                      <td className="px-4 py-2">{d.phoneNumber}</td>
                      <td className="px-4 py-2 font-mono text-xs">{d.nationalId || "—"}</td>
                      <td className="px-4 py-2 font-mono text-xs">{d.licenseNumber || "—"}</td>
                      <td className="px-4 py-2">
                        {d.onShift ? (
                          <Badge variant="success">Yes</Badge>
                        ) : (
                          <Badge variant="outline">No</Badge>
                        )}
                      </td>
                      <td className="px-4 py-2 text-xs">{d.activeCorridor || "—"}</td>
                      <td className="px-4 py-2">{d.walletBalance ?? 0} EGP</td>
                      <td className="px-4 py-2">
                        <Badge variant={statusBadge[d.approvalStatus] || "outline"}>
                          {d.approvalStatus}
                        </Badge>
                      </td>
                      <td className="px-4 py-2">
                        {d.approvalStatus === "PENDING" && (
                          <div className="flex gap-1">
                            <Button size="sm" variant="default" onClick={() => handleApprove(d.profileId)}>
                              Approve
                            </Button>
                            <Button size="sm" variant="destructive" onClick={() => handleReject(d.profileId)}>
                              Reject
                            </Button>
                          </div>
                        )}
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
