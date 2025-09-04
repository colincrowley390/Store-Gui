package com.example.storegui;

import java.util.ArrayList;
import java.util.List;

public class MemoryExperiment {

    public static void main(String[] args) {

        System.out.println("Stack Size Experiment:");
        runStackExperiment();

        System.out.println("\nHeap Size Experiment:");
        runHeapExperiment();
    }

    // Heap Size Experiment
    private static void runHeapExperiment() {
        // This will create a large number of objects to fill the heap
        List<long[]> objects = new ArrayList<>();
        int count = 0;
        long startTime = System.nanoTime(); // Start time for heap experiment

        try {
            // Continuously create large objects until an OutOfMemoryError occurs
            while (true) {
                objects.add(new long[100_000]); // Create large objects
                count++;
                if (count % 100 == 0) {
                    long currentTime = System.nanoTime();
                    System.out.println("Objects created: " + count +
                            ", Time elapsed: " + (currentTime - startTime) / 1_000_000_000.0 + " seconds.");
                }
            }
        } catch (OutOfMemoryError e) {
            // Catch the OutOfMemoryError and print the number of objects created
            long endTime = System.nanoTime();
            System.out.println("OutOfMemoryError occurred.");
            System.out.println("Total objects created: " + count);
            System.out.println("Time taken: " + (endTime - startTime) / 1_000_000_000.0 + " seconds.");
        }
    }

    // Stack Size Experiment
    private static void runStackExperiment() {
        long startTime = System.nanoTime();
        try {
            // This will create a large number of recursive calls to fill the stack
            recursiveMethod(0, startTime);
        } catch (StackOverflowError e) {
            // Catch the StackOverflowError and print the depth of recursion
            long endTime = System.nanoTime();
            System.out.println("StackOverflowError occurred.");
            System.out.println("Time taken: " + (endTime - startTime) / 1_000_000_000.0 + " seconds.");
        }
    }

    // Recursive method to fill the stack
    private static void recursiveMethod(int depth, long startTime) {
        // Simulate some work in the recursive method
        if (depth % 100 == 0) {
            // Print the current depth and time elapsed
            long currentTime = System.nanoTime();
            System.out.println("Recursion depth: " + depth +
                    ", Time elapsed: " + (currentTime - startTime) / 1_000_000_000.0 + " seconds.");
        }
        // Recursively call the method to increase the depth
        recursiveMethod(depth + 1, startTime);
    }
}