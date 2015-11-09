package com.awrtechnologies.carbudgetsales.fragements.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ogrelab.org.apache.http.HttpResponse;
import ogrelab.org.apache.http.client.HttpClient;
import ogrelab.org.apache.http.client.entity.UrlEncodedFormEntity;
import ogrelab.org.apache.http.client.methods.HttpPost;
import ogrelab.org.apache.http.impl.client.DefaultHttpClient;
import ogrelab.org.apache.http.message.BasicNameValuePair;

/**
 * Created by m004 on 27/08/15.
 */
public class FastLaneService extends Fragment implements View.OnClickListener {

    TextView timerhours;
    TextView timerminutes;
    TextView lanestatus;
    Button request;
    private List<Lanes> lanesarraylist;
    private SimpleDateFormat webDateFormat;
    private List<Services> services;
    boolean alreadyRequestedlanes = false;
    boolean checkemptylane = true;
    boolean checknotServiceRequest = false;
    boolean alreadyRequested = false;
    String serviceId;
    View view;
    ServiceStatusTask serviceStatusTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fastlaneservice, null);
        findViewbyid();
        lanestatus.setText("Lane is Empty");
        timerhours.setVisibility(View.GONE);
        timerminutes.setVisibility(View.GONE);

        serviceStatusTask=new ServiceStatusTask();
        serviceStatusTask.execute();
        webDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        checkServiceRequest();

        onClickListener();
        blinkText();
        return view;
    }

    @Override
    public void onPause() {
        serviceStatusTask.isCancelled();
        super.onPause();
    }

    private void blinkText() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 2000;    //in milissegunds
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Animation hoursanimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shrink_fade_out_center);
                            lanestatus.startAnimation(hoursanimation);
                        } catch (Exception e) {

                        }
                        blinkText();
                    }
                });
            }
        }).start();
    }

    private void onClickListener() {
        request.setOnClickListener(this);
    }

    private void findViewbyid() {
        request = (Button) view.findViewById(R.id.requestserviceButton);
        timerhours = (TextView) view.findViewById(R.id.timerhours);
        timerminutes = (TextView) view.findViewById(R.id.timerminutes);
        lanestatus = (TextView) view.findViewById(R.id.lanestatus);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DS_DIGIB.TTF");
        timerhours.setTypeface(tf);
        timerminutes.setTypeface(tf);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.requestserviceButton) {
            Log.d("Pawan", "checkemptylane--" + checkemptylane);
            if (checkemptylane) {
                ((MainActivity) getActivity()).openNewFragment(new ServiceRequest(false));
            } else {
//                request.setVisibility(View.GONE);
                ((MainActivity)getActivity()).openNewFragment(new QuickService(serviceId));
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Not Allowed");
//                builder.setMessage("Kindly wait for your turn, wait time is shown in front of you");
//                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.show();
            }
        }
    }


    public void startTimer(long totalMilisecondsForTimer, final TextView timerhours, final TextView timerminutes) {

        Log.d("Pawan","Timer");
        CountDownTimer timer = new CountDownTimer(totalMilisecondsForTimer, 60000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

            public void onTick(long millisUntilFinished) {
                int totalSecs = (int) (millisUntilFinished / 1000);
                int hours = totalSecs / 3600;
                int minutes = (totalSecs % 3600) / 60;
                int seconds = totalSecs % 60;


                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            Animation hoursanimation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
                            timerhours.startAnimation(hoursanimation);
                        } catch (Exception e) {

                        }

                    }
                }, 3600000);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            Animation minanimation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
                            timerminutes.startAnimation(minanimation);
                        } catch (Exception e) {

                        }

                    }
                }, 60000);

                if (hours <= 9) {
                    timerhours.setText("0" + hours + " ");
                } else {
                    timerhours.setText(hours + " ");
                }

                if (minutes <= 9) {
                    timerminutes.setText("0" + minutes + " ");
                } else {
                    timerminutes.setText(minutes + " ");
                }
            }

            public void onFinish() {
                try{
                    new ServiceStatusTask().execute();
                }catch (Exception e)
                {

                }
                lanestatus.setText("Lane is Empty");
                timerhours.setVisibility(View.GONE);
                timerminutes.setVisibility(View.GONE);
            }
        };
        timer.start();
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
                checkServiceRequest();
            }
        }

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
    public String getString(JSONObject obj, String key) {
        try {
            return obj.getString(key);
        } catch (Exception e) {
            return "";
        }
    }

    public void checkServiceRequest()
    {
        lanesarraylist = new ArrayList<Lanes>();
        lanesarraylist = Lanes.getAllLanes();
        services = Services.getAllServices();
        ArrayList<Long> timelist = new ArrayList<Long>();



        /*
         Show Empty Lane
         */


        if (services == null || services.size() <= 0) {

            checknotServiceRequest = true;

        } else {

            Date booked_until = null;
            Date nowDate = new Date();
            for (Services service : services) {

                try {
                    booked_until = webDateFormat.parse(service.getRequest_timestamp());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (booked_until.getTime() > nowDate.getTime()) {
                    alreadyRequested = true;
                    serviceId=service.getServiceid();
                    Log.d("Pawan", "booked_until====" + booked_until + "  " + "nowDate.getTime==" + nowDate);
                    break;
                }
                else
                {
                    alreadyRequested=false;
                }
            }
            if (alreadyRequested) {
//                request.setVisibility(View.GONE);
                checkemptylane = false;
                request.setText("Check into reserve a spot");
                lanestatus.setText("Already in Queue");
                timerhours.setVisibility(View.VISIBLE);
                timerminutes.setVisibility(View.VISIBLE);
                Log.d("Pawan", "Already queue time" + (booked_until.getTime() - nowDate.getTime()));
                startTimer(booked_until.getTime() - nowDate.getTime(), timerhours, timerminutes);
                Log.d("Pawan", "Already queue time" + (booked_until.getTime() - nowDate.getTime()));
                Log.d("Pawan", "booked_until time" + booked_until);
            }
            else {
                request.setText("Request For Service");
                request.setVisibility(View.VISIBLE);
                checknotServiceRequest = true;
            }
        }
        if (checknotServiceRequest) {
            for (int i = 0; i < lanesarraylist.size(); i++) {
                try {

                    if (lanesarraylist.get(i).getBooked_until().equals("null")) {
                        lanestatus.setText("Lane is Empty");
                        timerhours.setVisibility(View.GONE);
                        timerminutes.setVisibility(View.GONE);
                        checkemptylane = true;
                        break;

                    }
                    Date booked_until = webDateFormat.parse(lanesarraylist.get(i).getBooked_until());
                    Date nowDate = new Date();
                    if (booked_until.getTime() > nowDate.getTime()) {
                        timelist.add(booked_until.getTime() - nowDate.getTime());
                        timerhours.setVisibility(View.VISIBLE);
                        timerminutes.setVisibility(View.VISIBLE);
                        lanestatus.setText("Request For Service");
                        try {
                            long minIndex = timelist.indexOf(Collections.min(timelist));
                            int index = (int) minIndex;

                            startTimer(timelist.get(index), timerhours, timerminutes);
                            checkemptylane = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    } else {
                        Log.d("Pawan", "Lane is empty");
                        lanestatus.setText("Lane is Empty");
                        timerhours.setVisibility(View.GONE);
                        timerminutes.setVisibility(View.GONE);
                        checkemptylane = true;
                        break;
                    }
                } catch (ParseException e) {
                    lanestatus.setText("Lane is Empty");
                    timerhours.setVisibility(View.GONE);
                    timerminutes.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }

    }

}
