package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.Marble;

public class KeyboardHandler extends KeyAdapter 
{
	private final Marble _targetMarble;
	
	public KeyboardHandler(final Marble theMarble)
	{
		_targetMarble = theMarble;
	}
	
	public void keyPressed(final KeyEvent theEvent)
	{
		if (theEvent.getKeyCode() == KeyEvent.VK_SPACE)
		{
			_targetMarble.reset();
		}
	}
}
