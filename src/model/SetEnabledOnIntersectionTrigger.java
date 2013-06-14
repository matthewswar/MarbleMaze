package model;


public class SetEnabledOnIntersectionTrigger extends IntersectionTrigger {
    private Toggleable _toggleable;
    private boolean _setOnTrue;
    
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
