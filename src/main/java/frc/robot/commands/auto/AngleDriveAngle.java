package frc.robot.commands.auto;

import frc.robot.commands.drivetrain.DriveStraightDistance;
import frc.robot.commands.drivetrain.TurnToAngle;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AngleDriveAngle extends CommandGroup {

    public AngleDriveAngle(double a1, double d, double a2) {
        addSequential(new TurnToAngle(a1));
        addSequential(new DriveStraightDistance(d));
        addSequential(new TurnToAngle(a2));
    }
}
