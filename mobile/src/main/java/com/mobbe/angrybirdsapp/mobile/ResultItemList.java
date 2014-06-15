package com.mobbe.angrybirdsapp.mobile;

import android.app.Activity;
import android.content.Context;
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
import com.angry.web.deviceStateEndpoint.model.DeviceStateCollection;
import com.angry.web.deviceStateEndpoint.model.Level;
import com.angry.web.deviceStateEndpoint.model.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artur on 2014-06-12.
 */
public class ResultItemList extends Activity {
    private Context context;
    private ApplicationState state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_list_activity_item);
        context = getApplicationContext();

        state=(ApplicationState)getApplication();
        Bundle extras = getIntent().getExtras();
        int position= extras.getInt("position");;
        ArrayList<ResultWrapper> resultWrappers= new ArrayList<ResultWrapper>();
        DeviceState deviceState= state.getDeviceStates().getItems().get(position);
        for(World world: deviceState.getWorldArrayList()){
            if(world.getLevelArrayList()!=null)
                for(Level level: world.getLevelArrayList()){
                    ResultWrapper resultWrapper = new ResultWrapper();
                    resultWrapper.setWorld(world);
                    resultWrapper.setLevel(level);
                    resultWrappers.add(resultWrapper);
                }
            }
        ResultItemListAdapter adapter = new ResultItemListAdapter(this,
                R.layout.result_list_activity_item_view, resultWrappers);
        ListView listView1 = (ListView) findViewById(R.id.list_view);
        listView1.setAdapter(adapter);
    }

    public class ResultItemListAdapter  extends ArrayAdapter<ResultWrapper>
    {

        private final ResultItemList resultItemList;
        private final int result_list_activity_item_view;
        private final List<ResultWrapper> resultWrappers;
        private Context mContext;
        private int is=0;


        public ResultItemListAdapter(ResultItemList resultItemList, int result_list_activity_item_view, List<ResultWrapper> resultWrappers) {
            super(resultItemList, result_list_activity_item_view, resultWrappers);
            this.resultItemList = resultItemList;
            this.result_list_activity_item_view = result_list_activity_item_view;
            this.resultWrappers = resultWrappers;
            mContext=ResultItemList.this;

        }

        public int getCount()
        {
            // return the number of records in cursor
            return resultWrappers.size();
        }

        // getView method is called for each item of ListView
        public View getView(int position,  View view, ViewGroup parent)
        {
            // inflate the layout for each item of listView
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.result_list_activity_item_view, null);
            is=is+1;
            ResultWrapper resultWrapper= resultWrappers.get(position);
            TextView world=(TextView)view.findViewById(R.id.world);
            TextView level=(TextView)view.findViewById(R.id.level);
            TextView result=(TextView)view.findViewById(R.id.result);
            world.setText("World "+resultWrapper.getWorld().getId());
            level.setText("Level " + is);
            int value=resultWrapper.getLevel().getResult().getValue()+is*24;
            result.setText("Result "+value);
            return view;
        }
    }

}
