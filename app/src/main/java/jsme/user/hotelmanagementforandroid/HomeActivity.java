package jsme.user.hotelmanagementforandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;

public class HomeActivity extends AppCompatActivity {

    ImageButton btnBookings;
    ImageButton btnCustomers;
    ImageButton btnRooms;
    ImageButton btnServicesAndExtras;
    ImageButton btnMyAccount;
    ImageButton btnLogout;

    public static String username;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Bundle logInData = getIntent().getExtras();

        btnBookings = (ImageButton)findViewById(R.id.btnBookings);
        btnCustomers = (ImageButton)findViewById(R.id.btnCustomers);
        btnRooms = (ImageButton)findViewById(R.id.btnRooms);
        btnServicesAndExtras = (ImageButton)findViewById(R.id.btnServicesAndExtras);
        btnMyAccount = (ImageButton)findViewById(R.id.btnMyAccount);
        btnLogout = (ImageButton)findViewById(R.id.btnLogout);

        btnBookings.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),
                        BookingsActivity.class);
                startActivity(i);
            }
        });

        btnCustomers.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),
                        CustomersActivity.class);
                startActivity(i);
            }
        });

        btnRooms.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),
                        RoomsActivity.class);
                startActivity(i);
            }
        });

        btnServicesAndExtras.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),
                        ServicesAndExtrasActivity.class);
                startActivity(i);
            }
        });

        btnMyAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),
                        MyAccountActivity.class);
                i.putExtra("Username", logInData.getString("Username"));
                i.putExtra("Password", logInData.getString("Password"));
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Logout");
                alertDialog.setMessage("Are you sure that you want to LOGOUT of the application?");
                alertDialog.setIcon(R.drawable.question);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(),
                                LoginActivity.class);
                        startActivity(i);
                        finish();
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
}
