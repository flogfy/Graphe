package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.ElementNotFoundException;
import org.insa.algo.utils.Label;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class Marathon extends ShortestPathAlgorithm {
	
	public int compteur;
	
	public Marathon(ShortestPathData data, int compteur) {
        super(data);
        this.compteur=compteur;
    }

    @Override
	public ShortestPathSolution doRun() 
    {
        compteur=0;
    	ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        // Initialize array of distances.
        //ArrayList<Label> listelabel = new ArrayList<>(nbNodes);
        Label[] listelabel = new Label[nbNodes];
        BinaryHeap<Label> distances = new BinaryHeap<Label>();
    
        Label labeli=new Label(data.getOrigin(),data.getDestination());
    	labeli.setCout(0); //on met le 1er noeud à 0
        notifyOriginProcessed(data.getOrigin());
        listelabel[data.getOrigin().getId()]=labeli;
        distances.insert(labeli);
        
		
        
        //Iterations
        Label lab;
        boolean realisable=false;
        int compteur=0;
        double longueuractuelle=0;
        while(!(distances.isEmpty())&&(compteur<5)) //tq l'arbre n'est pas vide et qu'on a pas 4 arcs
        {
        	int somme4arcs=0;
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
        	compteur=compteur+1;
        	if(compteur==20) 
        	{
        		compteur=0;
        		for (Arc arccc : graph.get(x.getId())) //tous les arcs entrants ou sortants de x
            	{
            		if (!data.isAllowed(arccc)) //si on peut y aller à pied
            		{
    					continue;
            		}
            		if(arccc.getDestination()!=x.getNode()) //tous les arcs qui partent de x
            		{
            			
            			Node y=arccc.getDestination(); //on recupere le successeur
            			if(y.getPoint().distanceTo(data.getDestination().getPoint())>42.195)
            			{
            				continue;
            			}
            			else
            			{
            				//Lancer algo dijkstra et recuperer la longueur
	            			
	            			if(solution.getLength()+longueuractuelle<=42.195){
	            				if(listelabel[y.getId()]==null)
	                			{
	                				lab = new Label(y,data.getDestination());
	                				listelabel[y.getId()]=lab;
	                			}
	                			else
	                			{
	                				lab = listelabel[y.getId()];
	                			}
	            				
		            			lab.setCout(x.getCout()+data.getCost(arccc));
		            			longueuractuelle=lab.getCout();
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
									compteur+=1;
									continue;
								}
							}
	            			
            			}
            		}
            	}
        	}
        	
        			
        				if((lab.isMarquage()==false)&&(lab.getCout()>x.getCout()+data.getCost(arccc))) //label qui correspond au successeur
        				{
        				
        							lab.setCout(x.getCout()+data.getCost(arccc));
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
