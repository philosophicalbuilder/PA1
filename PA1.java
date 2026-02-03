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

            System.out.println("test case " + i + ":");
            System.out.println("there are " + n + " nodes, " + e + " edges, and " + q + " queries");
            System.out.println("the nodes are: " + nodes);
            System.out.println("the edges are: " + edges);
            System.out.println("the queries are: " + queries);
            System.out.println("");

            // YOUR CODE HERE (or called from here)
            Map<String, List<String>> graph = new HashMap<>();

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

                    color.put(source, Color.GRAY);
                    d.put(source, 0);

                    pi.put(source, null);
                    Q.offer(source);

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
                    System.out.println("Processing node: " + u);
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

            }
        }
    }
}