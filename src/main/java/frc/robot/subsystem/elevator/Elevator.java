package frc.robot.subsystem.elevator;

import frc.robot.Robot;
import frc.robot.subsystem.Refreshable;
import frc.robot.util.Loggable;
import frc.robot.util.RobotMath;
import frc.robot.util.XboxController;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Subsystem implements Loggable, Refreshable {

    private ElevatorStage currentStage = ElevatorStage.BOTTOM;

    //motor ports
    private static int FIRST_MOTOR_PORT = 4;
    private static int SECOND_MOTOR_PORT = 5;

    //encoder ports/channels
    private static int ELEVATOR_ENCODER_CHANNEL_A = 0;
    private static int ELEVATOR_ENCODER_CHANNEL_B = 1;

    private static boolean FORWARD_DIRECTION, REVERSE_DIRECTION;
    private static CounterBase.EncodingType ENCODER_TYPE;

    //encoder mapping
    private static int COUNTS_PER_REVOLUTION;
    private static double WHEEL_RADIUS;

    //limit switch mapping
    private static int LIMIT_SWITCH_BOTTOM;

    static {

        FORWARD_DIRECTION = false;
        REVERSE_DIRECTION = true;

        ENCODER_TYPE = CounterBase.EncodingType.k1X;
        COUNTS_PER_REVOLUTION = 2048;
        WHEEL_RADIUS = 47.75; //in mm

        LIMIT_SWITCH_BOTTOM = 2;    //DIO = 2
    }

    //elevator
    //motors
    private Spark firstMotor;
    private Spark secondMotor;

    //encoder
    private Encoder elevatorEncoder;
    //driver controller
    private XboxController controller;

    public Elevator() {

        //initialize hardware
        firstMotor = new Spark(FIRST_MOTOR_PORT);
        secondMotor = new Spark(SECOND_MOTOR_PORT);
        initializeEncoders();
        controller = Robot.getInstance().getSubsystemController();
    }

    @Override
    public void refresh() {
        //run elevator code here
        //incase encoders stop working
        joystickControl(controller);
    }

    public void joystickControl(XboxController controller){
        if(Math.abs(controller.LeftStick.Y) >= 0.1) {
            setMotorsSpeed(-controller.LeftStick.Y);
        }
    }

    public void setMotorsSpeed(double i){
        firstMotor.set(i);
        secondMotor.set(i);
    }

    @Override
    public void log() {
       // SmartDashboard.putNumber("Elevator motor", ???? );
        SmartDashboard.putNumber("Elevator/Lift Distance", getDistance());
    }

    @Override
    protected void initDefaultCommand() {

    }

    public Spark getFirstMotor() {
        return firstMotor;
    }

    public Spark getSecondMotor() {
        return secondMotor;
    }

    public Encoder getDistanceEncoder() {
        return elevatorEncoder;
    }

    public double getDistance() {
        return elevatorEncoder.getDistance();
    }

    private void initializeEncoders() {
        elevatorEncoder = new Encoder(ELEVATOR_ENCODER_CHANNEL_A,
                ELEVATOR_ENCODER_CHANNEL_B,
                REVERSE_DIRECTION,
                ENCODER_TYPE);

        final double encoderLinearDistancePerPulse = RobotMath.getDistancePerPulse(COUNTS_PER_REVOLUTION, WHEEL_RADIUS);

        elevatorEncoder.setDistancePerPulse(encoderLinearDistancePerPulse);
    }

    public ElevatorStage getStage() {
        return currentStage;
    }

    public void setStage(ElevatorStage currentStage) {
        this.currentStage = currentStage;
    }


}

enum ElevatorStage {
    BOTTOM,
    MIDDLE,
    TOP
}
