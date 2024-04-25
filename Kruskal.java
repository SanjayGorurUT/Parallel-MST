import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;

public class Kruskal extends LLP {
    int[] G;
    int[][] adjMatrix;
    int[] edges;

    public Kruskal(int[][] adjMatrix, int[] edges) {
        super();
        n = edges.length;
        barrier = new CyclicBarrier(n);
        this.adjMatrix = adjMatrix;
        this.edges = edges;
        this.G = new int[n];
        for (int i = 0; i < this.G.length; i++) {
            this.G[i] = 0;
        }
    }
    public boolean isPath(int start, int end, Set<Integer> edgeSet, Set<Integer> visited) {
        if (start == end) {
            return true;
        }
        visited.add(start);
        for (int j = 0; j < adjMatrix[start].length; j++) {
            if (!visited.contains(j) && edgeSet.contains(adjMatrix[start][j])) {
                if (isPath(j, end, edgeSet, visited)) return true;
            }
        }
        visited.remove(start);
        return false;
    }
    @Override
    public boolean forbidden(int j) {
        if (G[j] == 1) {
            return false;
        }
        for (int x = 0; x < adjMatrix.length; x++) {
            for (int y = 0; y < adjMatrix[x].length; y++) {
                if (adjMatrix[x][y] == edges[j]) {
                    Set<Integer> edgeSet = new HashSet<Integer>();
                    for (int i = 0; i < j; i++) {
                        edgeSet.add(edges[i]);
                    }
                    return !isPath(x, y, edgeSet, new HashSet<Integer>());
                }
            }
        }
        return true;
    }


    @Override
    public void advance(int j) {
        G[j] = 1;
    }

    public int[] getSolution() {
        return G;
    }
}