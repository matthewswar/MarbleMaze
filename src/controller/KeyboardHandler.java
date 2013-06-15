package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import model.Marble;

public class KeyboardHandler extends KeyAdapter 
{
	public static final float ROTATION_SPEED = 0.05f;
	private final Marble _targetMarble;
	private final TransformGroup _targetTG;
	
	public KeyboardHandler(final Marble theMarble, final TransformGroup theTG)
	{
		_targetMarble = theMarble;
		_targetTG = theTG;
	}
	
	public void keyPressed(final KeyEvent theEvent)
	{
		
		if (isValidKeyDown(theEvent.getKeyCode()))
		{
			final Transform3D rotation = new Transform3D();
			switch (theEvent.getKeyCode())
			{
			case KeyEvent.VK_SPACE:
				_targetMarble.reset();
				break;
			case KeyEvent.VK_LEFT:
				rotation.rotY(ROTATION_SPEED);
				break;
			case KeyEvent.VK_RIGHT:
				rotation.rotY(-ROTATION_SPEED);
				break;
			case KeyEvent.VK_UP:
				rotation.rotZ(ROTATION_SPEED);
				break;
			case KeyEvent.VK_DOWN:
				rotation.rotZ(-ROTATION_SPEED);
				break;
			case KeyEvent.VK_SHIFT:
				rotation.rotY(ROTATION_SPEED);
				break;
			case KeyEvent.VK_CONTROL:
				rotation.rotY(-ROTATION_SPEED);
			}
			
			final Transform3D t3d = new Transform3D();
			_targetTG.getTransform(t3d);
			t3d.mul(rotation);
			_targetTG.setTransform(t3d);
		}
	}
	
	private boolean isValidKeyDown(final int theCode)
	{
		return theCode == KeyEvent.VK_SPACE ||
				theCode == KeyEvent.VK_UP ||
				theCode == KeyEvent.VK_DOWN ||
				theCode == KeyEvent.VK_LEFT ||
				theCode == KeyEvent.VK_RIGHT ||
				theCode == KeyEvent.VK_SHIFT ||
				theCode == KeyEvent.VK_CONTROL;
	}
}
