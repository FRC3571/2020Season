package frc.robot.component;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ColorSensor {

    private final I2C.Port i2cPort;
    private final ColorSensorV3 m_colorSensor;
    private final ColorMatch m_colorMatcher;
    private final Color kBlue;
    private final Color kGreen;
    private final Color kRed;
    private final Color kYellow;

    public ColorSensor() {
        i2cPort = I2C.Port.kOnboard;
        m_colorSensor = new ColorSensorV3(i2cPort);
        m_colorMatcher = new ColorMatch();

        kBlue = ColorMatch.makeColor(0.143, 0.427, 0.429);
        kGreen = ColorMatch.makeColor(0.197, 0.561, 0.240);
        kRed = ColorMatch.makeColor(0.561, 0.232, 0.114);
        kYellow = ColorMatch.makeColor(0.361, 0.524, 0.113);

        m_colorMatcher.addColorMatch(kBlue);
        m_colorMatcher.addColorMatch(kGreen);
        m_colorMatcher.addColorMatch(kRed);
        m_colorMatcher.addColorMatch(kYellow);
    }

    public void matchedColor() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        String colorString;

        if (match.color == kBlue) {
            colorString = "Blue";
        } else if (match.color == kRed) {
            colorString = "Red";
        } else if (match.color == kGreen) {
            colorString = "Green";
        } else if (match.color == kYellow) {
            colorString = "Yellow";
        } else {
            colorString = "Unknown";
        }

        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putString("Detected Color", colorString);
    }

}