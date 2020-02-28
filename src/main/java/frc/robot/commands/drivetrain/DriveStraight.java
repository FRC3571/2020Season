package frc.robot.commands.drivetrain;

import frc.robot.Robot;
import frc.robot.subsystem.DriveTrain;
import frc.robot.util.XboxController;
import edu.wpi.first.wpilibj.command.PIDCommand;

public class DriveStraight extends PIDCommand {

    private XboxController controller;
    private DriveTrain driveTrain;

    public DriveStraight() {

        super(1, 0, 0);
        this.driveTrain = Robot.getInstance().getDrive();
        this.controller = Robot.getInstance().getDriverController();

        requires(driveTrain);
    }

    @Override
    protected void initialize() {
        driveTrain.resetEncoders();
    }

    @Override
    protected double returnPIDInput() {
        return Math.abs(-driveTrain.getLeftFrontEncoder().getPosition() - driveTrain.getRightFrontEncoder().getPosition());
    }

    @Override
    protected void usePIDOutput(double output) {
        System.out.println(controller.LT.getX());
        driveTrain.arcadeDrive(controller.LT.getX(), output);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

