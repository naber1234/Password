package ro.ace.ucv;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import ro.ace.ucv.gui.ServerGUI;
/**
 * 
 * Class implementation for the server model.
 * @author gabi
 *
 */
public class ServerComputing {

	private static int nrUsers = 0;

	
	public static void main(String[] args) throws Exception {
		new ServerComputing();
	}
	
	public ServerComputing() throws Exception {
		startServer();
	}
	
	/**
	 * Start the server and waits for clients to connect and open a responder for every connect.
	 * Open the user interface.
	 * @see ClientHandler
	 * @see ServerGUI
	 * @throws Exception
	 */
	public void startServer() throws Exception {

		ServerSocket welcomeSocket = new ServerSocket(5044);
		ServerGUI svGui = new ServerGUI();
		ClientHandler h = new ClientHandler(svGui);
		// server runs for infinite time and
		// wait for clients to connect
		while (true) {

			// waiting..
			Socket connectionSocket = welcomeSocket.accept();

			svGui.putMessage("Voluntar Nr:" + nrUsers + " connected to the server.");
			h.setClientName("Voluntar Nr:" + nrUsers);
			nrUsers++;

			// on connection establishment start a new thread for each client
			// each thread shares a common responder object
			// which will be used to respond every client request
			// need to synchronize method of common object not to have
			// unexpected behaviour
			Thread t = new Thread(new ServerThread(h, connectionSocket));

			// start thread
			t.start();

		}
	}
}

/**
 * Thread for handling a client.
 * @author gabi
 *
 */
class ServerThread implements Runnable {

	private ClientHandler responder;
	private Socket connectionSocket;

	/**
	 * Constructor.
	 * @param responder The model for handling a client.
	 * @param connectionSocket	Socket for the connection.
	 */
	public ServerThread(ClientHandler responder, Socket connectionSocket) {
		this.responder = responder;
		this.connectionSocket = connectionSocket;
	}

	@Override
	public void run() {

		while (responder.handleClient(connectionSocket)) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}

		try {
			connectionSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null,
					ex);
		}

	}

}
/**
 * Class implementation for handling a client.
 * @author gabi
 *
 */
class ClientHandler {

	private ServerGUI svGui;
	private String clientName;

	/**
	 * Constructor.
	 */
	public ClientHandler(ServerGUI svGui) {
		this.svGui = svGui;
	}
	
	/**
	 * Setter for the client that is handled.
	 * @param clientName	The name of the client.
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * Method for handling a client.
	 * Receive Pc information from client.
	 * Send tasks available for the client based on the pc information.
	 * Receive file name that user wants to execute.
	 * Send the file to the user.
	 * @param connectionSocket	The connection socket.
	 * @return	<code>true<code> if the operations are successful.
	 */
	public boolean handleClient(Socket connectionSocket) {
		try {

			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));

			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());

			String comand = inFromClient.readLine();
			if (!comand.equals("<result>")) {
				svGui.putMessage("Reving computer information...");

				String freeMemory = inFromClient.readLine();
				String cpuTime = inFromClient.readLine();
				String freeSpace = inFromClient.readLine();

				ObjectOutputStream oos;
				// if client process terminates it get null, so close connection
				if (freeMemory == null || cpuTime == null || freeSpace == null) {

					return false;
				} else {
					System.out.println(clientName + "Free memory received..."
							+ freeMemory);
					System.out.println(clientName + "Cpu Time recived..."
							+ cpuTime);
					System.out.println(clientName + "Free space received..."
							+ freeSpace);
					List<Task> tasks = TaskList.getProgramList(freeMemory,
							cpuTime, freeSpace);

					oos = new ObjectOutputStream(
							connectionSocket.getOutputStream());
					oos.writeObject(tasks);
					svGui.putMessage(clientName + "List of task sended...");

				}
				String fileName = inFromClient.readLine();

				Task task = TaskList.getTaskFromName(fileName);
				Properties prop = new Properties();
				String configPath = TaskList.serverPath + File.separator
						+ fileName + File.separator + fileName + ".config";
				InputStream inputStream = new FileInputStream(new File(
						configPath));
				prop.load(inputStream);
				String param1 = prop.getProperty("param1");

				String param2 = prop.getProperty("param2");
				svGui.putMessage(clientName + "Sending params..");

				String end = ""
						+ (Integer.parseInt(param1) + Integer.parseInt(param2) / 10);
				task.setStart(param1);
				task.setEnd(end);

				prop.setProperty("param1", end);

				prop.store(new FileOutputStream(new File(configPath)),
						"Interval Changed");

				outToClient.writeBytes(task.getStart() + "\n");
				outToClient.writeBytes(task.getEnd() + "\n");

				File myFile = new File(TaskList.serverPath + File.separator
						+ fileName + File.separator + fileName + ".class");
				byte[] mybytearray = new byte[(int) myFile.length()];

				FileInputStream fis = null;

				try {
					fis = new FileInputStream(myFile);
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				}
				BufferedInputStream bis = new BufferedInputStream(fis);
				System.out.println("aaaa");
				svGui.putMessage(clientName + "Sending file...");
				bis.read(mybytearray, 0, mybytearray.length);
				outToClient.write(mybytearray, 0, mybytearray.length);
				svGui.putMessage(clientName + "File send...");
				outToClient.flush();
				outToClient.close();

				connectionSocket.close();
				// File sent, exit the main method
				bis.close();
				oos.close();
				connectionSocket.close();
				return true;
			} else {
				String fileName = inFromClient.readLine();
				String result = inFromClient.readLine();
				svGui.putMessage(clientName + "Reciveing result..." + result);

				Properties prop = new Properties();
				String configPath = TaskList.serverPath + File.separator
						+ fileName + File.separator + fileName + ".config";
				InputStream inputStream = new FileInputStream(new File(
						configPath));
				prop.load(inputStream);

				prop.setProperty("param1", "" + 0);
				prop.setProperty("result", result);

				prop.store(new FileOutputStream(new File(configPath)),
						"Interval Changed");

				connectionSocket.close();
				return true;
			}
		} catch (SocketException e) {
			svGui.putMessage(clientName + " Disconected");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}