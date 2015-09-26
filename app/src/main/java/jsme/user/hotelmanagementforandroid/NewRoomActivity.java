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

public class NewRoomActivity extends AppCompatActivity {

    private EditText txtRoomNumber;
    private EditText txtRoomType;
    private EditText txtRoomView;
    private EditText txtRoomPrice;
    private ImageButton btnAddNewRoom;
    private ImageButton btnCancelNewRoom;

    private int intRoomNumber = 0;
    private String strRoomType = "";
    private String strRoomView = "";
    private double dblRoomPrice = 0.0;

    private final Context context = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);

        txtRoomNumber = (EditText)findViewById(R.id.txtRoomNumber);
        txtRoomType = (EditText)findViewById(R.id.txtRoomType);
        txtRoomView = (EditText)findViewById(R.id.txtRoomView);
        txtRoomPrice = (EditText)findViewById(R.id.txtRoomPrice);

        btnAddNewRoom = (ImageButton)findViewById(R.id.btnAddNewRoom);
        btnAddNewRoom.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if((!txtRoomNumber.getText().toString().trim().equals("")) && (!txtRoomType.getText().toString().trim().equals("")) &&
                        (!txtRoomView.getText().toString().trim().equals("")) && (!txtRoomPrice.getText().toString().trim().equals(""))){

                    int check1 = 0;
                    int check2 = 0;
                    try{
                        int roomNumCheck = 0;
                        roomNumCheck = Integer.parseInt(txtRoomNumber.getText().toString());
                        check1 = check1 + 1;
                    }
                    catch(Exception e){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Room Number Format");
                        alertDialog.setMessage("Please enter a valid ROOM NUMBER before tapping ADD NEW ROOM.");
                        alertDialog.setIcon(R.drawable.info);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                    try{
                        double roomPriceCheck = 0.0;
                        roomPriceCheck = Double.parseDouble(txtRoomPrice.getText().toString());
                        check2 = check2 + 1;
                    }
                    catch(Exception e){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Room Price Format");
                        alertDialog.setMessage("Please enter a valid ROOM PRICE before tapping ADD NEW ROOM.");
                        alertDialog.setIcon(R.drawable.info);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }

                    if((check1 == 1) && (check2 == 1)){
                        new addNewRoom().execute();
                    }
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Blank Fields");
                    alertDialog.setMessage("Please fill in all the requested fields before tapping ADD NEW ROOM.");
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

        btnCancelNewRoom = (ImageButton)findViewById(R.id.btnCancelNewRoom);
        btnCancelNewRoom.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RoomsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private class addNewRoom extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            intRoomNumber = Integer.parseInt(txtRoomNumber.getText().toString());
            strRoomType = txtRoomType.getText().toString();
            strRoomType = strRoomType.replace(" ", "%20");
            strRoomView = txtRoomView.getText().toString();
            strRoomView = strRoomView.replace(" ", "%20");
            dblRoomPrice = Double.parseDouble(txtRoomPrice.getText().toString());

            pDialog = new ProgressDialog(NewRoomActivity.this);
            pDialog.setTitle("Saving Process");
            pDialog.setMessage("Please wait while saving...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Boolean blnLog = false;
                String url = "http://hotelmanagement-jsme.rhcloud.com/room/create?roomNumber=" + intRoomNumber +
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
                alertDialog.setTitle("Room Successfully Created");
                alertDialog.setMessage("The ROOM was successfully added to listing.");
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
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Room Creation Unsuccessful");
                alertDialog.setMessage("A ROOM with the specified ROOM NUMBER already exists. Please change and try again");
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
