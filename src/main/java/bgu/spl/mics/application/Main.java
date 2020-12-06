package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.passiveObjects.JsonInputReader;

import bgu.spl.mics.application.services.LeiaMicroservice;
import com.google.gson.Gson;


import java.awt.font.TextHitInfo;
import java.io.IOException;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		//reading Input
	try{
		String input = args[1];
		String output = args[2];

		Input json = JsonInputReader.getInputFromJson(input);
		System.out.println(json);

		Ewoks ewoks = new Ewoks(json.getEwoks());

	} catch (IOException e) {
		e.printStackTrace();
	}

		//	Diary diary = new Diary();
		//Thread Leia = new Thread(LeiaMicroservice);
		//Thread C3PO = new Thread(C3POMicroservice);
		//Thread R2D2 = new Thread(R2D2Microservice);
		//Thread HanSolo = new Thread(HanSoloMicroservice);
		//Thread Lando = new Thread(LandoMicroservice);



	}
}
