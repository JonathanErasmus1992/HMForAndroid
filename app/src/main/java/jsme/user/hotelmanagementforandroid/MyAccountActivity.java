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

public class MyAccountActivity extends AppCompatActivity {

    private ImageButton btnUpdateAccount;
    private ImageButton btnDeactivateAccount;
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private EditText txtCPassword;
    private EditText txtRecoveryQ;
    private EditText txtRecoveryA;

    private ProgressDialog pDialog;

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        final Bundle logInData = getIntent().getExtras();

        txtEmailAddress = (EditText)findViewById(R.id.txtAccountEmailAddress);
        txtPassword = (EditText)findViewById(R.id.txtAccountPassword);
        txtCPassword = (EditText)findViewById(R.id.txtAccountCPassword);
        txtRecoveryQ = (EditText)findViewById(R.id.txtAccountRecoveryQ);
        txtRecoveryA = (EditText)findViewById(R.id.txtAccountRecoveryA);

        btnUpdateAccount = (ImageButton)findViewById(R.id.btnUpdateAccount);
        btnDeactivateAccount = (ImageButton)findViewById(R.id.btnDeactivateAccount);

        txtEmailAddress.setText(logInData.getString("Username"));
        txtPassword.setText(logInData.getString("Password"));
        txtCPassword.setText(logInData.getString("Password"));

        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new changeMyPassword().execute();
            }
        });

        btnDeactivateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Deactivate Account");
                alertDialog.setMessage("Are you sure that you want to DEACTIVATE your account?");
                alertDialog.setIcon(R.drawable.question);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new deactivateMyAccount().execute();
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
    }

    private class deactivateMyAccount extends AsyncTask<Void, Void, Boolean>{
        String strEmailAddress = "";
        String strPassword = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyAccountActivity.this);
            pDialog.setTitle("Deactivation Process");
            pDialog.setMessage("Please wait while deactivating account...");
            pDialog.setCancelable(false);
            pDialog.show();

            strEmailAddress = txtEmailAddress.getText().toString();
            strPassword = txtPassword.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/user/unregister?emailAddress=" + strEmailAddress;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                blnLog = restTemplate.getForObject(url, Boolean.class);
                return blnLog;
            } catch (Exception e) {
                Log.e("Message Log:", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Account Deactivated");
            alertDialog.setMessage("Your account was successfully removed from our system.");
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

    private class changeMyPassword extends AsyncTask<Void, Void, Boolean>{
        String strEmailAddress = "";
        String strPassword = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyAccountActivity.this);
            pDialog.setTitle("Deactivation Process");
            pDialog.setMessage("Please wait while de-registering...");
            pDialog.setCancelable(false);
            pDialog.show();

            strEmailAddress = txtEmailAddress.getText().toString();
            strPassword = txtPassword.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/user/changepassword?emailAddress=" + strEmailAddress +
                        "&password=" + strPassword;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                blnLog = restTemplate.getForObject(url, Boolean.class);
                return blnLog;
            } catch (Exception e) {
                Log.e("Message Log:", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Password Change");
            alertDialog.setMessage("Your PASSWORD has been changed successfully. Affective from next LOGIN");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Intent i = new Intent(getApplicationContext(),
                            HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            alertDialog.show();
        }
    }
}
