package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector2f;

import model.Stage;
import model.Transformable;

public class WorldRotator extends MouseMotionAdapter 
{
	public static final double MAX_ANGLE = Math.PI / 6;
	private MouseEvent lastDragEvent;
	private final Canvas3D _targetCanvas;
	private final Stage _targetStage;
	
	private static float xRot;
	private static float zRot;
	
	public WorldRotator(final Canvas3D theTargetCanvas, final Stage theTargetStage)
	{
		super();
		_targetCanvas = theTargetCanvas;
		_targetStage = theTargetStage;
		xRot = 0;
		zRot = 0;
	}

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
            	xRot += deltaX;
            	Transform3D rotationTransform = new Transform3D();
            	rotationTransform.rotX(deltaX);
                for (final Transformable t : _targetStage.getTransformables()) {
                    t.transform(rotationTransform);
                } 
            }
            
            if (Math.abs(zRot + deltaZ) < Math.abs(zRot) || Math.abs(zRot) < MAX_ANGLE)
            {
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

    public void mouseMoved(MouseEvent e)
    {
        lastDragEvent = null;
    }
}
