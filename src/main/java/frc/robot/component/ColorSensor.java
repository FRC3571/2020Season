package frc.robot.component;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;


import edu.wpi.first.wpilibj.I2C;

public class ColorSensor{

    private final I2C.Port i2cPort;
    private final ColorSensorV3 m_colorSensor;
    private final ColorMatch m_colorMatcher;

    public ColorSensor(){
        i2cPort = I2C.Port.kOnboard;
        m_colorSensor = new ColorSensorV3(i2cPort);
        m_colorMatcher = new ColorMatch();

    }

    public Color detectedColor(){
        return m_colorSensor.getColor();
    }
    

}