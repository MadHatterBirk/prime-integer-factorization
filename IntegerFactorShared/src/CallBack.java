import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallBack extends Remote {
	
	/** The server notifies a client of the other player's move */
	public void setName(int clientName) throws RemoteException;

	/** The server sends a message to be displayed by the client */
	public void notify(java.lang.String message) throws RemoteException;

	/** The server notifies a client of the other player's move */
	public void factor(BigInteger start, BigInteger end, BigInteger number) throws RemoteException;
}