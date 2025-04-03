import java.io.File;
import java.util.Scanner;

import core.LunarRoverFederate;
import core.LunarRoverFederateAmbassador;
import skf.config.ConfigurationFactory;
import model.Taps;
import model.Position;


public class TapsMain {

	private static final File conf = new File("config/conf.json");

	public static void main(String[] args) throws Exception {

		Taps lunarRover = new Taps("Taps", "MoonCentricFixed",
				new Position (100, 200, 300));

		LunarRoverFederateAmbassador ambassador = new LunarRoverFederateAmbassador();
		LunarRoverFederate federate = new LunarRoverFederate(ambassador, lunarRover);

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
		System.out.println("*            T.A.P.S.                    *");
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
