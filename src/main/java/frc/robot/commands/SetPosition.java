package frc.robot.commands;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class SetPosition extends Command {
    double xPos;
    double yPos;

    public SetPosition(double x, double y) {
        xPos = x;
        yPos = y;
        requires(Robot.getInstance().getDrive());
    }

    @Override
    protected void initialize() {
        Robot.getInstance().getDrive().setxPos(xPos);
        Robot.getInstance().getDrive().setyPos(xPos);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }
}
