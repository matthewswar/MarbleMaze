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
    
    private Transform3D T3D = new Transform3D();

    public OrientedBoundingBox(Vector3f position) {
        T3D.setTranslation(position);
    }
    
    /*
     * Unit box is scaled then translated to position.
     */
    public OrientedBoundingBox(Vector3f position, Vector3f dimensions) {
        T3D.setTranslation(position);
        T3D.setScale(new Vector3d(dimensions));
    }  
    
    /*
     * Unit box is scaled, rotated, then translated to position. Rotation occurs
     * in X-Y-Z order by angles specified in rotation vector.
     */
    public OrientedBoundingBox(Vector3f position, Vector3f dimensions, Vector3f rotation) {
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
        T3D = new Transform3D(obb.T3D);
    }
     
    public Point3f[] getVertices() {
        final Point3f[] vertices = new Point3f[8];
        for (int i = 0; i < 8; i++) {
            vertices[i] = new Point3f(BASE_VERTICES[i]);
            T3D.transform(vertices[i]);
        }
        
        return vertices;
    }
    
    public Vector3f[] getNormalizedAxes() {
        final Vector3f[] axes = getAxes();
        axes[0].normalize();
        axes[1].normalize();
        axes[2].normalize();
        return axes;
    }
   
    public Vector3f[] getAxes() {
        final Vector3f[] axes = new Vector3f[3];
        axes[0] = new Vector3f();
        axes[0].scaleAdd(-1, BASE_VERTICES[0], BASE_VERTICES[1]);
        T3D.transform(axes[0]);
            
        axes[1] = new Vector3f();
        axes[1].scaleAdd(-1, BASE_VERTICES[0], BASE_VERTICES[3]);
        T3D.transform(axes[1]);
            
        axes[2] = new Vector3f();
        axes[2].scaleAdd(-1, BASE_VERTICES[0], BASE_VERTICES[4]);
        T3D.transform(axes[2]);
        
        return axes;
    }
   
    public Point3f getCenter() {
        final Point3f center = new Point3f(BASE_VERTICES[0]);
        center.add(BASE_VERTICES[6]);
        center.scale(.5f);
        T3D.transform(center);
        return center;
    }
    
    @Override
    public void transform(Transform3D transform) {
        T3D.mul(transform, T3D);
    }
    
    public Transform3D getTransform() {
        return T3D;
    }
}
