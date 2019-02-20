package dick.android.remotecontrol;

import android.graphics.Bitmap;

public class ElectricalAppliance {
    private Bitmap image;
    private String name;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElectricalAppliance(Bitmap image, String name) {
        this.image = image;
        this.name = name;
    }
}
