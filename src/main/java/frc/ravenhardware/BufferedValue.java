package frc.ravenhardware;

import java.util.LinkedList;

public class BufferedValue {
	protected int listSize = 29;
	
	LinkedList<Double> values;
	
	public BufferedValue() {
		values = new LinkedList<Double>();
    }
    
    public BufferedValue(int listSize) {
        values = new LinkedList<Double>();

        this.listSize = listSize;
    }
	
	// Adds the current value to the list, and
	// removes the first item if the list is larger than the list size.
	public void maintainState(double currentValue) {
		values.add(currentValue);
		
		if (values.size() > listSize) {
			values.remove();
		}
    }
    
    public void setSize(int size) {
        this.listSize = size;
    }
	
	public double getMean() {
		double cumulativeValue = 0;
		
		for (Double value : values) {
			cumulativeValue += value;
		}
				
		return cumulativeValue / values.size();
    }
    
    public double getMedian() {
        double medianValue = 0;

        return medianValue;
    }

}