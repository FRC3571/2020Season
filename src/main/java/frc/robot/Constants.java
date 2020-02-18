package frc.robot;

public final class Constants {
    public static final class DriveConstants {
        public static final int kController = 0;

        public static final double kGearRatioLow = 4.6;
        public static final double kGearRatioHigh = 2.7;

        public static final int kLeftLeadID = 10;
        public static final int kLeftFollowID = 11;
        public static final int kRightLeadID = 20;
        public static final int kRightFollowID = 21;

        public static final double kGearRatioFirst = 0.3;
        public static final double kGearRatioSecond = 0.4;
        public static final double kGearRatioThird = 0.5;

        // Drive Modes
        public enum DriveMode {
            AONEJOY, ATWOJOY, TANK,
        }

        // Gears (Speeds)
        public enum Gear {
            FIRST, SECOND, THIRD, FOURTH,
        }
    }

    public static final class IntakeConstants {
    }

    public static final class ElevatorConstants {
        public enum ElevatorStage {
            BOTTOM,
            MIDDLE,
            TOP
        }
    }

    public static final class RobotConstants {
        public static final int kController = 1;
    }
}