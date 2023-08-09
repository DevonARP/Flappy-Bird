package FlappyBirdPackage; 

//Based on an implementation from this youtube video: https://www.youtube.com/watch?v=I1qTZaUcFX0
//Put my own custom features and spin on it but got the idea from Jaryt

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class flappyBird implements ActionListener, MouseListener, KeyListener{
	
	public static flappyBird fb;
	
	public int width = 1000, height = 1000; //Don't recommend changing
	
	public render render; //Repaints game image
	
	public Rectangle flappy; //Flappy
	
	public ArrayList<Rectangle> pipes; //List of pipes 
	
	public Random rand; //Helps randomize pipe size
	
	public int ticks, y, score, highScore; //Used for adding pipes, controlling speed, and scores
	
	public boolean gameOver, start; //Starts and ends game
	
	@SuppressWarnings("static-access")
	public flappyBird() {
		
		JFrame jf = new JFrame();
		Timer timer = new Timer(10, this); //Speed of scrolling
		
		
		
		render = new render(); 
		rand= new Random(); 
		jf.add(render); //Adding rendering panel to frame
		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		jf.setSize(width,height);
		jf.addMouseListener(this);
		jf.addKeyListener(this);
		jf.setTitle("Flappy Bird");
		jf.setVisible(true);
		
		flappy = new Rectangle(width /2 , height /2 , 20, 20); //He's cute
		pipes = new ArrayList<Rectangle>(); 
		
		addPipe(true); //Starting pipes, easier than the rest
		addPipe(true);
		
		timer.start();
	}
	
	public void paint(Graphics g) { //image of game while updating positions
		// TODO Auto-generated method stub
		g.setColor(Color.cyan); //Background
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.ORANGE.darker()); //Ground
		g.fillRect(0, height - 150,width,150);
		
		g.setColor(Color.green); //Grass
		g.fillRect(0, height - 150,width,20);
		
		g.setColor(Color.red); //Flappy
		g.fillRect(flappy.x, flappy.y, flappy.width, flappy.height);
		
		for (Rectangle pipe : pipes) { //Paints the pipes orange
			paintPipe(g,pipe);
		}
		
		if(gameOver) { //Death screen
			g.setColor(Color.white);
			g.setFont(new Font(Font.SERIF, 1, 90));
			g.drawString("Git Gud!", 330, height / 2);
		}
		
		if(!start) { //Start screen
			g.setColor(Color.white);
			g.setFont(new Font(Font.SERIF, 1, 90));
			g.drawString("Start!", 370, height / 2);
		}
		
		if(!gameOver && start) { //Shows high score
			g.setColor(Color.red);
			g.setFont(new Font(Font.SERIF, 1, 90));
			g.drawString(String.valueOf(highScore), width/4, 90 );
			
			g.setColor(Color.white); //Shows current score
			g.setFont(new Font(Font.SERIF, 1, 90));
			g.drawString(String.valueOf(score), width/2, 90 );  
		}

	}
	
	@Override
	public void actionPerformed(ActionEvent e) { //Mouse click
		// TODO Auto-generated method stub
		
		int speed = 10;
		
		ticks = ticks + 1;
		
		if (start) {
			
			for(int i = 0; i < pipes.size(); i++) {  //Get's initial pipes
				
				Rectangle pipe = pipes.get(i);
				pipe.x -= speed;
				
			}
			
			if (ticks%2 == 0 && y < 10) { //Flappy speed control, will go to Heaven without it
				y = y + 2;
			}
			
			for(int i = 0; i < pipes.size(); i++) { //Get's the pipes past the first initial ones
				
				Rectangle pipe = pipes.get(i);
				
				if (pipe.x + pipe.width < 0) {
					pipes.remove(pipe);
					
					if(pipe.y == 0) {
						addPipe(false);
					}
				}
				
			}
			
			flappy.y = flappy.y + y;
			
			for (Rectangle pipe: pipes){ //Adds a point to the score if it passes the middle of a pipe
				
				if(pipe.y == 0 && flappy.x + flappy.width / 2 > pipe.x + pipe.width /2 - 10 && flappy.x + flappy.width / 2 < pipe.x + pipe.width / 2 + 10) {
					
					score ++;
					
					if (score>highScore) {
						highScore = score;
					}
					
				}
				
				if (flappy.intersects(pipe)) { //Handles collisions
					gameOver = true;
					
					if(flappy.x <= pipe.x) {
						
						flappy.x = pipe.x - flappy.width;
						
					}
					
					else {
						if (pipe.y != 0 ) {
							flappy.y = pipe.y - flappy.height;
						}
						
						else if (flappy.y < pipe.height) {
							flappy.y = pipe.height;
						}
					}
					
					flappy.x = pipe.x - flappy.width;
				}
			}
			
			if(flappy.y > height - 150 || flappy.y < 0) { //Ends game if Flappy falls
				gameOver = true;
			}
			
			if (flappy.y + y >= height - 150) { //Brings Flappy back to where his body landed
				flappy.y = height - 150 - flappy.height;
			}
			

		}

		
		render.repaint();
	}
	
	public void addPipe(boolean first) {
		int space = 350; //Gap for Flappy
		int width2 = 100; 
		int height2 = 50 + rand.nextInt(350); //Randomizes size
		
		if (first) { //Generic first few
			pipes.add(new Rectangle(width + width2 + pipes.size() * 350, height - height2 - 150, width2, height2));
			pipes.add(new Rectangle(width + width2 + (pipes.size() - 1) * 350, 0, width2, height - height2 - space));
		}
		else { //Randomizes rest
			pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 700, height - height2 - 150, width2, height2));
			pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x, 0, width2, height - height2 - space));
		}

		
	}
	
	
	public void paintPipe(Graphics g, Rectangle pipe) {
		
		g.setColor(Color.orange); //Orange pipes
		g.fillRect(pipe.x, pipe.y, pipe.width,pipe.height); //Size
		
	}
	
	
	public static void main(String[] args) {
		
		fb = new flappyBird(); 
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		flap();
	}

	private void flap() { //Let's Flappy flap
		// TODO Auto-generated method stub
		if (gameOver) { //Generates image of starting screen
			
			flappy = new Rectangle(width /2 , height /2 , 20, 20);
			pipes.clear();
			y = 0;
			score = 0;
			
			addPipe(true);
			addPipe(true);
			
			gameOver = false;
		}
		
		if(!start) { //Forces start
			
			start = true;
		}
		
		else if (!gameOver){ //Keeps playing
			if ( y > 0) {
				y = 0;
			}
			
			y -= 10;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_SPACE) { //Space button
			flap();
		}
	}




	

}
