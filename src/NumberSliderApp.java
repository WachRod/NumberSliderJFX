import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class NumberSliderApp extends Application {
    public static  void main (String[] args ) {
        launch(args);
    }
    @Override
    public void start(Stage stage)  {
        Scene scene = new Scene(createBoard(),450,600);
        scene.getStylesheets().add(getClass().getResource("NumberSlider.css").toExternalForm());
        stage.setTitle("Number Puzzle Game.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    private Pane createBoard() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        Text txtTitle = new Text("Number Slider");
        txtTitle.setId("title");
        Text txtVersion = new Text("Version 0.0.1");
        txtVersion.getStyleClass().add("text-version");
        VBox  vboxForTitle = new VBox(10);
        vboxForTitle.setAlignment(Pos.CENTER);
        vboxForTitle.getChildren().addAll(txtTitle,txtVersion);
        SliderBoard sliderBoard = new SliderBoard();
        sliderBoard.play();

        // Size of Board
        ToggleGroup sizeGroup = new ToggleGroup();
        RadioButton size3x3 = new RadioButton("3 x 3");
        size3x3.setToggleGroup(sizeGroup);
        size3x3.setId("radioButton-txt");
        RadioButton size4x4 = new RadioButton("4 x 4");
        size4x4.setToggleGroup(sizeGroup);
        size4x4.requestFocus();
        size4x4.setSelected(true);
        size4x4.setId("radioButton-txt");
        RadioButton size5x5 = new RadioButton("5 x 5");
        size5x5.setToggleGroup(sizeGroup);
        size5x5.setId("radioButton-txt");
// Add a change to listener
        sizeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> observable,
                                Toggle oldObservable, Toggle newObservable) {
                if (size3x3.isSelected()){
                    sliderBoard.setNumberOfTilesPerRow(3);
                    sliderBoard.setNumberOfTilesPerColumn(3);
                }
                if (size4x4.isSelected()) {
                    sliderBoard.setNumberOfTilesPerRow(4);
                    sliderBoard.setNumberOfTilesPerColumn(4);
                }
                if (size5x5.isSelected()) {
                    sliderBoard.setNumberOfTilesPerRow(5);
                    sliderBoard.setNumberOfTilesPerColumn(5);
                }
                sliderBoard.newGame();
            }
        });
        Label lblBoardSize = new Label("Board Size: ");
        lblBoardSize.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sliderBoard.setSuper_Player_Mode();
            }
        });
        lblBoardSize.getStyleClass().add("label-topic");
        VBox vBoxForSizeGroup = new VBox(10,lblBoardSize, size3x3,size4x4 ,size5x5);
        vBoxForSizeGroup.setPadding(new Insets(0,0,0,10));
        vBoxForSizeGroup.setAlignment(Pos.TOP_CENTER);
        // Target pattern
        CheckBox verticalCheck = new CheckBox("Vertical Direction");
        verticalCheck.setSelected(false);
        verticalCheck.setIndeterminate(false);
        verticalCheck.setPrefWidth(150);
        verticalCheck.setId("checkBox-txt");
        verticalCheck.setOnAction( new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)       {
                if (verticalCheck.isSelected())
                    sliderBoard.setVertical_Direction(true);
                else {
                    sliderBoard.setVertical_Direction(false);
                }
                sliderBoard.newGame();
            }

        });
        // Tile Shuffling.
        CheckBox shuffleCheck = new CheckBox("Tiles shuffling.");
        shuffleCheck.setSelected(false);
        shuffleCheck.setIndeterminate(false);
        shuffleCheck.setPrefWidth(150);
        shuffleCheck.setId("checkBox-txt");
        shuffleCheck.setOnAction( new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)       {
                if (shuffleCheck.isSelected())
                    sliderBoard.setShuffling_Tile(true);
                else {
                    sliderBoard.setShuffling_Tile(false);
                }
                sliderBoard.newGame();
            }
        });
        Label lblTargetPattern = new Label("Target pattern:");
        lblTargetPattern.getStyleClass().add("label-topic");
        Label lblShuffleCheck = new Label("Starting Pattern:");
        lblShuffleCheck.getStyleClass().add("label-topic");
        VBox vBoxForPattern = new VBox(10,lblShuffleCheck,shuffleCheck,lblTargetPattern, verticalCheck);
        vBoxForPattern.setAlignment(Pos.TOP_CENTER);
        Button btnNewGame = new Button("New Game");
        Button btnEndGame = new Button("Quit");
         btnNewGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sliderBoard.newGame();
            }
        });
        btnEndGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });
        VBox vBoxForButton = new VBox(20);
        vBoxForButton.setPadding(new Insets(0,0,0,20));
        vBoxForButton.setAlignment(Pos.TOP_RIGHT);
        vBoxForButton.getChildren().addAll(btnNewGame,btnEndGame);
        HBox hBoxAtBottom = new HBox(20);
        hBoxAtBottom.getChildren().addAll(vBoxForSizeGroup,vBoxForPattern,vBoxForButton);
        root.getChildren().addAll(vboxForTitle,sliderBoard,hBoxAtBottom);
        return root;
    }
}