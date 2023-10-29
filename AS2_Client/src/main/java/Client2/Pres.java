package Client2;

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
        // 创建一个包含字段的字符串
        return String.format("{\n  \"ID\": \"%s\",\n  \"Size\": \"%s\",\n}", albumID, imageSize);

    }
}
