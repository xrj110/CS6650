package Client3;

public class Gres {
    String artist;
    String title;
    String year;

    public Gres(String artist, String title, String year) {
        this.artist = artist;
        this.title = title;
        this.year = year;
    }
    public Gres(){

    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    @Override
    public String toString() {
        // 创建一个包含字段的字符串
        return String.format("{\n  \"artist\": \"%s\",\n  \"title\": \"%s\",\n  \"year\": \"%s\"\n}", artist, title, year);

    }


}
