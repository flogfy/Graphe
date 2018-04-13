package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;

import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.Label;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
		// Initialize array of distances.
        ArrayList<Label> listelabel = new ArrayList<>();
        for (Node n : graph) {
        	Label labeli=new Label(n);
        	listelabel.add(labeli);
        }
        listelabel.get(data.getOrigin().getId()).setCout(0);
        BinaryHeap<Node> distances = new BinaryHeap<Node>();
        distances.insert(data.getOrigin());
        
        
        
        //Iterations
        while(!listelabel.get(data.getDestination().getId()).isMarquage()) {
        	Node x = distances.findMin();
        	listelabel.get(x.getId()).setMarquage(true);
        	
        }
        for(Label l : listelabel) {
        		
        	
        }

        
        
        
        
        
		
		// Initialize array of successors.
				Arc[] successorsArcs = new Arc[nbNodes];
		
        ShortestPathSolution solution = null;
        // TODO:
        return solution;
    }

}
