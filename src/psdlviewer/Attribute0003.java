package psdlviewer;
import psdlviewer.*;

/** Road strip, 3 cross-sections, with sidewalks.
 *
 *  3-----2----------------|---------------1-----0
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  7-----6----------------|---------------5-----4
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  |     |                |               |     |
 *  b-----a----------------|---------------9-----8
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

public class Attribute0003
extends Attribute0000
{
    public Attribute0003()
    {
	super();
	nSections = 3;
	id = 0x0003;
    }
}
