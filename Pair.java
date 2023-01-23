public class Pair implements Comparable<Pair>{
    // declare all required fields
    private char value;
    private double prob;

    //constructor
    public Pair(char value, double prob){
        this.value = value;
        this.prob = prob;
    }
    //getters

    public char getValue() {
        return value;
    }

    public double getProb() {
        return prob;
    }
    //setters

    public void setProb(double prob) {
        this.prob = prob;
    }

    public void setValue(char value) {
        this.value = value;
    }
    //toString
    public String toString(){
        return (Character.toString(value));
    }
    /**
     The compareTo method overrides the compareTo method of the
     Comparable interface.
     */
    @Override
    public int compareTo(Pair p){
        return Double.compare(this.getProb(), p.getProb());
    }
}
