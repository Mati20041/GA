/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lds.mati.geneticAlgorithm;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Mati
 */
@XmlRootElement(name = "settings")
//@XmlType(propOrder={"maxIterations", "colors", "populationSize", "parentsSize", "crossProbability", "mutationProbability", "isDoubleSlicedGenom", "isRandomCrossPosition", "plotGraph", "refreshNotificationTime", "graphFileName", "maxPlotVars"})
public class Settings {
    public  int maxIterations = 10000;
    public  int colors = 11;
    public  int populationSize = 40;
    public  int parentsSize = 10;
    public  double crossProbability = 0.7;
    public  double mutationProbability = 0.006;
    public  boolean isDoubleSlicedGenom = true;
    public  boolean isRandomCrossPosition = false;
    public  boolean plotGraph = true;
    public  int refreshNotificationTime = 1000;
    public  String graphFileName = "anna.col";
    public  int maxPlotVars = 1000;
}
