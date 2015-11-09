package com.awrtechnologies.carbudgetsales.fragements.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Appointments;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
 * Created by m004 on 24/08/15.
 */
public class AppointmentDetail extends Fragment implements View.OnClickListener {


    View view;
    TextView textmakemodelyear;
    TextView textstatus;
    TextView textresult;
    TextView textrequestedtime;
    TextView textmessage;
    Appointments appointment;

    String appointmentid;

    Button cancelbutton;
    private SimpleDateFormat webDateFormat;
    private List<Appointments> appointments;
    AppointmentCancelTask appointmentCancelTask;

    public AppointmentDetail() {
    }

    public AppointmentDetail(String appointmentid) {
        this.appointmentid = appointmentid;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.appointmentdetail, null);
        findviewbyId();
        appointmentCancelTask=new AppointmentCancelTask();
        appointments = Appointments.getAllAppointments();
        webDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        appointment = Appointments.getAppointmentsById(appointmentid);
        setDataInTextView();
        cancelbutton.setOnClickListener(this);
        return view;

    }

    private void setDataInTextView() {
        textmakemodelyear.setText(appointment.getMake() + "/" + appointment.getModel() + "/" + appointment.getYear());
        textstatus.setText(appointment.getStatus());
        textmessage.setText(appointment.getMessage());
        textrequestedtime.setText(appointment.getRequest_timestamp());
        textresult.setText(appointment.getResult());
        if (appointment.getStatus().equals("DONE")) {

            cancelbutton.setVisibility(View.GONE);
        } else {
            cancelbutton.setVisibility(View.VISIBLE);
        }

}

    private void findviewbyId() {

        textmakemodelyear = (TextView) view.findViewById(R.id.textmakemodelyear);
        textstatus = (TextView) view.findViewById(R.id.textstatus);
        textresult = (TextView) view.findViewById(R.id.textresult);
        textrequestedtime = (TextView) view.findViewById(R.id.textrequestedtime);
        textmessage = (TextView) view.findViewById(R.id.textmessage);
        cancelbutton = (Button) view.findViewById(R.id.cancelbutton);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancelbutton) {
            appointmentCancelTask.execute();
        }

    }

    @Override
    public void onPause() {
        appointmentCancelTask.isCancelled();
        super.onPause();
    }

    public class AppointmentCancelTask extends AsyncTask<Void, Void, String> {

    ProgressDialog mProgressDialog;

    private JSONObject jsonobject;

    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            User user = User.getUser();
            HttpPost httppost = new HttpPost(Constants.BASEURL
                    + "/api/cancel_appointment");
            ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
            BasicNameValuePair appid = new BasicNameValuePair("appid",
                    Constants.APPID);
            param.add(appid);
            BasicNameValuePair serverkey = new BasicNameValuePair("server_key",
                    Constants.SERVER_KEY);
            param.add(serverkey);
            BasicNameValuePair pasrd = new BasicNameValuePair("id",
                    appointmentid);
            param.add(pasrd);

            httppost.setEntity(new UrlEncodedFormEntity(param));
            HttpResponse httpresponse = httpclient.execute(httppost);

            InputStream inputstream = httpresponse.getEntity().getContent();

            String result = convertStreamToString(inputstream);

            System.out.println("API APPOINTMENT RESULT======" + result);
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
        System.out.println("API APPOINTMNET Post RESULT======" + result);
        try {
            jsonobject = new JSONObject(result);
            int responsecode = jsonobject.getInt("response_code");

            if (responsecode == 1) {

                showMessage(jsonobject.getString("message"), true);

            }
            else if(responsecode==0)
            {
                showMessage(jsonobject.getString("message"),false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void showMessage(String msg, final boolean backcall) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setMessage(msg).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if (backcall == true) {
                            ((MainActivity) getActivity()).back();
                            dialog.cancel();
                        } else if (backcall == false) {
                            dialog.cancel();
                        }


                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


}
