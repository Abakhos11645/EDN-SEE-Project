import java.io.File;
import java.util.Scanner;

import core.ERRVFederate;
import core.ERRVFederateAmbassador;
import skf.config.ConfigurationFactory;
import model.ERRV;
import model.Position;

public class Main {

	private static final File conf = new File("LunarRover/config/conf.json");

	public static void main(String[] args) throws Exception {

		ERRV lunarRover = new ERRV("ERRV", "MoonCentricFixed",
				new Position(100, 200, 300), 100, new model.Arm("robotic", 50));
		lunarRover.setAvailability(true);

		ERRVFederateAmbassador ambassador = new ERRVFederateAmbassador();
		ERRVFederate federate = new ERRVFederate(ambassador, lunarRover);

		// start execution
		federate.configureAndStart(new ConfigurationFactory().importConfiguration(conf));
		Thread.sleep(1000);

		System.out.println("******************************************");
		System.out.println("******************************************");
		System.out.println("******************************************");
		System.out.println("*                                        *");
		System.out.println("*   EEEEEEE   DDDDDDD   NNN     NN       *");
		System.out.println("*   EE        DD    DD  NNNN    NN       *");
		System.out.println("*   EEEEE     DD    DD  NN NN   NN       *");
		System.out.println("*   EE        DD    DD  NN  NN  NN       *");
		System.out.println("*   EEEEEEE   DDDDDDD   NN    NNNN       *");
		System.out.println("*                                        *");
		System.out.println("*            E.R.R.V.                    *");
		System.out.println("******************************************");
		System.out.println("******************************************");
		System.out.println("******************************************");

		System.out.println("Enter a value : ");
		System.out.println(" - a : To see the position");

		Scanner sc = new Scanner(System.in);
		String currValue = null;

		while (true) {

			currValue = sc.nextLine();
			System.out.println("Value of currValue : " + currValue);
			if (currValue.equals("a")) {
				federate.getPosition();
			}
		}
	}

}
