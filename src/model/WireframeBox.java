package model;
/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 5/5/2013
 * Homework 2: Particle System
 */

import javax.media.j3d.BranchGroup;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;


public class WireframeBox extends BranchGroup {
    private Point3d[] VERTICES = {
        new Point3d(-1, -1, 1), new Point3d(-1, 1, 1),
        new Point3d(1, 1, 1), new Point3d(1, -1, 1),
        new Point3d(-1, -1, -1), new Point3d(-1, 1, -1),
        new Point3d(1, 1, -1), new Point3d(1, -1, -1)
    };
    
    public WireframeBox(float xDim, float yDim, float zDim) {
        Transform3D scale = new Transform3D();
        scale.setScale(new Vector3d(xDim / 2, yDim / 2, zDim / 2));
        TransformGroup group = new TransformGroup(scale);
        group.addChild(createBoxShape());
        addChild(group);
    }
    
    public Shape3D createBoxShape() {
        LineArray lines = new LineArray(24,
                LineArray.COORDINATES | LineArray.COLOR_3);
        
        Point3f[] coordinates = new Point3f[24];
        Color3f[] colors = new Color3f[24];
        for (int i = 0, j = 0; j < coordinates.length; i++) {
            coordinates[j++] = new Point3f(VERTICES[i]);
            coordinates[j++] = new Point3f(VERTICES[(i + 1) % 4]);
            
            coordinates[j++] = new Point3f(VERTICES[i + 4]);
            coordinates[j++] = new Point3f(VERTICES[(i + 1) % 4 + 4]);
            
            coordinates[j++] = new Point3f(VERTICES[i]);
            coordinates[j++] = new Point3f(VERTICES[i + 4]);
        }
        
        for (int i = 0; i < colors.length; i++) {
            //colors[i] = new Color3f((coordinates[i].x + 1) / 2 + .1f,
            //        (coordinates[i].y + 1) / 2 + .1f,
            //        (coordinates[i].z + 1) / 2 + .1f);
        	colors[i] = new Color3f(255, 255, 255);
        }
        
        lines.setCoordinates(0, coordinates);
        lines.setColors(0, colors);
        
        return new Shape3D(lines); 
    }
}
