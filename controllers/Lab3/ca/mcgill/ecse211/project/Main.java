package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.FORWARD_SPEED;
import static ca.mcgill.ecse211.project.Resources.INITIAL_NUMBER_OF_PHYSICS_STEPS;
import static ca.mcgill.ecse211.project.Resources.NUMBER_OF_THREADS;
import static ca.mcgill.ecse211.project.Resources.PAUSE_DURATION;
import static ca.mcgill.ecse211.project.Resources.PHYSICS_STEP_PERIOD;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.odometer;
import static ca.mcgill.ecse211.project.Resources.rightMotor;

import simlejos.ExecutionController;


/**
 * Main class of the program.
 */
public class Main {
  
  /**
   * Main entry point.
   */
  public static void main(String[] args) {
    // Run a few physics steps to make sure everything is initialized and has settled properly
    ExecutionController.performPhysicsSteps(INITIAL_NUMBER_OF_PHYSICS_STEPS);
    ExecutionController.setNumberOfParties(NUMBER_OF_THREADS);
    ExecutionController.performPhysicsStepsInBackground(PHYSICS_STEP_PERIOD);
    leftMotor.setSpeed(FORWARD_SPEED);
    rightMotor.setSpeed(FORWARD_SPEED);
    new Thread(odometer).start();

    // TODO Implement the localizers in their respective classes.
    UltrasonicLocalizer.localize();
    pause();
    LightLocalizer.localize();

    odometer.printPosition();
  }

  /**
   * Halts the robot for a while to allow pausing the simulation to evaluate ultrasonic
   * localization.
   */
  private static void pause() {
    System.out.println("Ultrasonic localization completed. Pause simulation now!");
    leftMotor.setSpeed(0);
    rightMotor.setSpeed(0);
    
    for (int i = 0; i < PAUSE_DURATION; i++) {
      ExecutionController.waitUntilNextStep();
    }
    
    leftMotor.setSpeed(FORWARD_SPEED);
    rightMotor.setSpeed(FORWARD_SPEED);
  }

}
