
package tears.of.paint.v2;

import org.junit.Assert;
import org.junit.Test;


public class JUnitTest {
    
    //Testing for success in the variable change in PaintTabs
    @Test
    public void testChange(){
        PaintTabs paint = new PaintTabs();
        boolean b = false; //Should be false
        Assert.assertEquals(b, paint.getChange());
    }
    
    //Testing for success in the variable width in PaintCanvas
    @Test
    public void testCanWidth(){
        PaintCanvas canvas = new PaintCanvas();
        double w = 1920; //Should be 1920
        Assert.assertEquals(w, canvas.getWidth(), 0);
    }
    
    //Testing for success in the variable x1 in PaintCanvas
    @Test
    public void testCanx1(){
        PaintCanvas canvas = new PaintCanvas();
        double x = 0; //Should be 0
        Assert.assertEquals(x, canvas.x1, 0);
    }
    
}
