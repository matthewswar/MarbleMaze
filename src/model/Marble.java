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

public class Marble 
{
    private static final Color3f AMBIENT = new Color3f(.02f, .02f, .02f);
    private static final Color3f EMISSIVE = new Color3f(0, 0, 0);
    private static final Color3f SPECULAR = new Color3f(1, 1, 1);
    private static final float SHININESS = 100;
    
	public float mass;
	public float radius;
	public Vector3f position;
	public Vector3f velocity;
	public Vector3f forceAccumulator;
	public BranchGroup BG;
	private TransformGroup TG;
	private Transform3D T3D;
	private final Vector3f _startingPosition;

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
	}
	
	public Marble(float mass, Tuple3f position, float radius, Color3f color) {
		this(mass, position.x, position.y, position.z, radius, color);
	}
	
	public void updateState(float duration) {
	    Vector3f acceleration = new Vector3f(forceAccumulator);
	    acceleration.scale(1 / mass);
		position.scaleAdd(duration, velocity, position);
		position.scaleAdd(duration * duration / 2, acceleration, position);
		velocity.scaleAdd(duration, acceleration, velocity);
	}

	public void updateTransformGroup() {
		T3D.setTranslation(new Vector3f(position.x, position.y, position.z));
		TG.setTransform(T3D);
	}
	
	public void reset()
	{
		position = (Vector3f)_startingPosition.clone();
		velocity = new Vector3f(0, 0, 0);
	}
	
	private Node createShape(float radius, Color3f color) {
		if (color == null)
			color = new Color3f(Color.getHSBColor((float)Math.random(), 1, 1));
		Appearance appearance = new Appearance();
		appearance.setMaterial(new Material(AMBIENT, EMISSIVE, color,
		        SPECULAR, SHININESS));
		return new Sphere(radius, Sphere.GENERATE_NORMALS, 16, appearance);
	}
	
}
