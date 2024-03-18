package sokoban.model;

import javafx.beans.binding.ListBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Grid {
    final Cell[][] matrix;
    static  int GRID_WIDTH = 10;
    static  int GRID_LENGHT = 15;
    private final ListBinding<String> listErrors;
    public Grid() {
        this(GRID_WIDTH, GRID_LENGHT);
    }

    public Grid(int width, int length) {
        GRID_WIDTH = width;
        GRID_LENGHT=length;
        matrix = new Cell[width][length];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < length; ++j) {
                matrix[i][j] = new Cell();
            }
        }
        listErrors = new ListBinding<String>() {
            {
                for (int i = 0; i < GRID_WIDTH; ++i) {
                    for (int j = 0; j < GRID_LENGHT; ++j) {
                        bind(matrix[i][j].getObjects());
                    }
                }
            }
            @Override
            protected ObservableList<String> computeValue() {
                ObservableList<String> errors = FXCollections.observableArrayList();

                if (!checkPlayer()) {
                    errors.add("A player is required");
                }
                if (!checkTarget()) {
                    errors.add("At least one Target is required");
                }
                if(!checkBox()){
                    errors.add("At least one box");
                }
                if(!checkSame()){
                    errors.add("Number of boxes and target must be equals");
                }
                return errors;
            }
        };
    }

    public ListBinding<String> getListErrors(){
        return listErrors;
    }
    public static int getGridWidth() {
        return GRID_WIDTH;
    }
    public static int getGridLength() {
        return GRID_LENGHT;
    }
    void play(int line, int col, ObjectGame objectGame) {
        if (objectGame instanceof Player) {
            List<Pair<Integer, Integer>> playerPositions = new ArrayList<>();

            for (int i = 0; i < GRID_WIDTH; i++) {
                for (int j = 0; j < GRID_LENGHT; j++) {
                    ObservableSet<ObjectGame> list = matrix[i][j].getObjects();
                    for (ObjectGame obj : list) {
                        if (obj instanceof Player) {
                            playerPositions.add(new Pair<>(i, j));
                        }
                    }
                }
            }
            for (Pair<Integer, Integer> position : playerPositions) {
                int x = position.getKey();
                int y = position.getValue();
                matrix[x][y].clear();
                matrix[x][y].addObject(new Ground());
            }
        }
        matrix[line][col].addObject(objectGame);
    }
    ObservableSet<ObjectGame> valueProperty(int line, int col) {
        return matrix[line][col].getObjects();
    }
    public int numberOfCells(){
        int counter = 0;
        for (int i = 0; i < GRID_WIDTH; i++){
            for (int j = 0; j < GRID_LENGHT; j++){
                if (matrix[i][j].getSizeOfCell() > 1 ||!matrix[i][j].containsObject(new Ground())){
                    counter++;
                }
            }
        }
        return counter;
    }

    public int totalOfCells(){
        return (GRID_LENGHT * GRID_WIDTH)/2;
    }

    public Cell[][] getGrid() {
        return matrix;
    }

    public boolean checkPlayer(){
        for (int i = 0; i < GRID_WIDTH; i++){
            for (int j = 0; j < GRID_LENGHT; j++) {
                ObservableSet<ObjectGame> list = matrix[i][j].getObjects();
                for (ObjectGame object : list) {
                    if (object instanceof Player) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkTarget(){
        for (int i = 0; i < GRID_WIDTH; i++){
            for (int j = 0; j < GRID_LENGHT; j++) {
                ObservableSet<ObjectGame> list = matrix[i][j].getObjects();
                for (ObjectGame object : list) {
                    if (object instanceof Target) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean checkBox(){
        for (int i = 0; i < GRID_WIDTH; i++){
            for (int j = 0; j < GRID_LENGHT; j++) {
                ObservableSet<ObjectGame> list = matrix[i][j].getObjects();
                for (ObjectGame object : list) {
                    if (object instanceof Box) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean checkSame(){
        int counterBox = 0;
        int counterTarget = 0;
        for (int i = 0; i < GRID_WIDTH; i++){
            for (int j = 0; j < GRID_LENGHT; j++) {
                ObservableSet<ObjectGame> list = matrix[i][j].getObjects();
                for (ObjectGame object : list) {
                    if (object instanceof Box) {
                        counterBox++;
                    }
                    if (object instanceof Target) {
                        counterTarget++;
                    }
                }
            }
        }
        return counterBox == counterTarget;
    }
}
