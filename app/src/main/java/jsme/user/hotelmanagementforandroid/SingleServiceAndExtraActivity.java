package jsme.user.hotelmanagementforandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SingleServiceAndExtraActivity extends AppCompatActivity {

    private static final String TAG_SERVICEANDEXTRAID = "servExtraID";
    private static final String TAG_SERVICEANDEXTRANAME = "extraName";
    private static final String TAG_SERVICEANDEXTRAPRICE = "priceAdded";

    private TextView lblSEID;
    private EditText lblSEName;
    private EditText lblSEPrice;

    private ImageButton btnUpdate;
    private ImageButton btnDelete;
    private ImageButton btnCancel;

    private int intServiceAndExtraID = 0;
    private String strServiceAndExtraName = "";
    private Double dblServiceAndExtraPrice = 0.00;
    private String strServiceAndExtraID = "";

    private final Context context = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_service_and_extra);

        Intent i = getIntent();

        intServiceAndExtraID = Integer.parseInt(i.getStringExtra(TAG_SERVICEANDEXTRAID));
        strServiceAndExtraName = i.getStringExtra(TAG_SERVICEANDEXTRANAME);
        dblServiceAndExtraPrice = Double.parseDouble(i.getStringExtra(TAG_SERVICEANDEXTRAPRICE));

        lblSEID = (TextView)findViewById(R.id.lblSAndEIDList);
        lblSEName = (EditText)findViewById(R.id.lblSAndENameList);
        lblSEPrice = (EditText)findViewById(R.id.lblSAndEPriceList);

        lblSEID.setText(intServiceAndExtraID + "");
        lblSEName.setText(strServiceAndExtraName);
        lblSEPrice.setText(dblServiceAndExtraPrice.toString());

        btnUpdate = (ImageButton)findViewById(R.id.btnUpdateServiceAndExtra);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if ((!lblSEID.getText().toString().trim().equals("")) && (!lblSEName.getText().toString().trim().equals("")) &&
                        (!lblSEPrice.getText().toString().trim().equals(""))) {
                    try {
                        Double dblCheck = 0.0;
                        dblCheck = Double.parseDouble(lblSEPrice.getText().toString());
                        new updateServiceAndExtra().execute();

                    } catch (Exception e) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Price Format");
                        alertDialog.setMessage("Please enter a valid PRICE AMOUNT before tapping UPDATE.");
                        alertDialog.setIcon(R.drawable.info);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Blank Fields");
                    alertDialog.setMessage("Please fill in all the requested fields before tapping UPDATE.");
                    alertDialog.setIcon(R.drawable.info);
                    alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        btnDelete = (ImageButton)findViewById(R.id.btnDeleteServiceAndExtra);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Remove Service And Extra");
                alertDialog.setMessage("Are you sure that you want to remove the selected SERVICE AND EXTRA from the listing?");
                alertDialog.setIcon(R.drawable.question);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new deleteServiceAndExtra().execute();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        });

        btnCancel = (ImageButton)findViewById(R.id.btnCancelServiceAndExtra);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ServicesAndExtrasActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private class updateServiceAndExtra extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            intServiceAndExtraID = Integer.parseInt(lblSEID.getText().toString());
            strServiceAndExtraName = lblSEName.getText().toString();
            if (strServiceAndExtraName.contains(" ")){
                strServiceAndExtraName = strServiceAndExtraName.replace(" ", "%20");
            }
            dblServiceAndExtraPrice = Double.parseDouble((lblSEPrice.getText().toString()));

            pDialog = new ProgressDialog(SingleServiceAndExtraActivity.this);
            pDialog.setTitle("Updating Process");
            pDialog.setMessage("Please wait while updating...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/servicesandextras/update?SEID=" +
                        intServiceAndExtraID + "&extraName=" + strServiceAndExtraName + "&price=" + dblServiceAndExtraPrice;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                blnLog = restTemplate.getForObject(url, Boolean.class);
                return blnLog;
            }
            catch(Exception e){
                Log.e("Message Log", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean blnLog) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(blnLog == true){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Service And Extrea Updated");
                alertDialog.setMessage("The SERVICE AND EXTRA was successfully updated on our listing.");
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(getApplicationContext(),
                                ServicesAndExtrasActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
    }

    private class deleteServiceAndExtra extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            intServiceAndExtraID = Integer.parseInt(lblSEID.getText().toString());
            pDialog = new ProgressDialog(SingleServiceAndExtraActivity.this);
            pDialog.setTitle("Removing Process");
            pDialog.setMessage("Please wait while removing...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/servicesandextras/delete?SEID=" + intServiceAndExtraID;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                blnLog = restTemplate.getForObject(url, Boolean.class);
                return blnLog;
            }
            catch(Exception e){
                Log.e("Message Log", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean blnLog) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(blnLog == true){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Service And Extra Removed");
                alertDialog.setMessage("The SERVICE AND EXTRA was successfully removed from our listing.");
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(getApplicationContext(),
                                RoomsActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
    }
}
