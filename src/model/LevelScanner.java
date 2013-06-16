package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

public class LevelScanner 
{
	public static final int SCORE_MULTIPLIER = 5;
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
	
	private static int _totalScore;

    private static final FilenameFilter PNG_FILE_FILTER = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".png");
        }
    };
    
    private static final float MAX_RADIANS = (float) (2 * Math.PI);
    private static final int BLOCK = 255;
    private static final int RAMP_NORTH = 254;
    private static final int RAMP_SOUTH = 253;
    private static final int RAMP_WEST = 252;
    private static final int RAMP_EAST = 251;
    private static final int PUSH = 250;
    private static final int START = 249;
    private static final int END = 248;
    
	public static Stage loadLevel(final String theFileLocation) throws IOException
	{
		_totalScore = 0;
        final File fileOrDirectory = new File(theFileLocation);
        final ArrayList<File> files = new ArrayList<File>();
        if (fileOrDirectory.isDirectory()) {
            for (final File f : fileOrDirectory.listFiles(PNG_FILE_FILTER)) {
                files.add(f);
            } 
            
            Collections.sort(files);
            
            final BufferedImage img = ImageIO.read(files.get(0));
            final Stage stage = new Stage(img.getWidth(), img.getHeight());
            stage.getForces().add(new Push(new Vector3f(0, -1, 0), Stage.SPEED));
            
            for (int i = 0; i < files.size(); i++) {
                loadLayer(stage, files.get(i), i);
            }
            
            stage.setStartingScore(_totalScore);
            stage.compile();
            
            return stage;
        } else {
            final BufferedImage img = ImageIO.read(new File(theFileLocation));
            final Stage result = new Stage(img.getWidth(), img.getHeight());
            final List<Box> platforms = new ArrayList<Box>();
            final List<Box> walls = new ArrayList<Box>();
            final List<Force> forces = new ArrayList<Force>(); 
            _totalScore = img.getHeight() * img.getWidth() * SCORE_MULTIPLIER;
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
                        final double angle = (code.getGreen() * MAX_RADIANS) / MAX_COLOR_VAL;
                        final int mag = (code.getBlue() * MAX_MAGNITUDE) / MAX_COLOR_VAL;
                        addPush(j, i, new Vector3f((float)Math.cos(angle), 0, 
                                            (float)Math.sin(angle)), mag, forces, result);
                        
                    }
                }
            
            result.setStartingScore(_totalScore);
            result.setPlatforms(platforms);
            result.setWalls(walls);
            result.setForces(forces);
            result.compile();
            
            return result;
        }
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
    								final List<Force> forces, final Stage result) {
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
    
    private static void loadLayer(final Stage stage, final File file,
            final int layer) throws IOException {
        final BufferedImage img = ImageIO.read(file);
        final List<Box> platforms = stage.getBoundaries();
        final List<Box> walls = stage.getWalls();
        final List<Force> forces = stage.getForces(); 
        _totalScore += img.getHeight() * img.getWidth() * SCORE_MULTIPLIER;
        
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                final Color cellInfo = new Color(img.getRGB(i, j), true);
                final Color3f cellColor = new Color3f(cellInfo);
                final int cellType = cellInfo.getAlpha(); 
                
                switch (cellType) {
                case START: {
                    final Vector3f loc = new Vector3f(i * Box.DIMENSION, layer * Box.DIMENSION, j * Box.DIMENSION);
                    final OrientedBoundingBox obb = 
                            new OrientedBoundingBox((Vector3f)loc.clone(), new Vector3f(Box.DIMENSION, Box.DIMENSION, Box.DIMENSION));
                    platforms.add(new Box(obb, cellColor));
                    loc.y += 5.0f;
                    stage.getPlayer().position = loc;
                    stage.getPlayer().setStartingPos(loc);
                    break;
                    }
                case END: {
                    final OrientedBoundingBox obb = 
                            new OrientedBoundingBox(new Vector3f(i * Box.DIMENSION, layer * Box.DIMENSION, j * Box.DIMENSION), 
                            new Vector3f(Box.DIMENSION, Box.DIMENSION, Box.DIMENSION));
                    final Box endPlatform = new Box(obb, cellColor);
                    endPlatform.setIsGoal(true);
                    platforms.add(endPlatform);
                    break;
                    }
                case BLOCK: {
                    final OrientedBoundingBox obb = new OrientedBoundingBox(new Vector3f(i * Box.DIMENSION, layer * Box.DIMENSION, j * Box.DIMENSION),
                                        new Vector3f(Box.DIMENSION, Box.DIMENSION, Box.DIMENSION), new Vector3f());
                    walls.add(new Box(obb, cellColor));
                    break;
                    } 
                case PUSH: {
                    final double angle = (cellInfo.getGreen() * MAX_RADIANS) / MAX_COLOR_VAL;
                    final int mag = (cellInfo.getBlue() * MAX_MAGNITUDE) / MAX_COLOR_VAL;
                    addPush(j, i, layer, new Vector3f((float)Math.cos(angle), 0, 
                                        (float)-Math.sin(angle)), mag, forces, stage);
                    break;
                    }
                case RAMP_NORTH: {
                    final Vector3f position = new Vector3f(new Vector3f(i * Box.DIMENSION, layer * Box.DIMENSION, j * Box.DIMENSION));
                    final float sqrtOfTwo = (float) Math.sqrt(2);
                    position.add(new Vector3f(0, Box.DIMENSION * (1 - sqrtOfTwo / 4), -Box.DIMENSION * sqrtOfTwo / 4));
                    final float dim = (float) Math.sqrt(Box.DIMENSION * Box.DIMENSION + Box.DIMENSION * Box.DIMENSION);
                    final OrientedBoundingBox obb = new OrientedBoundingBox(position, new Vector3f(Box.DIMENSION, Box.DIMENSION, dim),
                            new Vector3f((float) (Math.PI / 4), 0, 0));
                    walls.add(new Box(obb, cellColor));
                    break;
                    }
                case RAMP_SOUTH: {
                    final Vector3f position = new Vector3f(new Vector3f(i * Box.DIMENSION, layer * Box.DIMENSION, j * Box.DIMENSION));
                    final float sqrtOfTwo = (float) Math.sqrt(2);
                    position.add(new Vector3f(0, Box.DIMENSION * (1 - sqrtOfTwo / 4), Box.DIMENSION * sqrtOfTwo / 4));
                    final float dim = (float) Math.sqrt(Box.DIMENSION * Box.DIMENSION + Box.DIMENSION * Box.DIMENSION);
                    final OrientedBoundingBox obb = new OrientedBoundingBox(position, new Vector3f(Box.DIMENSION, Box.DIMENSION, dim),
                            new Vector3f((float) (-Math.PI / 4), 0, 0));
                    walls.add(new Box(obb, cellColor));
                    break;
                    }
                case RAMP_WEST: {
                    final Vector3f position = new Vector3f(new Vector3f(i * Box.DIMENSION, layer * Box.DIMENSION, j * Box.DIMENSION));
                    final float sqrtOfTwo = (float) Math.sqrt(2);
                    position.add(new Vector3f(-Box.DIMENSION * sqrtOfTwo / 4, Box.DIMENSION * (1 - sqrtOfTwo / 4), 0));
                    final float dim = (float) Math.sqrt(Box.DIMENSION * Box.DIMENSION + Box.DIMENSION * Box.DIMENSION);
                    final OrientedBoundingBox obb = new OrientedBoundingBox(position, new Vector3f(dim, Box.DIMENSION, Box.DIMENSION),
                            new Vector3f(0, 0, (float) (-Math.PI / 4)));
                    walls.add(new Box(obb, cellColor));
                    break;
                    }
                case RAMP_EAST: {
                    final Vector3f position = new Vector3f(new Vector3f(i * Box.DIMENSION, layer * Box.DIMENSION, j * Box.DIMENSION));
                    final float sqrtOfTwo = (float) Math.sqrt(2);
                    position.add(new Vector3f(Box.DIMENSION * sqrtOfTwo / 4, Box.DIMENSION * (1 - sqrtOfTwo / 4), 0));
                    final float dim = (float) Math.sqrt(Box.DIMENSION * Box.DIMENSION + Box.DIMENSION * Box.DIMENSION);
                    final OrientedBoundingBox obb = new OrientedBoundingBox(position, new Vector3f(dim, Box.DIMENSION, Box.DIMENSION),
                            new Vector3f(0, 0, (float) (Math.PI / 4)));
                    walls.add(new Box(obb, cellColor));
                    break;
                    }
                }
            }
        }
    }
    
    private static void addPush(int row, int col, int layer, Vector3f direction, float magnitude, 
                                    final List<Force> forces, final Stage result)
    {
        direction.normalize();
        Push push = new Push(direction, magnitude);
        push.setEnabled(false);
        forces.add(push);
        
        OrientedBoundingBox obb =
                new OrientedBoundingBox(new Vector3f(col * Box.DIMENSION, layer * Box.DIMENSION, row * Box.DIMENSION),
                new Vector3f(Box.DIMENSION, Box.DIMENSION, Box.DIMENSION));
        
        PushZone pz = new PushZone(result.getPlayer(), push, obb);
        
        result.addZones(pz);
    }
}
