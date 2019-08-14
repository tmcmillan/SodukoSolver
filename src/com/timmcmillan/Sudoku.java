package com.timmcmillan;

import java.util.ArrayList;
import java.util.Scanner;

public class Sudoku {

    Scanner scanner = new Scanner(System.in);

    /**
     * allows the user to input the values of the cells which are known
     * given this input, attempts to solve the sudouko grid
     */
    public void newSudoku() {

        //intialise the arrays which will be used to input the values for the cells which are known at the outset
        ArrayList<Integer> rowIndex = new ArrayList<>();
        ArrayList<Integer> columnIndex = new ArrayList<>();
        ArrayList<Integer> cellValue = new ArrayList<>();

        System.out.println("Welcome to the game!");


        // taking each row in turn, the user is asked for the column index and the value of the known cells.
        // the user enters -1 when there are no more known values for that row.
        // the input must be an integer between 1 and 9, or -1
        // at the end of the for loop, we have 3 arrays, corresponding to the rowIndex, columnIndex and cellValues.

            for(int i=1; i<=9; i++) {

                boolean inputComplete = false;
                while (!inputComplete) {

                    boolean validColumnInput = false;
                    while (!validColumnInput) {
                        System.out.println("\"Enter the index of the column of the known number in row " + i + ". Enter -1 if all of the numbers for this row have been entered");
                        if (!scanner.hasNextInt()) {
                            System.out.println("Please enter an integer");
                            scanner.nextLine();
                        } else {
                            int column = scanner.nextInt();
                            scanner.nextLine();

                            if (column == -1) {
                                validColumnInput = true;
                                inputComplete = true;
                            } else if ((column <= 0 && column != -1) || column > 9) {
                                System.out.println("please enter either an integer between 1 and 9, or -1 to indicate all number have been entered");
                            } else {

                                rowIndex.add(i);
                                columnIndex.add(column);
                                validColumnInput = true;
                            }
                        }

                    }

                    if (!inputComplete) {
                        boolean validNumberInput = false;
                        while (!validNumberInput) {
                            System.out.println("Enter the value of the known number");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Please enter an integer");
                                scanner.nextLine();
                            } else {
                                int value = scanner.nextInt();
                                scanner.nextLine();

                                if (value <= 0 || value > 9) {
                                    System.out.println("please enter either an integer between 1 and 9");
                                } else {
                                    cellValue.add(value);
                                    validNumberInput = true;
                                }
                            }
                        }
                    }
                }
            }

