import java.util.*;
import java.lang.*;

public class RandomCircuit
{
	private static String[] serverKeys = {"C2D8F70A", "100CC8E3", "B7247D65"};
	private static String[] serverNames = {"ServerA", "ServerB", "ServerC"};

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

		//if we get some negative 
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

	public static void main(String[] args)
	{
		int steps = 6;
		double time = 10000;
		String email = "walker@davis";

		double[] timeSteps = getTimeWeights(time, steps);
		String[] pathSteps = getPath(steps, email);
		String plaintext = "MikeZhangIsTheGreatest.txt";

		for(int i = 0; i < steps; i++)
		{
			System.out.println("Step #" + i + ": " + pathSteps[i] + "   " + timeSteps[i] + " ");
		}


	}
}