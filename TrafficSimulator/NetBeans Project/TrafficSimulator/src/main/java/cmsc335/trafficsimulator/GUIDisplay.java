package cmsc335.trafficsimulator;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/*
Author: UselessFodder
Project: Traffic GUI Project 3
Course: CMSC 335
*/
//this class draws the Traffic GUI layout and take in user input
public class GUIDisplay extends JPanel {
    //panels to dynamically display time, traffic light, and car info
    private JPanel timePanel = new JPanel();
    private JPanel intersectionPanel = new JPanel();
    private JPanel carInfoPanel = new JPanel();
    
    //controls to start, pause, continue, and stop simulation
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton continueButton;
    
    //controls to add cars and intersections
    private JComboBox addCarComboBox;
    private JComboBox addCarSpeedComboBox;
    private JButton addCarButton;
    private JComboBox addIntersectionComboBox;
    private JComboBox addIntersectionLightComboBox;
    private JButton addIntersectionButton;
    
    //constructor
    public GUIDisplay(){
        //overall layout    
        setLayout(new BorderLayout());
        
        //panel to hold northSector information
        JPanel northSector = new JPanel(new GridLayout(2,1));
        //north sector for header information
        JLabel northLabel = new JLabel("Traffic Simulation", JLabel.CENTER);
        //add to northSector
        northSector.add(northLabel);
        
        //--------time display area
        timePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        //border
        timePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel timeLabel = new JLabel("Current Simulation Time:");
        timePanel.add(timeLabel);
        northSector.add(timePanel);
        
        //add north sector to GUIDisplay
        add(northSector, BorderLayout.NORTH);

        //center sector for the main content boxes
        JPanel centerSector = new JPanel();
        //grid layout to divide center sector
        centerSector.setLayout(new GridLayout(2,1));

        //---traffic light panel definitions             
        //border
        intersectionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //header
        JLabel intersectionLabel = makeHeader("Intersections:");
        intersectionPanel.add(intersectionLabel);

        //set layout as grid with number of rows equal to elements added
        intersectionPanel.setLayout( new GridLayout(intersectionPanel.getComponentCount(), 1));
       
        //add to sector
        centerSector.add(intersectionPanel);

        //---current car information panel definitions        
        //border
        carInfoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //header
        JLabel carLabel = makeHeader("Cars:");
        carInfoPanel.add(carLabel);      
        
        //set layout as grid with number of rows equal to elements added
        carInfoPanel.setLayout(new GridLayout(carInfoPanel.getComponentCount(),1));
        
        //add to sector
        centerSector.add(carInfoPanel);
        
        //add center sector to GUIDisplay
        add(centerSector, BorderLayout.CENTER);

        //south sector for control buttons
        JPanel southSector = new JPanel(new BorderLayout());

        //panel to hold control buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        //buttons to control simulation
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        //default to disabled
        pauseButton.setEnabled(false);
        stopButton = new JButton("Stop");
        //default to disabled
        stopButton.setEnabled(false);
        continueButton = new JButton("Continue");
        //default to disabled
        continueButton.setEnabled(false);
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(continueButton);
        
        //add panel to sector
        southSector.add(buttonPanel, BorderLayout.NORTH);

        //separate area to add new cars and intersections
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
        
        //controls for adding cars
        JPanel addCarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //label
        addCarPanel.add(new JLabel("Add Cars:"));
        //dropdown to allow more than one addition
        addCarComboBox = new JComboBox<>(new Integer[]{1, 2, 3});
        addCarPanel.add(addCarComboBox);
        //label
        addCarPanel.add(new JLabel(" at speed (km/h): "));
        //dropdown to select new car(s) speed
        addCarSpeedComboBox = new JComboBox<>(new Integer[]{30,60,90,120,150,180,210});
        addCarPanel.add(addCarSpeedComboBox);
        //button to add the number of of cars within combo box
        addCarButton = new JButton("Add Car(s)");
        addCarPanel.add(addCarButton);
        
        //add carPanel to addPanel area
        addPanel.add(addCarPanel);
        
        //controls for adding intersections
        JPanel addIntersectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //label
        addIntersectionPanel.add(new JLabel("Add Intersections:"));
        
        //dropdown to allow more than one addition
        addIntersectionComboBox = new JComboBox<>(new Integer[]{1, 2, 3});
        addIntersectionPanel.add(addIntersectionComboBox);
        //label
        addIntersectionPanel.add(new JLabel(" starting on: "));
        //dropdown to select new light(s) current active light state
        addIntersectionLightComboBox = new JComboBox<>(new TrafficLight.LightState[]{TrafficLight.LightState.GREEN,TrafficLight.LightState.YELLOW,TrafficLight.LightState.RED});
        addIntersectionPanel.add(addIntersectionLightComboBox);
        //button to add the number of intersections within combo box
        addIntersectionButton = new JButton("Add Intersection(s)");
        addIntersectionPanel.add(addIntersectionButton);
        
        //add intersectionPanel to addPanel area
        addPanel.add(addIntersectionPanel);
        
        //add to south sector
        southSector.add(addPanel, BorderLayout.CENTER);
        //add south sector to GUIDisplay
        add(southSector, BorderLayout.SOUTH);
    }//end constructor
    
