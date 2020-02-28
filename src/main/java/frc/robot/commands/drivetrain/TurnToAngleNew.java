package frc.robot.commands.drivetrain;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.subsystem.DriveTrain;

public class TurnToAngleNew extends PIDCommand {

    private static double p, i, d;

    private DriveTrain driveTrain;
    private AHRS ahrs;

    public TurnToAngleNew() {
        super(p, i, d);
        this.driveTrain = Robot.getInstance().getDrive();
        this.ahrs = Robot.getInstance().getNAVX().getAHRS();
    }

    @Override
    protected void initialize() {
        driveTrain.resetEncoders();

        driveTrain.setChosenGear(DriveTrain.Gear.FOURTH);
    }

    @Override
    protected double returnPIDInput() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected void usePIDOutput(double output) {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }
    
}