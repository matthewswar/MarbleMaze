/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

package model;

import javax.vecmath.Vector3f;

/**
 * 
 * @author Nottingham, Swartzendruber
 * @version June 16th, 2013
 * 
 * A small class that remembers the depth and normal of each collision.
 */
public class CollisionInfo {
	public Vector3f normal;
	// Depth of overlap (negative).
	public float depth;
}