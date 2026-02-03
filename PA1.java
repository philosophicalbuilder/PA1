// PA1 Skeleton Code
// DSA2, spring 2026

// This code will read in the input, and put the values into lists.  It is up
// to you to properly represent this as a graph -- this code only reads in the
// input properly.

/*
*
* Reading 2 test cases

Test case 0: n=7, e=10, q=10
Created 7 nodes
Read 10 edges
Read 10 queries
test case 0:
there are 7 nodes, 10 edges, and 10 queries
the nodes are: [v0, v1, v2, v3, v4, v5, v6]
the edges are: [v1->v2, v0->v3, v4->v6, v3->v6, v3->v4, v4->v5, v0->v6, v3->v5, v0->v2, v2->v5]
the queries are: [v1->v0, v3->v5, v1->v5, v0->v2, v6->v1, v0->v3, v4->v3, v3->v6, v2->v0, v5->v0]

Building adjacency list...
  Adding edge: v1->v2
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.util.List.add(Object)" because the return value of "java.util.Map.get(Object)" is null
        at PA1.main(PA1.java:80)
ram@Ramkrishnas-MacBook-Pro PA % 
* 
* 
* 
* 
* 
*/

import java.util.*;

class Pair {
    public String s, t;

    Pair(String s, String t) {
        this.s = s;
        this.t = t;
    }

    public String toString() {
        return s + "->" + t;
    }
}

public class PA1 {

    enum Color {
        WHITE,
        GRAY,
        BLACK
    }

