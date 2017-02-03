package org.usfirst.frc.team2175.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class SwitchCameraCommand extends Command {
    NetworkTable switcher = NetworkTable.getTable("GRIP/switch");

    @Override
    protected void initialize() {
        switcher.putBoolean("switch", true);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }
}
