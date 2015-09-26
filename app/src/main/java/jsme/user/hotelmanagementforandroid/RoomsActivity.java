package jsme.user.hotelmanagementforandroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomsActivity extends ListActivity {

    private static final String TAG_ID = "id";
    private static final String TAG_ROOMTYPE = "roomType";
    private static final String TAG_ROOMNUMBER = "roomNumber";
    private static final String TAG_ROOMVIEW = "roomView";
    private static final String TAG_ROOMPRICE = "roomPrice";

    JSONArray rooms = new JSONArray();

    List<HashMap<String, String>> roomList;

    final Context context = this;

    private ProgressDialog pDialog;

    private int intRoomNumber = 0;
    private String strRoomType = "";
    private String strRoomView = "";
    private Double dblRoomPrice = 0.00;

    private ImageButton btnAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        roomList = new ArrayList<HashMap<String, String>>();

        ListView listRoomView = getListView();

        listRoomView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String strRoomNumber = ((TextView) view.findViewById(R.id.lblRoomNumber))
                        .getText().toString();
                String strRoomType = ((TextView) view.findViewById(R.id.lblRoomType))
                        .getText().toString();
                String strRoomView = ((TextView) view.findViewById(R.id.lblRoomView))
                        .getText().toString();
                String strRoomPrice = ((TextView) view.findViewById(R.id.lblRoomPrice))
                        .getText().toString();

                Intent i = new Intent(getApplicationContext(), SingleRoomActivity.class);
                i.putExtra(TAG_ROOMNUMBER, strRoomNumber);
                i.putExtra(TAG_ROOMTYPE, strRoomType);
                i.putExtra(TAG_ROOMVIEW, strRoomView);
                i.putExtra(TAG_ROOMPRICE, strRoomPrice);
                startActivity(i);
                finish();
            }
        });

        new HttpRequestTask().execute();

        btnAddRoom = (ImageButton)findViewById(R.id.btnAddNewRoom);

        btnAddRoom.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        NewRoomActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RoomsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url = "http://hotelmanagement-jsme.rhcloud.com/room/all";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                roomList = restTemplate.getForObject(url, ArrayList.class);
            } catch (Exception e) {
                Log.e("Message Log:", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            //super.onPostExecute(s);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (roomList.size() == 0) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("No Items In List");
                alertDialog.setMessage("Please note that there were no items found in the requested list.");
                alertDialog.setIcon(R.drawable.info);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
            else
            {
                ListAdapter adapter = new SimpleAdapter(RoomsActivity.this, roomList, R.layout.room_list_item,
                        new String[]{TAG_ROOMNUMBER, TAG_ROOMTYPE, TAG_ROOMVIEW, TAG_ROOMPRICE}, new int[]{
                        R.id.lblRoomNumber, R.id.lblRoomType, R.id.lblRoomView, R.id.lblRoomPrice});
                setListAdapter(adapter);
            }
        }
    }
}
