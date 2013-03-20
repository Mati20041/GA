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

    public static Integer[] solveSmallestFirst(Graph graph) {
        ArrayList<Integer> cache = new ArrayList<>(graph.graph.size());
        Integer[] result = new Integer[graph.graph.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = -1;
        }
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
            int i = 0;
            color = 0;
            while (i < graph.graph.get(indexSmallest).size()) {
                if (result[graph.graph.get(indexSmallest).get(i)] == color) {
                    i = 0;
                    color++;
                } else {
                    ++i;
                }
            }
            result[indexSmallest] = color;
        }

        return result;
    }

    public static Integer[] solveLargestFirst(Graph graph) {
        ArrayList<Integer> cache = new ArrayList<>(graph.graph.size());
        Integer[] result = new Integer[graph.graph.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = -1;
        }
        int color = 0;
        while (cache.size() < graph.graph.size()) {
            int indexLargest = 0;
            int largestNeighbour = 0;
            for (int i = 0; i < graph.graph.size(); ++i) {
                if (cache.contains(i)) {
                    continue;
                }
                if (largestNeighbour == 0) {
                    indexLargest = i;
                    largestNeighbour = graph.graph.get(i).size();
                    continue;
                }
                if (graph.graph.get(i).size() > largestNeighbour) {
                    indexLargest = i;
                    largestNeighbour = graph.graph.get(i).size();
                }
            }
            color = 0;
            cache.add(indexLargest);
            int i = 0;
            while (i < graph.graph.get(indexLargest).size()) {
                if (result[graph.graph.get(indexLargest).get(i)] == color) {
                    i = 0;
                    color++;
                } else {
                    ++i;
                }
            }
            result[indexLargest] = color;
        }

        return result;
    }
}
