package ro.ace.ucv;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import ro.ace.ucv.gui.ConnectionListener;

/**
 * Class implementation for the Volunteer model.
 * @author gabi
 *
 */
public class Volunteer {

	private Socket socket;
	private int serverPort = 5044;
	private final String serverUrl = "localhost";
	private List<Task> tasks;
	private BufferedReader fromServer;
	private PrintWriter toServer;
	private String programUrl = "C:\\Users\\gabila\\Desktop\\recivedFile";
	private InetAddress host;
	private ConnectionListener clientListener;

	/**
	 * Constructor
	 * @param clientListener Listener for displaying the status of the server,and the tasks.
	 */
	public Volunteer(ConnectionListener clientListener) {
		this.clientListener = clientListener;

	}

	/**
	 * Starts the connection to the server.
	 * Sending Pc information.
	 *Receive tasks from the server. 
	 */
	public void run() {
		try {
			host = InetAddress.getByName(serverUrl);

			socket = new Socket(host, serverPort);
			// Socket socket = new Socket("127.0.0.1", serverPort);
			clientListener.succesfullConnected();
			System.out.println("Just connected to "
					+ socket.getRemoteSocketAddress());
			toServer = new PrintWriter(socket.getOutputStream(), true);
			fromServer = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			toServer.println("<read>");
			toServer.println(PerformanceViewer.getFreeMemoryInMb());
			toServer.println(PerformanceViewer.getCpuTimeInNanoSeconds());
			toServer.println(PerformanceViewer.getFreeSpaceInGB());

			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());

			try {
				tasks = (List<Task>) ois.readObject();
				for (Task task : tasks) {
					System.out.println(task.toString());
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			clientListener.fillTable(tasks);
		} catch (UnknownHostException ex) {
			clientListener.errorOccured();
			ex.printStackTrace();
		} catch (IOException e) {
			clientListener.errorOccured();
		}
	}

	/**
	 * Close the connection to the server.
	 * @throws IOException
	 */
	public void closeConection() throws IOException {
		toServer.close();
		fromServer.close();
		socket.close();
	}
	/**
	 * Send the fileName to the server requesting the download for the specified file.
	 * @param fileName	The file name that the user wants to download and run.
	 * @throws IOException
	 */
	public void requestFile(String fileName) throws IOException {
		toServer.println(fileName);
		String param1 = fromServer.readLine();
		String param2 = fromServer.readLine();
		reciveFile(fileName, param1, param2);
	}

	/**
	 * Executes a file.
	 * @param fileName	The file name that will be executed.
	 * @param param1	The first argument.
	 * @param param2	The second argument
	 */
	private void executeFile(final String fileName, String param1, String param2) {
		final Process process;
		try {
			process = Runtime.getRuntime().exec(
					"java -cp " + programUrl + " " + fileName + " " + param1
							+ " " + param2);

			final BufferedReader stdInput = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			final BufferedReader stdError = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						String s = null;
						while ((s = stdInput.readLine()) != null) {
							System.out.println(s);
							if (s != null && !s.equals("null")) {
								socket = new Socket(host, serverPort);
								sendResult(fileName, s);
							}
							stdInput.close();
							stdError.close();
							return;
						}

						// read any errors from the attempted command
						System.out
								.println("Here is the standard error of the command (if any):\n");
						while ((s = stdError.readLine()) != null) {
							System.out.println(s);
							stdInput.close();
							stdError.close();
							return;
						}

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}).start();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Send result to the server.
	 * @param fileName	The name of the file we found the result.
	 * @param result	The result found.
	 */
	private void sendResult(String fileName, String result) {
		try {
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			pw.println("<result>");
			pw.println(fileName);
			pw.println(result);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receive file from the server.
	 * @param fileName	The name of the file received.
	 * @param param1	The argument that will be ran with the file.
	 * @param param2	The argument that will be ran with the file.
	 */
	private void reciveFile(String fileName, String param1, String param2) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			InputStream is = socket.getInputStream();

			if (is != null) {

				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				byte[] aByte = new byte[1];
				int bytesRead;

				fos = new FileOutputStream(programUrl + File.separator
						+ fileName + ".class");
				bos = new BufferedOutputStream(fos);
				bytesRead = is.read(aByte, 0, aByte.length);

				do {
					baos.write(aByte);
					bytesRead = is.read(aByte);
				} while (bytesRead != -1);

				bos.write(baos.toByteArray());
				bos.flush();
				bos.close();
			}
			executeFile(fileName, param1, param2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}