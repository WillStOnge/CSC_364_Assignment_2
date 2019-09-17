/*
 * Name: MaxIncreasingSubseq.java
 * Author: Will St. Onge
 * Description: Finds the maximum increasing subsequence of an input string and outputs that string
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MaxIncreasingSubseq {
	public static void main(String[] args) {
		// Current largest lis length's index
		int starting_index = 0;
		
		// Array list that contains the lis
		ArrayList<Character> answer = new ArrayList<>();

		// Get user input
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a string: ");
		char[] input = scanner.next().toCharArray();
		
		// Setup arrays
		int[] ascii = new int[input.length], score = new int[input.length], prev = new int[input.length];

		// Set all scores to 1 and all previous' to -1
		for (int i = 0; i < input.length; i++) {
			score[i] = 1;
			prev[i] = -1;
		}

		// Convert all chars to integers for lis
		for (int i = 0; i < input.length; i++)
			ascii[i] = (int) input[i];

		// Fill in the arrays and keep track of the highest scoring index
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < i; j++) {
				if (ascii[j] < ascii[i] && score[i] < score[j] + 1) {
					score[i] = score[j] + 1;
					prev[i] = j;
					if(score[i] > score[starting_index])
						starting_index = i;
				}
			}
		}
		
		// Temporary index
		int index = starting_index;
		
		// Trace back the lis and convert the ascii back to chars
		for(int i = 0; i < score[starting_index]; i++) {
			answer.add((char)ascii[index]);
			index = prev[index];
		}
		
		// Reverse the array list
		Collections.reverse(answer);
		
		// Output each item in the array list to the console
		answer.forEach(e -> System.out.print(e));
	}
}