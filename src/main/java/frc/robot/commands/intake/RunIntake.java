package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystem.Intake;

public class RunIntake extends Command {

    private Intake intake;
    
    private static final double kSpeed = 0.5;

    public RunIntake() {
        this.intake = Robot.getInstance().getIntake();
        requires(intake);
    }

    @Override
    protected void execute() {
        intake.setMotor(kSpeed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        intake.setMotor(0);
    }
}