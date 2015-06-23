package ro.ace.ucv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 
 * Class implementation for the task list.
 * @author gabi
 *
 */
public class TaskList {

	private static List<Task> tasks = new ArrayList<Task>();

	public static final String serverPath = "C:\\Users\\gabila\\Desktop\\Task";

	static {

		File file = new File(serverPath);
		File[] files = file.listFiles();

		for (File fileTask : files) {
			Task task = new Task();
			String taskName = serverPath + File.separator + fileTask.getName();

			File configFile = null;
			for (File filesTask : fileTask.listFiles()) {
				if (filesTask.getName().contains(".config")) {
					configFile = filesTask;
					break;
				}
			}
			task.setFileName(fileTask.getName());

			Properties prop = new Properties();
			InputStream inputStream;
			try {
				inputStream = new FileInputStream(configFile);
				if (inputStream != null) {
					prop.load(inputStream);
					String freeSpaceNecessary = prop
							.getProperty("freeSpaceNecessary");
					String cpuTime = prop.getProperty("cpuTime");
					String freeMemoryNeeded = prop
							.getProperty("freeMemoryNeeded");

			
					task.setCpuTime(Long.parseLong(cpuTime));
					task.setFreeMemoryNeeded(Long.parseLong(freeMemoryNeeded));
					task.setFreeSpaceNecessary(Long
							.parseLong(freeSpaceNecessary));

					
					tasks.add(task);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Getter for the task list based on the PC information.
	 * @see Task
	 * @param freeMemory	The free memory of the Pc.
	 * @param cpuTime	The Cpu time of the pc.
	 * @param freeSpace	the free space pc of the pc.
	 * @return	List<Task> 	The task list based on the pc information.
	 */
	public static List<Task> getProgramList(String freeMemory, String cpuTime,
			String freeSpace) {

		Long freeMemoryLong = Long.parseLong(freeMemory);
		Long cpuTimeLong = Long.parseLong(cpuTime);
		Long freeSpaceLong = Long.parseLong(freeSpace);

		List<Task> avabileTask = new ArrayList<Task>();
		for (Task task : tasks) {

			if (freeMemoryLong > task.getFreeMemoryNeeded()
					&& cpuTimeLong > task.getCpuTime()
					&& freeSpaceLong > task.getFreeMemoryNeeded()) {
				avabileTask.add(task);
			}
		}
		return avabileTask;
	}

	/**
	 * Getter for a task from a name.
	 * @param fileName	The name of the file that we want to find a task.
	 * @return	The task with the specified file name.
	 */
	public static Task getTaskFromName(String fileName) {

		for (Task task : tasks) {
			if (task.getFileName().equals(fileName)) {
				return task;
			}
		}
		return null;
	}
}
