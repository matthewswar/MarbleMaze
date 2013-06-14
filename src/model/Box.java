package model;
import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;

/*
 * View and model for representing a box. Users should call updateTransform()
 * after modifying the Transform3D returned by getTransform() or after modifying
 * the Transform of the OBB returned by getOBB(). Modifying this class through
 * transform() does not require calling updateTransform().
 */
public class Box extends BranchGroup implements Transformable {
	public static final float DIMENSION = 5.0f;
    private static final Color3f AMBIENT = new Color3f(.02f, .02f, .02f);
    private static final Color3f EMISSIVE = new Color3f(0, 0, 0);
    private static final Color3f SPECULAR = new Color3f(1, 1, 1);
    private static final float SHININESS = 100;
    
    private OrientedBoundingBox OBB;
    private TransformGroup _TG;
    
    public Box(OrientedBoundingBox obb) {
        init(obb, new WireframeBox(1, 1, 1));
    }
    
    public Box(OrientedBoundingBox obb, Appearance appearance) {
        init(obb, new com.sun.j3d.utils.geometry.Box(.5f, .5f, .5f, appearance));
    }
    
    public Box(OrientedBoundingBox obb, Color3f color) {
        init(obb, createShape(color));
    }
    
    @Override
    public void transform(Transform3D transform) {
        OBB.transform(transform);
        _TG.setTransform(OBB.getTransform());
    }

    public void updateTransform() {
        _TG.setTransform(OBB.getTransform());
        OBB.updateTransform();
    }
    
    public Transform3D getTransform() {
        return OBB.getTransform();
    }
    
    public OrientedBoundingBox getOBB() {
        return OBB;
    }
    
    private void init(OrientedBoundingBox obb, Node shape) {
         OBB = obb;
        _TG = new TransformGroup();
        _TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _TG.addChild(shape);
        addChild(_TG);
        updateTransform();
    }

    private Node createShape(Color3f color) {
        if (color == null)
            color = new Color3f(Color.getHSBColor((float)Math.random(), 1, 1));
        Appearance appearance = new Appearance();
        appearance.setMaterial(new Material(AMBIENT, EMISSIVE, color,
                SPECULAR, SHININESS));
        return new com.sun.j3d.utils.geometry.Box(.5f, .5f, .5f, appearance);
    }
}
