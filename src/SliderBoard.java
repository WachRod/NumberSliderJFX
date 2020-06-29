import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Random;

class SliderBoard extends StackPane {

    private  int numberOfTilesPerRow = 4;
    private  int numberOfTilesPerColumn = 4;
    private final int TILE_WIDTH = 60;
    private final int TILE_HEIGHT = 60;

    private Tile [] tile;
    private  int numberOfTiles;
    private int [] digitPosition;
    private Image[] tileImage;

    private Point2D[] coordinates;
    private Boolean Shuffling_Tile = false;
    private Boolean Vertical_Direction = false;
    private Boolean Game_Completed = false;
    private Boolean Super_Player_Mode = false;
    private int movingCount= 0;
    private long startingTime ;
    private int [] targetPattern;
    private AudioClip slidingSound;
    private AudioClip clapping;

    private Canvas canvas= new Canvas();
    private GraphicsContext gc = canvas.getGraphicsContext2D();

    SliderBoard(){
       loadImages();
       slidingSound = new AudioClip(this.getClass().getResource("/sound/click.mp3").toExternalForm());
       clapping = new AudioClip(this.getClass().getResource("/sound/clapping2.mp3").toExternalForm());
        newGame();
     }
    void play() {
        getChildren().add(canvas);
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int clickedPosition = findClickedPosition(event.getX(), event.getY());
                int spacePosition = tile[0].getTileRank();
                if  (!Game_Completed) {
                   if (isValidClicked(clickedPosition,spacePosition) || Super_Player_Mode){
                    digitPosition[spacePosition] = digitPosition[clickedPosition];
                    digitPosition[clickedPosition] = 0;
                    tile[digitPosition[clickedPosition]].setTileRank(spacePosition);
                    tile[0].setTileRank(clickedPosition);
                    slidingSound.play();
                    drawTiles(coordinates, digitPosition);
                    movingCount++;
                    if (movingCount ==1) startingTime = System.currentTimeMillis();
                    if (isGameOver()) {
                        Game_Completed = true;
                        clapping.play();
                        displayMessage();

                    }
                }
                }
            }
        });
    }
    private void loadImages() {
        int MAX_NUMBER_OF_IMAGE = 25;
        tileImage = new Image[MAX_NUMBER_OF_IMAGE];
        for (int i = 0; i < MAX_NUMBER_OF_IMAGE; i++) {
            tileImage[i] = new Image("/img/"+i + ".gif");
        }
    }
    private void createTiles() {

        tile = new Tile[numberOfTiles];
        for (int i = 0; i < numberOfTiles; i++) {
          //  tileImage[i] = new Image(i + ".gif");
            tile[i] = new Tile(tileImage[i], findRank(digitPosition,i));
        }
       }
    private int findRank (int[] array, int value) {
        int rank = -1;
        for (int index = 0; index < array.length; index++){
            if (array[index] == value) {
                rank = index;
                break;
            }
        }
        return rank;
    }
    private void findCoordinateOfPosition() {
        coordinates = new Point2D[numberOfTiles];
        int count = 0;
        for (int y = 0; y < TILE_HEIGHT * numberOfTilesPerRow; y = y + TILE_HEIGHT) {
            for (int x = 0; x < TILE_WIDTH * numberOfTilesPerColumn; x = x + TILE_WIDTH) {
                coordinates[count] = new Point2D(x, y);
                count++;
            }
         }
    }
    private void drawTiles( Point2D[] point, int[] number) {
        for (int i =0; i < numberOfTiles; i++) {
            gc.drawImage(tile[number[i]].image, point[i].getX(), point[i].getY());

        }
   }
    void newGame(){
        Game_Completed = false;
        movingCount = 0;
        numberOfTiles = numberOfTilesPerRow*numberOfTilesPerColumn;
        createDefaultStartingPositon();
        designTargetPattern();
        if (Shuffling_Tile) shuffleTiles();
        createTiles();
        canvas.setWidth(numberOfTilesPerColumn*TILE_WIDTH);
        canvas.setHeight(numberOfTilesPerRow*TILE_HEIGHT);
        findCoordinateOfPosition();
        drawTiles(coordinates, digitPosition);
    }
    private void createDefaultStartingPositon(){
        digitPosition = new int[numberOfTiles];
        for (int i = 0; i < numberOfTiles - 3; i++) {
            digitPosition[i] = i + 1;
        }
        digitPosition[numberOfTiles - 1] = 0;
        digitPosition[numberOfTiles - 2] = numberOfTiles - 2;
        digitPosition[numberOfTiles - 3] = numberOfTiles - 1;

    }
    private void shuffleTiles() {
        Random random = new Random();
        int count = 0;
        int temp;
        while ( count < numberOfTiles -1) {
            // random number from 0 to numberOfTiles-2
            int n = random.nextInt(numberOfTiles -1);
            temp = digitPosition[count];
            digitPosition[count] = digitPosition[n];
            digitPosition[n]= temp;
            count++;
        }

    }
    private int findClickedPosition(double x, double y ) {
        int clickedRank=0;
        for (int i = 0; i < numberOfTiles; i++) {
            if ((x >= coordinates[i].getX() && x < coordinates[i].getX()+ TILE_WIDTH) &&
                    (y >= coordinates[i].getY() && y < coordinates[i].getY()+ TILE_HEIGHT)){
                clickedRank = i;
                break;
            }
        }
        return clickedRank;
    }
    private void designTargetPattern(){
        targetPattern = new int[numberOfTiles];
        if (!Vertical_Direction) {
            for (int i = 0; i < numberOfTiles - 1; i++) {
                targetPattern[i] = i + 1;
            }
            targetPattern[numberOfTiles - 1] = 0;
        } else {
            int count =1;
            for (int c = 0; c < numberOfTilesPerColumn; c++) {
                int n = 0;
                for (int r = 0; r < numberOfTilesPerRow; r++) {
                    targetPattern[c+n] = count;
                    n = n + numberOfTilesPerRow;
                    count++;
                }
            }
            targetPattern[numberOfTiles -1]=0;

        }
    }
    private boolean isGameOver() {
        int count=0;
        for (int index = 0; index < numberOfTiles; index++) {
            if (digitPosition[index] != targetPattern[index])
                break;
            count++;
        }
        return count == numberOfTiles;
    }

    private void displayMessage() {
        Alert alert = new Alert(Alert.AlertType.NONE,"", ButtonType.CLOSE);
        alert.setTitle("Congratulations...");
        alert.setHeaderText(null);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("NumberSlider.css").toExternalForm());
        Label lblContent = new Label("Game Completed.\n Moving Count : "+ movingCount
                 +"\n Elapse Time = "+ getElapseTime());
        lblContent.getStyleClass().add("label-topic");
        dialogPane.setContent(lblContent);
        dialogPane.getStyleClass().add("root");
        alert.showAndWait();
   }
    private String getElapseTime() {
        long elapseTime = System.currentTimeMillis() - startingTime;
        int hours = (int) ((elapseTime / 1000) / 3600);
        int minutes = (int) (((elapseTime / 1000) / 60) % 60);
        int seconds = (int) ((elapseTime / 1000) % 60);
        if (hours != 0) {
            return String.format("%d hours %d minutes %d seconds", hours, minutes, seconds);
        } else if (minutes != 0) {
            return String.format("%d minutes %d seconds",  minutes, seconds);
        }else {
            return String.format("%d seconds",  seconds);
        }
    }
    private boolean isValidClicked(int clickedRank, int spaceRank) {
        double displacement =  Math.abs(coordinates[clickedRank].distance(coordinates[spaceRank]));
        return displacement ==TILE_WIDTH;
 }
    void setNumberOfTilesPerRow(int numberOfTilesPerRow) {
        this.numberOfTilesPerRow = numberOfTilesPerRow;
    }
    void setNumberOfTilesPerColumn(int numberOfTilesPerColumn) {
        this.numberOfTilesPerColumn = numberOfTilesPerColumn;
    }
    void setShuffling_Tile(Boolean shuffling) {
        Shuffling_Tile = shuffling;
    }
    void setVertical_Direction(Boolean verticalSelected){
        Vertical_Direction = verticalSelected;
    }
    void setSuper_Player_Mode() {Super_Player_Mode= !Super_Player_Mode; }
}
