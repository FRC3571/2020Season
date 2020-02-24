package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystem.Shooter;
import frc.robot.util.XboxController;

public class ManualShoot extends Command {

    private Shooter shooter;
    private XboxController controller;
    private double topSpeed, bottomSpeed;

    public ManualShoot() {
        this.shooter = Robot.getInstance().getShooter();
        this.controller = Robot.getInstance().getSubsystemController();
        setInterruptible(true);
        requires(shooter);
    }

    @Override
    protected void execute() {
        topSpeed = controller.LeftStick.Y;
        bottomSpeed = controller.RightStick.Y;
        shooter.setMotors(topSpeed, bottomSpeed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        shooter.setMotors(0, 0);
    }
}