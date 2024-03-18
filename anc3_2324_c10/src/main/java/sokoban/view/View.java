package sokoban.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableSet;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sokoban.model.*;
import sokoban.viewmodel.ViewModel;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;



import java.io.*;
import java.util.List;
import java.util.Optional;

public class View extends BorderPane {

    private final ViewModel viewModel;
    private static final int SCENE_MIN_WIDTH = 700;
    private static final int SCENE_MIN_HEIGHT = 600;
    private final DoubleProperty gridWidthProperty = new SimpleDoubleProperty(250);
    private ToolView toolView;
    private Label errorLabel;
    private Label fillCountLabel = new Label("");
    private  VBox topContainer = new VBox();
    public View(Stage primaryStage, ViewModel viewModel){
        this.viewModel = viewModel;
        start(primaryStage);
    }
    public void start(Stage stage) {
        configMainComponents(stage);
        Scene scene = new Scene(this, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
    }

    private void configMainComponents(Stage stage) {
        stage.titleProperty().bind(new SimpleStringProperty("Sokoban"));
        topContainer = new VBox();
        topContainer.setSpacing(10);
        addFileMenu(stage, topContainer);
        addFillCountLabel(topContainer);
        addErrorLabel(topContainer);
        setTop(topContainer);
        toolMenu();
        createGrid();
    }


    private void addFileMenu(Stage stage, VBox container) {
        Menu fileMenu = new Menu("File");

        MenuItem newMenuItem = new MenuItem("New");
        MenuItem saveAsMenuItem = new MenuItem("Save As...");
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem exitMenuItem = new MenuItem("Exit");

        newMenuItem.setOnAction(event -> handleNew(stage));
        saveAsMenuItem.setOnAction(event -> handleSaveAs(stage));
        openMenuItem.setOnAction(event -> handleOpen(stage));
        exitMenuItem.setOnAction(event -> handleExit());

        fileMenu.getItems().addAll(newMenuItem, saveAsMenuItem, openMenuItem, new SeparatorMenuItem(), exitMenuItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        container.getChildren().add(menuBar);
    }

    private void handleNew(Stage stage) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("New Grid Dimensions");
        dialog.setHeaderText("Enter the new dimensions for the grid:");

        TextField widthField = new TextField();
        widthField.setPromptText("Width");
        TextField heightField = new TextField();
        heightField.setPromptText("Height");

        Label widthErrorLabel = new Label();
        Label heightErrorLabel = new Label();

        widthErrorLabel.setTextFill(Color.RED);
        heightErrorLabel.setTextFill(Color.RED);

        VBox content = new VBox(8, widthField, widthErrorLabel, heightField, heightErrorLabel);
        dialog.getDialogPane().setContent(content);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        Button okBtn = (Button) dialog.getDialogPane().lookupButton(okButton); // Récupérer le bouton OK
        okBtn.setDisable(true); // Désactiver le bouton OK initialement

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return widthField.getText() + "," + heightField.getText();
            }
            return null;
        });

        widthField.textProperty().addListener((observable, oldValue, newValue) -> {
            String errorMessage = validateDimension(newValue);
            widthErrorLabel.setText(errorMessage);
            checkValidityAndEnableButton(okBtn, widthErrorLabel.getText(), heightErrorLabel.getText());
        });

        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            String errorMessage = validateDimension(newValue);
            heightErrorLabel.setText(errorMessage);
            checkValidityAndEnableButton(okBtn, widthErrorLabel.getText(), heightErrorLabel.getText());
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(dimensions -> {
            String[] dimensionParts = dimensions.split(",");
            if (dimensionParts.length == 2) {
                try {
                    int width = Integer.parseInt(dimensionParts[0]);
                    int height = Integer.parseInt(dimensionParts[1]);

                    widthErrorLabel.setText("");
                    heightErrorLabel.setText(""); // Efface les messages d'erreur précédents

                    if (width < 10 || width > 50) {
                        widthErrorLabel.setText("Width must be between 10 and 50");
                    }

                    if (height < 10 || height > 50) {
                        heightErrorLabel.setText("Height must be between 10 and 50");
                    }

                    if (widthErrorLabel.getText().isEmpty() && heightErrorLabel.getText().isEmpty()) {
                        viewModel.resetBoard(width, height);
                        clearGrid();
                        createGrid();
                        clearErrorLabel();
                        addErrorLabel(topContainer);
                    }
                } catch (NumberFormatException e) {
                }
            }
        });
    }
    private void clearErrorLabel() {
        topContainer.getChildren().remove(errorLabel);
    }
    private void checkValidityAndEnableButton(Button okButton, String widthErrorMessage, String heightErrorMessage) {
        okButton.setDisable(!widthErrorMessage.isEmpty() || !heightErrorMessage.isEmpty());
    }

    private String validateDimension(String value) {
        try {
            int dimension = Integer.parseInt(value);
            if (dimension < 10 || dimension > 50) {
                return "Dimension must be between 10 and 50";
            } else {
                return "";
            }
        } catch (NumberFormatException e) {
            return "Invalid input";
        }
    }

    private void clearGrid() {
        setCenter(null);
    }

    private void handleSaveAs(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XSB files (*.xsb)", "*.xsb"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                List<ObservableSet<ObjectGame>> gridContent = viewModel.getGridContent();
                int currentRow = 0;

                for (ObservableSet<ObjectGame> cellContent : gridContent) {
                    boolean hasTarget = false;
                    boolean hasBox = false;
                    boolean hasPlayer = false;
                    boolean hasWall = false;
                    boolean hasGround = false;

                    for (ObjectGame object : cellContent) {
                        if (object instanceof Target) {
                            hasTarget = true;
                        } else if (object instanceof Box) {
                            hasBox = true;
                        } else if (object instanceof Player) {
                            hasPlayer = true;
                        } else if (object instanceof Wall) {
                            hasWall = true;
                        } else if (object instanceof Ground) {
                            hasGround = true;
                        }
                    }

                    if (hasTarget && hasBox) {
                        fileWriter.write("*");
                    } else if (hasTarget && hasPlayer) {
                        fileWriter.write("+");
                    } else if (hasTarget) {
                        fileWriter.write(".");
                    } else if (hasBox) {
                        fileWriter.write("$");
                    } else if (hasPlayer) {
                        fileWriter.write("@");
                    } else if (hasWall) {
                        fileWriter.write("#");
                    } else if (hasGround) {
                        fileWriter.write(" ");
                    } else {
                        fileWriter.write(" "); // Si la cellule est vide
                    }

                    currentRow++;
                    if (currentRow == ViewModel.gridLenght()) {
                        currentRow = 0;
                        fileWriter.write("\n");
                    }
                }

                fileWriter.close();
                System.out.println("Grid saved successfully as: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleOpen(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XSB File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XSB files (*.xsb)", "*.xsb"));
        File file = fileChooser.showOpenDialog(stage);

        int numRows = 0;
        int numCols = 0;

        if (file != null) {
            try {
                numRows = countRows(file);
                numCols = countColumns(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        viewModel.resetBoard(numRows, numCols);
        clearGrid();
        createGrid();

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int row = 0;
                while ((line = reader.readLine()) != null && row < numRows) {
                    for (int col = 0; col < line.length() && col < numCols; col++) {
                        char character = line.charAt(col);
                        switch (character) {
                            case '.':
                                viewModel.playTarget(row, col);
                                break;
                            case '+':
                                viewModel.playPlayerAndTarget(row, col);
                                break;
                            case '$':
                                viewModel.playBox(row, col);
                                break;
                            case '#':
                                viewModel.playWall(row, col);
                                break;
                            case ' ':
                                viewModel.playGround(row, col);
                                break;
                            case '@':
                                viewModel.playPlayer(row, col);
                                break;
                            case '*':
                                viewModel.playBoxAndTarget(row, col);
                                break;
                            default:

                                break;
                        }
                    }
                    row++;
                }   addErrorLabel(topContainer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private int countRows(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int lines = 0;
            while (reader.readLine() != null) lines++;
            return lines;
        }
    }
    private int countColumns(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int maxColumns = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                int currentColumns = line.length();
                if (currentColumns > maxColumns) {
                    maxColumns = currentColumns;
                }
            }
            return maxColumns;
        }
    }


    private void handleExit() {
        Platform.exit();
    }

    private void addErrorLabel(VBox container) {
        errorLabel= new Label();
        errorLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            StringBuilder sb = new StringBuilder();
            for (String error : viewModel.getErrors()) {
                sb.append(error).append("\n");
            }
            return sb.toString();
        }, viewModel.getErrors()));

        errorLabel.setTextFill(Color.RED);
        errorLabel.setStyle("-fx-font-weight: bold;");

        topContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        container.getChildren().add(errorLabel);

        VBox.setMargin(errorLabel, new Insets(10, 10, 10, 10));
        container.setAlignment(Pos.CENTER);
    }


    private void addFillCountLabel(VBox container) {
        fillCountLabel = new Label();
        fillCountLabel.textProperty().bind(viewModel.fillCountLabelProperty());
        fillCountLabel.setTextFill(Color.BLACK);
        fillCountLabel.setStyle("-fx-font-weight: bold;");

        VBox filledCountVbox = new VBox(fillCountLabel);
        container.getChildren().add(filledCountVbox);
        VBox.setMargin(fillCountLabel, new Insets(10, 10, 10, 10));
        filledCountVbox.setAlignment(Pos.CENTER);
    }
    private void toolMenu(){
        toolView = new ToolView(viewModel);
        setLeft(toolView);
    }
    private void createGrid() {
        GridView gridView = new GridView(gridWidthProperty,viewModel.getGridViewModel());

        gridView.minHeightProperty().bind(gridWidthProperty);
        gridView.minWidthProperty().bind(gridWidthProperty);
        gridView.maxHeightProperty().bind(gridWidthProperty);
        gridView.maxWidthProperty().bind(gridWidthProperty);
        gridWidthProperty.bind(Bindings.min(widthProperty().subtract(200), heightProperty()));
        setCenter(gridView);
    }
}
