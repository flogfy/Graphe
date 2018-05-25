package org.insa.algo.utils;

import org.insa.graph.*;

public class Label implements Comparable<Label>{
	private double cout;
	private Arc precedent;
	private boolean marquage;
	private int id;
	private Node n;
	
	public Label(Node N) {
		this.cout=Double.POSITIVE_INFINITY;
		this.precedent=null;
		this.marquage=false;
		this.id = N.getId();
		this.n=N;
	}

	
	
	@Override
	public int compareTo(Label o) {
		// TODO Auto-generated method stub
		return comparer(o);
	}
	public int comparer(Label o) {
	return (int)(this.getCouttotal()-o.getCouttotal());
	}
	public Node getNode() {
		return n;
	}
	
	
	
	public double getCout() {
		return cout;
	}
	public double getCouttotal() {
		return(this.cout);
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
