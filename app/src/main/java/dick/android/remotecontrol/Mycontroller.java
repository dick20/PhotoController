package dick.android.remotecontrol;

import android.graphics.Bitmap;

public class Mycontroller {
    private Bitmap icon;
    private String name;
    private String type;
    private String description;

    public Mycontroller(Bitmap icon, String name, String type, String description) {
        this.icon = icon;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
