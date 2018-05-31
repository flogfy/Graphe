package Shortestpathtest;
import static org.junit.Assert.*;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.algo.AbstractInputData.Mode;
import org.insa.graph.AccessRestrictions.AccessMode;
import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.BinaryPathReader;
import org.insa.graph.io.GraphReader;
import org.insa.graph.io.PathReader;
import java.io.*;
import java.time.LocalTime;
public abstract class testscenario {
	
	protected String algo;//On entre le nom de l'algo que l'on veut tester ( DIjkstra ou AStar
	
	testscenario(String algo) {
		this.algo = algo;
	}
	
	protected abstract ShortestPathAlgorithm testedAlgo (ShortestPathData testdata);
    
    private static ShortestPathAlgorithm oracleAlgo (ShortestPathData testdata) {
    	return new BellmanFordAlgorithm(testdata);
    }
    /* Pour executer soit AStar soit Dijkstra, retourne la solution de l'algo*/
    private static ShortestPathSolution solution (ShortestPathAlgorithm algo) {
    	return algo.doRun();
    }

	public void scenarioTest(String mapName, String pathName, Mode mode, AccessMode acces) throws Exception {
		
		//On va créer le scénario et d'abord on va retrouver le graphe et le chemin en fonction du nom qu'on a passé en paramètre
		
	/******recherche du graphe et chemin******/
		
		// Create a graph reader.
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		//System.out.println("Graphe prêt à être lu");

		//Lecture du graphe
		Graph graph = reader.read();

		//System.out.println("Fin de la lecture.");

		//Creation d'un lecteur de chemin
		PathReader pathReader = new BinaryPathReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))));

		//Read the path.
		Path path = pathReader.readPath(graph);
		System.out.println("ok");
		
	/********SCENARIO******/
		
		
		//Creation scenario
		scenariotest scenario = new scenariotest(acces, mode, graph, path.getOrigin(), path.getDestination());
		
		//Creation des données pour les algos
		ShortestPathData testdata = null;
		if (scenario.getType() == Mode.TIME) { //Si on veut le chemin le plus court en temps
			switch (scenario.getacces()) { //On va tester le moyen de transport désiré
			case MOTORCAR: { 
				testdata = new ShortestPathData(graph, scenario.getorigine(), scenario.getDestination(), ArcInspectorFactory.getAllFilters().get(3));
				break;
			}
			case FOOT: {
				testdata = new ShortestPathData(graph, scenario.getorigine(), scenario.getDestination(), ArcInspectorFactory.getAllFilters().get(4));
				break;
			}
			case BICYCLE: {
				testdata = new ShortestPathData(graph, scenario.getorigine(), scenario.getDestination(), ArcInspectorFactory.getAllFilters().get(7));
				break;
			}
			default:
				testdata = new ShortestPathData(graph, scenario.getorigine(), scenario.getDestination(), ArcInspectorFactory.getAllFilters().get(2));
				break;
			}
		} else if (scenario.getType() == Mode.LENGTH) {//SI on veut le plus court chemin en longueur
			switch (scenario.getacces()) {
			case MOTORCAR: {
				testdata = new ShortestPathData(graph, scenario.getorigine(), scenario.getDestination(), ArcInspectorFactory.getAllFilters().get(1));
				break;
			}
			case FOOT: {
				testdata = new ShortestPathData(graph, scenario.getorigine(), scenario.getDestination(), ArcInspectorFactory.getAllFilters().get(5));
				break;
			}
			case BICYCLE: {
				testdata = new ShortestPathData(graph, scenario.getorigine(), scenario.getDestination(), ArcInspectorFactory.getAllFilters().get(6));
				break;
			}
			default:
				testdata = new ShortestPathData(graph, scenario.getorigine(), scenario.getDestination(), ArcInspectorFactory.getAllFilters().get(0));
				break;
			}
		}
		
		//Creation algos
		ShortestPathAlgorithm testAlgo = testedAlgo(testdata);//Va renvoyer un algo Dijkstra ou Astar avec les donnéees de test choisies au dessus
		ShortestPathAlgorithm oracle = oracleAlgo(testdata); //Renvoie un algo BellmanFord avec les données de test choisies au dessus
		
		//Creation solutions
		ShortestPathSolution Solutionobtenue = solution(testAlgo);//Resultat de l'ASTAR ou Dijkstra
		ShortestPathSolution Solutionattendue = solution(oracle);//L'oracle est Bellmann-ford, c'est la référence
		//Check if both solutions are feasible or unfeasible
		assertEquals(Solutionobtenue.isFeasible(), Solutionattendue.isFeasible());
		if (!Solutionobtenue.isFeasible() && !Solutionattendue.isFeasible()) {
			System.out.println("Chemin trouvé comme convenu");
		} else if (Solutionobtenue.isFeasible() && !Solutionattendue.isFeasible()) {
			System.out.println("Chemin trouvé alors que ca ne devrait pas");
		} else if (!Solutionobtenue.isFeasible() && Solutionattendue.isFeasible()) {
			System.out.println("Chemin non trouvé alors que ca aurait dû");
		} else {
		//On recupère les chemins
			Path algoPath = Solutionobtenue.getPath();
			Path oraclePath = Solutionattendue.getPath();
			
			if (scenario.getType() == Mode.TIME) { //Si on recherchait le PCC en temps
				if (algoPath.getMinimumTravelTime() == oraclePath.getMinimumTravelTime() ) {
					System.out.println("Temps egaux");
				} else {
					System.out.println("Différence de temps");
				}
				System.out.println(scenario.getacces().toString() + " Time "+ this.algo + ": " + LocalTime.MIN.plusSeconds((long)algoPath.getMinimumTravelTime()).toString()  + " Time BellmanFord: " + LocalTime.MIN.plusSeconds((long)oraclePath.getMinimumTravelTime()).toString());
			} else if (scenario.getType() == Mode.LENGTH) {//Si on recherchait le PCC en longueur
				if (algoPath.getLength() == oraclePath.getLength()) {
					System.out.println("LOngueurs égales");
				} else {
					System.out.println("Différence de longueurs");
				}
				System.out.println(scenario.getacces().toString() + " Length " + this.algo + ": " + algoPath.getLength() + "m Length BellmanFord: " + oraclePath.getLength()+ "m");
			}
		}
	}
	
	public void algoIsValid() throws Exception { //Lancement du test, comprenant le choix de la carte, du chemin, du PCC voulu et du moyen de transport
	
		String path = "/home/radureau/Documents/workspaceeclipse/Graphe";
		
	
		String[][] files = new String[17][2];//Tableau en deux dimensions, l'un pour le nom du chemin, l'autre pour le nom de la MAP
		files[0][0] = "path_fr31_insa_aeroport_length.path";
		files[0][1] = "haute-garonne.mapgr";
		files[1][0] = "path_fr31_insa_aeroport_time.path";
		files[1][1] = "haute-garonne.mapgr";
		files[2][0] = "path_fr31_insa_bikini_canal.path";
		files[2][1] = "haute-garonne.mapgr";
		files[3][0] = "path_fr31insa_rangueil_insa.path";
		files[3][1] = "insa.mapgr";
		files[4][0] = "path_fr31insa_rangueil_r2.path";
		files[4][1] = "insa.mapgr";
		//chemin uniquement voiture
		files[5][0] = "path_fr31_highway_152765_152762.path";
		files[5][1] = "haute-garonne.mapgr";
		//chemins uniquement piétons
		files[6][0] = "path_fr31_pedestrian_39847_39843.path";
		files[6][1] = "haute-garonne.mapgr";
		files[7][0] = "path_frn_pedestrian_609543_360479.path";
		files[7][1] = "midi-pyrenees.mapgr";
		files[8][0] = "path_frn_pedestrian_214906_436038.path";
		files[8][1] = "midi-pyrenees.mapgr";
		//chemin uniquement voiture
		files[9][0] = "path_frn_highway_p1_32224_288320.path";
		files[9][1] = "midi-pyrenees.mapgr";
		//chemins dans les 2 sens
		files[10][0] = "path_frn_67506_35474.path";
		files[10][1] = "midi-pyrenees.mapgr";
		files[11][0] = "path_frn_35474_67506.path";
		files[11][1] = "midi-pyrenees.mapgr";
		// chemins ABC puis AB et BC
		files[12][0] = "ABC_car_l_path_frn_558384_293387.path";
		files[12][1] = "midi-pyrenees.mapgr";
		files[13][0] = "AB_car_path_frn_558384_181643.path";
		files[13][1] = "midi-pyrenees.mapgr";
		files[14][0] = "AB_foot_path_frn_558384_154982.path";
		files[14][1] = "midi-pyrenees.mapgr";
		files[15][0] = "BC_car_path_frn_181643_293387.path";
		files[15][1] = "midi-pyrenees.mapgr";
		files[16][0] = "BC_foot_path_frn_154982_293387.path";
		files[16][1] = "midi-pyrenees.mapgr";

		
		for(String[] file : files) {
	        String pathName = path + "/Paths/" + file[0];
	        String mapName  = path + "/Maps/" + file[1];
	        //scenarioTest(mapName, pathName, Mode.LENGTH, AccessMode.FOOT);
	        System.out.println( "\n" + file[0] + " & " + file[1]);
	        if (file[0].equals(files[13][0]) || file[0].equals(files[15][0])) {
				scenarioTest(mapName, pathName, Mode.TIME, AccessMode.MOTORCAR);
				System.out.println("ok");
				scenarioTest(mapName, pathName, Mode.LENGTH, AccessMode.MOTORCAR);
	        } else if (file[0].equals(files[14][0]) || file[0].equals(files[16][0])) {
				scenarioTest(mapName, pathName, Mode.TIME, AccessMode.FOOT);
				scenarioTest(mapName, pathName, Mode.LENGTH, AccessMode.FOOT);
				scenarioTest(mapName, pathName, Mode.TIME, AccessMode.BICYCLE);
				scenarioTest(mapName, pathName, Mode.LENGTH, AccessMode.BICYCLE);
	        } else {
				scenarioTest(mapName, pathName, Mode.TIME, AccessMode.MOTORCAR);
				scenarioTest(mapName, pathName, Mode.LENGTH, AccessMode.MOTORCAR);
				scenarioTest(mapName, pathName, Mode.TIME, AccessMode.FOOT);
				scenarioTest(mapName, pathName, Mode.LENGTH, AccessMode.FOOT);
				scenarioTest(mapName, pathName, Mode.TIME, AccessMode.BICYCLE);
				scenarioTest(mapName, pathName, Mode.LENGTH, AccessMode.BICYCLE);
	        }
		}
	}

	
}
