
package mypaint;


//Lots of imports necessary for Paint
import java.awt.image.RenderedImage;
import java.io.*;
import static java.lang.Integer.parseInt;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Modality;
import javafx.scene.text.*;
import javafx.scene.canvas.*;
import javax.imageio.ImageIO;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;

public class MyPaint extends Application{
    
    //These assist us in drawing lines
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    
    //These assist us in making rectangles
    private double rectx;
    private double recty;
    private double rectx2;
    private double recty2;
    
    //This assists us in saving (regular save) an image
    private File saveFile;
    
    //This assists us in zooming in/out
    private double zoom;
    private Scale scale;
    
    //This assists us in creating new tabs
    private int tabId = 0;
    
    //This assists us in the smart save
    private Boolean change;
    
    public void start(Stage frame) throws Exception{
        
        saveFile = null;
        change = false;
        
        //Implementation of the PaintMenuBar
        PaintMenuBar myMenuBar = new PaintMenuBar();
        
        //Implementation of the PaintToolBar
        PaintToolBar myToolBar = new PaintToolBar();
        
        //This assists us in undo/redo
        Stack<Image> undoHistory = new Stack();
        Stack<Image> redoHistory = new Stack();
        
        //Creation and implementation of a file chooser, which will choose the image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png")
                ,new FileChooser.ExtensionFilter("Image Files", "*.jpeg")
                ,new FileChooser.ExtensionFilter("Image Files", "*.gif")
                ,new FileChooser.ExtensionFilter("Image Files", "*.bmp")
                ,new FileChooser.ExtensionFilter("Image Files", "*.jpg")
        );
        
        //What does the newTabOpener do?
        TabPane tabPane = new TabPane();
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext graphic = canvas.getGraphicsContext2D();
        addTab(tabPane, canvas);
        myMenuBar.newTabOpener.setOnAction(eh -> {
            Canvas newCanvas = new Canvas(1920, 1080);
            takeCan(newCanvas);
            addTab(tabPane, newCanvas);
        });
        myMenuBar.newTabOpener.fire();
        
