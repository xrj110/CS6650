package src;

public class ReviewData {

    private long likes;
    private long dislikes;

    public ReviewData( long likes, long dislikes) {

        this.likes = likes;
        this.dislikes = dislikes;
    }





    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }
    public String toJson() {
        return "{\"likes\":\"" + likes + "\",\"dislikes\":\"" + dislikes + "\"}";
    }
}
