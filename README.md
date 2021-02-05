# Lab 3 - Localization

_Read this entire document before doing anything._

**Code due: Immediately before your demo(s), Tuesday 9 February at the latest**

**Report due: Thursday 11 February, 23:59 EST (Montréal time)**

This is the repository that contains the required files for the localization lab.
For lab objectives, demo and report requirements, and submission instructions, see
detailed instructions on MyCourses.

## Design Process

Unlike the previous labs, you will need to implement more than one method. The increased
complexity will make it harder to track your changes if you don't use version control (ie, `git`).
Please contact a TA if you need help with this.

Since this a **design** course, we emphasize elements of the **design process** here. They will be
covered in more detail in the coming weeks.

1. The first thing you should do when starting a complex task to **understand the requirements**.
Go over the provided instructions, this README, and have a look at the starter code (but don't
change anything yet!) and make sure you and your lab partner know the lab objectives.

1. Work with your partner to come up with **high level designs** for different program components,
eg, light localization. Keep these designs (eg, flowcharts, sketches, formula
derivations) since you will be asked to show them during the demo, and you should include the most
relevant ones in your report. Do this **before** writing any code.

1. **Implement** your designs in the given starter code. Both you and your partner must make meaningful
contributions to your submission. GitHub tracks these contributions automatically.

1. **Validate** your implementation to ensure it meets the all the given requirements.

1. If your implementation does not meet all requirements, go back to step 1 and **refine your design**.
It is unlikely that you will get everything right the first try, so don't be discouraged if you
repeat this cycle many times, that is expected.

**Note:** Your design process in the Research and Development (R&D) phase will be important later on in the
project, so ensure your design can be updated with future requirements, eg, can you localize in all four
corners of the given world (including the two red ones)? When implementing the light localizer, keep in 
mind that you will need to localize in the middle of the playing field in future labs and the project.


## Implementation Details

We describe the starter code and a few implementation details below. You will need to complete
missing parts for all the files described below (including any tasks mentioned in the code but not
in this README). Since we cannot cover all details here, please post any questions you have in the discussion board.

[`Main.java`](controllers/Lab3/ca/mcgill/ecse211/project/Main.java) is the main entry point for
your program. The main method sets up the program and starts the odometry thread. The execution controller
performs physics steps in the background at a rate defined by `Resources.PHYSICS_STEP_PERIOD`
(the default is 500 ms).

[`Odometer.java`](controllers/Lab3/ca/mcgill/ecse211/project/Odometer.java) keeps track of the
robot's position (_x_, _y_, _θ_) on the playing field. The provided implementation is based on what has been covered in class with some minor tweaks. 

[`Resources.java`](controllers/Lab3/ca/mcgill/ecse211/project/Resources.java) contains static 
resources (things that stay the same throughout the entire program execution), like constants and
hardware. Use these resources in other files by adding this line at the top (see given files for
examples):

```java
import static ca.mcgill.ecse211.project.Resources.*;
```

You will need to set some of the constants in this file based on your specific robot. You can
determine your robot dimensions using in Webots. Each node of your robot has a position, so you
can calculate your robot dimensions using that. You may need to tweak your values after testing.

**You will need to implement the following classes:**

- `UltrasonicLocalizer`: Responsible for handling the ultrasonic localization. The ultrasonic localization method can be implemented as a falling edge, rising edge, or an other known implementation of your choice. You may want to reuse some methods from previous labs regarding the use of the ultrasonic sensor (reading sensor distances through a filter) as well as conversion methods (convert distance and angle). Note that you might want to call `ExecutionController.sleepFor()` right after changing rotation direction while implementing rising or falling edge.

- `LightLocalizer`: Responsible for handling the light localization. Note that the light localization method should not have fixed thresholds for line detection as luminosity values will vary during demos. You are allowed to use more than one light sensor.

The localizer classes should not be run as separate threads for Lab 3.

[`lab3.wbt`](worlds/lab3.wbt) is the Webots world file for Lab 3. 
The world will have two markers as specified in the handout where the vector from the red to the blue marker defines the 0 degree theta.
Note that during the demo, you may be asked to change the world luminosity levels and/or the textured background. These can be found under the TexturedBackground tree.
