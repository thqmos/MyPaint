
package tears.of.paint.v2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tears.of.paint.v2.Autosave;

public class TearsOfPaintV2 extends Application{

    //This will be accessed outside of main in PaintTab
    protected static TabPane tabpane;
    protected static Stage stage;
    protected static Scene scene;
    
    /**
     * This method is what fires up the application.
     * @param frame, this is where the PaintMenuBar, the PaintToolBar, and etc. show up.
     * @throws Exception, this deals with the question of what if something goes wrong in the firing up process.
     */
    public void start(Stage frame) throws Exception{
        
        TearsOfPaintV2.stage = frame;
        
        //Autosave working in main
        TimerTask autosave = new Autosave();
        try{
            Timer t = new Timer();
            t.scheduleAtFixedRate(autosave, 0, 1000);
        }
        catch(Exception e){
            System.out.println("Something went wrong...");
        }
        
        //Implementation of the PaintMenuBar
        PaintMenuBar myMenuBar = new PaintMenuBar();
        
        //Implementation of the PaintToolBar
        PaintToolBar myToolBar = new PaintToolBar();
        
        //Implementation of the PaintCanvas
        PaintCanvas canvas = new PaintCanvas();
        
        //Setting up the stage to include various things
        frame.setTitle("Tears of Paint");
        tabpane = new TabPane(new PaintTab());
        tabpane.getSelectionModel().selectFirst();
        
        //This creates a new tab
        myMenuBar.newTabOpener.setOnAction(eh -> {
            tabpane.getTabs().add(new PaintTab());
        });
        
        myMenuBar.newTabOpener.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        
        VBox layout = new VBox(myMenuBar.menuBar, myToolBar.tool, tabpane);
        
        //Setting up the rest of the things
        scene = new Scene(layout, 500, 300);
        frame.setScene(scene);
        frame.setMaximized(true);
        frame.show();
        
        //This is how we get random colors
        myMenuBar.Random.setOnAction(eh -> {
            
            //Random stroke
            Random random = new Random();
            double r = random.nextDouble();
            double g = random.nextDouble();
            double b = random.nextDouble();
            myToolBar.colorPicker.setValue(Color.color(r, g, b));
            
            //Random fill
            Random rand = new Random();
            double r2 = rand.nextDouble();
            double g2 = rand.nextDouble();
            double b2 = rand.nextDouble();
            myToolBar.c22.setValue(Color.color(r2, g2, b2));
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
            File releaseText = new File("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\MyPaintThomasHohnholtReleaseNotes9-10-18.txt");
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
        myMenuBar.ReleaseNotes.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        
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
            File aboutText = new File("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\MyPaintAbout.txt");
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
        myMenuBar.About.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
        
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
            File HelpText = new File("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\MyPaintHelp.txt");
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
        myMenuBar.HelpM.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        
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
                    File aboutText = new File("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\MyPaintAbout.txt");
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
                    File HelpText = new File("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\MyPaintHelp.txt");
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
                    File releaseText = new File("C:\\Users\\thoma\\OneDrive - valpo.edu\\Desktop\\Paint\\Resources\\MyPaintThomasHohnholtReleaseNotes9-10-18.txt");
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
    }
    
    /**
     * This is what runs at run time, this calls the start method from earlier
     * @param args, an array of strings is passed to the main function, this is important for the program's functionality.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
