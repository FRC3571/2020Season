package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.util.Loggable;
import frc.robot.util.Refreshable;

public class ControlPanelWheel extends Subsystem implements Refreshable, Loggable {
    private static final int motorID = 8; // i just put a random number for now

    public enum ColorWheelColor {
        RED, YELLOW, GREEN, BLUE, NONE
    }

    private VictorSPX motor;
    private final I2C.Port i2cPort;
    private final ColorSensorV3 m_colorSensor;
    private final ColorMatch m_colorMatcher;
    private final Color kBlue, kGreen, kRed, kYellow;
    private String gameData;
    private ColorWheelColor colorAssignment;

    public ControlPanelWheel(){
        i2cPort = I2C.Port.kOnboard;
        m_colorSensor = new ColorSensorV3(i2cPort);
        m_colorMatcher = new ColorMatch();

        kBlue = ColorMatch.makeColor(0.148, 0.436, 0.420);
        kGreen = ColorMatch.makeColor(0.202, 0.547, 0.250);
        kRed = ColorMatch.makeColor(0.450, 0.385, 0.162);
        kYellow = ColorMatch.makeColor(0.323, 0.540, 0.138);

        m_colorMatcher.addColorMatch(kBlue);
        m_colorMatcher.addColorMatch(kGreen);
        m_colorMatcher.addColorMatch(kRed);
        m_colorMatcher.addColorMatch(kYellow);

        motor = new VictorSPX(motorID);
    }

    public ColorWheelColor findColor() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        ColorWheelColor result = ColorWheelColor.NONE;

        if (match.color == kBlue) {
            result = ColorWheelColor.BLUE;
        } else if (match.color == kRed) {
            result = ColorWheelColor.RED;
        } else if (match.color == kGreen) {
            result = ColorWheelColor.GREEN;
        } else if (match.color == kYellow) {
            result = ColorWheelColor.YELLOW;
        }

        SmartDashboard.putString("Detected Color", result.toString());
        return result;
    }

    private void detectColorAssignment() {
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0) {
            switch (gameData.charAt(0)) {
            case 'B':
            colorAssignment = ColorWheelColor.BLUE;
                break;
            case 'G':
                colorAssignment = ColorWheelColor.GREEN;
                break;
            case 'R':
                colorAssignment = ColorWheelColor.RED;
                break;
            case 'Y':
                colorAssignment = ColorWheelColor.YELLOW;
                break;
            default:
                // This is corrupt data
                colorAssignment = ColorWheelColor.NONE;
                break;
            }
        }
    }

    public void setMotor(double speed){
        motor.set(ControlMode.PercentOutput, speed);
    }


    @Override
    public void refresh() {
        detectColorAssignment();
    }

    @Override
    public void log() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub

    }

    public ColorWheelColor getColorAssignment(){
        return colorAssignment;
    }

}