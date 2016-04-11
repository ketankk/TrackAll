package in.kuari.trackall.bean;

import java.util.Comparator;

/**
 * Created by root on 2/12/16.
 */
public class ECommerce {
    private long id;
    private String name;
    private String URL;
    private String ImgPath;

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String imgPath) {
        ImgPath = imgPath;
    }

    public static Comparator<ECommerce> geteCommerceComp() {
        return eCommerceComp;
    }

    public static void seteCommerceComp(Comparator<ECommerce> eCommerceComp) {
        ECommerce.eCommerceComp = eCommerceComp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
    public static Comparator<ECommerce> eCommerceComp=new Comparator<ECommerce>() {
        @Override
        public int compare(ECommerce lhs, ECommerce rhs) {
            String name1 = lhs.getName().toUpperCase();
            String name2 = rhs.getName().toUpperCase();

            return name1.compareTo(name2);
        }
    };

    @Override
    public String toString() {
        return "ECommerce{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", URL='" + URL + '\'' +
                ", ImgPath='" + ImgPath + '\'' +
                '}';
    }
}
