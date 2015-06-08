package agenda2;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//import javax.swing.JTextArea;



import agenda2.AgendaRow;

//import java.util.prefs.*;

public class MainFrame extends JFrame implements ActionListener {
	
	
	///////////////////////save and load I think are the only two methods that need to be added to

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<AgendaRow> rows;
	private JPanel theJPanel;
	
	
	private final String PATH_KEY = "Path";
	private String loadedFilePath;
	private String[] theFile;
	private Preferences pref;
	private JButton save;
	private boolean hasLoadedOrCreatedFile;
	
	public MainFrame(){
		hasLoadedOrCreatedFile = false;
		pref =Preferences.userNodeForPackage(this.getClass());
		//System.out.println(pref.get(PATH_KEY, "no key"));
		loadedFilePath = "";
		theFile = new String[0];
		initMenu();
		initPanel();
		initArray();
		setBounds(200, 200, 1000, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		load(pref.get(PATH_KEY, null));
		//printFile();
		
		addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent e) {
				    if (loadedFilePath != null && !loadedFilePath.isEmpty() && fileHasChanged(loadedFilePath)) {
						/*int confirmed = JOptionPane.showConfirmDialog(null, 
										        "Are you sure you want to exit the program?", "Exit Program Message Box",
										        JOptionPane.YES_NO_OPTION);*/
						int confirmed = JOptionPane.showConfirmDialog(
								new JFrame(), "Save before exiting?");
						if (confirmed == JOptionPane.YES_OPTION) {
							//dispose();
							setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							save();
						} else if (confirmed == JOptionPane.NO_OPTION) {
							setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						} else if (confirmed == JOptionPane.CANCEL_OPTION) {
							setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						}
					} else {
						setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
				  }
				});
		
		setVisible(true);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean load(){
		String filePath = JOptionPane.showInputDialog(new JFrame(), "Enter filepath to load: ");
		if (filePath != null && !filePath.isEmpty()){
			return load(filePath);
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Could not load " + filePath);
			return false;
		}
	}
	
	
	

