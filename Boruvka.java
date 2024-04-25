import java.util.*;
import java.util.concurrent.*;

public class Boruvka {

    static class Edge {
        int node1;
        int node2;
        int weight;

        public Edge(int node1, int node2, int weight) {
            this.node1 = node1;
            this.node2 = node2;
            this.weight = weight;
        }
    }

    static class Subset {
        int parent;
        int rank;

        public Subset(int parent, int rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }

    static class Graph {
        int nodes;
        int edges;
        List<Edge> allEdges = new ArrayList<>();

        public Graph(int nodes, int edges) {
            this.nodes = nodes;
            this.edges = edges;
        }

        public void addEdge(int node1, int node2, int weight) {
            allEdges.add(new Edge(node1, node2, weight));
        }
    }

    static class ParallelBoruvkaTask implements Runnable {
        Graph graph;
        List<Edge> mstEdges;
        Subset[] subsets;

        public ParallelBoruvkaTask(Graph graph, Subset[] subsets, List<Edge> mstEdges) {
            this.graph = graph;
            this.subsets = subsets;
            this.mstEdges = mstEdges;
        }

        @Override
        public void run() {
            while (mstEdges.size() + 1 < graph.nodes) {
                Edge[] closest = new Edge[graph.nodes];

                for (Edge edge : graph.allEdges) {
                    int set1 = find(subsets, edge.node1);
                    int set2 = find(subsets, edge.node2);

                    if (set1 != set2) {
                        if (closest[set1] == null || edge.weight < closest[set1].weight)
                            closest[set1] = edge;

                        if (closest[set2] == null || edge.weight < closest[set2].weight)
                            closest[set2] = edge;
                    }
                }

                for (int i = 0; i < graph.nodes; i++) {
                    if (closest[i] != null) {
                        int set1 = find(subsets, closest[i].node1);
                        int set2 = find(subsets, closest[i].node2);

                        if (set1 != set2) {
                            mstEdges.add(closest[i]);
                            union(subsets, set1, set2);
                        }
                    }
                }
            }
        }

        private int find(Subset[] subsets, int i) {
            if (subsets[i].parent != i) {
                subsets[i].parent = find(subsets, subsets[i].parent);
            }
            return subsets[i].parent;
        }

        private void union(Subset[] subsets, int x, int y) {
            int xRoot = find(subsets, x);
            int yRoot = find(subsets, y);

            if (subsets[xRoot].rank < subsets[yRoot].rank) {
                subsets[xRoot].parent = yRoot;
            }
            else if (subsets[xRoot].rank > subsets[yRoot].rank) {
                subsets[yRoot].parent = xRoot;
            }
            else {
                subsets[yRoot].parent = xRoot;
                subsets[xRoot].rank++;
            }
        }
    }

    public static void main(String[] args) {
        int nodes = 5;
        int edges = 7;

        Graph graph = new Graph(nodes, edges);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 3);
        graph.addEdge(1, 3, 7);
        graph.addEdge(2, 3, 9);
        graph.addEdge(2, 4, 11);
        graph.addEdge(3, 4, 2);

        Subset[] subsets = new Subset[nodes];
        for (int v = 0; v < nodes; v++) {
            subsets[v] = new Subset(v, 0);
        }

        List<Edge> mstEdges = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(nodes);
        for (int i = 0; i < nodes; i++) {
            executor.execute(new ParallelBoruvkaTask(graph, subsets, mstEdges));
        }
        executor.shutdown();

        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Edge edge : mstEdges) {
            System.out.println(edge.node1 + " - " + edge.node2 + ": " + edge.weight);
        }
    }
}