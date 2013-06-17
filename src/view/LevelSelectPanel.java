/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import model.LevelScanner;
import model.Stage;

/**
 * 
 * @author Nottingham, Swartzendruber
 * @version June 16th, 2013
 * 
 * A panel that is displayed when the program starts. Allows the user to
 * select the level based on a PNG file.
 *
 */
@SuppressWarnings("serial")
public class LevelSelectPanel extends JPanel 
{
	/**
	 * The radio button that picks the default level.
	 */
	private final JRadioButton _pickDefault;
	
	/**
	 * The radio button that picks the user selected level.
	 */
	private final JRadioButton _pickCustom;
	
	/**
	 * The path to the custom level.
	 */
	private String _levelPath;
	
	/**
	 * Creates a new panel without populating it.
	 */
	public LevelSelectPanel()
	{
		super();
		_levelPath = "";
		_pickDefault = new JRadioButton("Default Level");
		_pickCustom = new JRadioButton("Custom Level");
		setLayout(new BorderLayout());
	}
	
	/**
	 * Fills the contents of the panel so it can be displayed.
	 */
	public void constructPanel()
	{
		final JPanel levelOptions = new JPanel(new GridLayout(3, 1));
		final JPanel buttonOptions = new JPanel(new FlowLayout());
		final JPanel helpOptions = new JPanel(new FlowLayout());
		final JButton chooseFile = new JButton("No level selected");
		final JButton viewHelp = new JButton("Basic");
		final JButton advHelp = new JButton("Advanced");
		final JLabel info = new JLabel("Help: ");
		final ButtonGroup bg = new ButtonGroup();
		bg.add(_pickDefault);
		bg.add(_pickCustom);
		
		chooseFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent theEvent)
			{
				// Pick a file to load.
				final JFileChooser fc = 
						new JFileChooser(Paths.get("").toAbsolutePath().toString());
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION);
				{
				    final File file = fc.getSelectedFile();
				    if (file != null) {
    					_levelPath = file.getAbsolutePath();
    					chooseFile.setText(file.getName());
    					_pickCustom.setSelected(true);
				    }
				}
			}
		});
		
		viewHelp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent theEvent)
			{
				// Read the basic readme.
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
		
		advHelp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent theEvent)
			{
				// Read the advanced readme.
				try 
				{
					final Scanner readme = new Scanner(new File("levelCreationAdvReadme.txt"));
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
		helpOptions.add(info);
		helpOptions.add(viewHelp);
		helpOptions.add(advHelp);
		levelOptions.add(_pickDefault);
		levelOptions.add(buttonOptions);
		levelOptions.add(helpOptions);
		add(levelOptions);
	}
	
	/**
	 * @return A stage that is populated with blocks and obstacles.
	 * @throws IOException The file is not found.
	 */
	public Stage getStage() throws IOException
	{
		if (_pickDefault.isSelected())
		{
			return LevelScanner.loadLevel("easy.png");
		}
		else
		{
			return LevelScanner.loadLevel(_levelPath);
		}
	}
	
}
