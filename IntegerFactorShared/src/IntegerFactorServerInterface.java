import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IntegerFactorServerInterface extends Remote{
	
	/**
	 * Connect to the IntegerFactor server
	 * @return 
	 */
	public boolean connect(CallBack client) throws RemoteException;
	
	/**
	 * Disconnect from the IntegerFactor server
	 */
	public boolean disconnect(int client) throws RemoteException;
	
	/** A client invokes this method to notify the server of its results */
	public void myResults(int clientName, BigInteger number, List<BigInteger> list) throws RemoteException;

}
