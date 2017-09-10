package jenish.khanpara.country.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Developed By JENISH KHANPARA on 10 September 2017.
 */

public class State extends BaseItem {

    @SerializedName("country_id")
    @Expose
    private String countryId;

    public String getCountryId() {
        return countryId;
    }

}