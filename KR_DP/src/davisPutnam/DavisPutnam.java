package davisPutnam;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
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
	public static void pureLiteral(ArrayList<String> oneclauses, Boolean[] literals, ArrayList<Integer> nflips) {
		for (int i = 0; i < oneclauses.size(); i ++) {
	    	if (oneclauses.get(i).charAt(0) == '-') {
	    		if(!oneclauses.contains(oneclauses.get(i).substring(1))) {
	    			flipLiteral(literals, Integer.parseInt(oneclauses.get(i)), true, false, nflips);
	    			//literals[1000 + Integer.parseInt(oneclauses.get(i)) -1] = true;
	    			//literals[1000 - Integer.parseInt(oneclauses.get(i)) -1] = false;
	    			System.out.println("Pure negative literal at " + oneclauses.get(i));
	    		}
	    	} else {
	    		if(!oneclauses.contains('-' + oneclauses.get(i))) {
	    			flipLiteral(literals, Integer.parseInt(oneclauses.get(i)), true, false, nflips);
	    			//literals[1000 + Integer.parseInt(oneclauses.get(i)) -1] = true;
	    			//flipLiteral(literals, Integer.parseInt(oneclauses.get(i)), true, false);
	    			//literals[1000 - Integer.parseInt(oneclauses.get(i)) -1] = false;
	    			System.out.println("Pure positive literal at " + oneclauses.get(i));
	    		}
	    	}
	    }
	}
	public static int unitLiteral(ArrayList<ArrayList<String>> clauses, Boolean[] literals, ArrayList<Integer> nflips) {
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
	
	public static int simplify(ArrayList<ArrayList<String>> clauses, Boolean[] literals, Boolean[] clausetrue, ArrayList<Integer> nflips) {
		boolean anychanges = true;
		    anychanges = false;
		    ArrayList<String> oneclauses = intoOneList(clauses);
		    //pureLiteral(oneclauses, literals, nflips);
		    int litchange = unitLiteral(clauses, literals, nflips);
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
	
	public static ArrayList<String> null_literals(ArrayList<String> unique_literals, Boolean[] literals) {
		ArrayList<String> literals_null = new ArrayList<String>();
		for (int i=0; i<unique_literals.size(); i++) {
			if (literals[1000 + Integer.parseInt(unique_literals.get(i)) - 1] == null) {
				literals_null.add(unique_literals.get(i));
			}
		}
		return literals_null;
	}
	
	public static ArrayList<String> null_variables(ArrayList<String> unique_literals, Boolean[] literals) {
		ArrayList<String> variables_null = new ArrayList<String>();
		for (int i=0; i<unique_literals.size(); i++) {
			if (literals[1000 + Integer.parseInt(unique_literals.get(i)) - 1] == null) {
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
	
	public static Integer jeroslow_wang_os(ArrayList<String> literals_null, ArrayList<ArrayList<String>> clauses_null, Random r) {
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

		return Integer.parseInt(literal);
	}
	
	public static ArrayList<Integer> jeroslow_wang_ts(ArrayList<String> variables_null, ArrayList<ArrayList<String>> clauses_null, Random r) {		
		HashMap<String, Double> variable_values = new HashMap<String, Double>();
		HashMap<String, Double> literal_values = new HashMap<String, Double>();
		
		String variable = "";
		double jw_score = 0.0;
		double flipped_jw_score = 0.0;
		
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
		variable = keys_max_value[r.nextInt(keys_max_value.length)];
		
		ArrayList<Integer> literal_value = new ArrayList<Integer>();
		
		literal_value.add(Integer.parseInt(variable));
		
		if (literal_values.get(variable) >= literal_values.get(flip_literal(variable))) { // if J(x) >= J(x')
			literal_value.add(1);
		} else {
			literal_value.add(0);
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

	public static Integer bohm(ArrayList<String> literals_null, ArrayList<ArrayList<String>> clauses_null, Random r) {		
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

		return Integer.parseInt(literal);
	}

	public static Integer rdlis(ArrayList<String> literals_null, ArrayList<ArrayList<String>> clauses_null, Random r) {	
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

		return Integer.parseInt(literal);
	}
	
	public static int split(ArrayList<ArrayList<String>> clauses, Boolean[] literals, Boolean[] clausetrue, ArrayList<Integer> nflips, ArrayList<String> unique_literals, Integer use_heuristic) {
		// consider only non-assigned literals
		ArrayList<String> literals_null = null_literals(unique_literals, literals);
		
		// consider only non-assigned variables
		ArrayList<String> variables_null = null_variables(unique_literals, literals);
		
		// consider only non-satisfied clauses	
		ArrayList<ArrayList<String>> clauses_null = null_clauses(clauses, clausetrue);
		
		Random r = new Random();
		
		if (getnulls(clausetrue).size() != 0) {
			ArrayList<Integer> literal_value;
			int selected_literal = 0;
			int result = 0;
			
			switch (use_heuristic) {
				case 0: // no heuristic
					selected_literal = Integer.parseInt(literals_null.get(r.nextInt(literals_null.size())));
					result = r.nextInt(2);
					break;
				case 1: // jeroslow-wang one-sided
					selected_literal = jeroslow_wang_os(literals_null, clauses_null, r);
					result = r.nextInt(2);
					break;
				case 2: // jeroslow-wang two-sided
					literal_value = jeroslow_wang_ts(variables_null, clauses_null, r);
					selected_literal = literal_value.get(0);
					result = literal_value.get(1);
					break;
				case 3: // bohm's heuristic
					selected_literal = bohm(literals_null, clauses_null, r);
					result = r.nextInt(2);
					break;
				case 4: // rdlis
					selected_literal = rdlis(literals_null, clauses_null, r);
					result = 1;
					break;
			}
			
			if (selected_literal != 0) {
				if (result == 1) {
					flipLiteral(literals, selected_literal, true, false, nflips);
				} else {
					flipLiteral(literals, selected_literal, false, false, nflips);
				}
				return selected_literal;
			} else {
				return 0;
			}
		} else {
			return 0;
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
	public static void readSudoku(Scanner rules, Scanner problem, ArrayList<ArrayList<String>> clauses, String nextclause, String[] after) {
		while (rules.hasNextLine()) {
	    	int j = 0;
	    	nextclause = rules.nextLine();
	    	ArrayList<String> singleList = new ArrayList<String>();
	    	after = nextclause.split("\\s+");
	    	while (!after[j].equals("0")) {
	    		singleList.add(after[j]);
	    		j += 1;
	    	}
	    	clauses.add(singleList);
	    }
	    outer: while (problem.hasNextLine()) {
	    	int j = 0;
	    	nextclause = problem.nextLine();
	    	if (nextclause.charAt(0)=='=') {
	    		break outer;
	    	}
	    	ArrayList<String> singleList = new ArrayList<String>();
	    	after = nextclause.split("\\s+");
	    	while (!after[j].equals("0")) {
	    		singleList.add(after[j]);
	    		j += 1;
	    	}
	    	clauses.add(singleList);
	    	
	    }  
	}
	public static void flipLiteral(Boolean[] literals, int tochange, Boolean firsttrue, Boolean nullify, ArrayList<Integer> nflips) {
		nflips.set(nflips.size()-1, nflips.get(nflips.size()-1) + 2);
		if (!nullify) {
			if (firsttrue) {
				literals[1000 + tochange - 1] = true;
				//literals[((int) Math.floor(literals.length / 2)) + tochange] = true;
				literals[1000 - tochange - 1] = false;
				//literals[((int) Math.floor(literals.length / 2)) - tochange] = false;
			} else {
				literals[1000 + tochange - 1] = false;
				//literals[((int) Math.floor(literals.length / 2)) + tochange] = false;
				literals[1000 - tochange - 1] = true;
				//literals[((int) Math.floor(literals.length / 2)) - tochange] = true;
			}
		} else {
			literals[1000 + tochange - 1] = null;
			//literals[((int) Math.floor(literals.length / 2)) + tochange] = null;
			//literals[((int) Math.floor(literals.length / 2)) - tochange] = null;
			literals[1000 - tochange - 1] = null;
		}
		
	}
	
	public static void solveSudoku(ArrayList<ArrayList<String>> clauses, Boolean[] literals, Boolean[] clausetrue, ArrayList<Integer> nflips) {
		nflips.add(0);
		String[] cont = new String[2];
		cont[0] = "n";
	    int ncount = 0;
	    boolean anychanges;
	    ArrayList<String> unique_literals = extract_literals(clauses);
	    boolean cdcl = false;
	    ArrayList<ArrayList<Integer>> flipt = new ArrayList<ArrayList<Integer>>(); //List with all values flipped per split
	    ArrayList<Integer> flipped = new ArrayList<Integer>(); //Temp list with flipped literal
	    ArrayList<Integer> splitd = new ArrayList<Integer>(); //List with all splitted literal
	    ArrayList<Integer> backtracksplitd= new ArrayList<Integer>(); //Splitted values that were used in backtracking
	    int splitted = 0; //Temp value with splitted literal
	    while (cont[0] != "t") {
	    	int litchange = 0;
	    	litchange = simplify(clauses, literals, clausetrue, nflips);  
	    	if (litchange==0) {
	    		splitted = split(clauses,literals, clausetrue, nflips, unique_literals, 1);
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
	    	if (cdcl == true) {
	    		outer: while (cont[0] == "f") {
		    		falsecount++;
		    		if (falsecount == 1) {
		    			splitd.add(splitted);
		    			flipt.add(flipped);
		    			flipped = new ArrayList<Integer>();
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
	    			/*
	    			 INSPECT THE FALSE CLAUSE
	    			 LOOK AT THE LITERALS
	    			 FIND THE ONE THAT WAS SPLITTED AT HIGHEST LEVEL 
	    			 MAKE ALL SPLITTED VALUES AFTER THAT AND ALL UNIT VALUES AFTER THAT NULL
	    			 ADJUST SPLITTED AND FLIPPED ARRAYS
	    			 ADD NEW CLAUSE
	    			
	    			 */
	    			
	    			
	    			
	    			
		    	}
	    	} else {
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
	    					flipLiteral(literals, flipt.get(j).get(i), false, true, nflips); // Set UNIT literals to NULL
	    					//literals[1000 + flipt.get(j).get(i) - 1] = null;
	    	    			//literals[1000 - flipt.get(j).get(i) - 1] = null;
	    	    		}
	    				flipLiteral(literals, splitd.get(j), false, true, nflips); //Set SPLIT literals to NULL
	    				//literals[1000 + splitd.get(j) - 1] = null;
	    				//literals[1000 - splitd.get(j) - 1] = null;
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
	    	}
	    	if (ncount % 100  == 0) {
	    		//System.out.println(ncount);
	    	}
	    	ncount += 1;
	    }
	    System.out.println(ncount);
	    int count = 0;
	    for (int i = 0; i < literals.length; i ++) {
	    	if (literals[i] != null) {
	    		if (literals[i] == true) {
	    			//System.out.println(i - 1000 + 1);
	    			count += 1;
	    		}
	    	}	
	    }
	    //System.out.println("SAT with " + count);
	    outputSudoku(literals);
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
		File file2 = new File("newsudoku.txt"); // always open
		//File file2 = new File("four_example.txt"); // always open
	    Scanner sc2 = new Scanner(file2);
	    boolean moreproblems = true;
	    int nsudoku = 0;
	    while (moreproblems) {
		    //Reading the sudoku rules file (Scanner 1) and sudoku problem file (Scanner 2) 
			File file = new File("sudoku-rules.txt"); 
			//File file = new File("four_rules.txt"); 
		    Scanner sc = new Scanner(file);  //open once
		    String nextclause = sc.nextLine();
		    String[] after = nextclause.split("\\s+");
		    int maxvar;
		    if (after[0].charAt(0) == 'p') {
	    		maxvar = Integer.parseInt(after[2]);
	    	} else {
		    	System.out.println("No starting line found.");
		    	throw new FileNotFoundException();
		    }
		    ArrayList<ArrayList<String>> clauses = new ArrayList<ArrayList<String>>(); //List containing all the clauses with the literals
		    if (maxvar < 900) maxvar = 999;
		    Boolean[] literals = new Boolean[2*maxvar + 1];  //List containing boolean values of literals
		    Arrays.fill(literals, null);
		    System.out.println(literals.length);
		    readSudoku(sc,sc2,clauses,nextclause,after);
		    if (!sc2.hasNextLine()) {
		    	moreproblems = false;
		    }
		    sc.close();
		    //List containing boolean values of clauses
		    Boolean[] clausetrue= new Boolean[clauses.size()]; 
		    Arrays.fill(clausetrue, null);
		    //System.out.println("There are " + clauses.size() + " clauses.");
		    nsudoku++;
		    ArrayList<Integer> nflips = new ArrayList<Integer>();
		    solveSudoku(clauses, literals, clausetrue, nflips);
		    System.out.println(nflips.get(nflips.size()-1));
	    }
	    System.out.println("You have " + nsudoku + " sudokus.");
	}
}
