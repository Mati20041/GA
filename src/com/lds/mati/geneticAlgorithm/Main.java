/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm;

import com.lds.mati.geneticAlgorithm.ColoringGraphProblem.GraphColoringProblem;
import com.lds.mati.geneticAlgorithm.ColoringGraphProblem.Graph;
import com.lds.mati.geneticAlgorithm.engine.GeneticAlgorithm;
import com.lds.mati.geneticAlgorithm.GUI.GUI;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Mati
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static int maxIterations = 10000;
    public static int colors = 11;
    public static int populationSize = 40;
    public static int parentsSize = 10;
    public static double crossProbability = 0.7;
    public static double mutationProbability = 0.2;

    public static void main(String[] args) {

        if (args.length != 0 && args[0].equals("nogui")) {
            Graph graf = new Graph();
            try {
                graf.loadFromFile("anna.col");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            GraphColoringProblem gcp = new GraphColoringProblem(graf, colors);
            GeneticAlgorithm<Integer> ea = new GeneticAlgorithm<>(gcp, populationSize, parentsSize, crossProbability, mutationProbability, maxIterations);
            ea.run();
            ea.plot();
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            new GUI().setVisible(true);
        }

//        Graph graf = new Graph();
//        try {
//            graf.loadFromFile("david.col");
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        GraphColoringProblem gcp = new GraphColoringProblem(graf, colors);
//        GeneticAlgorithm<Integer> ea = new GeneticAlgorithm<>(gcp, populationSize, parentsSize, crossProbability, mutationProbability);
//        ArrayList<Double> max = new ArrayList<>();
//        ArrayList<Double> avg = new ArrayList<>();
//        ArrayList<Double> min = new ArrayList<>();
//        double m, a, mi, mv, av, miv;
//        m = a = mi = mv = av = miv = 0;
//        for (int i = 0; i < 10; ++i) {
//            ea.run(maxIterations);
//            max.add(ea.bestSolutionCost);
//            avg.add(ea.avg.get(ea.avg.size() - 1));
//            min.add(ea.min.get(ea.min.size() - 1));
//            m += max.get(i);
//            a += avg.get(i);
//            mi += min.get(i);
//        }
//        m /= max.size();
//        a /= avg.size();
//        mi /= min.size();
//        for (int i = 0; i < max.size(); ++i) {
//            mv += Math.pow(max.get(i) - m, 2);
//            av += Math.pow(avg.get(i) - a, 2);
//            miv += Math.pow(min.get(i) - mi, 2);
//        }
//        mv /= max.size();
//        av /= avg.size();
//        miv /= min.size();
//
//        System.out.println(String.format("Dane dla 10 uruchomień\n~Max wartość %.4f dla %.10f\n~Wartość średnia %.4f dla %.10f\nWartość min %.4f dla %.10f", m, mv, a, av, mi, miv));
    }
}
