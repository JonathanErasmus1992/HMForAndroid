package jsme.user.hotelmanagementforandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class LoginActivity extends AppCompatActivity {

    private String strEmailAddress = "";
    private String strPassword = "";
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnLinkToForgottenPassword;
    final Context context = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmailAddress = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtUserPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLinkToRegister = (Button)findViewById(R.id.btnLinkToRegisterScreen);

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLinkToForgottenPassword = (Button)findViewById(R.id.btnLinkToForgottenPassword);

        btnLinkToForgottenPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ForgottenPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    public void Login(View view)
    {
        strEmailAddress = txtEmailAddress.getText().toString();
        strEmailAddress = strEmailAddress.trim();

        strPassword = txtPassword.getText().toString();
        strPassword = strPassword.trim();
        if ((!strEmailAddress.equals("")) && (!strPassword.equals(""))){
            strPassword = strPassword.replace(" ", "%20");
            new HttpRequestTask().execute();
        }
        else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Empty Fields");
            alertDialog.setMessage("Please fill in both your email address and password before pressing the LOGIN button");
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

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Verification Process");
            pDialog.setMessage("Please wait while authenticating...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            try{
                String url = "http://hotelmanagement-jsme.rhcloud.com/user/login?emailAddress=" + strEmailAddress
                        + "&password=" + strPassword;
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
        protected void onPostExecute(String strLOg) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (strLOg.equals("Username: Found\nPassword: Found")){
                Intent i = new Intent(getApplicationContext(),
                        HomeActivity.class);
                i.putExtra("Username", strEmailAddress);
                i.putExtra("Password", strPassword);
                startActivity(i);
                finish();
            }
            else if(strLOg.equals("Username: Found\nPassword: Not Found")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Invalid Password");
                alertDialog.setMessage("Please make sure that your password is correct and try again.");
                alertDialog.setIcon(R.drawable.info);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
            else if(strLOg.equals("Username: Not Found\nPassword: Found")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Invalid Username");
                alertDialog.setMessage("Please make sure that your username is correct and try again. If you are not a registered user please click below to REGISTER");
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Invalid Credentials");
                alertDialog.setMessage("Please make sure that both your username and password are correct and try again.");
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
