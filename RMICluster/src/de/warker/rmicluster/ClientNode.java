package de.warker.rmicluster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

public class ClientNode extends UnicastRemoteObject implements IClientNode
{
	public static final String RMIID = "ClientNode";

	//private static ClusterClassLoader _Loader = null;
	//private static ClassLoader _PreviousLoader = null;
	
	private static HashMap<String, byte[]> _UploadedFiles;
	
	protected ClientNode() throws RemoteException
	{
		super();
		
		_UploadedFiles = new HashMap<String, byte[]>();
		
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
	}

	@Override
	public <T> T execute(IClientTask task, Object... params) throws RemoteException
	{
		if(task == null)
			throw new RemoteException("Server has send an empty task");		
				
		return task.execute(params);
	}
	
	public void registerNode(String master, String local)
	{
		try
		{
			IServerNode sn = (IServerNode)Naming.lookup("//" + master + "/" + ServerNode.RMIID);
			Node ln = new Node("//" + local);
			sn.registerNode(ln);
		}
		catch (Exception e) {
			System.err.println("Could nor register my Node at the Server " + master + " : " + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void uploadFile(String name, byte[] bytes) throws RemoteException
	{
		try
		{
			final MessageDigest md5 = MessageDigest.getInstance("MD5");
			
			
			
			if(_UploadedFiles.containsKey(name))
			{
				System.out.println("File " + name + " already exists.. checking ...");
				byte[] datahash = md5.digest(bytes);
				if(!Arrays.equals(datahash, _UploadedFiles.get(name)))
				{
					_UploadedFiles.put(name, datahash);
					writeFile(name, bytes);
				}
				else
					System.out.println("Data is the same");
			}
			else
			{
				_UploadedFiles.put(name, md5.digest(bytes));
				writeFile(name, bytes);
			}
			
		}
		catch (Exception e) {
			System.err.println("ERROR while uploading class " + name + " : " + e.toString());
			e.printStackTrace();
			throw new RemoteException();
		}
	}
	
	public static void writeFile(String name, byte[] bytes)
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
			System.err.println("Could not write file: " + name + " : " + e.toString());
			e.printStackTrace();			
		}
	}

}
