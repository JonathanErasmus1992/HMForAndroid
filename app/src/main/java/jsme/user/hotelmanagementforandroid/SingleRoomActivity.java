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

public class SingleRoomActivity extends AppCompatActivity {

    private static final String TAG_ROOMTYPE = "roomType";
    private static final String TAG_ROOMNUMBER = "roomNumber";
    private static final String TAG_ROOMVIEW = "roomView";
    private static final String TAG_ROOMPRICE = "roomPrice";

    private TextView lblRoomNumber;
    private EditText lblRoomType;
    private EditText lblRoomView;
    private EditText lblRoomPrice;


    private int intRoomNumber = 0;
    private String strRoomType = "";
    private String strRoomView = "";
    private Double dblRoomPrice = 0.00;
    private String strRoomNumber = "";

    private ImageButton btnUpdate;
    private ImageButton btnDelete;
    private ImageButton btnCancel;

    private final Context context = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_room);

        Intent i = getIntent();

        intRoomNumber = Integer.parseInt(i.getStringExtra(TAG_ROOMNUMBER));
        strRoomType = i.getStringExtra(TAG_ROOMTYPE);
        strRoomView = i.getStringExtra(TAG_ROOMVIEW);
        dblRoomPrice = Double.parseDouble(i.getStringExtra(TAG_ROOMPRICE));

        lblRoomNumber = (TextView)findViewById(R.id.lblRoomNumber);
        lblRoomType = (EditText)findViewById(R.id.lblRoomType);
        lblRoomView = (EditText)findViewById(R.id.lblRoomView);
        lblRoomPrice = (EditText)findViewById(R.id.lblRoomPrice);

        lblRoomNumber.setText(intRoomNumber + "");
        lblRoomType.setText(strRoomType);
        lblRoomView.setText(strRoomView);
        lblRoomPrice.setText(dblRoomPrice.toString());

        btnUpdate = (ImageButton)findViewById(R.id.btnUpdateRoom);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if((!lblRoomType.getText().toString().trim().equals("")) && (!lblRoomView.getText().toString().trim().equals("")) &&
                        (!lblRoomPrice.getText().toString().trim().equals(""))){
                    try{
                        Double dblCheck = 0.0;
                        dblCheck = Double.parseDouble(lblRoomPrice.getText().toString());
                        new updateRoom().execute();
                    }
                    catch(Exception e){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Price Format");
                        alertDialog.setMessage("Please enter a valid PRICE AMOUNT before tapping UPDATE.");
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
                    alertDialog.setMessage("Please fill in all the requested fields before tapping UPDATE.");
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

        btnDelete = (ImageButton)findViewById(R.id.btnDeleteRoom);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Remove Room");
                alertDialog.setMessage("Are you sure that you want to remove the selected ROOM from the listing?");
                alertDialog.setIcon(R.drawable.question);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new deleteRoom().execute();
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

        btnCancel = (ImageButton)findViewById(R.id.btnCancelRoom);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RoomsActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    private class updateRoom extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            intRoomNumber = Integer.parseInt(lblRoomNumber.getText().toString());
            strRoomType = lblRoomType.getText().toString();
            if (strRoomType.contains(" ")){
                strRoomType = strRoomType.replace(" ", "%20");
            }
            strRoomView = lblRoomView.getText().toString();
            if(strRoomView.contains(" ")){
                strRoomView = strRoomView.replace(" ", "20");
            }
            dblRoomPrice = Double.parseDouble((lblRoomPrice.getText().toString()));

            pDialog = new ProgressDialog(SingleRoomActivity.this);
            pDialog.setTitle("Updating Process");
            pDialog.setMessage("Please wait while updating...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/room/update?roomNumber=" + intRoomNumber +
                        "&roomType=" + strRoomType + "&roomView=" + strRoomView + "&roomPrice=" + dblRoomPrice;
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
                alertDialog.setTitle("Room Updated");
                alertDialog.setMessage("The ROOM was successfully updated on our listing.");
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(getApplicationContext(),
                                RoomsActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
    }

    private class deleteRoom extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            intRoomNumber = Integer.parseInt(lblRoomNumber.getText().toString());
            pDialog = new ProgressDialog(SingleRoomActivity.this);
            pDialog.setTitle("Removing Process");
            pDialog.setMessage("Please wait while removing...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/room/delete?roomNumber=" + intRoomNumber;
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
                alertDialog.setTitle("Room Removed");
                alertDialog.setMessage("The ROOM was successfully removed from our listing.");
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(getApplicationContext(),
                                RoomsActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
    }
}
