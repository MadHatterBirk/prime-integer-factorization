package de.warker.rmicluster;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;

public class Starter
{
	
	public static File UserDir;
	public static File PathToClasses;
	
	protected static ClusterClassLoader _Loader;
	protected static ClassLoader _PreviousLoader;
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		setCodebase();
		
		String master = "//127.0.0.1";
		String local = "//127.0.0.1";
		String mode = "client";
		
		if(args.length >= 2)
		{
			mode = args[0];
			master = args[1];
			if(args.length == 3)
				local = args[2];
		}
		else
		{
			System.out.println("params: server|client //master [//local]");
			return;
		}
		
		setSecurityManager();
		
		try
		{
			if(mode.equals("client"))
			{
				LocateRegistry.createRegistry(1099);
				System.out.println("RMI registered on port 1099");
				System.setProperty("java.rmi.server.hostname", local);
				System.out.print("How many Threads?: ");
				Scanner sc = new Scanner(System.in);
				int numThreads = sc.nextInt();
				
				for(int i = 0; i < numThreads; i++)
				{
					ClientNode cn = new ClientNode();
					Naming.rebind(i + "/" + ClientNode.RMIID, cn);
					System.out.println("bound client to //" + local + "/"+ i + "/" + ClientNode.RMIID);
					cn.registerNode(master, local + "/" + i);
				}
			}
			else if(mode.equals("server"))
			{
				LocateRegistry.createRegistry(1099);
				System.out.println("RMI registered on port 1099");								
				System.setProperty("java.rmi.server.hostname", master);
				ServerNode sn = new ServerNode();
				Naming.rebind(ServerNode.RMIID, sn);
				System.out.println("bound server to //" + master + "/" + ServerNode.RMIID);
			}			
		}
		catch(Exception e)
		{
			System.out.println("ERROR" + e.toString());
			e.printStackTrace();
		}
	}
	
	public static void setSecurityManager()
	{
		if(System.getSecurityManager() == null)
		{
			System.setSecurityManager(new RMISecurityManager());
			System.out.println("Security Manager installed");
		}
		else
			System.out.println("Security Manager allready installed");
	}
	
	public static void setCodebase()
	{
		try
		{
			UserDir = new File(System.getProperty("user.dir"));
			if(UserDir.exists())
			{
				PathToClasses = new File(UserDir.getAbsolutePath() + File.separatorChar + "classes");
				if(!PathToClasses.exists())
					PathToClasses.mkdirs();
				
				URL[] urls = { PathToClasses.toURI().toURL() };
				_Loader = new ClusterClassLoader(urls);
				_PreviousLoader = Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(_Loader);
				//System.setProperty("java.rmi.server.codebase", PathToClasses.toURI().toURL().toString());
			}
			else
				System.err.println("ERROR: Userdir " + UserDir + " does not exist");
			
		}
		catch (Exception e) {
			System.err.println("ERROR could not set the Classes directory: " + e.toString());
			e.printStackTrace();
		}		
	}
		
	public static <T> Vector<T> wrapper(String Master, IServerTask stask, IClientTask ctask, Class... classes)
	{
		LinkedList<String> classString = new LinkedList<String>();
		String[] classpatharray = System.getProperty("java.class.path").split(":");
		String classpath = classpatharray[0];
		
		for(int j = 1; j < classpatharray.length; j++)
		{
			if(classpatharray[j].endsWith(".jar"))
				classString.add(classpatharray[j]);				
		}
		
		for(int i = 0; i < classes.length; i++)
		{
			classString.add(classpath + File.separator + classes[i].getName().replace('.', File.separatorChar) + ".class");
		}
		
		return wrapper(Master, stask, ctask, classString.toArray(new String[0]));
	}
	
	public static <T> Vector<T> wrapper(String Master, IServerTask stask, IClientTask ctask, String[] classes)
	{
		try
		{
			IServerNode msn = (IServerNode)Naming.lookup("//" + Master + "/" + ServerNode.RMIID);
			
			for(String c : classes)
			{
				File f = new File(c);
				FileInputStream fis = new FileInputStream(f);
				byte[] data = new byte[(int)f.length()];
				fis.read(data);
				if(c.endsWith(".class") || c.endsWith(".jar"))
					msn.uploadFile(getClassname(c), data);
				
				fis.close();
			}
			
			return msn.execute(stask, ctask);
		}
		catch (Exception e) {
			System.err.println("ERROR while starting Remotetask: " + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getClassname(String path)
	{
		File f = new File(path);
		if(f.exists() && f.isFile())
		{
			String n = f.getName();
			if(n.endsWith(".class") || n.endsWith(".jar"))
				return n;
		}
		return null;
	}

}
