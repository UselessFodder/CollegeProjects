package cmsc335.trafficsimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
Author: UselessFodder
Project: Traffic GUI Project 3
Course: CMSC 335
*/
//class to act as intermediary between calculator and GUI
public class SimulationController {
    //add GUI and calculator to this controller
    private GUIDisplay display;
    private TrafficCalculator calculator;
    
    //time in seconds simulation has been running
    private int simulationTime = 0;
    
    //synchronization intrinsic lock object
    private final Object pauseLock = new Object();
    //flags to hold current pause or terminate status
    private boolean isPaused = false;
    private volatile boolean isRunning = true; //volatile to ensure update within thread
    
    private static final int SIMULATION_TICK = 1000;
    
    //constructor
    public SimulationController(GUIDisplay display, TrafficCalculator calculator){
        this.display = display;
        this.calculator = calculator;
        
        //initialize GUI with calculator values
        initGUI();
        
        //add all listeners to GUI
        initController();
        
    }//end constructor
    
    //initialize GUI with starting information
    public void initGUI(){
        //init clock display
        updateClockDisplay();
        
        //init traffic light display
        updateTrafficLightDisplay();
        
        //init current car display
        updateCarDisplay();
    }//end initGUI
    
    //initialize all listeners for GUI
    public void initController(){
        //init simulation control button listeners
        display.getStartButton().addActionListener(e -> startSimulation());
        display.getPauseButton().addActionListener(e -> pauseSimulation());
        display.getStopButton().addActionListener(e -> stopSimulation());
        display.getContinueButton().addActionListener(e -> continueSimulation());
        
        //init buttons to add cars and intersections
        display.getAddCarButton().addActionListener(new ActionListener(){
            //overriden actionPerformed to get combo box values and pass to create new cars
            @Override
                public void actionPerformed(ActionEvent e) {
                //get combo box car number select value
                int numCars = (int)display.getAddCarComboBox().getSelectedItem();
                //get combo box car number select value
                int carSpeed = (int)display.getAddCarSpeedComboBox().getSelectedItem();

                //add cars to calculator object ArrayList
                addCars(numCars,carSpeed);

                //update GUI
                updateCarDisplay();
            }
        });//end addCarButton action listener
        
        display.getAddIntersectionButton().addActionListener(new ActionListener(){
            //overriden actionPerformed to get combo box values and pass to create new traffic lights
            @Override
            public void actionPerformed(ActionEvent e) {
                //get combo box number select value
                int numLights = (int) display.getAddIntersectionComboBox().getSelectedItem();
                //get combo box number select value
                TrafficLight.LightState lightState = (TrafficLight.LightState) display.getAddIntersectionLightComboBox().getSelectedItem();

                //add traffic lights to calculator object ArrayList
                addTrafficLights(numLights,lightState);

                //update GUI
                updateTrafficLightDisplay();
            }
        });//end addCarButton action listener

    }//end initController()
    
    //function to start simulation when "Start" is pressed
    public void startSimulation(){
        //check if simulation has been run before
        if (simulationTime > 0) {
            //if so, reset simulation
            resetSimulation();
        }//end if(simulationTime)
        
        //enable pause button
        display.getPauseButton().setEnabled(true);
        //enable stop button
        display.getStopButton().setEnabled(true);
        //disable start button until stop is pressed
        display.getStartButton().setEnabled(false);
        //disable continue button until pause is pressed
        display.getContinueButton().setEnabled(false);

        //start thread to update clock
        Thread clockThread = new Thread(() -> {
            //check if thread should take the lock
            while (isRunning){
                //if so, execute if lock is available
                synchronized (pauseLock){
                    //check for pause
                    while (isPaused){
                        //attempt to wait until not paused
                        try{
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }//end try-catch
                    }//end while(isPaused)
                    
                    //execute simulation clock update logic
                    updateSimulationTime();
                    //update GUI for clock
                    updateClockDisplay();
                }//end synchronized(pauseLock)
                
                //delay for a full simulation tick until running again
                try{
                    Thread.sleep(SIMULATION_TICK);
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }//end try-catch
            }//end while(isRunning)
        });//end Thread clockThread
        
        clockThread.start();
        
        //start thread to update cars
        Thread carThread = new Thread(() -> {
            //check if thread should take the lock
            while (isRunning){
                //if so, execute if lock is available
                synchronized (pauseLock){
                    //check for pause
                    while (isPaused){
                        //attempt to wait until not paused
                        try{
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }//end try-catch
                    }//end while(isPaused)
                    
                    //execute car update logic in calculator
                    calculator.moveCars();
                    //update GUI with new car information
                    updateCarDisplay();
                }//end synchronized(pauseLock)
                
                //delay for a full simulation tick until running again
                try{
                    Thread.sleep(SIMULATION_TICK);
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }//end try-catch
            }//end while(isRunning)
        });//end Thread carThread
        
        carThread.start();
        
        //start thread to update traffic lights
        Thread trafficLightThread = new Thread(() -> {
            //check if thread should take the lock
            while (isRunning){
                //if so, execute if lock is available
                synchronized (pauseLock){
                    //check for pause
                    while (isPaused){
                        //attempt to wait until not paused
                        try{
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }//end try-catch
                    }//end while(isPaused)
                    
                    //run traffic light update logic in calculator
                    calculator.checkLights();
                    //update GUI with new traffic light information
                    updateTrafficLightDisplay();
                }//end synchronized(pauseLock)
                
                //delay for a full simulation tick until running again
                try{
                    Thread.sleep(SIMULATION_TICK);
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }//end try-catch
                
            }//end while(isRunning)
        });//end Thread trafficLightThread
        
        trafficLightThread.start();
    }//end startSimulation
    
