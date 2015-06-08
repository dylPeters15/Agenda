package agenda2;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import agenda2.AgendaRow;

public class MainFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<AgendaRow> rows;
	private JPanel theJPanel;
	
	
	public MainFrame(){
		initMenu();
		initPanel();
		initArray();
		setBounds(200, 200, 750, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	

	private void initMenu() {
		// TODO Auto-generated method stub
		JMenuBar theJMenuBar = new JMenuBar();
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addRow();
			}
		});
		theJMenuBar.add(addButton);
		JButton selectAll = new JButton("Select All");
		selectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				selectAll();
			}
		});
		theJMenuBar.add(selectAll);
		JButton remove = new JButton("Remove");
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				removeSelectedRows();
			}
		});
		theJMenuBar.add(remove);
		JButton edit = new JButton("Edit");
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				editSelectedRows();
			}
		});
		theJMenuBar.add(edit);
		JButton up = new JButton("Up");
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				moveSelectedRowsUp();
			}
		});
		theJMenuBar.add(up);
		JButton down = new JButton("Down");
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				moveSelectedRowsDown();
			}
		});
		theJMenuBar.add(down);
		JButton finished = new JButton("Finished");
		finished.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				markFinished();
			}
		});
		theJMenuBar.add(finished);
		setJMenuBar(theJMenuBar);
	}
	
	
	private void initPanel() {
		// TODO Auto-generated method stub
		
		JScrollPane theJScrollPane = new JScrollPane();
		theJPanel = new JPanel();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		GridBagLayout theLayout = new GridBagLayout();
		theJPanel.setLayout(theLayout);
		theJScrollPane.setViewportView(theJPanel);
		
		
		/////////////////////////////////////////////////
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	    getContentPane().setLayout(layout);
	    layout.setHorizontalGroup(
	        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	        .addComponent(theJScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
	    );
	    layout.setVerticalGroup(
	        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	        .addComponent(theJScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
	    );

	    pack();
		/////////////////////////////////////////////////
		
	}
	
	
	private void initArray() {
		// TODO Auto-generated method stub
		rows = new ArrayList<AgendaRow>();
	}
	
	
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	

	private void addRow(){
		String newText = JOptionPane.showInputDialog("Enter the new text: ");
		if (newText != null && !newText.isEmpty()){
			AgendaRow newRow = new AgendaRow(false, false, newText);
			newRow.addSelfToJPanel(theJPanel, rows.size());
			rows.add(newRow);
		}
	}
	
	private boolean allAreSelected(){
		boolean allSelected = true;
		for (AgendaRow row: rows){
			if (!row.isSelected()){
				allSelected = false;
			}
		}
		return allSelected;
	}
	
	//goes through and selects all
	//if all rows are selected, it deselects all
	private void selectAll(){
		boolean allSelected = allAreSelected();
		for (AgendaRow row: rows){
			row.setSelected(!allSelected);
		}
	}
	
	
	private void removeSelectedRows(){
		for (int i = 0; i < rows.size(); i++){
			if(rows.get(i).isSelected()){
				removeRow(i);
				i--;
			}
		}
	}
	
	private void removeRow(int rowNum){
		rows.get(rowNum).removeSelfFromJPanel(theJPanel);
		rows.remove(rowNum);
		shiftGridRowsUpToMatchArrayList(rowNum);
	}
	
	//the Grid Rows do not match the arraylist because a row was just removed from the arraylist
		//now need to shift the grid rows up
	private void shiftGridRowsUpToMatchArrayList(int emptyRow){
		for(int i = emptyRow; i < rows.size(); i++){
			rows.get(i).removeSelfFromJPanel(theJPanel);
			rows.get(i).addSelfToJPanel(theJPanel, i);
		}
	}
	
	
	
	private void editSelectedRows(){
		for (AgendaRow row: rows){
			if (row.isSelected()){
				edit(row);
				row.setSelected(false);
			}
		}
	}
	
	private void edit(AgendaRow row){
		String newText = JOptionPane.showInputDialog("Enter the new text: ");
		if (newText != null && !newText.isEmpty()){
			row.setText(newText);
		}
	}
	
	
	//marks all finished
	//if all are finished, marks all unfinished
	private void markFinished(){
		boolean allFinished = true;
		for (AgendaRow row: rows){
			if (row.isSelected()) {
				if (!row.isFinished()) {
					allFinished = false;
				}
			}
		}
		for (AgendaRow row: rows){
			if (row.isSelected()){
				row.setFinished(!allFinished);
			}
		}
	}
	
	
	
	
	
	

	
	private void moveSelectedRowsUp(){
		if (!allAreSelected()) {
			if (rows.size() > 0) {
				boolean deselect = true;
				int i = 0;
				while (deselect) {
					if (rows.get(i).isSelected()) {
						rows.get(i).setSelected(false);
					} else {
						deselect = false;
					}
					i++;
				}
			}
			for (int i = 1; i < rows.size(); i++) {
				if (rows.get(i).isSelected()) {
					switchRows(i, i-1);
				}
			}
		}
	}
	
	private void switchRows(int i1, int i2){
		Collections.swap(rows, i1, i2);
		rows.get(i1).removeSelfFromJPanel(theJPanel);
		rows.get(i2).removeSelfFromJPanel(theJPanel);
		rows.get(i1).addSelfToJPanel(theJPanel, i1);
		rows.get(i2).addSelfToJPanel(theJPanel, i2);
	}
	
	
	private void moveSelectedRowsDown(){
		if (!allAreSelected()){
			if (rows.size() > 0){
				boolean deselect = true;
				int i = rows.size()-1;
				while (deselect){
					if (rows.get(i).isSelected()) {
						rows.get(i).setSelected(false);
					} else {
						deselect = false;
					}
					i--;
				}
			}
			for (int i = rows.size()-2; i >= 0; i--){
				if (rows.get(i).isSelected()){
					switchRows(i, i+1);
				}
			}
		}
	}
	
	
	
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
