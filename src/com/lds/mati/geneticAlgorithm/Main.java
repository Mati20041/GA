/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm;

import com.lds.mati.geneticAlgorithm.ColoringGraphProblem.GraphColoringProblem;
import com.lds.mati.geneticAlgorithm.ColoringGraphProblem.Graph;
import com.lds.mati.geneticAlgorithm.engine.GeneticAlgorithm;
import com.lds.mati.geneticAlgorithm.GUI.GUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
        if (args.length > 0 && args[0].equals("nogui")) {
            if (args.length == 2 && args[1].equals("notifications")) {
                noGui(settings,true);
            } else {
                noGui(settings,false);
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
}
