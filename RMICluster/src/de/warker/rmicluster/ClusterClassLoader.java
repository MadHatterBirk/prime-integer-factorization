package de.warker.rmicluster;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class ClusterClassLoader extends URLClassLoader
{

	public ClusterClassLoader(URL[] arg0)
	{
		super(arg0);
	}

	public ClusterClassLoader(URL[] arg0, ClassLoader arg1)
	{
		super(arg0, arg1);
	}

	public ClusterClassLoader(URL[] arg0, ClassLoader arg1,
			URLStreamHandlerFactory arg2)
	{
		super(arg0, arg1, arg2);
	}
	
	public void addURL(URL url)
	{
		System.out.println("adding " + url.toString() + " to Classloader");
		super.addURL(url);
	}

}
