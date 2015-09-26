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

public class NewCContactActivity extends AppCompatActivity {

    private EditText txtCellNumber;
    private EditText txtHomeNumber;
    private EditText txtEmailAddress;
    private EditText txtNOKN;
    private ImageButton btnSaveNewCustomer;
    private ImageButton btnCancelNewCustomer;

    private String strCellNumber = "";
    private String strHomeNumber = "";
    private String strEmailAddress = "";
    private String strNOKN = "";

    final Context context = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ccontact);

        txtCellNumber = (EditText)findViewById(R.id.txtCCellNumber);
        txtHomeNumber = (EditText)findViewById(R.id.txtCHomeNumber);
        txtEmailAddress = (EditText)findViewById(R.id.txtCEmailAddress);
        txtNOKN = (EditText)findViewById(R.id.txtCNOKC);

        btnSaveNewCustomer = (ImageButton)findViewById(R.id.btnSaveNewCustomer);
        btnSaveNewCustomer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if((!txtCellNumber.getText().toString().trim().equals("")) && (!txtEmailAddress.getText().toString().trim().equals(""))){
                    if(txtCellNumber.getText().toString().length() != 10){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Invalid Contact Number");
                        alertDialog.setMessage("Please enter a valid CONTACT NUMBER that is no more or less than 10 digits");
                        alertDialog.setIcon(R.drawable.info);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                    else if(txtEmailAddress.getText().toString().contains(" ")){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Invalid Email Address");
                        alertDialog.setMessage("Please enter a valid EMAIL ADDRESS that does not contain any spaces");
                        alertDialog.setIcon(R.drawable.info);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                    else{
                        new registerNewCustomer().execute();
                    }
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Blank Fields");
                    alertDialog.setMessage("Please fill in a CONTACT NUMBER and EMAIL ADDRESS before tapping REGISTER. All other fields are optional");
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

        btnCancelNewCustomer = (ImageButton)findViewById(R.id.btnCancelNeCustomer);
        btnCancelNewCustomer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        CustomersActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private class registerNewCustomer extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final Bundle logInData = getIntent().getExtras();

            /*String strIDNumber = "";
            String strFirstNames = "";
            String strLastName = "";
            String strPhysicalAddress = "";
            String strPostalAddress = "";
            String strPostalCode = "";

            strIDNumber = logInData.getString("CIDNumber");
            strFirstNames = logInData.getString("CFirstNames");
            strLastName = logInData.getString("CLastName");
            strPhysicalAddress = logInData.getString("CPhysicalAddress");
            strPostalAddress = logInData.getString("CPostalAddress");
            strPostalCode = logInData.getString("CPostalCode");*/

            strCellNumber = txtCellNumber.getText().toString().trim();
            strHomeNumber = txtHomeNumber.getText().toString().trim();
            if(strHomeNumber.equals("")){
                strHomeNumber = "Not Provided";
            }
            strHomeNumber = strHomeNumber.replace(" ", "%20");
            strEmailAddress = txtEmailAddress.getText().toString().trim();
            strNOKN = txtNOKN.getText().toString().trim();
            if(strNOKN.equals("")){
                strNOKN = "Not Provided";
            }
            strNOKN = strNOKN.replace(" ", "%20");


            pDialog = new ProgressDialog(NewCContactActivity.this);
            pDialog.setTitle("Registering Process");
            pDialog.setMessage("Please wait while registering...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Intent iPrev = getIntent();
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/customer/create?idNumber=" + iPrev.getStringExtra("CIDNumber") + "&firstName=" + iPrev.getStringExtra("CFirstNames") +
                        "&lastName=" + iPrev.getStringExtra("CLastName") + "&physicalAddress=" + iPrev.getStringExtra("CPhysicalAddress") +
                        "&postalAddress=" + iPrev.getStringExtra("CPostalAddress") + "&postalCode=" + iPrev.getStringExtra("CPostalCode") + "&cellNumber=" + strCellNumber +
                        "&homeNumber=" + strHomeNumber + "&emailAddress=" + strEmailAddress +
                        "&NOKN=" + strNOKN;
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
                alertDialog.setTitle("Customer Successfully Registered");
                alertDialog.setMessage("The CUSTOMER was successfully registered on our system.");
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(getApplicationContext(),
                                CustomersActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialog.show();
            }
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Customer Registration Unsuccessful");
                alertDialog.setMessage("A CUSTOMER with the specified ID Number already exists. Please change and try again");
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
