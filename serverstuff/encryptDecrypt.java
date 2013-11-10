import java.io.*;
import java.net.*;
import java.util.*;

public class encryptDecrypt
{
	private static String encryptCommandsA = "gpg --encrypt --batch -r";
	private static String encryptCommandsB = "--output";
	private static String decryptCommandsA = "gpg --passphrase";
	private static String decryptCommandsB = "--batch -d --output";

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
			p = Runtime.getRuntime().exec(fullCommand);
		}
		
		catch (IOException e)
      	{
         	System.out.println (e.getMessage());
      	}
	}

	public static void decrypt(String ciphertext, String passPhrase, String newFileName)
	{
		Process		p;
		String		fullCommand = decryptCommandsA + " " + passPhrase + " " + decryptCommandsB + " " + newFileName + " " + ciphertext;
		
		System.out.println (fullCommand);
		
		//encrypts the given file and saves it to the newFileName
		try
		{
			p = Runtime.getRuntime().exec(fullCommand);
		}
		catch (IOException e)
      	{
        	System.out.println (e.getMessage());
      	}
	}

	public static void main(String[] args)
	{
		String publicKeyA = "C2D8F70A";
		String publicKeyB = "100CC8E3";
		String publicKeyC = "B7247D65";

		String passPhraseA = "MikeZhang2017";
		String passPhraseB = "1234";
		String passPhraseC = "1234";

		String fileName = "MikeZhangIsTheGreatest.txt";
		String fileA = "fileA.txt";
		String fileB = "fileB.txt";
		String fileC = "fileC.txt";
		String fileD = "fileD.txt";
		String fileE = "fileE.txt";
		String fileF = "fileF.txt";

		//encrypt the given file three times
		encrypt(fileName, publicKeyA, fileA);
		for (int i = 0; i < 1e9; i++);

		encrypt(fileA, publicKeyB, fileB);
		for (int i = 0; i < 1e9; i++);

		encrypt(fileB, publicKeyC, fileC);
		for (int i = 0; i < 1e9; i++);

		decrypt(fileC, passPhraseC, fileD);
		for (int i = 0; i < 1e9; i++);
	
		decrypt(fileD, passPhraseB, fileE);
		for (int i = 0; i < 1e9; i++);

		decrypt(fileE, passPhraseA, fileF);
		for (int i = 0; i < 1e9; i++);

	}
}