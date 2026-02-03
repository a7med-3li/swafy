## Ride Module:
- This module will handle the business logic of estimating the ride details and provided it back to the user
and confirm the ride to be registered in the database.
  -  ### User Sequence:
     - The user will start with searching for the drop-off location and the ride module will take that search
     input and look for an address using the *addressing module*. The results will be returned back to the user to choose from.
     Once the user selects an address, they will be asked for confirming the pick-up location, this will follow
     the same logic unless the user chose the current location.
     Once we have the pick-up and drop-off locations we will send them to the maps API to calculate the distance,
     route, and ETA. These will be used to calculate the initial fare, and ride details. The estimated ride details
     will be sent to the user to confirm the ride. Once the ride is confirmed, the module will register it
     in the database and notify the drivers with the assist of the *Driver module*.
     
     [//]: # ( the rest of user sequence which include the driver matching)


- ### Ride Estimation Service:
  - A service that will do initial calculations, initial fares, and ETA, and send it back to the user to 
  choose from the available options and confirm the ride or cancel it. 
  - ####   
- ### Ride Service:
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

## Addressing Module:
- This module will work as a middle layer between the app and the maps API, since it will try to provide the 
user with addresses from the app local database before hitting an API call, to minimize the number of maps API calls.
This will be done by creating a local database to store all possible locations and search inside them 
based on the user input. If the location does not exist in the local database, the app will use the maps API 
to get that location and store it in the local database for the future searches. 


- ####
  - ##### Note: 
    - The client should not call the API after each keystroke immediately, they should wait, e.g. 300ms after each keystroke.
    The user types 'P', and then types 'Pa' quickly, then ignore 'P', and call the API with 'Pa' only.
    - When there is no match, return an empty list, not Error.
    - When local results are empty: Show something like: 🔍 “Search on map for ‘par’”