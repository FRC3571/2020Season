package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystem.Shooter;
import frc.robot.util.XboxController;

public class ManualShoot extends Command {

    private Shooter shooter;
    private XboxController controller;
    private double speed;

    public ManualShoot(){
        this.shooter = Robot.getInstance().getShooter();
        this.controller = Robot.getInstance().getSubsystemController();
        this.speed = speed;
        setInterruptible(true);
        requires(shooter);
    }

    @Override
    protected void execute() {
        speed = controller.RightStick.Y;

        shooter.setMotors(speed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        shooter.setMotors(0);
    }

}