package frc.robot.component;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NAVX {
    private AHRS ahrs;

    public NAVX() {
        try {
            ahrs = new AHRS(SPI.Port.kMXP); 
        } catch (final RuntimeException ex) {
            DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
        }

    }

    public void log() {
        SmartDashboard.putNumber("DriveTrain/Position/Yaw", ahrs.getYaw());
    }

    public AHRS getAHRS(){
        return ahrs;
    }
}