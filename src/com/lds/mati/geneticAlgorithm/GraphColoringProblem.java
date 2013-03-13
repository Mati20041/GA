/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 *
 *
 * @author Mati
 */
public class GraphColoringProblem implements Problem<Integer> {

    private Graph graph;
    private int colors;

    public GraphColoringProblem(Graph graph, int colors) {
        this.graph = graph;
        this.colors = colors;
    }

    @Override
    public ArrayList<Integer[]> init(int size) {
        ArrayList<Integer[]> temp = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            Integer[] t = new Integer[graph.graph.size()];
            for (int j = 0; j < t.length; ++j) {
                t[j] = getRandomColor();
            }
            temp.add(t);
        }
        return temp;
    }

    @Override
    public ArrayList<Integer[]> mutate(ArrayList<Integer[]> population, double mutationProbability) {
        for (Integer[] person : population) {
            for (int i = 0; i < person.length; ++i) {
                if (Math.random() < mutationProbability) {
                    person[i] = getRandomColor();
                }
            }
        }
        return population;
    }

    @Override
    public ArrayList<Integer[]> crossover(ArrayList<Integer[]> population, int size, double crossProbability) {
        int popSize = population.size();
        ArrayList<Integer[]> newPopulation = new ArrayList<>();
        for (int i = 0; newPopulation.size() < size; i = (i + 1) % popSize) {
            if (i >= popSize) {
                i = 0;
            }
            Integer[] t1, t2, t3, t4;
            t1 = population.get(i);
            if (Math.random() < crossProbability) {
                t2 = population.get((int) Math.floor(Math.random() * (popSize)));
                t3 = cross1(t1, t2);
                t4 = cross2(t1, t2);
                newPopulation.add(t3);
                newPopulation.add(t4);
            } else {
                newPopulation.add(t1);
            }
        }
        return newPopulation;
    }

    @Override
    public ArrayList<Integer[]> selection(ArrayList<Integer[]> population, int size) {
        if (size > population.size()) {
            return population;
        }

        ArrayList<Integer[]> parents = new ArrayList<>();
        Collections.shuffle(population);
        double[] grades = gradeEveryone(population);
        int matchLength = grades.length/size;
        for (int i = 0; i < size; ++i) {
            int best = i * matchLength;
            for (int j = 1; j < matchLength && i * size + j < grades.length; ++j) {
                if (grades[best] < grades[i * size + j]) {
                    best = i * size + j;
                }
            }
            parents.add(population.get(best));
        }
        return parents;
    }

    @Override
    public boolean stopFunction(Integer[] bestSolution) {
        return costFunction(bestSolution) >= 1;
    }

    @Override
    public double costFunction(Integer[] subject) {
        double cost = 0.;
        int conflicts = 0;
        int nonConflict = 0;
        for (int i = 0; i < subject.length; ++i) {
            int currentColor = subject[i];
            ArrayList<Integer> currentMember = graph.graph.get(i);
            for (int j = 0; j < currentMember.size(); ++j) {
                if (subject[currentMember.get(j)] == currentColor) {
                    ++conflicts;
                } else {
                    ++nonConflict;
                }
            }
        }
        cost = 1-(1. * conflicts / (conflicts + nonConflict));
        return cost;
    }

    private int getRandomColor() {
        return (int) Math.floor(Math.random() * (colors));
    }

    private Integer[] cross1(Integer[] t1, Integer[] t2) {
        Integer[] t = new Integer[t1.length];
        for (int i = 0; i < t.length; ++i) {
            if (i < t.length / 2) {
                t[i] = t1[i];
            } else {
                t[i] = t2[i];
            }
        }

        return t;
    }

    private Integer[] cross2(Integer[] t1, Integer[] t2) {
        Integer[] t = new Integer[t1.length];
        for (int i = 0; i < t.length; ++i) {
            if (i < t.length / 2) {
                t[i] = t2[i];
            } else {
                t[i] = t1[i];
            }
        }

        return t;
    }

    @Override
    public double[] gradeEveryone(ArrayList<Integer[]> population) {
        double[] temp = new double[population.size()];
        for (int i = 0; i < temp.length; ++i) {
            temp[i] = costFunction(population.get(i));
        }
        return temp;

    }
}
