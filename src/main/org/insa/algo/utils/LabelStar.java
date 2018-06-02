package org.insa.algo.utils;

import org.insa.graph.Node;




public class LabelStar extends Label{
	
	private double coutdestination;
	private double couttotal;
	public LabelStar(Node N,Node destination) {
		super(N, destination);
		this.coutdestination=Double.POSITIVE_INFINITY;
		this.couttotal=Double.POSITIVE_INFINITY;
		
		// TODO Auto-generated constructor stub
	}
	@Override
	public int comparer(Label o) {
		LabelStar l = (LabelStar) o;
		l.setCoutdestination(l.getNode().getPoint().distanceTo(l.getDestination().getPoint()));
		l.setCouttotal(l.getCout()+l.getCoutdestination());
		if (this.couttotal-l.getCouttotal()<=0.005 && this.couttotal-l.getCouttotal()>=-0.005) {
			return(int)(this.coutdestination-l.getCoutdestination());
		}
		else
		{
			return (int)(this.couttotal-l.getCouttotal());
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
