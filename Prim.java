import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;

public class Prim extends LLP {
    int[][] adjMatrix;
    int[] G;
    int source;
    Prim(int[][] adjMatrix) {
        super();
        n = adjMatrix.length-1;
        barrier = new CyclicBarrier(n);
        this.adjMatrix = adjMatrix;
        this.G = new int[n];
        this.source = 0;
        for(int i = 1; i < adjMatrix.length; i++) {
            int minEdge = Integer.MAX_VALUE;
            for(int j = 0; j < adjMatrix[i].length; j++) {
                if (adjMatrix[i][j] != 0) minEdge = Math.min(minEdge, adjMatrix[i][j]);
            }
            G[i-1] = minEdge;
        }
    }
    @Override
    public boolean forbidden(int j) {
        j++;
        ArrayList<Pair> ePrime = generateEPrime();
        int minWeight = Integer.MAX_VALUE;
        for(int i = 0; i < ePrime.size(); i++) {
            Pair p = ePrime.get(i);
            minWeight = Math.min(minWeight, adjMatrix[p.left][p.right]);
        }
        for (int i = 0; i < ePrime.size(); i++) {
            Pair p = ePrime.get(i);
            if (p.right == j && adjMatrix[p.left][p.right] == minWeight) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void advance(int j) {
        j++;
        ArrayList<Pair> ePrime = generateEPrime();
        int minWeight = Integer.MAX_VALUE;
        for(int i = 0; i < ePrime.size(); i++) {
            Pair p = ePrime.get(i);
            if (p.right == j) minWeight = Math.min(minWeight, adjMatrix[p.left][p.right]);
        }
        G[j-1] = minWeight;
    }
    public boolean fixed(int node, int[] edges) {
        Set<Integer> edgeSet = new HashSet<>();
        for (int i = 0; i < edges.length; i++) {
            edgeSet.add(edges[i]);
        }
        return isPath(node, 0, edgeSet, new HashSet<>());
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
    public ArrayList<Pair> generateEPrime() {
        ArrayList<Pair> ret = new ArrayList<>();
        for(int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix[i].length; j++) {
                if (adjMatrix[i][j] != 0 && fixed(i, G) && !fixed(j, G)) {
                    ret.add(new Pair(i,j));
                }
            }
        }
        return ret;
    }

    public int[] getSolution() {
        Arrays.sort(G);
        return G;
    }

    static class Pair {
        int left;
        int right;
        Pair(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }
}