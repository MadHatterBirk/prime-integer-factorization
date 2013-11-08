package de.warker.rmicluster;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.Map.Entry;

public class ServerThread/*<T>*/ extends Thread
{
	private IServerTask _ServerTask;
	private IClientTask _ClientTask;
	private IClientNode _Remote;
	
	public ServerThread(Node node, IServerTask stask, IClientTask ctask) 
		throws MalformedURLException, RemoteException, NotBoundException
	{
		_ServerTask = stask;
		_ClientTask = ctask;
		_Remote = (IClientNode)Naming.lookup(node.Host + "/" + ClientNode.RMIID);
	}
	
	@Override
	public void run()
	{
		System.out.println("Started a Thread");
		
		Vector<Object> job = null;
		int errorcount = 0;
		Vector tempresult = null;
		
		uploadAllClasses();
		
		while( (job = _ServerTask.getWork(tempresult)) != null )
		{
			errorcount = 0;
			
			while(true)
			{
				try
				{
					//System.out.println("A Thread is executing a subtask");
					tempresult = _Remote.execute(_ClientTask, job.toArray());
					//System.out.println("A Thread finished one subtask");
					break;
				}
				catch (Exception e) 
				{
					errorcount++;
					if(errorcount < 3)
						System.err.println("Error while starting a Remotejob .. trying again " + e.toString());
					else
					{
						System.err.println("Fatal Error while trying to send a Remotejob .. 3 in a row .. aborting" + e.toString());
						e.printStackTrace();
						return;
					}
					
				}
			}
		}
		
		System.out.println("A Thread ended (Work Complete)");
	}
	
	public void uploadAllClasses()
	{
		try
		{
			//File codebase = new File(new URI(System.getProperty("java.rmi.server.codebase")));

			for(File f : /*codebase*/Starter.PathToClasses.listFiles())
			{
				if(f.isFile())
				{
					try
					{
						FileInputStream fis = new FileInputStream(f);
						byte[] b = new byte[(int) f.length()];
						fis.read(b);
						_Remote.uploadFile(f.getName(), b);
						fis.close();
					}
					catch (Exception e) {
						System.err.println("ERROR while uploading File " + f.getName() + " : " + e.toString());
						e.printStackTrace();
					}
				}
			}
		}
		catch (Exception e) {
			System.err.println("ERROR while uploading all Classes: " + e.toString());
			e.printStackTrace();
		}
	}
	
}
