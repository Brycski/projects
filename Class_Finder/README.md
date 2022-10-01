# Class Path Finder

**Overview**

Mapping services like Google Maps or Apple Maps don't let users utilize their routing algorithms for walking paths 
on College Campuses, despite the data for such paths existing. So, I am using Google Earth Engine's latitude 
and longitude data, the "USGS National Elevation Dataset," and the A* search algorithm to find the fastest route to my 
classes. A* works by essentially adding a heuristic to Dijkstra's search algorithm, a search algorithm that works 
through weights between points. Here, I am using distance as the minimizing weights between latitude and longitude 
points and negative elevation gain as my heuristic. **This allows me to find the least distance route to class with 
the least elevation gain = the quickest route!**

**Features**

- Location selector - drop down menu of buildings to choose from to find path to.
- Map - Map that displays route
- Stats - Displays total distance of route, total elevation gain, and time estimate.

**Running the Project**

Project currently only planned to through IntelliJ IDEA by pressing run on the 'Main' file. **Must install the 
following project libraries: xchart-3.8.1.jar, ucb.jar, junit-4.13.2.jar, jh61b-junit.jar, hamcrest-core-1.3.jar, 
and algs4.jar.**

**Credit**

Credit to CS61BL at UC Berkeley for PriorityQueue.java interface.





