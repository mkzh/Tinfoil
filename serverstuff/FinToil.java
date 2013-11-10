import java.io.*;
import java.util.*;
import java.net.*;

public class FinToil
{
	private static String encryptCommandsA    = "gpg --encrypt --batch -r";
	private static String encryptCommandsB    = "--output";
	private static String decryptCommandsA    = "gpg --passphrase";
	private static String decryptCommandsB    = "--batch -d --output";
	private static String[] serverNames       = {"ServerA", "ServerB", "ServerC"};
	private static String[] serverAddresses   = {"A", "B", "C"};
	private static String[] serverKeys        = {"C2D8F70A"} //, "100CC8E3", "B7247D65"};
	private static String[] serverPassphrases = {"MikeZhang2017"} //, "1234", "1234"};

	//command line arguments 
	// args[0] = name of ciphertext file going in.
	// args[1] = name of the encrypted file going out.
	public static void main(String[] args) throws Exception
	{
		String email = "ERROR";
		double delay = -1;
		String publicKey = "ERROR";
		String url = "ERROR";

		String ciphertext = args[0];
		String plainFile = "plaintext.txt";

		//decrypt ciphertext into newFile
		decrypt(ciphertext, serverPassphrases[0], plainFile);

		String plaintext = "";

		try
		{
			//read newFile and turn it into a string
			BufferedReader br = new BufferedReader(new FileReader(plainFile));
			StringTokenizer tokenizer = new StringTokenizer(br.readLine());

	       	while(tokenizer.hasMoreTokens())
	       	{
	       		String x = tokenizer.nextToken();
	      		plaintext = plaintext + " " + x;
	      	}

	      	email = getAddress(plaintext);
	      	delay = getDelay(plaintext);
	      	publicKey = getKey(plaintext);
	      	url = getSite(plaintext);

	    }

	    catch (IOException e)
      	{
       		System.out.println (e.getMessage());
   		}

   		String onionFile = args[1];
   		
   		int steps = 2; 

   		double[] timeWeights = getTimeWeights(delay, steps);

   		double timeToFetch = timeWeights[0];
   		double timeToSend  = timeWeights[1];

   		for(int i = 0; i < 1e9 * timeToFetch; i++);

   		String html = "";
   		try
   		{
   			html = request(url);
   		}

   		catch (IOException e)
   		{
   			System.out.println (e.getMessage());
   		}

   	   	for(int i = 0; i < 1e9 * timeToSend; i++);

   		encrypt(html, publicKey, "encryptedFile.txt");

	}

