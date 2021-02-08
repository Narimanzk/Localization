package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import simlejos.ExecutionController;


/**
 * The light localizer.
 * TODO: Give an overview of your light localizer here, in the Javadoc comment.
 */
public class LightLocalizer {
  
  /**
   * Threshold value to determine if a black line is detected or not
   */
  private static final int THRESHOLD = 10000;
 
  private static float[] sensor1_data = new float[colorSensor1.sampleSize()];
  
  private static float[] sensor2_data = new float[colorSensor2.sampleSize()];
  
  private static int current_color_value_s1 = 1000;
  
  private static int current_color_value_s2 = 1000;
  
  private static double sampling_period = ((180* 0.005)/(Math.PI * WHEEL_RAD * FORWARD_SPEED));
  
  private static double d1;
  
  private static double d2;
  
  private static double prevsen1 = 0.0d;
  
  private static double prevsen2 = 0.0d;
  /**
   * Localizes the robot to (x, y, theta) = (1, 1, 0).
   */
  public static void localize() {
    leftMotor.forward();
    rightMotor.forward();
    while(true) {
//      colorSensor1.fetchSample(sensor1_data, 0);
//      colorSensor2.fetchSample(sensor2_data, 0);
//      d1 = (((sensor1_data[0]*1000) - prevsen1) / sampling_period);
//      d2 = (((sensor2_data[0]*1000) - prevsen2) / sampling_period);
//      prevsen1 = sensor1_data[0]*1000;
//      prevsen2 = sensor2_data[0]*1000;
//      System.out.println("Sens1: " + d1 + " prevsen1: " + prevsen1 + " sesnor data: " + sensor1_data[0]);
      //System.out.println("Sens2: " + sensor2_data[0]);
      //if(d1 == 0 && d2 == 0) {
        //System.out.println("Sens1: IF " + d1);
        //System.out.println("Sens2: " + sensor2_data[0]);
        //leftMotor.stop();
        //rightMotor.stop();
        //break;
      //}
      
      
      //ExecutionController.sleepFor((int)(sampling_period*1000));
      
      
      if(blackLineTrigger()) {
        leftMotor.stop();
        rightMotor.stop();
        moveStraightFor(-0.0273*2);
        turnBy(90.0);
        break;
      }
    }
    while(true) {
      leftMotor.setSpeed(FORWARD_SPEED);
      rightMotor.setSpeed(FORWARD_SPEED);
      leftMotor.forward();
      rightMotor.forward();
      
      if(blackLineTrigger()) {
        leftMotor.stop();
        rightMotor.stop();
        moveStraightFor(-0.0273*2);
        turnBy(-90.0);
        break;
      }
      
    }
  }
  
  /**
   * Moves the robot straight for the given distance.
   *
   * @param distance in feet (tile sizes), may be negative
   */
  public static void moveStraightFor(double distance) {
    //Set motor speeds and rotate them by the given distance.
    // This method will not return until the robot has finished moving.
    leftMotor.setSpeed(FORWARD_SPEED);
    rightMotor.setSpeed(FORWARD_SPEED);
    leftMotor.rotate(convertDistance(distance), true);
    rightMotor.rotate(convertDistance(distance), false);
    
  }
  

  
  /**
   * Turns the robot by a specified angle. Note that this method is different from
   * {@code Navigation.turnTo()}. For example, if the robot is facing 90 degrees, calling
   * {@code turnBy(90)} will make the robot turn to 180 degrees, but calling
   * {@code Navigation.turnTo(90)} should do nothing (since the robot is already at 90 degrees).
   *
   * @param angle the angle by which to turn, in degrees
   */
  public static void turnBy(double angle) {
    //Similar to moveStraightFor(), but with a minus sign
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-(convertAngle(angle)), false);
  }
  
  /**
   * Converts input distance to the total rotation of each wheel needed to cover that distance.
   *
   * @param distance the input distance in meters
   * @return the wheel rotations necessary to cover the distance in degrees
   */
  public static int convertDistance(double distance) {
    // Using arc length formula to calculate the distance + scaling
    return (int) ((180 * distance) / (Math.PI * WHEEL_RAD) * 100) / 100;
  }

  /**
   * Converts input angle to the total rotation of each wheel needed to rotate the robot by that
   * angle.
   *
   * @param angle the input angle in degrees
   * @return the wheel rotations necessary to rotate the robot by the angle in degrees
   */
  public static int convertAngle(double angle) {
    // Using convertDistance method to calculate constant rotation of the robot + scaling
    return convertDistance((Math.PI * BASE_WIDTH * angle / 360.0) * 100) / 100;
  }
  
  public static boolean blackLineTrigger() {
    colorSensor1.fetchSample(sensor1_data, 0);
    colorSensor2.fetchSample(sensor2_data, 0);
    
    current_color_value_s1 = (int) (sensor1_data[0] * 100);
    
    current_color_value_s2 = (int) (sensor2_data[0] * 100);
    
    System.out.println("Color1 val: " + current_color_value_s1);
    System.out.println("Color2 val: " + current_color_value_s2);
    if(current_color_value_s1 < THRESHOLD && current_color_value_s2 < THRESHOLD) {
      return true;
    }
    else {
      return false;
    }
  }

}
