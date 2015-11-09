package com.awrtechnologies.carbudgetsales.fragements.service;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Point;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.Spinneradapter;
import com.awrtechnologies.carbudgetsales.data.Appointments;
import com.awrtechnologies.carbudgetsales.data.Services;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by niteshpurohit on 20/08/15.
 */
public class ServiceRequest extends Fragment implements View.OnClickListener {

    RadioGroup radiogroup;
    List<VehicleInfo> vehiclelist;
    Spinneradapter sadapter;
    Spinner spinnerCar;
    Point screensize;
    Button submit;
    TextView date;

    EditText description;
    EditText kms;

    String carId;
    String makeManual;
    String modelManual;
    String yearManual;
    String kmsManual;
    String descrp;

    EditText makeManualText;
    EditText modelManualText;
    EditText yearManualText;
    EditText kmsManualText;
    View service;

    ArrayList<String> makeModel;
    LinearLayout relativlayoutSelect;
    RelativeLayout relativlayoutManual;
    int mode = 2;

    String makebyUser;
    String modelbyUser;
    String yearbyUser;
    TextView title;
    boolean checkappointmnet;
    String dateString;
    private int mYear, mMonth, mDay;
    RadioButton select;
    String laneID;
    FastServiceTask fastServiceTask;
    AppointmentServiceTask appointmentServiceTask;




