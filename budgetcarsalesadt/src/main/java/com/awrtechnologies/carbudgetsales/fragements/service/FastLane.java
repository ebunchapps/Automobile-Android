package com.awrtechnologies.carbudgetsales.fragements.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.LanesAdapter;
import com.awrtechnologies.carbudgetsales.data.Appointments;
import com.awrtechnologies.carbudgetsales.data.Lanes;
import com.awrtechnologies.carbudgetsales.data.Services;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ogrelab.org.apache.http.HttpResponse;
import ogrelab.org.apache.http.client.HttpClient;
import ogrelab.org.apache.http.client.entity.UrlEncodedFormEntity;
import ogrelab.org.apache.http.client.methods.HttpPost;
import ogrelab.org.apache.http.impl.client.DefaultHttpClient;
import ogrelab.org.apache.http.message.BasicNameValuePair;


/**
 * Created by niteshpurohit on 20/08/15.
 */
public class FastLane extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listview;
    private List<Lanes> lanesarraylist;
    private LanesAdapter laneadapter;
    private SimpleDateFormat webDateFormat;
    private List<Services> services;
    Button viewDetail;
    ServiceStatusTask serviceStatusTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fast_lane, null);
        serviceStatusTask=new ServiceStatusTask();
        services = Services.getAllServices();
        webDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        listview = (ListView) view.findViewById(R.id.listview);
        listview.setOnItemClickListener(this);

        /**
         * List Footer
         */

        View footer = inflater.inflate(R.layout.servicefooter, null);
        listview.addFooterView(footer);
        buildFooterView(footer);

        serviceStatusTask.execute();
        return view;
    }

    @Override
    public void onPause() {
        serviceStatusTask.isCancelled();

        super.onPause();
    }

    private void buildFooterView(View footer) {
        viewDetail = (Button) footer.findViewById(R.id.loadMoreButton);
        viewDetail.setText("Check into reserve a spot");
        viewDetail.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean alreadyRequested = false;
        Date booked_until = null;
        Date nowDate = new Date();
        if (services != null) {


            outer:
            for (Services service : services) {

                if (service.getStatus().equals("DONE")) {
                    alreadyRequested = true;
                }

//                if(alreadyRequested) {
                try {
                    booked_until = webDateFormat.parse(service.getRequest_timestamp());
                } catch (ParseException e) {
                    alreadyRequested = false;
                }

                if (booked_until.getTime() > nowDate.getTime()) {
                    alreadyRequested = true;
                    break outer;
                } else {
                    alreadyRequested = false;
                }
//                }
            }
        }

        if (alreadyRequested) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Not Allowed");
            builder.setMessage("You have already requested a service today, kindly wait for your turn, wait time is shown in front of the lane");
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        } else {
//            ((com.awrtechnologies.carbudgetsales.MainActivity) getActivity())
//                    .openNewFragment(new ServiceRequest(lanesarraylist.get(position).getLaneid()));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loadMoreButton) {
            ((MainActivity) getActivity()).openNewFragment(new FastLaneService());
        }
    }

    public class ServiceStatusTask extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;

        private JSONObject jsonobject;

        public String convertStreamToString(InputStream inputstream)
                throws IOException {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = inputstream.read()) != -1) {
                bytestream.write(ch);
            }
            return new String(bytestream.toByteArray(), "UTF-8");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                User user = User.getUser();
                HttpPost httppost = new HttpPost(Constants.BASEURL
                        + "/api/get_service_status");

                ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
                BasicNameValuePair appid = new BasicNameValuePair("appid",
                        Constants.APPID);
                param.add(appid);

                BasicNameValuePair serverkey = new BasicNameValuePair("server_key",
                        Constants.SERVER_KEY);
                param.add(serverkey);
                BasicNameValuePair userid = new BasicNameValuePair("userid",
                        user.getUserid());
                param.add(userid);

                httppost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpresponse = httpclient.execute(httppost);

                InputStream inputstream = httpresponse.getEntity().getContent();

                String result = convertStreamToString(inputstream);

                System.out.println("API RESULT======" + result);
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            try {
                JSONObject jsonobj = new JSONObject(result);
                int responsecode = jsonobj.getInt("response_code");
                if (responsecode == 1) {
                    new Delete().from(Lanes.class).execute();
                    JSONObject responseobj = jsonobj.getJSONObject("response");
                    JSONArray lanesarray = responseobj.getJSONArray("lanes");
                    for (int i = 0; i < lanesarray.length(); i++) {
                        JSONObject lanesobj = lanesarray.getJSONObject(i);
                        Lanes lanes = new Lanes();
                        lanes.setAppid(getString(lanesobj,"appid"));
                        lanes.setBooked_until(getString(lanesobj, "booked_until"));
                        lanes.setLaneid(getString(lanesobj, "id"));
                        lanes.setLanename(getString(lanesobj, "lanename"));
                        lanes.setService_request_id(getString(lanesobj, "service_request_id"));
                        lanes.save();
                    }

                    new Delete().from(Services.class).execute();
                    JSONArray servicearray = responseobj.getJSONArray("services");
                    for (int i = 0; i < servicearray.length(); i++) {
                        JSONObject serviceobj = servicearray.getJSONObject(i);
                        Services s = new Services();
                        s.setCar_id(getString(serviceobj, "car_id"));
                        s.setAppid(getString(serviceobj, "appid"));
                        s.setUser_id(getString(serviceobj, "user_id"));
                        s.setLane_id(getString(serviceobj, "lane_id"));
                        s.setMake(getString(serviceobj, "make"));
                        s.setMessage(getString(serviceobj, "message"));
                        s.setModel(getString(serviceobj, "model"));
                        s.setRequest_timestamp(getString(serviceobj, "request_timestamp"));
                        s.setResult(getString(serviceobj, "result"));
                        s.setServiceid(getString(serviceobj, "id"));
                        s.setStatus(getString(serviceobj, "status"));
                        s.setCompleted_timestamp(getString(serviceobj, "completed_timestamp"));
                        s.setYear(getString(serviceobj, "year"));
                        s.save();
                    }

                    new Delete().from(Appointments.class).execute();
                    JSONArray appointmentarray = responseobj.getJSONArray("appointments");
                    for (int i = 0; i < appointmentarray.length(); i++) {
                        JSONObject apptobj = appointmentarray.getJSONObject(i);
                        Appointments a = new Appointments();
                        a.setCar_id(getString(apptobj, "car_id"));
                        a.setYear(getString(apptobj, "year"));
                        a.setCompleted_timestamp(getString(apptobj, "completed_timestamp"));
                        a.setUser_id(getString(apptobj, "user_id"));
                        a.setStatus(getString(apptobj, "status"));
                        a.setAppid(getString(apptobj, "appid"));
                        a.setAppointmentid(getString(apptobj, "id"));
                        a.setMake(getString(apptobj, "make"));
                        a.setMessage(getString(apptobj, "message"));
                        a.setModel(getString(apptobj, "model"));
                        a.setRequest_timestamp(getString(apptobj, "request_timestamp"));
                        a.setResult(getString(apptobj,"result"));
                        a.save();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lanesarraylist = Lanes.getAllLanes();
                services = Services.getAllServices();
                laneadapter = new LanesAdapter(getActivity(), lanesarraylist);
                listview.setAdapter(laneadapter);
            }
        }

    }

    public String getString(JSONObject obj, String key) {
        try {
            return obj.getString(key);
        } catch (Exception e) {
            return "";
        }
    }
}
