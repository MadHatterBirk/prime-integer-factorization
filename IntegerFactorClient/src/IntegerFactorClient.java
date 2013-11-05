import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class IntegerFactorClient extends JApplet implements ActionListener {
	private boolean connected = false;

	// IntegerFactorServer is the server for coordinating with the clients
	private IntegerFactorServerInterface IntegerFactorServer;
	// Create callback for use by the server to control the client
	private CallBackImpl callBackControl;

	// Border for cells and panel
	private Border lineBorder = BorderFactory.createLineBorder(Color.black, 1);
	private JButton connectButton = new JButton("Connect to Server");
	private JButton disconnectButton = new JButton("Disconnect from Server");
	private JLabel jlblStatus = new JLabel("Click Connect!");
	private JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(textArea);

	boolean isStandalone = false;
	
	public void primeFactors(BigInteger number) {
		
		BigInteger numberToFactor = number;
		List<BigInteger> factors = new ArrayList<BigInteger>();
		textArea.append("Factoring: " + number + "\n");
		
		for (BigInteger i = BigInteger.valueOf(2); i.compareTo(number) <= 0; i = i.add(BigInteger.ONE)) {
			while (number.remainder(i).compareTo(BigInteger.ZERO) == 0) {
				factors.add(i);
				textArea.append(i + "\n");
				number = number.divide(i);
			}
		}
		
		textArea.append("Finished Factoring!" + "\n");
		try {
			IntegerFactorServer.myResults((CallBack) callBackControl, numberToFactor, factors);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Initialize the applet */
	public void init() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		connectButton.setActionCommand("connect");
		disconnectButton.setActionCommand("disconnect");
		disconnectButton.setEnabled(false);

		buttonPanel.add(connectButton);
		buttonPanel.add(disconnectButton);

		textArea.setEditable(false);
		scrollPane.setBorder(lineBorder);

		add(jlblStatus, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		// Add action listener to button
		connectButton.addActionListener(this);
		disconnectButton.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if ("connect".equals(e.getActionCommand())) {
				initializeRMI();
				connectButton.setEnabled(false);
				disconnectButton.setEnabled(true);
			} else if ("disconnect".equals(e.getActionCommand())) {
				connectButton.setEnabled(true);
				disconnectButton.setEnabled(false);
				IntegerFactorServer.disconnect((CallBack) callBackControl);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Initialize RMI */
	protected boolean initializeRMI() throws Exception {
		String host = "";
		if (!isStandalone)
			host = getCodeBase().getHost();
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			IntegerFactorServer = (IntegerFactorServerInterface) registry
					.lookup("IntegerFactorServer");
			System.out.println("Server object " + IntegerFactorServer
					+ " found");
		} catch (Exception ex) {
			System.out.println(ex);
		}

		callBackControl = new CallBackImpl(this);
		if (connected = IntegerFactorServer.connect((CallBack) callBackControl)) {
			System.out.println("connected to server.");
			return true;
		} else {
			System.out.println("Connection Failed!");
			return false;
		}
	}

	/** Set message on the status label */
	public void setMessage(String message) {
		jlblStatus.setText(message);
		textArea.append(message + "\n");
	}

	/** Main method */
	public static void main(String[] args) {
		IntegerFactorClient applet = new IntegerFactorClient();
		applet.isStandalone = true;
		applet.init();
		applet.start();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Integer Factor Client");
		frame.add(applet, BorderLayout.CENTER);
		frame.setSize(400, 320);
		frame.setVisible(true);
	}
	
}