    public ServiceRequest(boolean checkappointmnet) {
        this.checkappointmnet = checkappointmnet;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        service = inflater.inflate(R.layout.service_request, null);
        screensize = GeneralHelper.getInstance(getActivity()).getScreenSize();
        findviewbyid();
        fastServiceTask=new FastServiceTask();
        appointmentServiceTask=new AppointmentServiceTask();
        Log.d("Pawan","laneID=="+laneID);

        if (checkappointmnet == true) {
            date.setVisibility(View.VISIBLE);
            title.setText("Appointment");
        } else {
            date.setVisibility(View.GONE);
            title.setText("Quick Service");
        }
        vehiclelist = new ArrayList<VehicleInfo>();
        makeModel = new ArrayList<String>();
        vehiclelist = VehicleInfo.getAll();

        if (vehiclelist.size() <= 0) {
            radiogroup.check(R.id.manual);
            select.setVisibility(View.GONE);
            relativlayoutSelect.setVisibility(View.GONE);
            relativlayoutManual.setVisibility(View.VISIBLE);
        } else {
            select.setVisibility(View.VISIBLE);
            relativlayoutSelect.setVisibility(View.GONE);
            relativlayoutManual.setVisibility(View.VISIBLE);
            radiogroup.check(R.id.manual);
        }
        makeModel.add("Select");

        for (int i = 0; i < vehiclelist.size(); i++) {
            makeModel.add(vehiclelist.get(i).getMake() + vehiclelist.get(i).getModel());
        }
        sadapter = new Spinneradapter(getActivity(), makeModel);
        spinnerCar.setAdapter(sadapter);


        radiogroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton rb = (RadioButton) group
                                .findViewById(checkedId);
                        if (null != rb && checkedId > -1) {

                            if (rb.getText().toString().equals("Manual")) {
                                mode = 2;
                                relativlayoutSelect.setVisibility(View.GONE);
                                relativlayoutManual.setVisibility(View.VISIBLE);

                            } else {
                                mode = 1;
                                relativlayoutSelect.setVisibility(View.VISIBLE);
                                relativlayoutManual.setVisibility(View.GONE);

                            }
                        }

                    }
                });


        setParametersForSpinner(spinnerCar);

        spinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int pos = position - 1;
                if (!makeModel.get(position).equals("Select")) {
                    carId = vehiclelist.get(pos).getVehicleid();
                    makebyUser = vehiclelist.get(pos).getMake();
                    modelbyUser = vehiclelist.get(pos).getModel();
                    yearbyUser = vehiclelist.get(pos).getYear();
                } else {

                    carId = null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        onClicklistener();
        return service;
    }

    private void findviewbyid() {
        radiogroup = (RadioGroup) service.findViewById(R.id.radioGroup);
        spinnerCar = (Spinner) service.findViewById(R.id.spinner);
        description = (EditText) service.findViewById(R.id.edittext);
        kms = (EditText) service.findViewById(R.id.textkms);
        makeManualText = (EditText) service.findViewById(R.id.textmake);
        modelManualText = (EditText) service.findViewById(R.id.textmodel);
        yearManualText = (EditText) service.findViewById(R.id.textyear);
        kmsManualText = (EditText) service.findViewById(R.id.txtkms);
        submit = (Button) service.findViewById(R.id.buttonsubmit);
        relativlayoutManual = (RelativeLayout) service
                .findViewById(R.id.relativemanual);
        relativlayoutSelect = (LinearLayout) service
                .findViewById(R.id.relativeselect);
        date = (TextView) service.findViewById(R.id.date);
        title = (TextView) service.findViewById(R.id.title);
        select = (RadioButton) service.findViewById(R.id.select);
    }

    private void onClicklistener() {
        submit.setOnClickListener(this);
        date.setOnClickListener(this);
    }

    public void setParametersForSpinner(Spinner spinner) {

        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) spinner.getLayoutParams();
        lp.height = screensize.y / 15;
        // lp.width = screensize.x / 2;
        spinner.setLayoutParams(lp);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.date) {
            datePickerDialog();
        } else if (v.getId() == R.id.buttonsubmit) {

            if (mode == 1) {
                makeManual = makebyUser;
                modelManual = modelbyUser;
                yearManual = yearbyUser;
                kmsManual = kms.getText().toString();
                descrp = description.getText().toString();

            } else if (mode == 2) {
                carId = "0";
                makeManual = makeManualText.getText().toString();
                modelManual = modelManualText.getText().toString();
                yearManual = yearManualText.getText().toString();
                kmsManual = kmsManualText.getText().toString();
                descrp = description.getText().toString();

            }

            try {
                if (checkappointmnet == false) {
                    if (makeManual.isEmpty() || modelManual.isEmpty() || yearManual.isEmpty() || kmsManual.isEmpty() || descrp.isEmpty() || carId.isEmpty()) {
                        showMessage("Please Fill all Fields", false);
                    } else {
                        fastServiceTask.execute();
                    }
                } else if (checkappointmnet == true) {
                    if (makeManual.isEmpty() || modelManual.isEmpty() || yearManual.isEmpty() || kmsManual.isEmpty() || descrp.isEmpty() || carId.isEmpty() || date.getText().equals("Select Date")) {
                        showMessage("Please Fill all Fields", false);
                    } else {
                        appointmentServiceTask.execute();
                    }
                }
            }catch (Exception e)
            {
                showMessage("Please Fill all Fields", false);
            }
            emptyField();



        }
    }

    @Override
    public void onPause() {
        fastServiceTask.isCancelled();
        appointmentServiceTask.isCancelled();
        super.onPause();
    }

    public void emptyField() {
        description.setText("");
        kms.setText("");
        makeManualText.setText("");
        modelManualText.setText("");
        yearManualText.setText("");
        kmsManualText.setText("");
    }


    public class FastServiceTask extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;

        private JSONObject jsonobject;

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                User user = User.getUser();
                HttpPost httppost = new HttpPost(Constants.BASEURL
                        + "/api/request_fast_service");

                ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
                BasicNameValuePair appid = new BasicNameValuePair("appid",
                        Constants.APPID);
                param.add(appid);
                BasicNameValuePair serverkey = new BasicNameValuePair("server_key",
                        Constants.SERVER_KEY);
                param.add(serverkey);
                BasicNameValuePair pasrd = new BasicNameValuePair("make",
                        makeManual);
                param.add(pasrd);
                BasicNameValuePair pasrdd = new BasicNameValuePair("model",
                        modelManual);
                param.add(pasrdd);
                BasicNameValuePair year = new BasicNameValuePair("year",
                        yearManual);
                param.add(year);
                BasicNameValuePair kms = new BasicNameValuePair("kms",
                        kmsManual);
                param.add(kms);
                BasicNameValuePair descrpp = new BasicNameValuePair(
                        "message", descrp);
                param.add(descrpp);
                BasicNameValuePair carid = new BasicNameValuePair("carid",
                        carId);
                param.add(carid);
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
            System.out.println("API Fast Service Post RESULT======" + result);
            try {
                jsonobject = new JSONObject(result);
                int responsecode = jsonobject.getInt("response_code");

                if (responsecode == 1) {
//					Toast.makeText(getActivity(),
//							jsonobject.getString("message"), Toast.LENGTH_LONG)
//							.show();
                    showMessage(jsonobject.getString("message"), true);
//                    ((com.awrtechnologies.carbudgetsales.MainActivity) getActivity()).callRefreshService();

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


    public class AppointmentServiceTask extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;

        private JSONObject jsonobject;

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                User user = User.getUser();
                HttpPost httppost = new HttpPost(Constants.BASEURL
                        + "/api/request_service_appointment");
                ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
                BasicNameValuePair appid = new BasicNameValuePair("appid",
                        Constants.APPID);
                param.add(appid);
                BasicNameValuePair serverkey = new BasicNameValuePair("server_key",
                        Constants.SERVER_KEY);
                param.add(serverkey);
                BasicNameValuePair pasrd = new BasicNameValuePair("make",
                        makeManual);
                param.add(pasrd);
                BasicNameValuePair pasrdd = new BasicNameValuePair("model",
                        modelManual);
                param.add(pasrdd);
                BasicNameValuePair year = new BasicNameValuePair("year",
                        yearManual);
                param.add(year);
                BasicNameValuePair kms = new BasicNameValuePair("kms",
                        kmsManual);
                param.add(kms);
                BasicNameValuePair descrpp = new BasicNameValuePair(
                        "message", descrp);
                param.add(descrpp);
                BasicNameValuePair carid = new BasicNameValuePair("carid",
                        carId);
                param.add(carid);
                BasicNameValuePair userid = new BasicNameValuePair("userid",
                        user.getUserid());
                param.add(userid);
                BasicNameValuePair time = new BasicNameValuePair("requestTime", dateString);
                param.add(time);
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


    // datePicker
    public void datePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        // Display Selected date in textbox

                        Calendar choosen = Calendar.getInstance();
                        choosen.set(year, monthOfYear, dayOfMonth);
                        if (choosen.get(Calendar.DAY_OF_MONTH) == mDay) {
                            Toast.makeText(getActivity(), "Invalid Date..Sorry, Not Choose Today Date!!!", Toast.LENGTH_LONG).show();
                            date.setText("Select Date");
                            return;

                        } else if (choosen.compareTo(c) < 0) {
                            Toast.makeText(getActivity(), "Invalid Date..", Toast.LENGTH_LONG).show();
                            date.setText("Select Date");
                            return;
                        } else if (choosen.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            Toast.makeText(getActivity(), "Invalid Date..Sorry, Not Choose SUNDAY!!!", Toast.LENGTH_LONG).show();
                            date.setText("Select Date");
                            return;


                        } else if (choosen.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                            Toast.makeText(getActivity(), "Invalid Date..Sorry, Not Choose SATURDAY!!!", Toast.LENGTH_LONG).show();
                            date.setText("Select Date");
                            return;
                        } else {
//                            date.setText(year + "-" + (monthOfYear + 1)
//                                    + "-" + dayOfMonth);
                            Date datetime = choosen.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            dateString = sdf.format(datetime);
                            date.setText(dateString);
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }
}
