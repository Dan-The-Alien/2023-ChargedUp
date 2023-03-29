// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import java.util.Map;

public class Dashboard extends SubsystemBase {
  /** Creates a new Dashboard. */
  public Dashboard() {}

  public void initialize() {
    // Create shuffleboard tabs
    ShuffleboardTab bootTab = Shuffleboard.getTab("Boot-Up");
    ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");
    ShuffleboardTab teleopTab = Shuffleboard.getTab("Teleop");
    ShuffleboardTab postTab = Shuffleboard.getTab("Post Game");
    ShuffleboardTab tuneTab = Shuffleboard.getTab("Tune");
    ShuffleboardTab debugTab = Shuffleboard.getTab("Debug");

    /* CREATE BOOT TAB */
    bootTab
        .add("FMS Connected", false)
        .withWidget(BuiltInWidgets.kBooleanBox)
        .withSize(1, 1)
        .withPosition(0, 2);

    // Alliance color display (red when true)
    bootTab
        .add("Alliance", false)
        .withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(
            Map.of(
                "Color when false",
                Constants.ColorConstants.kAllianceBlue,
                "Color when true",
                Constants.ColorConstants.kAllianceRed))
        // .withSize(1, 1)
        .withPosition(2, 0);

    // Drive station number
    bootTab
        .add("Station", 0)
        .withWidget(BuiltInWidgets.kTextView)
        .withSize(1, 1)
        .withPosition(3, 0);

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
  }
}
