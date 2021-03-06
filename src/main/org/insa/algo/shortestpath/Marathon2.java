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


public abstract class Marathon2 extends ShortestPathAlgorithm{
	
	
	
	private ArrayList<Arc> listearcs;//pour les arcs de ce chemin
	ShortestPathAlgorithm dijkstraAlgo; //algo qu'on va utiliser
	private float distance;
	private boolean trouve;
	
	public Marathon2(ShortestPathData data,ArrayList<Arc> listearcs, float distance) {
		
		super(data);
		this.listearcs = new ArrayList<>();
		this.distance=distance;
		
	}
	public ArrayList<Arc> getListearcs() {
		return listearcs;
	}

	public void setListearcs(ArrayList<Arc> listearcs) {
		this.listearcs = listearcs;
	}

	public float getDistance() {
		return distance;
	}

	public boolean isTrouve() {
		return trouve;
	}
	public void setTrouve(boolean trouve) {
		this.trouve = trouve;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}

	
	
	
	
	
	public ShortestPathSolution doRun() 
    {
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
		// Initialize array of distances.
        //ArrayList<Label> listelabel = new ArrayList<>(nbNodes);
        Label[] listelabel = new Label[nbNodes];
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
        Label labeli=new Label(data.getOrigin(),data.getDestination());
    	labeli.setCout(0); //on met le 1er noeud à 0
        notifyOriginProcessed(data.getOrigin());
        listelabel[data.getOrigin().getId()]=labeli;
        distances.insert(labeli);
        


       
        
		// Initialize array of successors.
		//Arc[] successorsArcs = new Arc[nbNodes];
		
        
        //Iterations
        Label lab;
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
        		if (!data.isAllowed(arccc)) {
					continue;
        		}
        		if(arccc.getDestination()!=x.getNode()) //tous les arcs qui partent de x
        		{
        			Node y=arccc.getDestination(); //on recupere le successeur
        			if(listelabel[y.getId()]==null)
        			{
        				lab = new Label(y,data.getDestination());
        				listelabel[y.getId()]=lab;
        			}
        			else
        			{
        				lab = listelabel[y.getId()];
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
		
		ArrayList<Arc> quatrearcs=new ArrayList<>();
		for (int i=0; i<5;++i) {
			Arc a=arcs.get(i);
			quatrearcs.add(a);
		}
		
		setListearcs(listearcs+quatrearcs);

		// Create the final solution.
		solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        
        return solution;
    }
	
	
	
	
	
	
	
	
	
	

	}
	

