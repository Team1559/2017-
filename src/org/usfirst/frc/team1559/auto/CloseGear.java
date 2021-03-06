package org.usfirst.frc.team1559.auto;

import org.usfirst.frc.team1559.robot.GearPlacer;

public class CloseGear extends AutoCommand {

	@Override
	public void init() {
		GearPlacer.getInstance().set(false);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void done() {
		
	}

	@Override
	public boolean isFinished() {
		return !GearPlacer.getInstance().isPlacerOpen();
	}

}
