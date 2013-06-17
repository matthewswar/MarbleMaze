/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

package model;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * 
 * @author Nottingham, Swartzendruber
 * @version June 16th, 2013
 * 
 * A sphere that represents the player. The game is won when this sphere reaches the end.
 *
 */
public class Marble implements Transformable
{
	/**
	 * Ambient lighting color.
	 */
    private static final Color3f AMBIENT = new Color3f(.02f, .02f, .02f);
    
    /**
     * Emissive lighting color.
     */
    private static final Color3f EMISSIVE = new Color3f(0, 0, 0);
    
    /**
     * Specular lighting color.
     */
    private static final Color3f SPECULAR = new Color3f(1, 1, 1);
    
    /**
     * The shininess of the sphere.
     */
    private static final float SHININESS = 100;
    
    /**
     * The mass of the sphere.
     */
	public float mass;
	
	/**
	 * The radius of the sphere.
	 */
	public float radius;
	
	/**
	 * The position of the sphere in the stage.
	 */
	public Vector3f position;
	
	/**
	 * The speed and direction the sphere is moving.
	 */
	public Vector3f velocity;
	
	/**
	 * The amount of force the sphere has obtained.
	 */
	public Vector3f forceAccumulator;
	public BranchGroup BG;
	private TransformGroup TG;
	private Transform3D T3D;
	
	/**
	 * The initial starting position of the sphere.
	 */
	private Vector3f _startingPosition;
	
	/**
	 * Whether or not the sphere has beaten the stage.
	 */
	private boolean _isWinner;

	/**
	 * Creates a marble at the given location.
	 * 
	 * @param mass The mass of the sphere.
	 * @param positionX The starting x position.
	 * @param positionY The starting y position.
	 * @param positionZ The starting z position.
	 * @param radius The radius of the sphere.
	 * @param color The color of the sphere.
	 */
	public Marble(float mass, float positionX, float positionY,
	        float positionZ, float radius, Color3f color) {
		if (mass <= 0)
			throw new IllegalArgumentException();
		
		this.mass = mass;
		this.radius = radius;
		position = new Vector3f(positionX, positionY, positionZ);
		velocity = new Vector3f(0, 0, 0);
		forceAccumulator = new Vector3f();
		BG = new BranchGroup();
		BG.setCapability(BranchGroup.ALLOW_DETACH);
		TG = new TransformGroup();
		TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TG.addChild(createShape(radius, color));
		BG.addChild(TG);
		T3D = new Transform3D();
		updateTransformGroup();
		_startingPosition = (Vector3f)position.clone();
		_isWinner = false;
	}
	
	public Marble(float mass, Tuple3f position, float radius, Color3f color) {
		this(mass, position.x, position.y, position.z, radius, color);
	}
	
	/**
	 * Calculates the position and the velocity based on the amount of time passed.
	 * 
	 * @param duration The time that has passed since the last update.
	 */
	public void updateState(float duration) {
	    Vector3f acceleration = new Vector3f(forceAccumulator);
	    acceleration.scale(1 / mass);
		position.scaleAdd(duration, velocity, position);
		position.scaleAdd(duration * duration / 2, acceleration, position);
		velocity.scaleAdd(duration, acceleration, velocity);
	}

	/**
	 * Commit and update the changes made by the position.
	 */
	public void updateTransformGroup() {
		T3D.setTranslation(new Vector3f(position.x, position.y, position.z));
		TG.setTransform(T3D);
	}
	
	/**
	 * Return the marble to the starting position.
	 */
	public void reset()
	{
		position = (Vector3f)_startingPosition.clone();
		velocity = new Vector3f(0, 0, 0);
		_isWinner = false;
	}
	
	/**
	 * Change the starting position of the marble.
	 * 
	 * @param theStartingPos The location of the new starting position.
	 */
	public void setStartingPos(final Vector3f theStartingPos)
	{
		_startingPosition = (Vector3f)theStartingPos.clone();
	}
	
	/**
	 * 
	 * @param radius The radius of the sphere.
	 * @param color The color of the sphere.
	 * @return A sphere with the given radius and color.
	 */
	private Node createShape(float radius, Color3f color) {
		if (color == null)
			color = new Color3f(Color.getHSBColor((float)Math.random(), 1, 1));
		Appearance appearance = new Appearance();
		appearance.setMaterial(new Material(AMBIENT, EMISSIVE, color,
		        SPECULAR, SHININESS));
		return new Sphere(radius, Sphere.GENERATE_NORMALS, 16, appearance);
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void transform(Transform3D transform) {
        transform.transform(position);
        transform.transform(_startingPosition);
        updateTransformGroup();
    }
    
    /**
     * Sets the win state to true, indicating that the marble has completed the level.
     */
    public void triggerWin()
    {
    	_isWinner = true;
    }
    
    /**
     * @return Whether or not the marble knows it has completed the level.
     */
    public boolean isWinner()
    {
    	return _isWinner;
    }
}
