/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm.ColoringGraphProblem;

import java.util.ArrayList;

/**
 *
 * @author Mati
 */
public class NotEvolutionAlgorithms {

    public static int[] solveSmallestFirst(Graph graph) {
        ArrayList<Integer> cache = new ArrayList<>(graph.graph.size());
        int[] result = new int[graph.graph.size()];
        int color = 0;
        while (cache.size() < graph.graph.size()) {
            int indexSmallest = 0;
            int smallestNeighbour = 0;
            for (int i = 0; i < graph.graph.size(); ++i) {
                if (cache.contains(i)) {
                    continue;
                }
                if (smallestNeighbour == 0) {
                    indexSmallest = i;
                    smallestNeighbour = graph.graph.get(i).size();
                    continue;
                }
                if (graph.graph.get(i).size() < smallestNeighbour) {
                    indexSmallest = i;
                    smallestNeighbour = graph.graph.get(i).size();
                }
            }

            cache.add(indexSmallest);
            for (Integer i : graph.graph.get(indexSmallest)) {
                if (result[i] == color) {
                    color++;
                    break;
                }
            }
            result[indexSmallest] = color;
        }

        return result;
    }

    public static int[] solveLargestFirst(Graph graph) {
        ArrayList<Integer> cache = new ArrayList<>(graph.graph.size());
        int[] result = new int[graph.graph.size()];
        int color = 0;
        while (cache.size() < graph.graph.size()) {
            int indexSmallest = 0;
            int largestNeighbour = 0;
            for (int i = 0; i < graph.graph.size(); ++i) {
                if (cache.contains(i)) {
                    continue;
                }
                if (largestNeighbour == 0) {
                    indexSmallest = i;
                    largestNeighbour = graph.graph.get(i).size();
                    continue;
                }
                if (graph.graph.get(i).size() > largestNeighbour) {
                    indexSmallest = i;
                    largestNeighbour = graph.graph.get(i).size();
                }
            }

            cache.add(indexSmallest);
            for (Integer i : graph.graph.get(indexSmallest)) {
                if (result[i] == color) {
                    color++;
                    break;
                }
            }
            result[indexSmallest] = color;
        }

        return result;
    }
}
