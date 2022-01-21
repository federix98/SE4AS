/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.scheduler;

import it.univaq.seas.job.CheckTankIsFull;
import it.univaq.seas.job.ConsumptionAdaptation;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author valerio
 */
public class AnalyzerScheduler {
  
    public static void scedule() {
        Timer timer = new Timer();
        //TimerTask task1 = new CheckTankIsFull();
        TimerTask consumptionAdaptationTask = new ConsumptionAdaptation();

        timer.scheduleAtFixedRate(consumptionAdaptationTask, 0, 10000);
        
        //timer.scheduleAtFixedRate(task1, 0, 10000);
    }


}
