package src;

public class ResponseData {
    private String id;
    private String imageSize;

    public void setId(String id) {
        this.id = id;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String toJson() {
        return "{\"albumID\":\"" + id + "\",\"imageSize\":\"" + imageSize + "\"}";
    }
}
