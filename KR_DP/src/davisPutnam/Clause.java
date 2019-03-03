package davisPutnam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Clause {

    private List<Literal> clause;
    private boolean satisfied;

    public Clause() {

        this.clause = new ArrayList<Literal>();
    }


    public void addLiteral(Literal literal)    {
        this.clause.add(literal);
    }

    public boolean evaluate(HashMap<String, Boolean> posVarMap, HashMap<String, Boolean> negVarMap, boolean save)   {

        for (Literal literal : this.clause)  {
            //if (this.clause.size() == 1) {return true;}
            boolean negated = literal.getSign();
            String variable = literal.getLiteral();
            if (negated)    {
                boolean value = negVarMap.get(variable);
                if (value) {
                    if (save)   {
                        this.satisfied = true;
                    }
                    return true;
                }
            }
            else    {
                boolean value = posVarMap.get(variable);
                if (value)  {
                    if (save)   {
                        this.satisfied = true;
                    }
                    return true;
                }
            }
        }
        if (save)   {
            this.satisfied = false;
        }
        return false;
    }


    public List<Literal> getClause()    {return this.clause;}

    public boolean getSatisfied()   {return this.satisfied;}


    public String toString() {
        // Create a new StringBuilder.
        StringBuilder builder = new StringBuilder();

        // Loop and append values.

        int clause_length = clause.size();
        if (clause_length==0)   {
            return "";
        }
        builder.append("(");
        for (int i = 0; i < clause_length; i++)    {
            if ((clause_length-1)==i)   {
                builder.append(this.clause.get(i).toString()+")");
            }
            else    {builder.append(this.clause.get(i).toString()+" v ");}
        }
        // Convert to string.
        String result = builder.toString();
        return result;
    }
}
