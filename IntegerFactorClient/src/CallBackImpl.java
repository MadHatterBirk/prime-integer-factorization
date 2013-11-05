import java.math.BigInteger;
import java.rmi.*;
import java.rmi.server.*;

public class CallBackImpl extends UnicastRemoteObject implements CallBack {
	// The client will be called by the server through callback
	private IntegerFactorClient thisClient;

	/** Constructor */
	public CallBackImpl(Object client) throws RemoteException {
		thisClient = (IntegerFactorClient) client;
	}

	/** The server sends a message to be displayed by the client */
	public void notify(String message) throws RemoteException {
		thisClient.setMessage(message);
	}

	public void setName(int clientName) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	/** The server notifies a client of the number to factor */

	public void factor(BigInteger start, BigInteger end, BigInteger number)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}