package com.awrtechnologies.carbudgetsales.fragements.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.activeandroid.query.Delete;
import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.AppointmentAdapter;
import com.awrtechnologies.carbudgetsales.data.Appointments;
import com.awrtechnologies.carbudgetsales.data.Lanes;
import com.awrtechnologies.carbudgetsales.data.Services;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.hlistview.widget.AdapterView;

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

/**
 * Created by niteshpurohit on 20/08/15.
 */
public class Appointment extends Fragment implements View.OnClickListener, android.widget.AdapterView.OnItemClickListener {

    View view;
    ListView listview;
    AppointmentAdapter adapter;
    List<Appointments> arraylist;
    Button request;
    private SimpleDateFormat webDateFormat;
    boolean alreadyRequested = false;
    RelativeLayout relativemainfooter;
    ServiceStatusTask serviceStatusTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fast_lane, null);
        findviewbyid();
        serviceStatusTask=new ServiceStatusTask();
        serviceStatusTask.execute();
        webDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        arraylist = new ArrayList<Appointments>();
        arraylist = Appointments.getAllAppointments();
        adapter = new AppointmentAdapter(getActivity(), arraylist);
        listview.setAdapter(adapter);
        adapter.setList(arraylist);
        adapter.notifyDataSetChanged();

        /**
         * List Footer
         */

        View footer = inflater.inflate(R.layout.servicefooter, null);
        listview.addFooterView(footer);
        buildFooterView(footer);

        listview.setOnItemClickListener(this);
        checkAppointment();

        return view;
    }

    private void findviewbyid() {
        listview = (ListView) view.findViewById(R.id.listview);
    }

    private void buildFooterView(View footer) {
        request = (Button) footer.findViewById(R.id.loadMoreButton);
        relativemainfooter=(RelativeLayout)footer.findViewById(R.id.relativeMain);
        request.setText("Request");
        request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loadMoreButton) {

            ((MainActivity) getActivity())
                    .openNewFragment(new ServiceRequest(true));

        }

    }


    @Override
    public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {

        ((MainActivity) getActivity())
                .openNewFragment(new AppointmentDetail(arraylist.get(position).getAppointmentid()));

    }

    public void showMessage(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setMessage(msg).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public class ServiceStatusTask extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;



        @Override
        protected String doInBackground(Void... params) {
            try {
                ogrelab.org.apache.http.client.HttpClient httpclient = new ogrelab.org.apache.http.impl.client.DefaultHttpClient();
                User user = User.getUser();
                ogrelab.org.apache.http.client.methods.HttpPost httppost = new ogrelab.org.apache.http.client.methods.HttpPost(Constants.BASEURL
                        + "/api/get_service_status");

                ArrayList<ogrelab.org.apache.http.message.BasicNameValuePair> param = new ArrayList<ogrelab.org.apache.http.message.BasicNameValuePair>();
                ogrelab.org.apache.http.message.BasicNameValuePair appid = new ogrelab.org.apache.http.message.BasicNameValuePair("appid",
                        Constants.APPID);
                param.add(appid);

                ogrelab.org.apache.http.message.BasicNameValuePair serverkey = new ogrelab.org.apache.http.message.BasicNameValuePair("server_key",
                        Constants.SERVER_KEY);
                param.add(serverkey);
                ogrelab.org.apache.http.message.BasicNameValuePair userid = new ogrelab.org.apache.http.message.BasicNameValuePair("userid",
                        user.getUserid());
                param.add(userid);

                httppost.setEntity(new ogrelab.org.apache.http.client.entity.UrlEncodedFormEntity(param));
                ogrelab.org.apache.http.HttpResponse httpresponse = httpclient.execute(httppost);

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
                        lanes.setAppid(getString(lanesobj, "appid"));
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
                        a.setResult(getString(apptobj, "result"));
                        a.save();

                    }
                }
                else if(responsecode==0)
                {
                    showMessage(jsonobj.getString("message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                arraylist = new ArrayList<Appointments>();
                arraylist = Appointments.getAllAppointments();
                adapter = new AppointmentAdapter(getActivity(), arraylist);
                listview.setAdapter(adapter);
                adapter.setList(arraylist);
                adapter.notifyDataSetChanged();
                checkAppointment();
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

    public String convertStreamToString(InputStream inputstream)
            throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = inputstream.read()) != -1) {
            bytestream.write(ch);
        }
        return new String(bytestream.toByteArray(), "UTF-8");
    }

    public void checkAppointment()
    {
        if (arraylist.size() <= 0 || arraylist == null) {
            alreadyRequested = true;
        } else {
            for (int i = 0; i < arraylist.size(); i++) {

                if (arraylist.get(i).getStatus().equals("WAITING") || arraylist.get(i).getResult().equals("NA") || arraylist.get(i).getStatus().equals("ACCEPTED")) {
                    alreadyRequested = false;
                    break;
                } else if (arraylist.get(i).getStatus().equals("DONE")) {

                    alreadyRequested = true;

                }

            }
        }
        if (alreadyRequested) {

            relativemainfooter.setVisibility(View.VISIBLE);

        } else {
            relativemainfooter.setVisibility(View.GONE);
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle("Not Allowed");
//            builder.setMessage("Kindly wait for your turn..");
//            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            builder.show();
        }
    }

    @Override
    public void onPause() {
        serviceStatusTask.isCancelled();
        super.onPause();
    }
}