            //call the solveSudoku method using the known cellValues as the input.
        solveSoduko(rowIndex, columnIndex, cellValue);
    }

    /**
     * given the user input, attempts to return the solution
     * @param rowIndex the indexes of the rows of the known cells
     * @param columnIndex the indexes of the columns of the known cells
     * @param cellValue the values of the known cells
     */
        public void solveSoduko(ArrayList<Integer> rowIndex, ArrayList<Integer> columnIndex, ArrayList<Integer> cellValue) {

            // create a 2d array of SudokuCells, initialising the positions given the user input
        SudokuCell[][] sodukoGrid = setInitialNumberPositions(rowIndex, columnIndex, cellValue);

        // draw the board of the initial positions.
        drawGrid(sodukoGrid);

        int iter=0;
        boolean complete = false;

        // continue to attempt to solve the Sudoku until it is complete, or 50 iterations have been tried
        while (!complete && iter<50) {

            int completeCells = 0;

            //update the cell possibilities by row, column and then box
            updateCellPossibilitiesByRow(sodukoGrid);
            updateCellPossibilitiesByColumn(sodukoGrid);
            updateCellPossibilitiesByBox(sodukoGrid);
            iter++;

            // calculate how many cells are known
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (knownValue(sodukoGrid[i][j])) {
                        completeCells++;
                    }
                }
            }

            // if all 81 cells are known (have only one possible value, the puzzle is complete
            if (completeCells == 81) {
                System.out.println("Sudoku complete!");
                drawGrid(sodukoGrid);
                complete = true;
            }

            // if we have iterated 50 times without finding a solution, stop and tell the user know that we are having difficulties
            if(iter==50) {
                System.out.println("Wow, super fiendish! I'm stumped. Double check the input is correct! ");
                drawGrid(sodukoGrid);
            }
        }
    }


    /**
     *
     * @param rowIndex the indexes of the rows of the known cells
     * @param columnIndex the indexes of the columns of the known cells
     * @param cellValues the values of the known cells
     * @return a 2d array of SudokuCells. These SudokuCells have parameter numberOptionList, an arrayList of potenial values.
     * This is either equal to a given integer, if the value is known at the start, or an arrayList of integers from 1 to 9.
     */
    public SudokuCell[][] setInitialNumberPositions(ArrayList<Integer> rowIndex, ArrayList<Integer> columnIndex, ArrayList<Integer> cellValues) {


        SudokuCell[][] sodukoGrid = new SudokuCell[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sodukoGrid[i][j] = new SudokuCell(arrayListFrom1to9());
            }
        }

        for (int k = 0; k < rowIndex.size(); k++) {
            sodukoGrid[rowIndex.get(k) - 1][columnIndex.get(k) - 1].setNumberOptionsList(cellValues.get(k));
        }

        return sodukoGrid;
    }

    /**
     *
     * @return an arrayList of integers from 1 to 9 inclusive.
     */
    public ArrayList<Integer> arrayListFrom1to9(){
        ArrayList<Integer> arrayListFrom1to9 = new ArrayList<>();
        for (int k = 1; k <= 9; k++) {
            arrayListFrom1to9.add(k);
        }
        return arrayListFrom1to9;
    }

    /**
     *
     * @param sodukoGrid  2d array of Sudoku Cells, each if their ArrayList of potential Integers
     * draws the sudoku board, containing the values of the known cells. All cells that are unknown (i.e. >1 potential value) are blank.
     *
     */
    public void drawGrid(SudokuCell[][] sodukoGrid) {

        System.out.println("                                                                                                               \n" +
                "               1        2        3         4        5        6         7        8        9\n" +
                "       ------------------------------------------------------------------------------------------\n" +
                "       * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                System.out.println("       ------------------------------------------------------------------------------------------\n" +
                        "       |*|        |        |        |*|        |        |        |*|        |        |        |*|\n" +
                        "    " + (3 * j + i + 1) + "  |*|   " + cellNumberAsString(sodukoGrid, (3 * j + i), 0) + "    |   " + cellNumberAsString(sodukoGrid, 3 * j + i, 1) + "    |   " + cellNumberAsString(sodukoGrid, 3 * j + i, 2) + "    |*|   " + cellNumberAsString(sodukoGrid, 3 * j + i, 3) + "    |    " + cellNumberAsString(sodukoGrid, 3 * j + i, 4) + "   |   " + cellNumberAsString(sodukoGrid, 3 * j + i, 5) + "    |*|   " + cellNumberAsString(sodukoGrid, 3 * j + i, 6) + "    |   " + cellNumberAsString(sodukoGrid, 3 * j + i, 7) + "    |   " + cellNumberAsString(sodukoGrid, 3 * j + i, 8) + "    |*|\n" +
                        "       |*|        |        |        |*|        |        |        |*|        |        |        |*|");
            }
            System.out.println("       ------------------------------------------------------------------------------------------");
            System.out.println("       * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        }
        System.out.println("       ------------------------------------------------------------------------------------------\n");

    }

    /**
     *
     * @param sudokuGrid a 2d array of SudokuCells
     * @param rowIndex the index of the row of interest
     * @param columnIndex the index of the column of interest
     * @return if the value of the cell is known (only one possible integer), return that integer as a string
     * if the value of the cell is unknown (>1 possible integer), return " ".
     */
    public String cellNumberAsString(SudokuCell[][] sudokuGrid, int rowIndex, int columnIndex) {

        String cellNumberAsString = " ";

        if (knownValue(sudokuGrid[rowIndex][columnIndex])) {
            cellNumberAsString = cellValue(sudokuGrid[rowIndex][columnIndex]).toString();
        }

        return cellNumberAsString;
    }

    /**
     *
     * @param cell a given SudokuCell
     * @return whether the value of the cell is known (i.e. only one possibleNumberValue)
     */
    public boolean knownValue(SudokuCell cell) {
        boolean knownValue = false;
        if(cell.getNumberOptionsList().size()==1){
            knownValue = true;
        }
        return knownValue;
    }

    /**
     * for a given cell for which has only one possible value (a "known cell"), return said value.
     * @param cell
     * @return for a given cell for which has only one possible value (a "known cell"), return said value.
     */
    public Integer cellValue(SudokuCell cell){
        Integer cellValue = cell.getNumberOptionsList().get(0);
        return cellValue;
    }

    /**
     *
     * @param sodukoGrid 2d array of SudokuCells
     * @return 2d array of SudokuCells, the numberOptionsList of which have been updated
     */
    public SudokuCell[][] updateCellPossibilitiesByRow(SudokuCell[][] sodukoGrid) {

        // for each of the cells in the grid
        // if one cell in a row has a known value, that known value should be removed
        // from the list of possible values for all other cells in that row
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (knownValue(sodukoGrid[i][j])) {
                    for (int k = 0; k < 9; k++) {

                        if ((k != j) && sodukoGrid[i][k].getNumberOptionsList().contains(cellValue(sodukoGrid[i][j]))) {

                            sodukoGrid[i][k].getNumberOptionsList().remove(cellValue(sodukoGrid[i][j]));

                        }
                    }
                } else {
                    // if the value of a cell is not known (length numberOfPossibleValues >1)
                    // check whether, for the each of the value in the list of possible values of this cell,
                    // whether there are any values that are unique to that cell
                    // for example, if a cell has possible values {2, 3, 8}, and no other cells in that row have the potential value of 8,
                    // the value of that cell must be 8.

                    for (int x = 0; x < sodukoGrid[i][j].getNumberOptionsList().size(); x++) {
                        boolean uniqueNumber = true;
                        for (int y = 0; y < 9; y++) {
                            if ((y != j) && sodukoGrid[i][y].getNumberOptionsList().contains(sodukoGrid[i][j].getNumberOptionsList().get(x))) {
                                uniqueNumber = false;
                            }
                        }
                        if (uniqueNumber) {
                            int newNumber = sodukoGrid[i][j].getNumberOptionsList().get(x);
                            sodukoGrid[i][j].setNumberOptionsList(newNumber);
                        }
                    }

                }
            }
        }
        return sodukoGrid;
    }

    /**
     *
     * @param sodukoGrid 2d array of SudokuCells
     * @return 2d array of SudokuCells, the numberOptionsList of which have been updated
     */
    public SudokuCell[][] updateCellPossibilitiesByColumn(SudokuCell[][] sodukoGrid) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (knownValue(sodukoGrid[i][j])) {
                    for (int k = 0; k < 9; k++) {
                        if ((k != i) && sodukoGrid[k][j].getNumberOptionsList().contains(cellValue(sodukoGrid[i][j]))) {
                            sodukoGrid[k][j].getNumberOptionsList().remove(cellValue(sodukoGrid[i][j]));
                        } else {
                        }
                    }
                } else {
                    for (int x = 0; x < sodukoGrid[i][j].getNumberOptionsList().size(); x++) {
                        boolean uniqueNumber = true;
                        for (int y = 0; y < 9; y++) {
                            if ((y != i) && sodukoGrid[y][j].getNumberOptionsList().contains(sodukoGrid[i][j].getNumberOptionsList().get(x))) {
                                uniqueNumber = false;
                            }
                        }
                        if (uniqueNumber) {
                            int newNumber = sodukoGrid[i][j].getNumberOptionsList().get(x);
                            sodukoGrid[i][j].setNumberOptionsList(newNumber);
                        }
                    }

                }
            }
        }
        return sodukoGrid;
    }

    /**
     *
     * @param sodukoGrid 2d array of SudokuCells
     * @return 2d array of SudokuCells, the numberOptionsList of which have been updated
     */
    public SudokuCell[][] updateCellPossibilitiesByBox(SudokuCell[][] sodukoGrid) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int boxRow = i / 3;
                int boxCol = j / 3;
                if (knownValue(sodukoGrid[i][j])) {
                    for (int k = boxRow * 3; k < boxRow * 3 + 3; k++) {
                        for (int l = boxCol * 3; l < boxCol * 3 + 3; l++) {
                            if ((k != i && l != j) && sodukoGrid[k][l].getNumberOptionsList().contains(cellValue(sodukoGrid[i][j]))) {

                                sodukoGrid[k][l].getNumberOptionsList().remove(cellValue(sodukoGrid[i][j]));
                            }
                        }
                    }
                } else {
                    for (int x = 0; x < sodukoGrid[i][j].getNumberOptionsList().size(); x++) {
                        boolean uniqueNumber = true;
                        for (int k = boxRow * 3; k < boxRow * 3 + 3; k++) {
                            for (int l = boxCol * 3; l < boxCol * 3 + 3; l++) {
                                if (!(k == i && l == j) && sodukoGrid[k][l].getNumberOptionsList().contains(sodukoGrid[i][j].getNumberOptionsList().get(x))) {
                                    uniqueNumber = false;
                                }
                            }
                        }
                                if (uniqueNumber) {
                                    int newNumber = sodukoGrid[i][j].getNumberOptionsList().get(x);
                                    sodukoGrid[i][j].setNumberOptionsList(newNumber);
                                }
                            }
                        }
                }
            }

            return sodukoGrid;
        }


}
