package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.Label;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() 
    {
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        //final int nbNodes = graph.size();
		// Initialize array of distances.
        ArrayList<Label> listelabel = new ArrayList<>();
        for (Node n : graph) {
        	Label labeli=new Label(n);
        	listelabel.add(labeli);
        }
        listelabel.get(data.getOrigin().getId()).setCout(0); //on met à 0 le premier noeud
        BinaryHeap<Node> distances = new BinaryHeap<Node>();
        distances.insert(data.getOrigin());
        
		// Initialize array of successors.
		//Arc[] successorsArcs = new Arc[nbNodes];
		
        
        //Iterations
        while(!listelabel.get(data.getDestination().getId()).isMarquage())
        {
        	Node x = distances.findMin();
        	listelabel.get(x.getId()).setMarquage(true);
        	 for(Label labx : listelabel) 
        	 {
        		 if(labx.getId()==x.getId()) 
        		 {
        		 
		        	for (Arc arccc : x) 
					{
		        		if(arccc.getDestination()!=x)
		        		{
		        			Node y=arccc.getDestination();
		        		
		        			 for(Label lab : listelabel)
		        		        {
		        		
		        		    		if(lab.getId()==y.getId())
		        		    		{
		        		    			Label x2=lab;
		        		    			if(x2.isMarquage()==false)
		        		    			{
		        		    				x2.setCout(Math.min(x2.getCout(),(labx.getCout()+arccc.getLength())));
				                			if(x2.getCout()==labx.getCout()+arccc.getLength())
				                			{
				                				for(Node noeud: graph) 
				                				{
				                					if(noeud.getId()==x2.getId()) 
				                					{
				                						distances.insert(noeud);
				                        				x2.setPrecedent(x);
				                					}
				                				}
				                			}
				        				}
				        			}
				        			
				        		}
			    		}
			    	}
        		 }
        	 }
        	}
      //Partir de la destination ( data.getdestination)  en retrouvant le label associé a data destination,
        //et a chaque fois on regarde le precedent jusqu"a retomber sur data.getOrigin		
        ShortestPathSolution solution = null;
        ArrayList<Arc> arcs = new ArrayList<>();
        Node noeudintermediaire = data.getDestination();
        while(data.getOrigin()!=noeudintermediaire)
        {
	        for(Label l : listelabel) 
			{
	        	if(l.getId()==noeudintermediaire.getId())
	        	{
	        		for (Arc arc : data.getDestination()) 
	    			{
	    				if(arc.getOrigin()==l.getPrecedent())
	    				{
	    					arcs.add(arc);
	    					noeudintermediaire=l.getPrecedent();
	    				}
	    			}
	        	}
			}
        }
    	// Reverse the path...
		Collections.reverse(arcs);

		// Create the final solution.
		solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        // TODO:
        return solution;
}
    
}