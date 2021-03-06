/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

package model;

/**
 * 
 * @author Nottingham, Swartzendruber
 * @version June 16th, 2013
 *
 */
public class SetEnabledOnIntersectionTrigger extends IntersectionTrigger {
    protected Toggleable _toggleable;
    protected boolean _setOnTrue;
    
    public SetEnabledOnIntersectionTrigger(Marble m, Toggleable t,
            boolean setOnTrue, OrientedBoundingBox obb) {
        super(m, obb);
        _toggleable = t;
        _setOnTrue = setOnTrue;
    }
    
    @Override
    public void executeOnTrue() {
        _toggleable.setEnabled(_setOnTrue);
    }

    @Override
    public void executeOnFalse() {
        _toggleable.setEnabled(!_setOnTrue);
    }
}