        //Setting up the stage to include various things
        frame.setTitle("My Paint");
        graphic.setFill(Color.WHITE);
        VBox layout = new VBox(myMenuBar.menuBar, myToolBar.tool, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        
        //Setting up the rest of the things
        ScrollPane actualLayout = new ScrollPane(layout);
        Scene scene = new Scene(actualLayout, 500, 300);
        frame.setScene(scene);
        frame.show();
        
        myMenuBar.selectImage.setOnAction(actionEvent -> {
            //Further implementation of the file chooser
            File selectedFile = fileChooser.showOpenDialog(frame);
            
            //This is how the image becomes viewable from the GUI
            try{
                Image image1 = new Image(selectedFile.toURI().toString());
                canvas.setWidth(image1.getWidth());
                canvas.setHeight(image1.getHeight());
                graphic.drawImage(image1, 0, 0);
            }
            catch(Exception e){
                System.out.println("Something went wrong...");
            }
            change = true;
        });
        
        //This is how the color grabber works
        canvas.setOnMouseClicked(eh -> {
            if (myToolBar.colorGrabber.isSelected() == true) {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                PixelReader pr = writableImage.getPixelReader();
                myToolBar.colorPicker.setValue(pr.getColor((int) eh.getX(), (int) eh.getY()));
                myToolBar.colorGrabber.setSelected(false);
            }
        });
        
        //What if the mouse is pressed?
        canvas.setOnMousePressed(eh -> {
            
            //What if the line drawer is selected
            if(myToolBar.LineDrawer.isSelected() == true){
                x1 = eh.getX();
                y1 = eh.getY();
            }
            
            //What if the pencil tool is selected
            else if(myToolBar.PencilTool.isSelected() == true){
                initDraw(graphic, myToolBar.slider1, myToolBar.colorPicker); 
                graphic.beginPath();
                graphic.moveTo(eh.getX(), eh.getY());
                graphic.stroke();
            }
            
            //What if the rectangle tool is selected
            else if(myToolBar.Rectangle.isSelected() == true){
                rectx = eh.getX();
                recty = eh.getY();
            }
            
            //What if the ellipse tool is selected
            else if(myToolBar.Ellipse.isSelected() == true){
                rectx = eh.getX();
                recty = eh.getY();
            }
            
            //What if the circle tool is selected
            else if (myToolBar.Circle.isSelected() == true) {
                rectx = eh.getX();
                recty = eh.getY();
                
            } //What if the square tool is selected
            else if (myToolBar.Square.isSelected() == true) {
                rectx = eh.getX();
                recty = eh.getY();
                
            } //What if the text tool is selected
            else if (myToolBar.Text.isSelected() == true) {
                graphic.setLineWidth(1);
                graphic.setFont(Font.font(myToolBar.slider1.getValue()));
                graphic.setStroke(myToolBar.colorPicker.getValue());
                graphic.setFill(myToolBar.colorPicker.getValue());
                drawText(graphic, eh.getX(), eh.getY());
                change = true;
            }
        });

        //What if the mouse is dragged?
        canvas.setOnMouseDragged(eh -> {

            //What if the pencil tool is selected
            if (myToolBar.PencilTool.isSelected() == true) {
                initDraw(graphic, myToolBar.slider1, myToolBar.colorPicker);
                graphic.lineTo(eh.getX(), eh.getY());
                graphic.stroke();
            }
        });

        canvas.setOnMouseReleased(eh -> {

            //What if the line drawer is selected?
            if (myToolBar.LineDrawer.isSelected() == true) {
                x2 = eh.getX();
                y2 = eh.getY();
                initDraw(graphic, myToolBar.slider1, myToolBar.colorPicker);
                graphic.strokeLine(x1, y1, x2, y2);
                change = true;
                
            } //What if the pencil tool is selected?
            else if (myToolBar.PencilTool.isSelected() == true) {
                change = true;
                
            } //What if the rectangle tool is selected?
            else if (myToolBar.Rectangle.isSelected() == true) {
                rectx2 = eh.getX();
                recty2 = eh.getY();
                initRect(graphic, myToolBar.slider1, myToolBar.colorPicker, myToolBar.c22);

                //What if it goes down and to the right?
                if (rectx2 > rectx && recty2 > recty) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = recty2 - recty;
                    if (myToolBar.check.isSelected() == true) {
                        graphic.strokeRect(rectx, recty, rectwidth, rectheight);
                        graphic.fillRect(rectx, recty, rectwidth, rectheight);
                    } else {
                        graphic.strokeRect(rectx, recty, rectwidth, rectheight);
                    }

                    //What if it goes up and to the right?
                } else if (rectx2 > rectx && recty > recty2) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = recty - recty2;
                    if (myToolBar.check.isSelected() == true) {
                        graphic.strokeRect(rectx, recty2, rectwidth, rectheight);
                        graphic.fillRect(rectx, recty2, rectwidth, rectheight);
                    } else {
                        graphic.strokeRect(rectx, recty2, rectwidth, rectheight);
                    }

                    //What if it goes down and to the left?
                } else if (rectx > rectx2 && recty2 > recty) {
                    double rectwidth = rectx - rectx2;
                    double rectheight = recty2 - recty;
                    if (myToolBar.check.isSelected() == true) {
                        graphic.strokeRect(rectx2, recty, rectwidth, rectheight);
                        graphic.fillRect(rectx2, recty, rectwidth, rectheight);
                    } else {
                        graphic.strokeRect(rectx2, recty, rectwidth, rectheight);
                    }

                    //What if it goes up and to the left?
                } else {
                    double rectwidth = rectx - rectx2;
                    double rectheight = recty - recty2;
                    if (myToolBar.check.isSelected() == true) {
                        graphic.strokeRect(rectx2, recty2, rectwidth, rectheight);
                        graphic.fillRect(rectx2, recty2, rectwidth, rectheight);
                    } else {
                        graphic.strokeRect(rectx2, recty2, rectwidth, rectheight);
                    }

                }
                change = true;
            }
            
            //What if the ellipse tool is selected?
            else if(myToolBar.Ellipse.isSelected() == true){
                rectx2 = eh.getX();
                recty2 = eh.getY();
                initRect(graphic, myToolBar.slider1, myToolBar.colorPicker, myToolBar.c22);
                
                //What if it goes down and to the right?
                if (rectx2 > rectx && recty2 > recty) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = recty2 - recty;
                    if(myToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx, recty, rectwidth, rectheight);
                        graphic.fillOval(rectx, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx, recty, rectwidth, rectheight);
                    }
                    
                    //What if it goes up and to the right?
                } else if (rectx2 > rectx && recty > recty2) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = recty - recty2;
                    if(myToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx, recty2, rectwidth, rectheight);
                        graphic.fillOval(rectx, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx, recty2, rectwidth, rectheight);
                    }
                    
                    //What if it goes down and to the left?
                } else if (rectx > rectx2 && recty2 > recty){
                   double rectwidth = rectx - rectx2;
                   double rectheight = recty2 - recty;
                   if(myToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx2, recty, rectwidth, rectheight);
                        graphic.fillOval(rectx2, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx2, recty, rectwidth, rectheight);
                    } 
                   
                   //What if it goes up and to the left?
                } else{
                    double rectwidth = rectx - rectx2;
                    double rectheight = recty - recty2;
                    if(myToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx2, recty2, rectwidth, rectheight);
                        graphic.fillOval(rectx2, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx2, recty2, rectwidth, rectheight);
                    }   
                }
                change = true;
            }
            
            //What if the square tool is selected?
            else if(myToolBar.Square.isSelected() == true){
                rectx2 = eh.getX();
                recty2 = eh.getY();
                initRect(graphic, myToolBar.slider1, myToolBar.colorPicker, myToolBar.c22);
                
                //What if it goes down and to the right?
                if (rectx2 > rectx && recty2 > recty) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = rectx2 - rectx;
                    if(myToolBar.check.isSelected() == true){
                        graphic.strokeRect(rectx, recty, rectwidth, rectheight);
                        graphic.fillRect(rectx, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeRect(rectx, recty, rectwidth, rectheight);
                    }
                    
                    //What if it goes up and to the right?
                    } else if (rectx2 > rectx && recty > recty2) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = rectx2 - rectx;
                    if(myToolBar.check.isSelected() == true){
                        graphic.strokeRect(rectx, recty2, rectwidth, rectheight);
                        graphic.fillRect(rectx, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeRect(rectx, recty2, rectwidth, rectheight);
                    }
                    
                    //What if it goes down and to the left?
                } else if (rectx > rectx2 && recty2 > recty){
                   double rectwidth = rectx - rectx2;
                   double rectheight = rectx - rectx2;
                   if(myToolBar.check.isSelected() == true){
                        graphic.strokeRect(rectx2, recty, rectwidth, rectheight);
                        graphic.fillRect(rectx2, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeRect(rectx2, recty, rectwidth, rectheight);
                    } 
                   
                   //What if it goes up and to the left?
                } else{
                    double rectwidth = rectx - rectx2;
                    double rectheight = rectx - rectx2;
                    if(myToolBar.check.isSelected() == true){
                        graphic.strokeRect(rectx2, recty2, rectwidth, rectheight);
                        graphic.fillRect(rectx2, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeRect(rectx2, recty2, rectwidth, rectheight);
                    }
                }
                change = true;
            }
            
            //What if the circle tool is selected?
            else if(myToolBar.Circle.isSelected() == true){
                rectx2 = eh.getX();
                recty2 = eh.getY();
                initRect(graphic, myToolBar.slider1, myToolBar.colorPicker, myToolBar.c22);
                if (rectx2 > rectx && recty2 > recty) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = rectx2 - rectx;
                    
                //What if it goes down and to the right?
                    if(myToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx, recty, rectwidth, rectheight);
                        graphic.fillOval(rectx, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx, recty, rectwidth, rectheight);
                    }
                    
                    //What if it goes up and to the right?
                    } else if (rectx2 > rectx && recty > recty2) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = rectx2 - rectx;
                    if(myToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx, recty2, rectwidth, rectheight);
                        graphic.fillOval(rectx, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx, recty2, rectwidth, rectheight);
                    }
                    
                    //What if it goes down and to the left?
                } else if (rectx > rectx2 && recty2 > recty){
                   double rectwidth = rectx - rectx2;
                   double rectheight = rectx - rectx2;
                   if(myToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx2, recty, rectwidth, rectheight);
                        graphic.fillOval(rectx2, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx2, recty, rectwidth, rectheight);
                    } 
                   
                   //What if it goes up and to the left?
                } else{
                    double rectwidth = rectx - rectx2;
                    double rectheight = rectx - rectx2;
                    if(myToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx2, recty2, rectwidth, rectheight);
                        graphic.fillOval(rectx2, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx2, recty2, rectwidth, rectheight);
                    }
                }
                change = true;
            }
        });
        
        //This is how we zoom out
        scale = new Scale(canvas.getWidth(), canvas.getHeight());
        zoom = 1;
        myMenuBar.ZoomOut.setOnAction(eh -> {
            canvas.getTransforms().remove(scale);
            zoom -= 0.1;
            scale = new Scale(zoom, zoom, 0, 0);
            canvas.getTransforms().add(scale);
        });
        
        //This is how we zoom in
        myMenuBar.ZoomIn.setOnAction(eh -> {
            canvas.getTransforms().remove(scale);
            zoom += 0.1;
            scale = new Scale(zoom, zoom, 0, 0);
            canvas.getTransforms().add(scale);
        });
        
        //This is how we resize
        myMenuBar.Resize.setOnAction(eh -> {
            Stage resizeStage = new Stage();
            TextField xTextField = new TextField("Canvas x");
            TextField yTextField = new TextField("Canvas y");
            Button canvasR = new Button("Resize!");
            Label labelR = new Label("What dimensions would you like?");
            GridPane grid = new GridPane();
            grid.add(labelR, 0, 1);
            grid.add(xTextField, 0, 2);
            grid.add(yTextField, 0, 3);
            grid.add(canvasR, 0, 4);
            Scene resizes = new Scene(grid, 300, 200);
            resizeStage.setScene(resizes);
            resizeStage.show();
            
            //What if the button is clicked?
            canvasR.setOnAction(e -> {
               try{
                   WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                   canvas.snapshot(null, writableImage);
                   canvas.setWidth(parseInt(xTextField.getText()));
                   canvas.setHeight(parseInt(yTextField.getText()));
                   graphic.drawImage(writableImage, 0, 0, canvas.getWidth(), canvas.getHeight());
                   resizeStage.close();
                   change = true;
               }
               catch(Exception L){
                   System.out.println("Something went wrong...");
               }
               
            });
            
        });
        
        //This is how the save menu item works
        myMenuBar.Save.setOnAction(eh ->{

            //Tests if the file has been saved at all or not
            if (saveFile == null){
                saveasm(canvas, fileChooser, frame);
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
                    }
                    catch (IOException exp){
                        Logger.getLogger(MyPaint.class.getName()).log(Level.SEVERE, null, exp);
                    }
                }
            }
        });

        //This is how the save as menu item works
        myMenuBar.SaveAs.setOnAction(aevent ->{
            saveasm(canvas, fileChooser, frame);
            change = false;
        });
        
        //This is how the release notes become viewable
        myMenuBar.ReleaseNotes.setOnAction(actionEvent -> {
            
            //Creation of the new window where the release notes will open
            Stage Release = new Stage();
            Release.setTitle("Release Notes");
            
            //This eliminates any hindrances to the opening of the release notes
            Release.initModality(Modality.APPLICATION_MODAL);
            Release.initOwner(frame);
            
            //This is how the text gets read from the release notes file
            Text t = new Text();
            File releaseText = new File("Resources\\MyPaintThomasHohnholtReleaseNotes9-10-18.txt");
            Scanner console = null;
            try{
                console = new Scanner(releaseText);
            }
            catch(FileNotFoundException fail){
                System.out.println("File not found!");
            }
            while(console.hasNext()){
                t.setText(t.getText()+console.nextLine()+"\n");
            }
            
            //Setting up and showing the release notes stage
            ScrollPane releasePane = new ScrollPane();
            releasePane.setContent(t);
            Scene ReleaseScene = new Scene(releasePane, 500, 300);
            Release.setScene(ReleaseScene);
            Release.show();
        });
        
        //This is how the about window become viewable
        myMenuBar.About.setOnAction(actionEvent -> {
            
            //Creation of the new window where the about window will open
            Stage AboutS = new Stage();
            AboutS.setTitle("About");
            
            //This eliminates any hindrances to the opening of the about window
            AboutS.initModality(Modality.APPLICATION_MODAL);
            AboutS.initOwner(frame);
            
            //This is how the text gets read from the about file
            Text t = new Text();
            File aboutText = new File("Resources\\MyPaintAbout.txt");
            Scanner console = null;
            try{
                console = new Scanner(aboutText);
            }
            catch(FileNotFoundException fail){
                System.out.println("File not found!");
            }
            while(console.hasNext()){
                t.setText(t.getText()+console.nextLine()+"\n");
            }
            
            //Setting up and showing the about stage
            ScrollPane aboutPane = new ScrollPane();
            aboutPane.setContent(t);
            Scene AboutScene = new Scene(aboutPane, 500, 300);
            AboutS.setScene(AboutScene);
            AboutS.show();
        });
        
        //This is how the help window become viewable
        myMenuBar.HelpM.setOnAction(actionEvent -> {
            
            //Creation of the new window where the help window will open
            Stage HelpS = new Stage();
            HelpS.setTitle("Help");
            
            //This eliminates any hindrances to the opening of the help window
            HelpS.initModality(Modality.APPLICATION_MODAL);
            HelpS.initOwner(frame);
            
            //This is how the text gets read from the help file
            Text t = new Text();
            File HelpText = new File("Resources\\MyPaintHelp.txt");
            Scanner console = null;
            try{
                console = new Scanner(HelpText);
            }
            catch(FileNotFoundException fail){
                System.out.println("File not found!");
            }
            while(console.hasNext()){
                t.setText(t.getText()+console.nextLine()+"\n");
            }
            
            //Setting up and showing the help stage
            ScrollPane aboutPane = new ScrollPane();
            aboutPane.setContent(t);
            Scene HelpScene = new Scene(aboutPane, 500, 300);
            HelpS.setScene(HelpScene);
            HelpS.show();
        });
        
        //This is how the application gets closed nicely
        myMenuBar.CloseWS.setOnAction(actionEvent -> {
            
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
                    frame.close();
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
                    frame.close();
                });
                save.setOnAction(e -> {
                    //Tests if the file has been saved at all or not
                    if (saveFile == null) {
                        saveasm(canvas, fileChooser, frame);
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
                                Logger.getLogger(MyPaint.class.getName()).log(Level.SEVERE, null, exp);
                            }
                        }
                    }
                    change = false;
                    frame.close();
                    closureStage.close();
                });
                saveas.setOnAction(e -> {
                    saveasm(canvas, fileChooser, frame);
                    change = false;
                    frame.close();
                    closureStage.close();
                });
            }
        });
        
        //Keybinds/Keyboard UI Controls
        
        //Save keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.S,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    //Tests if the file has been saved at all or not
                    if (saveFile == null) {
                        saveasm(canvas, fileChooser, frame);
                    } //This is what happens if the file has been saved before
                    else {
                        //Tests to see if it can even save, if it can, move forward
                        if (saveFile != null) {
                            try {
                                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                                canvas.snapshot(null, writableImage);
                                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                                ImageIO.write(renderedImage, "png", saveFile);
                            } catch (IOException exp) {
                                Logger.getLogger(MyPaint.class.getName()).log(Level.SEVERE, null, exp);
                            }
                        }
                    }
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });
        
        //Save as keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.D,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    saveasm(canvas, fileChooser, frame);
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });

        //Open/Select Image keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.O,
                    KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    //Further implementation of the file chooser
                    File selectedFile = fileChooser.showOpenDialog(frame);

                    //This is how the image becomes viewable from the GUI
                    try {
                        Image image1 = new Image(selectedFile.toURI().toString());
                        canvas.setWidth(image1.getWidth());
                        canvas.setHeight(image1.getHeight());
                        graphic.drawImage(image1, 0, 0);
                    } catch (Exception e) {
                        System.out.println("Something went wrong...");
                    }
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });

        //Closure keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.Q,
                    KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
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
                            frame.close();
                        });
                        no.setOnAction(e -> {
                            closureStage.close();
                        });
                    } //What if changes have been made?
                    else if (change == true) {
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
                            frame.close();
                        });
                        save.setOnAction(e -> {
                            //Tests if the file has been saved at all or not
                            if (saveFile == null) {
                                saveasm(canvas, fileChooser, frame);
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
                                        Logger.getLogger(MyPaint.class.getName()).log(Level.SEVERE, null, exp);
                                    }
                                }
                            }
                            change = false;
                            frame.close();
                            closureStage.close();
                        });
                        saveas.setOnAction(e -> {
                            saveasm(canvas, fileChooser, frame);
                            change = false;
                            frame.close();
                            closureStage.close();
                        });
                    }
                }
            }
                
                });
        
        //About keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.A,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    //Creation of the new window where the about window will open
                    Stage AboutS = new Stage();
                    AboutS.setTitle("About");

                    //This eliminates any hindrances to the opening of the about window
                    AboutS.initModality(Modality.APPLICATION_MODAL);
                    AboutS.initOwner(frame);

                    //This is how the text gets read from the about file
                    Text t = new Text();
                    File aboutText = new File("Resources\\MyPaintAbout.txt");
                    Scanner console = null;
                    try {
                        console = new Scanner(aboutText);
                    } catch (FileNotFoundException fail) {
                        System.out.println("File not found!");
                    }
                    while (console.hasNext()) {
                        t.setText(t.getText() + console.nextLine() + "\n");
                    }

                    //Setting up and showing the about stage
                    ScrollPane aboutPane = new ScrollPane();
                    aboutPane.setContent(t);
                    Scene AboutScene = new Scene(aboutPane, 500, 300);
                    AboutS.setScene(AboutScene);
                    AboutS.show();
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });
        
        //Help Keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.H,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    //Creation of the new window where the help window will open
                    Stage HelpS = new Stage();
                    HelpS.setTitle("Help");

                    //This eliminates any hindrances to the opening of the help window
                    HelpS.initModality(Modality.APPLICATION_MODAL);
                    HelpS.initOwner(frame);

                    //This is how the text gets read from the help file
                    Text t = new Text();
                    File HelpText = new File("Resources\\MyPaintHelp.txt");
                    Scanner console = null;
                    try {
                        console = new Scanner(HelpText);
                    } catch (FileNotFoundException fail) {
                        System.out.println("File not found!");
                    }
                    while (console.hasNext()) {
                        t.setText(t.getText() + console.nextLine() + "\n");
                    }

                    //Setting up and showing the help stage
                    ScrollPane aboutPane = new ScrollPane();
                    aboutPane.setContent(t);
                    Scene HelpScene = new Scene(aboutPane, 500, 300);
                    HelpS.setScene(HelpScene);
                    HelpS.show();
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });

        //Release notes keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.R,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    //Creation of the new window where the release notes will open
                    Stage Release = new Stage();
                    Release.setTitle("Release Notes");

                    //This eliminates any hindrances to the opening of the release notes
                    Release.initModality(Modality.APPLICATION_MODAL);
                    Release.initOwner(frame);

                    //This is how the text gets read from the release notes file
                    Text t = new Text();
                    File releaseText = new File("Resources\\MyPaintThomasHohnholtReleaseNotes9-10-18.txt");
                    Scanner console = null;
                    try {
                        console = new Scanner(releaseText);
                    } catch (FileNotFoundException fail) {
                        System.out.println("File not found!");
                    }
                    while (console.hasNext()) {
                        t.setText(t.getText() + console.nextLine() + "\n");
                    }

                    //Setting up and showing the release notes stage
                    ScrollPane releasePane = new ScrollPane();
                    releasePane.setContent(t);
                    Scene ReleaseScene = new Scene(releasePane, 500, 300);
                    Release.setScene(ReleaseScene);
                    Release.show();
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });
        
        //File menu keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.F,
                    KeyCombination.ALT_ANY);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    myMenuBar.File.show();
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });
        
        //Zoom out keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.MINUS,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    canvas.getTransforms().remove(scale);
                    zoom -= 0.1;
                    scale = new Scale(zoom, zoom, 0, 0);
                    canvas.getTransforms().add(scale);
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });
        
        //Zoom in keybind
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.DIGIT0,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    canvas.getTransforms().remove(scale);
                    zoom += 0.1;
                    scale = new Scale(zoom, zoom, 0, 0);
                    canvas.getTransforms().add(scale);
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });
        
    }
    
    //This is the method called which assists in the creation of the pencil tool/line drawer
    public void initDraw(GraphicsContext gc, Slider s, ColorPicker c){
         gc.setLineWidth(s.getValue());
         gc.setStroke(c.getValue());
         gc.setFill(c.getValue());
    }
    
    //This is the method called which assists in the creation of the shapes tools
    public void initRect(GraphicsContext gc, Slider s, ColorPicker c, ColorPicker c2){
        gc.setLineWidth(s.getValue());
        gc.setStroke(c.getValue());
        gc.setFill(c2.getValue());
    }
    
    public void drawText(GraphicsContext gc, double x, double y){
        gc.strokeText(PaintToolBar.textField.getText(), x, y);
        gc.fillText(PaintToolBar.textField.getText(), x, y);
    }
    
    //This is how the pencil tool/line drawer disable
    public void disable(GraphicsContext gc){
        gc.setLineWidth(0);
        gc.setStroke(null);
        gc.setFill(null);
    }
    
    //Save as method
    public void saveasm(Canvas canvas, FileChooser fileChooser, Stage frame) {
        //Show save file dialog
        File file = fileChooser.showSaveDialog(frame);

        //Tests to see if it can even save, if it can, move forward
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                Logger.getLogger(MyPaint.class.getName()).log(Level.SEVERE, null, ex);
            }
            saveFile = file;
        }
    }
    
    //Tab addition
    private void addTab(TabPane tabPane, Canvas canvas) {
        Tab tab = new Tab("Tab: " + tabId++);
        tab.setContent(canvas);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }
    
    //Canvas duplication, specifically, event handlers
    public Canvas takeCan(Canvas canvas){
        GraphicsContext graphic = canvas.getGraphicsContext2D();
        
        //This is how the color grabber works
        canvas.setOnMouseClicked(eh -> {
            if (PaintToolBar.colorGrabber.isSelected() == true) {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                PixelReader pr = writableImage.getPixelReader();
                PaintToolBar.colorPicker.setValue(pr.getColor((int) eh.getX(), (int) eh.getY()));
                PaintToolBar.colorGrabber.setSelected(false);
            }
        });
        
        //What if the mouse is pressed?
        canvas.setOnMousePressed(eh -> {
            
            //What if the line drawer is selected
            if(PaintToolBar.LineDrawer.isSelected() == true){
                x1 = eh.getX();
                y1 = eh.getY();
            }
            
            //What if the pencil tool is selected
            else if(PaintToolBar.PencilTool.isSelected() == true){
                initDraw(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker); 
                graphic.beginPath();
                graphic.moveTo(eh.getX(), eh.getY());
                graphic.stroke();
            }
            
            //What if the rectangle tool is selected
            else if(PaintToolBar.Rectangle.isSelected() == true){
                rectx = eh.getX();
                recty = eh.getY();
            }
            
            //What if the ellipse tool is selected
            else if(PaintToolBar.Ellipse.isSelected() == true){
                rectx = eh.getX();
                recty = eh.getY();
            }
            
            //What if the circle tool is selected
            else if (PaintToolBar.Circle.isSelected() == true) {
                rectx = eh.getX();
                recty = eh.getY();
                
            } //What if the square tool is selected
            else if (PaintToolBar.Square.isSelected() == true) {
                rectx = eh.getX();
                recty = eh.getY();
                
            } //What if the text tool is selected
            else if (PaintToolBar.Text.isSelected() == true) {
                graphic.setLineWidth(1);
                graphic.setFont(Font.font(PaintToolBar.slider1.getValue()));
                graphic.setStroke(PaintToolBar.colorPicker.getValue());
                graphic.setFill(PaintToolBar.colorPicker.getValue());
                drawText(graphic, eh.getX(), eh.getY());
                change = true;
            }
        });

        //What if the mouse is dragged?
        canvas.setOnMouseDragged(eh -> {

            //What if the pencil tool is selected
            if (PaintToolBar.PencilTool.isSelected() == true) {
                initDraw(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker);
                graphic.lineTo(eh.getX(), eh.getY());
                graphic.stroke();
            }
        });

        canvas.setOnMouseReleased(eh -> {

            //What if the line drawer is selected?
            if (PaintToolBar.LineDrawer.isSelected() == true) {
                x2 = eh.getX();
                y2 = eh.getY();
                initDraw(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker);
                graphic.strokeLine(x1, y1, x2, y2);
                change = true;
                
            } //What if the pencil tool is selected?
            else if (PaintToolBar.PencilTool.isSelected() == true) {
                change = true;
                
            } //What if the rectangle tool is selected?
            else if (PaintToolBar.Rectangle.isSelected() == true) {
                rectx2 = eh.getX();
                recty2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);

                //What if it goes down and to the right?
                if (rectx2 > rectx && recty2 > recty) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = recty2 - recty;
                    if (PaintToolBar.check.isSelected() == true) {
                        graphic.strokeRect(rectx, recty, rectwidth, rectheight);
                        graphic.fillRect(rectx, recty, rectwidth, rectheight);
                    } else {
                        graphic.strokeRect(rectx, recty, rectwidth, rectheight);
                    }

                    //What if it goes up and to the right?
                } else if (rectx2 > rectx && recty > recty2) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = recty - recty2;
                    if (PaintToolBar.check.isSelected() == true) {
                        graphic.strokeRect(rectx, recty2, rectwidth, rectheight);
                        graphic.fillRect(rectx, recty2, rectwidth, rectheight);
                    } else {
                        graphic.strokeRect(rectx, recty2, rectwidth, rectheight);
                    }

                    //What if it goes down and to the left?
                } else if (rectx > rectx2 && recty2 > recty) {
                    double rectwidth = rectx - rectx2;
                    double rectheight = recty2 - recty;
                    if (PaintToolBar.check.isSelected() == true) {
                        graphic.strokeRect(rectx2, recty, rectwidth, rectheight);
                        graphic.fillRect(rectx2, recty, rectwidth, rectheight);
                    } else {
                        graphic.strokeRect(rectx2, recty, rectwidth, rectheight);
                    }

                    //What if it goes up and to the left?
                } else {
                    double rectwidth = rectx - rectx2;
                    double rectheight = recty - recty2;
                    if (PaintToolBar.check.isSelected() == true) {
                        graphic.strokeRect(rectx2, recty2, rectwidth, rectheight);
                        graphic.fillRect(rectx2, recty2, rectwidth, rectheight);
                    } else {
                        graphic.strokeRect(rectx2, recty2, rectwidth, rectheight);
                    }

                }
                change = true;
            }
            
            //What if the ellipse tool is selected?
            else if(PaintToolBar.Ellipse.isSelected() == true){
                rectx2 = eh.getX();
                recty2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right?
                if (rectx2 > rectx && recty2 > recty) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = recty2 - recty;
                    if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx, recty, rectwidth, rectheight);
                        graphic.fillOval(rectx, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx, recty, rectwidth, rectheight);
                    }
                    
                    //What if it goes up and to the right?
                } else if (rectx2 > rectx && recty > recty2) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = recty - recty2;
                    if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx, recty2, rectwidth, rectheight);
                        graphic.fillOval(rectx, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx, recty2, rectwidth, rectheight);
                    }
                    
                    //What if it goes down and to the left?
                } else if (rectx > rectx2 && recty2 > recty){
                   double rectwidth = rectx - rectx2;
                   double rectheight = recty2 - recty;
                   if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx2, recty, rectwidth, rectheight);
                        graphic.fillOval(rectx2, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx2, recty, rectwidth, rectheight);
                    } 
                   
                   //What if it goes up and to the left?
                } else{
                    double rectwidth = rectx - rectx2;
                    double rectheight = recty - recty2;
                    if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx2, recty2, rectwidth, rectheight);
                        graphic.fillOval(rectx2, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx2, recty2, rectwidth, rectheight);
                    }
                    
                }
                change = true;
            }
            
            //What if the square tool is selected?
            else if(PaintToolBar.Square.isSelected() == true){
                rectx2 = eh.getX();
                recty2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right?
                if (rectx2 > rectx && recty2 > recty) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = rectx2 - rectx;
                    if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeRect(rectx, recty, rectwidth, rectheight);
                        graphic.fillRect(rectx, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeRect(rectx, recty, rectwidth, rectheight);
                    }
                    
                    //What if it goes up and to the right?
                    } else if (rectx2 > rectx && recty > recty2) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = rectx2 - rectx;
                    if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeRect(rectx, recty2, rectwidth, rectheight);
                        graphic.fillRect(rectx, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeRect(rectx, recty2, rectwidth, rectheight);
                    }
                    
                    //What if it goes down and to the left?
                } else if (rectx > rectx2 && recty2 > recty){
                   double rectwidth = rectx - rectx2;
                   double rectheight = rectx - rectx2;
                   if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeRect(rectx2, recty, rectwidth, rectheight);
                        graphic.fillRect(rectx2, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeRect(rectx2, recty, rectwidth, rectheight);
                    } 
                   
                   //What if it goes up and to the left?
                } else{
                    double rectwidth = rectx - rectx2;
                    double rectheight = rectx - rectx2;
                    if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeRect(rectx2, recty2, rectwidth, rectheight);
                        graphic.fillRect(rectx2, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeRect(rectx2, recty2, rectwidth, rectheight);
                    }
                }
                change = true;
            }
            
            //What if the circle tool is selected?
            else if(PaintToolBar.Circle.isSelected() == true){
                rectx2 = eh.getX();
                recty2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                if (rectx2 > rectx && recty2 > recty) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = rectx2 - rectx;
                    
                //What if it goes down and to the right?
                    if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx, recty, rectwidth, rectheight);
                        graphic.fillOval(rectx, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx, recty, rectwidth, rectheight);
                    }
                    
                    //What if it goes up and to the right?
                    } else if (rectx2 > rectx && recty > recty2) {
                    double rectwidth = rectx2 - rectx;
                    double rectheight = rectx2 - rectx;
                    if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx, recty2, rectwidth, rectheight);
                        graphic.fillOval(rectx, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx, recty2, rectwidth, rectheight);
                    }
                    
                    //What if it goes down and to the left?
                } else if (rectx > rectx2 && recty2 > recty){
                   double rectwidth = rectx - rectx2;
                   double rectheight = rectx - rectx2;
                   if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx2, recty, rectwidth, rectheight);
                        graphic.fillOval(rectx2, recty, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx2, recty, rectwidth, rectheight);
                    } 
                   
                   //What if it goes up and to the left?
                } else{
                    double rectwidth = rectx - rectx2;
                    double rectheight = rectx - rectx2;
                    if(PaintToolBar.check.isSelected() == true){
                        graphic.strokeOval(rectx2, recty2, rectwidth, rectheight);
                        graphic.fillOval(rectx2, recty2, rectwidth, rectheight);
                    }
                    else{
                        graphic.strokeOval(rectx2, recty2, rectwidth, rectheight);
                    }
                }
                change = true;
            }
        });
        
        return canvas;
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
