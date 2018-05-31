package org.insa.algo.utils;

import org.insa.graph.*;

public class Label implements Comparable<Label>{
	private double cout;
	private Arc precedent;
	private boolean marquage;
	private int id;
	private Node n;
	private Node destination;
	
	public Label(Node N,Node destination) {
		this.cout=Double.POSITIVE_INFINITY;
		this.precedent=null;
		this.marquage=false;
		this.id = N.getId();
		this.n=N;
		this.destination=destination;
	}

	
	
	public Node getDestination() {
		return destination;
	}



	@Override
	public int compareTo(Label o) {
		return comparer(o);
	}
	public int comparer(Label o) {
	return (int)(this.cout-o.getCout());
	}
	public Node getNode() {
		return n;
	}
	
	public double getCoutTotal() {
		return cout;
	}
	
	public double getCout() {
		return cout;
	}


	public void setCout(double cout) {
		this.cout = cout;
	}

	public Arc getPrecedent() {
		return precedent;
	}

	public void setPrecedent(Arc precedent) {
		this.precedent = precedent;
	}

	public boolean isMarquage() {
		return marquage;
	}

	public void setMarquage(boolean marquage) {
		this.marquage = marquage;
	}

	public int getId() {
		return id;
	}
}
