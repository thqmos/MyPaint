
package tears.of.paint.v2;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import tears.of.paint.v2.*;
import static tears.of.paint.v2.PaintCanvas.currentTool;
import static tears.of.paint.v2.PaintCanvas.log;


public class PaintTab extends Tab{
    protected static Pane pane;
    protected static PaintCanvas canvas;
    private File path;
    private FileChooser fileChooser;
    protected static boolean change;
    private ScrollPane actualLayout;
    private String title;
    private StackPane stack;
    private File saveFile;
    
    
    /**
     * THE constructor for PaintTabs.
     */
    public PaintTab(){
        super();
        change = false;
        path = null;
        title = "Untitled";
        canvas = new PaintCanvas();
        setup();
        
        PaintMenuBar.selectImage.setOnAction(actionEvent -> {
            //Further implementation of the file chooser
            File selectedFile = fileChooser.showOpenDialog(TearsOfPaintV2.stage);
            
            //This is how the image becomes viewable from the GUI
            try{
                Image image1 = new Image(selectedFile.toURI().toString());
                canvas.setWidth(image1.getWidth());
                canvas.setHeight(image1.getHeight());
                canvas.graphic.drawImage(image1, 0, 0);
            }
            catch(Exception e){
                System.out.println("Something went wrong...");
            }
            path = selectedFile;
            logAction(" Image opened.");
            updateTabTitle();
        });
        
        PaintMenuBar.selectImage.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        
        //This is how the save menu item works
        PaintMenuBar.Save.setOnAction(eh ->{

            //Tests if the file has been saved at all or not
            if (saveFile == null){
                saveasm(canvas, fileChooser, TearsOfPaintV2.stage);
                change = false;
            }

            //This is what happens if the file has been saved before
            else{
                //Tests to see if it can even save, if it can, move forward
                if(saveFile != null){
                    try {
                        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                        canvas.snapshot(null, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", saveFile);
                        change = false;
                        logAction(" Image saved.");
                    }
                    catch (IOException exp){
                        Logger.getLogger(TearsOfPaintV2.class.getName()).log(Level.SEVERE, null, exp);
                    }
                    
                }
            }
        });
        
        PaintMenuBar.Save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        
        
        //This is how the save as menu item works
        PaintMenuBar.SaveAs.setOnAction(aevent ->{
            saveasm(canvas, fileChooser, TearsOfPaintV2.stage);
            change = false;
        });
        
        PaintMenuBar.SaveAs.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        
        //This is how the application gets closed nicely
        PaintMenuBar.CloseWS.setOnAction(actionEvent -> {
            
            //What if no changes have been made
            if (change == false) {
                //Setup of the closure stage
                Stage closureStage = new Stage();
                Label question = new Label("Exit without saving?");
                Button yes = new Button("Yes!");
                Button no = new Button("No...");

                //Arranging the contents so it looks decent
                VBox closureLayout = new VBox(question, no, yes);
                closureLayout.setSpacing(30);
                closureLayout.setAlignment(Pos.BASELINE_CENTER);

                //The continuation of the setup of the closure stage
                Scene closureScene = new Scene(closureLayout, 400, 200);
                closureStage.setScene(closureScene);
                closureStage.show();

                //Actions for each of the buttons of the closure stage
                yes.setOnAction(e -> {
                    closureStage.close();
                    TearsOfPaintV2.stage.close(); 
                    Platform.exit();
                    System.exit(0);
                });
                no.setOnAction(e -> {
                    closureStage.close();
                });
            }
            
            //What if changes have been made?
            else if (change == true){
                //Setup of the closure stage
                Stage closureStage = new Stage();
                Label question = new Label("You haven't saved in a while! Exit without saving?");
                Button save = new Button("Save!");
                Button saveas = new Button("Save As!");
                Button yes = new Button("Yes!");
                
                //Arranging the contents so it looks decent
                VBox closureLayout = new VBox(question, save, saveas, yes);
                closureLayout.setSpacing(10);
                closureLayout.setAlignment(Pos.BASELINE_CENTER);
                
                //The continuation of the setup of the closure stage
                Scene closureScene = new Scene(closureLayout, 400, 200);
                closureStage.setScene(closureScene);
                closureStage.show();
                
                //Actions for each of the buttons of the closure stage
                yes.setOnAction(e -> {
                    closureStage.close();
                    TearsOfPaintV2.stage.close();
                    Platform.exit();
                    System.exit(0);
                });
                save.setOnAction(e -> {
                    //Tests if the file has been saved at all or not
                    if (saveFile == null) {
                        saveasm(canvas, fileChooser, TearsOfPaintV2.stage);
                    } //This is what happens if the file has been saved before
                    else {
                        //Tests to see if it can even save, if it can, move forward
                        if (saveFile != null) {
                            try {
                                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                                canvas.snapshot(null, writableImage);
                                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                                ImageIO.write(renderedImage, "png", saveFile);
                                change = false;
                            } catch (IOException exp) {
                                Logger.getLogger(TearsOfPaintV2.class.getName()).log(Level.SEVERE, null, exp);
                            }
                        }
                    }
                    change = false;
                    TearsOfPaintV2.stage.close();
                    closureStage.close();
                    Platform.exit();
                    System.exit(0);
                });
                //What if the user presses the "save as" button?
                saveas.setOnAction(e -> {
                    saveasm(canvas, fileChooser, TearsOfPaintV2.stage);
                    change = false;
                    TearsOfPaintV2.stage.close();
                    closureStage.close();
                    Platform.exit();
                    System.exit(0);
                });
            }
            logAction(" Application closed.");
        });
        
        PaintMenuBar.CloseWS.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        
    }
    
    /**
     * This method sets up the tabs. This does not need to be called outside of this file
     */
    private void setup(){
        //Image file types, creation and implementation of the file chooser
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png")
                ,new FileChooser.ExtensionFilter("Image Files", "*.jpeg")
                ,new FileChooser.ExtensionFilter("Image Files", "*.gif")
                ,new FileChooser.ExtensionFilter("Image Files", "*.bmp")
                ,new FileChooser.ExtensionFilter("Image Files", "*.jpg")
        );
        pane = new Pane(canvas);
        stack = new StackPane(pane);
        this.actualLayout = new ScrollPane(this.stack);
        this.setContent(actualLayout);
        this.setText((change ? "*" : "") + this.title);
    }

