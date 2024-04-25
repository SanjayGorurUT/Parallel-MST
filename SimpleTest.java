import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTest {

    final int[][] ADJ_MATRIX1 = {
      // 0,  1,  2,  3,  4 (vertex labels)
        {0,  5,  4,  0,  0}, // 0
        {5,  0,  3,  7,  0}, // 1
        {4,  3,  0,  9, 11}, // 2
        {0,  7,  9, 0,  2}, // 3
        {0,  0,  11, 2, 0}, // 4
    };

    final int[] edges = {2, 3, 4, 5, 7, 9, 11};
    final int[] MST_Kruskal = {1, 1, 1, 0, 1, 0, 0};
    final int[] MST_Prim = {2,3,4,7};

    @Test
    public void testKruskal() {
        Kruskal k = new Kruskal(ADJ_MATRIX1, edges);
        k.solve();
        int[] components = k.getSolution();
        assertArrayEquals(components, MST_Kruskal);
    }

    @Test
    public void testPrim() {
        Prim p = new Prim(ADJ_MATRIX1);
        p.solve();
        int[] components = p.getSolution();
        assertArrayEquals(components, MST_Prim);
    }
}