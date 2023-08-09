package FlappyBirdPackage;

import java.awt.Graphics;

import javax.swing.JPanel;

public class render extends JPanel{ 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g); //Initial image
		flappyBird.fb.paint(g); //Updates positions
	}
	
}
