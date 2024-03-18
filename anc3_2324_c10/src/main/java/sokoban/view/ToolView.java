package sokoban.view;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import sokoban.viewmodel.ViewModel;

public class ToolView extends VBox {
    private final ViewModel viewModel;
    public ToolView(ViewModel viewModel){
        this.viewModel=viewModel;
        configureToolMenu();
    }
    private void configureToolMenu() {
        Button groundButton = new Button();
        Button targetButton = new Button();
        Button wallButton = new Button();
        Button playerButton = new Button();
        Button boxButton = new Button();

        Image grass = new Image("ground.png");
        Image target = new Image("goal.png");
        Image wall = new Image("wall.png");
        Image player = new Image("player.png");
        Image box = new Image("box.png");

        ImageView viewGround = new ImageView(grass);
        ImageView viewTarget = new ImageView(target);
        ImageView viewWall = new ImageView(wall);
        ImageView viewPlayer = new ImageView(player);
        ImageView viewBox = new ImageView(box);

        viewGround.setPreserveRatio(true);
        viewTarget.setPreserveRatio(true);
        viewWall.setPreserveRatio(true);
        viewPlayer.setPreserveRatio(true);
        viewBox.setPreserveRatio(true);

        getChildren().addAll(groundButton, targetButton, wallButton, playerButton, boxButton);

        groundButton.setDisable(false);
        targetButton.setDisable(false);
        wallButton.setDisable(false);
        playerButton.setDisable(false);
        boxButton.setDisable(false);

        groundButton.setGraphic(viewGround);
        targetButton.setGraphic(viewTarget);
        wallButton.setGraphic(viewWall);
        playerButton.setGraphic(viewPlayer);
        boxButton.setGraphic(viewBox);

        groundButton.setOnMouseClicked(event -> {
            viewModel.playGround();
        });
        targetButton.setOnMouseClicked(event -> {
            viewModel.playTarget();
        });
        wallButton.setOnMouseClicked(event -> {
            viewModel.playWall();
        });
        playerButton.setOnMouseClicked(event -> {
            viewModel.playPlayer();
        });
        boxButton.setOnMouseClicked(event -> {
            viewModel.playBox();
        });
    }
}
