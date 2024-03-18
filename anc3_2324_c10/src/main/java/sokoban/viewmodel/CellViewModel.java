package sokoban.viewmodel;

import javafx.collections.ObservableSet;
import sokoban.model.Board;
import sokoban.model.ObjectGame;

public class CellViewModel {
    private final int line, col;
    private final Board board;

    CellViewModel(int line, int col, Board board) {
        this.line = line;
        this.col = col;
        this.board = board;
    }

    public void play(){
        board.play(line, col);
    }
    public ObservableSet<ObjectGame> valueProperty() {
        return board.valueProperty(line, col);
    }

    public void addGeneralObject() {
        System.out.println("Ajout d'un objet général");
    }

    public void removeGeneralObject() {
        System.out.println("Suppression d'un objet général");
    }



}
