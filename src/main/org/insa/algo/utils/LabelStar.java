package org.insa.algo.utils;

import org.insa.graph.Node;




public class LabelStar extends Label{
	
	private double coutdestination;
	private double couttotal;
	public LabelStar(Node N) {
		super(N);
		this.coutdestination=Double.POSITIVE_INFINITY;
		this.couttotal=Double.POSITIVE_INFINITY;;
		
		// TODO Auto-generated constructor stub
	}
	public int comparer(LabelStar o) {
		if (this.couttotal-o.getCouttotal()==0) {
			return(int)(this.coutdestination-o.getCoutdestination());
		}
		else
		{
			return (int)(this.getCouttotal()-o.getCouttotal());
		}
	}
	public double getCoutdestination() {
		return coutdestination;
	}
	public void setCoutdestination(double coutdestination) {
		this.coutdestination = coutdestination;
	}
	public double getCouttotal() {
		return couttotal;
	}
	public void setCouttotal(double couttotal) {
		this.couttotal = couttotal;
	}
	

}
