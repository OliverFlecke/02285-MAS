package env;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

public class ServerEnv {

    protected BufferedReader serverIn;  
	protected PrintStream	serverOut;
    
	public ServerEnv()
	{
		serverIn 	= new BufferedReader(new InputStreamReader(System.in));
		serverOut 	= new PrintStream(new FileOutputStream(FileDescriptor.out));
	}
	
	public void sendJointAction(String[] jointAction)
	{
		try 
		{
			serverOut.println(Arrays.toString(jointAction));
			serverIn.readLine(); // Necessary to wait for server
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
