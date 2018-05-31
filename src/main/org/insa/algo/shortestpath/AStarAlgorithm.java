package org.insa.algo.shortestpath;
import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.ElementNotFoundException;
import org.insa.algo.utils.LabelStar;
import org.insa.algo.utils.Label;
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
    public ShortestPathSolution doRun()
    {
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        LabelStar[] listeLabelStar = new LabelStar[nbNodes];
        BinaryHeap<Label> distances = new BinaryHeap<Label>();
     
        LabelStar LabelStari=new LabelStar(data.getOrigin(),data.getDestination());
    	LabelStari.setCout(0); //on met le 1er noeud à 0
    	LabelStari.setCouttotal((0));
        notifyOriginProcessed(data.getOrigin());
        listeLabelStar[data.getOrigin().getId()]=LabelStari;
        distances.insert(LabelStari);
                
        //Iterations
        LabelStar lab;
        boolean realisable=false;
        while(!distances.isEmpty()) //tq l'arbre n'est pas vide
        {

        	Label x = distances.findMin();
        	distances.remove(x);
        	LabelStar xx=(LabelStar) x;
        	if((xx.getNode()).equals(data.getDestination())) 
        	{ //si on est arrivées au bout
        		notifyDestinationReached(xx.getNode());
        		realisable=true;
        		break;
        	}
        	xx.setMarquage(true);
        	notifyNodeMarked(xx.getNode());
        	for (Arc arccc : graph.get(xx.getId())) //tous les arcs entrants ou sortants de x
        	{
        		if (!data.isAllowed(arccc)) {
					continue;
        		}
        		if(arccc.getDestination()!=xx.getNode()) //tous les arcs qui partent de x
        		{
        			Node y=arccc.getDestination(); //on recupere le successeur
        			if(listeLabelStar[y.getId()]==null)
        			{
        				lab = new LabelStar(y,data.getDestination());
        				listeLabelStar[y.getId()]=lab;
        			}
        			else
        			{
        				lab = listeLabelStar[y.getId()];
        			}
        				if((lab.isMarquage()==false)&&(lab.getCouttotal()>(xx.getCouttotal()+data.getCost(arccc)))) //LabelStar qui correspond au successeur
        				{
        				
							lab.setCout(xx.getCout()+data.getCost(arccc));
							lab.setCoutdestination(xx.getNode().getPoint().distanceTo(data.getDestination().getPoint())); //distance à vol d'oiseau entre le pt actuel et la dest
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
      //Partir de la destination ( data.getdestination)  en retrouvant le LabelStar associé a data destination,
        //et a chaque fois on regarde le precedent jusqu"a retomber sur data.getOrigin
        ShortestPathSolution solution = null;
        if(!realisable) 
        {
        	solution=new ShortestPathSolution(data, Status.INFEASIBLE, null);
        	return solution;
		}
       
        ArrayList<Arc> arcs = new ArrayList<>();
        Node noeudintermediaire = data.getDestination();
        while(data.getOrigin()!=noeudintermediaire)
        {
	        LabelStar l=listeLabelStar[noeudintermediaire.getId()];	
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


