package frc.robot.commands.controlpanelwheel;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystem.ControlPanelWheel;
import frc.robot.subsystem.ControlPanelWheel.ColorWheelColor;

public class PositionControl extends Command {
    private ControlPanelWheel controlPanelWheel;
    private ColorWheelColor target;
    private double timeSpentOnColor;
    private double motorSpeed;


    public PositionControl(){
        this.controlPanelWheel = Robot.getInstance().getControlPanelWheel();
        requires(controlPanelWheel);
    }

    @Override
    protected void initialize() {
        switch (controlPanelWheel.getColorAssignment()){
            case BLUE:
            target = ColorWheelColor.RED;
            break;
            case RED:
            target = ColorWheelColor.BLUE;
            break;
            case YELLOW:
            target = ColorWheelColor.GREEN;
            break;
            case GREEN:
            target = ColorWheelColor.YELLOW;
            break;
            case NONE:
            target = ColorWheelColor.NONE;
            default:
            break;
        }
    }

    @Override
    protected void execute() {
        if (target != controlPanelWheel.findColor()) controlPanelWheel.setMotor(motorSpeed);
        else timeSpentOnColor += 20;
    }

    @Override
    protected boolean isFinished() {
        return timeSpentOnColor > 5250;
    }




}