package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.SetChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import sokoban.model.*;
import sokoban.viewmodel.CellViewModel;
import java.util.Set;

public class CellView extends StackPane {
    private static final Image ground = new Image("ground.png");
    private static final Image target = new Image("goal.png");
    private static final Image box = new Image("box.png");
    private static final Image player = new Image("player.png");
    private static final Image wall = new Image("wall.png");
    private final ImageView targetView = new ImageView();
    private final ImageView groundView = new ImageView();
    private final ImageView boxView = new ImageView();
    private final ImageView wallView = new ImageView();
    private final ImageView playerView = new ImageView();
    private final CellViewModel viewModel;
    public CellView(CellViewModel cellViewModel, DoubleBinding cellWidhtProperty){
        this.viewModel = cellViewModel;

        groundView.setPreserveRatio(true);
        groundView.fitWidthProperty().bind(cellWidhtProperty);
        targetView.setPreserveRatio(true);
        targetView.fitWidthProperty().bind(cellWidhtProperty);
        boxView.setPreserveRatio(true);
        boxView.fitWidthProperty().bind(cellWidhtProperty);
        wallView.setPreserveRatio(true);
        wallView.fitWidthProperty().bind(cellWidhtProperty);
        playerView.setPreserveRatio(true);
        playerView.fitWidthProperty().bind(cellWidhtProperty);
        setupMouseEvents();

        this.setOnMouseEntered(event -> {
            this.setOpacity(0.5);
        });
        this.setOnMouseExited(event -> {
            this.setOpacity(1.0);
        });

        getChildren().add(groundView);
        getChildren().add(playerView);
        getChildren().add(boxView);
        getChildren().add(wallView);
        getChildren().add(targetView);

        this.setOnMouseClicked(event -> viewModel.play());
        this.setOnMouseDragEntered(event -> viewModel.play());

        setImage(cellViewModel.valueProperty());

        viewModel.valueProperty().addListener((SetChangeListener<ObjectGame>) change -> {
            setImage(viewModel.valueProperty());
        });
    }
    public void setImage( Set<ObjectGame> objects) {
        boolean containsGround = false;
        boolean containsTarget = false;
        boolean containsBox = false;
        boolean containsWall = false;
        boolean containsPlayer = false;

        for (ObjectGame objectGame : objects) {
            if (objectGame instanceof Ground) {
                containsGround = true;
            } else if (objectGame instanceof Target) {
                containsTarget = true;
            } else if (objectGame instanceof Box) {
                containsBox = true;
            } else if (objectGame instanceof Wall) {
                containsWall = true;
            } else if (objectGame instanceof Player) {
                containsPlayer = true;
            }
        }

        setDisable(!(containsGround || containsTarget || containsBox || containsWall || containsPlayer));

        if (containsGround) {
            groundView.setImage(ground);
        }
        if (containsTarget) {
            targetView.setImage(target);
        }
        if (containsBox) {
            boxView.setImage(box);
        }
        if (containsWall) {
            wallView.setImage(wall);
        }
        if (containsPlayer) {
            playerView.setImage(player);
        }

        if (!containsGround) {
            groundView.setImage(null);
        }
        if (!containsTarget) {
            targetView.setImage(null);
        }
        if (!containsBox) {
            boxView.setImage(null);
        }
        if (!containsWall) {
            wallView.setImage(null);
        }
        if (!containsPlayer) {
            playerView.setImage(null);
        }

    }
    private void setupMouseEvents() {
        this.setOnDragDetected(event -> {
            this.startFullDrag();
            event.consume();
        });

        this.setOnMouseDragEntered(event -> {
            if (event.isPrimaryButtonDown()) {
                viewModel.addGeneralObject();
            } else if (event.isSecondaryButtonDown()) {
                viewModel.removeGeneralObject();
            }
            event.consume();
        });
    }
}
