package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.util.RobotMath;
import edu.wpi.first.wpilibj.command.Command;

public class FollowBall extends Command {
    private double xPos, speed;

    public FollowBall() {
        requires(Robot.getInstance().getDrive());
    }

    @Override
    protected void initialize() {
        xPos = Robot.getInstance().getVisionProcessor().yellowBallxPos();
    }

    @Override
    protected void execute() {
        xPos = Robot.getInstance().getVisionProcessor().yellowBallxPos();

        if (xPos > 0.05) {
            speed = RobotMath.mapDouble(xPos, 0, 1, 0.7, 1);
            Robot.getInstance().getDrive().tankdrive(speed, -speed);
        } else if (xPos < -0.05) {
            speed = RobotMath.mapDouble(xPos, -1, 0, 0.7, 1);
            Robot.getInstance().getDrive().tankdrive(-speed, speed);
        }
    }

    @Override
    protected boolean isFinished() {
        return (xPos < 0.05 && xPos > -0.05);
    }

    @Override
    protected void end() {
        Robot.getInstance().getDrive().arcadeDrive(0, 0);
    }
}