    /**
     * This updates the tab title
     */
        public void updateTabTitle(){
        if(path != null)
            this.title = path.getName();   //sets it to path name in case of update/save as
        if(change == true)
            this.setText("*" + title);
        else
            this.setText(title);
    }
        
    /**
     * Save as method
     * @param canvas from which the snapshot will be taken
     * @param fileChooser determines file formats, also you show save dialog
     * @param frame displays everything
     */
    public void saveasm(Canvas canvas, FileChooser fileChooser, Stage frame) {
        //Show save file dialog
        File file = fileChooser.showSaveDialog(frame);

        //Tests to see if it can even save, if it can, move forward
        try {
            WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException ex) {
            Logger.getLogger(TearsOfPaintV2.class.getName()).log(Level.SEVERE, null, ex);
        }
        saveFile = file;
        logAction(" Image saved as.");

    }
    
    /**
     * Accessor for change
     * @return change's value, which is a boolean
     */
    public boolean getChange(){
        return change;
    }
    
    /**
     * Accessor for title
     * @return title's value, which is a String
     */
    public String getTitle(){
        return title;
    }
    
    /**
     * This is the method which logs the actions being done
     * @param action, this is the string that describes what is being used/clicked.
     */
    public void logAction(String action){
        
        //Getting it so that the timestamps display cleanly
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        formatter.format(date);
        
        //Working with the file
        File fileToBeModified = new File("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\MyPaintLogging.log");
        FileHandler fh;
        try {
            
            //The actual logging process
            fh = new FileHandler("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\MyPaintLogging.log");
            log.addHandler(fh);
            SimpleFormatter sf = new SimpleFormatter();  
            fh.setFormatter(sf);
            
            //If the currentTool is equal to action, don't log it, otherwise, log the action
            if (currentTool != action) {
                currentTool = action;
                log.info(date + ":" + currentTool);
            }
            
        } catch (SecurityException e) {
            System.out.println("Something went wrong...");
        } catch (IOException L) {
            System.out.println("Something went wrong...");
        }
        catch (NullPointerException yikes){
            System.out.println("Something went wrong...");
        }
    }
    
    /**
     * This method sets the canvas of a tab to whatever we need
     * @param canvas, this is the canvas we will be setting the PaintCanvas of the tab to
     */
    public void setCanvas(PaintCanvas canvas){
        this.canvas = canvas;
    }
    

}
