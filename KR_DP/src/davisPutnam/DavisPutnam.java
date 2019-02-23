package davisPutnam;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class DavisPutnam {
	public static ArrayList<String> intoOneList(ArrayList<ArrayList<String>> bigList) {
		ArrayList<String> newList = new ArrayList<String>(); 
		for (int i = 0; i<bigList.size(); i++) {
			for (int k = 0; k < bigList.get(i).size(); k ++) {
				newList.add(bigList.get(i).get(k));
			}
		}
		return newList;
	}

	public static void pureLiteral(ArrayList<String> oneclauses, Boolean[] literals) {
		for (int i = 0; i < oneclauses.size(); i ++) {
	    	if (oneclauses.get(i).charAt(0) == '-') {
	    		if(!oneclauses.contains(oneclauses.get(i).substring(1))) {
	    			literals[1000 + Integer.parseInt(oneclauses.get(i)) -1] = true;
	    			literals[1000 - Integer.parseInt(oneclauses.get(i)) -1] = false;
	    			System.out.println("Pure negative literal at " + oneclauses.get(i));
	    		}
	    	} else {
	    		if(!oneclauses.contains('-' + oneclauses.get(i))) {
	    			literals[1000 + Integer.parseInt(oneclauses.get(i)) -1] = true;
	    			literals[1000 - Integer.parseInt(oneclauses.get(i)) -1] = false;
	    			System.out.println("Pure positive literal at " + oneclauses.get(i));
	    		}
	    	}
	    }
	}

	public static int unitLiteral(ArrayList<ArrayList<String>> clauses, Boolean[] literals) {
		Random r = new Random();
		for (int i = 0; i < clauses.size(); i ++) {
			int satcount = 0;
			int unsatcount = 0;
			int nullcount = 0;
			ArrayList<Integer> ii = new ArrayList<Integer>();
			ArrayList<Integer> uu = new ArrayList<Integer>();
			for (int m = 0; m < clauses.get(i).size(); m ++) {
				if (literals[1000 + Integer.parseInt(clauses.get(i).get(m)) - 1] != null) {
					if (literals[1000 + Integer.parseInt(clauses.get(i).get(m)) - 1] == true) {
						satcount += 1;
					}
					else {
						unsatcount += 1;
						uu.add(Integer.parseInt(clauses.get(i).get(m)));
					}
				} else {
					nullcount += 1;
					ii.add(Integer.parseInt(clauses.get(i).get(m)));
				}
			}
			if (nullcount == 1 && (clauses.get(i).size() - unsatcount == nullcount)) {
				for (int j = 0; j < ii.size(); j ++) {
					literals[1000 + ii.get(j) - 1] = true;
					literals[1000 - ii.get(j) - 1] = false;
					return ii.get(j);
				} 
			}
			if (satcount == 0 && unsatcount != 0 && nullcount == 0) {
				int m = r.nextInt(uu.size());
				m = uu.size() - 1;
				//literals[1000 + uu.get(m) - 1] = true;
				//literals[1000 - uu.get(m) - 1] = false;
				//System.out.println(uu.size() + " " + m);
				//System.out.println("Unsat literal at " + uu + ", in " + clauses.get(i));
    			//printLiterals(literals, clauses.get(i));
			}
	    }
		return 0;
	}
	
	public static void tautology(ArrayList<ArrayList<String>> clauses, Boolean[] clausetrue) {
		for (int i = 0; i < clauses.size(); i ++) {
	    	for (int m = 0; m < clauses.get(i).size(); m ++) {
	    		if(clauses.get(i).contains(clauses.get(i).get(m)) && clauses.get(i).contains('-' + clauses.get(i).get(m)) || clauses.get(i).contains(clauses.get(i).get(m)) && clauses.get(i).contains(clauses.get(i).get(m).substring(1))) {
	    			clausetrue[i] = true;
	    			System.out.println("Tautology at ");
	    		}
	    	}
	    }
	}
	
	public static boolean doTrue(ArrayList<ArrayList<String>> clauses, Boolean[] literals, Boolean[] clausetrue) {
		boolean anychanges = false;
		for (int i = 0; i < clauses.size(); i ++) {
			int count = 0;
			int nulls = 0;
			for (int m = 0; m < clauses.get(i).size(); m ++) {
				if (literals[1000 + Integer.parseInt(clauses.get(i).get(m)) -1] != null) {
					if (literals[1000 + Integer.parseInt(clauses.get(i).get(m)) -1] == true) {
						count += 1;
					}
				} else {
					nulls+=1;
				}
			}
			if (count == 0 && nulls == 0) {
				if (clausetrue[i] == null | (clausetrue[i] != null && clausetrue[i] == true)) {
					anychanges = true;
				}
				clausetrue[i] = false;
			} else if (count > 0 && nulls == 0) {
				if (clausetrue[i] == null | (clausetrue[i] != null && clausetrue[i] == false)) {
					anychanges = true;
				}
				clausetrue[i] = true;
			}
			if (nulls>0) clausetrue[i] = null; 
		}
		return anychanges;
	}
	
	public static int simplify(ArrayList<ArrayList<String>> clauses, Boolean[] literals, Boolean[] clausetrue) {
		boolean anychanges = true;
		    anychanges = false;
		    ArrayList<String> oneclauses = intoOneList(clauses);
		    //pureLiteral(oneclauses, literals);
		    int litchange = unitLiteral(clauses, literals);
		    anychanges = doTrue(clauses, literals, clausetrue);
		    return litchange;
		   
	}

	public static ArrayList<String> extract_literals(ArrayList<ArrayList<String>> clauses) {
		ArrayList<String> literals = new ArrayList<String>();
		for (int i=0; i<clauses.size(); i++) { // loop over the clauses
			for (int j=0; j<clauses.get(i).size(); j++) { // loop over the literals
				if (!clauses.get(i).contains(clauses.get(i).get(j))) { // literal not yet in arraylist
					literals.add(clauses.get(i).get(j));
				}
			}
		}
		return literals;
	}

	public static String jeroslaw_wang(ArrayList<ArrayList<String>> clauses, ArrayList<String> unique_literals, Boolean[] literals) {
		String literal = "";
		double jw_score = 0.0;
		for (int i=0; i<unique_literals.size(); i++) { // loop over the literals
			double current_jw_score = 0.0;
			for (int j=0; j<clauses.size(); j++) { // loop over all clauses
				if (clauses.get(j).contains(unique_literals.get(i))) { // if clause contains literal
					jw_score += Math.pow(2, -clauses.get(j).size());
				}
			}
			if (current_jw_score > jw_score) { // if the jeroslaw-wang score is higher; update
				if (literals[1000 + Integer.parseInt(unique_literals.get(i)) - 1] == null) { // if the literal is not assigned yet
					jw_score = current_jw_score;
					literal = unique_literals.get(i);
				}
			}
		}
		return literal;
	}

	public static String flip_literal(String literal) {
		String flipped_literal = "";
		if (literal.substring(0, 1).equals("-")) {
			flipped_literal = literal.substring(1, literal.length());
		} else {
			flipped_literal = "-" + literal;
		}
		return flipped_literal;
	}

	public static String bohm(ArrayList<ArrayList<String>> clauses, ArrayList<String> unique_literals, Boolean[] literals) {
		String literal = "";
		double bohm_score = 0.0;
		
		int alpha = 1;
		int beta = 2;

		BiHashMap<Integer, String, Integer> clause_literal_dict = new BiHashMap<Integer, String, Integer>();

		// create the clause_literal_dict
		for (int i=0; i<clauses.size(); i++) { // loop over all clauses
			for (int j=0; j<clauses.get(i).size(); j++) { // loop over the literals
				if (clause_literal_dict.containsKeys(clauses.get(i).size(), clauses.get(i).get(j))) { // clause-literal combination already exists
					clause_literal_dict.put(clauses.get(i).size(), clauses.get(i).get(j), clause_literal_dict.get(clauses.get(i).size(), clauses.get(i).get(j)) + 1)
				} else { // clause-literal combination does not exist yet
					clause_literal_dict.put(clauses.get(i).size(), clauses.get(i).get(j), 1)
				}
			}
		}

		// calculate the bohm score for all literals
		for (int i=0; i<unique_literals.size(); i++) { // loop over all literals
			String current_literal = unique_literals.get(i);
			String flipped_literal = flip_literal(current_literal);
			
			Vector vector = new Vector();

			Enumeration clauses_literal_dict_keys = clause_literal_dict.keys();

			// populate the bohm vector
			while (clauses_literal_dict_keys.hasMoreElements()) {
				Integer current_clause_length = clauses_literal_dict_keys.nextElement();
				double score = alpha * max(clause_literal_dict.get(current_clause_length, current_literal), clause_literal_dict.get(current_clause_length, flipped_literal));
				score += beta * min(clause_literal_dict.get(current_clause_length, current_literal), clause_literal_dict.get(current_clause_length, flipped_literal));
				vector.add(score);
			}

			double current_bohm_score = 0.0;
			Iterator vector_itr = vector.iterator(); 
			
			// get the magnitude of the bohm vector
			while (vector_itr.hasNext()) { 
				current_bohm_score += Math.pow(vvector_itr.next(), 2);
			}

			if (current_bohm_score > bohm_score) { // if the bohm score is higher; update
				if (literals[1000 + Integer.parseInt(current_literal) - 1] == null) { // if the literal is not assigned yet
					bohm_score = current_bohm_score;
					literal = current_literal;
				}
			}
		}

		return literal;
	}
	
	public static int split(ArrayList<ArrayList<String>> clauses, Boolean[] literals, Boolean[] clausetrue, Boolean use_heuristic) {
		boolean cont = true;
		while (cont) {
			Random r = new Random();
			ArrayList<Integer> nulls = getnulls(clausetrue);
			if (nulls.size() != 0) {
				if (use_heuristic == true) {
					ArrayList<String> unique_literals = extract_literals(clauses);
					String literal = jeroslaw_wang(clauses, unique_literals, literals);
					int selected_literal = Integer.parseInt(literal);
				} else {
					int ind1 = r.nextInt(nulls.size());
					ind1 = nulls.get(ind1);
					int ind2 = r.nextInt(clauses.get(ind1).size());
					int selected_literal = Integer.parseInt(clauses.get(ind1).get(ind2)) - 1;
				}
				int result = r.nextInt(2);
				if (literals[1000 + selected_literal - 1] == null) {
					if (result == 1) {
						literals[1000 + selected_literal - 1] = true;
						literals[1000 - selected_literal - 1] = false;
					}
					else {
						literals[1000 + selected_literal - 1] = false;
						literals[1000 - selected_literal - 1] = true;
					}
					//System.out.println("Split in " + clauses.get(ind1).get(ind2));
					cont = false;
					return selected_literal;
				}
			} else {
				cont = false;
				return 0;
			}
		}
		return 0;
	}

	public static ArrayList<Integer> getnulls(Boolean[] clausetrue) {
		ArrayList<Integer> nulls = new ArrayList<Integer>();
		for (int i = 0; i < clausetrue.length; i ++) {
			if (clausetrue[i] == null) {
				nulls.add(i);
			}
		}
		return nulls;
	}
	public static String[] checkTrue(Boolean[] clausetrue) {
		String[] alltrue = new String[2];
		alltrue[0] = "q";
		for (int i = 0; i < clausetrue.length; i++) {
			if (clausetrue[i] != null) {
				if (clausetrue[i] == false) {
					alltrue[0] = "f";
					alltrue[1] = String.valueOf(i);
					return alltrue;
				}
			} else {
				alltrue[0] = "n";
			}
		}
		if (alltrue[0] != "n") alltrue[0] = "t";
		return alltrue;
		
	}

	public static void printLiterals(Boolean[] literals, ArrayList<String> clauses) {
		for (int i = 0; i < clauses.size(); i ++) {
			System.out.print(literals[1000 + Integer.parseInt(clauses.get(i)) - 1] + ". ");
		}
		System.out.println();
	}

	public static int whichToChange(Boolean[] clausetrue, Boolean[] literals, ArrayList<ArrayList<String>> clauses, ArrayList<Integer> litCheck) {
		for (int j =0; j < litCheck.size(); j++) {
			int lit = litCheck.get(j);
			int count = 0;
			boolean temp = literals[1000 + lit - 1];
			boolean temp2 = literals[1000 - lit - 1];
			literals[1000 + lit - 1] = temp2;
			literals[1000 - lit - 1] = temp;
			doTrue(clauses, literals, clausetrue);
			for (int i = 0; i < clausetrue.length; i ++) {
				if (clausetrue[i] != null) {
					if (!clausetrue[i]) {
						count += 1;
					}
				}
			}
			if (count == 0) {
				return lit;
			}
			literals[1000 + lit - 1] = temp;
			literals[1000 - lit - 1] = temp2;
			doTrue(clauses, literals, clausetrue);
		}
		return 0;
	}

	public static void countTruths(Boolean[] literals, Boolean[] clausetrue, ArrayList<ArrayList<String>> clauses) {
		int countL = 0;
		for (int i = 0; i < literals.length; i ++) {
			if (literals[i] != null) if (literals[i])  if (i > 1000) countL += 1;
		}
		int countC = 0;
		int countF = 0;
		for (int i = 0; i < clausetrue.length; i ++) {
			if (clausetrue[i] != null) {
				if (clausetrue[i]) {
					countC += 1;
				} else {
					countF += 1;
				}
			}
		}
		System.out.println(countC + " true clauses with " + countL + " true literals, and " + countF + " false clauses.");
	}

	public static void outputSudoku(Boolean[] literals) {
		int count = 0;
		for (int i = 0; i < literals.length; i ++) {
			if (literals[i] != null && literals[i] && i > 1000) {
				System.out.print(String.valueOf(i - 1000 + 1).charAt(2) + " ");
				count += 1;
				if (count % 9 == 0) {
					System.out.println();
				}
				
			}
		}	
	}
	// 1. Simplify
	// Tautology (only checked once)
	// If clause has 'P or P then it can be removed
	// Pure Literal
	// If literal occurs only pos or neg, it can be set true or false respectively
	// Unit Clause
	// If clause consists of one clause, it has to be set to true
	
	// 2. Split
	// Pick one predicate and set it to true
	// Simplify again
	
	// 3. Repeat 1 and 2 until convergence or until inconsistence (false clause)
	
	// 4. Backtrack
	// For literal from 2 pick another value, try again
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		// pass the path to the file as a parameter 
	    File file = 
	      new File("sudoku-rules.txt"); 
	    Scanner sc = new Scanner(file); 
	    String nextclause = sc.nextLine();
	    String[] after = nextclause.split("\\s+");
	    int maxvar;
	    int nclauses;
	    if (after[0].charAt(0) == 'p') {
    		maxvar = Integer.parseInt(after[2]);
    		nclauses = Integer.parseInt(after[3]);
    	}
	    else {
	    	System.out.println("No starting line found.");
	    	throw new FileNotFoundException();
	    }
	    File file2 = 
	  	      new File("sudoku-example.txt"); 
	    Scanner sc2 = new Scanner(file2);
	    ArrayList<ArrayList<String>> clauses = new ArrayList<ArrayList<String>>();
	    int clausen = 0; 
	    Boolean[] literals = new Boolean[2*maxvar + 1];
	    Arrays.fill(literals, null); 
	    while (sc.hasNextLine()) {
	    	int j = 0;
	    	nextclause = sc.nextLine();
	    	ArrayList<String> singleList = new ArrayList<String>();
	    	after = nextclause.split("\\s+");
	    	while (!after[j].equals("0")) {
	    		singleList.add(after[j]);
	    		literals[1000 + Integer.parseInt(after[j]) -1] = null;
	    		j += 1;
	    	}
	    	clauses.add(singleList);
	    	clausen += 1;
	    }
	    while (sc2.hasNextLine()) {
	    	int j = 0;
	    	nextclause = sc2.nextLine();
	    	ArrayList<String> singleList = new ArrayList<String>();
	    	after = nextclause.split("\\s+");
	    	while (!after[j].equals("0")) {
	    		singleList.add(after[j]);
	    		literals[1000 + Integer.parseInt(after[j]) -1] = null;
	    		j += 1;
	    	}
	    	clauses.add(singleList);
	    	clausen += 1;
	    }  
	    sc.close();
	    sc2.close();

	    Boolean[] clausetrue= new Boolean[clauses.size()];
	    Arrays.fill(clausetrue, null);
	    System.out.println("There are " + clauses.size() + " clauses.");
	    String[] cont = new String[2]; 
	    cont[0] = "n";
	    int ncount = 0;
	    Random r = new Random(); 
	    boolean anychanges;
	    ArrayList<ArrayList<Integer>> flipt = new ArrayList<ArrayList<Integer>>();
	    ArrayList<Integer> flipped = new ArrayList<Integer>();
	    ArrayList<Integer> splitd = new ArrayList<Integer>();
	    ArrayList<Integer> backtracksplitd= new ArrayList<Integer>();
	    int splitted = 0;
	    while (cont[0] != "t") {
	    	int litchange = 0;
	    	litchange = simplify(clauses, literals, clausetrue); 
	    	if (litchange==0) {
	    		splitted = split(clauses,literals, clausetrue);
	    		splitd.add(splitted);
	    		if (splitted !=0) {
	    			//System.out.println("Split " + splitted);
	    			flipt.add(flipped);
	    			flipped = new ArrayList<Integer>();
	    		}
	    	} else {
	    		flipped.add(litchange);
	    		//System.out.println("Unit " + litchange);
	    	}
	    	int falsecount = 0;	
	    	anychanges = doTrue(clauses, literals, clausetrue);
	    	cont = checkTrue(clausetrue);
	    	outer: while (cont[0] == "f") {
	    		falsecount++;
	    		if (falsecount == 1) {
	    			splitd.add(splitted);
	    			flipt.add(flipped);
	    			flipped = new ArrayList<Integer>();
	    		}
	    		//System.out.println(flipped);
	    		
	    		//System.out.println("False clause: " + clauses.get(Integer.parseInt(cont[1])));
    			//printLiterals(literals, clauses.get(Integer.parseInt(cont[1])));
	    		if (falsecount > 50 + backtracksplitd.size()) {
		    		backtracksplitd.clear();
		    		System.out.println("Reset backtrack.");
		    	}
    			for (int j = splitd.size() - 1; j >= 0; j--) {
    				for (int i = 0; i < flipt.get(j).size(); i++) {
    					literals[1000 + flipt.get(j).get(i) - 1] = null;
    	    			literals[1000 - flipt.get(j).get(i) - 1] = null;
    	    		}
    				literals[1000 + splitd.get(j) - 1] = null;
    				literals[1000 - splitd.get(j) - 1] = null;
    				//System.out.println(splitd.get(j));
    				if (!backtracksplitd.contains(splitd.get(j))) {
    					doTrue(clauses, literals, clausetrue);
    					//System.out.println("bck out!");
    				}
    				cont = checkTrue(clausetrue);
    				if (cont[0] != "f") {
    					backtracksplitd.add(splitd.get(j));
    					//System.out.println("out!");
    					break outer; 
    				}
    			}
	    	}
	    	if (ncount % 100  == 0) {
	    		System.out.println(ncount);
	    	}
	    	ncount += 1;
	    }
	    System.out.println(ncount);
	    int count = 0;
	    for (int i = 0; i < literals.length; i ++) {
	    	if (literals[i] != null) {
	    		if (literals[i] == true) {
	    			System.out.println(i - 1000 + 1);
	    			count += 1;
	    			
	    		}
	    	}	
	    }
	    System.out.println("SAT with " + count);
	    outputSudoku(literals);
	    

	}

}
