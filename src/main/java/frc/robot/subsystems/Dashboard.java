// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.BooleanTopic;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.IntegerTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import java.util.Map;

public class Dashboard extends SubsystemBase {
  // Create shuffleboard tabs
  private ShuffleboardTab bootTab = Shuffleboard.getTab("Boot-Up");
  private ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");
  private ShuffleboardTab teleopTab = Shuffleboard.getTab("Teleop");
  private ShuffleboardTab postTab = Shuffleboard.getTab("Post Game");
  private ShuffleboardTab tuneTab = Shuffleboard.getTab("Tune");
  private ShuffleboardTab debugTab = Shuffleboard.getTab("Debug");

  private final EventLoop m_dashboardEventLoop = new EventLoop();

  private final NetworkTableInstance m_ntInst = NetworkTableInstance.getDefault();
  private final NetworkTable m_ntFMSInfo = m_ntInst.getTable("FMSInfo");

  private IntegerTopic m_ntFMSCtrlTopic = m_ntFMSInfo.getIntegerTopic("FMSControlData");
  private IntegerSubscriber m_ntFMSCtrlSub;

  private BooleanTopic m_ntIsRedTopic = m_ntFMSInfo.getBooleanTopic("IsRedAlliance");
  private BooleanSubscriber m_ntIsRedSub;

  private IntegerTopic m_ntStationNumTopic = m_ntFMSInfo.getIntegerTopic("StationNumber");
  private IntegerSubscriber m_ntStationNumSub;

  private void ntSubscribe() {
    m_ntFMSCtrlSub = m_ntFMSCtrlTopic.subscribe(0);
    m_ntIsRedSub = m_ntIsRedTopic.subscribe(false);
    m_ntStationNumSub = m_ntStationNumTopic.subscribe(-1);
  }

  private void createEventListeners() {
    BooleanEvent m_fmsConEvnt =
        new BooleanEvent(m_dashboardEventLoop, () -> (m_ntFMSCtrlSub.get() == 48));
    m_fmsConEvnt.rising().ifHigh(() -> System.out.println("FMS Connected"));
    m_fmsConEvnt.falling().ifHigh(() -> System.out.println("FMS Disconnected"));
  }

  private void populateStaticInfo() {}

  /** Creates a new Dashboard. */
  public Dashboard() {
    ntSubscribe();
    createEventListeners();

    /* POPULATE BOOT TAB */
    bootTab
        .add("FMS Connected", false)
        .withWidget(BuiltInWidgets.kBooleanBox)
        .withSize(1, 1)
        .withPosition(0, 0);

    // Alliance and station data
    ShuffleboardLayout fmsInfo =
        bootTab
            .getLayout("FMS Info", BuiltInLayouts.kGrid)
            .withSize(2, 1)
            .withProperties(Map.of("Number of columns", 2, "Number of rows", 1));

    fmsInfo
        .add("Alliance", false)
        .withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(
            Map.of(
                "Color when false",
                Constants.ColorConstants.kAllianceBlue,
                "Color when true",
                Constants.ColorConstants.kAllianceRed));

    // Drive station number
    fmsInfo.add("Station", 0).withWidget(BuiltInWidgets.kTextView);

    // Selected auto
    bootTab
        .add("Selected Auto", "initialization")
        .withWidget(BuiltInWidgets.kComboBoxChooser)
        .withSize(3, 1)
        .withPosition(5, 0);

    // Auto field
    bootTab
        .add("Location", new Field2d())
        .withWidget(BuiltInWidgets.kField)
        .withSize(4, 3)
        .withPosition(8, 0);

    // Robot location detected with vision
    bootTab
        .add("Vis Loc", false)
        .withWidget(BuiltInWidgets.kBooleanBox)
        .withSize(1, 1)
        .withPosition(8, 4);

    // # of April Tags detected
    bootTab
        .add("# April Tags", 0)
        .withWidget(BuiltInWidgets.kTextView)
        .withSize(1, 1)
        .withPosition(9, 4);

    // Distance between expected and actual position
    bootTab
        .add("∆ Trans", 0)
        .withWidget(BuiltInWidgets.kTextView)
        .withSize(1, 1)
        .withPosition(8, 5);

    // Distance between expected and actual position
    bootTab.add("∆ Rot", 0).withWidget(BuiltInWidgets.kTextView).withSize(1, 1).withPosition(9, 5);

    Shuffleboard.selectTab("Boot-Up");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_dashboardEventLoop.poll();
  }
}
