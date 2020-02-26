package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystem.Shooter;

public class ChangeShooterPower extends InstantCommand {

    private Shooter shooter;
    private boolean increase;
    private double amount;

    public ChangeShooterPower(boolean increase) {
        this.shooter = Robot.getInstance().getShooter();
        this.increase = increase;
        requires(shooter);
    }

    @Override
    protected void initialize() {
        amount = 0.05;
        if (!increase)
            amount *= -1;
    }

    @Override
    protected void execute() {
        shooter.setSpeed(shooter.getBottomSpeed() + amount);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }
}