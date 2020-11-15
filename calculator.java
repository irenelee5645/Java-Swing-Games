
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class MyCalculatorFrame extends JFrame implements ActionListener{
	TextField tf1=new TextField(40);
	String calclist[];
	Queue<Double> numbers=new LinkedList<Double>();
	Queue<String> ops=new LinkedList<String>();
	MyCalculatorFrame(){
		setTitle("Calculator");
		setSize(400,200);
		setLocation(500,500);
		setLayout(new BorderLayout(10,20));
		JPanel panels[]=new JPanel[2];
		for(int i=0;i<panels.length;i++)
			panels[i]=new JPanel();
		panels[0].setLayout(new FlowLayout());
		panels[0].add(tf1);
		panels[1].setLayout(new GridLayout(1,4));
		JButton bc=new JButton("C");
		bc.addActionListener(this);
		JButton sqrtb=new JButton("sqrt");
		sqrtb.addActionListener(this);

		JButton sqb=new JButton("^2");
		sqb.addActionListener(this);

		JButton cube=new JButton("^3");
		cube.addActionListener(this);

		panels[1].setLayout(new GridLayout(0,4));
		panels[1].add(bc);
		panels[1].add(sqrtb);
		panels[1].add(sqb);
		panels[1].add(cube);
		String bnames[]=new String[]{
				"7","8","9","/","4","5","6","*","1","2","3","-","0","+/-","=","+"
		};
		
		JButton buttons[]=new JButton[bnames.length];
		for(int i=0;i<buttons.length;i++){
			buttons[i]=new JButton(bnames[i]);
			buttons[i].addActionListener(this);
			panels[1].add(buttons[i]);
		}
		//setResizable(false);
		add(panels[0],"North");
		add(panels[1],"Center");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pack();
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("="))
			{
				tf1.setText(calc(e)+"");
			}
		else if(e.getActionCommand().equals("C"))
			tf1.setText("");
		else if(e.getActionCommand().equals("+/-"))
		{
			String s=tf1.getText();
			s+="_";
			tf1.setText(s);
		}
		else
		{
			String s=tf1.getText();
			s+=e.getActionCommand();
			tf1.setText(s);
		}
	}

	private double calc(ActionEvent e) {
		String temp=tf1.getText();
		StringTokenizer calcc=new StringTokenizer(temp,"+/-*^_",true);
		int count=0;
		calclist=new String[calcc.countTokens()];
		while(calcc.hasMoreTokens()){
			calclist[count++]=calcc.nextToken();
			System.out.println(calclist[count-1]);
		}
		String refine1[]=new String[count];
		count=0;
		try{
		for(int i=0;i<calclist.length-1;i++){
			if(calclist[i].equals("_"))
			{
				i+=1;
				refine1[count++]=(Double.parseDouble(calclist[i])*(-1))+"";
				i++;
			}
			if(calclist[i+1].equals("^"))
			{
				int tt=1;
				int multi=Integer.parseInt(calclist[i]);
				for(int j=0;j<Integer.parseInt(calclist[i+2]);j++)
				{
					tt*=multi;
				}
				refine1[count++]=tt+"";
				System.out.println("squared result : "+tt);
				i=i+2;
			}
			
			else
				{
				refine1[count++]=calclist[i];
				 if(i+1==calclist.length-1){
					refine1[count++]=calclist[calclist.length-1];
					break;
				}
				}
		}
		System.out.println("refined 1");
		for(int i=0;i<count;i++){
			System.out.println(refine1[i]);
		}
		if(count==1){
			numbers.add(Double.parseDouble(refine1[0]));
		}
		for(int i=0;i<count-1;i++){
			String s1=refine1[i];
			double a,b;
			if(s1.equals("*")||s1.equals("/")){
				a=Double.parseDouble(refine1[i-1]);
				b=Double.parseDouble(refine1[i+1]);
				if(s1.equals("*"))
					a=a*b;
				else
					a=a/b;
				while(true){
					i+=2;
					if(i>=count)
						break;
					s1=refine1[i];
					if(s1.equals("*")||s1.equals("/")){
						b=Double.parseDouble(refine1[i+1]);
						if(s1.equals("*"))
							a=a*b;
						else
							a=a/b;
					}
					else
						break;
				}
				numbers.add(a);
			}
			if(i+1>=count)
				break;
			if(refine1[i].equals("+")||refine1[i].equals("-")){
				ops.add(refine1[i]);
			}
			if((refine1[i+1].equals("+")||refine1[i+1].equals("-")))
			{
					numbers.add(Double.parseDouble(s1));
					ops.add(refine1[++i]);
					System.out.println("entered "+numbers.peek()+ops.peek());
			}
			if(i+1==count-1)
				numbers.add(Double.parseDouble(refine1[i+1]));
			
		}}catch(Exception aa){
			tf1.setText("Error!!");
			aa.printStackTrace();
		}
		
		double calcresult=numbers.poll();
		while(true){
			if(ops.isEmpty())
				break;
			String cop=ops.poll();
			if(cop.equals("+")){
				calcresult+=numbers.poll();
			}
			else
				calcresult-=numbers.poll();
		}
		return calcresult;

	}
}
public class calculator {

	public static void main(String[] args) {
		MyCalculatorFrame mm=new MyCalculatorFrame();
	}

}
