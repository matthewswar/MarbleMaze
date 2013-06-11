package model;
/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/8/2013
 * Homework 4: Marble Maze
 */

import javax.vecmath.*;

public class HalfSpace implements Cloneable {
	public Vector3f position;
	public Vector3f normal;
	public Vector3f dim;
	// Right-hand side of the plane equation A * x + B * y = C
	// Equals distance from origin to closest point on plane
	public float intercept;
	
	public HalfSpace(float positionX, float positionY, float positionZ,
	        float normalX, float normalY, float normalZ,
	        float xDim, float yDim, float zDim) {
		position = new Vector3f(positionX, positionY, positionZ);
		normal = new Vector3f(normalX, normalY, normalZ);
		normal.normalize();
		intercept = normal.dot(position);
		dim = new Vector3f(xDim, yDim, zDim);
	}

	public HalfSpace(Tuple3f position, Tuple3f normal, final Tuple3f dim) {
		this(position.x, position.y, position.z, normal.x, normal.y, normal.z, dim.x, dim.y, dim.z);
	}
}
