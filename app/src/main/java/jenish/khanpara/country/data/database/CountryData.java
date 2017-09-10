package jenish.khanpara.country.data.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Developed By JENISH KHANPARA on 10 September 2017.
 */

@DatabaseTable(tableName = "menu_country_data")
public class CountryData {

    // Fields

    // Primary key defined as an auto generated integer
    // If the database table column name differs than the Model class variable name, the way to map to use columnName


    @DatabaseField(columnName = "id",generatedId = true)
    private int id;

    @DatabaseField(columnName = "country_name")
    private String countryName;

    @DatabaseField(columnName = "country_id")
    private String countryId;

    @DatabaseField(columnName = "state_name")
    private String stateName;

    @DatabaseField(columnName = "state_id")
    private String stateId;

    @DatabaseField(columnName = "city_name")
    private String cityName;

    @DatabaseField(columnName = "city_id")
    private String cityId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "Country-Data:-Country:"+countryName+"--State:"+stateName+"--City:"+cityName;
    }
}
