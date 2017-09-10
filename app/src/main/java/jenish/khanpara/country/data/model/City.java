package jenish.khanpara.country.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Developed By JENISH KHANPARA on 10 September 2017.
 */

public class City extends BaseItem {


    @SerializedName("state_id")
    @Expose
    private String stateId;

    public String getStateId() {
        return stateId;
    }

}