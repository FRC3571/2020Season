package frc.robot.commands.drivetrain;

import frc.robot.Robot;
import frc.robot.subsystem.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

public class ChangeGear extends Command {
    private int gear;

    public ChangeGear(int d) {
        gear = d;
    }

    @Override
    protected void initialize() {
        switch (gear) {
            case 1:
                Robot.getInstance().getDrive().setChosenGear(DriveTrain.Gear.FIRST);
                break;
            case 2:
                Robot.getInstance().getDrive().setChosenGear(DriveTrain.Gear.SECOND);
                break;
            case 3:
                Robot.getInstance().getDrive().setChosenGear(DriveTrain.Gear.THIRD);
                break;
            case 4:
                Robot.getInstance().getDrive().setChosenGear(DriveTrain.Gear.FOURTH);
                break;
            default:
                break;
        }
    }
    
    @Override
    protected boolean isFinished() {
        return true;
    }
}
