package ro.ace.ucv;

import java.io.File;
import java.lang.management.ManagementFactory;

/**
 * Class implementation for getting the operating system specification that are avalible.
 * @author gabi
 *
 */
public class PerformanceViewer {

	private static com.sun.management.OperatingSystemMXBean operatingSystem = (com.sun.management.OperatingSystemMXBean) ManagementFactory
			.getOperatingSystemMXBean();

	/**
	 * Getter for the free memory in Mb.
	 * @return	The free memory available.
	 */
	public static long getFreeMemoryInMb() {

		long freeMemory = operatingSystem.getFreePhysicalMemorySize();

		return freeMemory / 1024 / 1024;
	}

	/**
	 * Getter for the cpu time in nano seconds.
	 * @return
	 */
	public static Long getCpuTimeInNanoSeconds() {
		return operatingSystem.getProcessCpuTime();
	}
/**
 * Getter for the free space in GB.
 * @return	The free space in gb.
 */
	public static Long getFreeSpaceInGB() {

		File file = new File(System.getProperty("user.dir"));
		long freeSpace = file.getFreeSpace(); // unallocated / free disk space
												// in bytes.
		return freeSpace / Math.round(Math.pow(1024, 3));
	}
}