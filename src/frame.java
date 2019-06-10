package de.luca.snake;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class frame extends JFrame{
	
	public frame(){
		add(new game());
		
		setTitle("Snake");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(200, 100);
		setResizable(false);
		
		pack();
		
	}
	

	
	public static void main(String[] args) {
		frame frm = new frame();
		frm.setVisible(true);
		
		
	}

}
