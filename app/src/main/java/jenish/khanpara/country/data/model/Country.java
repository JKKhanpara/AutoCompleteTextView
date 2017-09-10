package jenish.khanpara.country.data.model;

/**
 * Developed By JENISH KHANPARA on 09 September 2017.
 *
 * */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country extends BaseItem {

    @SerializedName("sortname")
    @Expose
    private String sortName;


    public String getSortName() {
        return sortName;
    }

}