package frc.robot.component;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    private NetworkTableInstance inst;
    private NetworkTable table;
    private NetworkTableEntry yellowBallXEntry;
    private double yellowBallX;

    public Vision(){
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("OpenSight");
        yellowBallXEntry = table.getEntry("centerYellowBall-x");
    }

    public void refresh(){
        yellowBallX = -yellowBallXEntry.getDouble(0);
    }

    public double yellowBallxPos(){
        return yellowBallX;
    }

    public void log(){
        SmartDashboard.putNumber("Vision/YellowBall/xPos", yellowBallX);
    }
}