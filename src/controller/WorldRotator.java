/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector2f;

import model.Stage;
import model.Transformable;

/**
 * 
 * @author Nottingham, Swartzendruber
 * @version June 16th, 2013
 * 
 * Handles mouse controls that rotate the world and everything inside of it.
 *
 */
public class WorldRotator extends MouseMotionAdapter 
{
	/**
	 * The maximum angle the world is allowed to rotate.
	 */
	public static final double MAX_ANGLE = Math.PI / 6;
	
	/**
	 * The state of the last mouse drag event.
	 */
	private MouseEvent lastDragEvent;
	
	/**
	 * The view that renders everything within the world.
	 */
	private final Canvas3D _targetCanvas;
	
	/**
	 * The level to be rotated.
	 */
	private final Stage _targetStage;
	
	/**
	 * The current rotation around the x-axis.
	 */
	private static float xRot;
	
	/**
	 * The current rotation around the z-axis.
	 */
	private static float zRot;
	
	/**
	 * Creates an object that targets the given canvas and stage for rotation.
	 * 
	 * @param theTargetCanvas Canvas that renders the world.
	 * @param theTargetStage The stage to be rotated.
	 */
	public WorldRotator(final Canvas3D theTargetCanvas, final Stage theTargetStage)
	{
		super();
		_targetCanvas = theTargetCanvas;
		_targetStage = theTargetStage;
		xRot = 0;
		zRot = 0;
	}

	/**
	 * {@inheritDoc}
	 */
    public void mouseDragged(MouseEvent e)
    {
        if (lastDragEvent != null) 
        {
            Vector2f lastMouseVector = new Vector2f(lastDragEvent.getX() - _targetCanvas.getWidth() / 2, lastDragEvent.getY() - _targetCanvas.getHeight() / 2);
            Vector2f currentMouseVector = new Vector2f(e.getX() - _targetCanvas.getWidth() / 2, e.getY() - _targetCanvas.getHeight() / 2);
            Vector2f deltaVector = new Vector2f();
            deltaVector.scaleAdd(-1, lastMouseVector, currentMouseVector);
            
            final float deltaX = deltaVector.y / 128;
            final float deltaZ = deltaVector.x / -128;
            
            if (Math.abs(xRot + deltaX) < Math.abs(xRot) || Math.abs(xRot) < MAX_ANGLE)
            {
            	// Rotate along the x-axis if able
            	xRot += deltaX;
            	Transform3D rotationTransform = new Transform3D();
            	rotationTransform.rotX(deltaX);
                for (final Transformable t : _targetStage.getTransformables()) {
                    t.transform(rotationTransform);
                } 
            }
            
            if (Math.abs(zRot + deltaZ) < Math.abs(zRot) || Math.abs(zRot) < MAX_ANGLE)
            {
            	// Rotate along the z-axis if able
            	zRot += deltaZ;
            	Transform3D rotationTransform = new Transform3D();
            	rotationTransform.rotZ(deltaZ);
                for (final Transformable t : _targetStage.getTransformables()) {
                    t.transform(rotationTransform);
                } 
            }
            
            _targetStage.checkCollisions();
        }
        
        lastDragEvent = e;
    }

    /**
     * {@inheritDoc}
     */
    public void mouseMoved(MouseEvent e)
    {
        lastDragEvent = null;
    }
}
