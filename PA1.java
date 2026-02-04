// PA1 Skeleton Code
// DSA2, spring 2026

// This code will read in the input, and put the values into lists.  It is up
// to you to properly represent this as a graph -- this code only reads in the
// input properly.

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

        for (int i = 0; i < test_cases; i++) {

            // read in n, e, and q
            int n = stdin.nextInt();
            int e = stdin.nextInt();
            int q = stdin.nextInt();

            // create the list of nodes
            ArrayList<String> nodes = new ArrayList<String>();
            for (int j = 0; j < n; j++)
                nodes.add("v" + j);

            // read in the edges
            ArrayList<Pair> edges = new ArrayList<Pair>();
            for (int j = 0; j < e; j++)
                edges.add(new Pair(stdin.next(), stdin.next()));

            // read in the queries
            ArrayList<Pair> queries = new ArrayList<Pair>();
            for (int j = 0; j < q; j++)
                queries.add(new Pair(stdin.next(), stdin.next()));

            // System.out.println("test case " + i + ":");
            // System.out.println("there are " + n + " nodes, " + e + " edges, and " + q + "
            // queries");
            // System.out.println("the nodes are: " + nodes);
            // System.out.println("the edges are: " + edges);
            // System.out.println("the queries are: " + queries);
            // System.out.println("");

            // YOUR CODE HERE (or called from here)
            Map<String, List<String>> graph = new HashMap<>();

            for (String node : nodes) {
                graph.put(node, new ArrayList<>()); // fixed
            }

            for (Pair edge : edges) {
                graph.get(edge.s).add(edge.t);
                graph.get(edge.t).add(edge.s);
            }

            // creating a cache for BFS results.
            Map<String, Map<String, Integer>> distanceCache = new HashMap<>();
            Map<String, Map<String, String>> parentCache = new HashMap<>();

            for (Pair query : queries) {
                String source = query.s;
                String destination = query.t;

                if (!distanceCache.containsKey(source)) {

                    /*
                     * BFS from slides: color, d, pi. Will use INTEGER.MAX_VALUE for d.
                     * Q is regular q, linkedlist version.
                     */
                    // 3 maps - color, d, pi. Each are <String, X> respectively.
                    // X is either Color, Integer, or String.
                    Map<String, Color> color = new HashMap<>();
                    Map<String, Integer> d = new HashMap<>();
                    Map<String, String> pi = new HashMap<>();
                    Queue<String> Q = new ArrayDeque<>();

                    // adding enum
                    // preprocessing for all nodes - setting up initial state.
                    // prof said white is unprocessed. New/fresh.
                    // for all nodes v in the group of nodes, put white for their color
                    // put their distance as infinity and their parent as null.
                    // Map being brought to default state for all nodes.
                    for (String v : nodes) {
                        color.put(v, Color.WHITE);
                        d.put(v, Integer.MAX_VALUE);
                        pi.put(v, null);
                    }

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

                    while (!Q.isEmpty()) {
                        // queue logic as per slides.
                        String u = Q.poll();
                        // System.out.println("Processing node: " + u);
                        List<String> Adj_u = graph.getOrDefault(u, new ArrayList<>());

                        // for a node v - loop through its neighbours stored in adj list.
                        // if that color is white then put it into processing.
                        // record its distance and parent and offer it to the queue.
                        for (String v : Adj_u) {
                            if (color.get(v) == Color.WHITE) {
                                color.put(v, Color.GRAY);
                                d.put(v, d.get(u) + 1);
                                pi.put(v, u);
                                Q.offer(v);
                            }
                        }
                        // if thats not the case, and it was already processing, then its black.
                        // update its color for later reference.
                        color.put(u, Color.BLACK);

                    }

                    // converting distance values before storing them in the cache.
                    // because unreachable nodes get distance max val, just use -1.
                    Map<String, Integer> finalDistances = new HashMap<>();
                    for (String v : nodes) {
                        if (d.get(v) == Integer.MAX_VALUE) {
                            finalDistances.put(v, -1);
                        } else {
                            finalDistances.put(v, d.get(v));
                        }
                    }
                    // cache so that we don't run BFS on same source node agian.
                    distanceCache.put(source, finalDistances);
                    // then store all parent pointers from this source.
                    parentCache.put(source, pi);
                    // this is a map recording "whose this node's parent?"
                    // without this pointer - we only know "v3 is 3 hops away from v0"
                    // however we don't the path we took - so parent cache is a call back to that.
                }

                // Retrieve cached results for this query
                Map<String, Integer> cachedD = distanceCache.get(source);
                Map<String, String> cachedPi = parentCache.get(source);

                // Find next hop
                int distance = cachedD.get(destination);
                if (distance == -1) { // bfs never reached it.
                    System.out.println(source + " " + destination + " None None");
                } else {
                    String nextHop = methodForHop(cachedPi, source, destination);
                    if (nextHop == null) {
                        System.out.println(source + " " + destination + " None None");
                    } else {
                        System.out.println(source + " " + destination + " " + distance + " " + nextHop);
                    }
                }
            }

            if (i < test_cases - 1) {
                System.out.println();
            }

        }

    }
    /*
     * test case 0:
     * there are 7 nodes, 10 edges, and 10 queries
     * the nodes are: [v0, v1, v2, v3, v4, v5, v6]
     * the edges are: [v1->v2, v0->v3, v4->v6, v3->v6, v3->v4, v4->v5, v0->v6,
     * v3->v5, v0->v2, v2->v5]
     * the queries are: [v1->v0, v3->v5, v1->v5, v0->v2, v6->v1, v0->v3, v4->v3,
     * v3->v6, v2->v0, v5->v0]
     * 
     * Exception in thread "main" java.lang.NullPointerException: Cannot invoke
     * "java.util.List.add(Object)" because the return value of
     * "java.util.Map.get(Object)" is null
     * at PA1.main(PA1.java:69)
     * ram@Ramkrishnas-MacBook-Pro PA %
     */

    private static String methodForHop(Map<String, String> pi, String source, String destination) {

        if (source.equals(destination)) {
            return destination;
        }
        String current = destination;
        String parent = pi.get(current);

        if (pi.get(destination) == null) {
            return null;
        }

        boolean reachedSource = false;
        while (parent != null && !reachedSource) {
            if (parent.equals(source)) {
                reachedSource = true;
            } else {
                current = parent;
                parent = pi.get(current);
            }
        }

        if (parent == null) {
            return null;
        } else {
            return current;
        }

    }

}