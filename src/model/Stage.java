/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

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
 * @author Nottingham, Swartzendruber
 * @version June 16th, 2013
 * 
 * Class that contains all of the blocks and forces that makes a world for the marble to traverse.
 * Contains 
 */
public class Stage extends BranchGroup
{
	/**
	 * The height (y) of the platforms.
	 */
	public static final float HEIGHT = 2.0f;
	
	/**
	 * The speed of the marble.
	 */
	public static final float SPEED = 100f;
	
	/**
	 * The initial starting score of the stage. This value decrements with each tick.
	 */
	public int _startingScore;
	
	/**
	 * List of all of the rotatable objects in the stage.
	 */
	private List<Transformable> _transformables;
	
	/**
	 * List of all the platforms in the stage.
	 */
    private List<Box> _platforms;
    
    /**
     * List of all the walls in the stage.
     */
    private List<Box> _walls;
    
    /**
     * List of all the forces in the stage.
     */
    private List<Force> _forces;
    
    /**
     * List of all the triggers in the stage.
     */
    private List<IntersectionTrigger> _intersectionTriggers;
    
    /**
     * List of all the push zones in the stage.
     */
    private List<PushZone> _pushZones;
    private final TransformGroup _TG;
    
    /**
     * The marble that the player must get to reach the end.
     */
    private final Marble _player;
    
    /**
     * The number of times the player has won the level.
     */
    private int _wins;
    
    /**
     * The current running score for the player.
     */
    private int _score;
    
    /**
     * Creates a Stage without any platforms, walls, etc.
     */
    public Stage()
    {
    	_player = new Marble(1.0f, new Vector3f(0, 5.0f, 0), 1.0f, new Color3f(1, 1, 1));
        _platforms = new ArrayList<Box>();
        _walls = new ArrayList<Box>();
        _forces = new ArrayList<Force>();
        _intersectionTriggers = new ArrayList<IntersectionTrigger>();
        _pushZones = new ArrayList<PushZone>();
        _TG = new TransformGroup();
        _wins = 0;
        setStartingScore(500);
     }

    /**
     * Compiles all of the transformables into a single collection.
     */
    private void addTransformables() {
        _transformables = new ArrayList<Transformable>();
        _transformables.addAll(_platforms);
        _transformables.addAll(_walls);
        _transformables.addAll(_intersectionTriggers);
        _transformables.add(_player);
        // Do not add PushZones. They are already added in _triggers.
    }

    /**
     * Adds all of the branch groups to the stage.
     */
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
    
    /**
     * Updates all of the working parts in the stage.
     */
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
        if (_score > 0)
        	_score--;
    }
    
    /**
     * Handles the collisions between the players and the static objects.
     */
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
    
    /**
     * @return The platforms that make up the stage.
     */
    public List<Box> getBoundaries()
    {
    	return _platforms;
    }
    
    /**
     * @return The walls that make up the stage.
     */
    public List<Box> getWalls()
    {
    	return _walls;
    }
    
    /**
     * @return The forces that make up the stage.
     */
    public List<Force> getForces()
    {
        return _forces;
    }
    
    /**
     * @return The triggers that are contained within the stage.
     */
    public List<IntersectionTrigger> getTriggers()
    {
        return _intersectionTriggers;
    }
    
    /**
     * @return The push zones that are contained within the stage.
     */
    public List<PushZone> getPushZones()
    {
        return _pushZones;
    }
    
    /**
     * @return The transformables that are contained within the stage.
     */
    public List<Transformable> getTransformables()
    {
        return _transformables;
    }
    
    /**
     * @return The marble representing the player.
     */
    public Marble getPlayer()
    {
    	return _player;
    }
    
    /** 
     * @param thePlatforms The platforms that are to be set in the stage.
     */
    public void setPlatforms(final List<Box> thePlatforms)
    {
    	_platforms = thePlatforms;
    }
    
    /**
     * @param theWalls The walls that are to be set in the stage.
     */
    public void setWalls(final List<Box> theWalls)
    {
    	_walls = theWalls;
    }
    
    /**
     * @param theForces The forces that are to be set in the stage.
     */
    public void setForces(final List<Force> theForces)
    {
    	_forces = theForces;
    }
    
    /**
     * @param theTriggers The triggers that are to be set in the stage.
     */
    public void setIntersectionTriggers(final List<IntersectionTrigger> theTriggers)
    {
        _intersectionTriggers = theTriggers;
    }
    
    /**
     * @param thePushZones The push zones that are to be set in the stage.
     */
    public void setPushZones(final List<PushZone> thePushZones)
    {
        _pushZones = thePushZones;
    }
    
    /**
     * @param thePZ The push zone to be added to the existing push zones.
     */
    public void addZones(final PushZone thePZ)
    {
		 _intersectionTriggers.add(thePZ);
		 _pushZones.add(thePZ);
    }
    
    /**
     * Increments the amount of wins.
     */
    public void playerWin()
    {
    	_wins++;
    }
    
    /**
     * @return The number of wins the player has.
     */
    public int getWins()
    {
    	return _wins;
    }
    
    /**
     * Resets the player back to the starting position and
     * sets the running score back to the starting score.
     */
    public void reset()
    {
    	_player.reset();
    	_score = _startingScore;
    }
    
    /**
     * @return The current score for the running score.
     */
    public int getScore()
    {
    	return _score;
    }
    
    /**
     * @param theStart The new starting score.
     */
    public void setStartingScore(final int theStart)
    {
        _startingScore = theStart;
        _score = _startingScore;
    }
}
