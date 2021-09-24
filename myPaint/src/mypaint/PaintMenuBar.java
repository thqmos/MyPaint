
package mypaint;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;


public class PaintMenuBar{
    
    //This helps us with consolidation of the main file
    protected MenuBar menuBar;
    protected Menu File;
    protected Menu Home;
    protected Menu View;
    protected Menu Help;
    protected MenuItem selectImage;
    protected MenuItem CloseWS;
    protected MenuItem ReleaseNotes;
    protected MenuItem Save;
    protected MenuItem SaveAs;
    protected MenuItem About;
    protected MenuItem HelpM;
    protected MenuItem ZoomOut;
    protected MenuItem ZoomIn;
    protected MenuItem Resize;
    protected MenuItem newTabOpener;
    protected MenuItem Undo;
    protected MenuItem Redo;
    
    
    
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
        File.getItems().add(selectImage);
        File.getItems().add(Save);
        File.getItems().add(SaveAs);
        File.getItems().add(newTabOpener);
        File.getItems().add(CloseWS);
        View.getItems().add(ZoomOut);
        View.getItems().add(ZoomIn);
        View.getItems().add(Resize);
        Help.getItems().add(HelpM);
        Help.getItems().add(About);
        Help.getItems().add(ReleaseNotes);
        
    }
        
}
