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
            result[i] = 0;
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

    public static Integer[] solveLargestFirst(Graph graph) {
        ArrayList<Integer> cache = new ArrayList<>(graph.graph.size());
        Integer[] result = new Integer[graph.graph.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = 0;
        }
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
