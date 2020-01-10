package frc.robot.commands;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class FollowBall extends Command {
    private double xPos;

    public FollowBall() {
        requires(Robot.getInstance().getDrive());
        Robot.getInstance().getVisionProcessor();
    }

    @Override
    protected void initialize() {
        xPos = Robot.getInstance().getVisionProcessor().yellowBallxPos();

        if (xPos > 0.05) Robot.getInstance().getDrive().tankdrive(0.7, -0.7);
        else if (xPos < -0.05) Robot.getInstance().getDrive().tankdrive(-0.7, 0.7);
    }

    @Override
    protected void execute(){
        xPos = Robot.getInstance().getVisionProcessor().yellowBallxPos();
    }
    
    
    @Override
    protected boolean isFinished() {
        return (xPos < 0.05 && xPos > -0.05);
    }

    @Override
    protected void end(){

        Robot.getInstance().getDrive().arcadeDrive(0, 0);
    }
}
