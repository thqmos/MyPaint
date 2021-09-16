
package mypaint;


//Lots of imports necessary for Paint
import java.awt.image.RenderedImage;
import java.io.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import java.util.Scanner;
import java.util.logging.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Modality;
import javafx.scene.text.*;
import javafx.scene.canvas.*;
import javax.imageio.ImageIO;
//import javafx.scene.input.MouseEvent;
//import javafx.event.EventHandler;

public class MyPaint extends Application{
    
    //These assist us in drawing lines
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    
    //This assists us in saving (regular save) an image
    private File saveFile;
    
    public void start(Stage frame) throws Exception{
        
        saveFile = null;

        //Creation of the menu bar
        MenuBar menuBar = new MenuBar();
        
        //Creation and addition of the menus
        Menu File = new Menu("File");
        Menu Home = new Menu("Home");
        Menu View = new Menu("View");
        Menu Help = new Menu("Help");
        menuBar.getMenus().add(File);
        menuBar.getMenus().add(Home);
        menuBar.getMenus().add(View);
        menuBar.getMenus().add(Help);
        
        //Creation and addition of the menu items
        MenuItem selectImage = new MenuItem("Select Image");
        MenuItem CloseWS = new MenuItem("Close Without Saving");
        MenuItem ReleaseNotes = new MenuItem("Release Notes");
        MenuItem PencilTool = new MenuItem("Pencil Tool");
        MenuItem LineDrawer = new MenuItem("Line Drawer");
        MenuItem Save = new MenuItem("Save");
        MenuItem SaveAs = new MenuItem("Save as");
        MenuItem About = new MenuItem("About");
        MenuItem HelpM = new MenuItem("Help");
        File.getItems().add(selectImage);
        File.getItems().add(Save);
        File.getItems().add(SaveAs);
        File.getItems().add(CloseWS);
        Help.getItems().add(HelpM);
        Help.getItems().add(About);
        Help.getItems().add(ReleaseNotes);
        Home.getItems().add(PencilTool);
        Home.getItems().add(LineDrawer);
        
        //Creation and implementation of a file chooser, which will choose the image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png")
                ,new FileChooser.ExtensionFilter("Image Files", "*.jpeg")
                ,new FileChooser.ExtensionFilter("Image Files", "*.gif")
                ,new FileChooser.ExtensionFilter("Image Files", "*.bmp")
                ,new FileChooser.ExtensionFilter("Image Files", "*.jpg")
        );
        
        //Creation of the color picker
        ColorPicker colorPicker = new ColorPicker();
        
        //Creation of our pencil tool width controller
        Slider slider1 = new Slider(1, 25, 1);
        
        //Setting up the tool bar, makes everything look cleaner
        ToolBar tool = new ToolBar();
        tool.getItems().addAll(colorPicker, slider1);
        
        //Setting up the stage to include various things
        frame.setTitle("My Paint");
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext graphic = canvas.getGraphicsContext2D();
        VBox layout = new VBox(menuBar, tool);
        layout.getChildren().add(canvas);
        ScrollPane actualLayout = new ScrollPane(layout);
        Scene scene = new Scene(actualLayout, 500, 300);
        frame.setScene(scene);
        frame.show();
        
        selectImage.setOnAction(actionEvent -> {
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
        });
        
        //This is how the line drawer works
        LineDrawer.setOnAction(eh -> {
            canvas.setOnMousePressed(event -> {
                x1 = event.getX();
                y1 = event.getY();
            });
            canvas.setOnMouseReleased(event -> {
                x2 = event.getX();
                y2 = event.getY();
                initDraw(graphic, slider1, colorPicker);
                graphic.strokeLine(x1, y1, x2, y2);
            });  
        });

        //This is how the pencil tool works
        /*
        PencilTool.setOnAction(actione -> {
            initDraw(graphic, slider1, colorPicker);

            //What if the mouse is being pressed?
            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
                new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                graphic.beginPath();
                graphic.moveTo(event.getX(), event.getY());
                graphic.stroke();
            }
        });

            //What if the mouse is being dragged?
            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
                new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                graphic.lineTo(event.getX(), event.getY());
                graphic.stroke();
            }
        });

            //What if the mouse is released?
            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
                new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {

            } 
        });          
    });
*/   

        //This is how the save menu item works
        Save.setOnAction(eh ->{

        //Tests if the file has been saved at all or not
        if (saveFile == null){

            //Show save file dialog
                File file = fileChooser.showSaveDialog(frame);

                //Tests to see if it can even save, if it can, move forward
                if(file != null){
                try {
                    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                    }
                    catch (IOException ex) {
                        Logger.getLogger(MyPaint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    saveFile = file;
                }
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
                    }
                        catch (IOException exp){
                            Logger.getLogger(MyPaint.class.getName()).log(Level.SEVERE, null, exp);
                    }
                }
            }
        });

        //This is how the save as menu item works
        SaveAs.setOnAction(aevent ->{

            //Show save file dialog
            File file = fileChooser.showSaveDialog(frame);

            //Tests to see if it can even save, if it can, move forward
            if(file != null){
                try {
                    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                }
                catch (IOException ex) {
                    Logger.getLogger(MyPaint.class.getName()).log(Level.SEVERE, null, ex);
                }
                saveFile = file;
            }
        });
        
        //This is how the release notes become viewable
        ReleaseNotes.setOnAction(actionEvent -> {
            
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
        About.setOnAction(actionEvent -> {
            
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
        HelpM.setOnAction(actionEvent -> {
            
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
        CloseWS.setOnAction(actionEvent -> {
            
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
        });
    }
    
    //This is the method called which assists in the creation of the pencil tool/line drawer
    private void initDraw(GraphicsContext gc, Slider s, ColorPicker c){
         gc.setLineWidth(s.getValue());
         gc.setStroke(c.getValue());
         gc.setFill(c.getValue());
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}
