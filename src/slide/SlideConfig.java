package slide;

import common.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/**
 * represent the puzzle board and change it with functions
 */
public class SlideConfig  implements Configuration {

    /** represent the puzzle with 2d array for rows anc columns*/
    public int[][] board;

    /** represent number of rows and columns in the puzzle*/
    public int numRows;
    public int numCols;

    /** the number for squares in the puzzle*/
    public int numSquares;

    /** represent the puzzles solution*/
    public int[][] solution;
    public int zeroRowPos;
    public int zeroColPos;


    /**
     * Read in the filename as a string and extract the puzzle's starting configuration
     * Create the board and the solution
     * Close the file stream.
     * @param filename
     */
    public SlideConfig(String filename) {

        try {
            BufferedReader f = new BufferedReader(new FileReader(filename));

            String dimensionsLine = f.readLine();
            String[] dimensions = dimensionsLine.split(" ");
            numRows = Integer.parseInt(dimensions[0]);
            numCols = Integer.parseInt(dimensions[1]);

            board = new int[numRows][numCols];
            solution = new int[numRows][numCols];


            for (int i = 0; i < numRows; i++) {
                String dataLine = f.readLine();
                String[] data = dataLine.split(" ");
                for (int j = 0; j < numCols; j++) {
                    if (data[j].equals(".")) {
                        this.zeroRowPos = i;
                        this.zeroColPos = j;
                        board[i][j] = 0; // or any other default value for the special character
                    } else {
                        board[i][j] = Integer.parseInt(data[j]);
                    }
                }
            }

            // close the input file
            f.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        numSquares = numCols * numRows;
        int count = 1;

        // build solution board
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (count != numSquares) {
                    solution[row][col] = count;
                } else {
                    solution[row][col] = 0;
                }
                count++;
            }
        }

