/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm.engine;

import java.util.ArrayList;

/**
 *
 * @author Mati
 */
public interface Problem<T> {
    public ArrayList<T[]> init(int size);
    public ArrayList<T[]> mutate(ArrayList<T[]> population, double mutationProbabilty);
    public ArrayList<T[]> crossover(ArrayList<T[]> population,int size, double crossProbabilty);
    public ArrayList<T[]> selection(ArrayList<T[]> population, int size);
    public boolean stopFunction(T[] bestSolution);
    public double costFunction(T[] subject);
    public double[] gradeEveryone(ArrayList<T[]> population);
}
