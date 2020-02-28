package frc.robot.commands.drivetrain;

import frc.robot.Robot;
import frc.robot.subsystem.DriveTrain;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ChangeGear extends InstantCommand {
    private boolean up;
    private DriveTrain driveTrain;

    public ChangeGear(boolean up) {
        this.up = up;
    }

    @Override
    protected void initialize() {
        this.driveTrain = Robot.getInstance().getDrive();
    }

    @Override
    protected void execute() {
        if (up) {
            switch (driveTrain.getChosenGear()) {
            case FIRST:
                driveTrain.setChosenGear(DriveTrain.Gear.SECOND);
                break;
            case SECOND:
                driveTrain.setChosenGear(DriveTrain.Gear.THIRD);
                break;
            case THIRD:
                driveTrain.setChosenGear(DriveTrain.Gear.FOURTH);
                break;
            case FOURTH:
                break;
            default:
                break;
            }
        } else {
            switch (driveTrain.getChosenGear()) {
            case FIRST:
                break;
            case SECOND:
                driveTrain.setChosenGear(DriveTrain.Gear.FIRST);
                break;
            case THIRD:
                driveTrain.setChosenGear(DriveTrain.Gear.SECOND);
                break;
            case FOURTH:
                driveTrain.setChosenGear(DriveTrain.Gear.THIRD);
                break;
            default:
                break;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
