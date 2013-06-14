package model;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;

public class Push extends Force implements Transformable {
    private static final Vector3f DEFAULT_FORCE = new Vector3f(0, -9.8f, 0);
    
    private final Vector3f _force;
    
    public Push() {
        _force = DEFAULT_FORCE;
    }
    
    public Push(Vector3f direction, float magnitude) {
        _force = new Vector3f(direction);
        _force.normalize();
        _force.scale(magnitude);
    }
    
    public Vector3f getForce() {
        return _force;
    }
    
    @Override
    protected void execute(Marble m) {
        Vector3f force = new Vector3f(_force);
        force.scale(m.mass);
        m.forceAccumulator.add(force);
    }

    @Override
    public void transform(Transform3D transform) {
        transform.transform(_force);
    }
}