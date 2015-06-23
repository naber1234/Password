package ro.ace.ucv.gui;

import java.util.List;

import ro.ace.ucv.Volunteer;
import ro.ace.ucv.Task;
/**
 * Interface for trigger the user interface based on the Volunteer
 * @see Volunteer
 * @author gabi
 *
 */
public interface ConnectionListener {

	/**
	 * Fills the ClientGui with the task received from the Volunteer
	 * @see ClientGui
	 * @see Volunteer
	 * @param tasks The list of task to be displayed on the ClientGui
	 */
	void fillTable(List<Task> tasks);
	/**
	 * Is called when the connection is successful. 
	 */
	void succesfullConnected();
	/**
	 * Is called when a error occurs on the server
	 */
	void errorOccured();
}
