package frc.robot.commands.drivetrain;

import frc.robot.Robot;
import frc.robot.subsystem.DriveTrain;
import frc.robot.util.XboxController;
import edu.wpi.first.wpilibj.command.Command;

public class DriveJoystick extends Command {

    private DriveTrain driveTrain;
    private XboxController controller;

    public DriveJoystick() {
        this.driveTrain = Robot.getInstance().getDrive();
        this.controller = driveTrain.getController();
        setInterruptible(true);
        requires(driveTrain);
    }

    @Override
    protected void execute() {
        switch (driveTrain.ChosenDrive) {
        case AONEJOY:
            driveTrain.arcadeDrive(controller.rightStick.getY(), -controller.rightStick.getX());
            break;
        case ATWOJOY:
            driveTrain.arcadeDrive(controller.leftStick.getY(), -controller.rightStick.getX());
            break;
        case TANK:
            driveTrain.tankdrive(controller.leftStick.getY(), controller.rightStick.getY());
            break;
        default:
            driveTrain.arcadeDrive(controller.leftStick.getY(), -controller.rightStick.getX());
            break;
        }
    }

    @Override
    protected boolean isFinished() {
        return false; // Runs until interrupted
    }

    @Override
    protected void end() {
        driveTrain.arcadeDrive(0, 0);
    }
}
