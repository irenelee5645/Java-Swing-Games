

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MyButton extends JButton{
	static int count=0;
	int index;
	int boxp=0;
	MyButton(String s){
		super(s);
		index=count++;
	}
	MyButton(){
		super("");
		index=count++;
	}
}



class MyFrame extends JFrame implements MouseListener{
	MyButton []buttons=new MyButton[9];
	boolean bingo=false;
	boolean players=false;
	boolean done=false;
	Vector<Integer> playerchecked[]=new Vector[]{
			new Vector(),
			new Vector()
	};
	JLabel pp=new JLabel("player 1");

	MyFrame(int gridSize){
		buttons = new MyButton[gridSize * gridSize];
		setTitle("Bingo!!");
		JPanel panel1=new JPanel();
		panel1.setLayout(new GridLayout(gridSize,gridSize,5,5));
		for(int i=0;i<buttons.length;i++){
			buttons[i]=new MyButton(" ");
			buttons[i].setPreferredSize(new Dimension(60,60));
			buttons[i].addMouseListener(this);
			panel1.add(buttons[i]);
			
		}
		JPanel panel2=new JPanel();
		pp.setFont(new Font("Arial",Font.ITALIC,40));
		pp.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel2.add(pp);
		add(panel1,"Center");
		add(panel2,"South");
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(done){
			try {
				Toolkit kit=Toolkit.getDefaultToolkit();
				kit.beep();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
		MyButton nowplay= (MyButton)arg0.getSource();
		boxcheck(nowplay);
		
		if(!players)
			pp.setText("Player 1");
		else
			pp.setText("Player 2");
		if(bingoCheck()){
			if(!players)
				pp.setText("Player 1 won");
			else
				pp.setText("Player 2 won");
			done=true;
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void boxcheck(MyButton b){
		int whichp;
		Color cc;
		if(!players)
			{
			whichp=1;
			cc=Color.YELLOW;
			}
		else
			{
			whichp=2;
			cc=Color.RED;
			}
		if(b.boxp==0){
			b.boxp=whichp;
			b.setBackground(cc);
			b.setText("P"+ whichp);
			playerchecked[whichp-1].addElement(b.index);
			players=!players;
			return;
		}
		
	}
	
	public boolean bingoCheck(){
		int whichp;
		if(!players)
			whichp=1;
		else
			whichp=2;
		Vector <Integer> nowboxes=playerchecked[whichp-1];
		if(nowboxes.size()<5)
			return false;
		if(sideCheck(nowboxes))
			return true;
		if(leftDownCheck(nowboxes))
			return true;
		if(downCheck(nowboxes))
			return true;
		if(RightDownCheck(nowboxes))
			return true;
		else
			return false;
		
	}
	public boolean downCheck(Vector<Integer> aa){
		int j;
		int index;
		int bingorow[]=new int[5];
		for(int i=0;i<aa.size();i++){
			index=aa.elementAt(i);
			for( j=1;j<5;j++){
				index+=10;
				if(aa.contains(Integer.valueOf(index)))
					{bingorow[j]=index;
					continue;
					}
				else
					break;
			}
			if(j==5)
				{
				bingorow[0]=aa.elementAt(i);
				for(int k=0;k<5;k++){
					buttons[bingorow[k]].setBackground(Color.cyan);
				}
				return true;
				}
		}
		return false;
	}
	public boolean sideCheck(Vector<Integer> aa){
		int j;
		int index;
		int bingorow[]=new int[5];
		for(int i=0;i<aa.size();i++){
			index=aa.elementAt(i);
			for( j=1;j<5;j++){
				if(aa.contains(Integer.valueOf(++index)))
					{bingorow[j]=index;
					continue;
					}
				else
					break;
			}
			if(j==5)
				{
				bingorow[0]=aa.elementAt(i);
				for(int k=0;k<5;k++){
					buttons[bingorow[k]].setBackground(Color.cyan);
				}
				return true;
				}
		}
		return false;
	}
	public boolean leftDownCheck(Vector<Integer> aa){
		int j;
		int index;
		int bingorow[]=new int[5];
		for(int i=0;i<aa.size();i++){
			index=aa.elementAt(i);
			for( j=1;j<5;j++){
				if(aa.contains(Integer.valueOf((index+=11))))
					{bingorow[j]=index;
					continue;
					}
				else
					break;
			}
			if(j==5)
				{
				bingorow[0]=aa.elementAt(i);
				for(int k=0;k<5;k++){
					buttons[bingorow[k]].setBackground(Color.cyan);
				}
				return true;
				}
		}
		return false;
	}
	public boolean RightDownCheck(Vector<Integer> aa){
		int j;
		int index;
		int bingorow[]=new int[5];
		for(int i=0;i<aa.size();i++){
			index=aa.elementAt(i);
			for( j=1;j<5;j++){
				if(aa.contains(Integer.valueOf((index+=9))))
					{bingorow[j]=index;
					continue;
					}
				else
					break;
			}
			if(j==5)
				{
				bingorow[0]=aa.elementAt(i);
				for(int k=0;k<5;k++){
					buttons[bingorow[k]].setBackground(Color.cyan);
				}
				return true;
				}
		}
		return false;
	}	
}


public class bingogame {
public static void main(String args[]){
	MyFrame mm=new MyFrame(5);
}
}
