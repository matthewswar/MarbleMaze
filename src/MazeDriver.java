/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import view.LevelSelectPanel;
import view.WorldDirector;


public class MazeDriver 
{	
	public static void main(final String[] theArgs)
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run()
			{
				final String[] choices = {"OK",  "Exit"};
				final LevelSelectPanel lsp = new LevelSelectPanel();
				lsp.constructPanel();
				final int result = JOptionPane.showOptionDialog(null, lsp, "Select Level",
								JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
								choices, null);
				
				if (result == JOptionPane.YES_OPTION)
				{
					try 
					{
						new WorldDirector(lsp.getStage()).start();
					} 
					catch (IOException e) 
					{
						JOptionPane.showMessageDialog(null, "Cannot find level file.");
					}
				}
			}
		});
	}

}
