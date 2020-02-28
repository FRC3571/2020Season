package frc.robot.commands.drivetrain;

import frc.robot.Robot;
import frc.robot.subsystem.DriveTrain;
import frc.robot.util.RobotMath;

//import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;

//import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightDistance extends Command {
    private double targetDistance;
    private CANPIDController leftPID, rightPID;
    private double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxVel, minVel, maxAcc, allowedErr;
    private double error;

    public DriveStraightDistance(double d) {

        targetDistance = d;
        leftPID = Robot.getInstance().getDrive().getLeftFrontMotor().getPIDController();
        rightPID = Robot.getInstance().getDrive().getRightFrontMotor().getPIDController();

        // PID coefficients
        kP = 5e-5;
        kI = 1e-6;
        kD = 0;
        kIz = 0;
        kFF = 0.000156; // 0.0003
        kMaxOutput = 1;
        kMinOutput = -1;

        // Smart Motion Coefficients
        maxVel = 2000; // rpm
        maxAcc = 1500;
        allowedErr = 0.001;

        // set PID coefficients
        leftPID.setP(kP);
        leftPID.setI(kI);
        leftPID.setD(kD);
        leftPID.setIZone(kIz);
        leftPID.setFF(kFF);
        leftPID.setOutputRange(kMinOutput, kMaxOutput);

        rightPID.setP(kP);
        rightPID.setI(kI);
        rightPID.setD(kD);
        rightPID.setIZone(kIz);
        rightPID.setFF(kFF);
        rightPID.setOutputRange(kMinOutput, kMaxOutput);

        int smartMotionSlot = 0;
        leftPID.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        leftPID.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        leftPID.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        leftPID.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

        rightPID.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        rightPID.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        rightPID.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        rightPID.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

        requires(Robot.getInstance().getDrive());
    }

    public DriveStraightDistance(double x, double y) {
        double xTarget = x;
        double yTarget = y;

        
        targetDistance = RobotMath.getDistanceFromPoint(xTarget, yTarget);

        if (Math.abs(RobotMath.getAngleFromPoint(xTarget, yTarget) - Robot.getInstance().getNAVX().getAHRS().getYaw()) > 90){
            targetDistance *= -1;
        }

        leftPID = Robot.getInstance().getDrive().getLeftFrontMotor().getPIDController();
        rightPID = Robot.getInstance().getDrive().getRightFrontMotor().getPIDController();

        // PID coefficients
        kP = 5e-5;
        kI = 1e-6;
        kD = 0;
        kIz = 0;
        kFF = 0.000156; // 0.0003
        kMaxOutput = 1;
        kMinOutput = -1;

        // Smart Motion Coefficients
        maxVel = 2000; // rpm
        maxAcc = 1500;
        allowedErr = 0.001;

        // set PID coefficients
        leftPID.setP(kP);
        leftPID.setI(kI);
        leftPID.setD(kD);
        leftPID.setIZone(kIz);
        leftPID.setFF(kFF);
        leftPID.setOutputRange(kMinOutput, kMaxOutput);

        rightPID.setP(kP);
        rightPID.setI(kI);
        rightPID.setD(kD);
        rightPID.setIZone(kIz);
        rightPID.setFF(kFF);
        rightPID.setOutputRange(kMinOutput, kMaxOutput);

        int smartMotionSlot = 0;
        leftPID.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        leftPID.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        leftPID.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        leftPID.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

        rightPID.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        rightPID.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        rightPID.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        rightPID.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

        requires(Robot.getInstance().getDrive());
    }

    @Override
    protected void initialize() {
        Robot.getInstance().getDrive().resetEncoders();
        Robot.getInstance().getDrive().setChosenGear(DriveTrain.Gear.FOURTH);
    }

    @Override
    protected void execute() {
        leftPID.setReference(-targetDistance, ControlType.kSmartMotion);
        rightPID.setReference(targetDistance, ControlType.kSmartMotion);
        

        error = Math.abs(targetDistance - Robot.getInstance().getDrive().getDistance());
    }

    @Override
    protected boolean isFinished() {
        return error < 0.001;
    }

    @Override
    protected void end() {
        //Robot.getInstance().getDrive().reset();
        Robot.getInstance().getDrive().setChosenGear(DriveTrain.Gear.SECOND);
    }
}
