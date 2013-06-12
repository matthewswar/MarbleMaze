package model;
import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

/*
 * View and model for representing a box. The underlying OrientedBoundingBox
 * model is public in order to enable arbitrary changes to its Transform3D.
 * Unfortunately, this requires that users of the class call updateTransform()
 * after modifying the Transform3D.
 */
public class Box extends BranchGroup {
	public static final float DIMENSION = 5.0f;
    private static final Vector3f DEFAULT_DIMENSION_VECTOR =
            new Vector3f(DIMENSION, DIMENSION, DIMENSION);
    private static final Vector3f ZERO_VECTOR = new Vector3f();
    private static final Color3f AMBIENT = new Color3f(.02f, .02f, .02f);
    private static final Color3f EMISSIVE = new Color3f(0, 0, 0);
    private static final Color3f SPECULAR = new Color3f(1, 1, 1);
    private static final float SHININESS = 100;
    
    public OrientedBoundingBox OBB;
    private TransformGroup _TG;
    
     /*
     * Unit box is scaled, rotated, then translated to position. Rotation occurs
     * in X-Y-Z order by angles specified in rotation vector.
     */
    
    public Box() {
        this(ZERO_VECTOR, DEFAULT_DIMENSION_VECTOR, ZERO_VECTOR);
    }
    
    public Box(Vector3f position) {
        this(position, DEFAULT_DIMENSION_VECTOR, ZERO_VECTOR);
    }
    
    public Box(Vector3f position, Vector3f size) {
        this(position, size, ZERO_VECTOR);
    }
 
    public Box(Vector3f position, Vector3f dimensions, Vector3f rotation) {
        OBB = new OrientedBoundingBox(position, dimensions, rotation);
        _TG = new TransformGroup();
        _TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _TG.addChild(new WireframeBox(1, 1, 1));
        addChild(_TG);
        updateTransform();
    }
    
    public Box(Appearance appearance) {
        this(ZERO_VECTOR, DEFAULT_DIMENSION_VECTOR, ZERO_VECTOR, appearance); 
    }
    
    public Box(Vector3f position, Appearance appearance) {
        this(position, DEFAULT_DIMENSION_VECTOR, ZERO_VECTOR, appearance); 
    }
    
    public Box(Vector3f position, Vector3f dimensions, Appearance appearance) {
        this(position, dimensions, ZERO_VECTOR, appearance); 
    }
    
   public Box(Vector3f position, Vector3f dimensions, Vector3f rotation, Appearance appearance) {
        OBB = new OrientedBoundingBox(position, dimensions, rotation);
        _TG = new TransformGroup();
        _TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _TG.addChild(new com.sun.j3d.utils.geometry.Box(.5f, .5f, .5f, appearance));
        addChild(_TG);
        updateTransform();
    }
    
    public Box(Color3f color) {
        this(ZERO_VECTOR, DEFAULT_DIMENSION_VECTOR, ZERO_VECTOR, color); 
    }
    
    public Box(Vector3f position, Color3f color) {
        this(position, DEFAULT_DIMENSION_VECTOR, ZERO_VECTOR, color); 
    }
    
    public Box(Vector3f position, Vector3f dimensions, Color3f color) {
        this(position, dimensions, ZERO_VECTOR, color); 
    }
    
    public Box(Vector3f position, Vector3f dimensions, Vector3f rotation, Color3f color) {
        OBB = new OrientedBoundingBox(position, dimensions, rotation);
        _TG = new TransformGroup();
        _TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _TG.addChild(createShape(color));
        addChild(_TG);
        updateTransform();
    }
    
    public void updateTransform() {
        _TG.setTransform(OBB.T3D);
        OBB.updateTransform();
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
