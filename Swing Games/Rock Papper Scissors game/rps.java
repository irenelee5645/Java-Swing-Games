

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class MyFrame extends JFrame implements ActionListener{
	JLabel result=new JLabel("...",SwingConstants.CENTER);
	int mychoice;
	int opponent;
	JPanel resultpic=new JPanel();
	JPanel pic1=new JPanel();
	JPanel pic2=new JPanel();
	MyFrame(){
		setLayout(new GridLayout(4,1));
		setTitle("Rock Paper Scissor");
		
		//instruction
		JLabel instruct=new JLabel("choose one",SwingConstants.CENTER);
		instruct.setOpaque(true);
		instruct.setBackground(Color.white);
		//setbutton;
		JButton buttons[]=new JButton[]{
				new JButton("Rock"),
				new JButton("Paper"),
				new JButton("Scissor")
		};
		JPanel p1=new JPanel();
		p1.setLayout(new GridLayout(1,3,10,10));
		p1.setPreferredSize(new Dimension(300,100));
		for(int i=0;i<3;i++)
			{
			buttons[i].addActionListener(this);
			p1.add(buttons[i]);
			}
		resultpic.setLayout(new GridLayout(1,2,10,10));
		resultpic.setPreferredSize(new Dimension(300,150));
		resultpic.add(pic1);
		resultpic.add(pic2);
		add(instruct);
		add(p1);
		add(resultpic);
		result.setOpaque(true);
		result.setBackground(Color.white);
		add(result);
		setVisible(true);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s=e.getActionCommand();
		
		if(s.equals("Rock"))
			mychoice=0;
		else if(s.equals("Paper"))
			mychoice=1;
		else
			mychoice=2;
		opponent=((int)(Math.random()*3));
		
		int whowon=showresult(mychoice,opponent);
		if(whowon==0)
			result.setText("tie"+mychoice+" "+opponent);
		else if(whowon==1)
			result.setText("you won!"+ mychoice+" "+opponent);
		else
			result.setText("you lost..."+mychoice+" "+opponent);
		
	}
	public void addpic(int me,int comp){
		BufferedImage img1=null;
		BufferedImage img2=null;
		BufferedImage img3=null;
		try{
			img1=ImageIO.read(new File("rock.png"));
			img1=ImageIO.read(new File("paper.png"));
			img1=ImageIO.read(new File("scissors.png"));
		}catch(Exception e){
			System.out.println("file could not be found...");
		}
		
	}
	public int showresult(int me, int comp){
		//same=0 melose=-1 mewin=1
		if(me==comp)
			return 0;
		if(me<comp &&comp-me==1)
			return -1;
		if(me>comp &&me-comp==1)
			return 1;
		if(me==2 && comp==0)
			return -1;
		if(me<comp)
			return 1;
		else 
			return 0;
		
		
	}
}


public class rps {
public static void main(String args[]){
	MyFrame mm=new MyFrame();
}
}
