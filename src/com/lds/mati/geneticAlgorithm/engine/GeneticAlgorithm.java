/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm.engine;

import java.util.ArrayList;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author Mati
 */
public class GeneticAlgorithm<T> {

    private ArrayList<T[]> gens;
    private Problem<T> problem;
    public T[] bestSolution;
    public ArrayList<Double> max;
    public ArrayList<Double> avg;
    public ArrayList<Double> min;
    public double bestSolutionCost;
    public int iterations;
    private boolean isRunning;
    
    public int populationSize;
    public int parentSize;
    public double crossOverProbability;
    public double mutationProbability;
    public int maxIterations;
    
    public GeneticAlgorithm(Problem<T> problem, int populationSize, int parentSize, double crossOverProbability, double mutationProbability,int maxIterations) throws IllegalStateException {
        this.problem = problem;
        this.populationSize = populationSize;
        this.parentSize = parentSize;
        this.crossOverProbability = crossOverProbability;
        this.mutationProbability = mutationProbability;
        this.maxIterations = maxIterations;
        max = new ArrayList<>();
        min = new ArrayList<>();
        avg = new ArrayList<>();
    }

    public T[] run() {
        validate();
        isRunning = true;
        max = new ArrayList<>();
        min = new ArrayList<>();
        avg = new ArrayList<>();
        
        gens = problem.init(populationSize);
        bestSolution = gens.get(0);
        refreshStatistics(gens);
        iterations = 0;
        for (; isRunning && iterations < maxIterations && !problem.stopFunction(bestSolution); ++iterations) {
            gens = problem.selection(gens, parentSize);
            gens = problem.crossover(gens, populationSize, crossOverProbability);
            gens = problem.mutate(gens, mutationProbability);
            refreshStatistics(gens);
        }
        isRunning = false;
        bestSolutionCost = problem.costFunction(bestSolution);
        System.out.println("Best solution cost: " + problem.costFunction(bestSolution) + " No iterations: " + iterations);
        return bestSolution;
    }

    private void refreshStatistics(ArrayList<T[]> gens) {

        double max, min, avg, current;
        avg = max = min = problem.costFunction(gens.get(0));

        for (int i = 1; i < gens.size(); ++i) {
            current = problem.costFunction(gens.get(i));
            if (max < current) {
                max = current;
                if (bestSolution == null || max > problem.costFunction(bestSolution)) {
                    bestSolution = gens.get(i);
                }
            }
            min = min > current ? current : min;
            avg += current;
        }
        avg /= gens.size();

        this.max.add(max);
        this.avg.add(avg);
        this.min.add(min);
    }

    public void plot() {
        Plot2DPanel p = new Plot2DPanel();
        p.addLegend("SOUTH");
        double[] min, max, avg;
        max = new double[this.max.size()];
        avg = new double[this.max.size()];
        min = new double[this.max.size()];
        for (int i = 0; i < max.length; ++i) {
            max[i] = this.max.get(i);
            avg[i] = this.avg.get(i);
            min[i] = this.min.get(i);
        }
        p.addLinePlot("Max", max);
        p.addLinePlot("Avg", avg);
        p.addLinePlot("Min", min);

        JFrame window = new JFrame("Plot");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(p);
        window.setSize(600, 600);
        window.setVisible(true);
    }

    public boolean validate() throws IllegalStateException {
        if(populationSize<parentSize){
            throw new IllegalStateException("Liczba populacji mniejsza od liczby rodziców");
        }
        if(populationSize==0||parentSize==0){
            throw new IllegalStateException("Liczba populacji lub rodziców wynosi 0");
        }
        if(mutationProbability<0||crossOverProbability<0){
            throw new IllegalStateException("Prawdopodobieństwo mutacji lub krzyżowania mniejsze niż 0%");
        }
        if(maxIterations<=1){
            throw new IllegalStateException("Liczba iteracji za mała (co najmniej 1)");
        }
        return true;
    }
    
    synchronized public void stop(){
        isRunning = false;
    }
    
    public boolean isRunning(){
        return isRunning;
    }
}
