package model;

import java.util.ArrayList;
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
	private List<Transformable> _transformables;
    private List<Box> _platforms;
    private List<Box> _walls;
    private List<Force> _forces;
    private List<IntersectionTrigger> _intersectionTriggers;
    private List<PushZone> _pushZones;
    private final TransformGroup _TG;
    private final Marble _player;
    
    public Stage(float xDim, float zDim)
    {
    	_player = new Marble(1.0f, new Vector3f(0, 5.0f, 0), 1.0f, new Color3f(1, 1, 1));
        _platforms = new ArrayList<Box>();
        _walls = new ArrayList<Box>();
        _forces = new ArrayList<Force>();
        _intersectionTriggers = new ArrayList<IntersectionTrigger>();
        _pushZones = new ArrayList<PushZone>();
        _TG = new TransformGroup();
        
        // Every level has gravity for now.
        //_forces.add(new Push(new Vector3f(0, -1, 0), SPEED)); //This line has been added to LevelScanner
        
        // Experimentation...
        /*
        addTestPush(3, 3, new Vector3f(1, 0, 0), 250);
        addTestPush(1, 8, new Vector3f(0, 0, 1), 250);
        addTestPush(2, 8, new Vector3f(0, 0, 1), 250);
        addTestPush(3, 8, new Vector3f(0, 0, 1), 250);
        addTestPush(6, 8, new Vector3f(0, 0, -1), 250);
        addTestPush(1, 16, new Vector3f(1, 0, 0), 250);
        */
     }

    private void addTransformables() {
        _transformables = new ArrayList<Transformable>();
        _transformables.addAll(_platforms);
        _transformables.addAll(_walls);
        _transformables.addAll(_intersectionTriggers);
        _transformables.add(_player);
        // Do not add PushZones. They are already added in _triggers.
    }

    public void compile()
    {
        addTransformables();
        
        _TG.addChild(_player.BG);
        
        for (final Box b : _platforms)
        {
        	_TG.addChild(b);
        }
        
        for (final Box b : _walls)
        {
        	_TG.addChild(b);
        }
        
        for (final PushZone pz : _pushZones) {
            _TG.addChild(pz.getShape());
        }
        
        addChild(_TG);
    }
    
    public void update()
    {
        for (final IntersectionTrigger t : _intersectionTriggers)
        {
            t.check();
        }
        
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
        	CollisionHandler.checkMarbleCollision(_player, b.getOBB());
        }
        
        for (final Box b : _walls)
        {
            CollisionHandler.checkMarbleCollision(_player, b.getOBB());
            
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
    
    public List<Force> getForces()
    {
        return _forces;
    }
    
    public List<IntersectionTrigger> getTriggers()
    {
        return _intersectionTriggers;
    }
    
    public List<PushZone> getPushZones()
    {
        return _pushZones;
    }
    
    public List<Transformable> getTransformables()
    {
        return _transformables;
    }
    
    public Marble getPlayer()
    {
    	return _player;
    }
    
    public void setPlatforms(final List<Box> thePlatforms)
    {
    	_platforms = thePlatforms;
    }
    
    public void setWalls(final List<Box> theWalls)
    {
    	_walls = theWalls;
    }
    
    public void setForces(final List<Force> theForces)
    {
    	_forces = theForces;
    }
    
    public void setIntersectionTriggers(final List<IntersectionTrigger> theTriggers)
    {
        _intersectionTriggers = theTriggers;
    }
    
    public void setPushZones(final List<PushZone> thePushZones)
    {
        _pushZones = thePushZones;
    }
    
    public void addZones(final PushZone thePZ)
    {
		 _intersectionTriggers.add(thePZ);
		 _pushZones.add(thePZ);
    }
}
