package Shortestpathtest;


import static org.junit.Assert.assertEquals;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Graph;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlgorithmTest {

	static GraphReader reader;
	static Graph graph;
	static ArrayList<int[]> points = new ArrayList<int[]>();
	static int echantSize = 50;
 
    @BeforeClass
    public static void initAll() throws IOException {
    	// Create a graph reader.
        String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/midi-pyrenees.mapgr";

        reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        // Read the graph.
        graph = reader.read();
        
        for (int i = 0; i<echantSize ; i++)
        {
        	int randomDep = ThreadLocalRandom.current().nextInt(0, graph.size());
        	int randomArr = ThreadLocalRandom.current().nextInt(0, graph.size());
        	if (graph.get(randomDep).getPoint().distanceTo(graph.get(randomArr).getPoint())<10000) 
        	{
        		i-=1;
        	}
        	else {
        		int[] list = {randomDep,randomArr};
        		points.add(list);
        	}
        	
        }
                
    }

    @Test
    public void testAlgo() {
    	ArrayList<ArrayList<Float>> results = new ArrayList<ArrayList<Float>>(); //Arraylist d'arraylist des résultats
    	ArrayList<Long> ttempsDji = new ArrayList<Long>(); //Tableau contenant les temps de Djikstra
    	ArrayList<Long> ttempsAStar = new ArrayList<Long>(); //Tableau contenant les temps de A*
    	
    	ShortestPathData spd;
    	DijkstraAlgorithm da;
    	AStarAlgorithm asa;
    	ShortestPathSolution sps;
    	ShortestPathSolution spsr;
    	int i = 0; //Compteur
    	long tempsDji = 0; //Variable permettant d'avoir le temps d execution de Djikstra à chaque itération
    	long tempsAStar = 0; //Variable permettant d'avoir le temps d execution de A* à chaque itération
    	int cptErr = 0; //Compteur d'erreur
    	int cptDjiGagnant = 0; //Compteur lorsque Djikstra est gagnant
    	int cptAStarGagnant = 0; //Compteur lorsque A* est gagnant
    	int cptEgalite = 0; //Compteur lorsqu il y a egalité
    	long sigmaTempsDji = 0; //Somme des temps Djikstra
    	long sigmaTempsAStar = 0; //Somme des temps A*
    	
    	for (int[] d : points) //On parcourt la liste des points initialisés
    	{
    		results.add(new ArrayList<Float>()); //On créé la liste des résultats
    		spd = new ShortestPathData(graph, graph.get(d[0]), graph.get(d[1]), ArcInspectorFactory.getAllFilters().get(7)); //On initialise le ShortestPathData
    		da = new DijkstraAlgorithm(spd); //Initialisation Djikstra
    		asa = new AStarAlgorithm(spd); //Initialisation A*
    		tempsDji -= System.currentTimeMillis(); //Compteur de temps Djikstra
    		sps = da.run(); //On lance Djikstra
    		tempsDji += System.currentTimeMillis();
    		
    		tempsAStar -= System.currentTimeMillis();
    		spsr = asa.run();
    		tempsAStar += System.currentTimeMillis();
    		
    		if (sps.isFeasible()) //Si faisable via Djikstra
    		{
    			results.get(i).add(sps.getPath().getLength()); //On ajoute la distance à l'array des résultats
    			results.get(i).add((float)sps.getPath().getMinimumTravelTime()); //On ajoute le temps de transfert
    		}
    		else { //Sinon on ajoute 0
    			results.get(i).add((float)0);
    			results.get(i).add((float)0);
    		}
    		if (spsr.isFeasible()) //idem
    		{
    			results.get(i).add(spsr.getPath().getLength());
        		results.get(i).add((float)spsr.getPath().getMinimumTravelTime());
    		}
    		else
    		{
    			results.get(i).add((float)0);
    			results.get(i).add((float)0);
    		}
    		i++;
    		
    		//On ajoute le temps pris par les algos aux array correspondantes
    		ttempsDji.add(tempsDji); 
    		ttempsAStar.add(tempsAStar);
    		tempsDji = 0;
        	tempsAStar = 0;
        	System.out.println(i);
    	}
    	
    	i = 0;
    	
    	//Parcours des résultats
    	for (ArrayList<Float> d: results)
    	{
    		//Si les résultats de Djikstra et A* sont égaux (10% de marge)

    		if (((Math.abs(d.get(0).doubleValue() - d.get(2).doubleValue())/d.get(0).doubleValue() < 0.10) && (Math.abs(d.get(1).doubleValue() - d.get(3).doubleValue())/d.get(1).doubleValue() < 0.10)) || (d.get(0).doubleValue()==0 && d.get(1).doubleValue()==0 && d.get(2).doubleValue() == 0 && d.get(3).doubleValue() == 0))
    		{
    			//Si les deux ont été aussi rapides
    			if ((Math.abs(ttempsDji.get(i) - ttempsAStar.get(i))<0.10*ttempsDji.get(i)))
        		{
        			cptEgalite++;
        		}
    			//Djikstra plus rapide
        		else if (ttempsDji.get(i) < ttempsAStar.get(i))
        		{
        			cptDjiGagnant++;
        		}
    			//A* plus rapide
        		else 
        		{
        			cptAStarGagnant++;
        		}

    		}
    		else {
    			cptErr++;
    			System.out.println(d);
    		}
    		sigmaTempsDji += ttempsDji.get(i);
    		sigmaTempsAStar += ttempsAStar.get(i);
    		i++;
    	}
    	
    	//Affichage des résultats
    	System.out.println("Taille de l'échantillon : " + echantSize);
    	System.out.println("Taux d'erreurs: " + (cptErr));
    	System.out.println("Dji Gagnant : " + Integer.toString(cptDjiGagnant));
    	System.out.println("A* Gagnant : " + Integer.toString(cptAStarGagnant));
    	System.out.println("Egalite : " + Integer.toString(cptEgalite));
    	System.out.println("Rapport de temps : " + (((float)sigmaTempsAStar)/((float)sigmaTempsDji)));

    }
}
