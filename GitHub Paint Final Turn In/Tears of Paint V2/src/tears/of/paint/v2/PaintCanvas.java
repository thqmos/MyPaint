
package tears.of.paint.v2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;


public class PaintCanvas extends Canvas{
    
    static GraphicsContext graphic;
    private Stack<Image> undoHistory;
    private Stack<Image> redoHistory;
    private WritableImage prevImage;
    protected static double x1;
    protected static double y1;
    private double x2;
    private double y2;
    //This assists us in zooming in/out
    protected static double zoom;
    private Scale scale;
    private Image nextSelect;
    
    //Logging stuff
    protected static String currentTool;
    protected static Logger log;
    protected int time;
    
    //Rainbow fun mode
    protected PixelWriter pw;
    
    /**
     * THE constructor for the PaintCanvas.
     */
    public PaintCanvas(){
        super();
        graphic = this.getGraphicsContext2D();
        undoHistory = new Stack<>();
        redoHistory = new Stack<>();
        
        this.setWidth(1920);
        this.setHeight(1080);
        graphic.setFill(Color.WHITE);
        graphic.strokeRect(0, 0, this.getWidth(), this.getHeight());
        graphic.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        zoom = 1;
        scale = null;
        nextSelect = null;
        log = Logger.getLogger(this.getClass().getName());
        pw = graphic.getPixelWriter();
        
        
        
        //What if the mouse is clicked?
        this.setOnMouseClicked(eh -> {
            
            //This is how the color grabber works
            if (PaintToolBar.colorGrabber.isSelected() == true) {
                WritableImage writableImage = new WritableImage((int) this.getWidth(), (int) this.getHeight());
                this.snapshot(null, writableImage);
                PixelReader pr = writableImage.getPixelReader();
                PaintToolBar.colorPicker.setValue(pr.getColor((int) eh.getX(), (int) eh.getY()));
                PaintToolBar.colorGrabber.setSelected(false);
            }
            //What if the paste tool is selected?
            else if (PaintToolBar.Paste.isSelected()){
                
                //Paste the selected image, either from copy or cut
                graphic.drawImage(nextSelect, eh.getX(), eh.getY());
                addUndo(undoHistory, this);
            }
            
        });
        
        this.setOnMousePressed(eh -> {
           //What if the line drawer is selected?
            if(PaintToolBar.LineDrawer.isSelected() == true){
                
                //Get the event coordinates
                x1 = eh.getX();
                y1 = eh.getY();
                
                //We need to be able to undo/redo lines, this helps with that
                addUndo(undoHistory, this);
            }
            
            //What if the pencil tool is selected
            else if(PaintToolBar.PencilTool.isSelected() == true){
                
                //Changing color, line width
                initDraw(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker); 
                
                //Starting the freehand draw
                graphic.beginPath();
                graphic.moveTo(eh.getX(), eh.getY());
                graphic.stroke();
                
                //We need to be able to undo/redo doodles, this helps with that
                addUndo(undoHistory, this);
            }
            
            //What if the rectangle tool is selected
            else if(PaintToolBar.Rectangle.isSelected() == true){
                
                //Get the event coordinates
                x1 = eh.getX();
                y1 = eh.getY();
                
                //We need to be able to undo/redo rectangles, this helps with that
                addUndo(undoHistory, this);
            }
            
            //What if the ellipse tool is selected
            else if(PaintToolBar.Ellipse.isSelected() == true){
                
                //Similar to that which the line tool does
                x1 = eh.getX();
                y1 = eh.getY();
                addUndo(undoHistory, this);
            }
            
            //What if the circle tool is selected
            else if (PaintToolBar.Circle.isSelected() == true) {
                
                //Similar to that which the line tool does
                x1 = eh.getX();
                y1 = eh.getY();
                addUndo(undoHistory, this);
                
            } //What if the square tool is selected
            else if (PaintToolBar.Square.isSelected() == true) {
                
                //Similar to that which the line tool does
                x1 = eh.getX();
                y1 = eh.getY();
                addUndo(undoHistory, this);
                
            } //What if the text tool is selected
            else if (PaintToolBar.Text.isSelected() == true) {
                
                //Setting the color and font size of the text
                graphic.setLineWidth(1);
                graphic.setFont(Font.font(PaintToolBar.slider1.getValue()));
                graphic.setStroke(PaintToolBar.colorPicker.getValue());
                graphic.setFill(PaintToolBar.colorPicker.getValue());
                drawText(graphic, eh.getX(), eh.getY());
                
                //Similar to that which the line tool does
                addUndo(undoHistory, this);    
            } //What if the rounded rectangle tool is selected
            else if (PaintToolBar.Rounded.isSelected() == true) {
                x1 = eh.getX();
                y1 = eh.getY();
                addUndo(undoHistory, this);
            }
            //What if the eraser tool is selected
            else if(PaintToolBar.Eraser.isSelected() == true){
                
                //Similar to that which the line tool does
                x1 = eh.getX();
                y1 = eh.getY();
                addUndo(undoHistory, this);
            }
            //What if the Polygon tool is selected?
            else if (PaintToolBar.Polygon.isSelected() == true) {
                
                //Similar to that which the line tool does
                x1 = eh.getX();
                y1 = eh.getY();
                addUndo(undoHistory, this);
            }
            //What if the cut tool is selected?
            else if (PaintToolBar.Cut.isSelected() == true){
                
                //Similar to that which the line tool does
                x1 = eh.getX();
                y1 = eh.getY();
                addUndo(undoHistory, this);
            }
            //What if the copy tool is selected?
            else if (PaintToolBar.Copy.isSelected() == true){
                
                //Similar to that which the line tool does
                x1 = eh.getX();
                y1 = eh.getY();
                addUndo(undoHistory, this);
            }
            
        });
        
        //What if the mouse is dragged?
        this.setOnMouseDragged(eh -> {
            
            //What if the line drawer is selected?
            if (PaintToolBar.LineDrawer.isSelected()){
                
                //Peek off of the undoHistory stack, makes live draw possible
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                
                //Getting the x and y of the event and drawing the line
                x2 = eh.getX();
                y2 = eh.getY();
                initDraw(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker);
                graphic.strokeLine(x1, y1, x2, y2);
            }
            
            //What if the pencil tool is selected?
            else if (PaintToolBar.PencilTool.isSelected() == true) {
                
                //Continuation of the freehand draw
                initDraw(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker);
                graphic.lineTo(eh.getX(), eh.getY());
                graphic.stroke();
            }
            
            //What if the rectangle tool is selected
            else if (PaintToolBar.Rectangle.isSelected()){
                
                //Peek off of the undoHistory stack, makes live draw possible
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                
                //Getting the x and y of the event and drawing the rectangle
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Calculate width and height as normal, then draw accordingly
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    drawRect(x1, y1, rectwidth, rectheight);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Treat the starting point of the rectangle as x1 and y2, then calculate width and height
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    
                    //Draw accordingly
                    drawRect(x1, y2, rectwidth, rectheight);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Treat the starting point of the rectangle as x2 and y1, then calculate width and height
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    
                    //Draw accordingly
                    drawRect(x2, y1, rectwidth, rectheight);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Treat the starting point of the rectangle as x2 and y2, then calculate width and height
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    
                    //Draw accordingly
                    drawRect(x2, y2, rectwidth, rectheight);
                }
            }
            
            //What if the ellipse tool is selected?
            else if (PaintToolBar.Ellipse.isSelected()){
                
                //Peek off of the undoHistory stack, makes live draw possible
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                
                //Getting the x and y of the event and drawing the ellipse
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except with ellipses
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    drawe(x1, y1, rectwidth, rectheight);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the rectangle tool, except with ellipses
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    drawe(x1, y2, rectwidth, rectheight);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the rectangle tool, except with ellipses
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    drawe(x2, y1, rectwidth, rectheight);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Similar to the rectangle tool, except with ellipses
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    drawe(x2, y2, rectwidth, rectheight);
                }
            }
            
            //What if the square tool is selected?
            else if(PaintToolBar.Square.isSelected() == true){
                
                //Similar to the rectangle and ellipse tools, different shape
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except width and height are the same
                    double rectwidth = x2 - x1;
                    drawRect(x1, y1, rectwidth, rectwidth);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the rectangle tool, except width and height are the same
                    double rectwidth = x2 - x1;
                    drawRect(x1, y2, rectwidth, rectwidth);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the rectangle tool, except width and height are the same
                    double rectwidth = x1 - x2;
                    drawRect(x2, y1, rectwidth, rectwidth);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Similar to the rectangle tool, except width and height are the same
                    double rectwidth = x1 - x2;
                    drawRect(x2, y2, rectwidth, rectwidth);
                }
            }
            
            
            //What if the circle tool is selected?
            else if(PaintToolBar.Circle.isSelected() == true){
                
                //Similar to the rectangle and ellipse tools, different shape
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except width and height are the same
                    //The shape is also a circle
                    double rectwidth = x2 - x1;
                    drawe(x1, y1, rectwidth, rectwidth);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the rectangle tool, except width and height are the same
                    //The shape is also a circle
                    double rectwidth = x2 - x1;
                    drawe(x1, y2, rectwidth, rectwidth);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the rectangle tool, except width and height are the same
                    //The shape is also a circle
                    double rectwidth = x1 - x2;
                    drawe(x2, y1, rectwidth, rectwidth);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Similar to the rectangle tool, except width and height are the same
                    //The shape is also a circle
                    double rectwidth = x1 - x2;
                    drawe(x2, y2, rectwidth, rectwidth);
                }
            }
            
            //What if the rounded rectangle tool is selected?
            else if (PaintToolBar.Rounded.isSelected()){
                
                //Similar to the rectangle and ellipse tools, different shape
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except different method is called
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    drawRound(x1, y1, rectwidth, rectheight);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the rectangle tool, except different method is called
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    drawRound(x1, y2, rectwidth, rectheight);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the rectangle tool, except different method is called
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    drawRound(x2, y1, rectwidth, rectheight);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Similar to the rectangle tool, except different method is called
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    drawRound(x2, y2, rectwidth, rectheight);
                }
            }
            
            //What if the eraser tool is selected?
            else if (PaintToolBar.Eraser.isSelected() == true) {
                
                //Similar to the rectangle and ellipse tools, except fill and stroke are white
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                x2 = eh.getX();
                y2 = eh.getY();
                graphic.setLineWidth(PaintToolBar.slider1.getValue());
                graphic.setStroke(Color.WHITE);
                graphic.setFill(Color.WHITE);
                //What if it goes down and to the right?
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except we only want fill
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    graphic.fillRect(x1, y1, rectwidth, rectheight);
                    //What if it goes up and to the right?
                } else if (x2 > x1 && y1 > y2) {
                    
                    //Similar to the rectangle tool, except we only want fill
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    graphic.fillRect(x1, y2, rectwidth, rectheight);
                    //What if it goes down and to the left?
                } else if (x1 > x2 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except we only want fill
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    graphic.fillRect(x2, y1, rectwidth, rectheight);
                    //What if it goes up and to the left?
                } else {
                    
                    //Similar to the rectangle tool, except we only want fill
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    graphic.fillRect(x2, y2, rectwidth, rectheight);
                }
                
            }
            
            //What if the Polygon tool is selected?
            else if (PaintToolBar.Polygon.isSelected() == true){
                
                //Peek and x2 y2 processes similar to that of the rectangle tool
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //Calculating radius and getting the amount of sides from the text field polytext
                double polyradius = Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
                int sides = parseInt(PaintToolBar.polytext.getText());
                
                //Arrays used in the calling of the strokePolygon()/fillPolygon()
                double[] xarr = new double[sides];
                double[] yarr = new double[sides];
                
                //How to populate the arrays
                for (int i = 0; i < sides; i++){
                    xarr[i] = ((x1 + (polyradius * Math.cos((2*Math.PI*i)/sides))));
                    yarr[i] = ((y2 + (polyradius * Math.sin((2*Math.PI*i)/sides))));
                }
                
                //To fill or not to fill, that is the question.
                if(PaintToolBar.check.isSelected()){
                    graphic.strokePolygon(xarr, yarr, sides);
                    graphic.fillPolygon(xarr, yarr, sides);
                }
                else{
                    graphic.strokePolygon(xarr, yarr, sides);
                }
                
            }
            
            //What if the cut tool is selected
            else if (PaintToolBar.Cut.isSelected()){
                
                //Peek and x2 y2 processes similar to that of the rectangle tool
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                x2 = eh.getX();
                y2 = eh.getY();
                
                //Setting up for the outline for the cut tool
                graphic.setStroke(Color.BLACK);
                graphic.setLineWidth(0.25);
                graphic.setFill(Color.WHITE);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    
                    //We do not fill here, this is the difference between this and the rectangle tool for mouse dragged
                    graphic.strokeRect(x1, y1, rectwidth, rectheight);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the rectangle tool
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    
                    //We do not fill here, this is the difference between this and the rectangle tool for mouse dragged
                    graphic.strokeRect(x1, y2, rectwidth, rectheight);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the rectangle tool
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    
                    //We do not fill here, this is the difference between this and the rectangle tool for mouse dragged
                    graphic.strokeRect(x2, y1, rectwidth, rectheight);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Similar to the rectangle tool
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    
                    //We do not fill here, this is the difference between this and the rectangle tool for mouse dragged
                    graphic.strokeRect(x2, y2, rectwidth, rectheight);
                }
            }
            
            //What if the copy tool is selected
            else if (PaintToolBar.Copy.isSelected()){
                
                //Peek and x2 y2 processes similar to that of the rectangle tool
                Image image1 = (Image)undoHistory.peek();
                graphic.drawImage(image1, 0, 0);
                x2 = eh.getX();
                y2 = eh.getY();
                
                //Setting up for the outline for the copy tool
                graphic.setStroke(Color.BLACK);
                graphic.setLineWidth(0.25);
                graphic.setFill(Color.WHITE);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the cut tool for mouse dragged
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    graphic.strokeRect(x1, y1, rectwidth, rectheight);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the cut tool for mouse dragged
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    graphic.strokeRect(x1, y2, rectwidth, rectheight);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the cut tool for mouse dragged
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    graphic.strokeRect(x2, y1, rectwidth, rectheight);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Similar to the cut tool for mouse dragged
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    graphic.strokeRect(x2, y2, rectwidth, rectheight);
                }
            }
            
        });
        
        
        //What if the mouse is released?
        this.setOnMouseReleased(eh -> {
            
            //What if the line drawer is selected?
            if (PaintToolBar.LineDrawer.isSelected()){
                
                //Getting the x and y of the event and drawing the line
                x2 = eh.getX();
                y2 = eh.getY();
                initDraw(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker);
                graphic.strokeLine(x1, y1, x2, y2);
            }
            
            //What if the pencil tool is selected?
            else if (PaintToolBar.PencilTool.isSelected()){
                //Do nothing
            }
            
            //What if the rectangle tool is selected?
            else if (PaintToolBar.Rectangle.isSelected()){
                
                //Getting the x and y of the event and drawing the rectangle
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Calculate width and height as normal, then draw accordingly
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    drawRect(x1, y1, rectwidth, rectheight);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Treat the starting point of the rectangle as x1 and y2, then calculate width and height
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    
                    //Draw accordingly
                    drawRect(x1, y2, rectwidth, rectheight);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Treat the starting point of the rectangle as x2 and y1, then calculate width and height
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    
                    //Draw accordingly
                    drawRect(x2, y1, rectwidth, rectheight);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Treat the starting point of the rectangle as x2 and y2, then calculate width and height
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    
                    //Draw accordingly
                    drawRect(x2, y2, rectwidth, rectheight);
                }
            }
            
            //What if the ellipse tool is selected?
            else if (PaintToolBar.Ellipse.isSelected()){
                
                //Getting the x and y of the event and drawing the ellipse
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except with ellipses
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    drawe(x1, y1, rectwidth, rectheight);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the rectangle tool, except with ellipses
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    drawe(x1, y2, rectwidth, rectheight);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the rectangle tool, except with ellipses
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    drawe(x2, y1, rectwidth, rectheight);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Similar to the rectangle tool, except with ellipses
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    drawe(x2, y2, rectwidth, rectheight);
                }
            }
            
            //What if the square tool is selected?
            else if(PaintToolBar.Square.isSelected() == true){
                
                //Similar to the rectangle and ellipse tools, different shape
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except width and height are the same
                    double rectwidth = x2 - x1;
                    drawRect(x1, y1, rectwidth, rectwidth);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the rectangle tool, except width and height are the same
                    double rectwidth = x2 - x1;
                    drawRect(x1, y2, rectwidth, rectwidth);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the rectangle tool, except width and height are the same
                    double rectwidth = x1 - x2;
                    drawRect(x2, y1, rectwidth, rectwidth);
                }
                
                //What if it goes up and to the left?
                else{
                    double rectwidth = x1 - x2;
                    
                    //Similar to the rectangle tool, except width and height are the same
                    drawRect(x2, y2, rectwidth, rectwidth);
                }
            }
            
            //What if the circle tool is selected?
            else if(PaintToolBar.Circle.isSelected() == true){
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the square tool, except the shape is a circle
                    double rectwidth = x2 - x1;
                    drawe(x1, y1, rectwidth, rectwidth);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the square tool, except the shape is a circle
                    double rectwidth = x2 - x1;
                    drawe(x1, y2, rectwidth, rectwidth);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the square tool, except the shape is a circle
                    double rectwidth = x1 - x2;
                    drawe(x2, y1, rectwidth, rectwidth);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Similar to the square tool, except the shape is a circle
                    double rectwidth = x1 - x2;
                    drawe(x2, y2, rectwidth, rectwidth);
                }
            }
            
            //What if the rounded rectangle tool is selected?
            else if (PaintToolBar.Rounded.isSelected()){
                
                //Similar to the rectangle and ellipse tools, different shape
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                
                //What if it goes down and to the right
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except different method is called
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    drawRound(x1, y1, rectwidth, rectheight);
            }
                //What if it goes up and to the right?
                else if (x2 > x1 && y1 > y2){
                    
                    //Similar to the rectangle tool, except different method is called
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    drawRound(x1, y2, rectwidth, rectheight);
                }
                
                //What if it goes down and to the left?
                else if (x1 > x2 && y2 > y1){
                    
                    //Similar to the rectangle tool, except different method is called
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    drawRound(x2, y1, rectwidth, rectheight);
                }
                
                //What if it goes up and to the left?
                else{
                    
                    //Similar to the rectangle tool, except different method is called
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    drawRound(x2, y2, rectwidth, rectheight);
                }
            }
            
            //What if the eraser tool is selected?
            else if (PaintToolBar.Eraser.isSelected() == true) {
                
                //Similar to the rectangle and ellipse tools, except fill and stroke are white
                x2 = eh.getX();
                y2 = eh.getY();
                graphic.setLineWidth(PaintToolBar.slider1.getValue());
                graphic.setStroke(Color.WHITE);
                graphic.setFill(Color.WHITE);
                //What if it goes down and to the right?
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool, except we only want fill
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    graphic.fillRect(x1, y1, rectwidth, rectheight);
                    //What if it goes up and to the right?
                } else if (x2 > x1 && y1 > y2) {
                    
                    //Similar to the rectangle tool, except we only want fill
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    graphic.fillRect(x1, y2, rectwidth, rectheight);
                    //What if it goes down and to the left?
                } else if (x1 > x2 && y2 > y2) {
                    
                    //Similar to the rectangle tool, except we only want fill
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    graphic.fillRect(x2, y1, rectwidth, rectheight);
                    //What if it goes up and to the left?
                } else {
                    
                    //Similar to the rectangle tool, except we only want fill
                    double rectwidth = x1 - x2;
                    double rectheight = y1 - y2;
                    graphic.fillRect(x2, y2, rectwidth, rectheight);
                }
                
            }
            
            //What if the Polygon tool is selected?
            else if (PaintToolBar.Polygon.isSelected() == true){
                
                //Getting the x2 and y2 is a similar process to that of the rectangle tool
                x2 = eh.getX();
                y2 = eh.getY();
                initRect(graphic, PaintToolBar.slider1, PaintToolBar.colorPicker, PaintToolBar.c22);
                double polyradius = Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
                int sides = parseInt(PaintToolBar.polytext.getText());
                
                //Arrays used in the calling of the strokePolygon()/fillPolygon()
                double[] xarr = new double[sides];
                double[] yarr = new double[sides];
                
                //How to populate the arrays
                for (int i = 0; i < sides; i++){
                    xarr[i] = ((x1 + (polyradius * Math.cos((2*Math.PI*i)/sides))));
                    yarr[i] = ((y2 + (polyradius * Math.sin((2*Math.PI*i)/sides))));
                }
                
                //To fill or not to fill, that is the question.
                if(PaintToolBar.check.isSelected()){
                    graphic.strokePolygon(xarr, yarr, sides);
                    graphic.fillPolygon(xarr, yarr, sides);
                }
                else{
                    graphic.strokePolygon(xarr, yarr, sides);
                }
                
            }
            //What if the cut tool is selected
            else if (PaintToolBar.Cut.isSelected() == true){
                x2 = eh.getX();
                y2 = eh.getY();
                graphic.setStroke(Color.BLACK);
                graphic.setLineWidth(1);
                graphic.setFill(Color.WHITE);
                
                //What if it goes down and to the right?
                if (x2 > x1 && y2 > y1) {
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    
                    //Here we get the image to be drawn
                    Image writable = this.snapshot(null, null);
                    BufferedImage image = SwingFXUtils.fromFXImage(writable, null);
                    BufferedImage subImage = new BufferedImage((int) rectwidth, (int) rectheight, BufferedImage.OPAQUE);
                    subImage.createGraphics().drawImage(image.getSubimage((int) x1, (int) y1, (int) rectwidth, (int) rectheight), 0, 0, null);
                    
                    //The selected image is copied to nextSelect to be pasted later
                    nextSelect = SwingFXUtils.toFXImage(subImage, null);
                    
                    //Fill white
                    graphic.fillRect(x1, y1, rectwidth, rectheight);

                    //What if it goes up and to the right?
                } else if (x2 > x1 && y1 > y2) {
                    
                    //Similar to the rectangle tool
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    
                    //Here we get the image to be drawn
                    Image writable = this.snapshot(null, null);
                    BufferedImage image = SwingFXUtils.fromFXImage(writable, null);
                    BufferedImage subImage = new BufferedImage((int) rectwidth, (int) rectheight, BufferedImage.OPAQUE);
                    subImage.createGraphics().drawImage(image.getSubimage((int) x1, (int) y2, (int) rectwidth, (int) rectheight), 0, 0, null);
                    
                    //The selected image is copied to nextSelect to be pasted later
                    nextSelect = SwingFXUtils.toFXImage(subImage, null);
                    
                    //Fill white
                    graphic.fillRect(x1, y2, rectwidth, rectheight);
                    
                    //What if it goes down and to the left?
                } else if (x1 > x2 && y2 > y1) {
                    
                    //Similar to the rectangle tool
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    
                    //Here we get the image to be drawn
                    Image writable = this.snapshot(null, null);
                    BufferedImage image = SwingFXUtils.fromFXImage(writable, null);
                    BufferedImage subImage = new BufferedImage((int) rectwidth, (int) rectheight, BufferedImage.OPAQUE);
                    subImage.createGraphics().drawImage(image.getSubimage((int) x2, (int) y1, (int) rectwidth, (int) rectheight), 0, 0, null);
                    
                    //The selected image is copied to nextSelect to be pasted later
                    nextSelect = SwingFXUtils.toFXImage(subImage, null);
                    
                    //Fill white
                    graphic.fillRect(x2, y1, rectwidth, rectheight);

                    //What if it goes up and to the left?
                } else if (x1 > x2 && y1 > y2){
                    
                    //Similar to the rectangle tool
                    double rectw = x1 - x2;
                    double recth = y1 - y2;
                    System.out.println(rectw);
                    
                    //Here we get the image to be drawn
                    Image writable = this.snapshot(null, null);
                    BufferedImage image = SwingFXUtils.fromFXImage(writable, null);
                    BufferedImage subImage = new BufferedImage((int) rectw, (int) recth, BufferedImage.OPAQUE);
                    subImage.createGraphics().drawImage(image.getSubimage((int) x2, (int) y2, (int) rectw, (int) recth), 0, 0, null);
                    
                    //The selected image is copied to nextSelect to be pasted later
                    nextSelect = SwingFXUtils.toFXImage(subImage, null);
                    
                    //Fill white
                    graphic.fillRect(x2, y2, rectw, recth);

                }
                
            }
            
            //What if the copy tool is selected?
            else if (PaintToolBar.Copy.isSelected() == true){
                x2 = eh.getX();
                y2 = eh.getY();
                graphic.setStroke(Color.BLACK);
                graphic.setLineWidth(1);
                graphic.setFill(Color.WHITE);
                
                //What if it goes down and to the right?
                if (x2 > x1 && y2 > y1) {
                    
                    //Similar to the rectangle tool
                    double rectwidth = x2 - x1;
                    double rectheight = y2 - y1;
                    
                    //Here we get the image to be drawn
                    Image writable = this.snapshot(null, null);
                    BufferedImage image = SwingFXUtils.fromFXImage(writable, null);
                    BufferedImage subImage = new BufferedImage((int) rectwidth, (int) rectheight, BufferedImage.OPAQUE);
                    subImage.createGraphics().drawImage(image.getSubimage((int) x1, (int) y1, (int) rectwidth, (int) rectheight), 0, 0, null);
                    
                    //The selected image is copied to nextSelect to be pasted later
                    nextSelect = SwingFXUtils.toFXImage(subImage, null);

                    //What if it goes up and to the right?
                } else if (x2 > x1 && y1 > y2) {
                    
                    //Similar to the rectangle tool
                    double rectwidth = x2 - x1;
                    double rectheight = y1 - y2;
                    
                    //Here we get the image to be drawn
                    Image writable = this.snapshot(null, null);
                    BufferedImage image = SwingFXUtils.fromFXImage(writable, null);
                    BufferedImage subImage = new BufferedImage((int) rectwidth, (int) rectheight, BufferedImage.OPAQUE);
                    subImage.createGraphics().drawImage(image.getSubimage((int) x1, (int) y2, (int) rectwidth, (int) rectheight), 0, 0, null);
                    
                    //The selected image is copied to nextSelect to be pasted later
                    nextSelect = SwingFXUtils.toFXImage(subImage, null);
                    
                    //What if it goes down and to the left?
                } else if (x1 > x2 && y2 > y1) {
                    
                    //Similar to the rectangle tool
                    double rectwidth = x1 - x2;
                    double rectheight = y2 - y1;
                    
                    //Here we get the image to be drawn
                    Image writable = this.snapshot(null, null);
                    BufferedImage image = SwingFXUtils.fromFXImage(writable, null);
                    BufferedImage subImage = new BufferedImage((int) rectwidth, (int) rectheight, BufferedImage.OPAQUE);
                    subImage.createGraphics().drawImage(image.getSubimage((int) x2, (int) y1, (int) rectwidth, (int) rectheight), 0, 0, null);
                    
                    //The selected image is copied to nextSelect to be pasted later
                    nextSelect = SwingFXUtils.toFXImage(subImage, null);

                    //What if it goes up and to the left?
                } else if (x1 > x2 && y1 > y2){
                    
                    //Similar to the rectangle tool
                    double rectw = x1 - x2;
                    double recth = y1 - y2;
                    System.out.println(rectw);
                    
                    //Here we get the image to be drawn
                    Image writable = this.snapshot(null, null);
                    BufferedImage image = SwingFXUtils.fromFXImage(writable, null);
                    BufferedImage subImage = new BufferedImage((int) rectw, (int) recth, BufferedImage.OPAQUE);
                    subImage.createGraphics().drawImage(image.getSubimage((int) x2, (int) y2, (int) rectw, (int) recth), 0, 0, null);
                    
                    //The selected image is copied to nextSelect to be pasted later
                    nextSelect = SwingFXUtils.toFXImage(subImage, null);
                }
                
            }
            PaintTab.change = true;
        });
        
        //This is how we undo
        PaintMenuBar.Undo.setOnAction(eh -> {
            if(!undoHistory.empty()){
                
                //Saving our work into the redoHistory stack before we undo
                WritableImage neww = new WritableImage((int) this.getWidth(), (int) this.getHeight());
                this.snapshot(null, neww);
                
                //Actually doing the undo
                Image randomI = undoHistory.pop();
                graphic.drawImage(randomI, 0, 0);
                redoHistory.push(neww);
                
                //Logging the undo process
                logAction(" Something was undone");
            }
        });
        
        //Keybind for undo
        PaintMenuBar.Undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        
        //This is how we redo
        PaintMenuBar.Redo.setOnAction(eh -> {
            if(!redoHistory.empty()){
                
                //Saving our work into the undoHistory stack before we undo
                WritableImage neww = new WritableImage((int) this.getWidth(), (int) this.getHeight());
                this.snapshot(null, neww);
                
                //Actually doing the redo
                Image randomI = redoHistory.pop();
                graphic.drawImage(randomI, 0, 0);
                undoHistory.push(neww);
                
                //Logging the redo process
                logAction(" Something was redone.");
            }
        });
        
        //Keybind for redo
        PaintMenuBar.Redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));

        
        //This is how we zoom out
        scale = new Scale(this.getWidth(), this.getHeight());
        zoom = 1;
        PaintMenuBar.ZoomOut.setOnAction(eh -> {
            
            //Removing the scale and replacing it with a more zoomed out scale
            PaintTab.pane.getTransforms().remove(scale);
            zoom -= 0.1;
            scale = new Scale(zoom, zoom, 0, 0);
            PaintTab.pane.getTransforms().add(scale);
            logAction(" The user zoomed in.");
        });
        
        //This is how we zoom in
        PaintMenuBar.ZoomIn.setOnAction(eh -> {
            
            //Removing the scale and replacing it with a more zoomed in scale
            PaintTab.pane.getTransforms().remove(scale);
            zoom += 0.1;
            scale = new Scale(zoom, zoom, 0, 0);
            PaintTab.pane.getTransforms().add(scale);
            logAction(" The user zoomed out.");
        });
        
        //This is how we resize
        PaintMenuBar.Resize.setOnAction(eh -> {
            
            //Setting up a new stage and adding text fields, buttons, and a layout to it
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
                   
                   //Modifying the width and the height of the canvas with the numbers the user entered
                   WritableImage writableImage = new WritableImage((int) this.getWidth(), (int) this.getHeight());
                   this.snapshot(null, writableImage);
                   this.setWidth(parseInt(xTextField.getText()));
                   this.setHeight(parseInt(yTextField.getText()));
                   graphic.drawImage(writableImage, 0, 0, this.getWidth(), this.getHeight());
                   resizeStage.close();
                   PaintTab.change = true;
                   logAction(" The user resized the canvas.");
               }
               catch(Exception L){
                   System.out.println("Something went wrong...");
               }
               
            });
            
        });
        
        //Implementation of rainbow fun mode
        PaintMenuBar.RainbowFun.setOnAction(eh -> {
            //Random rgb values, needed for rainbow fun mode
            Random random = new Random();
            for(int i = 0; i < this.getWidth(); i++){
                for(int j = 0; j < this.getHeight(); j++){
                    float r = random.nextFloat();
                    float g = random.nextFloat();
                    float b = random.nextFloat();
                    r = r*255;
                    b = b*255;
                    g = g*255;
                    pw.setColor(i, j, Color.rgb((int) r, (int) g, (int) b));
                }
            }
            logAction(" Rainbow Fun Mode initiated.");
        });
        
        
        //Keybinds for zoom and resize
        PaintMenuBar.ZoomIn.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
        PaintMenuBar.ZoomOut.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
        PaintMenuBar.Resize.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

        
    }
    
    /**
     * This is the method which draws rectangles, takes 4 doubles as input.
     * @param x, first x coordinate of the rectangle (where you start dragging)
     * @param y, first y coordinate of the rectangle (where you start dragging)
     * @param width, width of the rectangle you want to draw
     * @param height, height of the rectangle you want to draw
     */
    public void drawRect(double x, double y, double width, double height){
        if (PaintToolBar.check.isSelected() == true) {
            graphic.strokeRect(x, y, width, height);
            graphic.fillRect(x, y, width, height);
        } else {
            graphic.strokeRect(x, y, width, height);
        }
    }
    
    /**
     * This is the method which draws ellipses, takes 4 doubles as input.
     * @param x, first x coordinate of the ellipse (where you start dragging)
     * @param y, first y coordinate of the ellipse (where you start dragging)
     * @param width, width of the ellipse you want to draw
     * @param height, height of the ellipse you want to draw
     */
    public void drawe(double x, double y, double width, double height){
        if (PaintToolBar.check.isSelected() == true) {
            graphic.strokeOval(x, y, width, height);
            graphic.fillOval(x, y, width, height);
        } else {
            graphic.strokeOval(x, y, width, height);
        }
    }
    
     /**
     * This is the method which draws rounded rectangles, takes 4 doubles as input.
     * @param x, first x coordinate of the rounded rectangle (where you start dragging)
     * @param y, first y coordinate of the rounded rectangle (where you start dragging)
     * @param width, width of the rectangle you want to draw
     * @param height, height of the rectangle you want to draw
     */
    public void drawRound(double x, double y, double width, double height){
        if (PaintToolBar.check.isSelected() == true) {
            graphic.strokeRoundRect(x, y, width, height, 10, 10);
            graphic.fillRoundRect(x, y, width, height, 10, 10);
        } else {
            graphic.strokeRoundRect(x, y, width, height, 10, 10);
        }
    }
    
    /**
     * This helps us with undo/redo.
     * @param UndoHistory, the stack in which it holds the stuff you did
     * @param canvas, the canvas from which you are undoing
     */
    public void addUndo(Stack<Image> UndoHistory, PaintCanvas canvas){
        prevImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, prevImage);
        UndoHistory.push(prevImage);
    }

    /**
     * This is the method called which assists in the creation of the pencil tool/line drawer.
     * @param gc, graphics context very necessary, through this we set the colors and line width
     * @param s, slider that sets our line width
     * @param c, color picker that sets the color for both stroke and fill
     */
    public void initDraw(GraphicsContext gc, Slider s, ColorPicker c){
         gc.setLineWidth(s.getValue());
         gc.setStroke(c.getValue());
         gc.setFill(c.getValue());
    }
    
    /**
     * This is the method called which assists in the creation of the shapes tools.
     * @param gc, graphics context very necessary, through this we set the colors and line width
     * @param s, slider that sets the width of the stroke
     * @param c, color picker that sets the color for stroke
     * @param c2, color picker that sets the color for the fill
     */
    public void initRect(GraphicsContext gc, Slider s, ColorPicker c, ColorPicker c2){
        gc.setLineWidth(s.getValue());
        gc.setStroke(c.getValue());
        gc.setFill(c2.getValue());
    }

    /**
     * This is what helps us use the text tool.
     * @param gc, graphics context very necessary, through this we draw the text
     * @param x, x coordinate of where you click
     * @param y, y coordinate of where you click
     */
    public void drawText(GraphicsContext gc, double x, double y){
        gc.strokeText(PaintToolBar.textField.getText(), x, y);
        gc.fillText(PaintToolBar.textField.getText(), x, y);
    }
    
    /**
     * Accessor method for graphics context.
     * @return graphics context from a paint canvas object
     */
    public GraphicsContext getGC(){
        return graphic;
    }
    
    /**
     * This is the method which logs the actions being done.
     * @param action, this is the string that describes what is being used/clicked.
     */
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
    
}
