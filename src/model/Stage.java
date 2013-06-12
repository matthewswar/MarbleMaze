package model;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import view.WorldDirector;

/**
 * 
 * @author Matthew Swartzendruber
 * @author Tyson Nottingham
 * 
 * Create a class for the sphere to traverse around containing collidable platforms/walls and other forces
 */
public class Stage extends BranchGroup
{
	public static final float HEIGHT = 1.0f;
	public static final float SPEED = 100f;
    private final Box[] _platforms;
    private final Box[] _walls;
    private final TransformGroup _TG;
    private final Marble _player;
    
    public Stage(float xDim, float zDim) {
    	_player = new Marble(1.0f, new Vector3f(0, 1.0f, 0), 1.0f, new Color3f(1, 1, 1));
 
        _platforms = new Box[] {
            new Box(new Vector3f(), new Vector3f(xDim, HEIGHT, zDim))
        };        
        
        _walls = new Box[(int)(zDim / Box.DIMENSION) * 2 + 1];
        
        for (int i = 0; i < _walls.length / 2; i++)
        {
        	_walls[i] = new Box(new Vector3f(-xDim / 2 + Box.DIMENSION / 2, 2.0f, 
        								(float)(zDim / 2 - Box.DIMENSION / 2) - (Box.DIMENSION * i)));
        }
        
        for (int i = _walls.length / 2; i < _walls.length - 1; i++)
        {
        	_walls[i] = new Box(new Vector3f(xDim / 2 - Box.DIMENSION / 2, 2.0f, 
        								(float)(zDim / 2 - Box.DIMENSION / 2) - (Box.DIMENSION * (i - _walls.length / 2))));
        }
        
        _walls[_walls.length - 1] = new Box(new Vector3f(-10.0f, 2.0f, 5.0f));
        
        _TG = new TransformGroup();
        _TG.addChild(_player.BG);
        
        for (final Box b : _platforms)
        {
        	_TG.addChild(b);
        }
        
        for (final Box b : _walls)
        {
        	_TG.addChild(b);
        }
        
        addChild(_TG);
    }
    
    public void update()
    {
		_player.forceAccumulator.y += -SPEED * _player.mass;
        _player.updateState(1f / WorldDirector.UPDATE_RATE);
        checkCollisions();
    }
    
    public void checkCollisions() {
        for (final Box b : _platforms)
        {
        	CollisionHandler.checkMarbleCollision(_player, b.OBB);
        }
        
        for (final Box b : _walls)
        {
            CollisionHandler.checkMarbleCollision(_player, b.OBB);
            
        }
        
        _player.updateTransformGroup();
        _player.forceAccumulator.set(0, 0, 0);
    }
    
    public Box[] getBoundaries()
    {
    	return _platforms;
    }
    
    public Box[] getWalls()
    {
    	return _walls;
    }
    
    public Marble getPlayer()
    {
    	return _player;
    }
}
