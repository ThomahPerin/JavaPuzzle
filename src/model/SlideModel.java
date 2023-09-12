package model;

import common.Observer;
import common.Configuration;
import common.Solver;
import slide.SlideConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * The representation, logic and rules of the game.
 */
public class SlideModel {
    /** the collection of observers of this model */
    private final List<Observer<SlideModel, String>> observers = new LinkedList<>();

    /** a list of configurations that lead to the solution*/
    private LinkedList<Configuration> path;

    /** the number of rows and columns in the puzzle*/
    private int numRows;
    private int numCols;

    public int[][] board;

    /** the name of the file that holds the puzzle's info*/
    private String filename;

    /** the initial row and column position begins at -1 to signify it hasn't been selected yet*/
    private int rowSelect = -1;
    private int colSelect = -1;


    /** the current configuration */

    private SlideConfig currentConfig;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<SlideModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * The constructor takes a filename and creates a configuration out of it.
     * It then uses alert observers to indicate that the model has changed.
     *
     * @param filename
     * @throws IOException
     */
    public SlideModel(String filename) throws IOException {
        this.filename = filename;
        this.currentConfig = new SlideConfig(filename);

        this.alertObservers("Loaded: " + filename);
    }

    public SlideModel() throws IOException{
        System.out.println("Reached!");
        this.filename = "Random Puzzle";
        this.generate2();
    }


    public void generate2(){
        boolean isSolvable = false;

        // exit once a valid board is built
        while (isSolvable == false) {

            Random random = new Random();
            // generate 3 or 4
            int randomNumber = random.nextInt(2) + 3;
            // board is either 3 by 3 or 4 by 4
            this.board = new int[randomNumber][randomNumber];
            this.numRows = randomNumber;
            this.numCols = randomNumber;


            List<Integer> puzzlePieces = new ArrayList<>();

            if (randomNumber == 3) {
                for (int i = 0; i < 9; i++) {
                    puzzlePieces.add(i);
                }
                Collections.shuffle(puzzlePieces);
            }
            // otherwise random number is 4
            else {
                for (int i = 0; i < 16; i++) {
                    puzzlePieces.add(i);
                }
                Collections.shuffle(puzzlePieces);
            }
            // populate board with integers
            int index = 0;
            for (int i = 0; i < randomNumber; i++) {
                for (int j = 0; j < randomNumber; j++) {
                    board[i][j] = puzzlePieces.get(index);
                    index++;
                }
            }
            isSolvable = this.isSolvable(board, randomNumber);

            if (isSolvable == true){
                this.currentConfig = new SlideConfig(board, randomNumber, randomNumber);
                System.out.println("Alerted");
                this.alertObservers("Loaded a new random puzzle");
            }
        }

    }


    // Assumes square configuration
    boolean isSolvable(int[][] puzzleGrid, int dimensions) {
        int inversions = 0;

        int numPieces = dimensions * dimensions;
        int[] flattenedGrid = new int[numPieces]; // Assuming a 3x3 grid

        // Flatten the 2D grid into a 1D array
        for (int row = 0; row < dimensions; row++) {
            for (int col = 0; col < dimensions; col++) {
                flattenedGrid[row * dimensions + col] = puzzleGrid[row][col];
            }
        }

        // Count the number of inversions
        for (int i = 0; i < dimensions; i++) {
            for (int j = i + 1; j < dimensions; j++) {
                if (flattenedGrid[i] != 0 && flattenedGrid[j] != 0 && flattenedGrid[i] > flattenedGrid[j]) {
                    inversions++;
                }
            }
        }

        // If the number of inversions is even, the puzzle is solvable
        return inversions % 2 == 0;
    }


    /**
     * The function, when called, resets the puzzle to its initial configuration
     * It then uses alert observers to indicate that the model has changed.
     *
     * @throws IOException
     */
    public void reset() throws IOException {
        this.currentConfig = new SlideConfig(filename);
        this.alertObservers("Puzzle reset!");
    }


    /**
     * The function checks if the puzzle is already solved.
     * It then uses alert observers to indicate that the model has changed.
     * If not it creates a solved path for the model's configuration.
     * It then uses alert observers to indicate that the model has changed.
     */
    public void hint(){
        boolean isSolved = this.isSolution();
        if (isSolved == true) {
            this.alertObservers("Already solved!");
        } else {
            Solver solver = new Solver();
            path = solver.solve(currentConfig);
            int[][] nextStep;
            nextStep = path.get(1).returnBoard();
            currentConfig.nextStep(currentConfig.getNumRows(), currentConfig.getNumCols(), nextStep);
            this.alertObservers("Next step!");
        }

    }

