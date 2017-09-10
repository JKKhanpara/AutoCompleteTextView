package jenish.khanpara.country.data.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.sql.SQLException;
import java.util.List;

import jenish.khanpara.country.data.R;
import jenish.khanpara.country.data.adapters.CountryDataListAdapter;
import jenish.khanpara.country.data.database.CountryData;
import jenish.khanpara.country.data.database.DBHelper;

public class CountryDataListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CountryDataListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_data_list);

        DBHelper dbHelper = new DBHelper(this);
        try {
            List<CountryData> data=dbHelper.getAll(CountryData.class);
            recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            adapter = new CountryDataListAdapter(this, data);
            recyclerView.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_country_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_firebase:
                startActivity(new Intent(this,FireBaseCountryActivity.class));
                return true;
            case R.id.menu_rest:
                startActivity(new Intent(this,RestCountryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
