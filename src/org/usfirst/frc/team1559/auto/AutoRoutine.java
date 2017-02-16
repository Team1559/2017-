package org.usfirst.frc.team1559.auto;

import java.util.Vector;

public class AutoRoutine {

	Vector<AutoCommand> actions;

	int currentCommand;

	public AutoRoutine() {
		actions = new Vector<AutoCommand>();
		currentCommand = 0;
	}
	
	public void reset() {
		currentCommand = 0;
	}

	public void put(AutoCommand auto) {
		actions.add(auto);
	}

	public void run() {
		if (currentCommand >= actions.size())
			return;
		if (!actions.get(currentCommand).isInitialized()) {
			actions.get(currentCommand).initialize();
		}
		if (!actions.get(currentCommand).isFinished()) {
			actions.get(currentCommand).update();
		} else {
			actions.get(currentCommand).done();
			currentCommand++;
		}
	}

}
