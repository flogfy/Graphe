package Shortestpathtest;
import org.insa.algo.AbstractInputData.Mode;
import org.insa.graph.AccessRestrictions.AccessMode;
import org.insa.graph.Graph;
import org.insa.graph.Node;
public class scenariotest {
		
		private Mode type;
		private AccessMode acces;
		private Graph carte;
		private Node origine;
		private Node destination;

		public scenariotest(AccessMode acces, Mode type, Graph graph, Node origine, Node destination) {
			this.type = type;
			this.carte = graph;
			this.origine = origine;
			this.destination = destination;
			this.acces = acces;
		}

		public Mode getType() {
			return type;
		}

		public void setType(Mode type) {
			this.type = type;
		}

		public Graph getcarte() {
			return carte;
		}

		public void setcarte(Graph carte) {
			this.carte = carte;
		}

		public Node getorigine() {
			return origine;
		}

		public void setorigine(Node origine) {
			this.origine = origine;
		}

		public Node getDestination() {
			return destination;
		}

		public void setDestination(Node destination) {
			this.destination = destination;
		}

		public AccessMode getacces() {
			return acces;
		}

		public void setacces(AccessMode acces) {
			this.acces = acces;
		}

	}
