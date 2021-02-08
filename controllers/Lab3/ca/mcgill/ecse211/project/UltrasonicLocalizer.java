package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import java.util.Arrays;
import simlejos.ExecutionController;

/**
 * The ultrasonic localizer.
 * This classes contains the falling edge and rising edge localization routines
 */
public class UltrasonicLocalizer {
  
  /**
   * Arbitrary threshold constant for rising and falling edge cases for the ultrasonic localizer
   */
  public static final int COMMON_D = 40;

  /**
   * Noise margin constant for falling edge ultrasonic localizer
   */
  public static final int FALLINGEDGE_K = 1;

  /**
   * Noise margin constant for rising edge ultrasonic localizer
   */
  public static final int RISINGEDGE_K = 3;
  
  private static int minInvalidSampleCount;
  
  private static int maxInvalidSampleCount;
  
  private static int prevDistance;
  
  private static int distance;
  
  /** The number of invalid samples seen by filter() so far. */
  private static int invalidSampleCount;
  
  /** Buffer (array) to store US samples. */
  private static float[] usData = new float[usSensor.sampleSize()];
  
  /**
   * angle at which back wall is detected
   */
  public static double alpha;
  /**
   * angle at which left wall is detected
   */
  public static double beta;
  
  /**
   * Localizes the robot to theta = 0.
   */
  public static void localize() {
    if(readUsDistance() < (COMMON_D - FALLINGEDGE_K)){
      risingEdge();
    }
    else {
      fallingEdge();
    }
  }
  
  /**
  * Method performs the falling edge localization.
  * Robot always completes an clockwise rotation around 
  * its center of rotation to record the value for alpha.
  * Then it rotates in the anti-clockwise direction to record value for beta.
  * Using the values recorded,
  * the robot will then appropriately orient itself accordingly along the 0 degree y-axis.
  * 
  * @author Narry Zendehrooh
  */
  public static void fallingEdge() {
    
    // clockwise rotation to record value for alpha
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(-1 * ROTATE_SPEED);
    leftMotor.forward();
    rightMotor.backward();
    while(true) {
      int readus = readUsDistance();
      //System.out.println("READUS BEFORE: " + readus);
      if(readus < COMMON_D - FALLINGEDGE_K) {
        //System.out.println("READUS: " + readus);
        alpha = odometer.getXyt()[2];
        leftMotor.setSpeed(0);
        rightMotor.setSpeed(0);
        break;
      }
    }
     //anti-clockwise rotation to record beta value
    turnBy(-alpha);
    leftMotor.backward();
    rightMotor.forward();
    
    while(true) {
      if(readUsDistance() < COMMON_D - FALLINGEDGE_K) {
        beta = odometer.getXyt()[2];
        leftMotor.setSpeed(0);
        rightMotor.setSpeed(0);
        break;
      }
    }
    System.out.println("THETA: " + odometer.getXyt()[2] + " ALPHA: " + alpha + " BETA: " + beta);
    if (alpha < beta)
      odometer.setTheta((225 - (alpha + beta) / 2) + odometer.getXyt()[2]);
    else
      odometer.setTheta((45 - (alpha + beta) / 2) + odometer.getXyt()[2]);

    // Approximately orienting against the 0 degree y-axis.
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(-1 * ROTATE_SPEED);
    turnBy(360 - odometer.getXyt()[2]);
    
  }
  
  public static void risingEdge() {
    
  // clockwise rotation to record alpha value
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(-1 * ROTATE_SPEED);
    leftMotor.forward();
    rightMotor.backward();
    while(true) {
      int readus = readUsDistance();
      //System.out.println("RISING: " + readus);
      if(readus > COMMON_D - FALLINGEDGE_K) {
        //System.out.println("READUS: " + readus);
        alpha = odometer.getXyt()[2];
        leftMotor.setSpeed(0);
        rightMotor.setSpeed(0);
        break;
      }
    }
    
  //anti-clockwise rotation to record beta value
    turnBy(-alpha);
    leftMotor.backward();
    rightMotor.forward();
    
    while(true) {
      if(readUsDistance() > COMMON_D - FALLINGEDGE_K) {
        beta = odometer.getXyt()[2];
        leftMotor.setSpeed(0);
        rightMotor.setSpeed(0);
        break;
      }
    }
    System.out.println("THETA: " + odometer.getXyt());
    if (alpha > beta)
      odometer.setTheta((225 - (alpha + beta) / 2) + odometer.getXyt()[2]);
    else
      odometer.setTheta((45 - (alpha + beta) / 2) + odometer.getXyt()[2]);

    // Approximately orienting against the 0 degree y-axis.
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(-1 * ROTATE_SPEED);
    turnBy(360 - odometer.getXyt()[2]);
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
  
  /** Returns the filtered distance between the US sensor and an obstacle in cm. */
  public static int readUsDistance() {
    int[] filterArr = new int[21]; 
    for(int i = 0; i < filterArr.length; i++) {
      usSensor.fetchSample(usData, 0);
      filterArr[i] = (int) usData[0] * 100; 
      ExecutionController.sleepFor(60);
    }
    return filter(filterArr);
  }
  
  /**
   * Rudimentary filter - toss out invalid samples corresponding to null signal.
   *
   * @param distance raw distance measured by the sensor in cm
   * @return the filtered distance in cm
   */
  static int filter(int[] arr) {
    Arrays.sort(arr);
    distance = arr[10];
    if (distance >= MAX_SENSOR_DIST && invalidSampleCount < INVALID_SAMPLE_LIMIT) {
      // bad value, increment the filter value and return the distance remembered from before
      invalidSampleCount++;
      return prevDistance;
    } else {
      if (distance < MAX_SENSOR_DIST) {
        // distance went below MAX_SENSOR_DIST: reset filter and remember the input distance.
        invalidSampleCount = 0;
      }
      prevDistance = distance;
      return distance;
    }
  }

}
