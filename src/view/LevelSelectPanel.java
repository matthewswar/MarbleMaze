package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import model.LevelScanner;
import model.Stage;

@SuppressWarnings("serial")
public class LevelSelectPanel extends JPanel 
{
	private boolean _isDefault;
	private String _levelPath;
	
	public LevelSelectPanel()
	{
		super();
		_isDefault = true;
		setLayout(new BorderLayout());
	}
	
	public void constructPanel()
	{
		final JPanel levelOptions = new JPanel(new GridLayout(2, 1));
		final JPanel buttonOptions = new JPanel(new FlowLayout());
		final JRadioButton pickDefault = new JRadioButton("Default Level");
		final JRadioButton pickCustom = new JRadioButton("Custom Level");
		final JButton chooseFile = new JButton("No level selected");
		final ButtonGroup bg = new ButtonGroup();
		bg.add(pickDefault);
		bg.add(pickCustom);
		
		pickDefault.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent theEvent) 
			{
				_isDefault = true;
			}
		});
		
		pickCustom.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent theEvent)
			{
				_isDefault = false;
			}
		});
		
		chooseFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent theEvent)
			{
				final JFileChooser fc = new JFileChooser();
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION);
				{
					_levelPath = fc.getSelectedFile().getAbsolutePath();
					chooseFile.setText(fc.getSelectedFile().getName());
				}
			}
		});
		
		pickDefault.setSelected(_isDefault);
		
		buttonOptions.add(pickCustom);
		buttonOptions.add(chooseFile);
		levelOptions.add(pickDefault);
		levelOptions.add(buttonOptions);
		add(levelOptions);
	}
	
	public Stage getStage() throws IOException
	{
		if (_isDefault)
		{
			return LevelScanner.loadLevel("testlevel.png");
		}
		else
		{
			return LevelScanner.loadLevel(_levelPath);
		}
	}
	
}
