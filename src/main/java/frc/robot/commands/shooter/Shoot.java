package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystem.Shooter;

public class Shoot extends Command {

    private Shooter shooter;

    public Shoot() {
        this.shooter = Robot.getInstance().getShooter();
        setInterruptible(true);
        requires(shooter);
    }

    @Override
    protected void execute() {
        shooter.setMotors(shooter.getTopSpeed(), shooter.getBottomSpeed());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        shooter.setMotors(0, 0);
    }
}