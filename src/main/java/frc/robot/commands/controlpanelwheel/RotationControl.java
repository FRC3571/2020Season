package frc.robot.commands.controlpanelwheel;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystem.ControlPanelWheel;
import frc.robot.subsystem.ControlPanelWheel.ColorWheelColor;

public class RotationControl extends Command {

    private ControlPanelWheel controlPanelWheel;
    private int spinCount;
    private double motorSpeed;
    private ColorWheelColor startingColor;
    private boolean countedThisTime;

    public RotationControl(){
        this.controlPanelWheel = Robot.getInstance().getControlPanelWheel();
        requires(controlPanelWheel);
    }

    @Override
    protected void initialize() {
        startingColor = controlPanelWheel.findColor();
        motorSpeed = 1;
        countedThisTime = true;
    }

    @Override
    protected void execute() {
        controlPanelWheel.setMotor(motorSpeed);

        if (controlPanelWheel.findColor() == startingColor && countedThisTime == false){
            spinCount++;
            countedThisTime = true;
        }
        else if (controlPanelWheel.findColor() != startingColor && countedThisTime == true){
            countedThisTime = false;
        }
    }

    @Override
    protected boolean isFinished() {
        return spinCount >= 7;
    }

    @Override
    protected void end() {
        controlPanelWheel.setMotor(0);
    }





}