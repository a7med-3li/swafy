## Ride module:

- ### Ride Estimation Service:
  - A service that will do initial calculations, initial fares, and ETA, and send it back to the user to 
  choose from the available options and confirm the ride or cancel it. 
  - #### 
- Ride Service:
  - The service that will to basic operations on the actual ride entity (CRUD). 
  This will be used after the user confirms the ride request, and we want to save 
  the ride and notify the drivers.
  
- Ride Type Service:
  - A service that provides the available ride types based on the online drivers.
  - Currently, lets make just based on the registered vehicle types, and available ride types on the system.
    - That is because there might be no driver online and there is a user wants to request a ride. Now we are 
    preventing him from that until a driver be online. 
    We just can let him make the request and match him with a driver when there is a one online.
    That is because the driver will be online when there are requests, since he will be notified to be online. 