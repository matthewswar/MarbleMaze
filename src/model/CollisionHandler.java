package model;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class CollisionHandler {
	public static final float COEFFICIENT_OF_RESTITUTION = 0.3f;

	public static void checkMarbleCollision(Marble m, OrientedBoundingBox o) {
        final CollisionInfo ci = getCollisionInfo(m, o);   
        if (ci.depth < 0) {
            resolveMarbleCollision(m, ci);
        }
    }
	
    public static CollisionInfo getCollisionInfo(Marble m, OrientedBoundingBox o) {
        // OBB axes not normalized. Will normalize below.
        final Vector3f[] axes = o.getAxes();
        
        // Closest point in OBB to sphere's center. Start it at OBB's center.
        final Point3f closestPointOnOBB = o.getCenter();
        
        // Vector from box center to sphere center.
        final Vector3f centerToCenter = new Vector3f(m.position);
        centerToCenter.sub(closestPointOnOBB);
        
        // Component of center to center vector in a given OBB axis.
        final float[] componentInAxis = new float[3];
        
        // Signed distance to closest face in a given OBB axis.
        final float[] distanceToFace = new float[3];
        
        // Project centerToCenter onto each axis. If sphere center beyond extent
        // of OBB, clamp closest point to surface of OBB in that axis.
        for (int i = 0; i < axes.length; i++) {
            final Vector3f axis = axes[i];
            final float halfBoxLength = axis.length() / 2;
            axis.normalize();
            componentInAxis[i] = centerToCenter.dot(axis);
            float component = componentInAxis[i];
            
            if (component > 0) {
                distanceToFace[i] = halfBoxLength - component;
                
                if (component > halfBoxLength) {
                    component = halfBoxLength;
                }
            } else {
                distanceToFace[i] = -halfBoxLength - component;
                
                if (component < -halfBoxLength) {
                    component = -halfBoxLength;
                }
            }
           
            closestPointOnOBB.scaleAdd(component, axis, closestPointOnOBB);
        }
        
        final CollisionInfo ci = new CollisionInfo();
        ci.normal = new Vector3f();
        
        if (closestPointOnOBB.epsilonEquals(m.position, .0001f)) {
            // Sphere center is inside of box or exactly on face/edge/vertex.
            // This should be rather rare.
            
            // Find face closest to center of sphere, then push the closest
            // point onto it.
            final float a = Math.abs(distanceToFace[0]);
            final float b = Math.abs(distanceToFace[1]);
            final float c = Math.abs(distanceToFace[2]);
            int closestFaceIndex = 0;
            
            if (b <= a && b <= c) {
                closestFaceIndex = 1;
            } else if (c <= a) {
                closestFaceIndex = 2;
            }
            
            closestPointOnOBB.scaleAdd(distanceToFace[closestFaceIndex], axes[closestFaceIndex]);
            
            if (closestPointOnOBB.epsilonEquals(m.position, .0001f)) {
                // Sphere center is exactly on face/edge/vertex. Approximate collision
                // normal as a normal of one of the faces the sphere's center lies on.
                // This should be exceedingly rare, so approximation is more than fine.
                ci.normal = new Vector3f(axes[closestFaceIndex]);
                ci.depth = -m.radius;
                
                if (componentInAxis[closestFaceIndex] < 0) {
                    ci.normal.negate();
                }
            } else {
                // Sphere center was inside of box.
                ci.normal = new Vector3f();
                ci.normal.scaleAdd(-1, m.position, closestPointOnOBB);
                ci.depth = -m.radius - ci.normal.length();
                ci.normal.normalize();
            }
        } else {
            // Sphere center was outside of box.
            ci.normal = new Vector3f();
            ci.normal.scaleAdd(-1, closestPointOnOBB, m.position);
            ci.depth = -m.radius + ci.normal.length();
            ci.normal.normalize();
        }
        
        return ci;
    }
    
    private static void resolveMarbleCollision(final Marble m, CollisionInfo ci) {
        float v_f = ci.normal.dot(m.velocity);
        float v_i_squared = Math.max(0, v_f * v_f - 2 * m.forceAccumulator.dot(ci.normal) * ci.depth);
        m.velocity.scaleAdd(-v_f + COEFFICIENT_OF_RESTITUTION * (float)Math.sqrt(v_i_squared), ci.normal, m.velocity);
        m.position.scaleAdd(-ci.depth, ci.normal, m.position);
    }
}