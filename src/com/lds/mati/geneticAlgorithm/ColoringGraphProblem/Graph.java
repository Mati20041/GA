/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm.ColoringGraphProblem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Mati
 */
public class Graph {

    public ArrayList<ArrayList<Integer>> graph;

    public Graph() {
        graph = new ArrayList<>();
    }

    public void loadFromFile(String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fileName));
        String t = "";
        // Przeskoczenie blablania
        while (!t.equals("p")) {
            in.nextLine();
            t = in.next();
        }
        in.next();

        ArrayList<Integer> temp = new ArrayList<>();
        int edges = in.nextInt();
        int size = in.nextInt();
        graph = new ArrayList<>(edges);
        for (int i = 0; i < edges; ++i) {
            graph.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < size; ++i) {
            in.next();
            int v = in.nextInt() - 1;
            int neigh = in.nextInt() - 1;
            if (!graph.get(v).contains(neigh)) {
                graph.get(v).add(neigh);
            }
            if (!graph.get(neigh).contains(v)) {
                graph.get(neigh).add(v);
            }
        }
        in.close();
    }

    public void addVertex(int[] neighbors) {
        ArrayList<Integer> v = new ArrayList<>();
        for (int i : neighbors) {
            v.add(i);
        }
        graph.add(v);
    }
}
