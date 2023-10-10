package cmsc335.trafficsimulator;

import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;

/*
Author: UselessFodder
Project: Traffic GUI Project 3
Course: CMSC 335
*/
public class TrafficSimulator {
    
        public static void main(String[] args) {
        //create holder for JFrame
        JFrame trafficFrame = new JFrame("Traffic GUI");
        trafficFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        //add trafficGUI into frame
        GUIDisplay newGUI = new GUIDisplay();
        
        //set size
        newGUI.setPreferredSize(new Dimension(600,600));
        
        //add to frame
        trafficFrame.add(newGUI);
        trafficFrame.pack();
        
        //create new calculator object
        TrafficCalculator newCalculator = new TrafficCalculator();
        
        //create simulation controller
        SimulationController newController = new SimulationController(newGUI,newCalculator);
        
        //welcome user
        System.out.println("*** Welcome to the Traffic Program!  ***");
        
        //begin menu
        trafficFrame.setVisible(true);
        
        //once menu is exited, thank user
        System.out.println("***  Thank you for using the Traffic Program! ***");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss uuuu/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("*** Logging out at " + dtf.format(now) + " ***");

    }

}