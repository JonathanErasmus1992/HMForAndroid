package jsme.user.hotelmanagementforandroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServicesAndExtrasActivity extends ListActivity {

    private static final String TAG_ID = "id";
    private static final String TAG_SERVICEANDEXTRAID = "servExtraID";
    private static final String TAG_SERVICEANDEXTRANAME = "extraName";
    private static final String TAG_SERVICEANDEXTRAPRICE = "priceAdded";

    private int intServiceAndExtraID = 0;
    private String strServiceAndExtraName = "";
    private Double dblServiceAndExtraPrice = 0.00;

    List<HashMap<String, String>> servicesAndExtrasList;

    private ImageButton btnAddNewServiceAndExtra;

    final Context context = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_and_extras);

        servicesAndExtrasList = new ArrayList<HashMap<String, String>>();

        ListView listServiceAndExtraView = getListView();

        listServiceAndExtraView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String strSEID = ((TextView)findViewById(R.id.lblSAndEIDList)).getText().toString();
                String strSEName = ((TextView)findViewById(R.id.lblSAndENameList)).getText().toString();
                String strSEPrice = ((TextView)findViewById(R.id.lblSAndEPriceList)).getText().toString();

                Intent i = new Intent(getApplicationContext(), SingleServiceAndExtraActivity.class);
                i.putExtra(TAG_SERVICEANDEXTRAID, strSEID);
                i.putExtra(TAG_SERVICEANDEXTRANAME, strSEName);
                i.putExtra(TAG_SERVICEANDEXTRAPRICE, strSEPrice);
                startActivity(i);
                finish();
            }
        });

        new HttpRequestTask().execute();

        btnAddNewServiceAndExtra = (ImageButton)findViewById(R.id.btnAddNewSAndE);
        btnAddNewServiceAndExtra.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        NewServiceAndExtraActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ServicesAndExtrasActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url = "http://hotelmanagement-jsme.rhcloud.com/servicesandextras/all";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                servicesAndExtrasList = restTemplate.getForObject(url, ArrayList.class);
            } catch (Exception e) {
                Log.e("Message Log:", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(s);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (servicesAndExtrasList.size() == 0) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("No Items In List");
                alertDialog.setMessage("Please note that there were no items found in the requested list.");
                alertDialog.setIcon(R.drawable.info);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
            else
            {
                ListAdapter adapter = new SimpleAdapter(ServicesAndExtrasActivity.this, servicesAndExtrasList, R.layout.serviceandextra_list_item,
                        new String[]{TAG_SERVICEANDEXTRAID, TAG_SERVICEANDEXTRANAME, TAG_SERVICEANDEXTRAPRICE},
                        new int[]{R.id.lblSAndEIDList, R.id.lblSAndENameList, R.id.lblSAndEPriceList});
                setListAdapter(adapter);
            }
        }
    }
}
