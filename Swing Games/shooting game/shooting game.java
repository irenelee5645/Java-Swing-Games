package p6;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class MyFrame extends JFrame{
	static final int frameSize=400;
	MyFrame(){
		GamePane mypane1=new GamePane();
		add(mypane1,"North");
		//setPreferredSize(new Dimension(frameSize,frameSize));
		pack();
		this.setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
}


class GamePane extends JPanel implements Runnable,KeyListener{
	Chicken chick1;
	float timeelapsed;
	Shotgun gun1;
	boolean hit=false;
	List<Bullet> bullets=new LinkedList<Bullet>();
	 Thread mythread1=new Thread(this);
	 int paintcount=30;
	GamePane(){
		setPreferredSize(new Dimension(MyFrame.frameSize,MyFrame.frameSize));
		timeelapsed=System.currentTimeMillis();
		 chick1=new Chicken();
		 gun1=new Shotgun();
		 setFocusable(true);
		 requestFocus();
		 this.addKeyListener(this);
		 mythread1.start();
	}

	@Override
	public void run() {
	//	Thread chickthread=new Thread(chick1);
	//	chickthread.start();
		while(true){
			chick1.update();
		for(int i=0;i<bullets.size();i++)
			{
			bullets.get(i).update();
			if(bullets.get(i).y<0)
				{
				bullets.remove(i);
				continue;
				}
			int nowx=bullets.get(i).x;
			int nowy=bullets.get(i).y;
			if(nowy<=chick1.imgSize){
				if(nowx>=chick1.x&&nowx<=chick1.x+chick1.imgSize)
				{
					hit=true;
					
					
				}
			}
			
			}
		if(hit){
			timeelapsed=System.currentTimeMillis()-timeelapsed;

			for(paintcount=0;paintcount<100;paintcount++)
			{
				repaint();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(null,  "You got the Chicken!"/* Time elapsed(ms) : "*//*+timeelapsed/1000*/,"Game Status", JOptionPane.PLAIN_MESSAGE);				
			timeelapsed=System.currentTimeMillis();
			hit=false;
		}
		repaint();
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Runnable hitrun=new Runnable(){

			@Override
			public void run() {
				int hitx=chick1.x+chick1.imgSize/2;
				int hity=chick1.y+chick1.imgSize/2;
				int confettiSize=10;
				int colors[]=new int[3];
				int di=1;
				Color confettiColor[]=new Color[8];
				
					for(int k=0;k<confettiColor.length;k++){
					for(int j=0;j<3;j++){
						colors[j]=((int)(Math.random()*256));
					}
					confettiColor[k]=new Color(colors[0],colors[1],colors[2]);
					}
					g.setColor(confettiColor[0]);
					g.fillOval(hitx-(paintcount*di), hity, confettiSize, confettiSize);
					g.setColor(confettiColor[1]);
					g.fillOval(hitx-(paintcount*di), hity+(paintcount*di), confettiSize, confettiSize);
					g.setColor(confettiColor[2]);
					g.fillOval(hitx, hity+(paintcount*di), confettiSize, confettiSize);
					g.setColor(confettiColor[3]);
					g.fillOval(hitx+(paintcount*di), hity+(paintcount*di), confettiSize, confettiSize);
					g.setColor(confettiColor[4]);
					g.fillOval(hitx+(paintcount*di), hity, confettiSize, confettiSize);
					g.setColor(confettiColor[5]);
					g.fillOval(hitx+(paintcount*di), hity-(paintcount*di), confettiSize, confettiSize);
					g.setColor(confettiColor[6]);
					g.fillOval(hitx, hity-(paintcount*di), confettiSize, confettiSize);
					g.setColor(confettiColor[3]);
					g.fillOval(hitx-(paintcount*di), hity-(paintcount*di), confettiSize, confettiSize);
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

				
			}
			
		};
		if(hit){
			hitrun.run();
			return;
		}
		
		//hit=false;
		g.setColor(Color.black);
		
		g.drawImage(chick1.img.getImage(), chick1.x, chick1.y, chick1.x+chick1.imgSize, chick1.y+chick1.imgSize, 0, 0, chick1.img.getIconWidth(), chick1.img.getIconWidth(), this);
		g.fillRect(gun1.x, gun1.y, gun1.mainBoxSize, gun1.mainBoxSize);
		g.setColor(Color.RED);
		g.fillRect(gun1.x+(gun1.mainBoxSize-gun1.smallBoxSize)/2, gun1.y-gun1.smallBoxSize, gun1.smallBoxSize, gun1.smallBoxSize);
		g.setColor(Color.red);
		for(int i=0;i<bullets.size();i++)
			g.fillOval(bullets.get(i).x, bullets.get(i).y, bullets.get(i).size, bullets.get(i).size);
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode=e.getKeyCode();
		if(keycode==KeyEvent.VK_LEFT){
			if(gun1.x>0)
				gun1.x-=gun1.dx;
		}
		else if(keycode==KeyEvent.VK_RIGHT)
		{
			if(gun1.x<MyFrame.frameSize-gun1.mainBoxSize)
				gun1.x+=gun1.dx;
		}
		else if(keycode==KeyEvent.VK_SPACE)
			bullets.add(new Bullet(gun1.x+(gun1.mainBoxSize-gun1.smallBoxSize)/2, gun1.y-gun1.smallBoxSize, gun1.smallBoxSize));		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		int keycode=e.getKeyCode();
		if(keycode==KeyEvent.VK_LEFT){
			if(gun1.x>0)
				gun1.x-=gun1.dx;
		}
		else if(keycode==KeyEvent.VK_RIGHT)
		{
			if(gun1.x<MyFrame.frameSize-gun1.mainBoxSize)
				gun1.x+=gun1.dx;
		}
		else if(keycode==KeyEvent.VK_SPACE)
			bullets.add(new Bullet(gun1.x+(gun1.mainBoxSize-gun1.smallBoxSize)/2, gun1.y-gun1.smallBoxSize, gun1.smallBoxSize));
	}
	
}


class Chicken/* implements Runnable*/{
	int x,y;
	int dx=10;
	int imgSize=40;
	ImageIcon img=new ImageIcon("chicken.jpg");
	Chicken(){
		x=MyFrame.frameSize/5;
		y=0;
	}
	public void update(){
		//g.drawImage(img.getImage(), 0, 0, 40, 40, 0, 0, img.getIconWidth(), img.getIconHeight(), this);
		if(x<=0+imgSize||x>=400-imgSize)
			dx=-dx;
		x+=dx;
	}
//	@Override
//	public void run() {
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		this.update();
//	}
	
}

class Shotgun{
	int x,y;
	int mainBoxSize=40;
	int smallBoxSize=10;
	int dx=5;
	Shotgun(){
		x=(MyFrame.frameSize-mainBoxSize)/2;
		y=MyFrame.frameSize-mainBoxSize;
	}
}

class Bullet{
	int x,y;
	int dy=-30;
	int size;
	Bullet(int x,int y,int size){
		this.x=x;
		this.y=y;
		this.size=size;
	}
	public void update(){
		y+=dy;
	}
}

public class p6 {

	public static void main(String[] args) {
		MyFrame mm=new MyFrame();
	}

}
