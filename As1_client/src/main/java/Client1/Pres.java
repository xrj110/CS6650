package Client1;

public class Pres {
    String albumID;
    String imageSize;

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }
    public String toString() {

        return String.format("{\n  \"ID\": \"%s\",\n  \"Size\": \"%s\",\n}", albumID, imageSize);

    }
}
