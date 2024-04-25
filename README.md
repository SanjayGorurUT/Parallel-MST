CONTRIBUTOR(s): Ritvik Mahendra

Abstract

This term paper will cover the implementation of parallel minimum spanning tree algorithms. The paper will demonstrate the parallel Boruvka algorithm, as well as the LLP variants of Prim’s and Kruskal’s algorithm implemented in Java, as well as a discussion on design choices in the algorithms. Finally, a comparison of the algorithms implemented will be discussed.

Intro 

Consider a tree with n nodes and m edges, with each edge having some weight w[i,j] for edge from node i to node j. A very salient problem is to find a minimum spanning tree for this tree, or the least weight set of edges such that the nodes remain connected. With applications to network problems, it is important to be able to compute minimum spanning trees in an efficient manner. While these trees can be computed sequentially, namely with Kruskal’s or Prim’s algorithm, for trees with a large number of nodes and edges, this can become quite a costly computation. Thus, by parallelizing the problem, it can allow for more efficient computation of these trees.

Problem

The task at hand is to implement parallel algorithms to find minimum spanning trees. More clearly, given a graph, develop parallel algorithms that can return the edge set that comprises the minimum spanning tree. There are three algorithms that were covered in class to compute these minimum spanning trees - the parallel Boruvka algorithm, and the LLP variants of Prim’s and Kruskal’s algorithm. These algorithms are implemented in this paper.

Comparison of Algorithms Implemented

There are three algorithms that are implemented in this paper - parallel Boruvka, and the LLP variants of Prim’s and Kruskal’s algorithm.

In the parallel Boruvka algorithm, the algorithm attempts to connect fragments in the tree by finding the minimum edge out of that fragment to build bigger fragments, until there is one fragment left (the minimum spanning tree). By utilizing pointer jumping to carry out the BFS step of the Boruvka algorithm (BFS is used to find the component id for each node), each step takes O(log(N)) time (since pointer jumping finishes in at most O(log(N)) time as it takes log(N) steps to terminate), and there are maximum log(N) steps since if there are c fragments in the tree, the next step will generate c/2 new edges at minimum, so that halves the number of fragments, leading to log(N) steps. In conclusion, the Boruvka algorithm takes O(log2(N)) time. The pseudocode and implementation are included in the appendix.

On the other hand, the LLP variants of Prim’s and Kruskal’s algorithm have a very different time complexity measure. For Kruskal’s algorithm, it is possible to achieve an O(m) parallel time complexity if edges are given in sorted order to the algorithm. However, the implementation in this paper does not achieve this time complexity. This is because in the forbidden function, the algorithm checks if a path exists between node i and node j using the required set of edges using a recursive DFS function. This can be improved by precomputing the reachability of all nodes from one to the other using a certain subset of edges in the graph, which could then achieve the optimal parallel time complexity. As for Prim’s algorithm, the LLP variant requires computing an edge set for each step of the algorithm (it is used to check if the edge is forbidden and needs to be advanced on) that requires the usage of multiple DFS calls, which can be costly in terms of computation.

Comparing the run times on a sample tree with n = 5 nodes and m = 7 edges (shown in the appendix), the parallel Boruvka algorithm finished in 2ms, LLP Prim took 6ms and LLP Kruskal took 2ms. The reason for the Boruvka algorithm taking quite some time to finish could be attributed to the overhead of using a thread pool to execute the function, and timing around this could include the overhead of setting up and tearing down the thread pool. For larger trees, the difference in runtimes is more exaggerated. For example, with a fully connected graph of 50 nodes, LLP Kruskal took 343ms to run, LLP Prim took 712ms to run and parallel Boruvka took 5ms to run. In the appendix is a graph for runtimes (in ms) of different numbers of nodes for a fully connected graph.

Conclusions

This paper discussed the implementation of various parallel minimum spanning tree algorithms, including the parallel Boruvka algorithm, and LLP variants of Prim’s and Kruskal’s algorithm. Topics covered include the practical implementation of these algorithms, comparison with their theoretical time complexities and timing analysis on various trees.
x
References

[1] Course Notes (Parallel Algorithms Book Chapter 10)

Appendix

The implementation of the algorithms follows the pseudocode given in Figures 1, 2, 3 below.


Figure 1: Pseudocode for LLP Variant of Kruskal’s Algorithm [1]


Figure 2: Pseudocode for LLP Variant of Prim’s Algorithm [1]

Figure 3: Pseudocode for Parallel Boruvka Algorithm [1]

For validation of the parallel algorithms, a simple tree was used to test them. This tree is shown below in Figure 4.

Figure 4: Tree used for Testing Algorithms Implemented [1]

Once the algorithms were verified to be running correctly, they were then tested on fully connected graphs with the number of nodes ranging from n = 10 to n = 50. Figure 5 below shows the runtimes of the algorithms, in ms, on these graphs.

Figure 5: Runtimes of Parallel MST Algorithms
