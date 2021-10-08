
package tears.of.paint.v2;

import java.awt.image.RenderedImage;
import java.util.TimerTask;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import static tears.of.paint.v2.PaintToolBar.update;

public class Autosave extends TimerTask{
    
    //These allow for autosaving
    private int t;
    private File outputf;
    private int i;
    
    public Autosave(){
        Platform.runLater(() -> {
            t = parseInt(PaintToolBar.autotext.getText());
        });
        outputf = new File("Resources\\Project autosave.png");
    }
    
    @Override
    public void run(){
        
        //Updating the label
        Platform.runLater(() -> {
        PaintToolBar.update = new Label("Autosave activates in " + t + " seconds!");
        PaintToolBar.textlayout3.getChildren().setAll(PaintToolBar.update, PaintToolBar.autotext);
        if (PaintToolBar.check1.isSelected() == false) {
            update.setVisible(false);
        } else {
            update.setVisible(true);
            }
        });
        t--;
        
        if (t == 0) {
            
            //The actual saving process of autosaving
            try {
                WritableImage writableImage = new WritableImage((int) PaintTabs.canvas.getWidth(), (int) PaintTabs.canvas.getHeight());
                Platform.runLater(() -> {
                    PaintTabs.canvas.snapshot(null, writableImage);
                });
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png",  new File("Resources\\Project autosave.png"));
            } catch (IOException exp) {
                Logger.getLogger(TearsOfPaintV2.class.getName()).log(Level.SEVERE, null, exp);
            }
            
            //Updating the time so it does not constantly autosave
            Platform.runLater(() -> {
                t = parseInt(PaintToolBar.autotext.getText());
            });
            
            
        }
        }
        
    
}
