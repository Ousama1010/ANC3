package sokoban.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableSet;
import sokoban.model.Board;
import sokoban.model.Grid;
import sokoban.model.ObjectGame;

import java.util.List;

public class ViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;
    private IntegerProperty numberOfCells = new SimpleIntegerProperty(0);
    private IntegerProperty maxOfCells = new SimpleIntegerProperty(0);

    private StringProperty fillCountLabel = new SimpleStringProperty();


    public ViewModel(Board board) {
        this.board = board;
        gridViewModel = new GridViewModel(board);
        numberOfCells.bind(board.numberOfCellsProperty());
        maxOfCells.bind(board.maxOfCellsProperty());
        fillCountLabel.bind(Bindings.concat("Number of filled cells : ", numberOfCells," of ", maxOfCells));
    }

    public StringProperty fillCountLabelProperty() {
        return fillCountLabel;
    }
    public ListBinding<String> getErrors() {
        return board.listErrorsProperty();
    }

    public List<ObservableSet<ObjectGame>> getGridContent() {
        return board.getGridContent();
    }
    public GridViewModel getGridViewModel() {
        return gridViewModel;
    }

    public static int gridWidth() {
        return Grid.getGridWidth();
    }

    public static int gridLenght() {
        return Grid.getGridLength();
    }


    public void playGround(){
        board.setGroundObjectGame();
    }

    public void playTarget(){
        board.setTargetObjectGame();
    }

    public void playWall(){
        board.setWallObjectGame();
    }

    public void playPlayer(){
        board.setPlayerObjectGame();
    }

    public void playBox(){
        board.setBoxObjectGame();
    }
    public void resetBoard(int width,int weight) {
        board.reset( width ,  weight);
    }
    public void playGround(int row, int col) {
        board.playGround(row, col);
    }

    public void playTarget(int row, int col) {
        board.playTarget(row, col);
    }

    public void playPlayerAndTarget(int row, int col) {
        board.playPlayer(row, col);
        board.playTarget(row, col);

    }
    public void playBoxAndTarget(int row, int col) {
        board.playBox(row, col);
        board.playTarget(row, col);

    }


    public void playWall(int row, int col) {
        board.playWall(row, col);
    }

    public void playPlayer(int row, int col) {
        board.playPlayer(row, col);
    }

    public void playBox(int row, int col) {
        board.playBox(row, col);
    }
}
