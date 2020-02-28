package frc.robot.commands.auto;

import frc.robot.commands.drivetrain.DriveStraightDistance;
import frc.robot.commands.drivetrain.TurnToAngle;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveToPoint extends CommandGroup {
    double xTarget, yTarget, distance;
    public DriveToPoint(double x, double y) {
        xTarget = x;
        yTarget = y;

        addSequential(new TurnToAngle(xTarget, yTarget));
        addSequential(new DriveStraightDistance(xTarget, yTarget));
    }
}
