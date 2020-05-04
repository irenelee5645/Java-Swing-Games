package Drawingboardpackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

class MyFrame extends JFrame{
	boolean isSaved=false;
	int datasize=0;
	File currentPath;
	MyShapes selectedShape=null;
	int mouseclickcount=0;
	enum PolyToolStat{Start,Waiting,Done};
	PolyToolStat stat1=PolyToolStat.Done;
	boolean isPenDrawing=false;
	MyDrawPanel drawingP=new MyDrawPanel();
	Color currentColor=Color.black;
	int currentLineWidth;
	String currentShape;
	int currentPenSize;
	boolean moveToolSelected=false;
	boolean eraseToolSelected=false;
	boolean isFileOpen=false;
	boolean isExistingFile=false;
	float currentPenPattern[];
	String shapevariety[]=new String[]{"Rectangle","Rounded Rectangle","Oval","Line","Pen","PolyLine","Polygon"};
	float penPatternVariety[][]=new float[][]{new float[]{10},new float[]{10,10},new float[]{10,5,5,5},new float[]{5,5,5,5}};
	boolean isBeingDrawn=false;
	Rectangle2D borderRectangles[]=null;
	Ellipse2D rotatecircle=null;
	
	int startp[]=new int[2];
	int endp[]=new int[2];
	
	JButton colorexampleB[]=new JButton[10];
	JLabel currentcolorshow=new JLabel("      ");
	Toolkit tk=Toolkit.getDefaultToolkit();
	JToolBar toolbar=new JToolBar();
	Stack<MyShapes> saved=new Stack<MyShapes>();
	Stack<MyShapes> undone=new Stack<MyShapes>();
	JButton redoB=new JButton(new ImageIcon("C:\\Users\\연아\\Desktop\\images\\arrow_redo.png"));
	JButton undoB=new JButton(new ImageIcon("C:\\Users\\연아\\Desktop\\images\\arrow_undo.png"));

	Color mybasiccolors[]=new Color[]{
			Color.BLACK,
			Color.GRAY,
			Color.BLUE,
			Color.cyan,
			Color.green,
			Color.yellow,
			Color.orange,
			Color.magenta,
			Color.red,
			Color.PINK
	};
	
	public void createmenu(){
		JMenuBar mb=new JMenuBar();
		JMenu fileMenu=new JMenu("File");
		
		//new menuitem
		ImageIcon img1=new ImageIcon("C:\\Users\\연아\\Desktop\\images\\new.png");
		JMenuItem newFileMenuItem=new JMenuItem("  New",img1);
		fileMenu.add(newFileMenuItem);
		newFileMenuItem.addActionListener(new NewFileActionListener());
		fileMenu.addSeparator();

		//open menuitem
		ImageIcon img2=new ImageIcon("C:\\Users\\연아\\Desktop\\images\\open.png");
		JMenuItem openFileMenuItem=new JMenuItem("  Open",img2);
		openFileMenuItem.addActionListener(new FileOpenActionListener());
		fileMenu.add(openFileMenuItem);
		
		fileMenu.addSeparator();

		//open menuitem
		ImageIcon img3=new ImageIcon("C:\\Users\\연아\\Desktop\\images\\save.png");
		JMenuItem saveFileMenuItem=new JMenuItem("  Save",img3);
		saveFileMenuItem.addActionListener(new SaveFileActionListener());
		fileMenu.add(saveFileMenuItem);
		mb.add(fileMenu);
		this.setJMenuBar(mb);
	}
	
