package ro.ace.ucv;

import java.io.Serializable;

/**
 * Class implementation for a  client task.
 * @author gabi
 *
 */
public class Task implements Serializable {

	private String fileName;

	private Long freeSpaceNecessary;

	private Long cpuTime;

	private Long freeMemoryNeeded;
	
	private String start;
	
	private String end;

	/**
	 * Setter for the cpu time
	 * @param cpuTime	Sets the cpu time.
	 */
	public void setCpuTime(Long cpuTime) {
		this.cpuTime = cpuTime;
	}
	
	/**
	 * Setter for the file name.
	 * @param fileName	The name of the file.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * Setter for the free memory needed to execute the task.
	 * @param freeMemoryNeeded	The free memory needed to execute the task.
	 */
	public void setFreeMemoryNeeded(Long freeMemoryNeeded) {
		this.freeMemoryNeeded = freeMemoryNeeded;
	}
	/**
	 * Setter for the free space needed to execute the task.
	 * @param freeSpaceNecessary	The free space needed to execute the task.
	 */
	public void setFreeSpaceNecessary(Long freeSpaceNecessary) {
		this.freeSpaceNecessary = freeSpaceNecessary;
	}

	/**
	 * Getter for the cpu time
	 * @return	cpu time
	 */
	public Long getCpuTime() {
		return cpuTime;
	}
	/**
	 * Getter for the file name.
	 * @return	The name of the flile.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * Getter for the first arg of the task.
	 * @return	The first arg o the task.
	 */
	public String getStart() {
		return start;
	}
	/**
	 * Getter for the second arg of the task.
	 * @return	The second arg of the task.
	 */
	public String getEnd() {
		return end;
	}
	/**
	 * Getter for the free memory needed.
	 * @return	The free memory needed.
	 */
	public Long getFreeMemoryNeeded() {
		return freeMemoryNeeded;
	}

	/**
	 * Getter for the free space needed.
	 * @return	The free space needed.
	 */
	public Long getFreeSpaceNecessary() {
		return freeSpaceNecessary;
	}

	@Override
	public String toString() {

		return "CPU: " + cpuTime + "\n" + "FreeMem:" + freeMemoryNeeded + "\n"
				+ "FreeSpace:" + freeSpaceNecessary + "\n" + "FileName"
				+ fileName;
	}

	/**
	 * Setter for the first args that the task will be executed.
	 * @param param1
	 */
	public void setStart(String param1) {
		this.start = param1;
	}
	/**
	 * Setter for the second args that the task willbe executed.
	 * @param param2
	 */
	public void setEnd(String param2) {
		this.end = param2;
	}
}
