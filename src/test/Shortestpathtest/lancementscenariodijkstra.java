package Shortestpathtest;

import org.insa.algo.shortestpath.*;

public class lancementscenariodijkstra extends testscenario {

	lancementscenariodijkstra() {
		super("Dijkstra");
	}


	protected ShortestPathAlgorithm testedAlgo (ShortestPathData testdata) {
		return new AStarAlgorithm(testdata);
	}

	public static void main(String[] args) throws Exception {
		lancementscenariodijkstra test = new lancementscenariodijkstra();
		test.algoIsValid();
	}

}