	private boolean load(String filePath){
		boolean shouldContinue = true;
		if (filePath != null && !filePath.isEmpty()){
			File f = new File(filePath);
			if(f.exists() && !f.isDirectory()){ //file exists ... attempt to load file?
				if (loadedFilePath != null && !loadedFilePath.isEmpty()){
					if (filePath == loadedFilePath && fileHasChanged(filePath)){
						//show dialog..."Save before reloading" + filePath + "?"...if yes save...if no do nothing...if cancel shouldContinue = false;
					}
				}
				if (shouldContinue) {
					try {
						ReadFile read = new ReadFile(filePath);
						theFile = read.openFile();
						populateFromFile();
						//printFile();
						JOptionPane.showMessageDialog(new JFrame(), "File "
								+ filePath + " loaded successfully.");
						if (!hasLoadedOrCreatedFile) {
							enableButtons();
							hasLoadedOrCreatedFile = true;
						}
						pref.put(PATH_KEY, filePath);
						loadedFilePath = filePath;
						return true;
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(new JFrame(),
								"Could not load " + filePath);
						return false;
					}
				} else {
					return false;
				}
				
				
				
			} else { // file does not exist
				int choice = (int)JOptionPane.showOptionDialog(new JFrame(), "File " + filePath + " does not exist. Attempt to create file?", null, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[] {"Yes","No"}, "Yes");
				if (choice == JOptionPane.NO_OPTION){ //no
					return false;
				} else if (choice == JOptionPane.YES_OPTION){//yes
					//System.out.println("yes");
					return createAndLoad(filePath);
				} else {
					return false;
				}
			}
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Could not load " + filePath);
			return false;
		}
	}
	
	
	
	
	
	
	private void populateFromFile(){
		//System.out.println("File length: " + theFile.length);
		for (int i = 0; i < theFile.length; i++){
			boolean selected = false;
			boolean finished = false;
			String label = "";
			
			String line = theFile[i];
			
			try {
				if (!line.isEmpty() && line != null && !line.equals("\n")){
					//System.out.println(line);
					
					
					String thing = line.substring(1, line.indexOf(',')-1);
					//System.out.println("thing" + thing);
					if (thing.equals("selected")){
						selected = true;
					}
					
					
					line = line.substring(line.indexOf(',')+1);
					thing = line.substring(1, line.indexOf(',')-1);
					//System.out.println("thing" + thing);
					if (thing.equals("Finished")){
						finished = true;
					}
					
					line = line.substring(line.indexOf(',')+1);
					thing = line.substring(1, line.length()-1);
					label = thing;
					
					//System.out.println("thing" + thing);
					//thing = thing.substring(1, thing.indexOf('"'));
					//System.out.println("\n");
					//System.out.println("selected " + selected);
					//System.out.println("finished " + finished);
					//System.out.println("label " + label);
					//System.out.println("\n");
					
					addRow(selected, finished, label);
					
				}
			} catch (StringIndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}
	}
	
	
	
	
	
	
	
	
	private boolean fileHasChanged(String filePath){
		
		try {
			ReadFile read = new ReadFile(filePath);
			String[] newFile = read.openFile();
			String newFileString = "";
			for (int i = 0; i < newFile.length; i++){
				newFileString += newFile[i] + "\n";
			}
			if (newFileString.equals(this.toString())){
				return false;
			} else {
				return true;
			}
		} catch (IOException ex) {
			return true;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean createAndLoad(){
		String filePath = JOptionPane.showInputDialog(new JFrame(), "Enter filepath to create: ");
		if (filePath != null && !filePath.isEmpty()){
			return createAndLoad(filePath);
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Could not create " + filePath);
			return false;
		}
	}
	
	private boolean createAndLoad(String fileName){
		File f = new File(fileName);
		if(!f.exists() || f.isDirectory()) { //file does not exist
			
			try {
				WriteFile write = new WriteFile(fileName);
				write.writeToFile("asdf");
				load(fileName);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(new JFrame(), "Could not create " + fileName);
			}
		} else {
			int choice = (int)JOptionPane.showOptionDialog(new JFrame(), "File " + fileName + " exists. Attempt to load file?", null, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[] {"Yes","No"}, "Yes");
			if (choice == JOptionPane.NO_OPTION){ //no
				
			} else if (choice == JOptionPane.YES_OPTION){//yes
				//System.out.println("yes");
				load(fileName);
			}
		}
		
		if (!hasLoadedOrCreatedFile){
			enableButtons();
			hasLoadedOrCreatedFile = true;
		}
		return true;
	}
	
	private void enableButtons(){
		save.setEnabled(true);
	}
	
	
	
	
	public void printFile(){
		for (String line: theFile){
			System.out.println(line);
		}
	}
	
	
	
	
	
	private void save(){
		saveAs(loadedFilePath);
	}
	
	private void saveAs(String filePath){
		try {
			WriteFile write = new WriteFile(filePath);
			write.writeToFile(this.toString());
		} catch (IOException ex){
			JOptionPane.showMessageDialog(new JFrame(), "Could not save to " + filePath);
		}
	}
	
	
	
	/*public void initFrame(){
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JTextArea text = new JTextArea();
		JMenuBar bar = new JMenuBar();
		JButton load = new JButton("Load File");
		JButton create = new JButton("Create File");
		save = new JButton("Save File");
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				save();
			}
		});
		bar.add(save);
		save.setEnabled(false);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				load();
			}
		});
		bar.add(load);
		
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				createAndLoad();
			}
		});
		bar.add(create);
		
		
		panel.add(text);
		frame.setContentPane(panel);
		frame.setJMenuBar(bar);
		frame.setBounds(100,100,500,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent e) {
				    if (loadedFilePath != null && !loadedFilePath.isEmpty() && fileHasChanged(loadedFilePath)) {
						int confirmed = JOptionPane.showConfirmDialog(null, 
										        "Are you sure you want to exit the program?", "Exit Program Message Box",
										        JOptionPane.YES_NO_OPTION);
						int confirmed = JOptionPane.showConfirmDialog(
								new JFrame(), "Save before exiting?");
						if (confirmed == JOptionPane.YES_OPTION) {
							//dispose();
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							save();
						} else if (confirmed == JOptionPane.NO_OPTION) {
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						} else if (confirmed == JOptionPane.CANCEL_OPTION) {
							frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						}
					} else {
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
				  }
				});
		frame.setVisible(true);
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	

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
		
		save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				save();
			}
		});
		theJMenuBar.add(save);
		
		JButton load = new JButton("Load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				load();
			}
		});
		theJMenuBar.add(load);
		
		JButton create = new JButton("Create");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){
				createAndLoad();
			}
		});
		theJMenuBar.add(create);
		
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
		//System.out.println(this);
	}
	
	private void addRow(boolean selected, boolean finished, String text){
		if (text != null && !text.isEmpty()){
			AgendaRow newRow = new AgendaRow(selected, finished, text);
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String toString(){
		String toReturn = "";
		for (AgendaRow line: rows){
			toReturn += line.toString();
			toReturn += "\n";
		}
		return toReturn;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
