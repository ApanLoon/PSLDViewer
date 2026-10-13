package psdlviewer;
import psdlviewer.*;

/** Road strip, 5 cross-sections, with sidewalks.
 *
 *  3-----2----------------|---------------1-----0
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  7-----6----------------|---------------5-----4
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  b-----a----------------|---------------9-----8
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  f-----e----------------|---------------9-----c
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 * 13----12----------------|--------------11----10
 *
 * The road section is automatically lowered to make a step up to the
 * sidewalk. The road texture is also mirrored in the center line.
 *
 * Texture usage:
 *
 * For 0x0050 = n, texture n - 1 is used for the road section and texture n
 * is used for the sidewalks.
 *
 */

public class Attribute0005
extends Attribute0000
{
    public Attribute0005()
    {
	super();
	nSections = 5;
	id = 0x0005;
    }
}
