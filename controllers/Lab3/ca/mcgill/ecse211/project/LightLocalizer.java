package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import simlejos.ExecutionController;
import simlejos.robotics.SampleProvider;


/**
 * The light localizer.
 * TODO: Give an overview of your light localizer here, in the Javadoc comment.
 */
public class LightLocalizer {
  
  /* Threshold value to determine if a black line is detected or not */
  private static final int THRESHOLD = 10000;
 
  /** Buffer (array) to store US1 samples. */
  private static float[] sensor1_data = new float[colorSensor1.sampleSize()];
  
  /** Buffer (array) to store US2 samples. */
  private static float[] sensor2_data = new float[colorSensor2.sampleSize()];
  
  //Values to operate color sensor
  private static int current_color_value = 1000;
  
  /**
   * Localizes the robot to (x, y, theta) = (1, 1, 0).
   */
  public static void localize() {
    //Moving toward black line
    leftMotor.forward();
    rightMotor.forward();
    
    //First time that the robot pass the black line
    while (true) {
      if (blackLineTrigger(colorSensor1, sensor1_data) &&  blackLineTrigger(colorSensor2, sensor2_data)) {
        leftMotor.stop();
        rightMotor.stop();
        moveStraightFor(-0.0273 * 2);
        turnBy(90.0);
        break;
      }
      else if (blackLineTrigger(colorSensor1, sensor1_data)) {
        rightMotor.stop();
        if (blackLineTrigger(colorSensor2, sensor2_data)) {
          leftMotor.stop();
          moveStraightFor(-0.0273 * 2);
          turnBy(90.0);
          break;
        }
      }
      else if (blackLineTrigger(colorSensor2, sensor2_data)) {
        leftMotor.stop();
        if (blackLineTrigger(colorSensor1, sensor1_data)) {
          rightMotor.stop();
          moveStraightFor(-0.0273 * 2);
          turnBy(90.0);
          break;
        }
      }
    }
    //Moving toward (1,1)
    while (true) {
      leftMotor.setSpeed(FORWARD_SPEED);
      rightMotor.setSpeed(FORWARD_SPEED);
      leftMotor.forward();
      rightMotor.forward();      
//      //When it reaches (1,1)
      if (blackLineTrigger(colorSensor1, sensor1_data) &&  blackLineTrigger(colorSensor2, sensor2_data)) {
        leftMotor.stop();
        rightMotor.stop();
        moveStraightFor(-0.0273 * 2);
        turnBy(-90.0);
        break;
      }
      else if (blackLineTrigger(colorSensor1, sensor1_data)) {
        rightMotor.stop();
        if (blackLineTrigger(colorSensor2, sensor2_data)) {
          leftMotor.stop();
          moveStraightFor(-0.0273 * 2);
          turnBy(-90.0);
          break;
        }
      }
      else if (blackLineTrigger(colorSensor2, sensor2_data)) {
        leftMotor.stop();
        if (blackLineTrigger(colorSensor1, sensor1_data)) {
          rightMotor.stop();
          moveStraightFor(-0.0273 * 2);
          turnBy(-90.0);
          break;
        }
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
  
  /**
   * The method fetches data recorded by the color sensors in RedMode 
   * and compares the most recent value to verify if the
   * robot has traveled over a black line.
   * Method makes use of a fixed threshold value which may not be reliable in
   * certain conditions, however it has been tested and conditioned to minimize false negatives.
   * @return true if black line is detected by both sensors.
   */
  public static boolean blackLineTrigger(SampleProvider colorSensor, float[] sensor) {
    colorSensor.fetchSample(sensor, 0);
    
    current_color_value = (int) (sensor[0] * 100);
    
    if (current_color_value < THRESHOLD) {
      return true;
    } else {
      return false;
    }
  }
  
  

}
