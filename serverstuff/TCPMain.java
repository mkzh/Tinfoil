import java.io.*;
import java.net.*;
import java.util.*;

public class TCPMain
{
   private final String kGnuPGCommand = "/usr/local/bin/gpg --batch --armor --output -";

   public static void main(String argv[]) throws Exception
   {
      String clientSentence;
      String capitalizedSentence;
      ServerSocket welcomeSocket = new ServerSocket(6789);

      while(true)
      {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient =
               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);

            //capitalizedSentence = clientSentence.toUpperCase() + '\n';
            //outToClient.writeBytes(capitalizedSentence);

            System.out.println("Client Sentence:" + clientSentence);
            System.out.println("Delay: " + getDelay(clientSentence));
            System.out.println("Destination: " + getAddress(clientSentence));

            outToClient.writeDouble(getDelay(clientSentence));
            outToClient.writeBytes(getAddress(clientSentence));
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

   //scrape site by activating the python scrapy program
   //CHANGE COMMAND ONCE MOUSE GETS HERE
   private String scrapeSite(String site)
   {
      String source = "";
      String scrapyCommand = "python scrapy ";

      GnuPG pgp = new GnuPG();

      if(pgp.runGnuPG(scrapyCommand + site, ""))
      {
         source = pgp.getResult();
      }

      return source;
   }
}
