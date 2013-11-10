import java.io.*;
import java.util.*;

public class Onion
{
	private static String[] serverNames = {"ServerA", "ServerB", "ServerC"};
	//input server locations & keys when we determine which ones will be able to run sendgrid
	private static String[] serverAddresses = {"A", "B", "C"};
	private static String[] serverKeys = {"C2D8F70A", "100CC8E3", "B7247D65"};

	public static void makeOnion (String fromFile, String toFile, String userEmail, String userKey,  String[] path, double[] timeSteps)
	{
		int layers = path.length;
		String olderOnion = fromFile;
		String newerOnion = toFile;
		int indexOfServer = -1;

		//first case of having to encrypt the message w/ users key
		encryptDecrypt.encrypt(olderOnion, userKey, newerOnion);
		String delay = "Delay: " + timeSteps[timeSteps.length - 1];
		String destination = "Email: " + userEmail;


		//encrypt the onion moving backwards. wedon't use element 0 since it's not involved in sending things through the network.
		for(int i = layers - 2; i > 0; i--)
		{
			//NEED TO FIGURE OUT WAY OF GOING BETWEEN ONION FILES AND ENCRYPTING IT MORE AND MORE
			for(int j = 0; j < serverNames.length; j++)
			{
				if(serverNames[j].equals(path[i])) indexOfServer = j;
				System.out.println(serverNames[j] + " " + path[i]); 
			}

			encryptDecrypt.encrypt(olderOnion, serverKeys[indexOfServer], newerOnion);

			destination = "Email: " + serverAddresses[indexOfServer];
			delay = "Delay: " + timeSteps[indexOfServer]; 

			try
			{
				BufferedReader f = new BufferedReader(new FileReader(newerOnion));
        		// Use StringTokenizer vs. readLine/split -- lots faster
       			StringTokenizer tokenizer = new StringTokenizer(f.readLine());

       			String ciphertext = "";

       			while(tokenizer.hasMoreTokens())
       				ciphertext += tokenizer.nextToken();

       			String message = destination + " " + delay + " " + ciphertext;

				FileWriter fw = new FileWriter(olderOnion);
			}

			catch (IOException e)
      		{
        		System.out.println (e.getMessage());
      		}
		}
	}

	public static void main(String[] args)
	{
		String email = "wcdavis@princeton.edu";
		String publicKey = "81F3E938";
		int steps = 3;
		String fromFile = "fileA.txt";
		String toFile = "fileB.txt";
		double time = 100000;
		String[] path = RandomCircuit.getPath(steps, email);
		double[] times = RandomCircuit.getTimeWeights(time, steps);

		makeOnion(fromFile, toFile, email, publicKey, path, times);
		//for(int i = 0; i < steps; i++)
		//	System.out.println(path[i]);
	}

}