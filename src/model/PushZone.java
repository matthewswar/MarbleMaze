package model;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;

public class PushZone extends SetEnabledOnIntersectionTrigger {
    private Box _box;
    private Push _push;
    
    public PushZone(Marble m, Push p, OrientedBoundingBox zone) {
        super(m, p, true, zone);
        _box = new Box(zone);
        _push = p;
    }
    
    @Override
    public void transform(final Transform3D transform) {
        _box.transform(transform);
        _push.transform(transform);
    }
    
    public BranchGroup getShape() {
        return _box;
    }
}
