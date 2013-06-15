package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

public class LevelScanner 
{
	public static final int ALPHA_UNMASK = 0x00FFFFFF;
	public static final int RED_MASK = 0x00FF0000;
	public static final int GREEN_MASK = 0x0000FF00;
	public static final int BLUE_MASK = 0x000000FF;
	public static final int PUSH_IDENTIFIER = 42;
	public static final int MAX_DEGREE = 360;
	public static final int MAX_COLOR_VAL = 255;
	public static final int MAX_MAGNITUDE = 250;
	public static final Color PLATFORM = new Color(MAX_COLOR_VAL, MAX_COLOR_VAL, MAX_COLOR_VAL);
	public static final Color STARTING_POINT = new Color(0, 0, MAX_COLOR_VAL);
	public static final Color ENDING_POINT = new Color(MAX_COLOR_VAL, 0, 0);
	public static final Color WALL = new Color(0, 0, 0);
	
	public static Stage loadLevel(final String theFileLocation) throws IOException
	{
		final BufferedImage img = ImageIO.read(new File(theFileLocation));
		final Stage result = new Stage(img.getWidth(), img.getHeight());
		final List<Box> platforms = new ArrayList<Box>();
		final List<Box> walls = new ArrayList<Box>();
		final List<Force> forces = new ArrayList<Force>(); 
		
		forces.add(new Push(new Vector3f(0, -1, 0), Stage.SPEED));
		
		for (int i = 0; i < img.getWidth(); i++)
			for (int j = 0; j < img.getHeight(); j++)
			{
				final Color code = getPixelColor(img, i, j);
				if (code.equals(PLATFORM))
				{
				    final OrientedBoundingBox obb = 
				    new OrientedBoundingBox(new Vector3f(i * Box.DIMENSION, -Stage.HEIGHT, j * Box.DIMENSION), 
											new Vector3f(Box.DIMENSION, Stage.HEIGHT, Box.DIMENSION), new Vector3f());
					platforms.add(new Box(obb, new Color3f(0.5f, 0.5f, 0.5f)));
				}
				else if (code.equals(WALL))
				{
				    final OrientedBoundingBox obb = new OrientedBoundingBox(new Vector3f(i * Box.DIMENSION, 0, j * Box.DIMENSION),
										new Vector3f(Box.DIMENSION, Box.DIMENSION, Box.DIMENSION), new Vector3f());
					walls.add(new Box(obb, new Color3f(0, 0, 1)));
				}
				else if (code.equals(STARTING_POINT))
				{
					final Vector3f loc = new Vector3f(i * Box.DIMENSION, -Stage.HEIGHT, j * Box.DIMENSION);
					final OrientedBoundingBox obb = 
							new OrientedBoundingBox((Vector3f)loc.clone(), new Vector3f(Box.DIMENSION, Stage.HEIGHT, Box.DIMENSION));
					platforms.add(new Box(obb, new Color3f(1, 1, 1)));
					loc.y += 5.0f;
					result.getPlayer().position = loc;
					result.getPlayer().setStartingPos(loc);
				}
				else if (code.equals(ENDING_POINT))
				{
				    final OrientedBoundingBox obb = 
				    		new OrientedBoundingBox(new Vector3f(i * Box.DIMENSION, -Stage.HEIGHT, j * Box.DIMENSION), 
							new Vector3f(Box.DIMENSION, Stage.HEIGHT, Box.DIMENSION));
				    final Box endPlatform = new Box(obb, new Color3f(1, 0, 0));
				    endPlatform.setIsGoal(true);
					platforms.add(endPlatform);
				}
				else if (code.getRed() == PUSH_IDENTIFIER)
				{
				    final OrientedBoundingBox obb = 
				    new OrientedBoundingBox(new Vector3f(i * Box.DIMENSION, -Stage.HEIGHT, j * Box.DIMENSION), 
											new Vector3f(Box.DIMENSION, Stage.HEIGHT, Box.DIMENSION), new Vector3f());
					platforms.add(new Box(obb, new Color3f(0.5f, 0.5f, 0.5f)));
					final double angle = (code.getGreen() * MAX_DEGREE) / MAX_COLOR_VAL;
					final int mag = (code.getBlue() * MAX_MAGNITUDE) / MAX_COLOR_VAL;
					addPush(j, i, new Vector3f((float)Math.cos(angle), 0, 
										(float)Math.sin(angle)), mag, forces, result);
					
				}
			}
		
		result.setPlatforms(platforms);
		result.setWalls(walls);
		result.setForces(forces);
		result.compile();
		
		return result;
	}
	
	private static Color getPixelColor(final BufferedImage theImg, final int theWidth, 
										final int theHeight)
	{
		if (theWidth > theImg.getWidth() || theHeight > theImg.getHeight())
			return null;
		
		final int colors = theImg.getRGB(theWidth, theHeight) & ALPHA_UNMASK;
		final int r = (colors & RED_MASK) >> 16;
		final int g = (colors & GREEN_MASK) >> 8;
		final int b = colors & BLUE_MASK;
		return new Color(r, g, b);
	}
	
    private static void addPush(int row, int col, Vector3f direction, float magnitude, 
    								final List<Force> forces, final Stage result)
    {
        direction.normalize();
        Push push = new Push(direction, magnitude);
        push.setEnabled(false);
        forces.add(push);
        
        OrientedBoundingBox obb =
                new OrientedBoundingBox(new Vector3f(col * Box.DIMENSION, 0, row * Box.DIMENSION),
                new Vector3f(Box.DIMENSION, Box.DIMENSION, Box.DIMENSION));
        
        PushZone pz = new PushZone(result.getPlayer(), push, obb);
        
        result.addZones(pz);
    }
}
