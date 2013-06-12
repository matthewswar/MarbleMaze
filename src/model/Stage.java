package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public static final float HEIGHT = 2.0f;
	public static final float SPEED = 100f;
    private List<Box> _platforms;
    private List<Box> _walls;
    private List<Force> _forces;
    private final TransformGroup _TG;
    private final Marble _player;
    
    public Stage(float xDim, float zDim) {
    	_player = new Marble(1.0f, new Vector3f(0, 5.0f, 0), 1.0f, new Color3f(1, 1, 1));
 
        _platforms = new ArrayList<Box>();
        _walls = new ArrayList<Box>();
        /*
        _platforms.add(new Box(new Vector3f(), new Vector3f(xDim, HEIGHT, zDim)));
        
        final int testBoxes = (int)(zDim / Box.DIMENSION) * 2 + 1;
        
        for (int i = 0; i < testBoxes / 2; i++)
        {
        	_walls.add(new Box(new Vector3f(-xDim / 2 + Box.DIMENSION / 2, 2.0f, 
        								(float)(zDim / 2 - Box.DIMENSION / 2) - (Box.DIMENSION * i))));
        }
        
        for (int i = testBoxes / 2; i < testBoxes - 1; i++)
        {
        	_walls.add(new Box(new Vector3f(xDim / 2 - Box.DIMENSION / 2, 2.0f, 
        								(float)(zDim / 2 - Box.DIMENSION / 2) - (Box.DIMENSION * (i - testBoxes / 2)))));
        }
        
        _walls.add(new Box(new Vector3f(-10.0f, 2.0f, 5.0f)));
        */
        
        _forces = new ArrayList<Force>();
        _forces.add(new Gravity(new Vector3f(0, -1, 0), SPEED));
        _TG = new TransformGroup();
    }
    
    public void compile()
    {
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
        for (final Force f : _forces)
        {
            f.apply(_player);
        }
            
        _player.updateState(1f / WorldDirector.UPDATE_RATE);
        checkCollisions();
        _player.forceAccumulator.set(0, 0, 0);
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
    }
    
    public List<Box> getBoundaries()
    {
    	return _platforms;
    }
    
    public List<Box> getWalls()
    {
    	return _walls;
    }
    
    public Marble getPlayer()
    {
    	return _player;
    }
    
    public void setPlatforms(final List<Box> thePlatforms)
    {
    	_platforms = thePlatforms;
    }
    
    public void setPlatforms(final Box[] thePlatforms)
    {
    	setPlatforms(Arrays.asList(thePlatforms));
    }
    
    public void setWalls(final List<Box> theWalls)
    {
    	_walls = theWalls;
    }
    
    public void setWalls(final Box[] theWalls)
    {
    	setWalls(Arrays.asList(theWalls));
    }
    
    public void setForces(final List<Force> theForces)
    {
    	_forces = theForces;
    }
    
    public void setForces(final Force[] theForces)
    {
    	setForces(Arrays.asList(theForces));
    }
}
