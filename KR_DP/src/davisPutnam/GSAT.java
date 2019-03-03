package davisPutnam;
import javax.lang.model.element.VariableElement;
import java.io.*;
import java.util.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Map;

import static java.util.stream.Collectors.toMap;


public class GSAT {

    public int solution;
    public String cnf_solution;
    public CNF cnf_problem;
    public List<Literal> variableList;
    public HashMap<String, Boolean> posVarMap;
    public HashMap<String, Boolean> negVarMap;
    public Random randomGenerator;
    public boolean SAT;
    public String filepath;


    public GSAT(CNF cnf_problem, String filepath)   {
        this.cnf_problem = cnf_problem;
        this.variableList = new ArrayList<Literal>();
        this.posVarMap = createVariableMap(true);
        this.negVarMap = createVariableMap(false);
        randomGenerator = new Random();
        this.filepath = filepath;
    }

    public boolean GSATSolve() throws IOException {
        // Random assignation for all the variables
        this.randomAssignation();
        boolean save = true;

        this.cnf_problem.evaluateClauses(this.posVarMap, this.negVarMap, save);
        // get the number of satisfied clauses
        int satisfied_clauses = cnf_problem.getNumberSatisfied();
        System.out.println("Number of Initial Satisfied Clauses: "+satisfied_clauses);
        int clause_number = cnf_problem.getNumberClauses();
        int flip_num = 0;
        int old_satisfied_clauses = 0;
        int stuck = 0;
        int count = 0;
        for (int MAXFLIPS = 0; MAXFLIPS < 12; MAXFLIPS++)    {

            for (Literal literal : variableList)    {
                if  (clause_number==satisfied_clauses)  {
                    System.out.println("CNF Solved");
                    System.out.println("Number of satisfied Clauses: "+satisfied_clauses+", out of: "+clause_number);
                    System.out.println("Number of total Variable Flips: "+flip_num);
                    this.SAT = true;
                    writeSolution(this.filepath);
                    return true;
                }

                String variable = literal.getLiteral();
                boolean negated = literal.getSign();

                if (negated)    {
                    if (literal.getGround())    {
                        negVarMap.put(variable,true);
                        posVarMap.put(variable.substring(1),false);
                        continue;
                    }
                    else {
                        boolean value = negVarMap.get(variable);
                        negVarMap.put(variable, !value);
                        posVarMap.put(variable.substring(1), value);
                    }
                }
                else    {
                    if (literal.getGround())    {
                        negVarMap.put(variable,false);
                        posVarMap.put(variable.substring(1),true);
                        continue;
                    }
                    else {
                        boolean value = posVarMap.get(variable);
                        posVarMap.put(variable, !value);
                        negVarMap.put('-' + variable, value);
                    }
                }
                this.cnf_problem.evaluateClauses(this.posVarMap, this.negVarMap, save);
                int new_satisfied_clauses = cnf_problem.getNumberSatisfied();

                if (new_satisfied_clauses == old_satisfied_clauses) {
                    if (stuck >= 30) {
                        System.out.println("Local Minimum Detected, Applying a Random Walk...");
                        int index = randomGenerator.nextInt(variableList.size());
                        Literal random_literal = variableList.get(index);
                        String rand_var = random_literal.getLiteral();
                        boolean rand_neg = random_literal.getSign();
                        if (random_literal.getGround())    {
                            continue;
                        }
                        if (rand_neg)    {
                            boolean rand_value = negVarMap.get(rand_var);
                            negVarMap.put(rand_var,!rand_value);
                            posVarMap.put(rand_var.substring(1),rand_value);
                        }
                        else    {
                            boolean rand_value = posVarMap.get(rand_var);
                            posVarMap.put(rand_var,!rand_value);
                            negVarMap.put('-'+rand_var,rand_value);

                        }
                        this.cnf_problem.evaluateClauses(this.posVarMap, this.negVarMap, save);
                        new_satisfied_clauses = cnf_problem.getNumberSatisfied();
                        satisfied_clauses = new_satisfied_clauses;
                        flip_num++;

                    }
                    stuck++;

                }
                else    {stuck = 0;}
                old_satisfied_clauses = new_satisfied_clauses;
                if (new_satisfied_clauses >= satisfied_clauses) {
                    satisfied_clauses = new_satisfied_clauses;
                    flip_num++;
                }
                else    {
                    if (negated)    {
                        boolean value = negVarMap.get(variable);
                        negVarMap.put(variable,!value);
                        posVarMap.put(variable.substring(1),value);
                    }
                    else    {
                        boolean value = posVarMap.get(variable);
                        posVarMap.put(variable,!value);
                        negVarMap.put('-'+variable,value);
                    }
                }


            }
        }
        System.out.println("\nGSAT-Random-Walk is Stuck in a Local Minimum");
        System.out.println("Number of satisfied Clauses: "+satisfied_clauses+", out of: "+clause_number);
        System.out.println("Number of total Variable Flips: "+flip_num);
        System.out.println("No Solution Found\n");
        this.SAT = false;
        writeSolution(this.filepath);
        return false;
    }


