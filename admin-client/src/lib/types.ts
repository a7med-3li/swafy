export interface AdminStats {
  totalUsers: number
  totalDrivers: number
  approvedDrivers: number
  pendingDrivers: number
  totalRidesToday: number
  activeSubscriptions: number
  totalSubscriptionsSold: number
  totalSubscriptionRevenue: number
  totalRidesBooked: number
  totalRidesInProgress: number
  totalRidesCompleted: number
  totalRidesCancelled: number
  totalRidesNoShow: number
}

export interface RideAdminItem {
  id: string
  passengerId: string
  driverId: string | null
  corridorId: number
  status: string
  pin: string | null
  departureTime: string | null
  requestedAt: string | null
  startedAt: string | null
  completedAt: string | null
  boardingConfirmedAt: string | null
  noShowMarkedAt: string | null
  estimatedFare: number | null
  finalFare: number | null
}

export interface DriverAdminItem {
  profileId: string
  userId: string
  name: string
  phoneNumber: string
  nationalId: string | null
  licenseNumber: string | null
  onShift: boolean
  activeCorridor: string | null
  walletBalance: number | null
  approvalStatus: string
}

export interface DepositItem {
  id: number
  driverProfileId: string
  amount: number
  receiveDate: string
  isApproved: boolean
  isRefunded: boolean
  refundReason: string | null
  refundDate: string | null
}

export interface SubscriptionSalesItem {
  id: number
  passengerId: string
  plan: string
  totalRides: number
  remainingRides: number
  price: number
  startDate: string
  endDate: string
  status: string
  createdAt: string
  autoRenew: boolean
}

export interface SubscriptionSalesStats {
  totalActive: number
  totalExpired: number
  totalCancelled: number
  totalSuspended: number
  studentBasicCount: number
  studentPlusCount: number
  corporateCommuterCount: number
  totalRevenue: number
}

export interface NoShowPassengerItem {
  passengerId: string
  passengerName: string
  noShowCount: number
}

export interface LoginRequest {
  phoneNumber: string
  password: string
}

export interface LoginResponse {
  token: string
  refreshToken: string
}
