package slide;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import common.Observer;
import model.SlideModel;

import java.io.File;
import java.io.IOException;

/**
 * View and controller for the GUI
 */
public class SlideGUI extends Application implements Observer<SlideModel, String> {

    /** The copy of the model used by the controller*/
    private SlideModel model;

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int FONT_SIZE = 12;
    private final static int NUMBER_FONT_SIZE = 24;
    /** Colored buttons */
    private final static String EVEN_COLOR = "#ADD8E6";
    private final static String ODD_COLOR = "#FED8B1";
    private final static String EMPTY_COLOR = "#FFFFFF";

    /** the stage for the GUI*/
    private Stage stage;

    public int zeroRowPos;
    public int zeroColPos;

    public Label fileNameLabel;

    /** The init of the GUI class, gets filename, creates a model with it and adds the model as an observer*/
    @Override
    public void init() throws IOException {
        // get the file name from the command line
        try {
            String filename = getParameters().getRaw().get(0);
            this.model = new SlideModel(filename);
            this.model.addObserver(this);
        }
        catch(Exception E){
            System.out.println("No File Found Generating Random!");
            this.model = new SlideModel();
        }
    }

    /** GUI function called on start, creates the GUI board, the buttons, and the filename label*/
    @Override
    public void start(Stage stage) throws Exception {
        zeroColPos = model.getZeroRowPos();
        zeroColPos = model.getZeroColPos();
        this.stage = stage;
        BorderPane root = new BorderPane();

        // Top: File Name Label
        fileNameLabel = new Label(model.getFilename());

        root.setTop(fileNameLabel);

        // Center: Grid of Squares
        GridPane centerGrid = new GridPane();
        centerGrid.setAlignment(Pos.CENTER);
        //centerGrid.setHgap(10);
        //centerGrid.setVgap(10);

        System.out.println(model.getValue(zeroRowPos, zeroColPos));
        for (int row = 0; row < model.getNumRows(); row++) {

            for (int col = 0; col < model.getNumCols(); col++) {

                if (model.getValue(row, col) == 0) {
                    Button button2 = new Button();
                    button2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                    //button1.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                    // this is empty
                    button2.setMinSize(ICON_SIZE, ICON_SIZE);
                    button2.setMaxSize(ICON_SIZE, ICON_SIZE);
                    GridPane.setRowIndex(button2, row);
                    GridPane.setColumnIndex(button2, col);
                    centerGrid.getChildren().addAll(button2);

                    int finalRow = row;
                    int finalCol = col;
                    button2.setOnAction(e -> {
                                model.setSelection(finalRow, finalCol);
                                System.out.println("Selected");
                            }
                    );
                }
                else {
                    Button button1 = new Button();
                    //
                    if (model.getValue(row, col) % 2 == 0) {
                        button1.setStyle(
                                "-fx-font-family: Arial;" +
                                        "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                        "-fx-background-color: " + EVEN_COLOR + ";" +
                                        "-fx-font-weight: bold;");
                        button1.setText(String.valueOf(model.getValue(row, col)));
                        button1.setMinSize(ICON_SIZE, ICON_SIZE);
                        button1.setMaxSize(ICON_SIZE, ICON_SIZE);
                        GridPane.setRowIndex(button1, row);
                        GridPane.setColumnIndex(button1, col);
                        centerGrid.getChildren().addAll(button1);

                        int finalRow = row;
                        int finalCol = col;
                        button1.setOnAction(e -> {
                                    model.setSelection(finalRow, finalCol);
                                    System.out.println(finalRow + " " + finalCol);
                                }
                        );
                    } else {
                        button1.setStyle(
                                "-fx-font-family: Arial;" +
                                        "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                        "-fx-background-color: " + ODD_COLOR + ";" +
                                        "-fx-font-weight: bold;");
                        button1.setText(String.valueOf(model.getValue(row, col)));
                        button1.setMinSize(ICON_SIZE, ICON_SIZE);
                        button1.setMaxSize(ICON_SIZE, ICON_SIZE);
                        GridPane.setRowIndex(button1, row);
                        GridPane.setColumnIndex(button1, col);
                        centerGrid.getChildren().addAll(button1);

                        int finalRow = row;
                        int finalCol = col;
                        button1.setOnAction(e -> {
                                    model.setSelection(finalRow, finalCol);
                                    System.out.println(finalRow + " " + finalCol);
                                }
                        );
                    }

                }
            }
        }

        root.setCenter(centerGrid);

        // Bottom: Load, Reset, Hint Buttons
        HBox bottomBox = new HBox(10);
        bottomBox.setAlignment(Pos.CENTER);

        Button loadButton = new Button("Load");
        Button resetButton = new Button("Reset");
        Button hintButton = new Button("Hint");
        Button generateButton = new Button("Generate Random Puzzle");

        bottomBox.getChildren().addAll(loadButton, resetButton, hintButton, generateButton);
        root.setBottom(bottomBox);

        // Button Click Actions
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");

            // Display the file dialog and wait for user interaction
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                // Process the selected file
                System.out.println("Selected");
                model.load(selectedFile);
            }
            fileNameLabel.setText(String.valueOf(selectedFile));
        });
        //end
        resetButton.setOnAction(e -> {
            try {
                model.reset();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        hintButton.setOnAction(e -> {
            model.hint();
        });

        generateButton.setOnAction(e -> {
            try {
                SlideModel ice = new SlideModel();
            } catch (IOException ex) {
                System.out.println("Generation failed!");
            }
            //model.generate2();
        });

        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("GUI Interface");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This function takes the model and a message.
     *  It prints the message and the model
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param data optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(SlideModel model, String data) {
        if (Platform.isFxApplicationThread()){
            this.refresh(model, stage, data);
        }
        else{
            Platform.runLater( () -> this.refresh(model, stage, data));
        }
    }

    private void refresh(SlideModel model, Stage stage, String message){
        zeroColPos = model.getZeroRowPos();
        zeroColPos = model.getZeroColPos();
        this.stage = stage;
        BorderPane root = new BorderPane();

        // Top: File Name Label
        fileNameLabel = new Label(message);
        root.setTop(fileNameLabel);

        // Center: Grid of Squares
        GridPane centerGrid = new GridPane();
        centerGrid.setAlignment(Pos.CENTER);
        //centerGrid.setHgap(10);
        //centerGrid.setVgap(10);

        System.out.println(model.getValue(zeroRowPos, zeroColPos));
        for (int row = 0; row < model.getNumRows(); row++) {

            for (int col = 0; col < model.getNumCols(); col++) {

                if (model.getValue(row, col) == 0) {
                    Button button2 = new Button();
                    System.out.println(row + " " + col);
                    button2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                    //button1.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                    // this is empty
                    button2.setMinSize(ICON_SIZE, ICON_SIZE);
                    button2.setMaxSize(ICON_SIZE, ICON_SIZE);
                    GridPane.setRowIndex(button2, row);
                    GridPane.setColumnIndex(button2, col);
                    centerGrid.getChildren().addAll(button2);

                    int finalRow = row;
                    int finalCol = col;
                    button2.setOnAction(e -> {
                                model.setSelection(finalRow, finalCol);
                                System.out.println("Selected");
                            }
                    );
                }
                else {
                    Button button1 = new Button();
                    //
                    if (model.getValue(row, col) % 2 == 0) {
                        button1.setStyle(
                                "-fx-font-family: Arial;" +
                                        "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                        "-fx-background-color: " + EVEN_COLOR + ";" +
                                        "-fx-font-weight: bold;");
                        button1.setText(String.valueOf(model.getValue(row, col)));
                        button1.setMinSize(ICON_SIZE, ICON_SIZE);
                        button1.setMaxSize(ICON_SIZE, ICON_SIZE);
                        GridPane.setRowIndex(button1, row);
                        GridPane.setColumnIndex(button1, col);
                        centerGrid.getChildren().addAll(button1);

                        int finalRow = row;
                        int finalCol = col;
                        button1.setOnAction(e -> {
                                    model.setSelection(finalRow, finalCol);
                                    System.out.println(finalRow + " " + finalCol);
                                }
                        );
                    } else {
                        button1.setStyle(
                                "-fx-font-family: Arial;" +
                                        "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                        "-fx-background-color: " + ODD_COLOR + ";" +
                                        "-fx-font-weight: bold;");
                        button1.setText(String.valueOf(model.getValue(row, col)));
                        button1.setMinSize(ICON_SIZE, ICON_SIZE);
                        button1.setMaxSize(ICON_SIZE, ICON_SIZE);
                        GridPane.setRowIndex(button1, row);
                        GridPane.setColumnIndex(button1, col);
                        centerGrid.getChildren().addAll(button1);

                        int finalRow = row;
                        int finalCol = col;
                        button1.setOnAction(e -> {
                                    model.setSelection(finalRow, finalCol);
                                    System.out.println(finalRow + " " + finalCol);
                                }
                        );
                    }

                }
            }
        }

        root.setCenter(centerGrid);

        // Bottom: Load, Reset, Hint Buttons
        HBox bottomBox = new HBox(10);
        bottomBox.setAlignment(Pos.CENTER);

        Button loadButton = new Button("Load");
        Button resetButton = new Button("Reset");
        Button hintButton = new Button("Hint");
        Button generateButton = new Button("Generate Random Puzzle");

        bottomBox.getChildren().addAll(loadButton, resetButton, hintButton, generateButton);
        root.setBottom(bottomBox);

        // Button Click Actions
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");

            // Display the file dialog and wait for user interaction
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                // Process the selected file
                System.out.println("Selected");
                model.load(selectedFile);
            }
            fileNameLabel.setText(String.valueOf(selectedFile));
        });
        //end
        resetButton.setOnAction(e -> {
            try {
                model.reset();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        hintButton.setOnAction(e -> {
            model.hint();
        });

        generateButton.setOnAction(e -> {
            model.generate2();
        });

        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("GUI Interface");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launch the GUI
     *
     * @param args
     */
    public static void main(String[] args) {
        // passes command line to java fx
        Application.launch(args);
    }
}

