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

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class NewServiceAndExtraActivity extends AppCompatActivity {

    private EditText txtSEID;
    private EditText txtSEName;
    private EditText txtSEPrice;
    private ImageButton btnAddNewSE;
    private ImageButton btnCancelNewSE;

    private int intSEID = 0;
    private String strSEName= "";
    private double dblSEPrice = 0.0;

    private final Context context = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_and_extra);

        txtSEID = (EditText)findViewById(R.id.txtSEID);
        txtSEName = (EditText)findViewById(R.id.txtSEName);
        txtSEPrice = (EditText)findViewById(R.id.txtSEPrice);

        btnAddNewSE = (ImageButton)findViewById(R.id.btnSaveNewServExtr);
        btnAddNewSE.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if ((!txtSEID.getText().toString().trim().equals("")) && (!txtSEName.getText().toString().trim().equals("")) &&
                        (!txtSEPrice.getText().toString().trim().equals(""))){
                    int check1 = 0;
                    int check2 = 0;
                    try{
                        int seIDCheck = 0;
                        seIDCheck = Integer.parseInt(txtSEID.getText().toString());
                        check1 = check1 + 1;
                    }
                    catch(Exception e){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("SE ID Format");
                        alertDialog.setMessage("Please enter a valid SE ID before tapping ADD SERVICE AND EXTRA.");
                        alertDialog.setIcon(R.drawable.info);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                    try{
                        double sePriceCheck = 0.0;
                        sePriceCheck = Double.parseDouble(txtSEPrice.getText().toString());
                        check2 = check2 + 1;
                    }
                    catch(Exception e){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("SE Price Format");
                        alertDialog.setMessage("Please enter a valid SE PRICE before tapping ADD SERVICE AND EXTRA.");
                        alertDialog.setIcon(R.drawable.info);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }

                    if((check1 == 1) && (check2 == 1)){
                        new addNewServiceAndExtra().execute();
                    }
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Blank Fields");
                    alertDialog.setMessage("Please fill in all the requested fields before tapping ADD SERVICE AND EXTRA.");
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

        btnCancelNewSE = (ImageButton)findViewById(R.id.btnCancelNewServExtr);
        btnCancelNewSE.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ServicesAndExtrasActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private class addNewServiceAndExtra extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            intSEID = Integer.parseInt(txtSEID.getText().toString());
            strSEName = txtSEName.getText().toString();
            strSEName = strSEName.replace(" ", "%20");
            dblSEPrice = Double.parseDouble(txtSEPrice.getText().toString());

            pDialog = new ProgressDialog(NewServiceAndExtraActivity.this);
            pDialog.setTitle("Saving Process");
            pDialog.setMessage("Please wait while saving...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/servicesandextras/create?SEID=" + intSEID +
                        "&extraName=" + strSEName + "&price=" + dblSEPrice;
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
                alertDialog.setTitle("Service and Extra Successfully Created");
                alertDialog.setMessage("The SERVICE AND EXTRA was successfully added to listing.");
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
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Service and Extra Creation Unsuccessful");
                alertDialog.setMessage("A SERVICE AND EXTRA with the specified SE ID already exists. Please change and try again");
                alertDialog.setIcon(R.drawable.info);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
            }
        }
    }
}
