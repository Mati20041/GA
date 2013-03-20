/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm;

import com.lds.mati.geneticAlgorithm.ColoringGraphProblem.GraphColoringProblem;
import com.lds.mati.geneticAlgorithm.ColoringGraphProblem.Graph;
import com.lds.mati.geneticAlgorithm.ColoringGraphProblem.NotEvolutionAlgorithms;
import com.lds.mati.geneticAlgorithm.engine.GeneticAlgorithm;
import com.lds.mati.geneticAlgorithm.GUI.GUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author Mati
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Settings settings = loadSettings();
        if (args.length > 0) {
            if (args[0].equals("nogui")) {
                if (args.length == 2 && args[1].equals("notifications")) {
                    noGui(settings, true);
                } else {
                    noGui(settings, false);
                }
            } else if (args[0].equals("algorithm")) {
                runAlgorithm(settings, args.length < 2 ? "lf" : args[1]);
            } else if (args[0].equals("benchmark")) {
                runBenchmark(settings);
            }
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            GUI gui = new GUI();
            gui.setSettings(settings);
            gui.setVisible(true);
        }
    }

    public static void noGui(Settings settings, boolean showNotifications) {
        Graph graf = new Graph();
        try {
            graf.loadFromFile(settings.graphFileName);
        } catch (FileNotFoundException ex) {
            System.err.println("Błąd pliku grafu!");
            System.exit(-1);
        }
        GraphColoringProblem gcp = new GraphColoringProblem(graf, settings.colors, settings.isDoubleSlicedGenom, settings.isRandomCrossPosition);
        final GeneticAlgorithm<Integer> ea = new GeneticAlgorithm<>(gcp, settings.populationSize, settings.parentsSize, settings.crossProbability, settings.mutationProbability, settings.maxIterations);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                saveResult(ea.bestSolution);
            }
        }));
        if (showNotifications) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ea.run();
                }
            }).start();
            do {
                try {
                    Thread.sleep(settings.refreshNotificationTime);
                    System.out.printf("Iteration: %d  Fval: %.6f\n", ea.iterations, ea.bestSolutionCost);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (ea.isRunning());
        } else {
            ea.run();
        }
        System.out.println("Najlepszy wynik: " + ea.bestSolutionCost + " Liczba iteracji: " + ea.iterations);
        Plot2DPanel p = new Plot2DPanel();
        GUI.plot(ea.max, ea.avg, ea.min, p, settings.maxPlotVars);
        JFrame window = new JFrame("Algorytm Genetyczny");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(p);
        window.setSize(600, 600);
        window.setVisible(true);
    }

    public static void saveResult(Integer[] bestSolution) {
        File resultFile = new File("Result.txt");
        try {
            PrintWriter out = new PrintWriter(resultFile);
            for (int i = 0; i < bestSolution.length; ++i) {
                out.printf("v%d : %d\n", i, bestSolution[i]);
            }
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Settings loadSettings() {
        File settingsFile = new File("settings.xml");
        Settings settings = null;
        if (settingsFile.exists()) {
            try {
                JAXBContext context = JAXBContext.newInstance(Settings.class);
                Unmarshaller unmars = context.createUnmarshaller();
                settings = (Settings) unmars.unmarshal(settingsFile);
            } catch (JAXBException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            settings = new Settings();
            saveSettings(settings);
        }
        return settings;
    }

    public static void saveSettings(Settings settings) {
        File settingsFile = new File("settings.xml");
        try {
            JAXBContext context = JAXBContext.newInstance(Settings.class);
            Marshaller mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            mar.marshal(settings, System.out);
            mar.marshal(settings, settingsFile);
        } catch (JAXBException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void runAlgorithm(Settings settings, String par) {
        Graph graf = new Graph();
        try {
            graf.loadFromFile(settings.graphFileName);
        } catch (FileNotFoundException ex) {
            System.err.println("Błąd pliku grafu!");
            System.exit(-1);
        }
        Integer[] result = null;

        if (par.equals("lf")) {
            result = NotEvolutionAlgorithms.solveLargestFirst(graf);
        } else {
            result = NotEvolutionAlgorithms.solveSmallestFirst(graf);
        }

        int max = 0;
        for (int i = 0; i < result.length; ++i) {
            max = max > result[i] ? max : result[i];
        }
        System.out.println("Liczba kolorów wyznaczona przez alorytm " + par + " wynosi: " + (max + 1));
    }

    private static void runBenchmark(Settings settings) {
        Graph graf = new Graph();
        try {
            graf.loadFromFile(settings.graphFileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        GraphColoringProblem gcp = new GraphColoringProblem(graf, settings.colors, settings.isDoubleSlicedGenom, settings.isRandomCrossPosition);
        final GeneticAlgorithm<Integer> ea = new GeneticAlgorithm<>(gcp, settings.populationSize, settings.parentsSize, settings.crossProbability, settings.mutationProbability, settings.maxIterations);
        ArrayList<Double> max = new ArrayList<>();
        ArrayList<Double> avg = new ArrayList<>();
        ArrayList<Double> min = new ArrayList<>();
        ArrayList<Double> bestMax = new ArrayList<>();
        ArrayList<Double> bestAvg = new ArrayList<>();
        ArrayList<Double> bestMin = new ArrayList<>();
        int lowestIterations = settings.maxIterations + 1;
        Integer[] bestSolution = new Integer[graf.graph.size()];
        int avgIterations = 0;
        double m, a, mi, mv, av, miv;
        m = a = mi = mv = av = miv = 0;
        for (int i = 0; i < 10; ++i) {
            ea.run();
            double t1 = ea.bestSolutionCost;
            double t3 = ea.min.get(ea.min.size() - 1);
            double t2 = ea.avg.get(ea.avg.size() - 1);
            max.add(t1);
            avg.add(t2);
            min.add(t3);
            m += max.get(i);
            a += avg.get(i);
            mi += min.get(i);
            avgIterations += ea.iterations;
            if (ea.iterations < lowestIterations) {
                lowestIterations = ea.iterations;
                bestMax = new ArrayList<>(ea.max);
                bestAvg = new ArrayList<>(ea.avg);
                bestMin = new ArrayList<>(ea.min);
                bestSolution = ea.bestSolution;
            }
            System.out.println(String.format("Pętla %d ilość iteracji %d :{ MaxFval: %.4f , AvgFval: %.4f , MinFval: %.4f}", (i+1), ea.iterations, t1, t2, t3));
        }
        m /= max.size();
        a /= avg.size();
        mi /= min.size();
        avgIterations /= 10;
        for (int i = 0; i < max.size(); ++i) {
            mv += Math.pow(max.get(i) - m, 2);
            av += Math.pow(avg.get(i) - a, 2);
            miv += Math.pow(min.get(i) - mi, 2);
        }
        mv /= max.size();
        av /= avg.size();
        miv /= min.size();

        System.out.println(String.format("Dane dla 10 uruchomień\n~Max wartość %.4f dla %.10f\n~Wartość średnia %.4f dla %.10f\n~Wartość min %.4f dla %.10f\n~Ilość iteracji %d\n", m, mv, a, av, mi, miv,avgIterations));
        if (settings.plotGraph) {
            JFrame window = new JFrame("Najlepszy przebieg");
            window.setSize(600, 600);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Plot2DPanel p = new Plot2DPanel();
            GUI.plot(bestMax, bestAvg, bestMin, p, settings.maxPlotVars);
            window.setContentPane(p);
            window.setVisible(true);
        }
        saveResult(bestSolution);
    }
}
