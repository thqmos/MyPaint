
package tears.of.paint.v2;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;


public class PaintMenuBar {
    //This helps us with consolidation of the main file
    protected MenuBar menuBar;
    protected Menu File;
    protected Menu Home;
    protected Menu View;
    protected Menu Help;
    protected static MenuItem selectImage;
    protected static MenuItem CloseWS;
    protected MenuItem ReleaseNotes;
    protected static MenuItem Save;
    protected static MenuItem SaveAs;
    protected MenuItem About;
    protected MenuItem HelpM;
    protected static MenuItem ZoomOut;
    protected static MenuItem ZoomIn;
    protected static MenuItem Resize;
    protected MenuItem newTabOpener;
    protected static MenuItem Undo;
    protected static MenuItem Redo;
    protected MenuItem Random;
    
    public PaintMenuBar(){
        //Creation of the menu bar
        menuBar = new MenuBar();
        
        //Creation and addition of the menus
        File = new Menu("File");
        Home = new Menu("Home");
        View = new Menu("View");
        Help = new Menu("Help");
        menuBar.getMenus().add(File);
        menuBar.getMenus().add(Home);
        menuBar.getMenus().add(View);
        menuBar.getMenus().add(Help);
        
        //Creation and addition of the menu items
        selectImage = new MenuItem("Select Image");
        CloseWS = new MenuItem("Close");
        ReleaseNotes = new MenuItem("Release Notes");
        Save = new MenuItem("Save");
        SaveAs = new MenuItem("Save as");
        About = new MenuItem("About");
        HelpM = new MenuItem("Help");
        ZoomOut = new MenuItem("Zoom Out");
        ZoomIn = new MenuItem("Zoom In");
        Resize = new MenuItem("Resize");
        newTabOpener = new MenuItem("New Tab");
        Undo = new MenuItem("Undo");
        Redo = new MenuItem("Redo");
        Random = new MenuItem("Random Color!");
        File.getItems().add(selectImage);
        File.getItems().add(Save);
        File.getItems().add(SaveAs);
        File.getItems().add(newTabOpener);
        File.getItems().add(Undo);
        File.getItems().add(Redo);
        File.getItems().add(CloseWS);
        Home.getItems().add(Random);
        View.getItems().add(ZoomOut);
        View.getItems().add(ZoomIn);
        View.getItems().add(Resize);
        Help.getItems().add(HelpM);
        Help.getItems().add(About);
        Help.getItems().add(ReleaseNotes);
        
    }
}
