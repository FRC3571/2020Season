package frc.robot.commands.drivetrain;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.subsystem.DriveTrain;
import frc.robot.util.RobotMath;

public class TurnToAngle extends PIDCommand {

    private static final double kP = 0.015;
    private static final double kI = 0.000;
    private static final double kD = 0.00;
    private double angle;

    private static final double kToleranceDegrees = 2.0f;

    private DriveTrain driveTrain;
    private AHRS ahrs;

    public TurnToAngle(double angle) {
        super(kP, kI, kD);
        this.driveTrain = Robot.getInstance().getDrive();
        this.ahrs = Robot.getInstance().getNAVX().getAHRS();
        this.angle = angle;
    }

    public TurnToAngle(final double x, final double y) {
        super(kP, kI, kD);
        this.driveTrain = Robot.getInstance().getDrive();
        this.ahrs = Robot.getInstance().getNAVX().getAHRS();
        
        angle = RobotMath.getAngleFromPoint(x, y);

        if (Math.abs(angle - Robot.getInstance().getNAVX().getAHRS().getYaw()) > 90) {
            if (angle >= 0 && angle <= 180) {
                angle -= 180;
            } else {
                angle += 180;
            }
        }
    }

    @Override
    protected void initialize() {
        driveTrain.resetEncoders();

        driveTrain.setChosenGear(DriveTrain.Gear.FOURTH);

        this.setInputRange(-180.0f, 180.0f);
        this.setSetpoint(angle);
    }

    @Override
    protected double returnPIDInput() {
        return ahrs.getYaw();
    }

    @Override
    protected void usePIDOutput(double output) {
        if (output < 0) {
            output = RobotMath.mapDouble(output, -1.0, 0.0, -0.5, -0.2);
        } else {
            output = RobotMath.mapDouble(output, 0, 1, 0.2, 0.5);
        }

        Robot.getInstance().getDrive().arcadeDrive(0, -output);
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(angle - ahrs.getYaw()) < kToleranceDegrees ;
    }
    
}