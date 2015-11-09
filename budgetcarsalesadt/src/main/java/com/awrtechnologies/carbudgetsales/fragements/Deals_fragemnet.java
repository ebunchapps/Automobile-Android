package com.awrtechnologies.carbudgetsales.fragements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.Dealslistadapter;
import com.awrtechnologies.carbudgetsales.data.DealCategory;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Deals_fragemnet extends Fragment implements OnItemClickListener {
    PullToRefreshListView listview;

    Dealslistadapter dealsadapter;
    User user;
    TextView addText;
    DealsTask dealtask;

    private List<DealCategory> arraylist;

    BroadcastReceiver refreshReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            arraylist = DealCategory.getAll();
            if (arraylist == null) {
                arraylist = new ArrayList<DealCategory>();
            }
            if (dealsadapter != null) {
                dealsadapter.setList(arraylist);
            } else {
                dealsadapter = new Dealslistadapter(getActivity(), arraylist);
                listview.setAdapter(dealsadapter);
            }

        }
    };

    @Override
    public void onResume() {
        getActivity().registerReceiver(
                refreshReciever,
                new IntentFilter(getActivity().getApplicationInfo().packageName
                        + "refresh"));
        super.onResume();
    }




    @Override
    public void onPause() {
        dealtask.isCancelled();
        getActivity().unregisterReceiver(refreshReciever);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dealsview = inflater.inflate(R.layout.dealsfragement, null);
        addText = (TextView) dealsview.findViewById(R.id.addText);
        GeneralHelper.getInstance(getActivity()).setIscheckfragment(false);

        dealtask=new DealsTask();

        ((MainActivity) getActivity()).socialbutton();
        user = User.getUser();
        String userId = user.getUserid();
        PreferencesManager.setPreferenceByKey(getActivity(), "userid", userId);
        ((MainActivity) getActivity()).layoutShown();

        listview = (PullToRefreshListView) dealsview.findViewById(R.id.listviewdeals);

        arraylist = new ArrayList<DealCategory>();
        arraylist = DealCategory.getAll();

        if (arraylist == null || arraylist.size() <= 0) {

            listview.setVisibility(View.GONE);
            addText.setVisibility(View.VISIBLE);
            arraylist = new ArrayList<DealCategory>();
        }

        listview.setVisibility(View.VISIBLE);
        addText.setVisibility(View.GONE);
        dealsadapter = new Dealslistadapter(getActivity(), arraylist);
        listview.setAdapter(dealsadapter);

        ((MainActivity) getActivity()).deals
                .setBackgroundResource(R.drawable.presseddeals);
        ((MainActivity) getActivity()).inventory
                .setBackgroundResource(R.drawable.inventory);
        ((MainActivity) getActivity()).news
                .setBackgroundResource(R.drawable.news);
        ((MainActivity) getActivity()).tools
                .setBackgroundResource(R.drawable.tools);
        ((MainActivity) getActivity()).info
                .setBackgroundResource(R.drawable.information);
        ((MainActivity) getActivity()).service
                .setBackgroundResource(R.drawable.services);


        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                callDealApi();

            }
        });
        listview.setOnItemClickListener(this);

        return dealsview;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PreferencesManager.setPreferenceIntByKey(getActivity(), "dealposition",
                position - 1);

        int catgid = Integer.parseInt(arraylist.get(position - 1).getDealId());
        ((MainActivity) getActivity()).openNewFragment(new Main(catgid));
    }

    public class DealsTask extends AsyncTask<Void, Void, String> {

//		private ProgressDialog mProgressDialog;

        @Override
        protected String doInBackground(Void... params) {

            try {
                User user = User.getUser();
                HttpClient httpclient = new DefaultHttpClient();
                String url = Constants.BASEURL + "/api/get_all?appid="
                        + Constants.APPID + "&" + "server_key="
                        + Constants.SERVER_KEY + "&userid=" + user.getUserid();
//                String urll="http://awrtechnologies.com/automobile/api/get_all?appid=7e0b57b8ec0cb5b2f49cb86911b1366a&server_key=T03moz10tfj5y3K0QFgh5F3AroyRJ60d&userid=1";
//                String urll="http://www.awrtechnologies.com/automobile/api/get_all?appid=uyDuIhAk9PvK1xk2nS9pfzoLg96fF3169OgeXM6pWh4gZUty1IInI7jglPBc6Qil10kJE0hIbIxIIGj3s9k2qbeVKXWVRihhUEeXW68vPTr4ToX9727SRL3FAZ8Z0eBx&server_key=T03moz10tfj5y3K0QFgh5F3AroyRJ60d&userid=37";
                HttpGet httpget = new HttpGet(url);
                HttpResponse httpresponse = httpclient.execute(httpget);

                InputStream inputstream = httpresponse.getEntity().getContent();

                String result = convertStreamToString(inputstream);
                ((MainActivity) getActivity()).processData(result);
            } catch (Exception e) {
//				showMessage("Please Connect to Internet");
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                arraylist = new ArrayList<DealCategory>();
                arraylist = DealCategory.getAll();
                dealsadapter = new Dealslistadapter(getActivity(), arraylist);
                listview.setAdapter(dealsadapter);
                dealsadapter.notifyDataSetChanged();
                listview.onRefreshComplete();
            }catch (Exception e)
            {

            }
            finally {
                listview.onRefreshComplete();
            }
            super.onPostExecute(result);
        }
    }

    public String convertStreamToString(InputStream inputstream)
            throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = inputstream.read()) != -1) {
            bytestream.write(ch);
        }
        return new String(bytestream.toByteArray(), "UTF-8");
    }

    public void callDealApi() {
        try {
            dealtask.execute();
        } catch (Exception e) {

        }
    }


}
