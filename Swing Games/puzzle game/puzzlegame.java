
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class MyFrame extends JFrame implements ActionListener, KeyListener {
	ImageIcon icon=new ImageIcon("flowerpic.jpg");
	// 	ImageIcon icon=new ImageIcon("flowerpic.jpg");
	Image img=icon.getImage();
	int numr,numc;
	int blankspace_loc;
	Mypic picpanels[];
	JPanel panel1=new JPanel();

	MyFrame(){
		numr=4;
		numc=4;
		picpanels=new Mypic[numr*numc];
		panel1.setSize(img.getWidth(this),img.getHeight(this));
		panel1.setLayout(new GridLayout(numr,numc));
		for(int i=0;i<picpanels.length;i++){
			picpanels[i]=new Mypic();
			picpanels[i].location=i;
			picpanels[i].index=i;
			//picpanels[i].setSize(picpanels[i].w, picpanels[i].h);
			panel1.add(picpanels[i]);
		}
		panel1.addKeyListener(this);
		panel1.setFocusable(true);
		panel1.requestFocus();
		JButton shufflebutton= new JButton("shuffle");
		shufflebutton.setSize(panel1.getWidth(), 50);
		shufflebutton.addActionListener(this);
		add(panel1,"Center");
		add(shufflebutton,"South");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(panel1.getWidth(),panel1.getHeight()+50);

		setVisible(true);
	}
	
	class Mypic extends JPanel{
		int location=0;
		int index=0;	
		
		int w=img.getWidth(this)/numc;
		int h=img.getHeight(this)/numr;
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			setPreferredSize(new Dimension(w,h));
			if(index==0){
				blankspace_loc=location;
				return;

			}
			int a,b;
			a=index%numc;
			b=index/numc;
			g.drawImage(img, 0, 0, w, h, a*w, b*h, w*(a+1), h*(b+1), this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for(int i=0;i<picpanels.length;i++){
			int temp=picpanels[0].index;
			int randomnum=(int)(Math.random()*(numr*numc));
			picpanels[0].index=picpanels[randomnum].index;
			picpanels[randomnum].index=temp;
			
		}
		for(int i=0;i<picpanels.length;i++){
			picpanels[i].repaint();
		}
		panel1.setFocusable(true);
		panel1.requestFocus();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int keycode=arg0.getKeyCode();
		switch(keycode){
		case KeyEvent.VK_UP:
			upb();break;
		case KeyEvent.VK_DOWN:
			downb();break;
		case KeyEvent.VK_RIGHT:
			rightb();break;
		case KeyEvent.VK_LEFT:
			leftb();break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	private void leftb() {
		if(blankspace_loc%numc!=0){
			picpanels[blankspace_loc].index=picpanels[blankspace_loc-1].index;
			picpanels[blankspace_loc-1].index=0;
			picpanels[blankspace_loc].repaint();
			picpanels[blankspace_loc-1].repaint();

		}
	}

	private void rightb() {
		if(blankspace_loc%numc!=numc-1&&blankspace_loc!=(numc*numr)-1){
			
			picpanels[blankspace_loc].index=picpanels[blankspace_loc+1].index;
			picpanels[blankspace_loc+1].index=0;
			picpanels[blankspace_loc].repaint();
			picpanels[blankspace_loc+1].repaint();

		}		
	}

	private void downb() {
		if(blankspace_loc/numc!=numr-1){
			picpanels[blankspace_loc].index=picpanels[blankspace_loc+numc].index;
			picpanels[blankspace_loc+numc].index=0;
			picpanels[blankspace_loc].repaint();
			picpanels[blankspace_loc+numc].repaint();

		}		
	}

	private void upb() {
		if(blankspace_loc/numc!=0){
			picpanels[blankspace_loc].index=picpanels[blankspace_loc-numc].index;
			picpanels[blankspace_loc-numc].index=0;
			picpanels[blankspace_loc].repaint();
			picpanels[blankspace_loc-numc].repaint();

		}				
	}


	
}


public class puzzlegame {

	public static void main(String[] args) {
		MyFrame mm=new MyFrame();
	}

}
