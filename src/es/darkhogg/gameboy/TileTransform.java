package es.darkhogg.gameboy;

/**
 * Represents the transformation a sprite can do over a tile. This class is
 * meant to be used within a set, to select zero or more transforms.
 * 
 * @author Daniel Escoz (Darkhogg)
 * @version 1.0
 */
public enum TileTransform {
	/**
	 * Performs an horizontal flip
	 */
	HORIZONTAL_FLIP,
	
	/**
	 * Performs a vertical flip
	 */
	VERTICAL_FLIP;
}
