package org.usfirst.frc.team1559.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;

public class DriveTrain {
	
	private static DriveTrain instance;

	public static DriveTrain getInstance() {
		if (instance == null) {
			instance = new DriveTrain();
		}
		return instance;
	}

	private CANTalon fl, fr, rl, rr;
	private Solenoid drop;
	private RobotDrive drive;
	private AnalogGyro g;
	private boolean mecanumized;
	private double maxSpeed;
	double gAngle = 0.0;
	double gyroAngle;

	public DriveTrain() {
		fl = new CANTalon(Wiring.FL_SRX); //Front left talon
		fr = new CANTalon(Wiring.FR_SRX); //Front right talon
		rl = new CANTalon(Wiring.RL_SRX); //Rear left talon
		rr = new CANTalon(Wiring.RR_SRX); //Rear right talon
		drop = new Solenoid(Wiring.DROPPER); // The solenoid that drops the mecanum wheels or brings them up
		maxSpeed = Constants.MAX_DRIVE_SPEED;
		g = new AnalogGyro(1); // GET THE ID
		
		//Front left talon config
		fl.configNominalOutputVoltage(Constants.NOMIAL_OUTPUT_VOLTAGE, Constants.NEGATIVE_NOMIAL_OUTPUT_VOLTAGE); //Configures the nomial output voltages
		fl.configPeakOutputVoltage(Constants.PEAK_OUTPUT_VOLTAGE, Constants.NEGATIVE_PEAK_OUTPUT_VOLTAGE); //Configures the peak output voltages
		fl.setProfile(Constants.PROFILE); //Configures the profile of the talon
		fl.setP(Constants.P); //Configures the proportional of the talon
		fl.setI(Constants.I); //Configures the integral of the talon
		fl.setD(Constants.D); //Configures the derivative of the talon
		fl.setF(Constants.F); //Configures the feed-forward of the talon
		
		//Repeat for the 3 other talons
		
		//Front right talon config
		fr.configNominalOutputVoltage(Constants.NOMIAL_OUTPUT_VOLTAGE, Constants.NEGATIVE_NOMIAL_OUTPUT_VOLTAGE);
		fr.configPeakOutputVoltage(Constants.PEAK_OUTPUT_VOLTAGE, Constants.NEGATIVE_PEAK_OUTPUT_VOLTAGE);
		fr.setProfile(Constants.PROFILE);
		fr.setP(Constants.P);
		fr.setI(Constants.I);
		fr.setD(Constants.D);
		fl.setF(Constants.F);
		
		//Rear left talon config
		rl.configNominalOutputVoltage(Constants.NOMIAL_OUTPUT_VOLTAGE, Constants.NEGATIVE_NOMIAL_OUTPUT_VOLTAGE);
		rl.configPeakOutputVoltage(Constants.PEAK_OUTPUT_VOLTAGE, Constants.NEGATIVE_PEAK_OUTPUT_VOLTAGE);
		rl.setProfile(Constants.PROFILE);
		rl.setP(Constants.P);
		rl.setI(Constants.I);
		rl.setD(Constants.D);
		rl.setF(Constants.F);
		
		//Rear right talon config
		rr.configNominalOutputVoltage(Constants.NOMIAL_OUTPUT_VOLTAGE, Constants.NEGATIVE_NOMIAL_OUTPUT_VOLTAGE);
		rr.configPeakOutputVoltage(Constants.PEAK_OUTPUT_VOLTAGE, Constants.NEGATIVE_PEAK_OUTPUT_VOLTAGE);
		rr.setProfile(Constants.PROFILE);
		rr.setP(Constants.P);
		rr.setI(Constants.I);
		rr.setD(Constants.D);
		rr.setF(Constants.F);
		
		//Enables the PIDF loop
		fl.enable();
		fr.enable();
		rl.enable();
		rr.enable();
	}

	public void drop() {
		mecanumized = !mecanumized;
		drop.set(mecanumized);
	}

	public void driveMecanum(double x, double y, double rotation) {
		//
		gAngle = g.getAngle();

		// desiredAngle += rotation;
		//
		// if(desiredAngle > gyroAngle+1){
		// correctionAngle-=0.01;
		// } else if(desiredAngle < gyroAngle-1){
		// correctionAngle+=0.01;
		// } else {
		// correctionAngle = 0.0;
		// }
		//
		// rotation += correctionAngle;

		gyroAngle = gAngle;

		double xIn = x;
		double yIn = y;
		// Negate y for the joystick.
		yIn = -yIn;
		// Compenstate for gyro angle.
		double rotated[] = rotateVector(xIn, yIn, gyroAngle);
		xIn = rotated[0];
		yIn = rotated[1];

		double flSpeed = xIn + yIn + rotation;
		double frSpeed = -xIn + yIn - rotation;
		double rlSpeed = -xIn + yIn + rotation;
		double rrSpeed = xIn + yIn - rotation;
		double[] speeds = { flSpeed, frSpeed, rlSpeed, rrSpeed };
		normalize(speeds);

		fl.set(-speeds[0] * maxSpeed);
		fr.set(speeds[1] * maxSpeed);
		rl.set(-speeds[2] * maxSpeed);
		rr.set(speeds[3] * maxSpeed);

	}

	private static double[] rotateVector(double x, double y, double angle) {
		double cosA = Math.cos(angle * (3.14159 / 180.0));
		double sinA = Math.sin(angle * (3.14159 / 180.0));
		double out[] = new double[2];
		out[0] = x * cosA - y * sinA;
		out[1] = x * sinA + y * cosA;
		return out;
	}

	private static void normalize(double[] speeds) {
		double maxMagnitude = Math.abs(speeds[0]);
		for (int i = 1; i < speeds.length; i++) {
			double temp = Math.abs(speeds[i]);
			if (maxMagnitude < temp)
				maxMagnitude = temp;
		}
		if (maxMagnitude > 1.0) {
			for (int i = 0; i < speeds.length; i++) {
				speeds[i] = speeds[i] / maxMagnitude;
			}
		}
	}
	
	public void driveTraction(int x, int y) {
		drive.arcadeDrive(y, x);
	}
}
