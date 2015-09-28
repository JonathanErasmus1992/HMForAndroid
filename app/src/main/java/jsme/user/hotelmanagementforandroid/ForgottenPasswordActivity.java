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

public class ForgottenPasswordActivity extends AppCompatActivity {


    private TextView lblRecoveryQ;
    private EditText txtVerifyEmailAddress;
    private EditText txtRecoveryA;
    private ImageButton btnVerifyEmailAddress;
    private ImageButton btnUnlockMyAccount;

    private static String strEmailAddress;
    private static String strRecoveryQ;
    private static String strRecoveryA;

    final Context context = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        txtVerifyEmailAddress = (EditText)findViewById(R.id.txtVerifyEmailAddress);
        lblRecoveryQ = (TextView)findViewById(R.id.lblVerifyRecoveryQ);
        txtRecoveryA = (EditText)findViewById(R.id.txtVerifyRecoveryA);

        btnVerifyEmailAddress = (ImageButton)findViewById(R.id.btnVerifyEmailAddress);

        btnVerifyEmailAddress.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(!txtVerifyEmailAddress.getText().toString().trim().equals("")){
                    if(txtVerifyEmailAddress.getText().toString().trim().contains(" ")){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Invalid Email Address");
                        alertDialog.setMessage("Please fill in a valid EMAIL ADDRESS without containing any spaces before tapping VERIFY");
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
                        new verifyEmailAddress().execute();
                    }
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Blank Field");
                    alertDialog.setMessage("Please fill in a valid EMAIL ADDRESS before tapping VERIFY");
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

        btnUnlockMyAccount = (ImageButton)findViewById(R.id.btnUnlockmyAccount);
        btnUnlockMyAccount.setEnabled(false);
        btnUnlockMyAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if((!txtVerifyEmailAddress.getText().toString().trim().equals("")) &&
                        (!txtRecoveryA.getText().toString().trim().equals(""))){
                    new verifyRecoveryAnswer().execute();
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Blank Field");
                    alertDialog.setMessage("Please fill in a valid ANSWER before tapping UNLOCK MY ACCOUNT");
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
    }
    private class verifyEmailAddress extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            strEmailAddress = txtVerifyEmailAddress.getText().toString().trim();

            pDialog = new ProgressDialog(ForgottenPasswordActivity.this);
            pDialog.setTitle("Verification Process");
            pDialog.setMessage("Please wait while authenticating...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Boolean blnLog = false;
                String urlVerify = "http://hotelmanagement-jsme.rhcloud.com/user/forgotpassword?emailAddress=" + strEmailAddress;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                blnLog = restTemplate.getForObject(urlVerify, Boolean.class);
                return blnLog;
            }
            catch(Exception e){
                Log.e("Message Log", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean blnLog) {
            //super.onPostExecute(strLOg);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(blnLog == true){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("User Verification Successful");
                alertDialog.setMessage("Verification Successful for EMAIL ADDRESS entered. Please click OK");
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        new retrieveRecoveryQuestion().execute();
                    }
                });
                alertDialog.show();
            }
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Verification Failed");
                alertDialog.setMessage("Please make sure that your EMAIL ADDRESS is correct and try again");
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

    private class retrieveRecoveryQuestion extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgottenPasswordActivity.this);
            pDialog.setTitle("Retrieving Recovery Question");
            pDialog.setMessage("Please wait while retrieving...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                String url = "http://hotelmanagement-jsme.rhcloud.com/user/recoveryquestion?emailAddress=" + strEmailAddress;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                String strLog = restTemplate.getForObject(url, String.class);
                return strLog;
            }
            catch(Exception e){
                Log.e("Message Log", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strLog) {
            //super.onPostExecute(strLog);
            if (pDialog.isShowing())
                pDialog.dismiss();
            lblRecoveryQ.setText("Recovery Question: \n" + strLog);
            txtVerifyEmailAddress.setEnabled(false);
            btnUnlockMyAccount.setEnabled(true);
        }
    }

    private class verifyRecoveryAnswer extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            strEmailAddress = txtVerifyEmailAddress.getText().toString().trim();
            strRecoveryA = txtRecoveryA.getText().toString().trim();
            strRecoveryA = strRecoveryA.replace(" ", "%20");

            pDialog = new ProgressDialog(ForgottenPasswordActivity.this);
            pDialog.setTitle("Verification Process");
            pDialog.setMessage("Please wait while authenticating...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Boolean blnLog = false;
                String urlVerify = "http://hotelmanagement-jsme.rhcloud.com/user/recoveryanswer?emailAddress=" + strEmailAddress +
                        "&recoveryAnswer=" + strRecoveryA;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                blnLog = restTemplate.getForObject(urlVerify, Boolean.class);
                return blnLog;
            }
            catch(Exception e){
                Log.e("Message Log", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean blnLog) {
            //super.onPostExecute(blnLog);

            if (pDialog.isShowing())
                pDialog.dismiss();
            if(blnLog == true){
                new unlockUserAccount().execute();
            }
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Incorrect Answer");
                alertDialog.setMessage("Please make sure that the ANSWER is correct and try again");
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
    private class unlockUserAccount extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            strEmailAddress = txtVerifyEmailAddress.getText().toString().trim();

            pDialog = new ProgressDialog(ForgottenPasswordActivity.this);
            pDialog.setTitle("Unlocking Account");
            pDialog.setMessage("Please wait while processing...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Boolean blnLog = false;
                String urlVerify = "http://hotelmanagement-jsme.rhcloud.com/user/changepassword?emailAddress=" + strEmailAddress + "&password=00000";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                blnLog = restTemplate.getForObject(urlVerify, Boolean.class);
                return blnLog;
            }
            catch(Exception e){
                Log.e("Message Log", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean blnLog) {
            //super.onPostExecute(blnLog);
            if (pDialog.isShowing())
                pDialog.dismiss();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Account Unlocked");
            alertDialog.setMessage("Account Unlocked. Please LOGIN with your EMAIL ADDRESS and use the password 00000. Please remember to change your password once logged in");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Intent i = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            alertDialog.show();
        }
    }
}
