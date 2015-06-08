package agenda2;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class AgendaRow implements ActionListener {
	
	private JCheckBox check;
	private JToggleButton finished;
	private JLabel label;
	
	AgendaRow(){
		this(false, false, "This is a new label.");
	}
	
	AgendaRow(boolean checked, boolean isFinished, String text){
		check = new JCheckBox();
		finished = new JToggleButton("Not Finished");
		label = new JLabel();
		
		//set action listeners
		finished.addActionListener(this);
		//set initial states
		check.setSelected(true);
		if (isFinished){
			finished.doClick();
			//finished.setText("Not Finished");
		}
		label.setText(text);
		
	}
	
	public boolean isFinished(){
		if (finished.getText() == "Finished"){
			return true;
		} else {
			return false;
		}
	}
	
	public void setFinished(boolean newBool){
		if (isFinished() != newBool){
			finished.doClick();
		}
	}
	
	public void setText(String newText){
		label.setText(newText);
	}
	
	
	public void removeSelfFromJPanel(JPanel panel){
		panel.remove(check);
		panel.remove(finished);
		panel.remove(label);
		panel.validate();
		panel.repaint();
	}
	
	public void addSelfToJPanel(JPanel panel, int row){
		GridBagConstraints[] c = new GridBagConstraints[3];
		for (int i = 0; i < c.length; i++){
			c[i] = new GridBagConstraints();
			c[i].gridx = i;
			c[i].gridy = row;
		}
		panel.add(check, c[0]);
		panel.add(finished, c[1]);
		panel.add(label, c[2]);
		panel.validate();
		panel.repaint();
	}
	
	public void setSelected(boolean selected){
		check.setSelected(selected);
	}
	
	public boolean isSelected(){
		return check.isSelected();
	}
	
	
	
	public String toString(){
		String toReturn = "";
		
		if (check.isSelected()){
			toReturn += "\"selected\"";
		} else {
			toReturn += "\"not selected\"";
		}
		toReturn += ",";
		toReturn += "\"" + finished.getText() + "\"";
		toReturn += ",";
		toReturn += "\"" + label.getText() + "\"";
		
		return toReturn;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source == finished){
			if (finished.getText() == "Finished")
				finished.setText("Not Finished");
			else
				finished.setText("Finished");
		}

	}

}