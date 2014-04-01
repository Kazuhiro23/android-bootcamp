package jp.bootcamp.arai;


public class News {
    
    @SuppressWarnings("unused")
    private static final String TAG = News.class.getSimpleName();

    
    private String title;
    private String link;
    private String publisher;
    private String photoUrl;
    private String photoDetail;
    
    public News(String title, String publisher, String photoUrl) {
        this.title = title;
        this.publisher = publisher;
        this.photoUrl = photoUrl;
    }
    public News() {
        
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public String getPhotoDetail() {
        return photoDetail;
    }
    public void setPhotoDetail(String photoDetail) {
        this.photoDetail = photoDetail;
    }
        
        
    
}
