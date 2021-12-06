package tolabuth.mobilebook.model;

public  class Contact {
    private int id;
    private String name;
    private String mobile;
    private String image;
    public Contact(int id, String name, String mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }

    public Contact() {
    }

    public Contact(int id, String name, String mobile, String image) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.image = image;
    }

    public Contact(String name, String mobile, String image) {
        this.name = name;
        this.mobile = mobile;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Contact(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}