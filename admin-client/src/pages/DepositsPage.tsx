import { useEffect, useState } from "react"
import { get, post } from "@/lib/api"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  Dialog, DialogTrigger, DialogContent, DialogHeader, DialogTitle,
  DialogDescription, DialogClose,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from "@/components/ui/select"
import type { DepositItem } from "@/lib/types"

export function DepositsPage() {
  const [deposits, setDeposits] = useState<DepositItem[]>([])
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState("")
  const [forfeitReason, setForfeitReason] = useState("")
  const [forfeitId, setForfeitId] = useState<number | null>(null)

  const fetchDeposits = () => {
    setLoading(true)
    const params = filter ? `?approved=${filter}` : ""
    get<DepositItem[]>(`/api/v1/admin/deposits${params}`)
      .then(setDeposits)
      .catch(console.error)
      .finally(() => setLoading(false))
  }

  useEffect(() => { fetchDeposits() }, [filter])

  const handleApprove = async (id: number) => {
    await post(`/api/v1/admin/deposits/${id}/approve`)
    fetchDeposits()
  }

  const handleForfeit = async () => {
    if (forfeitId === null || !forfeitReason.trim()) return
    await post(`/api/v1/admin/deposits/${forfeitId}/forfeit`, { reason: forfeitReason })
    setForfeitId(null)
    setForfeitReason("")
    fetchDeposits()
  }

  return (
    <div className="space-y-4">
      <h1 className="text-2xl font-bold">Deposit Management</h1>

      <div className="w-40">
        <Select value={filter} onValueChange={setFilter}>
          <SelectTrigger>
            <SelectValue placeholder="All Deposits" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">All Deposits</SelectItem>
            <SelectItem value="true">Approved</SelectItem>
            <SelectItem value="false">Pending</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {loading ? (
        <div className="text-center text-muted-foreground py-8">Loading...</div>
      ) : (
        <Card>
          <CardHeader>
            <CardTitle className="text-sm text-muted-foreground">
              {deposits.length} deposit{deposits.length !== 1 ? "s" : ""}
            </CardTitle>
          </CardHeader>
          <CardContent className="p-0">
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">ID</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Driver</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Amount</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Date</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Approved</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Refunded</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {deposits.map((d) => (
                    <tr key={d.id} className="border-b border-border hover:bg-muted/50">
                      <td className="px-4 py-2 font-mono text-xs">{d.id}</td>
                      <td className="px-4 py-2 font-mono text-xs">{d.driverProfileId.slice(0, 8)}...</td>
                      <td className="px-4 py-2">{d.amount} EGP</td>
                      <td className="px-4 py-2 text-xs">{new Date(d.receiveDate).toLocaleDateString()}</td>
                      <td className="px-4 py-2">
                        {d.isApproved ? (
                          <Badge variant="success">Approved</Badge>
                        ) : (
                          <Badge variant="warning">Pending</Badge>
                        )}
                      </td>
                      <td className="px-4 py-2">
                        {d.isRefunded ? (
                          <Badge variant="destructive">Refunded</Badge>
                        ) : (
                          <Badge variant="outline">No</Badge>
                        )}
                      </td>
                      <td className="px-4 py-2">
                        <div className="flex gap-1">
                          {!d.isApproved && (
                            <Button size="sm" onClick={() => handleApprove(d.id)}>
                              Approve
                            </Button>
                          )}
                          {d.isApproved && !d.isRefunded && (
                            <Dialog>
                              <DialogTrigger asChild>
                                <Button size="sm" variant="destructive" onClick={() => setForfeitId(d.id)}>
                                  Forfeit
                                </Button>
                              </DialogTrigger>
                              <DialogContent>
                                <DialogHeader>
                                  <DialogTitle>Forfeit Deposit</DialogTitle>
                                  <DialogDescription>
                                    This will deduct the deposit amount from the driver's wallet.
                                    Enter a reason for forfeiture.
                                  </DialogDescription>
                                </DialogHeader>
                                <div className="space-y-3">
                                  <Input
                                    placeholder="Reason for forfeiture"
                                    value={forfeitReason}
                                    onChange={(e) => setForfeitReason(e.target.value)}
                                  />
                                  <div className="flex justify-end gap-2">
                                    <DialogClose asChild>
                                      <Button variant="outline">Cancel</Button>
                                    </DialogClose>
                                    <DialogClose asChild>
                                      <Button
                                        variant="destructive"
                                        onClick={handleForfeit}
                                        disabled={!forfeitReason.trim()}
                                      >
                                        Confirm Forfeit
                                      </Button>
                                    </DialogClose>
                                  </div>
                                </div>
                              </DialogContent>
                            </Dialog>
                          )}
                        </div>
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
