
package tears.of.paint.v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
    
    private HBox filllayout;
    private HBox strokelayout;
    private VBox colorLayout;
    private HBox draw1;
    private HBox draw2;
    private HBox draw3;
    private VBox drawLayout;
    private VBox sliderLayout;
    private VBox PolyTextLayout;
    protected static VBox AutoTextLayout;
    private VBox misclayout;
    private VBox tools;
    
    private Label fill;
    private Label stroke;
    private Label slider;
    private Label polynosides;
    private Label drawt;
    private Label misc;
    protected static ChoiceBox <Integer>changeauto;
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
        changeauto = new ChoiceBox<Integer>();
        changeauto.getItems().addAll(30, 60, 90, 120, 300, 600, 900, 1200, 1500, 1800);
        check1 = new CheckBox("Enable Countdown View");
        selectedTool = new Label("Selected Tool: None");
        
        //Setting up autosave stuff
        update = new Label("Autosave activates in " + PaintToolBar.changeauto.getValue() + " seconds!");
        check1.setSelected(true);
        if(check1.isSelected() == false){
            update.setVisible(false);
        }
        else{
            update.setVisible(true);
        }
        
        //Adding icons to all of the tools
        //Rectangle
        String imageFilePath = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\rectangle.png";
        addIcon(Rectangle, imageFilePath);
        
        //Rounded rectangle
        String imageFRR = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\rectangle_blue.png";
        addIcon(Rounded, imageFRR);
        
        //Line drawer
        String imageL = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\487144-200.png";
        addIcon(LineDrawer, imageL);
        
        //Pencil tool
        String imageP = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\pencil.png";
        addIcon(PencilTool, imageP);
        
        //Ellipse tool
        String imageE = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\12975-200.png";
        addIcon(Ellipse, imageE);
        
        //Square tool
        String imageS = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\images.png";
        addIcon(Square, imageS);
        
        //Text tool
        String imageT = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\66759-200.png";
        addIcon(Text, imageT);
        
        //Color Grabber
        String imageG = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\79715_color_picker_icon.png";
        addIcon(colorGrabber, imageG);
        
        //Circle tool
        String imageC = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\12971-200.png";
        addIcon(Circle, imageC);
        
        //Polygon tool
        String imagen = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\polygon-icon-21.png";
        addIcon(Polygon, imagen);
        
        //Eraser tool
        String imagee = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\img_18444.png";
        addIcon(Eraser, imagee);
        
        //Cut
        String images = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\cut.png";
        addIcon(Cut, images);
        
        //Copy
        String copyi = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\178921.png";
        addIcon(Copy, copyi);
        
        //Paste
        String cop = "C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\clipboard_paste.png";
        addIcon(Paste, cop);
        
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
        
        //Working with the combo box because text files and autosaving are really buggy at the moment
        changeauto.setValue(30);
        
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
        
        draw3 = new HBox(colorGrabber, Cut, Copy, Paste);
        misc = new Label("Miscellaneuous Tools");
        misclayout = new VBox(misc, draw3);
        misclayout.setSpacing(13);
        misclayout.setAlignment(Pos.BASELINE_CENTER);
        
        PolyTextLayout = new VBox(polynosides, polytext);
        PolyTextLayout.setSpacing(6);
        PolyTextLayout.setAlignment(Pos.BASELINE_CENTER);
        
        AutoTextLayout = new VBox(update, changeauto);
        AutoTextLayout.setSpacing(6);
        AutoTextLayout.setAlignment(Pos.BASELINE_CENTER);
        
        //Creation of our pencil tool width controller
        slider1 = new Slider(1, 25, 1);
        sliderLayout = new VBox(slider, slider1);
        sliderLayout.setSpacing(23.5);
        sliderLayout.setAlignment(Pos.BASELINE_CENTER);
        
        //Setting up the tool bar, makes everything look cleaner  
        tool.getItems().addAll(colorLayout, check, drawLayout, sliderLayout, textField, misclayout,
                PolyTextLayout, AutoTextLayout, check1);
        
        //What if the line drawer is clicked?
        LineDrawer.setOnAction(eh -> {
            
            //Update the selected tool label
            selectedTool.setText("Selected Tool: Line Drawer");
            
            //Log the tool that is selected.
            logTool(" Line Drawer is selected.");
            
            //If the tool is no longer selected, make sure that the label displays that
            if(LineDrawer.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the pencil tool is clicked?
        PencilTool.setOnAction(eh -> {
            
            //Update the selected tool label
            selectedTool.setText("Selected Tool: Pencil Tool");
            
            //Log the tool that is selected.
            logTool(" Pencil Tool is selected.");
            
            //If the tool is no longer selected, make sure that the label displays that
            if(PencilTool.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the rectangle tool is clicked?
        Rectangle.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Rectangle Tool");
            logTool(" Rectangle Tool is selected.");
            if(Rectangle.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the ellipse tool is clicked?
        Ellipse.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Ellipse Tool");
            logTool(" Ellipse Tool is selected.");
            if(Ellipse.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the square tool is clicked?
        Square.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Square Tool");
            logTool(" Square Tool is selected.");
            if(Square.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the circle tool is clicked?
        Circle.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Circle Tool");
            logTool(" Circle Tool is selected.");
            if(Circle.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the rounded rectangle tool is clicked?
        Rounded.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Rounded Rectangle Tool");
            logTool(" Rounded Rectangle Tool is selected.");
            if(Rounded.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the eraser tool is clicked?
        Eraser.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Eraser Tool");
            logTool(" Eraser Tool is selected.");
            if(Eraser.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the polygon tool is clicked?
        Polygon.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Polygon Tool");
            logTool(" Polygon Tool is selected.");
            if(Polygon.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the cut tool is clicked?
        Cut.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Cut Tool");
            logTool(" Cut Tool is selected.");
            if(Cut.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the copy tool is clicked?
        Copy.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Copy Tool");
            logTool(" Copy Tool is selected.");
            if(Copy.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the paste tool is clicked?
        Paste.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Paste Tool");
            logTool(" Paste Tool is selected.");
            if(Paste.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the color grabber tool is clicked?
        colorGrabber.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Color Grabber");
            logTool(" Color Grabber is selected.");
            if(colorGrabber.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the text tool is clicked?
        Text.setOnAction(eh -> {
            
            //Does a similar thing that the Line Drawer and Pencil Tool do.
            selectedTool.setText("Selected Tool: Text Tool");
            logTool(" Text Tool is selected.");
            if(Text.isSelected() == false){
                selectedTool.setText("Selected Tool: None");
            }
        });
        
        //What if the change autosave timer combo box gets clicked?
        changeauto.setOnAction(eh -> {
            
            //Log the button press
            logTool(" Autosave time was changed.");
        });
    }
    
    /**
     * This method adds icons to the toggle buttons
     * @param t, this is the toggle button which the icon will be added to
     * @param path, this represents the file path from which the icon will be taken
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
     * This is the method which logs the tools being selected.
     * @param tool This is the string that describes what is being used/clicked.
     */
    public void logTool(String tool){
        
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
            PaintCanvas.log.addHandler(fh);
            SimpleFormatter sf = new SimpleFormatter();  
            fh.setFormatter(sf);
            
            //If the currentTool is equal to tool, don't log it, otherwise, log the tool
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
