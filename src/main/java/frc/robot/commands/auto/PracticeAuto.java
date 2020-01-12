package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PracticeAuto extends CommandGroup {
    double angle, distance;
    public PracticeAuto() {
       addSequential(new DriveToPoint(0,0));
    }

    @Override
    protected void initialize() {
        System.out.println("Executing practice autonomous");
    }
}
