
package tears.of.paint.v2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Integer.parseInt;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class PaintToolBar {
    //This helps us with consolidation of the main file
    //This is the creation of everything needed for the tool bar
    protected ToolBar tool;
    protected static ToggleButton PencilTool;
    protected static ToggleButton LineDrawer;
    protected static ToggleButton Rectangle;
    protected static ToggleButton Ellipse;
    protected static ToggleButton Square;
    protected static ToggleButton Circle;
    protected static ToggleButton Text;
    protected static CheckBox check;
    protected static ToggleButton colorGrabber;
    protected static ColorPicker colorPicker;
    protected static ColorPicker c22;
    protected static Slider slider1;
    protected static TextField textField;
    protected static ToggleButton Rounded;
    protected static ToggleButton Eraser;
    protected static ToggleButton Polygon;
    protected static ToggleButton Cut;
    protected static TextField polytext;
    protected static ToggleButton Copy;
    protected static ToggleButton Paste;
    
    protected HBox filllayout;
    protected HBox strokelayout;
    protected VBox colorLayout;
    protected HBox draw1;
    protected HBox draw2;
    protected HBox draw3;
    protected VBox drawLayout;
    protected VBox sliderlayout;
    protected VBox textlayout1;
    protected VBox textlayout2;
    protected static VBox textlayout3;
    protected VBox misclayout;
    protected VBox tools;
    
    private Label fill;
    private Label stroke;
    private Label slider;
    private Label polynosides;
    private Label drawt;
    private Label misc;
    protected static TextField autotext;
    private Button changeauto;
    private String oldString;
    protected static CheckBox check1;
    protected static Label update;
    
    protected static Label selectedTool;
    
    /**
     * THE constructor for the PaintToolBar.
     */
    public PaintToolBar(){
        
        //Creation of the tool bar
        tool = new ToolBar();
        
        //Creation of the art tool buttons/text fields
        PencilTool = new ToggleButton();
        LineDrawer = new ToggleButton();
        Rectangle = new ToggleButton();
        Ellipse = new ToggleButton();
        Square = new ToggleButton();
        Circle = new ToggleButton();
        Text = new ToggleButton();
        Polygon = new ToggleButton();
        Rounded = new ToggleButton();
        Eraser = new ToggleButton();
        Cut = new ToggleButton();
        Copy = new ToggleButton();
        Paste = new ToggleButton();
        check = new CheckBox("Fill");
        colorGrabber = new ToggleButton();
        textField = new TextField("Enter text here!");
        polytext = new TextField("5");
        changeauto = new Button();
        check1 = new CheckBox("Enable Countdown View");
        oldString = "";
        selectedTool = new Label("Selected Tool: None");
        
        //Autosave stuff
        File f = new File("Resources\\PaintAutoSaveTime.txt");
        Scanner texts = null;
        Text ts = new Text();
        try{
            texts = new Scanner(f);
            String s = texts.nextLine();
            oldString = s;
            autotext = new TextField(s);
        }
        catch(Exception e){
            System.out.println("Something went wrong...");
        }
        String newString = new String();
        changeauto.setOnAction(eh -> {
            modifyFile("Resources\\PaintAutoSaveTime.txt", oldString, autotext.getText());
        });
        update = new Label("Autosave activates in " + parseInt(PaintToolBar.autotext.getText()) + " seconds!");
        check1.setSelected(true);
        if(check1.isSelected() == false){
            update.setVisible(false);
        }
        else{
            update.setVisible(true);
        }
        
        //Adding icons to all of the tools
        //Rectangle
        String imageFilePath = "Resources\\rectangle.png";
        addIcon(Rectangle, imageFilePath);
        
        //Rounded rectangle
        String imageFRR = "Resources\\rectangle_blue.png";
        addIcon(Rounded, imageFRR);
        
        //Line drawer
        String imageL = "Resources\\487144-200.png";
        addIcon(LineDrawer, imageL);
        
        //Pencil tool
        String imageP = "Resources\\pencil.png";
        addIcon(PencilTool, imageP);
        
        //Ellipse tool
        String imageE = "Resources\\12975-200.png";
        addIcon(Ellipse, imageE);
        
        //Square tool
        String imageS = "Resources\\images.png";
        addIcon(Square, imageS);
        
        //Text tool
        String imageT = "Resources\\66759-200.png";
        addIcon(Text, imageT);
        
        //Color Grabber
        String imageG = "Resources\\79715_color_picker_icon.png";
        addIcon(colorGrabber, imageG);
        
        //Circle tool
        String imageC = "Resources\\12971-200.png";
        addIcon(Circle, imageC);
        
        //Polygon tool
        String imagen = "Resources\\polygon-icon-21.png";
        addIcon(Polygon, imagen);
        
        //Eraser tool
        String imagee = "Resources\\img_18444.png";
        addIcon(Eraser, imagee);
        
        //Cut
        String images = "Resources\\cut.png";
        addIcon(Cut, images);
        
        //Copy
        String copyi = "Resources\\178921.png";
        addIcon(Copy, copyi);
        
        //Paste
        String cop = "Resources\\clipboard_paste.png";
        addIcon(Paste, cop);
        
        //Change autosave time
        String imag = "Resources\\timer_icon_153935.png";
        try{
            InputStream is = new FileInputStream(imag);
            Image image5 = new Image(is, 25, 25, false, false);
            changeauto.setGraphic(new ImageView(image5));
        }
        catch (FileNotFoundException fail){
            System.out.println("Failed to add icon");
        }
        
        //Adding tooltips
        //Line drawer tooltip
        Tooltip tooltip1 = new Tooltip("Line Tool!");
        tooltip1.setTextAlignment(TextAlignment.LEFT);
        LineDrawer.setTooltip(tooltip1);
        
        //Pencil tool tooltip
        Tooltip tooltip2 = new Tooltip("Pencil Tool!");
        tooltip2.setTextAlignment(TextAlignment.LEFT);
        PencilTool.setTooltip(tooltip2);
        
        //Rectangle tool tooltip
        Tooltip tooltip3 = new Tooltip("Rectangle Tool!");
        tooltip3.setTextAlignment(TextAlignment.LEFT);
        Rectangle.setTooltip(tooltip3);
        
        //Ellipse tool tooltip
        Tooltip tooltip4 = new Tooltip("Ellipse Tool!");
        tooltip4.setTextAlignment(TextAlignment.LEFT);
        Ellipse.setTooltip(tooltip4);
        
        //Square tool tooltip
        Tooltip tooltip5 = new Tooltip("Square Tool!");
        tooltip5.setTextAlignment(TextAlignment.LEFT);
        Square.setTooltip(tooltip5);
        
        //Circle tool tooltip
        Tooltip tooltip6 = new Tooltip("Circle Tool!");
        tooltip6.setTextAlignment(TextAlignment.LEFT);
        Circle.setTooltip(tooltip6);
        
        //Polygon tool tooltip
        Tooltip tooltip7 = new Tooltip("Polygon Tool!");
        tooltip7.setTextAlignment(TextAlignment.LEFT);
        Polygon.setTooltip(tooltip7);
        
        //Rounded Rectangle tool tooltip
        Tooltip tooltip8 = new Tooltip("Rounded Rectangle Tool!");
        tooltip8.setTextAlignment(TextAlignment.LEFT);
        Rounded.setTooltip(tooltip8);
        
        //Color Grabber tooltip
        Tooltip tooltip9 = new Tooltip("Color Grabber!");
        tooltip9.setTextAlignment(TextAlignment.LEFT);
        colorGrabber.setTooltip(tooltip9);
        
        //Cut tooltip
        Tooltip tooltip10 = new Tooltip("Cut!");
        tooltip10.setTextAlignment(TextAlignment.LEFT);
        Cut.setTooltip(tooltip10);
        
        //Eraser tool tooltip
        Tooltip tooltip11 = new Tooltip("Eraser Tool!");
        tooltip11.setTextAlignment(TextAlignment.LEFT);
        Eraser.setTooltip(tooltip11);
        
        //Eraser tool tooltip
        Tooltip tooltip12 = new Tooltip("Text Tool!");
        tooltip12.setTextAlignment(TextAlignment.LEFT);
        Text.setTooltip(tooltip12);
        
        //Change autosave button tooltip
        Tooltip tooltip13 = new Tooltip("Change time!");
        tooltip13.setTextAlignment(TextAlignment.LEFT);
        changeauto.setTooltip(tooltip13);
        
        //Copy tooltip
        Tooltip tooltip14 = new Tooltip("Copy!");
        tooltip14.setTextAlignment(TextAlignment.LEFT);
        Copy.setTooltip(tooltip14);
        
        //Paste tooltip
        Tooltip tooltip15 = new Tooltip("Paste!");
        tooltip15.setTextAlignment(TextAlignment.LEFT);
        Paste.setTooltip(tooltip15);
        
        //Creation of the color pickers
        colorPicker = new ColorPicker();
        c22 = new ColorPicker();
        
        //Making everything a little bit more clean
        fill = new Label(" Fill Color");
        stroke = new Label(" Stroke Color");
        polynosides = new Label("No. of sides for your polygon!");
        slider = new Label("Width Controller");
        strokelayout = new HBox(colorPicker, stroke);
        filllayout = new HBox(c22, fill);
        colorLayout = new VBox(strokelayout, filllayout, selectedTool);
        colorLayout.setSpacing(6);
        
        draw1 = new HBox(LineDrawer, PencilTool, Rectangle, Ellipse, Square);
        draw2 = new HBox(Circle, Text, Rounded, Polygon, Eraser);
        drawt = new Label("Drawing Tools");
        tools = new VBox(draw1, draw2);
        drawLayout = new VBox(drawt, tools);
        drawLayout.setSpacing(13);
        drawLayout.setAlignment(Pos.BASELINE_CENTER);
        
        draw3 = new HBox(colorGrabber, Cut, Copy, Paste, changeauto);
        misc = new Label("Miscellaneuous Tools");
        misclayout = new VBox(misc, draw3);
        misclayout.setSpacing(13);
        misclayout.setAlignment(Pos.BASELINE_CENTER);
        
        textlayout1 = new VBox(polynosides, polytext);
        textlayout1.setSpacing(6);
        textlayout1.setAlignment(Pos.BASELINE_CENTER);
        
        textlayout3 = new VBox(update, autotext);
        textlayout3.setSpacing(6);
        textlayout3.setAlignment(Pos.BASELINE_CENTER);
        
        //Creation of our pencil tool width controller
        slider1 = new Slider(1, 25, 1);
        sliderlayout = new VBox(slider, slider1);
        sliderlayout.setSpacing(23.5);
        sliderlayout.setAlignment(Pos.BASELINE_CENTER);
        
        //Setting up the tool bar, makes everything look cleaner  
        tool.getItems().addAll(colorLayout, check, drawLayout, sliderlayout, textField, misclayout,
                textlayout1, textlayout3, check1);
        
        //What if the line drawer is clicked?
        LineDrawer.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Line Drawer");
            logTool(" Line Drawer is selected.");
            if(LineDrawer.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the pencil tool is clicked?
        PencilTool.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Pencil Tool");
            logTool(" Pencil Tool is selected.");
            if(PencilTool.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the rectangle tool is clicked?
        Rectangle.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Rectangle Tool");
            logTool(" Rectangle Tool is selected.");
            if(Rectangle.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the ellipse tool is clicked?
        Ellipse.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Ellipse Tool");
            logTool(" Ellipse Tool is selected.");
            if(Ellipse.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the square tool is clicked?
        Square.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Square Tool");
            logTool(" Square Tool is selected.");
            if(Square.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the circle tool is clicked?
        Circle.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Circle Tool");
            logTool(" Circle Tool is selected.");
            if(Circle.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the rounded rectangle tool is clicked?
        Rounded.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Rounded Rectangle Tool");
            logTool(" Rounded Rectangle Tool is selected.");
            if(Rounded.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the eraser tool is clicked?
        Eraser.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Eraser Tool");
            logTool(" Eraser Tool is selected.");
            if(Eraser.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the polygon tool is clicked?
        Polygon.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Polygon Tool");
            logTool(" Polygon Tool is selected.");
            if(Polygon.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the cut tool is clicked?
        Cut.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Cut Tool");
            logTool(" Cut Tool is selected.");
            if(Cut.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the copy tool is clicked?
        Copy.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Copy Tool");
            logTool(" Copy Tool is selected.");
            if(Copy.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the paste tool is clicked?
        Paste.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Paste Tool");
            logTool(" Paste Tool is selected.");
            if(Paste.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the color grabber tool is clicked?
        colorGrabber.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Color Grabber");
            logTool(" Color Grabber is selected.");
            if(colorGrabber.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the text tool is clicked?
        Text.setOnAction(eh -> {
            selectedTool.setText("Selected Tool: Text Tool");
            logTool(" Text Tool is selected.");
            if(Text.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the change autosave timer button gets clicked?
        changeauto.setOnAction(eh -> {
            logTool(" Autosave time was changed.");
        });
    }
    
    /**
     * This method adds icons to the toggle buttons
     * @param t, this is the toggle button which the icon will be added to
     * @param path, this represents the file path from which the icon will be taken
     * @see the icons associated with the correct toggle buttons
     */
    public void addIcon(ToggleButton t, String path){
        try{
            InputStream is = new FileInputStream(path);
            Image image = new Image(is, 25, 25, false, false);
            t.setGraphic(new ImageView(image));
        }
        catch (FileNotFoundException fail){
            System.out.println("Failed to add icon");
        }
    }
    
    /**
     * This method deletes the contents of a text file and replaces them with another
     * @param filePath, this is the file path of the text file
     * @param oldString, this is the string which will be removed from the file
     * @param newString, this is the string which will be added from the file
     */
    static void modifyFile(String filePath, String oldString, String newString) {
        File fileToBeModified = new File(filePath);
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent
            String line = reader.readLine();

            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Replacing oldString with newString in the oldContent
            String newContent = oldContent.replaceAll(oldString, newString);

            //Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources

                reader.close();

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * This is the method which logs the actions being done
     * @param tool, this is the string that describes what is being used/clicked.
     */
    public void logTool(String tool){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        formatter.format(date);
        File fileToBeModified = new File("Resources\\MyPaintLogging.log");
        FileHandler fh;
        try {
            fh = new FileHandler("Resources\\MyPaintLogging.log");
            PaintCanvas.log.addHandler(fh);
            SimpleFormatter sf = new SimpleFormatter();  
            fh.setFormatter(sf);
            if (PaintCanvas.currentTool != tool) {
                PaintCanvas.currentTool = tool;
                PaintCanvas.log.info(date + ":" + PaintCanvas.currentTool);
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
}