        //System.out.println(this.toString());
    }

    /**
     * Read in the filename as a file and extract the puzzle's starting configuration
     * Create the board and the solution
     * Close the file stream.
     * @param file
     */
    public SlideConfig(File file) {
        try {
            BufferedReader f = new BufferedReader(new FileReader(file));

            String dimensionsLine = f.readLine();
            String[] dimensions = dimensionsLine.split(" ");
            numRows = Integer.parseInt(dimensions[0]);
            numCols = Integer.parseInt(dimensions[1]);

            board = new int[numRows][numCols];
            solution = new int[numRows][numCols];

            for (int i = 0; i < numRows; i++) {
                String dataLine = f.readLine();
                String[] data = dataLine.split(" ");
                for (int j = 0; j < numCols; j++) {
                    if (data[j].equals(".")) {
                        this.zeroRowPos = i;
                        this.zeroColPos = j;
                        board[i][j] = 0; // or any other default value for the special character
                    } else {
                        board[i][j] = Integer.parseInt(data[j]);
                    }
                }
            }

            f.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        numSquares = numCols * numRows;
        int count = 1;

        // build solution board
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (count != numSquares) {
                    solution[row][col] = count;
                } else {
                    solution[row][col] = 0;
                }
                count++;
            }
        }
    }


    /**
     * Create a puzzle configuration from the board, and the number of rows and columns in the board
     *
     * @param board
     * @param numRows
     * @param numCols
     */
    public SlideConfig(int[][] board, int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;

        this.board = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++){
            for (int j = 0; j < numCols; j++){
                this.board[i][j] = board[i][j];
            }
        }

        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                if (board[i][j] == 0) {
                    this.zeroRowPos = i;
                    this.zeroColPos = j;
                }
                this.board[i][j] = board[i][j];
            }
        }

        this.solution = new int[numRows][numCols];
        numSquares = this.numCols * this.numRows;
        int count = 1;

        // build solution board
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (count != numSquares) {
                    solution[row][col] = count;
                } else {
                    solution[row][col] = 0;
                }
                count++;
            }
        }

    }


    /**
     * Check if the cureent configuration of the puzzle is the solution
     * Return true if it is
     *
     * @return
     */
    @Override
    public boolean isSolution() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (board[i][j] != solution[i][j]) {
                    return false; // The arrays have different values at corresponding positions.
                }
            }
        }

        return true; // The arrays are equal.
    }

    /**
     * This function returns a collection of possible configurations
     * based on the board. The puzzle can chane based around sliding pieces into the empty square (represented by 0).
     * The function figures out where the empty piece is (e.g., bottom row) which tells the program how many niehbors it has
     * and how to calculate them. These neighbors are then returned as a configuration.
     *
     * @return
     */
    @Override
    public Collection<Configuration> getNeighbors () {
        int rowPos = 0;
        int colPos = 0;
        int foundYet = 0;
        boolean topLCorner = false;
        boolean topRCorner = false;
        boolean botLCorner = false;
        boolean botRCorner = false;
        boolean topRow = false;
        boolean botRow = false;
        boolean leftRow = false;
        boolean rightRow = false;
        boolean middle = false;

        Collection<Configuration> neighbors = new ArrayList<>();

        // determine where the empty square is which determines how many neighbors there are and what the neighbors are
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (board[i][j] == 0){
                    rowPos = i;
                    colPos = j;

                    if (i == 0 && j == 0){
                        topLCorner = true;
                        foundYet++;
                        break;
                    }
                    if (i == 0 && j == numCols - 1){
                        topRCorner = true;
                        foundYet++;
                        break;
                    }
                    if (i == numRows - 1 && j == numCols - 1){
                        botRCorner = true;
                        foundYet++;
                        break;
                    }
                    if (i == numRows - 1 && j == 0){
                        botLCorner = true;
                        foundYet++;
                        break;
                    }
                    if (topLCorner == false && topRCorner == false && i == 0){
                        topRow = true;
                        foundYet++;
                        break;
                    }
                    if (botLCorner == false && botRCorner == false && i == numRows - 1){
                        botRow = true;
                        foundYet++;
                        break;
                    }
                    if (topLCorner == false && botLCorner == false && j == 0){
                        leftRow = true;
                        foundYet++;
                        break;
                    }
                    if (topRCorner == false && botRCorner == false && j == numCols - 1){
                        rightRow = true;
                        foundYet++;
                        break;
                    }
                    if (foundYet == 0){
                        middle = true;
                        break;
                    }
                }
            }
        }

        // 0 position: board[rowPos][intPos]
        // generate neighbors for top left corner
        if (topLCorner == true){
            if (numRows > 1) {
                int[][] first = new int[numRows][numCols];

                for (int i = 0; i < this.numRows; i++) {
                    for (int j = 0; j < this.numCols; j++) {
                        first[i][j] = board[i][j];
                    }
                }
                int temp = board[1][0];
                first[1][0] = 0;
                first[0][0] = temp;
                SlideConfig first1 = new SlideConfig(first, numRows, numCols);
                neighbors.add(first1);
            }

            if (numCols > 1) {
                int[][] second = new int[numRows][numCols];

                for (int i = 0; i < this.numRows; i++) {
                    for (int j = 0; j < this.numCols; j++) {
                        second[i][j] = board[i][j];
                    }
                }
                int temp1 = board[0][1];
                second[0][1] = 0;
                second[0][0] = temp1;
                SlideConfig second1 = new SlideConfig(second, numRows, numCols);
                neighbors.add(second1);
            }


            return neighbors;
        }

        else if (topRCorner == true){
            int[][] first = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    first[i][j] = board[i][j];
                }
            }
            int temp = board[1][numCols - 1];
            first[1][numCols - 1] = 0;
            first[0][numCols - 1] = temp;

            int[][] second = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    second[i][j] = board[i][j];
                }
            }
            int temp1 = board[0][numCols - 2];
            second[0][numCols - 2] = 0;
            second[0][numCols - 1] = temp1;

            SlideConfig first1 = new SlideConfig(first, numRows, numCols);
            SlideConfig second1 = new SlideConfig(second, numRows, numCols);
            neighbors.add(first1);
            neighbors.add(second1);

            return neighbors;
        }

        else if (botLCorner == true){

            int[][] first = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    first[i][j] = board[i][j];
                }
            }
            int temp = board[numRows - 2][0];
            first[numRows - 2][0] = 0;
            first[numRows - 1][0] = temp;

            int[][] second = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    second[i][j] = board[i][j];
                }
            }
            int temp1 = board[numRows - 1][1];
            second[numRows - 1][1] = 0;
            second[numRows - 1][0] = temp1;

            SlideConfig first1 = new SlideConfig(first, numRows, numCols);
            SlideConfig second1 = new SlideConfig(second, numRows, numCols);
            neighbors.add(first1);
            neighbors.add(second1);

            return neighbors;
        }

        else if (botRCorner == true){

            int[][] first = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    first[i][j] = board[i][j];
                }
            }
            int temp = board[numRows - 2][numCols - 1];
            first[numRows - 2][numCols - 1] = 0;
            first[numRows - 1][numCols - 1] = temp;

            int[][] second = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    second[i][j] = board[i][j];
                }
            }
            int temp1 = board[numRows - 1][numCols - 2];
            second[numRows - 1][numCols - 2] = 0;
            second[numRows - 1][numCols - 1] = temp1;

            SlideConfig first1 = new SlideConfig(first, numRows, numCols);
            SlideConfig second1 = new SlideConfig(second, numRows, numCols);
            neighbors.add(first1);
            neighbors.add(second1);

            return neighbors;
        }

        else if (topRow == true){

            int[][] first = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    first[i][j] = board[i][j];
                }
            }
            int temp = board[rowPos][colPos - 1];
            first[rowPos][colPos - 1] = 0;
            first[rowPos][colPos] = temp;

            int[][] second = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    second[i][j] = board[i][j];
                }
            }
            int temp1 = board[rowPos][colPos + 1];
            second[rowPos][colPos + 1] = 0;
            second[rowPos][colPos] = temp1;

            int[][] third = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    third[i][j] = board[i][j];
                }
            }

            int temp2 = board[rowPos + 1][colPos];
            third[rowPos + 1][colPos] = 0;
            third[rowPos][colPos] = temp2;

            SlideConfig first1 = new SlideConfig(first, numRows, numCols);
            SlideConfig second1 = new SlideConfig(second, numRows, numCols);
            SlideConfig third1 = new SlideConfig(third, numRows, numCols);
            neighbors.add(first1);
            neighbors.add(second1);
            neighbors.add(third1);

            return neighbors;
        }

        else if (botRow == true){

            int[][] first = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    first[i][j] = board[i][j];
                }
            }
            int temp = first[rowPos][colPos + 1];
            first[rowPos][colPos + 1] = 0;
            first[rowPos][colPos] = temp;

            int[][] second = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    second[i][j] = board[i][j];
                }
            }
            int temp1 = second[rowPos][colPos - 1];
            second[rowPos][colPos - 1] = 0;
            second[rowPos][colPos] = temp1;

            int[][] third = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    third[i][j] = board[i][j];
                }
            }
            int temp2 = third[rowPos - 1][colPos];
            third[rowPos - 1][colPos] = 0;
            third[rowPos][colPos] = temp2;

            SlideConfig first1 = new SlideConfig(first, numRows, numCols);
            SlideConfig second1 = new SlideConfig(second, numRows, numCols);
            SlideConfig third1 = new SlideConfig(third, numRows, numCols);
            neighbors.add(first1);
            neighbors.add(second1);
            neighbors.add(third1);

            return neighbors;
        }

        else if (leftRow == true){

            int[][] first = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    first[i][j] = board[i][j];
                }
            }
            int temp = board[rowPos][colPos + 1];
            first[rowPos][colPos + 1] = 0;
            first[rowPos][colPos] = temp;

            int[][] second = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    second[i][j] = board[i][j];
                }
            }
            int temp1 = board[rowPos - 1][colPos];
            second[rowPos - 1][colPos] = 0;
            second[rowPos][colPos] = temp1;

            int[][] third = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    third[i][j] = board[i][j];
                }
            }
            int temp2 = board[rowPos + 1][colPos];
            third[rowPos + 1][colPos] = 0;
            third[rowPos][colPos] = temp2;

            SlideConfig first1 = new SlideConfig(first, numRows, numCols);
            SlideConfig second1 = new SlideConfig(second, numRows, numCols);
            SlideConfig third1 = new SlideConfig(third, numRows, numCols);
            neighbors.add(first1);
            neighbors.add(second1);
            neighbors.add(third1);

            return neighbors;
        }

        else if (rightRow == true){

            int[][] first = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    first[i][j] = board[i][j];
                }
            }
            int temp = board[rowPos][colPos - 1];
            first[rowPos][colPos - 1] = 0;
            first[rowPos][colPos] = temp;

            int[][] second = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    second[i][j] = board[i][j];
                }
            }
            int temp1 = board[rowPos - 1][colPos];
            second[rowPos - 1][colPos] = 0;
            second[rowPos][colPos] = temp1;

            int[][] third = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    third[i][j] = board[i][j];
                }
            }
            int temp2 = board[rowPos + 1][colPos];
            third[rowPos + 1][colPos] = 0;
            third[rowPos][colPos] = temp2;

            SlideConfig first1 = new SlideConfig(first, numRows, numCols);
            SlideConfig second1 = new SlideConfig(second, numRows, numCols);
            SlideConfig third1 = new SlideConfig(third, numRows, numCols);
            neighbors.add(first1);
            neighbors.add(second1);
            neighbors.add(third1);

            return neighbors;
        }

        else if (middle == true){

            int[][] first = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    first[i][j] = board[i][j];
                }
            }
            int temp = board[rowPos][colPos + 1];
            first[rowPos][colPos + 1] = 0;
            first[rowPos][colPos] = temp;

            int[][] second = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    second[i][j] = board[i][j];
                }
            }
            int temp1 = board[rowPos][colPos - 1];
            second[rowPos][colPos - 1] = 0;
            second[rowPos][colPos] = temp1;

            int[][] third = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    third[i][j] = board[i][j];
                }
            }
            int temp2 = board[rowPos + 1][colPos];
            third[rowPos + 1][colPos] = 0;
            third[rowPos][colPos] = temp2;

            int[][] fourth = new int[numRows][numCols];

            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < this.numCols; j++) {
                    fourth[i][j] = board[i][j];
                }
            }
            int temp3 = board[rowPos - 1][colPos];
            fourth[rowPos - 1][colPos] = 0;
            fourth[rowPos][colPos] = temp3;

            SlideConfig first1 = new SlideConfig(first, numRows, numCols);
            SlideConfig second1 = new SlideConfig(second, numRows, numCols);
            SlideConfig third1 = new SlideConfig(third, numRows, numCols);
            SlideConfig fourth1 = new SlideConfig(fourth, numRows, numCols);
            neighbors.add(first1);
            neighbors.add(second1);
            neighbors.add(third1);
            neighbors.add(fourth1);

            return neighbors;
        }
        else{
            return null;
        }

    }

    /**
     * Return a string representation of the puzzle
     *
     * @return
     */
    public String toString(){
        String s = "\n";
        // Using nested loops to print the array
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int checkZero = board[i][j];
                if (checkZero == 0){
                    s += ". ";
                }
                else {
                    s += checkZero + " ";
                }
            }
            s += "\n"; // Move to the next line after each row
        }
        return s;
    }

    /**
     * Return the board
     *
     * @return
     */
    @Override
    public int[][] returnBoard() {
        return board;
    }

    /**
     * Return number or rows the puzzle has
     *
     * @return
     */


    /**
     * Return number of cols the puzzle has
     *
     * @return
     */


    /**
     * Equals generated by intelliJ
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlideConfig that = (SlideConfig) o;
        return Arrays.deepEquals(board, that.board);
    }

    /**
     * hashCode generated by intelliJ
     * @return
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Return true if the row and column corresponds
     * to a square on the board that isn't empty
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isAPiece(int row, int col){

        if (row < 0 || row >= numRows) {

            return false;
        }
        if (col < 0 || col >= numCols){

            return false;
        }
        if (board[row][col] == 0) {
            return false;
        }

        return true;
    }


    /**
     * Return number or rows the puzzle has
     *
     * @return
     */
    public int getNumRows(){
        return numRows;
    }

    /**
     * Return number or columns the puzzle has
     *
     * @return
     */
    public int getNumCols(){return numCols;}

    /**
     * Return true if the first and second piece, as represent by the parameters,
     * are neighbors and thus can be swapped
     *
     * @param row
     * @param col
     * @param row1
     * @param col1
     * @return
     */
    public boolean isValid(int row, int col, int row1, int col1){
        // row, col
        if (row1 < 0 || row1 >= numRows) {
            return false;
        }
        if (col1 < 0 || col1 >= numCols){
            return false;
        }
        if (board[row][col] == 0) {
            return false;
        }
        if (board[row1][col1] != 0) {
            return false;
        }


        if (row == row1 && Math.abs(col - col1) == 1){
            return true;
        }
        else if (col == col1 && Math.abs(row - row1) == 1){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Create the board based on parameters
     *
     * @param row
     * @param col
     * @param board
     */
    public void nextStep(int row, int col, int[][] board){
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                this.board[i][j] = board[i][j];
            }
        }
    }

    /**
     * Return value at certain row and columns (assumes it's valid)
     * @param row
     * @param col
     * @return
     */
    public int getValue(int row, int col){

        return board[row][col];
    }

    /**
     * Set the value of a piece at the row and column to the value
     * given by the parameter
     *
     * @param row
     * @param col
     * @param value
     */
    public void setValue(int row, int col, int value){
        this.board[row][col] = value;
    }

    public int getZeroRowPos(){
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (board[i][j] == 0){
                    this.zeroRowPos = i;
                    this.zeroColPos = j;
                }
            }
        }
        return zeroRowPos;
    }

    public int getZeroColPos(){
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (board[i][j] == 0){
                    this.zeroRowPos = i;
                    this.zeroColPos = j;
                }
            }
        }
        return zeroColPos;
    }

}

