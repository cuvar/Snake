package de.luca.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class game extends JPanel implements KeyListener, ActionListener{
	
	private int width = 800;
	private int height = 500;
	
	private int snakeWidth = 20;
	private int xPlayer[] = new int [width*height / (snakeWidth * snakeWidth)];
	private int yPlayer[] = new int [width*height / (snakeWidth * snakeWidth)];
	private int xApple;
	private int yApple;
	private int xItem;
	private int yItem;
	private int item;
	private float vel = 1.0f;
	
	private int direction = 0;	//0=hoch; 1=runter; 2=links; 3=rechts
	private int points = 0;
	private int highscore;
	
	private Timer timer;
	public static Color background = new Color(120,120,120);
	private int tailWidth = 3;
	private JLabel label;
	private JLabel labelP;
	private JLabel labelH;
	private boolean isDead = false;
	private boolean hasItemNoDeath = false;
	private boolean hasItemSpeed = false;
	
	private HelpFrame frame;
	
	//Konstruktor
	public game(){
		addKeyListener(this);
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		setBackground(background);
		
		label = new JLabel(" Drücke \"SPACE\", um das Spiel zu starten. Drücke \"H\" für die Anleitung.");
		label.setLocation(width/2 - 100, 20);
		label.setSize(new Dimension(200, 100));
		add(label);
		
		timer = new Timer(70, this);
		timer.start();
		posApple();
		posItem();
	}
	
	
	/*
	 * 
	*LISTENER
	*
	*/
	
	//ActionListener
	@Override
	public void actionPerformed(ActionEvent e) {			
		
		moveSnake();
		hitApple();
		checkDeath();
		hitItem();
		repaint();
		
		if(isDead) {
			if(highscore < points){
				highscore = points;
			}
			
			
			
			labelP = new JLabel(); 
			labelP.setLocation(width/2 - 85, 30);
			labelP.setSize(400, 20);
			labelH = new JLabel(); 
			labelH.setSize(400, 20);
			labelH.setLocation(width/2 - 70, 60);
		
			labelP.setText(" Deine erreichten Punkte: " + points);
			labelH.setText(" Dein Highscore: " + highscore);
		
			add(label);
			add(labelP);
			add(labelH);
		}
	
	}
	
	//KeyListener
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_W && direction != 1){
			direction = 0;
		}
		if(key == KeyEvent.VK_S && direction != 0){
			direction = 1;	
		}
		if(key == KeyEvent.VK_A && direction != 3){
			direction = 2;
		}
		if(key == KeyEvent.VK_D && direction != 2){
			direction = 3;
		}
		if(key == KeyEvent.VK_H){
			help();
		}
		
		//Exit aus Spiel
		if(key == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
		//Neustart
		if(key == KeyEvent.VK_SPACE){
			timer.start();
			direction = 0;
			points = 0;
			isDead = false;
			hasItemNoDeath = false;
			hasItemSpeed = false;
			remove(label);
			vel = 1;
			if(labelP != null){				
				remove(labelP);
			}
			if(labelH != null){				
				remove(labelH);
			}
		}
		
	}

	


	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	
	
	
	
	/*
	 * 
	*METHODEN
	*
	*/
	
	//Anleitung
	private void help() {
		frame = new HelpFrame();
		frame.setVisible(true);
		
	}
	
	//Bewegung der Snake
	private void moveSnake() {
		for(int i = tailWidth; i > 0; i--) {
			xPlayer[i] = xPlayer[i-1];
			yPlayer[i] = yPlayer[i-1];
		}	
		//0=hoch; 1=runter; 2=links; 3=rechts
		switch(direction) {
		case 0:
			yPlayer[0] -= (snakeWidth) * vel; //hoch
			break;
		case 1:
			yPlayer[0] += (snakeWidth) * vel; //runter
			break;
		case 2:
			xPlayer[0] -= (snakeWidth) * vel; //links
			break;
		case 3:
			xPlayer[0] += (snakeWidth) * vel; //rechts
			break;
		default:
			break;
		}	
	}



	private void checkDeath() {
		//Wenn Snake sich selber berührt
		for(int i = 1; i < tailWidth; i++) {
			if(hasItemNoDeath == false){	
				if(xPlayer[0] == xPlayer[i] && yPlayer[0] == yPlayer[i]) {
					for(int j =0; j<tailWidth; j++) {
						xPlayer[j] = width/2;
						yPlayer[j] = height/2 + (j*20);
						
						isDead  = true;
						hasItemNoDeath = false;
						hasItemSpeed = false;
						tailWidth = 3;
						timer.stop();
						direction = 0;
						posApple();
						posItem();
					}
				}
			}
			if(hasItemNoDeath == true){
				
			}
		}
		
		//Wenn Snake den Rand berührt
		if(yPlayer[0] <= 0 || yPlayer[0] >= height - 20 || xPlayer[0] <= 0 || xPlayer[0] >= width -20) {
			for(int i =0; i<tailWidth; i++) {
				xPlayer[i] = width/2;
				yPlayer[i] = height/2 + (i*20);
			}
			if(hasItemNoDeath == false) {
				isDead  = true;
				hasItemNoDeath = false;
				tailWidth = 3;
				timer.stop();
				direction = 0;
				posApple();
				posItem();
			}
			if(hasItemNoDeath == true) {
				//Oberer Rand
				if(yPlayer[0] <= 0) {
					for(int i =0; i<tailWidth; i++) {
						if(direction == 0) {
							yPlayer[0] = 0;
						}
					}
				}
				//Unterer Rand
				if(yPlayer[0] >= height - 20) {
					for(int i =0; i<tailWidth; i++) {
						if(direction == 1) {
							yPlayer[0] = height - 20;
						}
					}
				}
				
				//Linker Rand
				if(xPlayer[0] <= 0) {
					for(int i =0; i<tailWidth; i++) {
						if(direction == 2) {
							yPlayer[0] = 0;
						}
					}
				}
				//Rechter Rand
				if(xPlayer[0] >= width -20 ) {
					for(int i =0; i<tailWidth; i++) {
						if(direction == 3) {
							yPlayer[0] = width -20;
						}
					}
				}
			}
		}		
	}

	//Wenn Snake den Apfel frisst
	private void hitApple() {
		for(int i = 0; i <= tailWidth; i++) {
			if(yPlayer[i] <= yApple + 20 && yPlayer[i] >= yApple || yPlayer[i] +20 <= yApple +20 && yPlayer[i] +20 >= yApple) {
				if(xPlayer[i] <= xApple + 20 && xPlayer[i] >= xApple || xPlayer[i] +20 <= xApple +20 && xPlayer[i] +20 >= xApple) {
					posApple();
					tailWidth ++;
					points+=10;
				}
			}
		}
		
		
	}
		
	
	//Position des Apfels
	private void posApple() {
		double rx = (Math.random() * 770) +5 ;
		xApple = (int) rx;
		double ry = (Math.random() * 470) +5;
		yApple = (int) ry;
		
	}
	
	//Position des Items
	private void posItem() {
		double ix = (Math.random() * 770) +5 ;
		xItem = (int) ix;
		double iy = (Math.random() * 470) +5;
		yItem = (int) iy;
		
		double item2 = Math.random();
		if(item2 < 0.5) {
			item = 0;
		}
		if(item2 > 0.5) {
			item = 1;
		}
	}
	
	//Wenn Item berührt
	private void hitItem() {
		for(int i = 0; i <= tailWidth; i++) {
			if(yPlayer[i] <= yItem + 20 && yPlayer[i] >= yItem || yPlayer[i] +20 <= yItem +20 && yPlayer[i] +20 >= yItem) {
				if(xPlayer[i] <= xItem + 20 && xPlayer[i] >= xItem || xPlayer[i] +20 <= xItem +20 && xPlayer[i] +20 >= xItem) {
					switch(item) {
					case 0:		// + 20% Speed
						if (hasItemSpeed == false) {
							setItemSpeed();
						}
						if (hasItemSpeed == true) {
							vel = vel;
						}
						break;
					case 1:		//Kein Tod durch Wände
						setItemNoDeathByWalls();
						break;
					default:
						break;
					}					
					posItem();
				}
			}
		}
	}
	
	//Item für Geschwindigkeit
	private void setItemSpeed() {
		hasItemSpeed = true;
		vel += 0.2;
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		ScheduledFuture<?> countdown = scheduler.schedule(new Runnable() {
			@Override
		    public void run() {
				vel = 1;
				hasItemSpeed = false;
		    }}, 10, TimeUnit.SECONDS);
		    scheduler.shutdown();
	}
	
	//Kein Tod, wenn Wand berührt
	private void setItemNoDeathByWalls() {
		hasItemNoDeath = true;
		ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
		ScheduledFuture<?> countdown2 = scheduler2.schedule(new Runnable() {
			@Override
		    public void run() {
				hasItemNoDeath = false;
		    }}, 10, TimeUnit.SECONDS);
		    scheduler2.shutdown();
		    
		//Oberer Rand
		if(yPlayer[0] <= 0) {
			for(int i =0; i<tailWidth; i++) {
				yPlayer[0] =height/2;
				xPlayer[0] = width/2; 
			}
		}
		//Unterer Rand
		if(yPlayer[0] + snakeWidth>= height - 20) {
			for(int i =0; i<tailWidth; i++) {
				yPlayer[0] =height/2;
				xPlayer[0] = width/2; 
			}
		}
		
		//Linker Rand
		if(xPlayer[0] <= 0) {
			for(int i =0; i<tailWidth; i++) {
				yPlayer[0] =height/2;
				xPlayer[0] = width/2; 
			}
		}
		//Rechter Rand
		if(xPlayer[0] + snakeWidth >= width ) {
			for(int i =0; i<tailWidth; i++) {
					yPlayer[0] =height/2;
					xPlayer[0] = width/2; 
				
			}
		}
	}
	
	
	/*
	 * 
	 *ZEICHNET
	 *ELEMENTE
	 * 
	 */	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Snake
		for(int i = 0; i<tailWidth; i++) {
			if(hasItemNoDeath) g.setColor(Color.green);	
			if(hasItemNoDeath == false) g.setColor(Color.black);
			g.fillRect(xPlayer[i], yPlayer[i], snakeWidth, snakeWidth);
		}
		//Apple
		g.setColor(Color.red);
		g.fillOval(xApple, yApple, 20, 20);
		//Item
		g.setColor(Color.blue);
		g.fillOval(xItem, yItem, 20, 20);
		Toolkit.getDefaultToolkit().sync();

	}
	
}
