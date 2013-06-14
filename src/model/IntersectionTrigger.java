package model;

import javax.media.j3d.Transform3D;


public abstract class IntersectionTrigger extends Trigger implements Transformable {
    private OrientedBoundingBox _OBB;
    private Marble _marble;
    
    public IntersectionTrigger(Marble m, OrientedBoundingBox obb) {
        _OBB = obb;
        _marble = m;
    }
    
    public void transform(final Transform3D transform) {
        _OBB.transform(transform);
    }
    
    public OrientedBoundingBox getOBB() {
        return _OBB;
    }

    public boolean condition() {
        return CollisionHandler.getCollisionInfo(_marble, _OBB).depth < 0;
    }
 }