    //turn text into header JLabel and return it
    public JLabel makeHeader(String toHeader){
        //create JLabel
        JLabel newHeader = new JLabel(toHeader,JLabel.CENTER);
        
        //set font as bold and larger to stand out
        Font currentFont = newHeader.getFont();
        Font boldFont = new Font(currentFont.getFontName(),Font.BOLD,currentFont.getSize()+4);
        newHeader.setFont(boldFont);
        
        //return
        return newHeader;
    }//end makeHeader()
    
    //update simulation time at top of the GUI
    public void updateSimulationTime(int simulationTime){
        //make this thread-safe by utilizing invokeLater and lambda expression
        SwingUtilities.invokeLater(() -> {
            //remove all current components within the panel
            timePanel.removeAll();
            
            //add JLabel with updated time
            JLabel timeLabel = new JLabel("Current Simulation Time: " + simulationTime);
            //add to panel
            timePanel.add(timeLabel);
            
            //refresh and repaint GUI
            timePanel.revalidate();
            timePanel.repaint();
        });//end invokeLater lambda expression 
    }//end updateSimulationTime
    
    //update traffic display with ArrayList of traffic light objects
    public void updateTrafficLights(ArrayList<TrafficLight> trafficLights){
        //make this thread-safe by utilizing invokeLater and lambda expression
        SwingUtilities.invokeLater(() -> {
            //remove all current components within the panel
            intersectionPanel.removeAll();
            
            //add header
            JLabel carLabel = makeHeader("Intersections:");
            intersectionPanel.add(carLabel);
            
            //cycle through all traffic light objects and display information
            for (TrafficLight trafficLight : trafficLights){
                //create a label with traffic light info
                JLabel carEntry = new JLabel(trafficLight.toFormattedString(), JLabel.CENTER);
                //add to proper panel
                intersectionPanel.add(carEntry);
            }//end for(Cars)
            
            //set layout as grid with number of rows equal to elements added
            intersectionPanel.setLayout( new GridLayout(intersectionPanel.getComponentCount(), 1));
            
            //refresh and repaint GUI
            intersectionPanel.revalidate();
            intersectionPanel.repaint();
        });//end invokeLater lambda expression
    }//end updateTrafficLights()
    
    //update car with ArrayList of car objects
    public void updateCars(ArrayList<Car> cars){
        //make this thread-safe by utilizing invokeLater and lambda expression
        SwingUtilities.invokeLater(() -> {
            //remove all current components within the panel
            carInfoPanel.removeAll();            
            
            //add header
            JLabel carLabel = makeHeader("Cars:");
            carInfoPanel.add(carLabel);
            
            //cycle through all car objects and display information
            for (Car car : cars){
                //create a label with car info
                JLabel carEntry = new JLabel(car.toFormattedString(), JLabel.CENTER);
                //add to proper panel
                carInfoPanel.add(carEntry);
            }//end for(Cars)
            
            //reset grid to accomodate new number of cars
            carInfoPanel.setLayout(new GridLayout(carInfoPanel.getComponentCount(),1));
            
            //refresh and repaint GUI
            carInfoPanel.revalidate();
            carInfoPanel.repaint();
        });//end invokeLater lambda expression
    }//end updateCars()
    
    //getters for control objects
    public JButton getStartButton(){
        return this.startButton;
    }
    public JButton getPauseButton(){
        return this.pauseButton;
    }
    public JButton getStopButton(){
        return this.stopButton;
    }
    public JButton getContinueButton(){
        return this.continueButton;
    }
    public JComboBox<String> getAddCarComboBox(){
        return this.addCarComboBox;
    }
    public JComboBox<Integer> getAddCarSpeedComboBox(){
        return this.addCarSpeedComboBox;
    }
    public JButton getAddCarButton(){
        return this.addCarButton;
    }
    public JComboBox<String> getAddIntersectionComboBox(){
        return this.addIntersectionComboBox;
    }
    public JComboBox<TrafficLight.LightState> getAddIntersectionLightComboBox(){
        return this.addIntersectionLightComboBox;
    }
    public JButton getAddIntersectionButton(){
        return this.addIntersectionButton;
    }

}
