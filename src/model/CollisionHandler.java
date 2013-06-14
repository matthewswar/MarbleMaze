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
        final Point3f[] vertices = o.getVertices();
        final Vector3f[] axes = o.getAxes();
        final Vector3f min = new Vector3f();
        final Vector3f max = new Vector3f();
        getMinAndMax(vertices, axes, min, max);
        
        final Vector3f pp = new Vector3f(m.position.dot(axes[0]), m.position.dot(axes[1]), m.position.dot(axes[2]));
        
        final Vector3f closestPoint = new Vector3f(pp);
        if (closestPoint.x < min.x) closestPoint.x = min.x; 
        if (closestPoint.x > max.x) closestPoint.x = max.x; 
        if (closestPoint.y < min.y) closestPoint.y = min.y; 
        if (closestPoint.y > max.y) closestPoint.y = max.y; 
        if (closestPoint.z < min.z) closestPoint.z = min.z; 
        if (closestPoint.z > max.z) closestPoint.z = max.z;
        
        final CollisionInfo ci = new CollisionInfo();
        ci.normal = new Vector3f();
        
        if (closestPoint.equals(pp)) {
            // Sphere center is inside of box or exactly on face/edge/vertex.
            // This should be rather rare.
            
            // Find point and face closest to center of sphere.
            int closestFace = 0;
            float minDistance = closestPoint.x - min.x;
            float temp;
            if ((temp = max.x - closestPoint.x) < minDistance) { minDistance = temp; closestFace = 1; };
            if ((temp = closestPoint.y - min.y) < minDistance) { minDistance = temp; closestFace = 2; };
            if ((temp = max.y - closestPoint.y) < minDistance) { minDistance = temp; closestFace = 3; };
            if ((temp = closestPoint.z - min.z) < minDistance) { minDistance = temp; closestFace = 4; };
            if ((temp = max.z - closestPoint.x) < minDistance) { minDistance = temp; closestFace = 5; };
            
            // Push the closest point, currently inside OBB, onto the closest face.
            switch (closestFace) {
            case 0: closestPoint.x = min.x; break;
            case 1: closestPoint.x = max.x; break;
            case 2: closestPoint.y = min.y; break;
            case 3: closestPoint.y = max.y; break;
            case 4: closestPoint.z = min.z; break;
            case 5: closestPoint.z = max.z; break;
            }
            
            if (closestPoint.equals(pp)) {
                // Sphere center is exactly on face/edge/vertex. Approximate collision
                // normal as a normal of one of the faces the sphere's center lies on.
                // This should be exceedingly rare, so approximation is more than fine.
                ci.normal = new Vector3f(axes[closestFace / 2]);
                ci.depth = -m.radius;
                
                if (closestFace % 2 == 0) {
                    ci.normal.negate();
                }
            } else {
                // Sphere center is inside box.
                // Get closest point in world coordinates.
                Vector3f cp = new Vector3f();
                cp.scaleAdd(closestPoint.x, axes[0], cp);
                cp.scaleAdd(closestPoint.y, axes[1], cp);
                cp.scaleAdd(closestPoint.z, axes[2], cp);
                
                ci.normal = new Vector3f();
                ci.normal.scaleAdd(-1, m.position, cp);
                ci.depth = -m.radius - ci.normal.length();
                ci.normal.normalize();
            }
        } else {
            // Get closest point in world coordinates.
            Vector3f cp = new Vector3f();
            cp.scaleAdd(closestPoint.x, axes[0], cp);
            cp.scaleAdd(closestPoint.y, axes[1], cp);
            cp.scaleAdd(closestPoint.z, axes[2], cp);
            
            ci.normal = new Vector3f();
            ci.normal.scaleAdd(-1, cp, m.position);
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

	private static void getMinAndMax(Point3f[] vertices, Vector3f[] axes, Vector3f min, Vector3f max) {
        // Project each vertex onto each axis, find out the min and max components in each axes.
        min.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        max.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        
        for (final Point3f point : vertices) {
            final Vector3f vertex = new Vector3f(point);
            float c = vertex.dot(axes[0]);
            if (c < min.x) {
                min.x = c;
            } else if (c > max.x) {
                max.x = c;
            }
            
            c = vertex.dot(axes[1]);
            if (c < min.y) {
                min.y = c;
            } else if (c > max.y) {
                max.y = c;
            }
            
            c = vertex.dot(axes[2]);
            if (c < min.z) {
                min.z = c;
            } else if (c > max.z) {
                max.z = c;
            }
        }
   }
}