/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import model.Marble;

/**
 * 
 * @author Nottingham, Swartzendruber
 * @version June 16th, 2013
 * 
 * Handles keyboard input from the player during gameplay.
 *
 */
public class KeyboardHandler extends KeyAdapter 
{
	/**
	 * Whether or not to run the program is debug mode, some features 
	 * that did not make the cut are enabled in debug mode.
	 */
	public static final boolean DEBUG = false;
	
	/**
	 * The speed that rotates the camera (debug feature) 
	 */
	public static final float ROTATION_SPEED = 0.05f;
	
	/**
	 * The marble the player controls.
	 */
	private final Marble _targetMarble;
	
	/**
	 * The Transform Group that belongs to the camera.
	 */
	private final TransformGroup _targetTG;
	
	/**
	 * @param theMarble The marble the player controls.
	 * @param theTG The TG of the camera.
	 */
	public KeyboardHandler(final Marble theMarble, final TransformGroup theTG)
	{
		_targetMarble = theMarble;
		_targetTG = theTG;
	}
	
	/**
	 * {@inheritDoc}
	 */
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
			
			if (DEBUG)
			{
				final Transform3D t3d = new Transform3D();
				_targetTG.getTransform(t3d);
				t3d.mul(rotation);
				_targetTG.setTransform(t3d);
			}
		}
	}
	
	/**
	 * @param theCode The code of the key pressed.
	 * @return Whether a valid key is pressed.
	 */
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
