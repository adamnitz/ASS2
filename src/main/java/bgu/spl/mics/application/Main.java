package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.*;

import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		//reading Input

	/*	String input = args[1];
		String output = args[2];

		Input json = JsonInputReader.getInputFromJson(input);
		System.out.println(json);
*/
		final String inputPath = "./input.json";
		Gson gson = new Gson();
		Input json = gson.fromJson(new FileReader(inputPath),Input.class);

		MessageBusImpl messageBus = MessageBusImpl.getInstance();
		Ewoks ewoks = Ewoks.getInstance();
		ewoks.setEwoks(json.getEwoks());
		Diary diary = Diary.getInstance();

		LeiaMicroservice leia = new LeiaMicroservice(json.getAttacks());//check casting
		Thread leiaT = new Thread(leia);
		HanSoloMicroservice hanSolo = new HanSoloMicroservice();
		Thread hanSoloT = new Thread(hanSolo);
		C3POMicroservice c3po = new C3POMicroservice();
		Thread c3poT = new Thread(c3po);
		R2D2Microservice r2d2 = new R2D2Microservice(json.getR2D2());
		Thread r2d2T = new Thread(r2d2);
		LandoMicroservice lando = new LandoMicroservice(json.getLando());
		Thread landoT = new Thread(lando);

		leiaT.start();
		hanSoloT.start();
		c3poT.start();
		r2d2T.start();
		landoT.start();

		leiaT.join();
		hanSoloT.join();
		c3poT.join();
		r2d2T.join();
		landoT.join();

		Gson json1 = new GsonBuilder().setPrettyPrinting().create();
		FileWriter writer = new FileWriter("./output1.json1");
		json1.toJson(diary,writer);
		writer.flush();
		writer.close();



	}
}
