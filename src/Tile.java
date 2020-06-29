import javafx.scene.image.Image;

class Tile {
    Image image;
    private int tileRank; // rank index = 0 to numberOfTiles - 1
    Tile (Image image, int rank) {
        this.image = image;
        this.tileRank = rank;
    }

    int getTileRank(){
        return tileRank;
    }
    void setTileRank(int rank) {
        this.tileRank = rank;
    }
}