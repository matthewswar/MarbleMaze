package model;

public abstract class Force {
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
