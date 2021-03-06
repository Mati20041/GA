/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm.ColoringGraphProblem;

import com.lds.mati.geneticAlgorithm.engine.Problem;
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
    private boolean isDoubleSlicedGenom;
    private boolean isRandomCrossPosition;

    public GraphColoringProblem(Graph graph, int colors, boolean isTwoPartCrossing, boolean isRandomCrossPosition) {
        if (colors < 1) {
            throw new IndexOutOfBoundsException("Musi być przynajmniej jeden kolor!");
        }
        this.graph = graph;
        this.colors = colors;
        this.isDoubleSlicedGenom = isTwoPartCrossing;
        this.isRandomCrossPosition = isRandomCrossPosition;
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

    
    public ArrayList<Integer[]> selection2(ArrayList<Integer[]> population, int size) {
        if (size > population.size()) {
            return population;
        }

        ArrayList<Integer[]> parents = new ArrayList<>();

        //int matchLength = grades.length / size;
        while (parents.size() < size) {
            Collections.shuffle(population);
            double[] grades = gradeEveryone(population);
            int best = 0;
            for(int i = 1 ; i < size; ++i){
                if(grades[i]<grades[best]){
                    best = i;
                }
            }
            parents.add(population.get(best));
        }
        return parents;
    }
    @Override
    public ArrayList<Integer[]> selection(ArrayList<Integer[]> population, int size) {
        if (size > population.size()) {
            return population;
        }

        ArrayList<Integer[]> parents = new ArrayList<>();
        Collections.shuffle(population);
        double[] grades = gradeEveryone(population);
        int matchLength = grades.length / size;
        for (int i = 0; i < size; ++i) {
            int best = i * matchLength;
            for (int j = 1; j < matchLength && i * size + j < grades.length; ++j) {
                if (grades[best] > grades[i * size + j]) {
                    best = i * size + j;
                }
            }
            parents.add(population.get(best));
        }
        return parents;
    }

    @Override
    public ArrayList<Integer[]> crossover(ArrayList<Integer[]> population, int size, double crossProbability) {
        int popSize = population.size();
        ArrayList<Integer[]> newPopulation = new ArrayList<>();
        Integer[] t1;
        Integer[][] t;
        for (int i = 0; newPopulation.size() < size; i = (i + 1) % popSize) {
            if (i >= popSize) {
                i = 0;
            }

            t1 = population.get(i);
            if (Math.random() < crossProbability) {
                if (isDoubleSlicedGenom) {
                    t = cross(t1, population.get((int) Math.floor(Math.random() * (popSize))), isRandomCrossPosition);
                } else {
                    t = cross2(t1, population.get((int) Math.floor(Math.random() * (popSize))), isRandomCrossPosition);
                }
                for (int j = 0; j < t.length; ++j) {
                    newPopulation.add(t[j]);
                }
            } else {
                newPopulation.add(t1);
            }
        }
        return newPopulation;
    }

    private Integer[][] cross(Integer[] t1, Integer[] t2, boolean randomCrossPosition) {
        int length = t1.length;
        int crossPosition = randomCrossPosition ? (int) (Math.random() * length) : length / 2;
        Integer[][] t = new Integer[2][length];
        for (int i = 0; i < length; ++i) {
            if (i < crossPosition / 2) {
                t[0][i] = t1[i];
                t[1][i] = t2[i];
            } else {
                t[0][i] = t2[i];
                t[1][i] = t1[i];
            }
        }

        return t;
    }

    private Integer[][] cross2(Integer[] t1, Integer[] t2, boolean randomCrossPosition) {
        int length = t1.length;
        int crossPosition1, crossPosition2;
        Integer[][] t = new Integer[6][length];
        if (randomCrossPosition) {
            double d1, d2;
            d1 = Math.random() * length;
            d2 = Math.random() * length;
            crossPosition1 = (int) Math.min(d1, d2);
            crossPosition2 = (int) Math.max(d1, d2);
        } else {
            crossPosition1 = length / 3;
            crossPosition2 = crossPosition1 * 2;
        }

        for (int i = 0; i < length; ++i) {
            if (i < crossPosition1) {
                t[0][i] = t1[i];
                t[1][i] = t1[i];
                t[2][i] = t1[i];
                t[3][i] = t2[i];
                t[4][i] = t2[i];
                t[5][i] = t2[i];
            } else if (i >= crossPosition1 && i < crossPosition2) {
                t[0][i] = t1[i];
                t[1][i] = t1[i];
                t[2][i] = t2[i];
                t[3][i] = t2[i];
                t[4][i] = t1[i];
                t[5][i] = t1[i];
            } else {
                t[0][i] = t1[i];
                t[1][i] = t2[i];
                t[2][i] = t1[i];
                t[3][i] = t2[i];
                t[4][i] = t1[i];
                t[5][i] = t2[i];
            }
        }

        return t;
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
    public boolean stopFunction(Integer[] bestSolution) {
        return costFunction(bestSolution) ==0;
    }

    @Override
    public double costFunction(Integer[] subject) {
        double cost;
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
        cost = 1 - (1. * conflicts / (conflicts + nonConflict));
        return conflicts;
    }

    private int getRandomColor() {
        return (int) Math.floor(Math.random() * colors);
    }

    @Override
    public double[] gradeEveryone(ArrayList<Integer[]> population) {
        double[] temp = new double[population.size()];
        for (int i = 0; i < temp.length; ++i) {
            temp[i] = costFunction(population.get(i));
        }
        return temp;

    }

    @Override
    public boolean isGreaterCostBetter() {
        return false;
    }
}
