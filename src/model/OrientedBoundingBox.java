package model;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/*
 * Model for an oriented bounding box. Users should call updateTransform() after
 * modifying the Transform3D returned by getTransform(). Modifying this class
 * through transform() does not require calling updateTransform().
 */
public class OrientedBoundingBox implements Transformable {    
    private static final Point3f[] BASE_VERTICES = {
        new Point3f(-.5f, -.5f, -.5f),
        new Point3f(.5f, -.5f, -.5f),
        new Point3f(.5f, .5f, -.5f),
        new Point3f(-.5f, .5f, -.5f),
        new Point3f(-.5f, -.5f, .5f),
        new Point3f(.5f, -.5f, .5f),
        new Point3f(.5f, .5f, .5f),
        new Point3f(-.5f, .5f, .5f),
    };
    
    private Point3f[] _vertices;
    private Vector3f[] _axes;
    private Transform3D T3D = new Transform3D();
    private boolean _isGoal;
    
    public OrientedBoundingBox()
    {
    	this(new Vector3f(), new Vector3f(), new Vector3f());
    }

    public OrientedBoundingBox(Vector3f position) {
        this(position, new Vector3f(), new Vector3f());
        
    }
    
    /*
     * Unit box is scaled then translated to position.
     */
    public OrientedBoundingBox(Vector3f position, Vector3f dimensions) {
        this(position, dimensions, new Vector3f());
    }  
    
    /*
     * Unit box is scaled, rotated, then translated to position. Rotation occurs
     * in X-Y-Z order by angles specified in rotation vector.
     */
    public OrientedBoundingBox(Vector3f position, Vector3f dimensions, Vector3f rotation) {
    	_isGoal = false;
        T3D.setTranslation(position);
        Transform3D temp = new Transform3D();
        temp.rotZ(rotation.z);
        T3D.mul(temp);
        temp.rotY(rotation.y);
        T3D.mul(temp);
        temp.rotX(rotation.x);
        T3D.mul(temp);
        temp.setScale(new Vector3d(dimensions));
        T3D.mul(temp);
    }
    
    public OrientedBoundingBox(OrientedBoundingBox obb) {
    	_isGoal = false;
        T3D = new Transform3D(obb.T3D);
    }
     
    public Point3f[] getVertices() {
        if (_vertices == null) {
            _vertices = new Point3f[8];
            for (int i = 0; i < 8; i++) {
                _vertices[i] = new Point3f(BASE_VERTICES[i]);
                T3D.transform(_vertices[i]);
            }
        }
        
        return _vertices;
    }
    
    public Vector3f[] getAxes() {
        if (_axes == null) {
            if (_vertices == null) {
                getVertices();
            }
            
            _axes = new Vector3f[3];
            _axes[0] = new Vector3f();
            _axes[0].scaleAdd(-1, _vertices[0], _vertices[1]);
            _axes[0].normalize();
            
            _axes[1] = new Vector3f();
            _axes[1].scaleAdd(-1, _vertices[0], _vertices[3]);
            _axes[1].normalize();
            
            _axes[2] = new Vector3f();
            _axes[2].scaleAdd(-1, _vertices[0], _vertices[4]);
            _axes[2].normalize();
        }
        
        return _axes;
    }
    
    public void updateTransform() {
        _vertices = null;
        _axes = null;
    }

    @Override
    public void transform(Transform3D transform) {
        T3D.mul(transform, T3D);
        updateTransform();
    }
    
    public Transform3D getTransform() {
        return T3D;
    }
    
    public void setIsGoal(final boolean theGoal)
    {
    	_isGoal = theGoal;
    }
    
    public boolean isGoal()
    {
    	return _isGoal;
    }
}