	public void createToolBar(){
		//move cursor--
		ImageIcon img1=new ImageIcon("C:\\Users\\연아\\Desktop\\images\\cursor.png");
		JButton editbutton=new JButton(img1);
		editbutton.setToolTipText("edit shapes");
		editbutton.addActionListener(new EditActionListener());
		toolbar.add(editbutton);
		toolbar.addSeparator();

		//shapeSelection--
		JLabel chooseShapeinstruct=new JLabel("Shape");
		JComboBox shapeExamples=new JComboBox();
		for(int i=0;i<shapevariety.length;i++)
			shapeExamples.addItem(shapevariety[i]);
		shapeExamples.addActionListener(new ShapeSelectionActionListener());
		toolbar.add(chooseShapeinstruct);
		toolbar.add(shapeExamples);
		toolbar.addSeparator();
		this.currentShape=shapevariety[shapeExamples.getSelectedIndex()];
		
		//ColorChooser;--
		JPanel colorpanel=new JPanel();
		colorpanel.setLayout(new BoxLayout(colorpanel,BoxLayout.Y_AXIS));
		currentcolorshow.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		currentcolorshow.setOpaque(true);
		currentcolorshow.setBackground(currentColor);
		colorpanel.add(currentcolorshow);
		JButton colorChooseB=new JButton("Color");
		colorChooseB.addActionListener(new ColorChooserActionListener());
		colorChooseB.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		colorpanel.add(colorChooseB);
	//	colorpanel.setBorder(BorderFactory.createLineBorder(Color.black));
		JButton fillB=new JButton("fill");
		fillB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(moveToolSelected&&selectedShape!=null){
					selectedShape.cc=currentColor;
					selectedShape.isFilled=true;
					drawingP.repaint();
				}
			}
			
		});
		JButton unfillB=new JButton("unfill");
		unfillB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(moveToolSelected&&selectedShape!=null){
					selectedShape.cc=currentColor;
					selectedShape.isFilled=false;
					drawingP.repaint();
				}
			}
			
		});
		toolbar.add(colorpanel);
		toolbar.add(fillB);
		toolbar.add(unfillB);

		
		toolbar.addSeparator();

		JPanel colorExample=new JPanel();
		colorExample.setLayout(new GridLayout(2,5,5,5));
		for(int i=0;i<colorexampleB.length;i++){
			colorexampleB[i]=new JButton();
			colorexampleB[i].setPreferredSize(new Dimension(20,20));
			colorexampleB[i].setBackground(mybasiccolors[i]);
			colorexampleB[i].addActionListener(new ColorChooserActionListener());
			colorExample.add(colorexampleB[i]);
		}
		colorExample.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		toolbar.add(colorExample);
		toolbar.addSeparator();
		
		//penpanel
		JLabel penSizeLabel=new JLabel("Pensize");
		JComboBox penSizeSelect=new JComboBox();
		penSizeSelect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				currentPenSize=(int) ((JComboBox)e.getSource()).getSelectedItem();				
			}
			
		});
		for(int i=0;i<10;i++){
			penSizeSelect.addItem(new Integer((i+1)*3));
		}
		this.currentPenSize=(int) penSizeSelect.getSelectedItem();
		toolbar.add(penSizeLabel);
		toolbar.add(penSizeSelect);
		toolbar.add(new JLabel("pt"));
		toolbar.addSeparator();
		
		//penPattern
		JLabel penPatternLabel=new JLabel("Pattern");
		JComboBox penPatternSelect=new JComboBox();
		penPatternSelect.addItem("___________");
		penPatternSelect.addItem("- - - - - ");
		penPatternSelect.addItem("_ . _ . _ . ");
		penPatternSelect.addItem(". . . . . .");
		currentPenPattern=penPatternVariety[penPatternSelect.getSelectedIndex()];
		penPatternSelect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				currentPenPattern=penPatternVariety[((JComboBox)e.getSource()).getSelectedIndex()];				
			}
			
		});
		toolbar.add(penPatternLabel);
		toolbar.add(penPatternSelect);
		toolbar.addSeparator();
		
		//penWidth
		toolbar.add(new JLabel("Line Width"));
		JComboBox lineWidthSelection=new JComboBox();
		for(int i=0;i<5;i++){
			lineWidthSelection.addItem(new Integer((i+1)*3));
		}
		lineWidthSelection.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				currentLineWidth=(int) lineWidthSelection.getSelectedItem();
			}
			
		});
		this.currentLineWidth=(int) lineWidthSelection.getSelectedItem();
		toolbar.add(lineWidthSelection);
		toolbar.addSeparator();
		
		//eraseButton
		JButton eraseB=new JButton(new ImageIcon("C:\\Users\\연아\\Desktop\\images\\eraser.png"));
		eraseB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				moveToolSelected=false;
				eraseToolSelected=true;
			}
			
		});
		JButton resetB=new JButton("Reset");
		resetB.setToolTipText("Erase All");
		resetB.addActionListener(new ResetActionListener());
		resetB.setForeground(Color.RED);
		JPanel erasePanel=new JPanel();
		erasePanel.add(eraseB);
		erasePanel.add(resetB);
		toolbar.add(erasePanel);
		
		
		//undo redo buttons--need to add ActionListener
		
		redoB.setToolTipText("Redo");
		redoB.setEnabled(false);
		redoB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				saved.add(undone.pop());
				borderRectangles=null;
				rotatecircle=null;
				if(undone.size()==0)
					redoB.setEnabled(false);
				selectedShape=null;
				drawingP.repaint();
			}
			
		});
		undoB.setToolTipText("Undo");
		undoB.setEnabled(false);
		undoB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				undone.add(saved.pop());
				redoB.setEnabled(true);
				selectedShape=null;
				borderRectangles=null;
				rotatecircle=null;

				if(saved.size()==0)
					undoB.setEnabled(false);
				drawingP.repaint();
			}
			
		});
		toolbar.addSeparator();
		toolbar.add(undoB);
		toolbar.add(redoB);

		
		
	}
	MyFrame(){
		createmenu();
		createToolBar();
		//setSize(300,300);
		toolbar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		add(toolbar,"North");
		add(drawingP,"Center");
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//MenuBar ActionListeners--currently empty!
	class FileOpenActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!isSaved && saved.size()!=0){
				if( isExistingFile){
					SavingMethod();
				}
				else{
					JOptionPane.showMessageDialog(null, "Save before opening new file", "warning", JOptionPane.WARNING_MESSAGE);
					currentPath=fileChooseMethod(2);
					SavingMethod();
				}
			}
			currentPath=fileChooseMethod(1);
			if(currentPath==null)
				return;
			saved.clear();
			undone.clear();
			OpeningMethod();
			isExistingFile=true;
			isFileOpen=true;
		}
		
	}	
	class NewFileActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!isSaved){
				if( isExistingFile){
					SavingMethod();
				}
				else{
					JOptionPane.showMessageDialog(null, "Save before closing", "warning", JOptionPane.WARNING_MESSAGE);
					currentPath=fileChooseMethod(2);
					if(currentPath!=null)
						SavingMethod();
				}
			}
			saved.removeAll(saved);
			undone.removeAll(saved);
			borderRectangles=null;
			rotatecircle=null;

			selectedShape=null;
			drawingP.repaint();
			isExistingFile=false;
			isFileOpen=true;
			redoB.setEnabled(false);
			undoB.setEnabled(false);
		}
		
	}
	class SaveFileActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println(isFileOpen+" : "+isExistingFile);
			if(saved.size()==0)
				return;
			if(isExistingFile)
			{
				SavingMethod();
			}
			else{
				currentPath=fileChooseMethod(2);
				System.out.println(currentPath);
				if(currentPath!=null){
					SavingMethod();
					
				}
			}
			undone.clear();
			redoB.setEnabled(false);
			isSaved=true;
		}
		
	}
	
	
	public void SavingMethod(){
		try {
			DataOutputStream os1=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(currentPath)));
			String stemp=fileSaveMethod();
			System.out.println("Stemp : "+ stemp);
			if(stemp!=null){
				os1.writeBytes(stemp);
				System.out.println("written");
			}
			os1.close();
			
			/////
