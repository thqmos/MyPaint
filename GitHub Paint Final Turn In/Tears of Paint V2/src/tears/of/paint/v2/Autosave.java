
package tears.of.paint.v2;

import java.awt.image.RenderedImage;
import java.util.TimerTask;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import static tears.of.paint.v2.PaintToolBar.update;
import tears.of.paint.v2.*;

public class Autosave extends TimerTask{
    
    //These allow for autosaving
    private int t;
    private File outputf;
    private WritableImage writableImage;
    private RenderedImage renderedImage;
    
    /**
     * This is THE constructor for the auto save class.
     */
    public Autosave(){
            Platform.runLater(() -> {
                t = PaintToolBar.changeauto.getValue();
            });
        outputf = new File("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\Project autosave.png");
        writableImage = null;
        renderedImage = null;
    }
    
    @Override
    /**
     * This is what all happens during the auto saving process.
     */
    public void run(){
        
        //Updating the label
        Platform.runLater(() -> {
            PaintToolBar.update = new Label("Autosave activates in " + t + " seconds!");
            PaintToolBar.AutoTextLayout.getChildren().setAll(PaintToolBar.update, PaintToolBar.changeauto);
            if (PaintToolBar.check1.isSelected() == false) {
                update.setVisible(false);
            } else {
                update.setVisible(true);
            }
        });
        t--;
        
        if (t == 0) {
            
            //The actual saving process of autosaving
            Platform.runLater(() -> {
                try {
                    writableImage = new WritableImage((int) PaintTab.canvas.getWidth(), (int) PaintTab.canvas.getHeight());
                    PaintTab.canvas.snapshot(null, writableImage);
                    renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", new File("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\Project autosave.png"));
                } catch (IOException exp) {
                    Logger.getLogger(TearsOfPaintV2.class.getName()).log(Level.SEVERE, null, exp);
                }
            });
            //Updating the time so it does not constantly autosave
            Platform.runLater(() -> {
                t = PaintToolBar.changeauto.getValue();
            });
        }
        }
        
    
}
