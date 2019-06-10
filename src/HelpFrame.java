package de.luca.snake;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HelpFrame extends JFrame{
	
	public HelpFrame(){
		setTitle("Snake - Anleitung");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(200, 100);
		setResizable(false);
		setSize(new Dimension(550, 300));
		
		JTextArea general = new JTextArea();
		general.setText("Das Spiel wird mit \"SPACE\" gestartet."
				+ "\n\nDie roten Bälle sind Äpfel und geben jeweils 10 Punkte."
				+ "\nDie blauen Bälle sind Items."
				+ "\nDas \"Speed-Item\" gibt dir für 10 Sekunden 20% mehr Geschwindigkeit."
				+ "\nDas \"No-Death-Item\" gibt dir für 10 Sekunden Unsterblchkeit.. Die Schlange wird hierbei grün.");
		general.setBackground(new Color(160,160,160));
		general.setForeground(new Color(0, 0, 0));
		
		JTextArea controls = new JTextArea();
		controls.setText("Die Steuerung erfolgt mittels der Tasten:"
				+ "\n\"W\" - nach oben"
				+ "\n\"A\" - nach links"
				+ "\n\"S\" - nach unten"
				+ "\n\"D\" - nach rechts "
				+ "\n\"SPACE\" - zum Neustarten nach dem Tod");
		controls.setBackground(new Color(160,160,160));
		controls.setForeground(new Color(0, 0, 0));
		
		JPanel panel = new JPanel();
		panel.add(general);
		panel.add(controls);
		panel.setBackground(new Color(160,160,160));
		add(panel);
		
	}

}
