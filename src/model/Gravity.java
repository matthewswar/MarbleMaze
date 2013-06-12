package model;

import javax.vecmath.Vector3f;

public class Gravity extends Force {
    private static final Vector3f DEFAULT_FORCE = new Vector3f(0, -9.8f, 0);
    
    private final Vector3f _force;
    
    public Gravity() {
        _force = DEFAULT_FORCE;
    }
    
    public Gravity(Vector3f direction, float magnitude) {
        _force = new Vector3f(direction);
        _force.normalize();
        _force.scale(magnitude);
    }
    
    @Override
    protected void execute(Marble m) {
        Vector3f force = new Vector3f(_force);
        force.scale(m.mass);
        m.forceAccumulator.add(force);
    }
}
