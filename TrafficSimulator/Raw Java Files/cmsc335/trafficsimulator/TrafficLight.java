package cmsc335.trafficsimulator;

/*
Author: UselessFodder
Project: Traffic GUI Project 3
Course: CMSC 335
*/
public class TrafficLight {
    private String lightName;
    //1= red, 2= yellow, 3= green
    private LightState lightActive;
    //location on x axis
    private final double lightLocation;
    //timer in miliseconds between changes
    private int lightTimer = 0;
    
    public enum LightState {
        RED, YELLOW, GREEN
    }//end LightState
    
    //check if light is at correct time
    public void checkTimer(){
        //settings are: Green 10s, Yellow 2s, Red 12s
        int timerCheck = 0;
        //get these values
        switch(lightActive){
            case RED:
                timerCheck = 12000;
                break;
            case YELLOW:
                timerCheck = 2000;
                break;
            case GREEN:
                timerCheck = 10000;
                break;
        }//end switch
        
        //compare current lightTimer with light values
        if(this.lightTimer >= timerCheck){
            //if light is ready to change, execute method
            changeLight();
            //reset light timer
            this.lightTimer=0;
        } else{
            //if not, update lightTimer
            incrementLightTimer();
        }//end if-else (this.lightTimer >= timerCheck)
    }//end checkTimer()
    
    //update light to new state
    public void changeLight(){
        //move light in RED->GREEN->YELLOW->RED fashion
        switch(lightActive){
            case RED:
                lightActive = LightState.GREEN;
                break;
            case GREEN:
                lightActive = LightState.YELLOW;
                break;
            case YELLOW:
                lightActive = LightState.RED;
                break;
        }//end switch
        
    }//end changeLight()
    
    //increment timer by 1 second
    public void incrementLightTimer(){
        this.lightTimer+=1000;
    }//end incrementLightTimer
    
    
    //constructor
    public TrafficLight(String lightName, double lightLocation){
        this.lightName = lightName;
        this.lightActive=LightState.RED;
        this.lightLocation=lightLocation;
    }
    public TrafficLight(String lightName, LightState lightActive, double lightLocation){
        this.lightName = lightName;
        this.lightActive=lightActive;
        this.lightLocation=lightLocation;
    }
    
    //getters, setters
    public String getLightName(){
        return this.lightName;
    }
    public LightState getLightActive(){
        return this.lightActive;
    }
    public double getLightLocation(){
        return this.lightLocation;
    }
    public int getLightTimer(){
        return this.lightTimer;
    }
    public void setLightName(String lightName){
        this.lightName = lightName;
    }
    public void setLightActive(LightState lightActive){
        this.lightActive = lightActive;
    }
    
    //overridden toString method
    @Override
    public String toString() {
        return "TrafficLight{" +
               "lightName='" + lightName + '\'' +
               ", lightLocation'" + lightLocation + '\'' +
               ", lightActive=" + lightActive.name() +
               '}';
    }//end toString
    
    //more formatted return string option
    public String toFormattedString(){
        return lightName + " at " + lightLocation + "m: Current Light: " + lightActive.name();
    }//end toFormattedString()
}
