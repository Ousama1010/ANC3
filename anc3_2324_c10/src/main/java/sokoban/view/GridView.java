package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.ViewModel;

public class GridView extends GridPane {

    private static  int GRID_WIDTH = ViewModel.gridWidth();
    private static  int GRID_LENGHT = ViewModel.gridLenght();


    public GridView(DoubleProperty gridWidthProperty,  GridViewModel gridViewModel){
        setGridLinesVisible(false);
        setPadding(new Insets(20));

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100.0 / GRID_LENGHT);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.0 / GRID_WIDTH);
        DoubleBinding cellWidthProperty = gridWidthProperty.subtract(20 * 2).divide(14.5);

        for (int i = 0; i < 15; ++i) {
            getColumnConstraints().add(columnConstraints);
            getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < ViewModel.gridWidth(); ++i) {
            for (int j = 0; j < ViewModel.gridLenght(); ++j) {
                CellView caseView = new CellView(gridViewModel.getCellViewModel(i,j),  cellWidthProperty);
                add(caseView, j, i);
            }
        }

    }
}
