package eu.fbk.se.tcgen2;

import java.util.List;

public class Statistics {
	boolean successful;
	int evaluationsConsumed;
	int[] test;
	int fitness;
	
	List<Integer> fitnesses;
	
	/*public Statistics(boolean successful, int evaluationsConsumed, int fitness, int[] test) {
		this.successful = successful;
		this.evaluationsConsumed = evaluationsConsumed;
		this.fitness=fitness;
		this.test = test;
	}*/
	
	public Statistics(boolean successful, int evaluationsConsumed, int fitness, int[] test, List<Integer> fitnesses) {
		this.successful = successful;
		this.evaluationsConsumed = evaluationsConsumed;
		this.fitness=fitness;
		this.test = test;
		this.fitnesses = fitnesses;
		while (fitness==0 && fitnesses.size()<TestGenerator.MAX_ITERATIONS) fitnesses.add(0);
	}
	
	public static String getHeaderCSV() {
		return "successful,evaluationsConsumed,fitness";
	}
	public static String getHeaderCSV2() {
		return "method,iteration,fitness";
	}
	
	public String getCSV() {
		return (successful?"1":"0")+","+evaluationsConsumed+","+fitness;
	}
	
	public String getCSV2(String method) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<fitnesses.size(); i++) sb.append(method+","+i+","+fitnesses.get(i)+"\n");
		return sb.toString();
	}
	
	
}
