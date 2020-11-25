

import pixeljelly.features.Palette;
import pixeljelly.ops.ColorDitheringOp;
import pixeljelly.ops.SierraColorDitheringOp;

public class Sierra_2_4AColorDitheringOp extends ColorDitheringOp
{
    private static float[][] diffusionMatrix;
    
    public Sierra_2_4AColorDitheringOp(Palette pal) 
    {
        super(pal);
        Sierra_2_4AColorDitheringOp.diffusionMatrix = new float[][] {
        	{ 0.0f, 0.0f, 2.0f/4},
        	{ 1.0f/4, 1.0f/4, 0.0f}
        };
    }
    
    public float[][] getDiffusionMatrix()
    {
        return Sierra_2_4AColorDitheringOp.diffusionMatrix;
    }
 
}