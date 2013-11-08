package de.warker.rmicluster;

import java.io.Serializable;
import java.util.Vector;

public interface IServerTask extends Serializable
{
	public <T> Vector<T> getWork(Vector result); 
	public <T> Vector<T> getResults();
}
