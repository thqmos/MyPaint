
package mypaint;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;


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
    
    public PaintToolBar(){
        
        //Creation of the tool bar
        tool = new ToolBar();
        
        //Creation of the pencil tool/line drawer menu buttons
        
        PencilTool = new ToggleButton();
        LineDrawer = new ToggleButton();
        Rectangle = new ToggleButton();
        Ellipse = new ToggleButton();
        Square = new ToggleButton();
        Circle = new ToggleButton();
        Text = new ToggleButton();
        
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
        
        //Creation of the color pickers
        colorPicker = new ColorPicker();
        c22 = new ColorPicker();
        
        //Creation of our pencil tool width controller
        slider1 = new Slider(1, 25, 1);
        
        //Setting up the tool bar, makes everything look cleaner  
        tool.getItems().addAll(colorPicker, slider1, PencilTool, LineDrawer, Rectangle,
                Ellipse, Square, Circle, c22, check, colorGrabber, Text, textField);
    }
    
}
