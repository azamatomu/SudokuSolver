package davisPutnam;
public class Literal {

    private String literal;
    private boolean negated;
    private boolean ground_truth;


    public Literal(String literal, boolean negated, boolean ground_truth) {
        this.literal = literal;
        // if positive then the sign is true, else false
        this.negated = negated;
        this.ground_truth = ground_truth;

    }

    // getters
    public String getLiteral()  {return this.literal;}
    public boolean getSign()    {return this.negated;}
    public boolean getGround()  {return this.ground_truth;}

    public String toString()    {
        return this.literal;
    }
}
