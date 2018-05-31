package Shortestpathtest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Point;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;

public class astartest {

	 // Small graph use for tests
    protected static Graph graph;

    // List of nodes
    protected static Node[] nodes;

    // List of points
    protected static Point[] points;
   
    
    
    @BeforeClass
    public static void initAll() throws IOException {

        // Create nodes
        nodes = new Node[6];
        points = new Point[6];
      	points[0]= new Point(0,1);
      	points[1]= new Point(1,2);
      	points[2]= new Point(1,0);
      	points[3]= new Point(2,3);
      	points[4]= new Point(3,2);
      	points[5]= new Point(3,0);
        for (int i = 0; i < nodes.length; ++i) {
      
            nodes[i] = new Node(i, points[i]);
            
        }

        Node.linkNodes(nodes[0], nodes[1], 7,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[0], nodes[2], 8,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[1], nodes[3], 4,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[1], nodes[4], 1,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[1], nodes[5], 5,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[0], 7,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[1], 2,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[5], 2,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[4], nodes[2], 2,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[4], nodes[3], 2,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[4], nodes[5], 3,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[5], nodes[4], 3,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        
        graph = new Graph("ID", "", Arrays.asList(nodes), null);
    }
    
    protected ShortestPathAlgorithm testedAlgo (ShortestPathData testdata) {
    	return new AStarAlgorithm(testdata);
    }
    
    private ShortestPathAlgorithm oracleAlgo (ShortestPathData testdata) {
    	return new BellmanFordAlgorithm(testdata);
    }
    
    public ShortestPathSolution solution (ShortestPathAlgorithm algo) {
    	return algo.doRun();
    }
    	
    void testIsValid(ShortestPathSolution tested, ShortestPathSolution oracle) {
		assertEquals(tested.getPath().isValid(), oracle.getPath().isValid());
    }
    
   void testSameLength(ShortestPathSolution tested, ShortestPathSolution oracle) {
		assertEquals(tested.getPath().getLength(), oracle.getPath().getLength(), 1e-6);
    }
    
    void testSameTime(ShortestPathSolution tested, ShortestPathSolution oracle) {
		assertEquals(tested.getPath().getMinimumTravelTime(), oracle.getPath().getMinimumTravelTime(), 1e-6);
    }
    
    void testSameSize(ShortestPathSolution tested, ShortestPathSolution oracle) {
		assertEquals(tested.getPath().size(), oracle.getPath().size());
    }
   
    @Test
    public void BellmanAstarSameShortestPath() {
		String message = "";
		//Generate pairs
		for (int i = 0; i < nodes.length; ++i) {
			for (int j = 0; j < nodes.length; ++j) {
				if (i != j) {
					ShortestPathData testdata = new ShortestPathData(graph, nodes[i], nodes[j], ArcInspectorFactory.getAllFilters().get(0));
					
					ShortestPathAlgorithm astarAlgo = testedAlgo(testdata);
					ShortestPathAlgorithm bellmanAlgo = oracleAlgo(testdata);
					
					ShortestPathSolution astarSolution = solution(astarAlgo);
					ShortestPathSolution bellmanSolution = solution(bellmanAlgo);
					
					//Check if both solutions are feasible or unfeasible
					assertEquals(astarSolution.isFeasible(), bellmanSolution.isFeasible());
					
					if (astarSolution.isFeasible() && bellmanSolution.isFeasible()) {
						//Check if both paths are valid
						testIsValid(astarSolution,bellmanSolution);
						//Check if both paths have the same length
						testSameLength(astarSolution,bellmanSolution);
						//Check if both paths take the same time
						//testSameTime(astarSolution,bellmanSolution);
						//Check if both paths have the same size
						//testSameSize(astarSolution,bellmanSolution);
						message += astarSolution.getPath().getLength() + " ";
					} else {
						message+= "inf ";
					}
					
				} else {
					message+= "- ";
				}
			}
			System.out.println(message);
			message="";
		}
}
	
	
}
