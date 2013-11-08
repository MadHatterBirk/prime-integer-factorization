package de.warker.rmicluster;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientNode extends Remote
{
	public <T> T execute(IClientTask task, Object...params) throws RemoteException;
	public void uploadFile(String name, byte[] classbytes) throws RemoteException;
}
