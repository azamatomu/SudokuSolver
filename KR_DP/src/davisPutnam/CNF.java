package davisPutnam;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class CNF {

    private List<Clause> cnf;
    private boolean SAT;
    private int clauses_satisfied;

    public CNF()    {
        cnf = new ArrayList<Clause>();

    }


    public void addClause(Clause clause)    {
        this.cnf.add(clause);

    }

    public int getNumberSatisfied() {
        clauses_satisfied = 0;
        for (Clause clause: this.cnf)   {
            if (clause.getSatisfied())  {
                clauses_satisfied++;
            }
        }

        return this.clauses_satisfied;
    }

    public int getNumberClauses()   {
        return this.cnf.size();
    }

    public int[] evaluateClauses(HashMap<String, Boolean> posVarMap, HashMap<String, Boolean> negVarMap, boolean save)   {
        int[] made_break = new int[2];
        int made_clauses_satisfied = 0;
        int broke_clauses_satisfied = 0;
        for (Clause clause : this.cnf)  {
            //System.out.println("Clause checked: "+clause.toString());
            boolean clause_new_value = clause.evaluate(posVarMap, negVarMap, save);
            boolean current_value = clause.getSatisfied();
            if  (!current_value && clause_new_value)  {
                made_clauses_satisfied++;
            }
            else  if (current_value && !clause_new_value)   {broke_clauses_satisfied++;}
            //System.out.println("Clause value after evaluation: "+clause.getSatisfied());
        }
        made_break[0] = made_clauses_satisfied;
        made_break[1] = broke_clauses_satisfied;
        return made_break;

    }

    public List<Clause> getCNF() {return this.cnf;}


    public String toString()    {
        // Create a new StringBuilder.
        StringBuilder builder = new StringBuilder();

        // Loop and append values.

        int formula_length = cnf.size();
        if (formula_length==0)  {
            return "";
        }

        builder.append("(");
        for (int i = 0; i < formula_length; i++)    {
            if ((formula_length-1)==i)   {
                builder.append(this.cnf.get(i).toString()+")\n");
            }
            else    {
                builder.append(this.cnf.get(i).toString()+" ^ ");
            }
        }
        // Convert to string.
        String result = builder.toString();
        return result;
    }


}
