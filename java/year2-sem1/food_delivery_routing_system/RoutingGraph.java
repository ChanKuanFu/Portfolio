package food_delivery_routing_system;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class RoutingGraph {
    private Map<Location, List<Route>> adjacentList = new HashMap<>();
    
    public void addLocation(Location loc){
        //if new loc then create list for new path
        if (!adjacentList.containsKey(loc)) {
            adjacentList.put(loc, new ArrayList<>());
        }   
    }
    
    public void removeLocation(Location loc){
        adjacentList.remove(loc);
        
        // traverse Route list for all location, and clear the edge for loc
        for (Map.Entry<Location, List<Route>> entry : adjacentList.entrySet()) {
            List<Route> routeList = entry.getValue();
            
            // traverse and remove
            Iterator<Route> it = routeList.iterator();
            while (it.hasNext()){
                Route rt = it.next();
                // if adjacency vertex is removed then delete edge which incident to
                // and remove the relevant edge in FOOD delivery network
                if(rt.getOther(loc) != null){
                    if(rt.getStartVertex().equals(loc) || rt.getEndVertex().equals(loc)){
                        it.remove();
                    }
                }
            }
        }
    }
    
    public void addRoute(Location a, Location b){
        // add new loc in list
        addLocation(a);
        addLocation(b);
        
        // edge
        Route rt = new Route(a,b);
    
        // adding route to each loc
        adjacentList.get(a).add(rt);
        adjacentList.get(b).add(rt);
    }
    
    public boolean removeRoute(Location a, Location b) {
        boolean removed = false;
        
        if (a == null || b == null) {
            return false; // exit if missing vertex
        }
        
        List<Route> rtsA = adjacentList.get(a);
        if (rtsA != null) {
            Iterator<Route> itA = rtsA.iterator();
            while (itA.hasNext()) {
                Route route = itA.next();
                if (route.getOther(a).equals(b)) {
                    itA.remove();
                    removed = true;
                }
            }
        }

        List<Route> rtsB = adjacentList.get(b);
        if (rtsB != null) {
            Iterator<Route> itB = rtsB.iterator();
            while (itB.hasNext()) {
                Route route = itB.next();
                if (route.getOther(b).equals(a)) {
                    itB.remove();
                    removed = true;
                }
            }
        }
        return removed;
    }
    
    public List<Route> getAdjacent(Location loc){
        // get adjacency vertex which link with same edge
        if (adjacentList.containsKey(loc)) {
            return adjacentList.get(loc);
        }
        return new ArrayList<>(); // return empty list if not found
    }
    
    public List<Location> getAllLocations(){
        // get all key in adjacentList and return with List
        return new ArrayList<>(adjacentList.keySet()); 
    }
    
    public List<Location> findShortestRoute(Location start, Location target){

        // if not have loc then return empty list
        if(start == null || target == null){
            return new ArrayList<>();
        }
        // if not found in adjacentList then return empty list
        if(!adjacentList.containsKey(start) || !adjacentList.containsKey(target)){
            return new ArrayList<>();
        }
        
        // create Queue collection for traversing
        Queue<Location> queue = new LinkedList<>();
        // store visited node
        Set<Location> visited = new HashSet<>();
        // store parent node
        Map<Location, Location> parent = new HashMap<>();
        
        // initialize starting point
        queue.add(start); // in queue 
        visited.add(start); // and in visited
        
        // BFS algo status , true to terminate
        boolean found = false;
        
        // BFS algo
        while(!queue.isEmpty()){
            Location currentVertex = queue.poll();
            
            // if found targetVertex then terminate
            if(currentVertex.equals(target)){
                found = true;
                break;
            }
            
            List<Route> neighboursNode = getAdjacent(currentVertex);
            // traverse all adjacent vertex in adjacentList
            for(Route rt:neighboursNode){
                Location neighbourNode = rt.getOther(currentVertex);
               
                
                if(!visited.contains(neighbourNode)){
                    /*
                        visited[neighbourNode] <- true
                        parent[neighbourNode] <- currentNode
                        Q.enqueue(neighbourNode)    
                    */
                    visited.add(neighbourNode);
                    parent.put(neighbourNode,currentVertex);
                    queue.add(neighbourNode);
                }
            }
        }
        
        // if not found then return empty list
        if(!found){
            return new ArrayList<>();
        }
        
        // check path from target to start
        List<Location> path = new ArrayList<>();
        Location current = target;
        while(current!=null){
            path.add(0,current); // insert to index 0 for reverse action
            current = parent.get(current);
        }
        
        return path;
    }
    
    public Location getLocationByName(String name){
        // return matching location from customer input
        // traverse all loc in adjacenList
        // compare name with getName()
        for(Location loc:adjacentList.keySet()){
            if(loc.getName().equalsIgnoreCase(name)){
                return loc;
            }
        }
        return null; // if not found mathing loc
    }
    
    public String displayRoutingGraph(){
        // traverse each entry in adjacentList
        // output with String
        StringBuilder sb = new StringBuilder();
        sb.append("  > Routing Graph Structure \n");
        
        int vertexCounter = 0;
        for(Location loc:adjacentList.keySet()){
            List<Route> rts = adjacentList.get(loc);
            sb.append(String.format("    %2d",vertexCounter + 1)); 
            if((vertexCounter + 1)%10 == 1){
                sb.append("st");
            } else if((vertexCounter + 1)%10 == 2){
                sb.append("nd");
            } else if((vertexCounter + 1)%10 == 3){
                sb.append("rd");
            } else{
                sb.append("th");
            } 
            sb.append(" path: ");
            sb.append(loc.getName());
            sb.append(" -> [ ");
            
            Iterator<Route> it = rts.iterator();
            while (it.hasNext()) {
                Route rt = it.next();
                Location neighbourNode = rt.getOther(loc);

                sb.append(neighbourNode.getName());

                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append(" ]\n");
            vertexCounter++; // for next vertex if have
        }
        return sb.toString();
    }
}
