package de.warker.rmicluster;

import java.io.Serializable;

public class Node implements Serializable
{
	public String Host;
	
	public Node()
	{
		Host = "//127.0.0.1";
	}
	
	public Node(String host)
	{
		this.Host = host;
	}
	
	@Override
	public String toString()
	{
		return "Host: " + Host;
	}
}
