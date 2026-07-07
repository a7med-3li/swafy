import { useEffect, useRef, useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogClose } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import L from "leaflet"
import "leaflet/dist/leaflet.css"

const defaultIcon = L.icon({
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  iconRetinaUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41],
})

interface MapPickerProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onPick: (lat: number, lng: number) => void
  initialLat?: number
  initialLng?: number
}

export function MapPicker({ open, onOpenChange, onPick, initialLat, initialLng }: MapPickerProps) {
  const markerRef = useRef<L.Marker | null>(null)
  const mapRef = useRef<L.Map | null>(null)
  const containerRef = useRef<HTMLDivElement>(null)
  const [ready, setReady] = useState(false)
  const [hasMarker, setHasMarker] = useState(false)

  useEffect(() => {
    if (!open) {
      setReady(false)
      setHasMarker(false)
      return
    }

    const timer = setTimeout(() => {
      const container = containerRef.current
      if (!container) return

      // height is set by the parent container

      const center: [number, number] = initialLat && initialLng ? [initialLat, initialLng] : [30.0444, 31.2357]

      const map = L.map(container, {
        center,
        zoom: 13,
        zoomControl: true,
      })

      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "&copy; OpenStreetMap contributors",
      }).addTo(map)

      // Force size recalculation after tiles start loading
      setTimeout(() => map.invalidateSize(), 50)

      if (initialLat && initialLng) {
        const marker = L.marker([initialLat, initialLng], { icon: defaultIcon, draggable: true }).addTo(map)
        markerRef.current = marker
        setHasMarker(true)
      }

      map.on("click", (e: L.LeafletMouseEvent) => {
        if (markerRef.current) {
          markerRef.current.setLatLng(e.latlng)
        } else {
          const marker = L.marker(e.latlng, { icon: defaultIcon, draggable: true }).addTo(map)
          markerRef.current = marker
          setHasMarker(true)
        }
      })

      mapRef.current = map
      setReady(true)
    }, 150)

    return () => {
      clearTimeout(timer)
      if (mapRef.current) {
        mapRef.current.remove()
        mapRef.current = null
      }
      markerRef.current = null
      setReady(false)
    }
  }, [open, initialLat, initialLng])

  const handleConfirm = () => {
    if (markerRef.current) {
      const pos = markerRef.current.getLatLng()
      onPick(pos.lat, pos.lng)
    }
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-4xl">
        <DialogHeader>
          <DialogTitle>Pick a Location</DialogTitle>
          <DialogDescription>Click on the map to place a marker, then confirm.</DialogDescription>
        </DialogHeader>
        <div className="relative h-[500px] w-full rounded-lg border border-border overflow-hidden">
          <div ref={containerRef} className="absolute inset-0" />
          {!ready && (
            <div className="absolute inset-0 flex items-center justify-center bg-background text-muted-foreground text-sm">
              Loading map...
            </div>
          )}
        </div>
        <div className="flex justify-end gap-2">
          <DialogClose asChild>
            <Button variant="outline">Cancel</Button>
          </DialogClose>
          <Button onClick={handleConfirm} disabled={!hasMarker}>
            Confirm Location
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  )
}
