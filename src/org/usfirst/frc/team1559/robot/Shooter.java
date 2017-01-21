package org.usfirst.frc.team1559.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Servo;

public class Shooter {
	// The Shooter is a Fly Wheel Shooter.
	// This is where most of the variables are instantiated.
	Servo ballOpener; // The instantiation of the servo. Acts as the gate.
	CANTalon shooterTalon; // The motor that is used to fire them balls.
	int i; // Used as a counter.
	int switchCaseVar; // Used with the switch case to initiate a delay in the fire rate.
	
	public void ShooterInit() { // All the variables are assigned values.
		// All the variables are defined here.
		ballOpener = new Servo(1); // Will change for actual Robot.
		shooterTalon = new CANTalon(1);// Will change for the actual Robot.
		i = 0; // To be used with the for loop to limit the ball rate.
		switchCaseVar = 0;
		
		// Initiation for the CANTalon
		shooterTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		shooterTalon.configEncoderCodesPerRev(4096);
		shooterTalon.changeControlMode(TalonControlMode.Speed);
		shooterTalon.configNominalOutputVoltage(Constants.NOMIAL_OUTPUT_VOLTAGE, Constants.NEGATIVE_PEAK_OUTPUT_VOLTAGE);
		shooterTalon.configPeakOutputVoltage(Constants.PEAK_OUTPUT_VOLTAGE, Constants.NOMIAL_OUTPUT_VOLTAGE);
		shooterTalon.setProfile(0);
		shooterTalon.setP(Constants.P); // P = 0.3
		shooterTalon.setI(Constants.I); // I = 0
		shooterTalon.setD(Constants.D); // D = 0.05
		shooterTalon.setF(Constants.F); // F = 0.32
		
	}
	public void ShooterMotor(int rpm) { // Starts up the motor so that them balls can be fired.
		shooterTalon.set(rpm*Constants.RPM_CONVERSION); // Motor is started up.
	}
	public void Fire() { // Used to control the fire rate of the balls
		switch(switchCaseVar){
		case 0: // If switchCaseVar = 0, then this line will execute.
			ballOpener.setAngle(Constants.OPEN_VAL); // The gate is set to open.
			i++; // 1 is added to 'i'.
			if(i >= Constants.CLOSE_DELAY*50){ // CLOSE_DELAY = (1.0/8)*50
				switchCaseVar++; // switchCaseVar is updated by 1.
				i = 0; // 'i' is reset to 0.
			}
		break; // end line of code
		case 1: // If switchCaseVar = 1, then this line will execute.
			ballOpener.setAngle(Constants.CLOSE_VAL); // CLOSE_VAL = 0
			i++; // 1 is added to 'i'.
			if(i >= Constants.FIRE_DELAY*50){ // FIRE_DELAY = 1.0/3
				switchCaseVar++; // switchCaseVar is updated by 1.
				i = 0; // counter 'i' is set to 0. (reset)
			}
		break; // end line of code
		default: // If the other cases don't work come down here.
		switchCaseVar = 0;// We screwed up real bad bois.
		break; // end line of code
		}
	}
	public void clearCounter() { // Used to reset the counter 'i'.
		i = 0; // counter 'i' is reset
	}
	
	
}