    /**
     * The function takes in a filename as a parameter and turns it into a file
     * and creates a configuration using the file's data.
     * It then uses alert observers to indicate that the model has changed.
     *
     * @param fileName
     */
    public void load(String fileName){
        this.filename = fileName;
        File file = new File(fileName);
        currentConfig = new SlideConfig(file);
        /**Solver solver = new Solver();
         path = solver.solve(currentConfig);
         progression = 1;*/
        this.alertObservers("Loaded: " + fileName);
    }

    /**
     * The function takes in a filename as a parameter
     * and creates a configuration using the file's data.
     * It then uses alert observers to indicate that the model has changed.
     *
     * @param fileName
     */
    public void load(File fileName){
        //File file = new File(fileName.toURI());
        currentConfig = new SlideConfig(fileName);
        /**Solver solver = new Solver();
         path = solver.solve(currentConfig);
         progression = 1;*/
        this.alertObservers("Loaded: " + fileName);
    }

    /**
     * return the filename
     * @return
     */
    public String getFilename(){return this.filename;}

    /**
     * Create a string representation of the puzzle
     * @return
     */
    @Override
    public String toString() {
        numCols = currentConfig.getNumCols();
        numRows = currentConfig.getNumRows();
        String s = "    ";
        for (int i = 0; i < currentConfig.numCols; i++){
            s += i + "  ";
        }
        s += "\n";
        s += "  ----------";
        s += "\n";
        for (int i = 0; i < numRows; i++){
            s += i + "|";
            for (int j = 0; j < numCols; j++){
                s += "  ";
                if (this.currentConfig.getValue(i, j) == 0){
                    s += ".";
                }
                else{
                    s += this.currentConfig.getValue(i, j);
                }
            }
            s += "\n";
        }
        return s;
    }

    /**
     * This function takes a row and a column.
     * If the configuration is alreayd solved it
     * uses alert observers to indicate that.
     *
     * If the user is selecting the first puzzle piece it checks if it is valid.
     * It then uses alert observers to indicate that the model has changed.
     *
     * If the user is selectting the seond puzzle piece it checks that it is zero.
     * It then uses alert observers to indicate that the model has changed.
     *
     * @param row
     * @param col
     */
    public void setSelection(int row, int col){

        if (currentConfig.isSolution()){
            alertObservers("Already Solved!");
            return;
        }
        // this is first selection
        if (this.rowSelect == -1) {
            if (!currentConfig.isAPiece(row, col)) {
                alertObservers("Invalid coordinates!");
                return;
            }
            else {
                this.rowSelect = row;
                this.colSelect = col;
                this.alertObservers("Selected (" + rowSelect + ", " + colSelect + ")");
                return;
            }
        }
        // this is second selection
        else {
            if (currentConfig.isAPiece(row, col)) {

                this.alertObservers("Invalid coords");
                this.colSelect = -1;
                this.rowSelect = -1;
                return;
            }

            else if (currentConfig.isValid(rowSelect, colSelect, row, col)){
                currentConfig.setValue(row, col, currentConfig.getValue(rowSelect, colSelect));
                currentConfig.setValue(rowSelect, colSelect, 0);
                this.alertObservers("Moved (" + rowSelect + ", " + colSelect + ") to (" + row + ", " + col + ")");
                this.rowSelect = -1;
                this.colSelect = -1;
            }
            else{
                this.alertObservers("Invalid coords!");
                this.colSelect = -1;
                this.rowSelect = -1;
                return;

            }
        }

    }

    /**
     * return true if the configuration is solved.
     * @return
     */
    public boolean isSolution(){
        return currentConfig.isSolution();
    }

    /** return number of rows in the board*/
    public int getNumRows(){return currentConfig.getNumRows();}

    /** return number of columns in the board*/
    public int getNumCols(){return currentConfig.getNumCols();}

    /** return the value at the given row and column*/
    public int getValue(int row, int column){
        return currentConfig.getValue(row, column);
    }

    public int getZeroRowPos() {
        return currentConfig.getZeroRowPos();
    }

    public int getZeroColPos(){
        return currentConfig.getZeroColPos();
    }


}

