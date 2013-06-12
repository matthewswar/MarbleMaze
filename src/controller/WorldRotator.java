package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector2f;

import model.Box;
import model.HalfSpace;
import model.Stage;

public class WorldRotator extends MouseMotionAdapter 
{
	private MouseEvent lastDragEvent;
	private final Canvas3D _targetCanvas;
	private final Stage _targetStage;
	
	public WorldRotator(final Canvas3D theTargetCanvas, final Stage theTargetStage)
	{
		super();
		_targetCanvas = theTargetCanvas;
		_targetStage = theTargetStage;
	}

    public void mouseDragged(MouseEvent e)
    {
        if (lastDragEvent != null) 
        {
            Vector2f lastMouseVector = new Vector2f(lastDragEvent.getX() - _targetCanvas.getWidth() / 2, lastDragEvent.getY() - _targetCanvas.getHeight() / 2);
            Vector2f currentMouseVector = new Vector2f(e.getX() - _targetCanvas.getWidth() / 2, e.getY() - _targetCanvas.getHeight() / 2);
            Vector2f deltaVector = new Vector2f();
            deltaVector.scaleAdd(-1, lastMouseVector, currentMouseVector);
            
            Transform3D rotationTransform = new Transform3D();
            Transform3D rotZ = new Transform3D();
            rotationTransform.rotX(deltaVector.y / 128);
            rotZ.rotZ(deltaVector.x / -128);
            rotationTransform.mul(rotZ);
            

            // Rotate the extent
            Transform3D extT3D = new Transform3D();
            _targetStage.getTG().getTransform(extT3D);
            extT3D.mul(rotationTransform, extT3D);
            _targetStage.getTG().setTransform(extT3D);
            
            
            // Rotate each boundary
            for (HalfSpace hs : _targetStage.getBoundaries()) 
            {    
                rotationTransform.transform(hs.normal);
                rotationTransform.transform(hs.dim);
            }
            
            for (Box b : _targetStage.getWalls())
            {
                b.OBB.T3D.mul(rotationTransform, b.OBB.T3D);
                b.updateTransform();
            }
        }
        
        lastDragEvent = e;
    }

    public void mouseMoved(MouseEvent e) {
        lastDragEvent = null;
    }
	
}
