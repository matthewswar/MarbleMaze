package model;

import javax.vecmath.Vector3f;

public class CollisionHandler 
{
	public static final float COEFFICIENT_OF_RESTITUTION = 0.3f; 
	public static void checkMarbleCollision(final Marble theMarble, final HalfSpace theBorder)
	{
        float distance = theBorder.normal.dot(theMarble.position) - theBorder.intercept - theMarble.radius;
        if (distance < 0 && inBounds(theMarble, theBorder)) 
        {
            // Use Torricelli's equation to approximate the particle's
            // velocity (v_i) at the time it contacts the halfspace.
            // v_f^2 = v_i^2 + 2 * acceleration * distance
            
            // Final velocity of the particle in the direction of the halfspace normal
            float v_f = theBorder.normal.dot(theMarble.velocity);
            // Velocity of the particle in the direction of the halfspace normal at the
            // time of contact, squared
            float v_i_squared = v_f * v_f - 2 * theMarble.forceAccumulator.dot(theBorder.normal) * distance;
            // If v_i_squared is less than zero, then the quantities involved are so small
            // that numerical inaccuracy has produced an impossible result.  The velocity
            // at the time of contact should therefore be zero.
            if (v_i_squared < 0)
                v_i_squared = 0;
            // Remove the incorrect velocity acquired after the contact and add the flipped
            // correct velocity.
            theMarble.velocity.scaleAdd(-v_f + COEFFICIENT_OF_RESTITUTION * (float)Math.sqrt(v_i_squared), theBorder.normal, theMarble.velocity);
            
            theMarble.position.scaleAdd(-distance, theBorder.normal, theMarble.position);
        }
	}
	
	
	private static boolean inBounds(final Marble theMarble, final HalfSpace theBorder)
	{
		final Vector3f pos = theMarble.position;
		final Vector3f dim = theBorder.dim;
		final Vector3f borderPos = theBorder.position;
		
		return pos.x >= (borderPos.x - dim.x / 2) &&
				pos.x < (borderPos.x + dim.x / 2) &&
				//pos.y >= (borderPos.y - dim.y / 2) &&
				//pos.y < (borderPos.y + dim.y / 2) &&
				pos.z >= (borderPos.z - dim.z / 2) &&
				pos.z < (borderPos.z + dim.z / 2);
	}
}
