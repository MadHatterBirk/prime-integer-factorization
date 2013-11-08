package de.warker.rmicluster;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class ServerNode extends UnicastRemoteObject implements IServerNode
{

	public static final String RMIID = "ServerNode";
	
	//private static ClusterClassLoader _Loader = null;
	//private static ClassLoader _PreviousLoader = null;
	
	private Vector<Node> _Nodes;
	private Vector<ServerThread> _Threads;
	private IServerTask _ServerTask;
	private IClientTask _ClientTask;
	
	protected ServerNode() throws RemoteException
	{
		super();
		
		/*try
		{
			if(_Loader == null)
			{
				System.out.println("creating Loader");
				_PreviousLoader = Thread.currentThread().getContextClassLoader();
				URL[] urls = {Starter.PathToClasses.toURI().toURL()};
				_Loader = new ClusterClassLoader(urls);
				Thread.currentThread().setContextClassLoader(_Loader);
			}
		}
		catch (Exception e) {
			System.err.println("ERROR while creating Classloader: " + e.toString());
			e.printStackTrace();
		}*/
			
		_Nodes = new Vector<Node>();
		_Threads = new Vector<ServerThread>();
		_ServerTask = null;
		_ClientTask = null;
	}

	@Override
	public <T> Vector<T> execute(IServerTask stask, IClientTask ctask) throws RemoteException
	{
		if(stask == null || ctask == null)
			throw new RemoteException("The User send an empty Server or Cliettask");
		
		System.out.println("Got class for server: " + stask.getClass().getName());
		System.out.println("Got class for server: " + ctask.getClass().getName());
		
		_ServerTask = stask;
		_ClientTask = ctask;
		
		//_ServerTask = _Loader.loadClass(stask.getClass().getName()).getConstructor(stask.getClass());
		//_ClientTask = ctask;
		
				
		while(_Nodes.size() == 0)
		{
			System.out.println("Got a Job, but no Workerclients .. waiting 10 seconds");
			try
			{
				Thread.sleep(10000);
			}
			catch (Exception e){}
		}
		
		for(Node node : _Nodes) // add current Task to all available Nodes at registration
		{
			try
			{
				ServerThread st = new ServerThread(node, _ServerTask, _ClientTask);
				st.start();
				_Threads.add(st);
			}
			catch (Exception e) {
				System.err.println("Could not Start a Nodethread.. ignoring Node: " + e.toString());
				e.printStackTrace();
			}
		}
		
		waitAllReady(); 	
				
		return (Vector<T>)_ServerTask.getResults();
	}

	@Override
	public void registerNode(Node node) throws RemoteException
	{
		if(node == null)
			throw new RemoteException("There was a registerNode attempt with an empty node");
		
		if(node.Host.startsWith("//"))
		{
			if(node.Host.length() == 2)
				throw new RemoteException("There was a registerNode attempt with no Hostname in it");
		}
		else
			node.Host = "//" + node.Host;
		
		System.out.println("Node " + node.Host + " just registered as a ClientNode");
		_Nodes.add(node);
		
		if(_ServerTask != null && _ClientTask != null)
		{
			try
			{
				ServerThread st = new ServerThread(node, _ServerTask, _ClientTask);
				st.start();
				_Threads.add(st);
			}
			catch (Exception e) {
				System.err.println("ERROR: could not add current task to new Node " + node.Host + " " + e.toString());
				e.printStackTrace();
			}
		}		
	}
	
	private void waitAllReady()
	{
		System.out.println("Waiting for all threads to end");
	
		for(Thread t : _Threads)
		{
			try
			{
				t.join();
			}
			catch (Exception e){}
		}
		
		System.out.println("All Threads ended");
	}

	@Override
	public void uploadFile(String name, byte[] bytes) throws RemoteException
	{
		try
		{
			System.out.println("Got a File: " + name + " with " + bytes.length + " Bytes saving at " + Starter.PathToClasses);
			FileOutputStream fos = new FileOutputStream(Starter.PathToClasses + File.separator + name);
			fos.write(bytes);
			fos.close();
			
			if(name.endsWith(".jar") && Starter._Loader != null)
				Starter._Loader.addURL(new File(Starter.PathToClasses + File.separator + name).toURI().toURL());
				
		}
		catch (Exception e) {
			System.err.println("ERROR while uploading class " + name + " : " + e.toString());
			e.printStackTrace();
			throw new RemoteException();
		}		
	}

}
