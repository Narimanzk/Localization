package ca.mcgill.ecse211.project;

import simlejos.hardware.motor.Motor;
import simlejos.hardware.port.SensorPort;
import simlejos.hardware.sensor.EV3ColorSensor;
import simlejos.hardware.sensor.EV3UltrasonicSensor;
import simlejos.robotics.RegulatedMotor;
import simlejos.robotics.SampleProvider;

/**
 * Class for static resources (things that stay the same throughout the entire program execution),
 * like constants and hardware.
 * <br><br>
 * Use these resources in other files by adding this line at the top (see examples):<br><br>
 * 
 * {@code import static ca.mcgill.ecse211.project.Resources.*;}
 */
public class Resources {
  
  /** The number of threads used in your program (main, odometer). */
  public static final int NUMBER_OF_THREADS = 2;
  
  /** The initial number of physics steps performed at the start of the program. */
  public static final int INITIAL_NUMBER_OF_PHYSICS_STEPS = 50;
  
  /** The time between physics steps in milliseconds. */
  public static final int PHYSICS_STEP_PERIOD = 500; // ms
  
  /** The duration of the pause between ultrasonic and light localization. */
  public static final int PAUSE_DURATION = 10000; // performPhysicsStep() calls
  
  /** The wheel radius in meters. */
  public static final double WHEEL_RAD = 0.021;
  
  /** The robot width in meters. */
  public static final double BASE_WIDTH = 0.158;
  
  /** The speed at which the robot moves forward in degrees per second. */
  public static final int FORWARD_SPEED = 200;
  
  /** The speed at which the robot rotates in degrees per second. */
  public static final int ROTATE_SPEED = 80;
  
  /** The motor acceleration in degrees per second squared. */
  public static final int ACCELERATION = 1000;
  
  // Other constants and class variables
  
  /** The tile size in meters. Note that 0.3048 m = 1 ft. */
  public static final double TILE_SIZE = 0.3048;
  
  /** The maximum distance detected by the ultrasonic sensor, in cm. */
  public static final int MAX_SENSOR_DIST = 255;
  
  /** The limit of invalid samples that we read from the US sensor before assuming no obstacle. */
  public static final int INVALID_SAMPLE_LIMIT = 20;
  
  /** Threshold constant for rising and falling edge cases for the ultrasonic localizer. */
  public static final int COMMON_D = 40;

  /** Noise margin constant for falling edge ultrasonic localizer. */
  public static final int FALLINGEDGE_K = 1;

  /** Noise margin constant for rising edge ultrasonic localizer. */
  public static final int RISINGEDGE_K = 3;
  
  /** The odometer. */
  public static Odometer odometer = Odometer.getOdometer();
  
  // Hardware resources
  
  /** The left motor. */
  public static final RegulatedMotor leftMotor = Motor.A;
  
  /** The right motor. */
  public static final RegulatedMotor rightMotor = Motor.D;
  
  /** The ultrasonic sensor. */
  public static final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S1);
  
  /** The color sensor sample provider. */
  public static final SampleProvider colorSensor1 = new EV3ColorSensor(SensorPort.S2).getRGBMode();
  
  /** The color sensor sample provider. */
  public static final SampleProvider colorSensor2 = new EV3ColorSensor(SensorPort.S3).getRGBMode();
  
}
