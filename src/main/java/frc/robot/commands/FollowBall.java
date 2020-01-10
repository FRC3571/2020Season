package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.subsystem.DriveTrain;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;

public class FollowBall extends Command {
    private final NetworkTableEntry xCoord;
    private double number;

    public FollowBall(final NetworkTableEntry e) {
        xCoord = e;
        requires(Robot.getInstance().getDrive());
    }

    @Override
    protected void initialize() {
        number = xCoord.getDouble(0);; //have to do this cuz camera is upside down
        
    }

    @Override
    protected void execute(){
        System.out.println("THIS IS WOKRING AND " + number);
        if (number > 0.4) Robot.getInstance().getDrive().arcadeDrive(0, 0.3);
        else if (number < -0.4) Robot.getInstance().getDrive().arcadeDrive(0, -0.3);
        number = xCoord.getDouble(0);
    }
    
    
    @Override
    protected boolean isFinished() {
        return (number < 0.4 && number > -0.4);
    }

    @Override
    protected void end(){

        Robot.getInstance().getDrive().arcadeDrive(0, 0);
    }
}
