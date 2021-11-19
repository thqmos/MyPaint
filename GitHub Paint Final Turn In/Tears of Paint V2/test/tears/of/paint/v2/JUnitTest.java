
package tears.of.paint.v2;

import org.junit.Assert;
import org.junit.Test;
import tears.of.paint.v2.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;


public class JUnitTest {
    
    //Testing for success in the variable change in PaintTab
    @Test
    public void testChange(){
        PaintTab paint = new PaintTab();
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

    //Testing for success in the entirety of the application
    @Test
    public void testA() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                // Initializes the JavaFx Platform
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try{
                            new TearsOfPaintV2().start(new Stage());
                        }
                        catch (Exception E){
                            System.out.println("Something went wrong...");
                        }
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(10000); 
    }

}
    
