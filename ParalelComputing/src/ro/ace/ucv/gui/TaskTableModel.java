package ro.ace.ucv.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import ro.ace.ucv.Task;

/**
 * Class implementation for the task table
 * @author gabi
 *
 */
public class TaskTableModel extends AbstractTableModel {
   
	private static final int FILE_NAME = 0;
    private static final int FREE_MEMORY = 1;
    private static final int CPU_TIME = 2;
    private static final int  FREE_SPACE = 3;

    private String[] columnNames;
    private Vector dataVector;

    /**
     * Constructor
     * @param columnNames Columns names to be displayed in the table.
     */
    public TaskTableModel(String[] columnNames) {
        this.columnNames = columnNames;
        dataVector = new Vector();
    }

    /**
     * Getter for the column name.
     * @param column index of the item we want to get.
     */
    public String getColumnName(int column) {
        return columnNames[column];
    }
    /**
     * Getter for the object at a specified position.
     * @param row The row of the object wanted to be get.
     * @param column The column of the object wanted to be get.
     */
    public Object getValueAt(int row, int column) {
        Task task = (Task)dataVector.get(row);
        switch (column) {
            case FILE_NAME:
               return task.getFileName();
            case FREE_MEMORY:
               return task.getFreeMemoryNeeded();
            case CPU_TIME:
               return task.getCpuTime();
            case FREE_SPACE:
                return task.getFreeSpaceNecessary();
            default:
               return new Object();
        }
    }
    /**
     * Getter for the row count.
     */
    public int getRowCount() {
        return dataVector.size();
    }
    /**
     * Getter for the column count.
     */
    public int getColumnCount() {
        return columnNames.length;
    }
    
    /**
     * Adds a task in the table.
     * @param task	The task that will be added in the table.
     */
    public void addTask(Task task) {
        dataVector.add(task);
        fireTableRowsInserted(
           dataVector.size() - 1,
           dataVector.size() - 1);
    }

}