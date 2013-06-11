package model;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

public class HalfSquare extends BranchGroup
{
	public static final float DIMENSION = 5.0f;
	private final HalfSpace[] _boundaries;
	private final Vector3f _position;
	private final TransformGroup _TG;
	
	public HalfSquare(final float theX, final float theY, final float theZ)
	{
		super();
		_TG = new TransformGroup();
		_position = new Vector3f(theX, theY, theZ);
		_boundaries = new HalfSpace[]
		{ // Left, right, front, back, top, bottom	
				new HalfSpace(theX - DIMENSION / 2, theY, theZ, -1, 0, 0, DIMENSION, DIMENSION, DIMENSION),
				new HalfSpace(theX + DIMENSION / 2, theY, theZ, 1, 0, 0, DIMENSION, DIMENSION, DIMENSION),
				new HalfSpace(theX, theY, theZ + DIMENSION / 2, 0, 0, 1, DIMENSION, DIMENSION, DIMENSION),
				new HalfSpace(theX, theY, theZ - DIMENSION / 2, 0, 0, -1, DIMENSION, DIMENSION, DIMENSION),
				new HalfSpace(theX, theY + DIMENSION / 2, theZ, 0, 1, 0, DIMENSION, DIMENSION, DIMENSION),
				new HalfSpace(theX, theY - DIMENSION / 2, theZ, 0, -1, 0, DIMENSION, DIMENSION, DIMENSION)
		};
		_TG.addChild(new WireframeBox(DIMENSION, DIMENSION, DIMENSION));
		final Transform3D t3d = new Transform3D();
		_TG.getTransform(t3d);
		t3d.setTranslation(_position);
		_TG.setTransform(t3d);
		addChild(_TG);
	}
	
	public HalfSquare(final Vector3f thePos)
	{
		this(thePos.x, thePos.y, thePos.z);
	}
	
	public TransformGroup getTG()
	{
		return _TG;
	}
	
	public Vector3f getPos()
	{
		return _position;
	}
	
	public HalfSpace[] getBoundaries()
	{
		return _boundaries;
	}
}
