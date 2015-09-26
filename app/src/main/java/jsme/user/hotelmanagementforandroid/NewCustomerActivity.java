package jsme.user.hotelmanagementforandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewCustomerActivity extends AppCompatActivity {

    private ImageButton btnCustomerAddress;
    private ImageButton btnCancelNewCustomer;

    private EditText txtIDNumber;
    private EditText txtFirstNames;
    private EditText txtLastName;

    private String strIDNumber = "";
    private String strFirstNames = "";
    private String strLastName = "";

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        txtIDNumber = (EditText)findViewById(R.id.txtCIDNumber);
        txtFirstNames = (EditText)findViewById(R.id.txtCIDNumber);
        txtLastName = (EditText)findViewById(R.id.txtCIDNumber);

        btnCustomerAddress = (ImageButton)findViewById(R.id.btnCustomerAddress);
        btnCustomerAddress.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if((!txtIDNumber.getText().toString().trim().equals("")) && (!txtFirstNames.getText().toString().trim().equals("")) &&
                        (!txtLastName.getText().toString().trim().equals(""))){

                    strIDNumber = txtIDNumber.getText().toString();
                    if((!strIDNumber.contains(" ")) && (strIDNumber.length() == 13)){

                        strFirstNames = txtFirstNames.getText().toString();
                        strFirstNames = strFirstNames.replace(" ", "%20");

                        strLastName = txtLastName.getText().toString();
                        strLastName = strLastName.replace(" ", "%20");

                        Intent i = new Intent(getApplicationContext(),
                                NewCAddressActivity.class);
                        startActivity(i);
                        i.putExtra("CIDNumber", strIDNumber);
                        i.putExtra("CFirstNames", strFirstNames);
                        i.putExtra("CLastName", strLastName);
                    }
                    else{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Invalid ID Number");
                        alertDialog.setMessage("Please enter a valid ID Number before tapping CUSTOMER ADDRESS.");
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
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Blank Fields");
                    alertDialog.setMessage("Please fill in all the requested fields before tapping CUSTOMER ADDRESS.");
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


        btnCancelNewCustomer = (ImageButton)findViewById(R.id.btnCancelNewCustomer);
        btnCancelNewCustomer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
