package Shortestpathtest;

import org.insa.algo.shortestpath.*;

public class lancementscenarioastar extends testscenario {

	lancementscenarioastar() {
		super("AstarAlgorithm");
	}


	protected ShortestPathAlgorithm testedAlgo (ShortestPathData testdata) {
		return new AStarAlgorithm(testdata);
	}

	public static void main(String[] args) throws Exception {
		lancementscenarioastar test = new lancementscenarioastar();
		test.algoIsValid();
	}

}
