package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.*;

import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;


import java.awt.*;
import java.awt.font.TextHitInfo;
import java.io.IOException;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws IOException {
		//reading Input

		String input = args[1];
		String output = args[2];

		Input json = JsonInputReader.getInputFromJson(input);
		System.out.println(json);

		MessageBusImpl messageBus = MessageBusImpl.getInstance();
		Ewoks ewoks = Ewoks.getInstance();
		ewoks.setEwoks(json.getEwoks());
		Diary diary = Diary.getInstance();

		LeiaMicroservice leia = new LeiaMicroservice((AttackEvent[]) json.getAttacks());//check casting
		Thread leiaT = new Thread(leia);
		HanSoloMicroservice hanSolo = new HanSoloMicroservice();
		Thread hanSoloT = new Thread(hanSolo);
		C3POMicroservice c3po = new C3POMicroservice();
		Thread c3poT = new Thread(c3po);
		R2D2Microservice r2d2 = new R2D2Microservice(json.getR2D2());
		Thread r2d2T = new Thread(r2d2);
		LandoMicroservice lando = new LandoMicroservice(json.getLando());
		Thread landoT = new Thread(lando);




		//	Diary diary = new Diary();
		//Thread Leia = new Thread(LeiaMicroservice);
		//Thread C3PO = new Thread(C3POMicroservice);
		//Thread R2D2 = new Thread(R2D2Microservice);
		//Thread HanSolo = new Thread(HanSoloMicroservice);
		//Thread Lando = new Thread(LandoMicroservice);



	}
}
