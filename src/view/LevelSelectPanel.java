package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import model.LevelScanner;
import model.Stage;

@SuppressWarnings("serial")
public class LevelSelectPanel extends JPanel 
{
	private final JRadioButton _pickDefault;
	private final JRadioButton _pickCustom;
	private String _levelPath;
	
	public LevelSelectPanel()
	{
		super();
		_levelPath = "";
		_pickDefault = new JRadioButton("Default Level");
		_pickCustom = new JRadioButton("Custom Level");
		setLayout(new BorderLayout());
	}
	
	public void constructPanel()
	{
		final JPanel levelOptions = new JPanel(new GridLayout(2, 1));
		final JPanel buttonOptions = new JPanel(new FlowLayout());
		final JButton chooseFile = new JButton("No level selected");
		final JButton viewHelp = new JButton("?");
		final ButtonGroup bg = new ButtonGroup();
		bg.add(_pickDefault);
		bg.add(_pickCustom);
		
		chooseFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent theEvent)
			{
				final JFileChooser fc = 
						new JFileChooser(Paths.get("").toAbsolutePath().toString());
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION);
				{
					_levelPath = fc.getSelectedFile().getAbsolutePath();
					chooseFile.setText(fc.getSelectedFile().getName());
					_pickCustom.setSelected(true);
				}
			}
		});
		
		viewHelp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent theEvent)
			{
				try 
				{
					final Scanner readme = new Scanner(new File("levelCreationReadme.txt"));
					readme.useDelimiter("\\Z");
					final String contents = readme.next();
					readme.close();
					JOptionPane.showMessageDialog(null, contents);
				}
				catch (FileNotFoundException e) 
				{
					JOptionPane.showMessageDialog(null, "Readme file not found.");
				}
			}
		});
		
		
		_pickDefault.setSelected(true);
		
		buttonOptions.add(_pickCustom);
		buttonOptions.add(chooseFile);
		buttonOptions.add(viewHelp);
		levelOptions.add(_pickDefault);
		levelOptions.add(buttonOptions);
		add(levelOptions);
	}
	
	public Stage getStage() throws IOException
	{
		if (_pickDefault.isSelected())
		{
			return LevelScanner.loadLevel("testlevel.png");
		}
		else
		{
			return LevelScanner.loadLevel(_levelPath);
		}
	}
	
}