    public static void main(String[] args) {

        Scanner stdin = new Scanner(System.in);
        int test_cases = stdin.nextInt();
        System.out.println("Reading " + test_cases + " test cases");

        for (int i = 0; i < test_cases; i++) {

            // read in n, e, and q
            int n = stdin.nextInt();
            int e = stdin.nextInt();
            int q = stdin.nextInt();

            System.out.println("\nTest case " + i + ": n=" + n + ", e=" + e + ", q=" + q);

            // create the list of nodes
            ArrayList<String> nodes = new ArrayList<String>();
            for (int j = 0; j < n; j++)
                nodes.add("v" + j);

            System.out.println("Created " + nodes.size() + " nodes");

            // read in the edges
            ArrayList<Pair> edges = new ArrayList<Pair>();
            for (int j = 0; j < e; j++)
                edges.add(new Pair(stdin.next(), stdin.next()));

            System.out.println("Read " + edges.size() + " edges");

            // read in the queries
            ArrayList<Pair> queries = new ArrayList<Pair>();
            for (int j = 0; j < q; j++)
                queries.add(new Pair(stdin.next(), stdin.next()));

            System.out.println("Read " + queries.size() + " queries");

            System.out.println("test case " + i + ":");
            System.out.println("there are " + n + " nodes, " + e + " edges, and " + q + " queries");
            System.out.println("the nodes are: " + nodes);
            System.out.println("the edges are: " + edges);
            System.out.println("the queries are: " + queries);
            System.out.println("");

            // YOUR CODE HERE (or called from here)
            Map<String, List<String>> graph = new HashMap<>();

            System.out.println("Building adjacency list...");
            for (Pair edge : edges) {
                System.out.println("  Adding edge: " + edge);
                graph.get(edge.s).add(edge.t);
                graph.get(edge.t).add(edge.s);
            }
            System.out.println("Graph built successfully");

            // creating a cache for BFS results.
            Map<String, Map<String, Integer>> distanceCache = new HashMap<>();
            Map<String, Map<String, String>> parentCache = new HashMap<>();

            System.out.println("\nProcessing queries:");
            for (Pair query : queries) {
                String source = query.s;
                String destination = query.t;

                System.out.println("  Query: " + source + " -> " + destination);

                if (!distanceCache.containsKey(source)) {

                    System.out.println("    Cache miss - running BFS from " + source);

                    // 3 maps - color, d, pi. Each are <String, X> respectively.
                    // X is either Color, Integer, or String.
                    Map<String, Color> color = new HashMap<>();
                    Map<String, Integer> d = new HashMap<>();
                    Map<String, String> pi = new HashMap<>();
                    Queue<String> Q = new ArrayDeque<>();

                    // Map being brought to default state for all nodes.
                    for (String v : nodes) {
                        color.put(v, Color.WHITE);
                        d.put(v, Integer.MAX_VALUE);
                        pi.put(v, null);
                    }
                    System.out.println("    Initialized all nodes to WHITE");

                    color.put(source, Color.GRAY);
                    d.put(source, 0);

                    pi.put(source, null);
                    Q.offer(source);
                    System.out.println("    Starting BFS from " + source + ", queue size: " + Q.size());

                }
                /*
                 * BFS from slides: color, d, pi. Will use INTEGER.MAX_VALUE for d.
                 * Q is regular q, linkedlist version.
                 */
                Map<String, Color> color = new HashMap<>();
                Map<String, Integer> d = new HashMap<>();
                Map<String, String> pi = new HashMap<>();
                Queue<String> Q = new LinkedList<>();

                // adding enum
                // preprocessing for all nodes - setting up initial state.
                // prof said white is unprocessed. New/fresh.
                // for all nodes v in the group of nodes, put white for their color
                // put their distance as infinity and their parent as null.
                for (String v : nodes) {
                    color.put(v, Color.WHITE);
                    d.put(v, Integer.MAX_VALUE);
                    pi.put(v, null);
                }

                System.out.println("    Initializing BFS data structures");

                /*
                 * Now, set source color as defaults from enum.
                 * grey color as it's the first node being put in the queue.
                 * distance from itself is 0.
                 * doesn't have a parent so pi is null.
                 * and now we offer the source.
                 */
                color.put(source, Color.GRAY);
                d.put(source, 0);
                pi.put(source, null);
                Q.offer(source);

                System.out.println("    Queue after adding source: size=" + Q.size());

                int iterations = 0;
                while (!Q.isEmpty()) {
                    // queue logic as per slides.
                    String u = Q.poll();
                    iterations++;
                    System.out.println("      [BFS iter " + iterations + "] Processing node: " + u + " (queue size: "
                            + Q.size() + ")");
                    List<String> Adj_u = graph.getOrDefault(u, new ArrayList<>());

                    System.out.println("      Node " + u + " has " + Adj_u.size() + " neighbors: " + Adj_u);

                    // for a node v - loop through its neighbours stored in adj list.
                    // if that color is white then put it into processing.
                    // record its distance and parent and offer it to the queue.
                    for (String v : Adj_u) {
                        if (color.get(v) == Color.WHITE) {
                            System.out.println(
                                    "        Discovered " + v + " from " + u + " (distance: " + (d.get(u) + 1) + ")");
                            color.put(v, Color.GRAY);
                            d.put(v, d.get(u) + 1);
                            pi.put(v, u);
                            Q.offer(v);
                        } else {
                            System.out.println("        Skipping " + v + " (already visited)");
                        }
                    }
                    // if thats not the case, and it was already processing, then its black.
                    // update its color for later reference.
                    color.put(u, Color.BLACK);
                    System.out.println("      Finished processing " + u + " (marked BLACK)");

                }

                System.out.println("    BFS completed in " + iterations + " iterations");

                // converting distance values before storing them in the cache.
                // because unreachable nodes get distance max val, just use -1.
                Map<String, Integer> finalDistances = new HashMap<>();
                System.out.println("    Converting distances:");
                for (String v : nodes) {
                    if (d.get(v) == Integer.MAX_VALUE) {
                        System.out.println("      " + v + ": unreachable (setting to -1)");
                        finalDistances.put(v, -1);
                    } else {
                        System.out.println("      " + v + ": distance = " + d.get(v));
                        finalDistances.put(v, d.get(v));
                    }
                    // cache so that we don't run BFS on same source node agian.
                    distanceCache.put(source, finalDistances);
                    // then store all parent pointers from this source.
                    parentCache.put(source, pi);
                    // this is a map recording "whose this node's parent?"
                    // without this pointer - we only know "v3 is 3 hops away from v0"
                    // however we don't the path we took - so parent cache is a call back to that.
                }

                System.out.println("    Cached results for source " + source);
                System.out.println("    distanceCache now has " + distanceCache.size() + " entries");
                System.out.println("    parentCache now has " + parentCache.size() + " entries");

            }

            System.out.println("\nFinished test case " + i);

        }
    }
}