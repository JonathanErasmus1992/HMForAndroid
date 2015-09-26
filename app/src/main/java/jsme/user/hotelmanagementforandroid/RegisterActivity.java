package jsme.user.hotelmanagementforandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmailAddress;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private EditText txtRecoveryQuestion;
    private EditText txtRevoveryAnswer;
    private Button btnRegister;
    private Button btnLinkToLogin;
    final Context context = this;
    private String strEmailAddress;
    private String strPassword;
    private String strConfirmPassword;
    private String strRecoveryQuestion;
    private String strRecoveryAnswer;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtEmailAddress = (EditText)findViewById(R.id.txtEmailAddress);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText)findViewById(R.id.txtConfirmPassword);
        txtRecoveryQuestion = (EditText)findViewById(R.id.txtRecoveryQuestion);
        txtRevoveryAnswer = (EditText)findViewById(R.id.txtRecoveryAnswer);

        btnRegister = (Button)findViewById(R.id.btnCreateNewUser);

        btnLinkToLogin = (Button)findViewById(R.id.btnLinkToLoginScreen);

        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void RegisterUser(View view){
        strEmailAddress = txtEmailAddress.getText().toString();
        strEmailAddress = strEmailAddress.trim();
        strPassword = txtPassword.getText().toString();
        strPassword = strPassword.trim();
        strConfirmPassword = txtConfirmPassword.getText().toString();
        strConfirmPassword = strConfirmPassword.trim();
        strRecoveryQuestion = txtRecoveryQuestion.getText().toString();
        strRecoveryQuestion = strRecoveryQuestion.trim();
        strRecoveryAnswer = txtRevoveryAnswer.getText().toString();
        strRecoveryAnswer = strRecoveryAnswer.trim();

        if((!strEmailAddress.equals("")) && (!strPassword.equals("")) && (!strConfirmPassword.equals("")) &&
                (!strRecoveryQuestion.equals("")) && (!strRecoveryAnswer.equals(""))){
            if (!strPassword.equals(strConfirmPassword)){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Password Fields Not Matching");
                alertDialog.setMessage("Please make sure that the password you entered and the confirm password match and try again.");
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
                if(strEmailAddress.contains(" "))
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Username Invalid");
                    alertDialog.setMessage("Please enter a username/email address without containing any spaces");
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
                    strPassword = strPassword.replace(" ", "%20");
                    strRecoveryQuestion = strRecoveryQuestion.replace(" ", "%20");
                    strRecoveryAnswer = strRecoveryAnswer.replace(" ", "%20");

                    new HttpRequestTask().execute();
                }
            }
        }
        else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Empty Fields");
            alertDialog.setMessage("Please fill in all requested fields before pressing the REGISTER button");
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

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setTitle("Registration Process");
            pDialog.setMessage("Please wait while registering...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/user/add?emailAddress=" + strEmailAddress + "&password=" + strPassword +
                        "&recoveryQ=" + strRecoveryQuestion + "&recoveryA=" + strRecoveryAnswer;
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
        protected void onPostExecute(Boolean blnLOg) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(blnLOg == true)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("User Successfully Created");
                alertDialog.setMessage("Your account was successfully registered.");
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
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("User Already Exists");
                alertDialog.setMessage("The email address that you provided is already registered on the system");
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
    }
}
