/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

package model;

public abstract class Trigger implements Toggleable {
    protected boolean _enabled = true;
    
    public void setEnabled(boolean enabled) {
        _enabled = enabled;
    }
    
    public boolean isEnabled() {
        return _enabled;
    }
    
    public void check() {
        if (_enabled) {
            if (condition()) {
                executeOnTrue();
            } else {
                executeOnFalse();
            }
        }
    }
    
    public abstract boolean condition();
    public abstract void executeOnTrue();
    public abstract void executeOnFalse();
}
