# Food Delivery Routing System - Year 2 Sem 1 (Java)

A Java-based food delivery routing system that models a delivery network as
a graph of Suppliers, Intersections, Delivery Hubs, and Customers, then uses
Breadth-First Search to compute the shortest route for each order — all
through a console menu with role-based access for guests and an admin.

## Features

- **Graph-Based Delivery Network** — Locations (`Supplier`, `Intersection`,
  `DeliveryHub`, `Customer`) are modeled as vertices extending a shared
  abstract `Location` class, connected by `Route` edges in a `RoutingGraph`
- **Shortest Route Computation** — `RoutingGraph` runs a BFS traversal
  (`findShortestRoute`) to find the shortest path between a supplier and a
  customer, tracing the path back through a parent map
- **Graph Management** — Add or remove locations and routes dynamically at
  runtime, with input validation (duplicate names, existing vertices,
  self-loops, duplicate edges) before any mutation
- **Order Placement & Cost Calculation** — Customers place orders by
  selecting a supplier and destination; the system computes the route,
  locates the delivery hub along the path, and calculates the total payable
  amount from product price + delivery cost
- **Order History Tracking** — Every completed order (ID, customer, route,
  total paid) is recorded in `OrderHistory` for later review
- **Admin Role & Authentication** — A separate `Admin` account (extends
  base login/password logic) can log in, reset its password, view full
  order history, and look up individual customer accounts and their
  outstanding pay amount
- **Guest vs Admin Menus** — The console menu adapts dynamically: guests
  can browse the network, search locations, and place orders; admins get
  extra options for order history, account management, and logout

## Project Structure

```
food_delivery_routing_system/
├── DriverProgram_FoodDeliveryRoutingSystem.java   # Entry point — builds the graph/history/customer list and starts the menu
├── FoodDeliveryRoutingSystem.java                 # Core logic: console menus, graph seeding, order flow, input validation
├── Location.java                                  # Abstract base for all vertex types (name, address, equals/hashCode)
├── Supplier.java                                  # Supplier vertex — extends Location
├── Intersection.java                              # Intersection vertex — extends Location
├── DeliveryHub.java                               # Delivery hub vertex — extends Location
├── Customer.java                                  # Customer vertex — extends Location
├── Route.java                                     # Edge between two Location vertices
├── RoutingGraph.java                              # Adjacency-list graph — add/remove vertices & edges, BFS shortest path
├── Order.java                                     # Order record — links supplier & customer, stores computed path/cost
├── OrderHistory.java                              # Stores and retrieves all placed orders
└── Admin.java                                     # Admin role — login, password reset, order history view, account lookup
```

## How to Run

```bash
# Compile
javac food_delivery_routing_system/*.java -d build

# Run
java -cp build food_delivery_routing_system.DriverProgram_FoodDeliveryRoutingSystem
```

On startup, the system seeds a default delivery network (1 supplier, 5
intersections, 2 delivery hubs, 2 customers) so you can immediately search
locations, view the network, or place an order. A default admin account is
also seeded — see the console output / source for its credentials.

## What I Learned

Building this project helped me practice modeling a real-world network as
a graph in Java — using an abstract base class (`Location`) and inheritance
to represent different vertex types, and implementing BFS from scratch to
find shortest routes between two nodes. It also gave me practice with
defensive input validation across a multi-step console workflow, and
structuring a larger application around a graph data structure (adjacency
list, edge objects, vertex/edge CRUD operations) rather than a simple
list-based model.