    //function to pause simulation when "Pause" is pressed
    public void pauseSimulation(){
        //execute in synch
        synchronized (pauseLock) {
            //set bool flag to pause all threads
            isPaused = true;
        }
        
        //disable pause button until stop is pressed
        display.getPauseButton().setEnabled(false);
        //reenable continue button
        display.getContinueButton().setEnabled(true);
    }//end pauseSimulation()
    
    //function to start simulation when "Continue" is pressed
    public void continueSimulation(){
        //execute in synch
        synchronized (pauseLock){
            //set bool flag to unpause all threads
            isPaused = false;
            //push notification to all threads
            pauseLock.notifyAll();
        }
        //reenable pause button
        display.getPauseButton().setEnabled(true);
        //disable continue button until pause is pressed
        display.getContinueButton().setEnabled(false);
    }//end resumeSimulation()
    
    //function to end simulation and destroy threads when "Stop" is pressed
    public void stopSimulation(){
        //to gracefully end all threads, set isRunning bool to stop
        isRunning = false;
        //wake up any paused threads and notify to update
        continueSimulation();
        
        //reenable start button
        display.getStartButton().setEnabled(true);
        //disable stop button until start is pressed
        display.getStopButton().setEnabled(false);
        //disable pause button until start is pressed
        display.getPauseButton().setEnabled(false);
        
    }//end endSimulation()
    
    //function to reset simulation status
    public void resetSimulation(){
        //create new calculator object
        this.calculator = new TrafficCalculator();

        //reset simulation time
        this.simulationTime = 0;

        //reset flags to hold current pause or terminate status
        this.isPaused = false;
        this.isRunning = true;
        
        //update GUI
        updateClockDisplay();
        updateTrafficLightDisplay();
        updateCarDisplay();
    }
    
    //function to add a number of cars equal to parameter
    public void addCars(int numCars, int carSpeed){
        //for loop to create a number of cars
        for (int i =0; i<numCars; i++){
            calculator.addCar((int)carSpeed);
        }//end for loop
    }//end addCars
    
    //function to add a number of traffic lights equal to parameter
    public void addTrafficLights(int numLights, TrafficLight.LightState lightState){
        //for loop to create a number of cars
        for (int i =0; i<numLights; i++){
            calculator.addTrafficLight(lightState);
        }//end for loop
    }//end addCars
    
    //update simulation time via increment
    public void updateSimulationTime(){
        //increment simulation time
        this.simulationTime+=1;
    }//end updateSimulationTime()
    
    //update display for current time
    public void updateClockDisplay(){
        //pass current simulation time into GUI
        display.updateSimulationTime(simulationTime);
        
    }//end updateClockDisplay()
    
    //update display for all trafficlights
    public void updateTrafficLightDisplay(){
        //get all traffic lights from calculator
        ArrayList<TrafficLight> trafficLights = calculator.getTrafficLights();
        
        //pass list onto GUI to update
        display.updateTrafficLights(trafficLights);
    }//end updateCarDisplay()
    
    //update display for all cars
    public void updateCarDisplay(){
        //get all cars from calculator
        ArrayList<Car> cars = calculator.getCars();
        
        //pass list onto GUI to update
        display.updateCars(cars);
    }//end updateCarDisplay()
}