    public void printSudoku(int[][] sudoku) {
        for (int[] row : sudoku)    {
            for (int colnum : row)  {
                System.out.print(colnum+" ");
            }
            System.out.print("\n");
        }
    }

    public int[][] printSolution(boolean only_pos, int dim) {
        int[][] sudoku = new int[dim][dim];
        for (Literal literal : variableList)    {
            String lit = literal.getLiteral();
            boolean negated = literal.getSign();
            if (!negated)  {
                boolean value = posVarMap.get(lit);
                if (value && only_pos)  {
                    //System.out.println(lit + ": " +value);
                    sudoku[Character.getNumericValue(lit.charAt(0)) - 1][Character.getNumericValue(lit.charAt(1)) - 1] = Character.getNumericValue(lit.charAt(2));

                }

            }

            if (negated && !only_pos) {
                boolean value = negVarMap.get(lit);
                System.out.println(lit+": "+value);

            }

        }
        return sudoku;
    }

    public void writeSolution(String filepath) throws IOException {
        if (!this.SAT)  {
            Writer writer = new FileWriter(filepath + ".out");
            writer.write("");

        }
        else    {
            int truecount = 0;
            for (Literal literal : variableList)    {
                String lit = literal.getLiteral();
                boolean negated = literal.getSign();
                if (!negated)   {
                    boolean value = posVarMap.get(lit);
                    if (value)  {
                        truecount++;
                    }
                }
            }

            Writer writer = new FileWriter(filepath + ".out");
            writer.write("p cnf " + truecount + " " + truecount + "\n");
            for (Literal literal : variableList)    {
                String lit = literal.getLiteral();
                boolean negated = literal.getSign();
                if (!negated)  {
                    boolean value = posVarMap.get(lit);
                    if (value)  {
                        writer.write(lit+ " 0\n");
                    }
                }
            }
            writer.close();
        }
    }

    public HashMap<String, Boolean> createVariableMap(boolean positive_map)   {
        HashMap<String, Boolean> variableMap = new HashMap<String,Boolean>();

        for (Clause clause : cnf_problem.getCNF()) {
            for (Literal literal : clause.getClause()) {
                String variable = literal.getLiteral();
                boolean iscontained = variableMap.containsKey(variable);
                boolean isnegated = literal.getSign();
                if (positive_map && !isnegated && !iscontained) {
                    variableMap.put(variable, false);
                    variableList.add(literal);
                } else if (!positive_map && isnegated && !iscontained) {
                    variableMap.put(variable, false);
                    variableList.add(literal);
                }
            }
        }

        return variableMap;
    }

    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }

    public void randomAssignation() {

        for (Literal literal : this.variableList)   {
            String variable = literal.getLiteral();
            boolean negated = literal.getSign();
            if (!negated)   {
                this.posVarMap.put(variable, this.getRandomBoolean());
            }
            else    {
                this.negVarMap.put(variable, this.getRandomBoolean());
            }
        }

        // correct/coordinate the tables
        for (Literal literal : this.variableList)   {
            String variable = literal.getLiteral();
            boolean negated = literal.getSign();
            if (!negated)   {
                String negatedVar = '-'+variable;
                if (this.negVarMap.containsKey(negatedVar))  {
                    boolean varValue = this.posVarMap.get(variable);
                    this.negVarMap.put(negatedVar,!varValue);
                }
            }
        }
    }
}
