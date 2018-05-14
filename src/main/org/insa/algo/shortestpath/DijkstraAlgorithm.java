package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.ElementNotFoundException;
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
        BinaryHeap<Label> distances = new BinaryHeap<Label>();
        for (Node n : graph) {
        	
        	Label labeli=new Label(n);
        	if(n.getId()==data.getOrigin().getId())
    			{
    				labeli.setCout(0); //on met le 1er noeud à 0
    				distances.insert(labeli);
    			}

        	listelabel.add(labeli);
        	
        }


       
        
		// Initialize array of successors.
		//Arc[] successorsArcs = new Arc[nbNodes];
		
        
        //Iterations
        
        while(!distances.isEmpty()) //tq l'arbre n'est pas vide
        {

        	Label x = distances.findMin();
        	distances.remove(x);
        	if(x.equals(data.getDestination())) 
        	{ //si on est arrivées au bout
        		break;
        	}
        	x.setMarquage(true);


        	for (Arc arccc : graph.get(x.getId())) //tous les arcs entrants ou sortants de x
        	{
        		if(arccc.getDestination()!=graph.get(x.getId())) //tous les arcs qui partent de x
        		{
        			Node y=arccc.getDestination(); //on recupere le successeur

        			for(Label lab : listelabel)
        			{

        				if(lab.getId()==y.getId()) //label qui correspond au sucesseur
        				{
        					if(lab.isMarquage()==false)
        					{

        						if(lab.getCout()>x.getCout()+data.getCost(arccc))
        						{
        							lab.setCout(x.getCout()+data.getCost(arccc));
        							try
        							{
        								distances.remove(lab);
        							}
        							catch(ElementNotFoundException E) {}
        							finally
        							{
        								distances.insert(lab);

        								lab.setPrecedent(arccc); //on initialise son predecesseur

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
	        		
	    					arcs.add(l.getPrecedent());
	    					noeudintermediaire=l.getPrecedent().getOrigin();
	    					break;
	    			
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