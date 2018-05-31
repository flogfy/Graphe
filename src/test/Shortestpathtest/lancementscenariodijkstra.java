package Shortestpathtest;

import org.insa.algo.shortestpath.*;


public class lancementscenariodijkstra extends testscenario {

	lancementscenariodijkstra() {
		super("DijkstraAlgorithm");
	}


	protected ShortestPathAlgorithm testedAlgo (ShortestPathData testdata) {
		return new DijkstraAlgorithm(testdata);
	}

	public static void main(String[] args) throws Exception {
		lancementscenariodijkstra test = new lancementscenariodijkstra();
		test.algoIsValid();
	}

}