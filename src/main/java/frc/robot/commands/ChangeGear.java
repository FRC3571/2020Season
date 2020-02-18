package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.Constants.DriveConstants.Gear;

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
                Robot.getInstance().getDrive().setChosenGear(Gear.FIRST);
                break;
            case 2:
                Robot.getInstance().getDrive().setChosenGear(Gear.SECOND);
                break;
            case 3:
                Robot.getInstance().getDrive().setChosenGear(Gear.THIRD);
                break;
            case 4:
                Robot.getInstance().getDrive().setChosenGear(Gear.FOURTH);
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
