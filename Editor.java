import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.event.*;
class Notepad extends WindowAdapter implements ActionListener,TextListener,KeyListener
{
	Frame f;
	MenuBar mb;
	Menu m1,m2;
	MenuItem nw,opn,sav,savas,ext,fnd,rpl;
	TextArea ta;
	TextField tf1,tf2;
	FileDialog fd,fd1,fd2;

	File f1,f2;
	FileInputStream fis;
	FileWriter fw;
	BufferedWriter bfw;
	BufferedReader bfr;

	Dialog d,d1;
	Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,bm;
	Label l,l1;

	Pattern p1;
	Matcher mm;

	String s1,s2,s3,s4;
	int cpos,st,en,idx;
	boolean finderror=false;
	boolean fileopen=false;
	boolean filesave=false;
	boolean textevent=true;
	public Editor()
	{
		f=new Frame();
		f.setSize(600,400);
		mb=new MenuBar();
		m1=new Menu("File");
		m2=new Menu("Edit");
		nw=new MenuItem("New");
		nw.addActionListener(this);
		opn=new MenuItem("Open");
		opn.addActionListener(this);
		sav=new MenuItem("Save");
		sav.addActionListener(this);
		savas=new MenuItem("Save As");
		savas.addActionListener(this);
		ext=new MenuItem("Exit");
		ext.addActionListener(this);
		fnd=new MenuItem("Find");
		fnd.addActionListener(this);
		rpl=new MenuItem("Find & Replace");
		rpl.addActionListener(this);
		m2.add(fnd);	
		m2.add(rpl);
		m1.add(nw);	
		m1.add(opn);	
		m1.add(sav);
		m1.add(savas);	
		m1.addSeparator();	
		m1.add(ext);
		mb.add(m1);	
		mb.add(m2);
		f.setMenuBar(mb);
		ta=new TextArea();
		ta.addTextListener(this);
		ta.addKeyListener(this);
		f.add(ta);
		f.setVisible(true);
		f.addWindowListener(this);
	}

//METHOD OFFIND AND REPLACE AND REPLACE ALL
	public void findTextWithSelection()
	{
	
		s2=ta.getText().replaceAll("\r\n","\n");

		cpos=ta.getCaretPosition();

		st=ta.getSelectionStart();
		en=ta.getSelectionEnd();




		if(st!=en)
		{
			cpos=st+1;
		}
		try
		{
			p1=Pattern.compile(tf1.getText());
			ta.setText(s2);
			mm=p1.matcher(s2);
			mm.find(cpos);
			if(cpos!=-1)
			{
				ta.select(mm.start(),mm.end());
				ta.requestFocus();
			}

		}
		catch(Exception e)
		{
				if(finderror==true)
				{
					d1.requestFocus();
				}
				else
				{
					d1=new Dialog(f,"Message");
					d1.setSize(300,250);
					d1.setLayout(new GridBagLayout());
					GridBagConstraints gbc=new GridBagConstraints();
					gbc.weightx=1.0;	
					gbc.weighty=1.0;
					gbc.gridwidth=3;	
					gbc.gridx=0;	
					gbc.gridy=0;
					Label lm=new Label("Not Found !");
					d1.add(lm,gbc);
					gbc.gridx=0;	gbc.gridy=1;
					bm=new Button("OK");
					bm.addActionListener(this);
					d1.add(bm,gbc);
					d1.setVisible(true);
					finderror=true;
					d1.addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent we)
								{
									Window ww=we.getWindow();
									ww.dispose();
									ww.setVisible(false);
								}});
				}

		}
	}

	public void replace()
	{
		if(ta.getSelectionStart()==ta.getSelectionEnd())
		{
			findTextWithSelection();
		}
		else
		{
			ta.replaceRange(tf2.getText(),mm.start(),mm.end());
			findTextWithSelection();
		}
	}
	public void replaceAll()
	{
		ta.setCaretPosition(0);
		s2=ta.getText().replaceAll("(\r\n|\n\r|\r)","\n");
		s3=tf1.getText();
		Pattern p=Pattern.compile(s3);
		Matcher m=p.matcher(s2);
		while(m.find())
			ta.setText(m.replaceAll(tf2.getText()));

		
	}







	public void news()
	{
			if(ta.getText().equals(""))
			{
				ta.setText("");
				filesave=false;
				textevent=true;
				System.out.println("Filesave="+filesave+"\ttextevent"+textevent);
			}
			else
				if(textevent==true)
				{
					ta.setText("");
					filesave=false;
					textevent=true;
				}
				else
				{
					d=new Dialog(f,"Editor");
					d.setSize(350,200);
					d.setLayout(new GridBagLayout());
					GridBagConstraints gbc=new GridBagConstraints();
					gbc.weightx=1.0;	gbc.weighty=1.0;
					gbc.gridwidth=3;	
					gbc.gridx=0;	gbc.gridy=0;
					l=new Label("Do you want to save the file?");
					d.add(l,gbc);
					gbc.gridwidth=1;
					gbc.gridx=0;	gbc.gridy=1;
					b1=new Button("Save");
					d.add(b1,gbc);
					b1.addActionListener(this);
					gbc.gridx=1;	gbc.gridy=1;
					b2=new Button("Don't Save");
					b2.addActionListener(this);
					d.add(b2,gbc);
					gbc.gridx=2;	gbc.gridy=1;
					b3=new Button("Cancel");
					b3.addActionListener(this);
					d.add(b3,gbc);
					d.setVisible(true);
						d.addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent we)
								{
									Window ww=we.getWindow();
									ww.dispose();
									ww.setVisible(false);
								}});			
				}
	}
	public void open()
	{
		if(ta.getText().equals(""))
		{
			fd=new FileDialog(f,"Open",FileDialog.LOAD);
			fd.setVisible(true);
			s1=fd.getDirectory()+fd.getFile();
			try
			{
				if(s1.equals("nullnull"))
				{
					fd.setVisible(false);
				}
				else
				{				
					fis=new FileInputStream(fd.getDirectory()+fd.getFile());
					bfr=new BufferedReader(new InputStreamReader(fis));
					String str=" ";
					while(str!=null)
					{
						str=bfr.readLine();
						if(str==null)
						break;
						ta.appendText(str+"\n");
					}
				}
				fis.close();
			}
			catch(Exception m)
			{
				System.out.println(m.getMessage());
			}
			finally
			{
				ta.setCaretPosition(0);
				filesave=true;
				fileopen=true;
				textevent=true;
			}
		}
		else
			if(textevent==true)
			{
			fd=new FileDialog(f,"Open",FileDialog.LOAD);
			fd.setVisible(true);
			s1=fd.getDirectory()+fd.getFile();
			try
			{
				if(s1.equals("nullnull"))
				{
					fd.setVisible(false);
				}
				else
				{				
					fis=new FileInputStream(fd.getDirectory()+fd.getFile());
					
					bfr=new BufferedReader(new InputStreamReader(fis));
					String str=" ";
					while(str!=null)
					{
						str=bfr.readLine();
						if(str==null)
						break;
						ta.appendText(str+"\n");
					}
				}
				fis.close();
			}
			catch(Exception m)
			{
				System.out.println(m.getMessage());
			}
			finally
			{
				ta.setCaretPosition(0);
				filesave=true;
				fileopen=true;
				textevent=true;
			}
			}
			else
			{
			System.out.println("second = "+textevent); //textevent checks for change in any file text..
			d=new Dialog(f,"Editor");
			d.setSize(350,200);
			d.setLayout(new GridBagLayout());
			GridBagConstraints gbc=new GridBagConstraints();
			gbc.weightx=1.0;	
			gbc.weighty=1.0;
			gbc.gridwidth=3;	
			gbc.gridx=0;	
			gbc.gridy=0;
			l=new Label("Do you want to save the file?");
			d.add(l,gbc);
			gbc.gridwidth=1;
			gbc.gridx=0;	
			gbc.gridy=1;
			b4=new Button("Save");
			d.add(b4,gbc);
			b4.addActionListener(this);
			gbc.gridx=1;	
			gbc.gridy=1;
			b5=new Button("Don't Save");
			b5.addActionListener(this);
			d.add(b5,gbc);
			gbc.gridx=2;	gbc.gridy=1;
			b6=new Button("Cancel");
			b6.addActionListener(this);
			d.add(b6,gbc);
			d.setVisible(true);
			d.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we)
			{
			Window ww=we.getWindow();
				ww.dispose();
				ww.setVisible(false);
			}});			
		}
	}
	public void save()
	{
		if(filesave==false)
		{
			fd1=new FileDialog(f,"Save",FileDialog.SAVE);
			fd1.setVisible(true);
			try
			{
				f1=new File(fd1.getDirectory()+fd1.getFile());
				s1=fd1.getDirectory()+fd1.getFile();
				if(s1.equals("nullnull"))
				{
					fd1.setVisible(false);
				}
				else
				{
					if(!f1.exists())
					{
						System.out.println(s1);
						fw=new FileWriter(s1);
						bfw= new BufferedWriter(fw);
						bfw.write(ta.getText());
						bfw.close();
						
						filesave=true;
						textevent=true;
					}
					else
					{
						d=new Dialog(f,"Confirm Save As");
						d.setSize(350,200);
						d.setLayout(new GridBagLayout());
						GridBagConstraints gbc=new GridBagConstraints();
						gbc.weightx=1.0;	
						gbc.weighty=1.0;
						gbc.gridwidth=3;	
						gbc.gridx=0;	
						gbc.gridy=0;
						l=new Label("File Already Exists !");
						d.add(l,gbc);
						gbc.gridx=0;	
						gbc.gridy=1;
						l1=new Label("Do you want to replace it?");
						d.add(l1,gbc);
						gbc.gridwidth=1;
						gbc.gridx=0;	
						gbc.gridy=2;
						b7=new Button("Yes");
						b7.addActionListener(this);
						d.add(b7,gbc);
						gbc.gridx=1;	
						gbc.gridy=2;
						Label l3=new Label();
						d.add(l3,gbc);
						gbc.gridx=2;	
						gbc.gridy=2;
						b8=new Button("No");
						b8.addActionListener(this);
						d.add(b8,gbc);
						d.setVisible(true);
						d.addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent we)
								{
									Window ww=we.getWindow();
									ww.dispose();
									ww.setVisible(false);
								}});			
					}
				}
			}
			catch(Exception m2)
			{
				System.out.println(m2.getMessage());
			}
		}
		else
			if(textevent==false)
			{
				try
				{
					fw=new FileWriter(s1);
					bfw= new BufferedWriter(fw);
					bfw.write(ta.getText());
					bfw.close();
					filesave=true;
					textevent=true;
				}
				catch(Exception m3)
				{
					System.out.println(m3.getMessage());
				}
			}
	}
	



	public void saveAs()
	{
			fd1=new FileDialog(f,"Save",FileDialog.SAVE);
			fd1.setVisible(true);
			try
			{
				f1=new File(fd1.getDirectory()+fd1.getFile());
				s1=fd1.getDirectory()+fd1.getFile();
				if(s1.equals("nullnull"))
				{
					fd1.setVisible(false);
				}
				else
				{
					if(!f1.exists())
					{
						System.out.println(s1);
						fw=new FileWriter(s1);
						bfw= new BufferedWriter(fw);
						bfw.write(ta.getText());
						bfw.close();
						filesave=true;
						textevent=true;
					}
					else
					{
						d=new Dialog(f,"Confirm Save As");
						d.setSize(350,200);
						d.setLayout(new GridBagLayout());
						GridBagConstraints gbc=new GridBagConstraints();
						gbc.weightx=1.0;	
						gbc.weighty=1.0;
						gbc.gridwidth=3;	
						gbc.gridx=0;	
						gbc.gridy=0;
						l=new Label("File Already Exists !");
						d.add(l,gbc);
						gbc.gridx=0;	
						gbc.gridy=1;
						l1=new Label("Do you want to replace it?");
						d.add(l1,gbc);
						gbc.gridwidth=1;
						gbc.gridx=0;	
						gbc.gridy=2;
						b7=new Button("Yes");
						b7.addActionListener(this);
						d.add(b7,gbc);
						gbc.gridx=1;	
						gbc.gridy=2;
						Label l3=new Label();
						d.add(l3,gbc);
						gbc.gridx=2;	
						gbc.gridy=2;
						b8=new Button("No");
						b8.addActionListener(this);
						d.add(b8,gbc);
						d.setVisible(true);
						d.addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent we)
								{
									Window ww=we.getWindow();
									ww.dispose();
									ww.setVisible(false);
								}});			
					}
				}
			}
			catch(Exception m2)
			{
				System.out.println(m2.getMessage());
			}
	}










	public void exit()
	{
		if(textevent==true)
		{
			f.setVisible(false);
			f.dispose();
			System.exit(1);
		}
		else
		{
			d=new Dialog(f,"Editor");
			d.setSize(350,200);
			d.setLayout(new GridBagLayout());
			GridBagConstraints gbc=new GridBagConstraints();
			gbc.weightx=1.0;	
			gbc.weighty=1.0;
			gbc.gridwidth=3;	
			gbc.gridx=0;	
			gbc.gridy=0;
			l=new Label("Do you want to save the file?");
			d.add(l,gbc);
			gbc.gridwidth=1;
			gbc.gridx=0;	
			gbc.gridy=1;
			b15=new Button("Save");
			d.add(b15,gbc);
			b15.addActionListener(this);
			gbc.gridx=1;			
			gbc.gridy=1;
			b16=new Button("Don't Save");
			b16.addActionListener(this);
			d.add(b16,gbc);
			gbc.gridx=2;	
			gbc.gridy=1;
			b17=new Button("Cancel");
			b17.addActionListener(this);
			d.add(b17,gbc);
			d.setVisible(true);
						d.addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent we)
								{
									Window ww=we.getWindow();
									ww.dispose();
									ww.setVisible(false);
								}});			
		}
	}





	public void actionPerformed(ActionEvent e)
	{
	
		if(e.getSource()==nw)
		{
			news();
		}
		if(e.getSource()==b1)
		{
			d.setVisible(false);
			save();
			if(filesave==true)
				news();
		}
		else	
			if(e.getSource()==b2)
			{
				d.setVisible(false);
				ta.setText(null);
				textevent=true;
				news();
			}
			else
				if(e.getSource()==b3)
				{
					d.setVisible(false);
				}

		if(e.getSource()==opn)
		{
			open();
			filesave=true;
			textevent=true;
			System.out.println("third= "+textevent);
			
		}
		if(e.getSource()==b4)
		{
			d.setVisible(false);
			save();
			open();
		}
		else	
			if(e.getSource()==b5)
			{
				d.setVisible(false);
				ta.setText("");
				open();
			}
			else
				if(e.getSource()==b6)
				{
					d.setVisible(false);
				}


		if(e.getSource()==sav)
		{
			save();
		}
		if(e.getSource()==b7)
		{
			d.setVisible(false);
			try
			{
				System.out.println(s1);
				fw=new FileWriter(s1);
				bfw= new BufferedWriter(fw);
				bfw.write(ta.getText());
				bfw.close();
				filesave=true;
				textevent=true;
			}
			catch(Exception m3)
			{
				System.out.println(m3.getMessage());
			}

		}
		else
			if(e.getSource()==b8)
			{
				d.setVisible(false);
			}

		if(e.getSource()==savas)
		{
			saveAs();
		}

	
		if(e.getSource()==ext)
		{
			exit();
		}
	
	
		if(e.getSource()==fnd)
		{
			d=new Dialog(f,"Find");
			d.setSize(350,200);
			d.setLayout(new GridBagLayout());
			GridBagConstraints gbc=new GridBagConstraints();
			gbc.gridx=0;	
			gbc.gridy=0;
			Label lfind=new Label("Find",Label.RIGHT);
			d.add(lfind,gbc);
			gbc.gridx=0;	
			gbc.gridy=1;
			tf1=new TextField(10);
			d.add(tf1,gbc);
			gbc.gridx=0;	
			gbc.gridy=2;
			b9=new Button("Find Next");
			b9.addActionListener(this);
			d.add(b9,gbc);
			gbc.gridx=1;	
			gbc.gridy=2;
			Label ll=new Label();
			d.add(ll,gbc);
			gbc.gridx=2;	
			gbc.gridy=2;
			b10=new Button("Close");
			b10.addActionListener(this);
			d.add(b10,gbc);
			d.setVisible(true);
						d.addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent we)
								{
									Window ww=we.getWindow();
									ww.dispose();
									ww.setVisible(false);
								}});			
		}
		if(e.getSource()==b9)
		{
			findTextWithSelection();
		}
		else
			if(e.getSource()==b10)
			{
				d.setVisible(false);
			}

	//CODE FOR REPLACE OPTION FROM EDIT MENU
		if(e.getSource()==rpl)
		{
			d=new Dialog(f,"Find & Replace");
			d.setSize(350,200);
			d.setLayout(new GridBagLayout());
			GridBagConstraints gbc=new GridBagConstraints();
			gbc.gridx=0;	
			gbc.gridy=0;
			Label lfind=new Label("Find");
			d.add(lfind,gbc);
			gbc.gridx=0;	
			gbc.gridy=1;
			tf1=new TextField(10);
			d.add(tf1,gbc);
			gbc.gridx=0;	
			gbc.gridy=2;
			Label lrpl=new Label("Replace");
			d.add(lrpl,gbc);
			gbc.gridx=0;	
			gbc.gridy=3;
			tf2=new TextField(10);
			d.add(tf2,gbc);
			gbc.gridx=0;	
			gbc.gridy=4;
			b9=new Button("Find Next");
			b9.addActionListener(this);
			d.add(b9,gbc);
			gbc.gridx=1;		
			gbc.gridy=4;
			b12=new Button("Replace");
			b12.addActionListener(this);
			d.add(b12,gbc);
			gbc.gridx=2;	
			gbc.gridy=4;
			b13=new Button("ReplaceAll");
			b13.addActionListener(this);
			d.add(b13,gbc);
			gbc.gridx=3;	
			gbc.gridy=4;
			b10=new Button("Close");
			b10.addActionListener(this);
			d.add(b10,gbc);
			d.setVisible(true);
						d.addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent we)
								{
									Window ww=we.getWindow();
									ww.dispose();
									ww.setVisible(false);
								}});
		}
		if(e.getSource()==b12)
		{
			replace();
		}
		else
			if(e.getSource()==b13)
			{
				replaceAll();
			}
			

	
		if(e.getSource()==b15)
		{
			d.setVisible(false);
			save();
			f.setVisible(false);
			System.exit(1);
		}
		else
			if(e.getSource()==b16)
			{
				System.exit(1);
			}
			else
				if(e.getSource()==b17)
					d.setVisible(false);
		
		if(e.getSource()==bm)
		{
			d1.dispose();
			d1.setVisible(false);
			finderror=false;
		}





	}

	public void keyTyped(KeyEvent ke)				//only activate when a character is pressed
	{
		if(fileopen==true)						
		{
			fileopen=false;
		}
	}
	public void keyPressed(KeyEvent ke)
	{}
	public void keyReleased(KeyEvent ke)
	{}






	
	public void textValueChanged(TextEvent te)
	{
		if(ta.getText().equals(""))
		{
			textevent=true;
		}
		else
			if(fileopen==true)
			{
				textevent=true;
			}
			else
			{
				textevent=false;
				System.out.println("TEXTEVENT ="+textevent);
			}
		
	}



	public void windowClosing(WindowEvent e)
	{
		exit();
	}
	public void windowClosed(WindowEvent e1)
	{
		exit();
	}




		
	public static void main(String args[])
	{
		Editor ed=new Editor();
	}
}