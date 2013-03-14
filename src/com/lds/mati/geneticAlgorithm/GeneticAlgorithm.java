/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm;

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
    private int populationSize;
    private int parentSize;
    private double crossOverProbability;
    private double mutationProbability;

    public GeneticAlgorithm(Problem<T> problem, int populationSize, int parentSize, double crossOverProbability, double mutationProbability) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.parentSize = parentSize;
        this.crossOverProbability = crossOverProbability;
        this.mutationProbability = mutationProbability;
        max = new ArrayList<>();
        min = new ArrayList<>();
        avg = new ArrayList<>();
    }

    public T[] run(int maxIterations) {
        max = new ArrayList<>();
        min = new ArrayList<>();
        avg = new ArrayList<>();
        gens = problem.init(populationSize);
        bestSolution = gens.get(0);
        refreshStatistics(gens);
        int i = 0;
        for (i = 0; i < maxIterations && !problem.stopFunction(bestSolution); ++i) {
            gens = problem.selection(gens, parentSize);
            gens = problem.crossover(gens, populationSize, crossOverProbability);
            gens = problem.mutate(gens, mutationProbability);
            refreshStatistics(gens);
        }

        bestSolutionCost = problem.costFunction(bestSolution);
        iterations = i;
        System.out.println("Best solution cost: " + problem.costFunction(bestSolution) + " No iterations: " + i);
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
}
