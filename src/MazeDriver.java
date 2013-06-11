import javax.swing.SwingUtilities;

import view.WorldDirector;


public class MazeDriver 
{
	public static void main(final String[] theArgs)
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run()
			{
				new WorldDirector().start();
			}
		});
	}

}
