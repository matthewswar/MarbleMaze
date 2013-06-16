Advanced levels can be created by selecting a folder that contains multiple PNG images (Example: level0).
These images are read in alphabetical order and represent different layers of the level.
Unlike the basic level images, the color of the pixels in these images represent the actual color of the block.
Different kinds of blocks are represented with different alpha channel values:
	255 - Spawns a block that the marble can collide with.
	254 - Spawns a ramp that faces the north.
	253 - Spawns a ramp that faces the south.
	252 - Spawns a ramp that faces the west.
	251 - Spawns a ramp that faces the east.
	250 - Spawns a push field. Unlike the other blocks, the color on these blocks do not change and
		  are handled like the ones mentioned in the basic readme. See basic readme for further details.
	249 - The spawning block of the marble.
	248 - The goal block the marble must reach.