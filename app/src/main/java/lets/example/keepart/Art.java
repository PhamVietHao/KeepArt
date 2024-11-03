package lets.example.keepart;

public class Art {
    private String title;
    private int imageResId;
    private int like;
    private String description;
    private int price;
    private boolean favorited;

    public Art(String title, int imageResId, int like, String description, int price, boolean favorited) {
        this.title = title;
        this.imageResId = imageResId;
        this.like = like;
        this.description = description;
        this.price = price;
        this.favorited = favorited;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}