//			FileOutputStream fout=new FileOutputStream(currentPath);
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		     ObjectOutputStream oos = new ObjectOutputStream(bos);
//		    Iterator it1=saved.iterator();
//		    while(it1.hasNext()){
//		    	oos.writeObject(it1.next());
//			    oos.flush();
//			    byte [] data = bos.toByteArray();
//			    System.out.println(data.length);
//			    fout.write(data);
//		    }
//			fout.close();

		     
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String fileSaveMethod(){
		String tosave="";
		Iterator<MyShapes> it1=saved.iterator();
		while(it1.hasNext()){
			MyShapes ctemp=it1.next();
			tosave+=ctemp.dataStoremethod();
			if(it1.hasNext())
				tosave+="\n";
		}
		return tosave;
	}
	
	public void OpeningMethod(){
		try {

			BufferedReader fin=new BufferedReader(new FileReader(currentPath));
			String raw1;
			while((raw1=fin.readLine())!=null){
				MyShapes tempsave=new MyShapes();

				System.out.println("raw1");
				System.out.println(raw1);
				String list1[]=raw1.split(":");
				
				for(int i=0;i<list1.length;i++){
					System.out.println(list1[i]);
				}
				tempsave.type=list1[0];
				String colorlist[]=list1[1].split(",");
				tempsave.cc=new Color(Integer.valueOf(colorlist[0]),Integer.valueOf(colorlist[1]),Integer.valueOf(colorlist[2]));
				tempsave.x1=Integer.valueOf(list1[2]);
				tempsave.x2=Integer.valueOf(list1[3]);
				tempsave.y1=Integer.valueOf(list1[4]);
				tempsave.y2=Integer.valueOf(list1[5]);
				tempsave.w=Integer.valueOf(list1[6]);
				tempsave.h=Integer.valueOf(list1[7]);
				tempsave.ang1=Integer.valueOf(list1[8]);
				tempsave.ang2=Integer.valueOf(list1[9]);
				tempsave.strokeWidth=Integer.valueOf(list1[10]);
				tempsave.donedrawing=Boolean.valueOf(list1[11]);
				tempsave.isFilled=Boolean.valueOf(list1[12]);
				if(list1[13].length()>2){
				String gplistStr=list1[13].substring(1, list1[13].length()-1);
				String gpList[]=gplistStr.split(",");
				for(int j=0;j<gpList.length;j++){
					gpList[j]=gpList[j].trim();
					String gpPointList[]=gpList[j].split("-");
					tempsave.forgp.add(new myPoint(Integer.valueOf(gpPointList[0]),Integer.valueOf(gpPointList[1])));
				}

				}
				String strokePatternListraw=list1[14].substring(1, list1[14].length()-1);
				String strokePatternList[]=strokePatternListraw.split(",");
				tempsave.strokepattern=new float[strokePatternList.length];
				for(int j=0;j<strokePatternList.length;j++){
					strokePatternList[j]=strokePatternList[j].trim();
					tempsave.strokepattern[j]=Float.valueOf(strokePatternList[j]);
				}
				if(list1[15].length()>2){
					String gplistStr=list1[15].substring(1, list1[15].length()-1);
					String gpList[]=gplistStr.split(",");
					for(int j=0;j<gpList.length;j++){
						gpList[j]=gpList[j].trim();
						String gpPointList[]=gpList[j].split("-");
						tempsave.drawpen.add(new MyPen(Integer.valueOf(gpPointList[0]),Integer.valueOf(gpPointList[1]),Integer.valueOf(gpPointList[2])));
					}
				}
				tempsave.isrotated=Boolean.valueOf(list1[16]);
				tempsave.rotateAngle=Integer.valueOf(list1[17]);
				saved.push(tempsave);
			}
		     drawingP.repaint();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public File fileChooseMethod(int fileActionIndex){
		JFileChooser chooser=new JFileChooser();
		FileNameExtensionFilter filter=new FileNameExtensionFilter("DRP","drp");
		chooser.setFileFilter(filter);
		int checkfile=-10;
		if(fileActionIndex==1)
			checkfile=chooser.showOpenDialog(null);
		else if(fileActionIndex==2)
			checkfile=chooser.showSaveDialog(null);
		if(checkfile==JFileChooser.APPROVE_OPTION)
			return chooser.getSelectedFile();
		else
			return null;
	}
	
	class EditActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			currentShape="moveTool";
			eraseToolSelected=false;
			moveToolSelected=true;
		}
		
	}
	class ShapeSelectionActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			currentShape=shapevariety[((JComboBox)e.getSource()).getSelectedIndex()];
			eraseToolSelected=false;
			moveToolSelected=false;
			System.out.println(currentShape);
		}
		
	}
	class ColorChooserActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Color temp;
			if(e.getActionCommand().equals("Color")){
				JColorChooser colorchooser=new JColorChooser();
				 temp=colorchooser.showDialog(null, "Choose Color", Color.WHITE);
				 if(temp!=null)
					 currentColor=temp;
				 currentcolorshow.setBackground(currentColor);
				 return;
			}
			else{
				for(int i=0;i<mybasiccolors.length;i++){
					if((JButton)e.getSource()==colorexampleB[i])
						{
						currentColor=mybasiccolors[i];
						currentcolorshow.setBackground(currentColor);
						 return;
						}
					
				}
			}
		}
		
	}
	class ResetActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int i=JOptionPane.showConfirmDialog(null, "This action is not restorable\nContinue?", "Reset", JOptionPane.WARNING_MESSAGE);
			if(i==JOptionPane.CANCEL_OPTION)
				return;
			saved.removeAll(saved);
			undone.removeAll(saved);
			borderRectangles=null;
			rotatecircle=null;

			selectedShape=null;
			drawingP.repaint();
		}
		
	}
	
	
	class MyDrawPanel extends JPanel{
		MyDrawPanel(){
			setPreferredSize(new Dimension(1000,600));
			DrawingPanelMouseListener dpml=new DrawingPanelMouseListener();
			this.addMouseListener(dpml);
			this.addMouseMotionListener(dpml);
			this.setBackground(Color.WHITE);
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2=(Graphics2D)g;
			isSaved=false;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Iterator it=saved.iterator();
			while(it.hasNext()& saved.size()!=0){
				MyShapes temp=(MyShapes) it.next();
				g2.setColor(temp.cc);
				
				if(temp.strokepattern.length==1)
					g2.setStroke(new BasicStroke(temp.strokeWidth));
				else
					g2.setStroke(new BasicStroke(temp.strokeWidth,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,1.0f,temp.strokepattern,0));
				if(temp.isrotated)
					{
					g2.translate(temp.x1+temp.w/2, temp.y1+temp.h/2);
					g2.rotate(Math.toRadians(temp.rotateAngle));
					}
				if(temp.type.equals("eraser"))
					{
					g2.setColor(Color.WHITE);
					temp.drawPen(g2);
					}
				else if(temp.type.equals(shapevariety[0]))
					temp.drawrect(g2);
				else if(temp.type.equals(shapevariety[1]))
					temp.drawroundrect(g2);
				else if(temp.type.equals(shapevariety[2]))
					temp.drawellipse(g2);
				else if(temp.type.equals(shapevariety[3]))
					temp.drawline(g2);
				else if(temp.type.equals(shapevariety[4]))
					temp.drawPen(g2);
				else if(temp.type.equals(shapevariety[5]))
					temp.drawPolyLine(g2);
				else if(temp.type.equals(shapevariety[6]))
					temp.drawPolyTool(g2);
				if(temp.isrotated)
				{
					g2.rotate(-Math.toRadians(temp.rotateAngle));
				g2.translate(-(temp.x1+temp.w/2), -(temp.y1+temp.h/2));
				}
			}
			if(eraseToolSelected&&!saved.isEmpty()){
				this.drawongoingPen(g2);
				return;
			}
			if(moveToolSelected&&selectedShape!=null){
				selectedShapeDisplay(g2);
				return;
			}
			if(moveToolSelected)
				return;
			if(isBeingDrawn){
				isFileOpen=true;
				undone.removeAll(undone);
				undoB.setEnabled(true);
				redoB.setEnabled(false);
				g2.setColor(currentColor);
				
				if(currentPenPattern.length==1)
					g2.setStroke(new BasicStroke(currentLineWidth));
				else
					g2.setStroke(new BasicStroke(currentLineWidth,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,1.0f,currentPenPattern,0));
				System.out.println("currentStorke : "+ Arrays.toString(currentPenPattern));
				if(currentShape.equals(shapevariety[0]))
					g2.draw(new Rectangle2D.Double(startp[0], startp[1], endp[0]-startp[0], endp[1]-startp[1]));
				else if(currentShape.equals(shapevariety[1]))
					g2.draw(new RoundRectangle2D.Double(startp[0], startp[1], endp[0]-startp[0], endp[1]-startp[1],20,20));
				else if(currentShape.equals(shapevariety[2]))
					g2.draw(new Ellipse2D.Double(startp[0], startp[1], endp[0]-startp[0], endp[1]-startp[1]));
				else if(currentShape.equals(shapevariety[3]))
					g2.draw(new Line2D.Double(startp[0],startp[1],endp[0],endp[1]));
				else if(currentShape.equals(shapevariety[4]))
					this.drawongoingPen(g2);
				else if(currentShape.equals(shapevariety[5])||currentShape.equals(shapevariety[6]))
					this.drawPoly(g2);
					
			
			}
			
		}
		
		public void selectedShapeDisplay(Graphics2D g){
			g.setColor(Color.red);
			int rectSize=10;
			int indexcheck=0;
			for(indexcheck=0;indexcheck<shapevariety.length;indexcheck++){
				if(selectedShape.type.equals(shapevariety[indexcheck]))
					break;
			}
			if(indexcheck<3){
				int x=selectedShape.x1;
				int y=selectedShape.y1;
				int w=selectedShape.w;
				int h=selectedShape.h;
				g.translate(x+w/2, y+h/2);
				g.rotate(Math.toRadians(selectedShape.rotateAngle));
				borderRectangles=new Rectangle2D[9];
				for(int i=0;i<3;i++){
					for(int j=0;j<3;j++){
						//borderRectangles[i*3+j]=new Rectangle2D.Double(x+w/2*j-rectSize/2,y+h/2*i-rectSize/2,rectSize,rectSize);
						borderRectangles[i*3+j]=new Rectangle2D.Double(-w/2+w/2*j-rectSize/2,-h/2+h/2*i-rectSize/2,rectSize,rectSize);
						g.draw(borderRectangles[i*3+j]);
					}
				}
				rotatecircle= new Ellipse2D.Double(0,-h/2-30,rectSize,rectSize);
				g.draw(rotatecircle);
				g.rotate(-Math.toRadians(selectedShape.rotateAngle));
				g.translate(-(x+w/2), -(y+y/2));
			}
			else{
				int x1=selectedShape.x1;
				int y1=selectedShape.y1;
				int x2=selectedShape.x2;
				int y2=selectedShape.y2;
				borderRectangles=new Rectangle2D[3];
				for(int i=0;i<3;i++){
					borderRectangles[i]=new Rectangle2D.Double(x1+(x2-x1)/2*i-rectSize/2,y1+(y2-y1)/2*i-rectSize/2,rectSize,rectSize);
					g.draw(borderRectangles[i]);

				}
			}
			g.setColor(currentColor);
		}
		public void drawongoingPen(Graphics2D g){
			//MyPen temp=new MyPen(endp[0],endp[1],currentPenSize/2);
			saved.get(saved.size()-1).drawpen.add(new MyPen(endp[0],endp[1],currentPenSize/2));
			saved.get(saved.size()-1).drawPen(g);
		}
		public void drawPoly(Graphics2D g){
			System.out.println("drawing polyTool");
			GeneralPath gp=new GeneralPath();
			MyShapes temp=saved.get(saved.size()-1);
			if(stat1==PolyToolStat.Start){
				System.out.println("started");
				if(mouseclickcount==1){
				System.out.println("added");
				temp.forgp.add(new myPoint(startp[0],startp[1]));
				gp.moveTo(startp[0], startp[1]);
				g.draw(gp);
				stat1=PolyToolStat.Waiting;}
				else{
					System.out.println("Done");
					stat1=PolyToolStat.Done;
					System.out.println("false called at start");
					isBeingDrawn=false;
					saved.pop();
				}
				mouseclickcount=0;
			}
			else if(stat1==PolyToolStat.Waiting){
				System.out.println("Waiting");
				gp.moveTo(temp.forgp.get(0).x, temp.forgp.get(0).y);
				System.out.println(mouseclickcount);
				if(mouseclickcount==0){
					g.draw(new Line2D.Double(startp[0],startp[1],endp[0],endp[1]));
					return;
				}
				for(int i=1;i<temp.forgp.size();i++){
					gp.lineTo(temp.forgp.get(i).x, temp.forgp.get(i).y);
					drawingP.repaint();

				}
				temp.forgp.add(new myPoint(startp[0],startp[1]));
				if(mouseclickcount==1){
				gp.lineTo(startp[0], startp[1]);
				mouseclickcount=0;}
				else{
					stat1=PolyToolStat.Done;
					isBeingDrawn=false;
					mouseclickcount=0;
					if(currentShape.equals(shapevariety[5])){
						gp.lineTo(startp[0], startp[1]);
						drawingP.repaint();
					}
					else
					{
						gp.closePath();
						temp.donedrawing=true;
						drawingP.repaint();
					}
				}
			}
			
		}
		
		
	}
	
	class DrawingPanelMouseListener implements MouseListener,MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent arg0) {
			System.out.println("mouse drag called");
			endp[0]=arg0.getX();
			endp[1]=arg0.getY();
			isBeingDrawn=true;
			if(moveToolSelected&&borderRectangles!=null){
				
				int rectindex=-1;
				for(int i=0;i<borderRectangles.length;i++)
				{
					if(borderRectangles[4].contains(new Point(endp[0]-(selectedShape.x1+selectedShape.w/2),endp[1]-(selectedShape.y1+selectedShape.h/2)))){
						rectindex=4;
						break;
					}
					else{
					int centralx=selectedShape.x1+selectedShape.w/2;
					int centraly=selectedShape.y1+selectedShape.h/2;
					double boxdis=(Math.sqrt((borderRectangles[i].getCenterX())*(borderRectangles[i].getCenterX())+(borderRectangles[i].getCenterY())*(borderRectangles[i].getCenterY())));
					int boxloc_x=0;
					int boxloc_y=0;
					 boxloc_x=(int)(centralx+Math.cos(Math.acos(borderRectangles[i].getCenterX()/boxdis)-Math.toRadians(selectedShape.rotateAngle))*boxdis);
					 boxloc_y=(int)(centraly+Math.sin(Math.asin(borderRectangles[i].getCenterY()/boxdis)-Math.toRadians(selectedShape.rotateAngle))*boxdis);

//					 if(i<=5){
//					 boxloc_y=(int)(centraly-Math.sin(Math.acos(borderRectangles[i].getCenterX()/boxdis)-Math.toRadians(selectedShape.rotateAngle))*boxdis);
//
//					}
//					else{
//						 boxloc_y=(int)(centraly+Math.sin(Math.acos(borderRectangles[i].getCenterX()/boxdis)-Math.toRadians(selectedShape.rotateAngle))*boxdis);
//						
//					}
					
					System.out.println(i+" : "+centralx+" , "+centraly+" , "+boxdis+"( "+borderRectangles[i].getCenterX()+" , "+borderRectangles[i].getCenterY()+")");
					System.out.println("calculated location -->"+(boxloc_x-centralx));
					System.out.println("calculated location -->"+(boxloc_y-centraly));
					 System.out.println(endp[0]+" compared to "+boxloc_x);
						System.out.println(endp[1]+" compared to "+boxloc_y+"\n\n");
					if(Math.abs(endp[0]-(boxloc_x+5))< 10&& Math.abs(endp[1]-(boxloc_y+5))<10 ){
						rectindex=i;
						break;
					}}
				}
//				if(rotatecircle.contains(new Point((endp[0]-(selectedShape.x1+selectedShape.w/2)),(endp[1]-(selectedShape.y1+selectedShape.h/2)))))
//					{
//					rectindex=25;
//					System.out.println("selected");
//					}
//				System.out.println(endp[0]+" : "+endp[1]);
//				System.out.println((selectedShape.x1+selectedShape.w/2+Math.sin(Math.toRadians(selectedShape.rotateAngle))));
//				System.out.println((selectedShape.y1+selectedShape.h/2-(Math.cos(Math.toRadians(selectedShape.rotateAngle)))*(selectedShape.h/2+30)));
				if(Math.abs((selectedShape.x1+selectedShape.w/2+(Math.sin(Math.toRadians(selectedShape.rotateAngle))*(selectedShape.h/2+30)))-endp[0])<10 &&
						Math.abs((selectedShape.y1+selectedShape.h/2-(Math.cos(Math.toRadians(selectedShape.rotateAngle)))*(selectedShape.h/2+30))-endp[1])<10){
					
					
					rectindex=25;
					System.out.println("selected");
				}
				if(selectedShape.type!=shapevariety[3]){
				
				if(rectindex!=-1){
					if(rectindex!=25){
				if(rectindex<3)
					{
					selectedShape.y1+=(endp[1]-startp[1]);
					selectedShape.h-=(endp[1]-startp[1]);
					}
				if(rectindex%3==0){
					selectedShape.x1+=(endp[0]-startp[0]);
					selectedShape.w-=endp[0]-startp[0];
				}
				if(rectindex%3==2)
					selectedShape.w+=endp[0]-startp[0];
				if(rectindex>5)
					selectedShape.h+=endp[1]-startp[1];
				if(rectindex==4){
					selectedShape.y1+=(endp[1]-startp[1]);
					selectedShape.x1+=(endp[0]-startp[0]);
					
				}}
				if(rectindex==25){
					selectedShape.isrotated=true;
					System.out.println("rotate circle if statement");
					if(selectedShape.rotateAngle<90){
						System.out.println("in1");
						if(endp[0]-startp[0]>0 ||endp[1]-startp[1]>0)
							selectedShape.rotateAngle++;
						else
							selectedShape.rotateAngle--;
					}
					else if(selectedShape.rotateAngle<180){
						if(endp[0]-startp[0]<0||endp[1]-startp[1]>0)
							selectedShape.rotateAngle++;
						else
							selectedShape.rotateAngle--;
					}
					else if(selectedShape.rotateAngle<270){
						if(endp[0]-startp[0]<0||endp[1]-startp[1]<0)
							selectedShape.rotateAngle++;
						else
							selectedShape.rotateAngle--;
					}
					else if(selectedShape.rotateAngle<360){
						if(endp[0]-startp[0]>0||endp[1]-startp[1]<0)
							selectedShape.rotateAngle++;
						else
							selectedShape.rotateAngle--;
					}
					if(selectedShape.rotateAngle>=360)
						selectedShape.rotateAngle-=360;
					else if(selectedShape.rotateAngle<0)
						selectedShape.rotateAngle+=360;
				}
				System.out.println(selectedShape.rotateAngle);
				}
				
			}				
				startp[0]=endp[0];
				startp[1]=endp[1];
			}
			drawingP.repaint();
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			if(stat1==PolyToolStat.Waiting)
			{
				endp[0]=arg0.getX();
				endp[1]=arg0.getY();
				drawingP.repaint();
				
			}			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			startp[0]=e.getX();
			startp[1]=e.getY();
			mouseclickcount=e.getClickCount();
			if(moveToolSelected){
				selectedShape=null;
				Shape tempshape=null;
				MyShapes currenttemp;

				for(int i=0;i<saved.size();i++){
					currenttemp=saved.get(i);
					if(currenttemp.type.equals(shapevariety[0]))
						tempshape=new Rectangle2D.Double(currenttemp.x1,currenttemp.y1,currenttemp.w,currenttemp.h);
					else if(currenttemp.type.equals(shapevariety[1]))
						tempshape=new RoundRectangle2D.Double(currenttemp.x1,currenttemp.y1,currenttemp.w,currenttemp.h,20,20);
					else if(currenttemp.type.equals(shapevariety[2]))
						tempshape=new Ellipse2D.Double(currenttemp.x1,currenttemp.y1,currenttemp.w,currenttemp.h);
					else if(currenttemp.type.equals(shapevariety[3])&&currenttemp.x2-currenttemp.x1>0)
						{
//							double slope=(currenttemp.y2-currenttemp.y1)/(currenttemp.x2-currenttemp.x1);
//							if(2.0>Math.abs(slope*startp[0]+currenttemp.y1-currenttemp.x1)-startp[1]){
//								selectedShape=currenttemp;
//								System.out.println("line selected");
//							}
						tempshape=new Line2D.Double(currenttemp.x1,currenttemp.y1,currenttemp.x2,currenttemp.y2);
						}
					if(tempshape!=null&&tempshape.contains(new Point(startp[0],startp[1])))
					{
						selectedShape=currenttemp;
					}
				}
			}
			
			else if(stat1==PolyToolStat.Done&&(currentShape.equals(shapevariety[5])||currentShape.equals(shapevariety[6]))){
				MyShapes stemp=new MyShapes();
				stemp.cc=currentColor;
				stemp.strokeWidth=currentLineWidth;
				stemp.strokepattern=currentPenPattern;
				stemp.type=currentShape;
				saved.push(stemp);
				stat1=PolyToolStat.Start;
				isBeingDrawn=true;
			}
			if(selectedShape==null)
				{
				borderRectangles=null;
				rotatecircle=null;

				}
			drawingP.repaint();

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			startp[0]=e.getX();
			startp[1]=e.getY();
			if(eraseToolSelected&&isPenDrawing==false){
				MyShapes stemp=new MyShapes();
				stemp.cc=Color.WHITE;
				stemp.strokeWidth=currentLineWidth;
				stemp.strokepattern=currentPenPattern;
				stemp.type="eraser";
				saved.push(stemp);
				isPenDrawing=true;
			}
			else if(currentShape.equals(shapevariety[4])&&isPenDrawing==false)
				{
				MyShapes stemp=new MyShapes();
				stemp.cc=currentColor;
				stemp.strokeWidth=currentLineWidth;
				stemp.strokepattern=currentPenPattern;
				stemp.type=currentShape;
				saved.push(stemp);
				isPenDrawing=true;
				}
			if(currentShape.equals(shapevariety[5])||currentShape.equals(shapevariety[6]))
				return;
			drawingP.repaint();

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(!currentShape.equals(shapevariety[5])&&!currentShape.equals(shapevariety[6]))
			{endp[0]=e.getX();
			endp[1]=e.getY();
			isBeingDrawn=false;
}
			 if(currentShape.equals(shapevariety[4])||eraseToolSelected){
				isPenDrawing=false;
			}
			 else if(currentShape.equals(shapevariety[0])||currentShape.equals(shapevariety[2])){
				MyShapes stemp=new MyShapes();
				stemp.x1=startp[0];
				stemp.y1=startp[1];
				stemp.w=endp[0]-startp[0];
				stemp.h=endp[1]-startp[1];
				stemp.cc=currentColor;
				stemp.strokeWidth=currentLineWidth;
				stemp.strokepattern=currentPenPattern;
				stemp.type=currentShape;
				saved.push(stemp);
				
			}
			else if(currentShape.equals(shapevariety[1])){
				MyShapes stemp=new MyShapes();
				stemp.x1=startp[0];
				stemp.y1=startp[1];
				stemp.w=endp[0]-startp[0];
				stemp.h=endp[1]-startp[1];
				stemp.cc=currentColor;
				stemp.strokeWidth=currentLineWidth;
				stemp.strokepattern=currentPenPattern;
				stemp.type=currentShape;
				stemp.ang1=20;
				stemp.ang2=20;
				saved.push(stemp);
			}
			else if(currentShape.equals(shapevariety[3])){
				MyShapes stemp=new MyShapes();
				stemp.x1=startp[0];
				stemp.y1=startp[1];
				stemp.x2=endp[0];
				stemp.y2=endp[1];
				stemp.cc=currentColor;
				stemp.strokeWidth=currentLineWidth;
				stemp.strokepattern=currentPenPattern;
				stemp.type=currentShape;
				saved.push(stemp);


			}
			
			drawingP.repaint();
			
		}
		
	}

}
	
