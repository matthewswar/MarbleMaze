Levels must be created with a small (usually around 10x10 pixel) PNG image.
Recognized color codes:
	Blue (0, 0, 255) - The spawning location of the marble.
	Red  (255, 0, 0) - The goal block the marble must reach.
	White (255, 255, 255) - Platforms the marble can traverse.
	Black (0, 0, 0) - Walls the marble can collide with.
	
Advanced color code: Pixels with a red value of EXACTLY 42 will spawn a "Force" block. 
This block takes the green value as the angle for the direction of the force and blue is the magnitude. 
The degree green value must be a ratio of 2^8 - 1 to 360. For example, 90 degrees is represented as a green value of 63 or 64 (x / 255 = 90 / 360, x = 63.75)