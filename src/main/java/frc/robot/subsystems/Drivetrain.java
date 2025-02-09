package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVLibError;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DrivetrainConstants;

public class Drivetrain extends SubsystemBase {

  private final CANSparkMax m_frontLeftMotor =
      new CANSparkMax(DrivetrainConstants.FRONT_LEFT_MOTOR_ID, MotorType.kBrushless);
  private final CANSparkMax m_backLeftMotor =
      new CANSparkMax(DrivetrainConstants.BACK_LEFT_MOTOR_ID, MotorType.kBrushless);
  private final CANSparkMax m_frontRightMotor =
      new CANSparkMax(DrivetrainConstants.FRONT_RIGHT_MOTOR_ID, MotorType.kBrushless);
  private final CANSparkMax m_backRightMotor =
      new CANSparkMax(DrivetrainConstants.BACK_RIGHT_MOTOR_ID, MotorType.kBrushless);

  private final MotorControllerGroup m_leftControllerGroup =
      new MotorControllerGroup(m_frontLeftMotor, m_backLeftMotor);
  private final MotorControllerGroup m_rightControllerGroup =
      new MotorControllerGroup(m_frontRightMotor, m_backRightMotor);

  private final DifferentialDrive m_differentialDrive =
      new DifferentialDrive(m_leftControllerGroup, m_rightControllerGroup);

  private DataLog m_log = DataLogManager.getLog();
  private DoubleLogEntry l_frontLeftMotorCurrent =
      new DoubleLogEntry(m_log, "/custom/drivetrain/frontleft/current");
  private DoubleLogEntry l_frontLeftMotorTemp =
      new DoubleLogEntry(m_log, "/custom/drivetrain/frontleft/temp");
  private DoubleLogEntry l_backLeftMotorCurrent =
      new DoubleLogEntry(m_log, "/custom/drivetrain/backleft/current");
  private DoubleLogEntry l_backLeftMotorTemp =
      new DoubleLogEntry(m_log, "/custom/drivetrain/backleft/temp");
  private DoubleLogEntry l_frontrightMotorCurrent =
      new DoubleLogEntry(m_log, "/custom/drivetrain/frontright/current");
  private DoubleLogEntry l_frontrightMotorTemp =
      new DoubleLogEntry(m_log, "/custom/drivetrain/frontright/temp");
  private DoubleLogEntry l_backrightMotorCurrent =
      new DoubleLogEntry(m_log, "/custom/drivetrain/backright/current");
  private DoubleLogEntry l_backrightMotorTemp =
      new DoubleLogEntry(m_log, "/custom/drivetrain/backright/temp");

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    configureMotors();
  }

  private void configureMotors() {
    // Reset all the motors to factory defaults (except CAN ID)
    m_frontLeftMotor.restoreFactoryDefaults();
    m_backLeftMotor.restoreFactoryDefaults();
    m_frontRightMotor.restoreFactoryDefaults();
    m_backRightMotor.restoreFactoryDefaults();

    // Set all the motor's idle modes to Coast
    if (m_frontLeftMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk) {
      System.out.println("ERROR while setting Front Left Motor to Coast Mode");
    }
    if (m_backLeftMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk) {
      System.out.println("ERROR while setting Back Left Motor to Coast Mode");
    }
    if (m_frontRightMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk) {
      System.out.println("ERROR while setting Front Right Motor to Coast Mode");
    }
    if (m_backRightMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk) {
      System.out.println("ERROR while setting Back Right Motor to Coast Mode");
    }

    // Set current limits for all motors
    if (m_frontLeftMotor.setSmartCurrentLimit(DrivetrainConstants.MOTOR_CURRENT_LIMIT)
        != REVLibError.kOk) {
      System.out.println("ERROR while setting Front Left Motor smart current limit");
    }
    if (m_backLeftMotor.setSmartCurrentLimit(DrivetrainConstants.MOTOR_CURRENT_LIMIT)
        != REVLibError.kOk) {
      System.out.println("ERROR while setting Back Left Motor smart current limit");
    }
    if (m_frontRightMotor.setSmartCurrentLimit(DrivetrainConstants.MOTOR_CURRENT_LIMIT)
        != REVLibError.kOk) {
      System.out.println("ERROR while setting Front Right Motor smart current limit");
    }
    if (m_backRightMotor.setSmartCurrentLimit(DrivetrainConstants.MOTOR_CURRENT_LIMIT)
        != REVLibError.kOk) {
      System.out.println("ERROR while setting Back Right Motor smart current limit");
    }

    // Burn all changed settings to flash
    m_frontLeftMotor.burnFlash();
    m_backLeftMotor.burnFlash();
    m_frontRightMotor.burnFlash();
    m_backRightMotor.burnFlash();
  }

  public void arcadeDrive(double speed, double rotation) {
    m_leftControllerGroup.setInverted(true);
    m_differentialDrive.arcadeDrive(speed, rotation);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logDrivetrainMotors();
  }

  private void logDrivetrainMotors() {
    l_frontLeftMotorCurrent.append(m_frontLeftMotor.getOutputCurrent());
    l_frontLeftMotorTemp.append(m_frontLeftMotor.getMotorTemperature());
    l_backLeftMotorCurrent.append(m_backLeftMotor.getOutputCurrent());
    l_backLeftMotorTemp.append(m_backLeftMotor.getMotorTemperature());
    l_frontrightMotorCurrent.append(m_frontRightMotor.getOutputCurrent());
    l_frontrightMotorTemp.append(m_frontRightMotor.getMotorTemperature());
    l_backrightMotorCurrent.append(m_backRightMotor.getOutputCurrent());
    l_backrightMotorTemp.append(m_backRightMotor.getMotorTemperature());
  }
}
