package Client4;

import java.io.File;

public class Formdata {
    String year="1997";
    String title="Never Mind The Bollocks!";
    String artist="Sex Pistols";
    File image=new File("src/main/java/nmtb.png");


    public Formdata(String year, String title, String artist, File img) {
        this.year = year;
        this.title = title;
        this.artist = artist;
        this.image = img;
    }
    public Formdata(){}


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public File getImg() {
        return image;
    }

    public void setImg(File img) {
        this.image = img;
    }


}
