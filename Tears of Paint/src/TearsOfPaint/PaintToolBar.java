
package TearsOfPaint;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


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
    protected ToggleButton Rounded;
    protected ToggleButton Eraser;
    protected ToggleButton Polygon;
    protected ToggleButton MovePaste;
    protected TextField polytext;
    protected HBox filllayout;
    protected HBox strokelayout;
    protected VBox colorLayout;
    protected HBox draw1;
    protected HBox draw2;
    protected HBox draw3;
    protected VBox drawLayout;
    private Label fill;
    private Label stroke;
    
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
        Polygon = new ToggleButton("Polygon Tool");
        Rounded = new ToggleButton("Rounded Rectangle Tool");
        Eraser = new ToggleButton("Eraser Tool");
        MovePaste = new ToggleButton("Select and Move");
        PencilTool.setText("Pencil Tool");
        LineDrawer.setText("Line Drawer");
        Rectangle.setText("Rectangle Tool");
        Ellipse.setText("Ellipse Tool");
        Square.setText("Square Tool");
        Circle.setText("Circle Tool");
        Text.setText("Text Tool");
        check = new CheckBox("Fill");
        colorGrabber = new ToggleButton("Color Grabber");
        textField = new TextField("Enter text here!");
        polytext = new TextField("5");
        
        //Creation of the color pickers
        colorPicker = new ColorPicker();
        c22 = new ColorPicker();
        
        //Making everything a little bit more clean
        fill = new Label(" Fill Color");
        stroke = new Label(" Stroke Color");
        strokelayout = new HBox(colorPicker, stroke);
        filllayout = new HBox(c22, fill);
        colorLayout = new VBox(strokelayout, filllayout);
        draw1 = new HBox(LineDrawer, PencilTool, Rectangle);
        draw2 = new HBox(Ellipse, Square, Circle);
        draw3 = new HBox(Text, Rounded, Polygon);
        drawLayout = new VBox(draw1, draw2, draw3);
        
        //Creation of our pencil tool width controller
        slider1 = new Slider(1, 25, 1);
        
        //Setting up the tool bar, makes everything look cleaner  
        tool.getItems().addAll(colorLayout, drawLayout, slider1, check, colorGrabber, textField,
                Eraser, polytext, MovePaste);
    }
    
}
