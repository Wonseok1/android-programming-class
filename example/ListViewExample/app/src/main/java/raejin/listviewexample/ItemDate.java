package raejin.listviewexample;

public class ItemDate {
    int image_id;
    String name;
    String desc;

    ItemDate(int image_id, String name, String desc) {
        this.image_id = image_id;
        this.name = name;
        this.desc = desc;
    }

    public int getImage_id() {
        return image_id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
