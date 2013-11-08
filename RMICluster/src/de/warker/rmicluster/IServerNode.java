package de.warker.rmicluster;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface IServerNode extends Remote
{
	public <T> Vector<T> execute(IServerTask stask, IClientTask ctask) throws RemoteException;
	public void registerNode(Node node) throws RemoteException;
	public void uploadFile(String name, byte[] bytes) throws RemoteException;
}
