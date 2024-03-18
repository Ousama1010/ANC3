package sokoban.model;

import javafx.beans.binding.ListBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableSet;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private ObjectGame objectGame=new Ground();

    private Grid grid = new Grid();
    private IntegerProperty numberOfCells = new SimpleIntegerProperty(0);
    private IntegerProperty maxOfCells = new SimpleIntegerProperty(0);


    public ListBinding<String> listErrorsProperty(){
        return grid.getListErrors();
    }

    public IntegerProperty numberOfCellsProperty() {
        return numberOfCells;
    }


    public IntegerProperty maxOfCellsProperty() {
        return maxOfCells;
    }




    public List<ObservableSet<ObjectGame>> getGridContent() {
        List<ObservableSet<ObjectGame>> gridContent = new ArrayList<>();

        for (int i = 0; i < grid.getGridWidth(); i++) {
            for (int j = 0; j < grid.getGridLength(); j++) {
                ObservableSet<ObjectGame> cellContent = grid.valueProperty(i, j);
                gridContent.add(cellContent);
            }
        }

        return gridContent;
    }
    public void updateFilledCells() {
        int count = 0;
        Cell[][] cells = grid.getGrid();
        for (int i = 0; i < grid.getGridWidth(); i++) {
            for (int j = 0; j < grid.getGridLength(); j++) {
                // Assume a cell is "filled" if it contains any object that isn't Ground
                if (cells[i][j].getObjects().stream().anyMatch(obj -> !(obj instanceof Ground))) {
                    count++;
                }
            }
        }
        numberOfCells.set(count);
    }



    public void setGroundObjectGame(){
        objectGame = new Ground();
    }

    public void setTargetObjectGame(){
        objectGame = new Target();
    }

    public void setWallObjectGame(){
        objectGame = new Wall();
    }
    public void setPlayerObjectGame(){
        objectGame = new Player();
    }

    public void setBoxObjectGame(){
        objectGame = new Box();
    }

    public void play (int line , int col){
        grid.play(line,col,objectGame);
        calculateNumber();
        updateFilledCells();

    }

    public ObservableSet<ObjectGame> valueProperty(int line, int col) {
        return grid.valueProperty(line, col);
    }
    public void reset(int width , int height) {
        grid = new Grid(width,height);
        calculateNumber();
        updateFilledCells();
        maxOfCells.set((width * height) / 2);
    }

    public int getTotalOfCells(){
        return grid.totalOfCells();
    }

    public int getNumberOfCells(){
        return grid.numberOfCells();
    }

    public void calculateNumber(){
        numberOfCells.set(getNumberOfCells());
        maxOfCells.set(getTotalOfCells());
    }




    public void playGround(int row, int col) {
        grid.play(row, col, new Ground());
    }

    public void playTarget(int row, int col) {
        grid.play(row, col, new Target());
    }

    public void playWall(int row, int col) {
        grid.play(row, col, new Wall());
    }

    public void playPlayer(int row, int col) {
        grid.play(row, col, new Player());
    }

    public void playBox(int row, int col) {
        grid.play(row, col, new Box());
    }


}
