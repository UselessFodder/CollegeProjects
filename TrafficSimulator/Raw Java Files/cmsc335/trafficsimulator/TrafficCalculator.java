package cmsc335.trafficsimulator;

//imports
import java.util.ArrayList;

/*
Author: UselessFodder
Project: Traffic GUI Project 3
Course: CMSC 335
*/
public class TrafficCalculator {
    //hold list of cars and traffic lights
    private ArrayList<Car> cars;
    private ArrayList<TrafficLight> trafficLights;
    
    //constructor
    public TrafficCalculator() {
        //start with empty arrays
        cars = new ArrayList<>();
        trafficLights = new ArrayList<>();
        
        //add three traffic lights as per parameter 2
        addTrafficLight();
        addTrafficLight();
        addTrafficLight();
                
    }//end constructor
    
    //add a bew car to simulation with no defined name or values
    public void addCar(){
        //get current count of cars to name
        int carNum = cars.size()+1;
        String newCarName = "Car " + carNum;
        //creat new car based on this value
        Car newCar = new Car(newCarName);
        cars.add(newCar);
    }//end addCar()
    
    //add new car to simulation with a defined speed
    public void addCar(int carSpeed){
        //get current count of cars to name
        int carNum = cars.size()+1;
        String newCarName = "Car " + carNum;
        //creat new car based on parameter speed value
        Car newCar = new Car(newCarName,carSpeed);
        cars.add(newCar);
    }//end addCar(String)
    
    //add new car to simulation with a defined name
    public void addCar(int carSpeed, String carName){
        Car newCar = new Car(carName, carSpeed);
        cars.add(newCar);
    }//end addCar(String)
    
    //add new traffic light to simulation
    public void addTrafficLight(){
        //create light name and location based on number of lights already existant
        int lightNum = trafficLights.size()+1;
        String newLightName = "Light " + lightNum;
        double newLightLocation = lightNum*1000;
        //create new light based on these values and add to ArrayList
        TrafficLight newTrafficLight = new TrafficLight(newLightName, newLightLocation);
        trafficLights.add(newTrafficLight);
    }//end addTrafficLight()
    
    //add new traffic light to simulation with a defined light state
    public void addTrafficLight(TrafficLight.LightState currentstate){
        //create light name and location based on number of lights already existant
        int lightNum = trafficLights.size()+1;
        String newLightName = "Light " + lightNum;
        double newLightLocation = lightNum*1000;
        //create new light based on these values and add to ArrayList
        TrafficLight newTrafficLight = new TrafficLight(newLightName,currentstate, newLightLocation);
        trafficLights.add(newTrafficLight);
    }//end addTrafficLight(String)
    
    //add new traffic light to simulation with a defined name and lightState
    public void addTrafficLight(TrafficLight.LightState currentstate, String lightName){
        //create light location based on number of lights already existant
        int lightNum = trafficLights.size()+1;
        double newLightLocation = lightNum*1000;
        //create new light based on these values and add to ArrayList
        TrafficLight newTrafficLight = new TrafficLight(lightName, newLightLocation);
        trafficLights.add(newTrafficLight);
    }//end addTrafficLight(String)
    
    //getters
    public ArrayList<Car> getCars(){
        return this.cars;
    }
    public ArrayList<TrafficLight> getTrafficLights(){
        return this.trafficLights;
    }
    
    //Simulate car movement
    public void moveCars(){
        //loop through all cars
        for (Car car : cars){
            //latch variable to verify car is not at light
            boolean atLight = false;
            //latch variable
            //loop through all lights looking for red
            for (TrafficLight light : trafficLights){
                //get workable object values
                double carX = car.getX();
                double carNextX = (car.getSpeed()*1000)/3600 + carX;
                double lightX = light.getLightLocation();                
                
                //check if light is on red
                if (light.getLightActive() == TrafficLight.LightState.RED){
                    //check if car is right at light
                    if (carX == lightX) {
                        //if so, set speed to 0 and set bool
                        car.setSpeed(0);
                        atLight = true;
                    } else {
                        //if not, check if car would drive past light               
                        if (carX < lightX && carNextX > lightX){
                            //if so, set car to light location, turn speed to 0 and set atLight
                            car.setX(lightX);
                            car.setSpeed(0);
                            atLight = true;
                        }//end if (carX < lightX && carNextX > lightX
                    }//end if-else
                }//end if (light.getLightActive() == TrafficLight.LightState.RED
             }//end for (TrafficLight light : trafficLights)
            
            //if car is not at a red light, then move car 1 second worth of distance
            if (!atLight){
                //set car back to normal speed
                car.setSpeed(car.getTopSpeed());
                //move 1 second worth
                car.moveCar();
            }//end if(!atLight)
            
        }//end for (Car car : cars)
    }//end moveCars()
    
    //Change traffic lights when directed
    public void checkLights(){
        for (TrafficLight light : trafficLights){
            //update all lights if needed
            light.checkTimer();
        }//end for(TrafficLight light : trafficLights)
    }//end changeLight()
    
}