	public static String request(String url) throws Exception {
        URL site = new URL(url);
        URLConnection connect = site.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connect.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) 
            sb.append(inputLine);
        return sb.toString();
    }


	//make an onion file with the filename and extension found in toFile
	// @String fromFile: file we're encrypting
	// @String toFile: file we're 
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
       			System.out.println("Message: " + message);

				FileWriter fw = new FileWriter(olderOnion);

				//POSSIBLE SOURCE OF ERROR
				fw.write(message);
			}

			catch (IOException e)
      		{
        		System.out.println (e.getMessage());
      		}
		}
	}

	public static double[] getTimeWeights(double time, int steps)
	{
		double weight = 1.0;
		double[] timeSteps = new double[steps];

		for(int i = 0; i < steps; i++)
		{
			double random = Math.random();

			//if you're on the last step...
			if(i == steps - 1) timeSteps[i] = weight;

			//generate the random values
			else if(random < 1.0 / 2 && (weight - random) > 0)
			{
				weight -= random;
				timeSteps[i] = random;
			}
			else
			{
				random = Math.random() * 0.1;
				weight -= random;
				timeSteps[i] = random;
			}
		}

		//if we get some negative value
		for(int i = 0; i < steps; i++)
		{
			if(timeSteps[i] < 0) return getTimeWeights(time, steps);
		}

		for(int i = 0; i < steps; i++)
		{

			timeSteps[i] *= time;
		}

		return timeSteps;
	}

	public static String[] getPath(int steps, String userEmail)
	{
		String[] path = new String[steps];
		path[0] = "Fetch";

		for(int i = 1; i < steps - 1; i++)
		{
			int destination = (int) (Math.random() * 3.0);
			//System.out.println("destination: " + destination);

			if(destination == 0) path[i] = serverNames[0];
			else if(destination == 1) path[i] = serverNames[1];
			else if(destination == 2) path[i] = serverNames[2];
		}

		path[steps - 1] = userEmail;

		return path;
	}

	//encrypts the given file and saves it to the newFileName.
	//return true if it works, and false if it fails. You can 
	//access the created file later.
	public static void encrypt(String plaintext, String publicKey, String newFileName)
	{
		Process		p;
		String		fullCommand = encryptCommandsA + " " + publicKey + " " + encryptCommandsB 
								+ " " + newFileName + " " + plaintext;
		
		System.out.println (fullCommand);
		
		//encrypts the given file and saves it to the newFileName
		try
		{
      System.out.println(fullCommand);
			p = Runtime.getRuntime().exec(fullCommand);
		}
		
		catch (IOException e)
      	{
         	System.out.println (e.getMessage());
      	}
	}

	//decrypt ciphertext into a new file
	public static void decrypt(String ciphertext, String passPhrase, String newFileName)
	{
		Process		p;
		String		fullCommand = decryptCommandsA + " " + passPhrase + " " + decryptCommandsB + " " + newFileName + " " + ciphertext;
		
		System.out.println (fullCommand);
		
		//encrypts the given file and saves it to the newFileName
		try
		{
			for(int i = 0; i < 5e8; i++);
			p = Runtime.getRuntime().exec(fullCommand);
		}
		catch (IOException e)
      	{
        	System.out.println (e.getMessage());
      	}
	}

	//parse the message we decrypted for the destination IP
   public static String getAddress(String message)
   {

      String str = message;
      String address = "";
      boolean foundDestination = false;
      
      try{
         // convert String into InputStream
         InputStream is = new ByteArrayInputStream(str.getBytes());
         // read it with BufferedReader into a string tokenizer
         BufferedReader inFromUser = new BufferedReader(new InputStreamReader(is));
         StringTokenizer tokenizer = new StringTokenizer(inFromUser.readLine());

         //while there's more to read
         while(tokenizer.hasMoreTokens() && !foundDestination)
         {
            //if we find the tag for Destination IP
            if(tokenizer.nextToken().contains("Email:"))
            {
               address = tokenizer.nextToken();
               foundDestination = true; 
            }
         }
      }

      catch (IOException e)
      {
         System.out.println (e.getMessage());
      }

      return address;
   }

   //parse the message we decrypted for the delay we hold the message
   //look for "Delay:"
   public static double getDelay(String message)
   {

      String str = message;
      double delay = -1;
      boolean foundDelay = false;
   
      try{ 
        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(str.getBytes());
        // read it with BufferedReader into a tokenizer
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(is));
        StringTokenizer tokenizer = new StringTokenizer(inFromUser.readLine());



        //while there's more to read
        while(tokenizer.hasMoreTokens() && !foundDelay)
        {
            //if we find the tag for Delay
            if(tokenizer.nextToken().contains("Delay:"))
            {
               delay = Double.parseDouble(tokenizer.nextToken());
               foundDelay = true; 
            }
         }
      }

      catch (IOException e)
      {
         System.out.println (e.getMessage());
      }

      return delay;
   }

   //parse the message we decrypted for the site to scrape
   //Looking for "Site:"
   public static String getSite(String message)
   {

      String str = message;
      String site = "";
      boolean foundSite = false;
      
      try{
         // convert String into InputStream
         InputStream is = new ByteArrayInputStream(str.getBytes());
         // read it with BufferedReader into a string tokenizer
         BufferedReader inFromUser = new BufferedReader(new InputStreamReader(is));
         StringTokenizer tokenizer = new StringTokenizer(inFromUser.readLine());

         //while there's more to read
         while(tokenizer.hasMoreTokens() && !foundSite)
         {
            //if we find the tag for Destination IP
            if(tokenizer.nextToken().contains("Site:"))
            {
               site = tokenizer.nextToken();
               foundSite = true; 
            }
         }
      }

      catch (IOException e)
      {
         System.out.println (e.getMessage());
      }

      return site;
   }

   //parse the message we decrypted for the destination IP
   //Looking for "Key:"
   public static String getKey(String message)
   {

      String str = message;
      String key = "";
      boolean foundKey = false;
      
      try{
         // convert String into InputStream
         InputStream is = new ByteArrayInputStream(str.getBytes());
         // read it with BufferedReader into a string tokenizer
         BufferedReader inFromUser = new BufferedReader(new InputStreamReader(is));
         StringTokenizer tokenizer = new StringTokenizer(inFromUser.readLine());

         //while there's more to read
         while(tokenizer.hasMoreTokens() && !foundKey)
         {
            //if we find the tag for Destination IP
            if(tokenizer.nextToken().contains("Key:"))
            {
               key = tokenizer.nextToken();
               foundKey = true; 
            }
         }
      }

      catch (IOException e)
      {
         System.out.println (e.getMessage());
      }

      return key;
   }
}