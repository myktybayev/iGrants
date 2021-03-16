package kz.school.grants.news_menu;

import java.io.Serializable;

public class NewsFeed implements Serializable {

    private String fKey;
    private String news_title;
    private String news_date;
    private String news_desc;
    private String news_img;
    int likes;
    int dislikes;

    public NewsFeed() {

    }

    public NewsFeed(String fKey, String news_title, String news_desc, String news_date, String news_img, int likes, int dislikes) {
        this.fKey = fKey;
        this.news_title = news_title;
        this.news_date = news_date;
        this.news_desc = news_desc;
        this.news_img = news_img;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getfKey() {
        return fKey;
    }

    public void setfKey(String fKey) {
        this.fKey = fKey;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    public String getNews_desc() {
        return news_desc;
    }

    public void setNews_desc(String news_desc) {
        this.news_desc = news_desc;
    }

    public String getNews_img() {
        return news_img;
    }

    public void setNews_img(String news_img) {
        this.news_img = news_img;
    }
}
