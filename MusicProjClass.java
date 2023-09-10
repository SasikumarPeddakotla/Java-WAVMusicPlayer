import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MusicProjClass extends JFrame implements ActionListener
{	
	private static final long serialVersionUID = 1L;
	JButton play;
	JButton prev;
	JButton next;
	JButton forw;
	JButton back;
	
	JPanel panel;
	
	JLabel label = new JLabel();
	
	ImageIcon playimage;
	ImageIcon previmage;
	ImageIcon nextimage;
	ImageIcon pauseimage;
	ImageIcon forimage;
	ImageIcon backimage;
	ImageIcon image;
	
	File file;
	Clip clip;
	
	AudioInputStream as;
	
	ResultSet rs;
	
	MusicProjClass() throws ClassNotFoundException, SQLException, UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3300/music","root","system");
		Statement s=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,EXIT_ON_CLOSE);
		rs=s.executeQuery("select * from songs");
		rs.next();
		String song_path = rs.getString("song_path");
		String song_name = rs.getString("song_name");
		String image = rs.getString("image");
		
		SongSelect(song_path,song_name,image);
		
		panel = new JPanel();
		panel.setBounds(200,300,260,200);
		panel.setPreferredSize(new Dimension(400,75));
		panel.setBackground(Color.black);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT,27,5));
		
		
		playimage = new ImageIcon("src/images/playButtonResized.png");
		previmage = new ImageIcon("src/images/prevresized1.jpg");
		nextimage = new ImageIcon("src/images/next1resized1.jpg");
		pauseimage = new ImageIcon("src/images/pauseresized.png");
		forimage = new ImageIcon("src/images/forwardresized.jpg");
		backimage = new ImageIcon("src/images/backwardresized.jpg");
		
		play = new JButton();
		play.setMargin(new Insets(0, 0, 0, 0));
		play.setIcon(playimage);
		play.setFocusable(false);
		play.addActionListener(this);
		
		next = new JButton();
		next.setMargin(new Insets(0, 0, 0, 0));
		next.setIcon(nextimage);
		next.setFocusable(false);
		next.addActionListener(this);
		
		prev = new JButton();
		prev.setMargin(new Insets(0, 0, 0, 0));
		prev.setIcon(previmage);
		prev.setFocusable(false);
		prev.addActionListener(this);
		
		forw = new JButton();
		forw.setMargin(new Insets(0, 0, 0, 0));
		forw.setIcon(forimage);
		forw.setFocusable(false);
		forw.addActionListener(this);
		
		back = new JButton();
		back.setMargin(new Insets(0, 0, 0, 0));
		back.setIcon(backimage);
		back.setFocusable(false);
		back.addActionListener(this);
		
		play.setBorder(BorderFactory.createEtchedBorder());
		next.setBorder(BorderFactory.createRaisedBevelBorder());
		prev.setBorder(BorderFactory.createRaisedBevelBorder());
		forw.setBorder(BorderFactory.createRaisedBevelBorder());
		back.setBorder(BorderFactory.createRaisedBevelBorder());
		
		setTitle("Music Player");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450,650);
		setResizable(false);
		setLayout(new FlowLayout());
		getContentPane().setBackground(Color.black);
		
		panel.add(back);
		panel.add(prev);
		panel.add(play);
		panel.add(next);
		panel.add(forw);
		
		label.setFont(new Font("Calibri",Font.BOLD,25));
		label.setForeground(Color.white);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setIconTextGap(20);
		
		add(label);
		add(panel);
		
	}
	
	void SongSelect(String song_path,String song_name,String image1) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		file = new File(song_path);
		as = AudioSystem.getAudioInputStream(file);
		clip = AudioSystem.getClip();
		clip.open(as);
		
		image = new ImageIcon(image1);
		Image i = image.getImage();
		Image j = i.getScaledInstance(360, 450, EXIT_ON_CLOSE);
		image = new ImageIcon(j);
		
		label.setIcon(image);
		label.setText(song_name);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource() == play)
		{
			if(play.getIcon() == playimage)
			{
				play.setIcon(pauseimage);
				clip.start();
			}
			else if(play.getIcon() == pauseimage)
			{
				play.setIcon(playimage);
				clip.stop();
			}
		}
		
		else if(e.getSource() == forw)
		{
			clip.setMicrosecondPosition(clip.getMicrosecondPosition() + 10*1000000);
		}
		
		else if(e.getSource() == back)
		{
			clip.setMicrosecondPosition(clip.getMicrosecondPosition() - 10*1000000);
		}
		
		else if(e.getSource() == next)
		{
			clip.stop();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			String song_path = "";
			String song_name = "";
			String image = "";
			try {
				if(!rs.next())
				{
					rs.first();
				}
				song_path = rs.getString("song_path");
				song_name = rs.getString("song_name");
				image = rs.getString("image");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				SongSelect(song_path,song_name,image);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				e1.printStackTrace();
			}
			
			if(play.getIcon() == pauseimage)
				clip.start();
		}
		
		else if(e.getSource() == prev)
		{
			clip.stop();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			String song_path = "";
			String song_name = "";
			String image = "";
			try {
				if(!rs.previous())
				{
					rs.last();
				}
				song_path = rs.getString("song_path");
				song_name = rs.getString("song_name");
				image = rs.getString("image");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				SongSelect(song_path,song_name,image);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				e1.printStackTrace();
			}
			
			if(play.getIcon() == pauseimage)
				clip.start();
		}
	}
}