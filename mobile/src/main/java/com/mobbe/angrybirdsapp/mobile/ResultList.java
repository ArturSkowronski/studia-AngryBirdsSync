package com.mobbe.angrybirdsapp.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.angry.web.deviceStateEndpoint.model.DeviceState;
import com.angry.web.deviceStateEndpoint.model.Level;
import com.angry.web.deviceStateEndpoint.model.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artur on 2014-06-12.
 */
public class ResultList extends Activity {

    private Context context;
    private ApplicationState state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_list_activity);
        context = getApplicationContext();
        state=(ApplicationState)getApplication();
        ResultListAdapter adapter = new ResultListAdapter(this,
                R.layout.result_list_activity_view, state.getDeviceStates().getItems());
        ListView listView1 = (ListView) findViewById(R.id.list_view);
        listView1.setAdapter(adapter);
    }

    public class ResultListAdapter extends ArrayAdapter<DeviceState>
    {

        private final ResultList resultItemList;
        private final int result_list_activity_item_view;
        private final List<DeviceState> deviceStates;
        private Context mContext;


        public ResultListAdapter(ResultList resultItemList, int result_list_activity_item_view, List<DeviceState> deviceStates) {
            super(resultItemList, result_list_activity_item_view, deviceStates);
            this.resultItemList = resultItemList;
            this.result_list_activity_item_view = result_list_activity_item_view;
            this.deviceStates = deviceStates;
            mContext=ResultList.this;

        }

        public int getCount()
        {
            // return the number of records in cursor
            return deviceStates.size();
        }

        // getView method is called for each item of ListView
        public View getView(final int position,  View view, ViewGroup parent)
        {
            // inflate the layout for each item of listView
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.result_list_activity_view, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ResultList.this,ResultItemList.class);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });
            DeviceState deviceState= deviceStates.get(position);
            TextView time=(TextView)view.findViewById(R.id.time);
            TextView device=(TextView)view.findViewById(R.id.device);
            time.setText("Czas "+deviceState.getLastSync());
            device.setText("Urządzenie "+deviceState.getDeviceName());
            return view;
        }
    }
}
