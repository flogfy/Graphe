package org.insa.algo.utils;

import org.insa.graph.*;

public class Label implements Comparable<Label>{
	private double cout;
	private Arc precedent;
	private boolean marquage;
	private int id;
	
	public Label(Node N) {
		this.cout=Double.POSITIVE_INFINITY;
		this.precedent=null;
		this.marquage=false;
		this.id = N.getId();
	}

	@Override
	public int compareTo(Label o) {
		// TODO Auto-generated method stub
		return (int)(this.cout-o.cout);
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
