package davisPutnam;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Comparator;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;
import java.util.Objects;

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
	public static void pureLiteral(ArrayList<String> oneclauses, HashMap<String, Boolean> literals, ArrayList<Integer> nflips) {
		for (int i = 0; i < oneclauses.size(); i ++) {
	    	if (oneclauses.get(i).charAt(0) == '-') {
	    		if(!oneclauses.contains(oneclauses.get(i).substring(1))) {
	    			flipLiteral(literals, oneclauses.get(i), true, false, nflips);
	    			//literals[1000 + Integer.parseInt(oneclauses.get(i)) -1] = true;
	    			//literals[1000 - Integer.parseInt(oneclauses.get(i)) -1] = false;
	    			System.out.println("Pure negative literal at " + oneclauses.get(i));
	    		}
	    	} else {
	    		if(!oneclauses.contains('-' + oneclauses.get(i))) {
	    			flipLiteral(literals, oneclauses.get(i), true, false, nflips);
	    			//literals[1000 + Integer.parseInt(oneclauses.get(i)) -1] = true;
	    			//flipLiteral(literals, Integer.parseInt(oneclauses.get(i)), true, false);
	    			//literals[1000 - Integer.parseInt(oneclauses.get(i)) -1] = false;
	    			System.out.println("Pure positive literal at " + oneclauses.get(i));
	    		}
	    	}
	    }
	}
	
	public static String unitLiteral(ArrayList<ArrayList<String>> clauses, HashMap<String, Boolean> literals, ArrayList<Integer> nflips) {
		Random r = new Random();
		for (int i = 0; i < clauses.size(); i ++) {
			int satcount = 0;
			int unsatcount = 0;
			int nullcount = 0;
			ArrayList<String> ii = new ArrayList<String>();
			ArrayList<String> uu = new ArrayList<String>();
			for (int m = 0; m < clauses.get(i).size(); m ++) {
				if (literals.get(clauses.get(i).get(m)) != null) {
					if (literals.get(clauses.get(i).get(m)) == true) {
						satcount += 1;
					}
					else {
						unsatcount += 1;
						uu.add(clauses.get(i).get(m));
					}
				} else {
					nullcount += 1;
					ii.add(clauses.get(i).get(m));
				}
			}
			if (nullcount == 1 && (clauses.get(i).size() - unsatcount == nullcount)) {
				for (int j = 0; j < ii.size(); j ++) {
					flipLiteral(literals, ii.get(j), true, false, nflips);
					//literals[1000 + ii.get(j) - 1] = true;
					//literals[1000 - ii.get(j) - 1] = false;
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
		return "";
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
	
	public static boolean doTrue(ArrayList<ArrayList<String>> clauses, HashMap<String, Boolean> literals, Boolean[] clausetrue) {
		boolean anychanges = false;
		for (int i = 0; i < clauses.size(); i ++) {
			int count = 0;
			int nulls = 0;
			for (int m = 0; m < clauses.get(i).size(); m ++) {
				if (literals.get(clauses.get(i).get(m)) != null) {
					if (literals.get(clauses.get(i).get(m)) == true) {
						count += 1;
					}
				} else {
					nulls++;
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
	
	public static String simplify(ArrayList<ArrayList<String>> clauses, HashMap<String, Boolean> literals, Boolean[] clausetrue, ArrayList<Integer> nflips) {
		boolean anychanges = true;
		    anychanges = false;
		    ArrayList<String> oneclauses = intoOneList(clauses);
		    //pureLiteral(oneclauses, literals, nflips);
		    String litchange = unitLiteral(clauses, literals, nflips);
		    anychanges = doTrue(clauses, literals, clausetrue);
		    return litchange;
		   
	}
	
	public static ArrayList<String> extract_literals(ArrayList<ArrayList<String>> clauses) {
		ArrayList<String> literals = new ArrayList<String>();
		for (int i=0; i<clauses.size(); i++) { // loop over the clauses
			for (int j=0; j<clauses.get(i).size(); j++) { // loop over the literals
				if (!literals.contains(clauses.get(i).get(j))) { // literal not yet in arraylist
					literals.add(clauses.get(i).get(j));
				}
			}
		}
		return literals;
	}
	
	public static ArrayList<String> null_literals(ArrayList<String> unique_literals, HashMap<String, Boolean> literals) {
		ArrayList<String> literals_null = new ArrayList<String>();
		for (int i=0; i<unique_literals.size(); i++) {
			if (literals.get(unique_literals.get(i)) == null) {
				literals_null.add(unique_literals.get(i));
			}
		}
		return literals_null;
	}
	
	public static ArrayList<String> null_variables(ArrayList<String> unique_literals, HashMap<String, Boolean> literals) {
		ArrayList<String> variables_null = new ArrayList<String>();
		for (int i=0; i<unique_literals.size(); i++) {
			if (literals.get(unique_literals.get(i)) == null) {
				if (! unique_literals.get(i).substring(0, 1).equals("-") ) { // keep only positive literals (variables)
					variables_null.add(unique_literals.get(i));
				} else {
					if (!variables_null.contains(flip_literal(unique_literals.get(i)))) {
						variables_null.add(flip_literal(unique_literals.get(i)));
					}
				}
			}
		}
		return variables_null;
	}
	
	public static ArrayList<ArrayList<String>> null_clauses(ArrayList<ArrayList<String>> clauses, Boolean[] clausetrue) {
		ArrayList<ArrayList<String>> clauses_null = new ArrayList<ArrayList<String>>();
		for (int i=0; i<clauses.size(); i++) {
			if (clausetrue[i] == null) {
				clauses_null.add(clauses.get(i));
			}
		}
		return clauses_null;
	}
	
	public static String jeroslow_wang_os(ArrayList<String> literals_null, ArrayList<ArrayList<String>> clauses_null, Random r) {
		HashMap<String, Double> literal_values = new HashMap<String, Double>();
		String literal;
		
		String current_literal;
		double current_jw_score;
		
		for (int i=0; i<literals_null.size(); i++) { // loop over the literals
			current_literal = literals_null.get(i);
			current_jw_score = 0.0;

			for (int j=0; j<clauses_null.size(); j++) { // loop over the clauses
				if (clauses_null.get(j).contains(current_literal)) { // if clause contains literal
					current_jw_score += Math.pow(2, -clauses_null.get(j).size());
				}
			}
			
			literal_values.put(current_literal, current_jw_score);
		}
		
		Double max_value = literal_values.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
		String[] keys_max_value = Arrays.stream(literal_values.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), max_value)).map(Map.Entry::getKey).collect(toSet()).toArray()).toArray(String[]::new);
		literal = keys_max_value[r.nextInt(keys_max_value.length)];

		return literal;
	}
	
	public static ArrayList<String> jeroslow_wang_ts(ArrayList<String> variables_null, ArrayList<ArrayList<String>> clauses_null, Random r) {		
		HashMap<String, Double> variable_values = new HashMap<String, Double>();
		HashMap<String, Double> literal_values = new HashMap<String, Double>();
		
		String current_literal;
		String flipped_literal;
		double current_jw_score;
		double current_flipped_jw_score;
		
		for (int i=0; i<variables_null.size(); i++) { // loop over the literals
			current_literal = variables_null.get(i);
			flipped_literal = flip_literal(current_literal);
			
			current_jw_score = 0.0;
			current_flipped_jw_score = 0.0;

			for (int j=0; j<clauses_null.size(); j++) { // loop over the clauses
				if (clauses_null.get(j).contains(current_literal)) { // if clause contains literal
					current_jw_score += Math.pow(2, -clauses_null.get(j).size());
				}
				
				if (clauses_null.get(j).contains(flipped_literal)) { // if clause contains flipped literal
					current_flipped_jw_score += Math.pow(2, -clauses_null.get(j).size());
				}
			}
			
			variable_values.put(current_literal, current_jw_score + current_flipped_jw_score);
			literal_values.put(current_literal, current_jw_score);
			literal_values.put(flipped_literal, current_flipped_jw_score);
		}
		
		Double max_value = variable_values.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
		String[] keys_max_value = Arrays.stream(variable_values.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), max_value)).map(Map.Entry::getKey).collect(toSet()).toArray()).toArray(String[]::new);
		String variable = keys_max_value[r.nextInt(keys_max_value.length)];
		
		ArrayList<String> literal_value = new ArrayList<String>();
		
		literal_value.add(variable);
		
		if (literal_values.get(variable) >= literal_values.get(flip_literal(variable))) { // if J(x) >= J(x')
			literal_value.add("1");
		} else {
			literal_value.add("0");
		}
		
		return literal_value;
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

	public static String bohm(ArrayList<String> literals_null, ArrayList<ArrayList<String>> clauses_null, Random r) {		
		String literal = "";
		String current_literal;
		String flipped_literal;
		
		double bohm_score = 0.0;
		
		int alpha = 1;
		int beta = 2;

		BiHashMap<Integer, String, Integer> clause_literal_dict = new BiHashMap<Integer, String, Integer>();
		ArrayList<Integer> clause_lengths = new ArrayList<Integer>();

		// create the clause_literal_dict
		for (int i=0; i<clauses_null.size(); i++) { // loop over all clauses
			for (int j=0; j<clauses_null.get(i).size(); j++) { // loop over the literals
				if (clause_literal_dict.containsKeys(clauses_null.get(i).size(), clauses_null.get(i).get(j))) { // clause-literal combination already exists
					clause_literal_dict.put(clauses_null.get(i).size(), clauses_null.get(i).get(j), clause_literal_dict.get(clauses_null.get(i).size(), clauses_null.get(i).get(j)) + 1);
				} else { // clause-literal combination does not exist yet
					clause_literal_dict.put(clauses_null.get(i).size(), clauses_null.get(i).get(j), 1);
					clause_lengths.add(clauses_null.get(i).size());
				}
			}
		}
		
		HashMap<String, Double> literal_values = new HashMap<String, Double>();

		// calculate the bohm score for all literals
		for (int i=0; i<literals_null.size(); i++) { // loop over all literals
			current_literal = literals_null.get(i);
			flipped_literal = flip_literal(current_literal);
						
			ArrayList<Double> vector = new ArrayList<Double>();

			// populate the bohm vector
			for (int j=0; j<clause_lengths.size(); j++) {
				Integer current_clause_length = clause_lengths.get(j);
				Integer h_i;
				Integer flipped_h_i;
				
				if (clause_literal_dict.get(current_clause_length, current_literal) != null) {
					h_i = clause_literal_dict.get(current_clause_length, current_literal);
				} else {
					h_i = 0;
				}
				
				if (clause_literal_dict.get(current_clause_length, flipped_literal) != null) {
					flipped_h_i = clause_literal_dict.get(current_clause_length, flipped_literal);
				} else {
					flipped_h_i = 0;
				}
				
				double score = alpha * Math.max(h_i, flipped_h_i);
				score += beta * Math.min(h_i, flipped_h_i);
				vector.add(score);
			}

			// get the magnitude of the bohm vector and add it to the hashmap
			double current_bohm_score = 0.0;
			for (int k=0; k<vector.size(); k++) {
				current_bohm_score += vector.get(k) * vector.get(k);
			}
			current_bohm_score = Math.sqrt(current_bohm_score);
			literal_values.put(current_literal, current_bohm_score);
		}
		
		Double max_value = literal_values.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
		String[] keys_max_value = Arrays.stream(literal_values.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), max_value)).map(Map.Entry::getKey).collect(toSet()).toArray()).toArray(String[]::new);
		literal = keys_max_value[r.nextInt(keys_max_value.length)];

		return literal;
	}

	public static String rdlis(ArrayList<String> literals_null, ArrayList<ArrayList<String>> clauses_null, Random r) {	
		String literal;
		String current_literal;
		HashMap<String, Integer> literal_counts = new HashMap<String, Integer>();

		for (int i=0; i<clauses_null.size(); i++) {
			for (int j=0; j<clauses_null.get(i).size(); j++) {
				current_literal = clauses_null.get(i).get(j);
				if (literal_counts.containsKey(current_literal)) {
					literal_counts.put(current_literal, literal_counts.get(current_literal) + 1);
				} else {
					literal_counts.put(current_literal, 1);
				}
			}
		}
		
		// get the key (literal) associated with the highest value (count)
		// System.out.println(literal_counts.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)));
		// literal = literal_counts.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
		Integer max_value = literal_counts.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
		String[] keys_max_value = Arrays.stream(literal_counts.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), max_value)).map(Map.Entry::getKey).collect(toSet()).toArray()).toArray(String[]::new);
		literal = keys_max_value[r.nextInt(keys_max_value.length)];

		return literal;
	}
	
	public static String split(ArrayList<ArrayList<String>> clauses, HashMap<String, Boolean> literals, Boolean[] clausetrue, ArrayList<Integer> nflips, ArrayList<String> unique_literals, String version) {
		// consider only non-assigned literals
		ArrayList<String> literals_null = null_literals(unique_literals, literals);
		
		// consider only non-assigned variables
		ArrayList<String> variables_null = null_variables(unique_literals, literals);
		
		// consider only non-satisfied clauses	
		ArrayList<ArrayList<String>> clauses_null = null_clauses(clauses, clausetrue);
		
		Random r = new Random();
		
		if (getnulls(clausetrue).size() != 0) {
			ArrayList<String> literal_value;
			String selected_literal = "";
			int result = 0;
			
			switch (version) {
				case "S1": // no heuristic
					selected_literal = literals_null.get(r.nextInt(literals_null.size()));
					result = r.nextInt(2);
					break;
				case "S2": // jeroslow-wang one-sided
					selected_literal = jeroslow_wang_os(literals_null, clauses_null, r);
					result = r.nextInt(2);
					break;
				case "S3": // jeroslow-wang two-sided
					literal_value = jeroslow_wang_ts(variables_null, clauses_null, r);
					selected_literal = literal_value.get(0);
					if (literal_value.get(1) == "1") {
						result = 1;
					} else {
						result = 0;
					}
					break;
				case "S4": // bohm's heuristic
					selected_literal = bohm(literals_null, clauses_null, r);
					result = r.nextInt(2);
					break;
				case "S5": // rdlis
					selected_literal = rdlis(literals_null, clauses_null, r);
					result = 1;
					break;
			}
			
			if (selected_literal != "") {
				if (result == 1) {
					flipLiteral(literals, selected_literal, true, false, nflips);
				} else {
					flipLiteral(literals, selected_literal, false, false, nflips);
				}
				return selected_literal;
			} else {
				return "";
			}
		} else {
			return "";
		}
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

	public static void outputSudoku(HashMap<String, Boolean> literals, int divisor) {
		int count = 0;
		for (int i=1; i<=divisor; i++) {
			for (int j=1; j<=divisor; j++) {
				for (int k=1; k<=divisor; k++) {
					String key = String.valueOf(i) + String.valueOf(j) + String.valueOf(k);
					if (literals.get(key) == true) {
						System.out.print(String.valueOf(k) + " ");
						count++;
						if (count % divisor == 0) {
							System.out.println();
						}
					}
				}
			}
		}
		System.out.println();
	}
	
	public static void readSudoku(Scanner sat, ArrayList<ArrayList<String>> clauses, HashMap<String, Boolean> literals, String nextclause, String[] after) {
		while (sat.hasNextLine()) {
	    	int j = 0;
	    	nextclause = sat.nextLine();
	    	ArrayList<String> singleList = new ArrayList<String>();
	    	after = nextclause.split("\\s+");
	    	while (!after[j].equals("0")) {
	    		literals.put(after[j], null);
	    		singleList.add(after[j]);
	    		j++;
	    	}
	    	clauses.add(singleList);
	    }
	}
	
	public static void flipLiteral(HashMap<String, Boolean> literals, String tochange, Boolean firsttrue, Boolean nullify, ArrayList<Integer> nflips) {
		nflips.set(nflips.size()-1, nflips.get(nflips.size()-1) + 2);
		if (!nullify) {
			if (firsttrue) {
				literals.put(tochange, true);
				literals.put(flip_literal(tochange), false);
			} else {
				literals.put(tochange, false);
				literals.put(flip_literal(tochange), true);
			}
		} else {
			literals.put(tochange, null);
			literals.put(flip_literal(tochange), null);
		}	
	}
	
	public static void solveSudoku(ArrayList<ArrayList<String>> clauses, HashMap<String, Boolean> literals, Boolean[] clausetrue, ArrayList<Integer> nflips, String version) {
		nflips.add(0);
		String[] cont = new String[2];
		cont[0] = "n";
	    int ncount = 0;
	    boolean anychanges;
	    ArrayList<String> unique_literals = extract_literals(clauses);
	    boolean cdcl = false;
	    ArrayList<ArrayList<String>> flipt = new ArrayList<ArrayList<String>>(); //List with all values flipped per split
	    ArrayList<String> flipped = new ArrayList<String>(); //Temp list with flipped literal
	    ArrayList<String> splitd = new ArrayList<String>(); //List with all splitted literal
	    ArrayList<String> backtracksplitd= new ArrayList<String>(); //Splitted values that were used in backtracking
	    String splitted = ""; //Temp value with splitted literal
	    while (cont[0] != "t") {
	    	String litchange = "";
	    	litchange = simplify(clauses, literals, clausetrue, nflips);  
	    	
	    	if (litchange == "") {
	    		splitted = split(clauses, literals, clausetrue, nflips, unique_literals, version);
	    		splitd.add(splitted);
	    		if (splitted != "") {
	    			//System.out.println("Split " + splitted);
	    			flipt.add(flipped);
	    			flipped = new ArrayList<String>();
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
	    			flipped = new ArrayList<String>();
	    		}
	    		if (falsecount > 50 + backtracksplitd.size()) {
		    		backtracksplitd.clear();
		    		System.out.println("Reset backtrack.");
		    		
		    	}
    			for (int j = splitd.size() - 1; j >= 0; j--) {
    				for (int i = 0; i < flipt.get(j).size(); i++) {
    					flipLiteral(literals, flipt.get(j).get(i), false, true, nflips); // Set UNIT literals to NULL
    	    		}
    				flipLiteral(literals, splitd.get(j), false, true, nflips); //Set SPLIT literals to NULL
    				if (!backtracksplitd.contains(splitd.get(j))) {
    					doTrue(clauses, literals, clausetrue);
    				}
    				cont = checkTrue(clausetrue);
    				if (cont[0] != "f") {
    					backtracksplitd.add(splitd.get(j));
    					break outer; 
    				}
    			}		
	    	}
    	}

	    int count = 0;
	    for (Boolean value : literals.values()) {
	    	if (value != null) {
	    		if (value == true) {
	    			count++;
	    		}
	    	}
	    }
	    
	    //System.out.println("SAT with " + count);
	    if (clauses.size() > 10000) {
	    	outputSudoku(literals, 9);
	    } else {
	    	outputSudoku(literals, 4);
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
		String version = "";
		String filepath = "";

		if (args.length == 2) {
			// read command line arguments
			version = args[0];
			filepath = args[1];
		} else {
			System.err.println("This script requires exactly 2 arguments, as specified in the assignment.");
	        System.exit(1);
		}
		
		//Reading the SAT file (Scanner)
		File file = new File(filepath); 
	    Scanner sc = new Scanner(file);  //open once

    	// Start the timer
    	long startTime = System.currentTimeMillis();
	    
    	//Reading the SAT file (Scanner)
	    String nextclause = sc.nextLine();
	    String[] after = nextclause.split("\\s+");
	    
	    if (!(after[0].charAt(0) == 'p')) {
	    	sc.close();
	    	System.out.println("No starting line found.");
	    	throw new FileNotFoundException();
    	} 
	    
	    ArrayList<ArrayList<String>> clauses = new ArrayList<ArrayList<String>>(); //List containing all the clauses with the literals
	    HashMap<String, Boolean> literals = new HashMap<String, Boolean>();

	    readSudoku(sc, clauses, literals, nextclause, after);
	    sc.close();

	    //List containing boolean values of clauses
	    Boolean[] clausetrue= new Boolean[clauses.size()]; 
	    Arrays.fill(clausetrue, null);
	    ArrayList<Integer> nflips = new ArrayList<Integer>();
	    
	    System.out.println("Solution to Sudoku:");
	    solveSudoku(clauses, literals, clausetrue, nflips, version);
	    
	    System.out.println("Number of flips: " + String.valueOf(nflips.get(nflips.size()-1)));	    
	    System.out.println("Solved sudoku in " + String.valueOf((System.currentTimeMillis() - startTime)/1000) + " seconds");
	}
}
