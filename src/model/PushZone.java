/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

package model;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

/**
 * 
 * @author Nottingham, Swartzendruber
 * @version June 16th, 2013
 * 
 * A field on the stage that is represented by a box that pushes the marble in the specified direction.
 *
 */
public class PushZone extends SetEnabledOnIntersectionTrigger {
    private static final Appearance ACTIVE_APPEARANCE = getActiveAppearance();
    private static final Appearance INACTIVE_APPEARANCE = getInactiveAppearance();
    
    private Box _box;
    private Push _push;
    
    public PushZone(Marble m, Push p, OrientedBoundingBox zone) {
        super(m, p, true, zone);
        _box = new Box(zone, INACTIVE_APPEARANCE);
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
    
    @Override
    public void executeOnTrue() {
        if (!_toggleable.isEnabled()) {
            _toggleable.setEnabled(_setOnTrue);
            _box.setAppearance(ACTIVE_APPEARANCE);
        }
    }

    @Override
    public void executeOnFalse() {
        if (_toggleable.isEnabled()) {
            _toggleable.setEnabled(!_setOnTrue);
            _box.setAppearance(INACTIVE_APPEARANCE);
        }
    } 
    
    private static Appearance getInactiveAppearance() {
        final Appearance a = new Appearance();
        final TransparencyAttributes t = new TransparencyAttributes(TransparencyAttributes.BLENDED, .5f);
        final Material m = new Material(new Color3f(0, 0, 0), new Color3f(0, 0, 0), new Color3f(1, 0, 0), new Color3f(1, 1, 1), 100);
        a.setTransparencyAttributes(t);
        a.setMaterial(m);
        return a;
    }
    
    private static Appearance getActiveAppearance() {
        final Appearance a = new Appearance();
        final TransparencyAttributes t = new TransparencyAttributes(TransparencyAttributes.BLENDED, .5f);
        final Material m = new Material(new Color3f(0, 0, 0), new Color3f(0, 0, 0), new Color3f(0, 1, 0), new Color3f(1, 1, 1), 100);
        a.setTransparencyAttributes(t);
        a.setMaterial(m);
        return a;
    }
}
