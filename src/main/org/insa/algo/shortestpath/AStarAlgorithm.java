package org.insa.algo.shortestpath;
import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.ElementNotFoundException;
import org.insa.algo.utils.Label;
import org.insa.algo.utils.LabelStar;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class AStarAlgorithm extends DijkstraAlgorithm 
{

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
        @Override
        public ShortestPathSolution doRun() {
            ShortestPathData data = getInputData();
            Graph graph = data.getGraph();
            final int nbNodes = graph.size();
    		// Initialize array of distances.
            //ArrayList<Label> listelabel = new ArrayList<>(nbNodes);
            LabelStar[] listelabel = new LabelStar[nbNodes];
            BinaryHeap<Label> distances = new BinaryHeap<Label>();
         /*   for (Node n : graph) {
            	
            	Label labeli=new Label(n);
            	if(n.getId()==data.getOrigin().getId())
        			{
            			notifyOriginProcessed(n);
        				labeli.setCout(0); //on met le 1er noeud à 0
        				distances.insert(labeli);
        			}

            	listelabel.add(labeli);*/
            LabelStar labeli=new LabelStar(data.getOrigin());
        	labeli.setCout(0); //on met le 1er noeud à 0
            notifyOriginProcessed(data.getOrigin());
            listelabel[data.getOrigin().getId()]=labeli;
            distances.insert(labeli);
            


           
            
    		// Initialize array of successors.
    		//Arc[] successorsArcs = new Arc[nbNodes];
    		
            
            //Iterations
            LabelStar lab;
            boolean realisable=false;
            while(!distances.isEmpty()) //tq l'arbre n'est pas vide
            {

            	Label x = distances.findMin();
            	distances.remove(x);
            	if((x.getNode()).equals(data.getDestination())) 
            	{ //si on est arrivées au bout
            		notifyDestinationReached(x.getNode());
            		realisable=true;
            		break;
            	}
            	x.setMarquage(true);
            	notifyNodeMarked(x.getNode());
            	for (Arc arccc : graph.get(x.getId())) //tous les arcs entrants ou sortants de x
            	{
            		if(arccc.getDestination()!=x.getNode()) //tous les arcs qui partent de x
            		{
            			Node y=arccc.getDestination(); //on recupere le successeur
            			if(listelabel[y.getId()]==null)
            			{
            				lab = new LabelStar(y);
            				listelabel[y.getId()]=lab;
            			}
            			else
            			{
            				lab = listelabel[y.getId()];
            			}
            				if((lab.isMarquage()==false)&&(lab.getCouttotal()>x.getCouttotal()+data.getCost(arccc))) //label qui correspond au successeur
            				{
            				
            							lab.setCout(x.getCout()+data.getCost(arccc));
            							lab.setCoutdestination(x.getNode().getPoint().distanceTo(data.getDestination().getPoint())); //distance à vol d'oiseau entre le pt actuel et la dest
            							lab.setCouttotal(lab.getCout()+lab.getCoutdestination());
            							notifyNodeReached(y);
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
            
          //Partir de la destination ( data.getdestination)  en retrouvant le label associé a data destination,
            //et a chaque fois on regarde le precedent jusqu"a retomber sur data.getOrigin
            ShortestPathSolution solution = null;
            if(!realisable) {
            	solution=new ShortestPathSolution(data, Status.INFEASIBLE, null);
            	return solution;
    		}
           
            ArrayList<Arc> arcs = new ArrayList<>();
            Node noeudintermediaire = data.getDestination();
            while(data.getOrigin()!=noeudintermediaire)
            {
    	        Label l=listelabel[noeudintermediaire.getId()];	
    	       

    		    	arcs.add(l.getPrecedent());
    	        
    	        
    	    	noeudintermediaire=l.getPrecedent().getOrigin();
            }
            
        	// Reverse the path...
    		Collections.reverse(arcs);

    		// Create the final solution.
    		solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));


            
            
            return solution;
    }
        
}  


