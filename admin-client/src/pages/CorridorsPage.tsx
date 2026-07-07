import { useEffect, useState } from "react"
import { get, post } from "@/lib/api"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import {
  Dialog, DialogTrigger, DialogContent, DialogHeader, DialogTitle,
  DialogDescription, DialogClose,
} from "@/components/ui/dialog"
import { MapPicker } from "@/components/MapPicker"
import type { CorridorItem, CorridorStopItem } from "@/lib/types"
import { Plus, X, MapPin, Map } from "lucide-react"

export function CorridorsPage() {
  const [corridors, setCorridors] = useState<CorridorItem[]>([])
  const [loading, setLoading] = useState(true)

  const [name, setName] = useState("")
  const [route, setRoute] = useState("")
  const [price, setPrice] = useState("")
  const [stops, setStops] = useState<{ name: string; latitude: string; longitude: string }[]>([])
  const [submitting, setSubmitting] = useState(false)

  const [mapOpen, setMapOpen] = useState(false)
  const [editingStopIndex, setEditingStopIndex] = useState<number | null>(null)

  const fetchCorridors = () => {
    setLoading(true)
    get<CorridorItem[]>("/api/v1/corridors")
      .then(setCorridors)
      .catch(console.error)
      .finally(() => setLoading(false))
  }

  useEffect(() => { fetchCorridors() }, [])

  const addStop = () => {
    setStops([...stops, { name: "", latitude: "", longitude: "" }])
  }

  const removeStop = (index: number) => {
    setStops(stops.filter((_, i) => i !== index))
  }

  const updateStop = (index: number, field: string, value: string) => {
    const updated = stops.map((s, i) => (i === index ? { ...s, [field]: value } : s))
    setStops(updated)
  }

  const openMapPicker = (index: number) => {
    setEditingStopIndex(index)
    setMapOpen(true)
  }

  const handleMapPick = (lat: number, lng: number) => {
    if (editingStopIndex !== null) {
      setStops((prev) =>
        prev.map((s, i) =>
          i === editingStopIndex
            ? { ...s, latitude: lat.toFixed(6), longitude: lng.toFixed(6) }
            : s,
        ),
      )
    }
  }

  const handleSubmit = async () => {
    if (!name.trim() || !route.trim() || !price || stops.some((s) => !s.name.trim() || !s.latitude || !s.longitude)) return

    setSubmitting(true)
    try {
      await post("/api/v1/corridors/add", {
        name: name.trim(),
        route: route.trim(),
        price: parseFloat(price),
        stops: stops.map((s) => ({
          name: s.name.trim(),
          latitude: parseFloat(s.latitude),
          longitude: parseFloat(s.longitude),
        })),
      })
      setName("")
      setRoute("")
      setPrice("")
      setStops([])
      fetchCorridors()
    } catch (e) {
      console.error(e)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Corridor Management</h1>

      <Card>
        <CardHeader>
          <CardTitle>Add New Corridor</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-3 gap-4">
            <div className="space-y-1">
              <label className="text-sm font-medium">Name</label>
              <Input placeholder="e.g. Downtown Express" value={name} onChange={(e) => setName(e.target.value)} />
            </div>
            <div className="space-y-1">
              <label className="text-sm font-medium">Route</label>
              <Input placeholder="e.g. Main St to 5th Ave" value={route} onChange={(e) => setRoute(e.target.value)} />
            </div>
            <div className="space-y-1">
              <label className="text-sm font-medium">Price (EGP)</label>
              <Input type="number" step="0.01" min="0" placeholder="e.g. 25.00" value={price} onChange={(e) => setPrice(e.target.value)} />
            </div>
          </div>

          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <label className="text-sm font-medium">Stops (VBS)</label>
              <Button size="sm" variant="outline" onClick={addStop}>
                <Plus className="size-4 mr-1" /> Add Stop
              </Button>
            </div>
            {stops.length === 0 && (
              <p className="text-sm text-muted-foreground">No stops added yet.</p>
            )}
            {stops.map((stop, i) => (
              <div key={i} className="flex items-end gap-2 rounded-lg border border-border p-3">
                <div className="flex-1 space-y-1">
                  <label className="text-xs text-muted-foreground">Stop Name</label>
                  <Input
                    placeholder="Stop name"
                    value={stop.name}
                    onChange={(e) => updateStop(i, "name", e.target.value)}
                  />
                </div>
                <div className="w-36 space-y-1">
                  <label className="text-xs text-muted-foreground">Location</label>
                  <Button
                    variant={stop.latitude && stop.longitude ? "outline" : "secondary"}
                    size="sm"
                    className="w-full justify-start gap-1.5"
                    onClick={() => openMapPicker(i)}
                  >
                    <Map className="size-3.5 shrink-0" />
                    {stop.latitude && stop.longitude
                      ? `${parseFloat(stop.latitude).toFixed(4)}, ${parseFloat(stop.longitude).toFixed(4)}`
                      : "Pick on Map"}
                  </Button>
                </div>
                <Button size="icon" variant="ghost" onClick={() => removeStop(i)}>
                  <X className="size-4" />
                </Button>
              </div>
            ))}
          </div>

          <Button
            onClick={handleSubmit}
            disabled={submitting || !name.trim() || !route.trim() || !price || stops.length === 0 || stops.some((s) => !s.name.trim() || !s.latitude || !s.longitude)}
          >
            {submitting ? "Saving..." : "Save Corridor"}
          </Button>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-sm text-muted-foreground">
            {corridors.length} corridor{corridors.length !== 1 ? "s" : ""}
          </CardTitle>
        </CardHeader>
        <CardContent className="p-0">
          {loading ? (
            <div className="text-center text-muted-foreground py-8">Loading...</div>
          ) : corridors.length === 0 ? (
            <div className="text-center text-muted-foreground py-8">No corridors yet.</div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">ID</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Name</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Price</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Stops</th>
                    <th className="px-4 py-2 text-left font-medium text-muted-foreground">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {corridors.map((c) => (
                    <tr key={c.id} className="border-b border-border hover:bg-muted/50">
                      <td className="px-4 py-2 font-mono text-xs">{c.id}</td>
                      <td className="px-4 py-2 font-medium">{c.name}</td>
                      <td className="px-4 py-2">{c.price} EGP</td>
                      <td className="px-4 py-2">{c.stops.length} stop{c.stops.length !== 1 ? "s" : ""}</td>
                      <td className="px-4 py-2">
                        <Dialog>
                          <DialogTrigger asChild>
                            <Button size="sm" variant="outline">
                              <MapPin className="size-3 mr-1" /> View Stops
                            </Button>
                          </DialogTrigger>
                          <DialogContent>
                            <DialogHeader>
                              <DialogTitle>{c.name} - Stops</DialogTitle>
                              <DialogDescription>
                                Virtual bus stops along this corridor.
                              </DialogDescription>
                            </DialogHeader>
                            <div className="space-y-2">
                              {c.stops.map((stop: CorridorStopItem) => (
                                <div key={stop.id} className="rounded-lg border border-border p-3">
                                  <p className="font-medium">{stop.name}</p>
                                  <p className="text-xs text-muted-foreground">
                                    {stop.latitude}, {stop.longitude}
                                  </p>
                                </div>
                              ))}
                            </div>
                          </DialogContent>
                        </Dialog>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>

      <MapPicker
        open={mapOpen}
        onOpenChange={setMapOpen}
        onPick={handleMapPick}
        initialLat={editingStopIndex !== null ? parseFloat(stops[editingStopIndex]?.latitude) || undefined : undefined}
        initialLng={editingStopIndex !== null ? parseFloat(stops[editingStopIndex]?.longitude) || undefined : undefined}
      />
    </div>
  )
}
