package psdlviewer;
import psdlviewer.*;

public class HeightList
extends java.util.Vector
{

    public Height heightAt (int i)
    {
	return (Height)super.elementAt (i);
    }

    public Height getHeight (HeightRef ref)
    {
	return heightAt (ref.getIndex());
    }
}