class MyShapes implements Serializable{
	String type;
	Color cc;
	int x1,x2;
	int y1,y2;
	int w,h;
	int ang1,ang2;
	int strokeWidth;
	boolean donedrawing=false;
	boolean isFilled=false;
	Vector<myPoint> forgp=new Vector<myPoint>();
	float strokepattern[]=new float[4];
	Vector<MyPen> drawpen=new Vector<MyPen>();
	boolean isrotated=false;
	int rotateAngle=0;
	public void drawrect(Graphics2D g){
		int xtemp=x1;
		int ytemp=y1;
		if(isrotated){
			x1=-w/2;
			y1=-h/2;
		}
		if(isFilled)
			g.fill(new Rectangle2D.Double(x1,y1,w,h));
		else
			g.draw(new Rectangle2D.Double(x1,y1,w,h));
		if(isrotated){
			x1=xtemp;
			y1=ytemp;
		}

	}
	public void drawroundrect(Graphics2D g){
		int xtemp=x1;
		int ytemp=y1;
		if(isrotated){
			x1=-w/2;
			y1=-h/2;
		}
		if(isFilled)
			g.fill(new RoundRectangle2D.Double(x1,y1,w,h,ang1,ang2));
		else
			g.draw(new RoundRectangle2D.Double(x1,y1,w,h,ang1,ang2));
		if(isrotated){
			x1=xtemp;
			y1=ytemp;
		}

	}
	public void drawellipse(Graphics2D g){
		int xtemp=x1;
		int ytemp=y1;
		if(isrotated){
			x1=-w/2;
			y1=-h/2;
		}
		if(isFilled)
			g.fill(new Ellipse2D.Double(x1,y1,w,h));
		else
			g.draw(new Ellipse2D.Double(x1,y1,w,h));
		if(isrotated){
			x1=xtemp;
			y1=ytemp;
		}

	}
	public void drawline(Graphics2D g){
		
			g.draw(new Line2D.Double(x1,y1,x2,y2));

	}
	public void drawPen(Graphics2D g){
		for(int i=0;i<drawpen.size();i++){
			MyPen pp=drawpen.get(i);
			g.fill(new Ellipse2D.Double(pp.x,pp.y,pp.size,pp.size));

		}
	}
	public void drawPolyLine(Graphics2D g){
		System.out.println("drawing at MyShape");
		if(forgp.isEmpty())
			return;
		GeneralPath gp=new GeneralPath();
		gp.moveTo(forgp.get(0).x, forgp.get(0).y);
		for(int i=1;i<forgp.size();i++){
			gp.lineTo(forgp.get(i).x, forgp.get(i).y);
		}
		g.draw(gp);
	}
	public void drawPolyTool(Graphics2D g){
		if(forgp.isEmpty())
			return;
		GeneralPath gp=new GeneralPath();
		gp.moveTo(forgp.get(0).x, forgp.get(0).y);
		for(int i=1;i<forgp.size();i++){
			
			if(i==forgp.size()-1){
				if(donedrawing)
					{
					gp.closePath();
					break;
					}
			}
			gp.lineTo(forgp.get(i).x, forgp.get(i).y);
		}
		g.draw(gp);
	}
	
	public String dataStoremethod(){
		String tosaveString="";
		tosaveString+=type+":"+cc.getRed()+","+cc.getGreen()+","+cc.getBlue()+":"+x1+":"+x2+":"+y1+":"+y2+":"+w+":"+h+":"+ang1+":"+ang1+":"
				+strokeWidth+":"+donedrawing+":"+isFilled+":"+Arrays.toString(forgp.toArray())+":"+Arrays.toString(strokepattern)+":"+Arrays.toString(drawpen.toArray())+":"+isrotated+":"+rotateAngle;
		return tosaveString;
	}
}

class myPoint{
	int x;
	int y;
	public myPoint(int i, int j) {
		x=i;
		y=j;
	}
	@Override
	public String toString() {
		return x+"-"+y;
	}
	
}

class MyPen{
	int x;
	int y;
	int size;
	public MyPen(int x, int y, int size) {
		super();
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	@Override
	public String toString(){
		return x+"-"+y+"-"+size;
	}
	
}













public class DrawingBoardClass {

	public static void main(String[] args) {
		MyFrame mm=new MyFrame();
		Color tempc=Color.black;
		System.out.println(tempc.toString());
	}

}
