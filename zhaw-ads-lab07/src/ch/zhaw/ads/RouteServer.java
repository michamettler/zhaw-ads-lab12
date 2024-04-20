package ch.zhaw.ads;

import java.util.*;

public class RouteServer implements CommandExecutor {
    /**
     * build the graph given a text file with the topology
     */
    public Graph<DijkstraNode, Edge> createGraph(String topo) throws Exception {
        Graph<DijkstraNode, Edge> graph = new AdjListGraph<>(DijkstraNode.class, Edge.class);
        Scanner scanner = new Scanner(topo);
        while (scanner.hasNextLine()) {
            String[] routeInfo = scanner.nextLine().split(" ");
            try {
                graph.addNode(routeInfo[0]);
                graph.addNode(routeInfo[1]);
                graph.addEdge(routeInfo[0], routeInfo[1], Integer.parseInt(routeInfo[2]));
                graph.addEdge(routeInfo[1], routeInfo[0], Integer.parseInt(routeInfo[2]));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return graph;
    }


    /**
     * apply the dijkstra algorithm
     */
    public void dijkstraRoute(Graph<DijkstraNode, Edge> graph, String from, String to) {
        for (DijkstraNode node : graph.getNodes()) {
            node.setDist(Double.MAX_VALUE);
        }
        Queue<DijkstraNode> pq = new PriorityQueue<>();
        DijkstraNode start = graph.findNode(from);
        start.setDist(0);
        pq.add(start);

        while (!pq.isEmpty()) {
            DijkstraNode curr = pq.poll();
            if (curr.equals(graph.findNode(to))) return;
            curr.setMark(true);
            for (Edge edge : curr.getEdges()) {
                var dest = (DijkstraNode) edge.getDest();
                if (!dest.getMark()) {
                    double dist = curr.getDist() + edge.getWeight();
                    if (dist < dest.getDist()) {
                        dest.setDist(dist);
                        dest.setPrev(curr);
                        pq.add(dest);
                    }
                }
            }
        }
    }

    /**
     * find the route in the graph after applied dijkstra
     * the route should be returned with the start town first
     */
    public List<DijkstraNode> getRoute(Graph<DijkstraNode, Edge> graph, String to) {
        List<DijkstraNode> route = new LinkedList<>();
        DijkstraNode town = graph.findNode(to);
        do {
            route.add(0, town);
            town = town.getPrev();
        } while (town != null);
        return route;
    }

    public String execute(String topo) throws Exception {
        Graph<DijkstraNode, Edge> graph = createGraph(topo);
        dijkstraRoute(graph, "Winterthur", "Lugano");
        List<DijkstraNode> route = getRoute(graph, "Lugano");
        // generate result string
        StringBuilder builder = new StringBuilder();
        for (DijkstraNode rt : route) builder.append(rt).append("\n");
        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        String swiss = "Winterthur Zürich 25\n" +
                "Zürich Bern 126\n" +
                "Zürich Genf 277\n" +
                "Zürich Luzern 54\n" +
                "Zürich Chur 121\n" +
                "Zürich Berikon 16\n" +
                "Bern Genf 155\n" +
                "Genf Lugano 363\n" +
                "Lugano Luzern 206\n" +
                "Lugano Chur 152\n" +
                "Chur Luzern 146\n" +
                "Luzern Bern 97\n" +
                "Bern Berikon 102\n" +
                "Luzern Berikon 41\n";
        RouteServer server = new RouteServer();
        System.out.println(server.execute(swiss));
    }
}
