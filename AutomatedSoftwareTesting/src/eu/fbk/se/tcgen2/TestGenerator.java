package eu.fbk.se.tcgen2;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestGenerator {
	
	public static final int MAX_ITERATIONS = 300;
	public static final int MAX_FITNESS = 1000;
	public static final int STATS_ITERATIONS = 1000;
	
	public static void main(String[] args) {
		try {
			PrintWriter fout = new PrintWriter(new FileWriter("stats.csv"));
			PrintWriter fout2 = new PrintWriter(new FileWriter("stats2.csv"));
			fout.println("id,method,trial,"+Statistics.getHeaderCSV());
			fout2.println(Statistics.getHeaderCSV2());
			Statistics h=null,r=null;
			for (int i=0; i<STATS_ITERATIONS*2; i+=2) {
				if (i%10==0) System.out.println(i);
				fout.println(i+",hill,"+(i/2)+","+(h=hillClimbing()).getCSV());
				fout.println((i+1)+",random,"+(i/2)+","+(r=randomTesting()).getCSV());
				fout2.println(h.getCSV2("hill"));
				fout2.println(r.getCSV2("random"));
			}
			fout.close();
			fout2.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static Statistics hillClimbing() {
		int[] bestTest = getRandomTest();
		int bestFitness = execute(bestTest);
		int iterations=1;
		List<Integer> fitnesses = new ArrayList<>();
		fitnesses.add(bestFitness);
		if (bestFitness==0) return new Statistics(true, 1, 0, bestTest, fitnesses);
		while (iterations<MAX_ITERATIONS) {
			List<int[]> test = getNeighbors(bestTest);
			int result=0;
			for (int[] t : test) if (iterations<MAX_ITERATIONS) {
				iterations++;
				if ((result=execute(t))<bestFitness) {
					bestFitness = result;
					bestTest=t;
				}
				fitnesses.add(bestFitness);
				if (bestFitness==0) return new Statistics(true, iterations, 0, bestTest, fitnesses);
			}
		}
		return new Statistics(false, MAX_ITERATIONS, bestFitness, bestTest, fitnesses);
	}
	
	public static Statistics randomTesting() {
		int[] bestTest = getRandomTest();
		int bestFitness = execute(bestTest);
		List<Integer> fitnesses = new ArrayList<>();
		fitnesses.add(bestFitness);
		if (bestFitness==0) return new Statistics(true, 1, 0, bestTest, fitnesses);
		for (int i=2; i<=MAX_ITERATIONS; i++) {
			int[] test = getRandomTest();
			int result = execute(test);
			if (result < bestFitness) {
				bestFitness = result;
				bestTest = test;
			}
			fitnesses.add(bestFitness);
			if (bestFitness==0) return new Statistics(true, i, 0, bestTest, fitnesses);
		}
		return new Statistics(false, MAX_ITERATIONS, bestFitness, bestTest, fitnesses);
	}
	
	public static int execute(int[] array) {
		int[] a = new int[5];
		for (int i=0; i<a.length; i++) a[i]=array[i];
		BinarySearch_INSTR_43.fitness=MAX_FITNESS;
		BinarySearch_INSTR_43.search(a, array[5]);
		return BinarySearch_INSTR_43.fitness;
	}
	
	public static List<int[]> getNeighbors(int[] currentSolution) {
		List<int[]> l = new ArrayList<>();
		for (int i=0; i<currentSolution.length; i++) {
			l.add(Arrays.copyOf(currentSolution, currentSolution.length));
			l.get(i)[i] = getRandom();
		}
		return l;
	}
	
	private static int getRandom() { return (int)(Math.random()*50); }
	
	private static int[] getRandomTest() {
		int[] a = new int[6];
		for (int i=0; i<a.length; i++) a[i]=getRandom();
		return a;
	}
}
