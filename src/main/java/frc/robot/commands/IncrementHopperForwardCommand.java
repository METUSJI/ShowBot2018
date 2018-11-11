/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import java.util.Timer;
import java.util.TimerTask;

public class IncrementHopperForwardCommand extends Command {

  Timer timer;

  private double speed;

  public IncrementHopperForwardCommand(double speed) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.hopper);
    this.speed = speed;
  }
  
  private boolean initialState;
  private int increment;
  private boolean lastState;
  private boolean thisState;

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("timer triggered");
        end();
      }
    }, 750L);
    initialState = Robot.hopper.getHopperSwitchState();
    lastState = Robot.hopper.getHopperSwitchState();
    increment = 0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    thisState = Robot.hopper.getHopperSwitchState();
    if(thisState != lastState) {
      increment++;
      lastState = thisState;
    }
    System.out.println("execute running");
    Robot.hopper.driveHopper(speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return (initialState && increment == 1) || (!initialState && increment == 2);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.hopper.driveHopper(0);
    timer.cancel();
    if(initialState)
      increment = 1;
    else
      increment = 2;
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
