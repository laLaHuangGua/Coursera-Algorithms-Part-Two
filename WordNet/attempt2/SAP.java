// Attempt2: get scores 71/100

package WordNet.attempt2;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
  private static final boolean NEED_LENGTH = true;
  private static final boolean NEED_ANCESTOR = false;
  private final Digraph graph;

  public SAP(Digraph G) {
    if (G == null)
      throw new IllegalArgumentException("digraph is null");
    graph = new Digraph(G);
  }

  public int length(int v, int w) {
    validateVertex(v);
    validateVertex(w);
    return dfs(NEED_LENGTH, v, w);
  }

  public int ancestor(int v, int w) {
    validateVertex(v);
    validateVertex(w);
    return dfs(NEED_ANCESTOR, v, w);
  }

  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    validateVertices(v);
    validateVertices(w);
    return dfs(NEED_LENGTH, v, w);
  }

  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    validateVertices(v);
    validateVertices(w);
    return dfs(NEED_ANCESTOR, v, w);
  }

  private void validateVertex(int v) {
    int size = graph.V();
    if (v < 0 || v >= size)
      throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (size - 1));
  }

  private void validateVertices(Iterable<Integer> vertices) {
    if (vertices == null)
      throw new IllegalArgumentException("argument is null");

    for (Integer v : vertices) {
      if (v == null)
        throw new IllegalArgumentException("vertex is null");
      validateVertex(v);
    }
  }

  private int dfs(boolean flag, int v, int w) {
    var dfsFromV = new BreadthFirstDirectedPaths(graph, v);
    var dfsFromW = new BreadthFirstDirectedPaths(graph, w);
    return find(flag, dfsFromV, dfsFromW);
  }

  private int dfs(boolean flag, Iterable<Integer> v, Iterable<Integer> w) {
    var dfsFromV = new BreadthFirstDirectedPaths(graph, v);
    var dfsFromW = new BreadthFirstDirectedPaths(graph, w);
    return find(flag, dfsFromV, dfsFromW);
  }

  private int find(boolean flag,
      BreadthFirstDirectedPaths dfsFromV, BreadthFirstDirectedPaths dfsFromW) {

    int dist = Integer.MIN_VALUE;
    int ancestor = Integer.MIN_VALUE;
    for (int x = 0; x < graph.V(); x++)
      if (dfsFromV.hasPathTo(x) && dfsFromW.hasPathTo(x)) {
        int newDist = dfsFromV.distTo(x) + dfsFromW.distTo(x);
        if (newDist < dist || dist < 0) {
          dist = newDist;
          ancestor = x;
        }
      }

    if (Integer.compare(Integer.MIN_VALUE, dist) == 0)
      return -1;
    if (flag == NEED_LENGTH)
      return dist;
    return ancestor;
  }

  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}
