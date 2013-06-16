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
 * Provides the framework for togglable force within a level.
 *
 */
public abstract class Force implements Toggleable {
    private boolean _enabled = true;
    
    public void apply(Marble m) {
        if (_enabled) {
            execute(m);
        }
    }
    
    public void setEnabled(boolean enabled) {
        _enabled = enabled;
    }
    
    public boolean isEnabled() {
        return _enabled;
    }
    
    protected abstract void execute(Marble m);
}
