package jsme.user.hotelmanagementforandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewCAddressActivity extends AppCompatActivity {

    private ImageButton btnCustomerContact;
    private ImageButton btnCancelNewCustomer;

    private EditText txtPhysicalAddress;
    private EditText txtPostalAddress;
    private EditText txtPostalCode;

    private String strPhysicalAddress = "";
    private String strPostalAddress = "";
    private String strPostalCode = "";

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_caddress);

        final Bundle logInData = getIntent().getExtras();

        txtPhysicalAddress = (EditText)findViewById(R.id.txtCPhysicalAddress);
        txtPostalAddress = (EditText)findViewById(R.id.txtCPostalAddress);
        txtPostalCode = (EditText)findViewById(R.id.txtCPostalCode);

        btnCustomerContact = (ImageButton)findViewById(R.id.btnCustomerContact);

        btnCustomerContact.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if((!txtPhysicalAddress.getText().toString().trim().equals("")) && (!txtPhysicalAddress.getText().toString().trim().equals("")) &&
                        (!txtPhysicalAddress.getText().toString().trim().equals(""))){

                    strPhysicalAddress = txtPhysicalAddress.getText().toString();
                    strPhysicalAddress = strPhysicalAddress.replace(" ", "%20");

                    strPostalAddress = txtPostalAddress.getText().toString();
                    strPostalAddress = strPostalAddress.replace(" ", "%20");

                    strPostalCode = txtPostalCode.getText().toString();
                    strPostalCode = strPostalCode.replace(" ", "%20");

                    Intent iPrev = getIntent();

                    Intent i = new Intent(getApplicationContext(),
                            NewCContactActivity.class);
                    startActivity(i);

                    i.putExtra("CIDNumber", iPrev.getStringExtra("CIDNumber"));
                    i.putExtra("CFirstNames", iPrev.getStringExtra("CFirstNames"));
                    i.putExtra("CLastName", iPrev.getStringExtra("CLastName"));
                    i.putExtra("CPhysicalAddress", strPhysicalAddress);
                    i.putExtra("CPostalAddress", strPostalAddress);
                    i.putExtra("CPostalCode", strPostalCode);
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Blank Fields");
                    alertDialog.setMessage("Please fill in all the requested fields before tapping CUSTOMER CONTACT.");
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

        btnCancelNewCustomer = (ImageButton)findViewById(R.id.btnCancelNCustomer);

        btnCancelNewCustomer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        CustomersActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
