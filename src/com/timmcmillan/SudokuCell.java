package com.timmcmillan;

import java.util.ArrayList;

public class SudokuCell {


    private ArrayList<Integer> numberOptionsList;

    public SudokuCell(ArrayList<Integer> numberOptionsList) {
        this.numberOptionsList = numberOptionsList;
    }

    public void setNumberOptionsList(int cellNumber) {

        ArrayList<Integer> numberOptions = new ArrayList<Integer>();
        numberOptions.add(cellNumber);
        this.numberOptionsList = numberOptions;
    }

    public ArrayList<Integer> getNumberOptionsList() {
        return numberOptionsList;
    }

    public void setNumberOptionsList(ArrayList<Integer> numberOptionsList) {
        this.numberOptionsList = numberOptionsList;
    }
}


