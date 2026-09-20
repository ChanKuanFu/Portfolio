package food_delivery_routing_system;

public class Route {
    private Location startVertex;
    private Location endVertex;
    
    Route(Location startVertex, Location endVertex){
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    public Location getStartVertex() {
        return startVertex;
    }

    public Location getEndVertex() {
        return endVertex;
    }

    
    // get adjacency vertex which the edge incident with
    public Location getOther(Location currentVertex){
        // check whether currentVertex is startVertex
        if (startVertex.equals(currentVertex)){
            return this.endVertex;  // if startingpoint , return endpoint as neighbourNode
        } else {
            return this.startVertex;
        }
    }
    /*
    for each neighbourNode in G[currentNode] do
        if not visited[neighbourNode] then
    */
    
    @Override
    public String toString() {
        return String.format("Start vertex: %-20s\nEnd vertex: %-20s",getStartVertex(),getEndVertex());
    }
}
