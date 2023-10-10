
package cmsc335.trafficsimulator;

/*
Author: UselessFodder
Project: Traffic GUI Project 3
Course: CMSC 335
*/
public class Car {
    private String carName;
    private double x;
    private double y;
    //speed in km/h
    private double speed;
    private double topSpeed;
    
    //constructors
    public Car(String carName){
        this.carName = carName;
        this.x = 0;
        this.y = 0;
        this.speed = 60; 
        this.topSpeed = this.speed;
    }
    public Car(String carName, double carSpeed){
        this.carName = carName;
        this.x = 0;
        this.y = 0;
        this.speed = carSpeed; 
        this.topSpeed = this.speed;
    }    
    public Car(String carName, double x, double y, double speed){
        this.carName = carName;
        this.x = x;
        this.y = y;
        this.speed = speed; 
        this.topSpeed = this.speed;
    }
    
    //moves car equal to speed (in meters) * 1 second (hour/60 min /60sec)
    public void moveCar(){
        double moveDistance = (speed * 1000)/3600;
        this.x = this.x + moveDistance;
    }
    
    //getters, setters
    public String getCarName(){
        return this.carName;
    }
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public double getSpeed(){
        return this.speed;
    }
    public double getTopSpeed(){
        return this.topSpeed;
    }
    public void setCarName(String carName){
        this.carName = carName;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void setSpeed(double speed){
        this.speed = speed;
    }
    public void setTopSpeed(double topSpeed){
        this.topSpeed = topSpeed;
    }
    
    //overridden toString method
    @Override
    public String toString() {
        return "Car{" +
               "carName='" + carName + '\'' +
               ", x=" + x +
               ", y=" + y +
               ", speed=" + speed +
               '}';
    }//end toString
    
    //more formatted return string option
    public String toFormattedString(){
        //x is truncated to 2 decimal places through floor() method
        return carName + " is at X: " + Math.floor(x *100)/100 + "m and Y: " + y + " going " + speed + "km/h";
    }//end toFormattedString()
}
