package jsme.user.hotelmanagementforandroid;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class BookingsActivity extends ListActivity {

    private ImageButton btnMakeNewBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        btnMakeNewBooking = (ImageButton)findViewById(R.id.btnMakeNewBooking);

        btnMakeNewBooking.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),
                        NewBookingActivity.class);
                startActivity(i);
            }
        });
    }
}
