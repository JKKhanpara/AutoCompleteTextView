package jenish.khanpara.country.data.model;

/**
 * Developed By JENISH KHANPARA on 09 September 2017.
 *
 * */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}