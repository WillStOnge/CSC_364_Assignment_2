/*
 * Name: Knapsack.java
 * Author: Will St. Onge
 * Description: Finds the most efficient configuration of projects for an arbitrary company to make the most profit
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Knapsack {
	// Global Variables
	private static int[][] matrix = new int[1][1];
	private static int profit;
	private static ArrayList<Project> chosen = new ArrayList<>();

	public static void main(String[] args) {
		// Initialize variable for the main method
		Scanner console = new Scanner(System.in), in = null;
		Project[] projects = null;
		int total_weight = 0, project_count = 0;
		String filename, output;

		// Get user input
		System.out.print("Enter the number of available employee work weeks: ");
		total_weight = console.nextInt();

		System.out.print("Enter the name of input file: ");
		filename = console.next();

		System.out.print("Enter the name of output file: ");
		output = console.next();

		// Get all projects from the input file
		try {
			Scanner scanner = new Scanner(new File(filename));

			while (scanner.hasNextLine()) {
				project_count++;
				scanner.nextLine();
			}

			scanner.close();
			scanner = new Scanner(new File(filename));

			projects = new Project[project_count];

			// Get all data from the text file
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] inputData = line.split(" ");
				int index = Integer.parseInt(inputData[0].substring(7, inputData[0].length()));
				String name = inputData[0];
				int weight = Integer.parseInt(inputData[1]);
				int value = Integer.parseInt(inputData[2]);

				projects[index] = new Project(name, weight, value);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Invalid input file");
			System.exit(1);
		}

		// Setup the size of the matrix
		System.out.println();
		matrix = new int[projects.length + 1][total_weight + 1];

		// Fill in matrix
		fillMatrix(projects, projects.length + 1, total_weight + 1);
		profit = matrix[projects.length][total_weight];
		
		// Solve the matrix and reverse the result
		solveMatrix(total_weight, projects);
		Collections.reverse(chosen);

		// Output results to output file
		try (FileWriter fw = new FileWriter(new File(output))) {
			fw.write("Number of projects available: " + project_count + "\n");
			fw.write("Available employee work weeks: " + total_weight + "\n");
			fw.write("Number of projects chosen: " + chosen.size() + "\n");
			fw.write("Total profit: " + profit + "\n");
			for (Project project : chosen)
				fw.write(project.toString() + "\n");
		} catch (IOException e) {
			System.out.println("Invalid output file");
			System.exit(1);
		}
		
		System.out.println("Number of projects = " + projects.length + "\nDone");
	}

	// Solves the matrix
	private static void solveMatrix(int total_weight, Project[] projects) {
		int profit1 = profit;

		for(int i = projects.length; i > 0 && profit1 > 0; i--) {
			// Test if project is included
			if(profit1 == matrix[i - 1][total_weight])
				continue;
			// Project is excluded
			else {			
				profit1 -= projects[i - 1].getValue();
				total_weight -= projects[i - 1].getWeight();
				chosen.add(projects[i - 1]);
			}
		}
	}

	// Fills in the matrix in an iterative fashion O(n^2)
	private static void fillMatrix(Project[] projects, int x, int y) {
		// Fill in the the first column with 0s
		for (int i = 0; i <= y - 1; i++)
			matrix[0][i] = 0;

		// Go left to right, up to down
		for (int i = 1; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (projects[i - 1].getWeight() > j)
					matrix[i][j] = matrix[i - 1][j];
				else
					matrix[i][j] = Math.max(matrix[i - 1][j],
							matrix[i - 1][j - projects[i - 1].weight] + projects[i - 1].getValue());
			}
		}
	}

	// Data structure for projects
	private static class Project {
		private String name;
		private int weight, value;

		public Project(String name, int weight, int value) {
			this.name = name;
			this.weight = weight;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public int getWeight() {
			return weight;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name + " " + weight + " " + value;
		}
	}
}