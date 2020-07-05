package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, Gerry Bong, Yang Zuo
 * with help from https://github.com/yngz/cs61b/tree/master/proj2c/bearmaps/proj2c
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private List<Point> points;
    private Map<Point, Node> pointToNode;
    private MyTrieSet trieSet;
    private Map<String, List<Node>> nameToNodes;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);

        List<Node> nodes = this.getNodes();
        //Create an array of points to construct KDTree
        points = new ArrayList<>();
        //Create a map<Point, Node> to convert point to node
        pointToNode = new HashMap<>();

        //Create a Trie Set for the prefix autocomplete
        trieSet = new MyTrieSet();
        //Create a map<String(cleanName), Node> for easy search for Part III
        nameToNodes = new HashMap<>();

        for (Node n : nodes) {
            // Only insert points with neighbors to array and map
            if (neighbors(n.id()).size() > 0) {
                Point p = new Point(n.lon(), n.lat());
                points.add(p);
                pointToNode.put(p, n);
            }

            // Insert vertex names into Trie Set
            if (n.name() != null) {
                String cleanName = cleanString(n.name());
                trieSet.add(cleanName);

                // Add list of nodes with same name into a map<String, List<Node>>
                if (!nameToNodes.containsKey(cleanName)) {
                    nameToNodes.put(cleanName, new LinkedList<>());
                }
                nameToNodes.get(cleanName).add(n);
            }
        }
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        PointSet kdTree = new KDTree(points);
        Point nearestPoint = kdTree.nearest(lon, lat);
        Node nearestNode = pointToNode.get(nearestPoint);

        return nearestNode.id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        //use lab 9 trie set to generate list of strings with the prefix
        List<String> cleanNames = trieSet.keysWithPrefix(cleanString(prefix));
        List<String> fullNames = new LinkedList<>();
        for (String name : cleanNames) {
            for (Node n : nameToNodes.get(name)) {
                if (!fullNames.contains(n.name())) {
                    fullNames.add(n.name());
                }
            }
        }
        return fullNames;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> locationList = new LinkedList<>();
        String cleanName = cleanString(locationName);
        if (nameToNodes.containsKey(cleanName)) {
            for (Node n : nameToNodes.get(cleanName)) {
                Map<String, Object> location = new HashMap<>();
                location.put("lat", n.lat());
                location.put("lon", n.lon());
                location.put("name", n.name());
                location.put("id", n.id());
                locationList.add(location);
            }
        }
        return locationList;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
