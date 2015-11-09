package com.awrtechnologies.carbudgetsales.fragements.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.SpinnerVehicle;
import com.awrtechnologies.carbudgetsales.adapter.SpinnerYear;
import com.awrtechnologies.carbudgetsales.adapter.SpinnermainAdapter;
import com.awrtechnologies.carbudgetsales.data.ModelDetail;
import com.awrtechnologies.carbudgetsales.data.ServiceDetail;
import com.awrtechnologies.carbudgetsales.data.VehiclesDetail;
import com.awrtechnologies.carbudgetsales.data.YearDetail;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.PDFHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by niteshpurohit on 20/08/15.
 */
public class Maintenance extends Fragment implements View.OnClickListener {


    private static int highKMS;
    private static int highMONTH;
    private static int highMONTHANDKMS;
    View view;
    Spinner spinnerModel;
    Spinner spinnerYear;
    Spinner spinnerVehicle;
    EditText kms;
    EditText month;

    TextView txtcar;
    TextView txtjeep;
    TextView txtvan;
    TextView truck;
    TextView price;
    TextView description;
    TextView seletcategory;


    TextView serachInfo;

    ArrayList<String> arr;

    SpinnerYear spinneradpteryear;
    SpinnerVehicle spinneradptervehicle;

    public LinearLayout linearModel;
    public LinearLayout linearYear;
    public LinearLayout linearVehicle;
    LinearLayout layoutkmsandmonth;
    LinearLayout layoutdescandprice;
    LinearLayout linearTabs;

    ArrayList<String> selectModel;
    ArrayList<String> selectYear;
    ArrayList<String> selectVehicle;
    ArrayList<String> selectKms;
    ArrayList<String> selectMonth;
    ArrayList<View> arraylistview;

    List<com.awrtechnologies.carbudgetsales.data.Maintenance> maintenance;
    List<ModelDetail> modelDetail;
    List<ServiceDetail> serviceDetail;
    List<YearDetail> yearDetail;
    List<VehiclesDetail> vehicleDetails;
    SpinnermainAdapter sa;
    TextView text;

    String selectsearchkms;
    String selectsearchmonth;

    ArrayList<Integer> subtractkmslist;

    ArrayList<Integer> subtractmonthlist;

    TextView searchkmsandmonth;
    Button pdfbutton;
    String pdflink;
    String vehicleName;
    TextView textadd;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.maintenance, null);
        findviewbyid();
        arraylistview = new ArrayList<View>();
        maintenance = new ArrayList<>();
        serviceDetail = new ArrayList<ServiceDetail>();


        addCategories();


        arr = new ArrayList<String>();
        selectVehicle = new ArrayList<>();
        selectVehicle.add("Select Vehicle");
        for (int i = 0; i < arr.size(); i++) {
            selectVehicle.add(arr.get(i).toString());
        }
        spinneradptervehicle = new SpinnerVehicle(getActivity(), arr);
        spinnerVehicle.setAdapter(spinneradptervehicle);

        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String modelName;
                String spn_data = (String) parent.getItemAtPosition(position);
                int positionmodel = position - 1;

                if (!selectModel.get(position).equals("Select Model")) {

                    modelName = modelDetail.get(positionmodel).getName();
                    yearDetail = new ArrayList<YearDetail>();
                    yearDetail = YearDetail.getAll();
                    for (int i = 0; i < yearDetail.size(); i++) {
                        Log.d("Pawan", yearDetail.get(i).getMainId());
                        Log.d("Pawan", yearDetail.get(i).getModelId());
                        Log.d("Pawan", yearDetail.get(i).getName());
                    }

                    yearDetail = YearDetail.getYearByModelId(modelDetail.get(positionmodel).getMainId());


                    selectYear = new ArrayList<>();
                    selectYear.add("Select Year");

                    for (int i = 0; i < yearDetail.size(); i++) {


                        selectYear.add(yearDetail.get(i).getName());

                    }


                    spinneradpteryear = new SpinnerYear(getActivity(), selectYear);
                    spinnerYear.setAdapter(spinneradpteryear);
                    linearYear.setVisibility(View.VISIBLE);
                    pdfbutton.setVisibility(View.GONE);
                    layoutdescandprice.setVisibility(View.GONE);
                    layoutkmsandmonth.setVisibility(View.GONE);
                    serachInfo.setVisibility(View.GONE);
                    kms.setText("");
                    month.setText("");
                } else {

                    linearYear.setVisibility(View.INVISIBLE);

                    modelName = null;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String yearName;
                String spn_data = (String) parent.getItemAtPosition(position);

                int pos = position - 1;

                if (!selectYear.get(position).equals("Select Year")) {

                    yearName = yearDetail.get(pos).getName();
                    vehicleDetails = new ArrayList<VehiclesDetail>();
                    vehicleDetails = VehiclesDetail.getVehicleByModelId(yearDetail.get(pos).getMainId());
                    selectVehicle = new ArrayList<>();
                    selectVehicle.add("Select Vehicle");
                    for (int i = 0; i < vehicleDetails.size(); i++) {
                        selectVehicle.add(vehicleDetails.get(i).getName());
                    }


                    spinneradptervehicle = new SpinnerVehicle(getActivity(), selectVehicle);
                    spinnerVehicle.setAdapter(spinneradptervehicle);
                    linearVehicle.setVisibility(View.VISIBLE);
                    pdfbutton.setVisibility(View.GONE);
                    layoutdescandprice.setVisibility(View.GONE);
                    layoutkmsandmonth.setVisibility(View.GONE);
                    serachInfo.setVisibility(View.GONE);
                    kms.setText("");
                    month.setText("");
                } else {

                    linearVehicle.setVisibility(View.INVISIBLE);

                    yearName = null;
                }
//
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });

        spinnerVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String spn_data = (String) parent.getItemAtPosition(position);


                int pos = position - 1;

                if (!selectVehicle.get(position).equals("Select Vehicle")) {

                    vehicleName = vehicleDetails.get(pos).getName();
                    layoutkmsandmonth.setVisibility(View.VISIBLE);
                    serachInfo.setVisibility(View.VISIBLE);
                    pdfbutton.setVisibility(View.VISIBLE);
                    kms.setText("");
                    month.setText("");
                    pdflink = vehicleDetails.get(pos).getPdf();
                    serviceDetail = ServiceDetail.getServicesbyVehicleId(vehicleDetails.get(pos).getMainId());
                    selectKms = new ArrayList<String>();
                    selectMonth = new ArrayList<String>();

                    for (int i = 0; i < serviceDetail.size(); i++) {
                        selectKms.add(serviceDetail.get(i).getKms());
                        selectMonth.add(serviceDetail.get(i).getMonth());

                    }
                } else {

                    vehicleName = null;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        kms.setOnClickListener(this);
        month.setOnClickListener(this);
        searchkmsandmonth.setOnClickListener(this);
        pdfbutton.setOnClickListener(this);
        return view;
    }


    public void selectPricebykms() {
        int selectkms = 0;


        try {
            int key = Integer.parseInt(selectsearchkms);
            subtractkmslist = new ArrayList<Integer>();

            for (int i = 0; i < serviceDetail.size(); i++) {

                Log.d("Pawan", "Service Detail Kms List" + serviceDetail.get(i).getKms());
                selectkms = Integer.parseInt(serviceDetail.get(i).getKms());
                int subtractkms = key - selectkms;
                subtractkmslist.add(subtractkms);
            }
            Log.d("Pawan", "Service Detail Subtract Kms List" + subtractkmslist);
            Integer[] array = subtractkmslist.toArray(new Integer[subtractkmslist.size()]);
            getClosestKms(array, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void selectPricebymonth() {
        int selectmonth = 0;


        try {
            int key = Integer.parseInt(selectsearchmonth);
            subtractmonthlist = new ArrayList<Integer>();
            for (int i = 0; i < serviceDetail.size(); i++) {

                selectmonth = Integer.parseInt(serviceDetail.get(i).getMonth());
                int subtractmonth = key - selectmonth;
                subtractmonthlist.add(subtractmonth);
            }
            Log.d("Pawan", "Month list" + subtractmonthlist);
            Integer[] array = subtractmonthlist.toArray(new Integer[subtractmonthlist.size()]);
            getClosestMonth(array, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addCategories() {

        maintenance = com.awrtechnologies.carbudgetsales.data.Maintenance.getAll();
        if (maintenance.size() <= 0) {
            textadd.setVisibility(View.VISIBLE);
            seletcategory.setVisibility(View.GONE);

        } else {
            textadd.setVisibility(View.GONE);
            seletcategory.setVisibility(View.VISIBLE);
            for (int i = 0; i < maintenance.size(); i++) {
                View addView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.maintenance_tab_list, null);
                text = (TextView) addView
                        .findViewById(R.id.textcategory);
                final TextView textline = (TextView) addView
                        .findViewById(R.id.textline);
                TextView line = (TextView) addView.findViewById(R.id.textline);
                text.setText(maintenance.get(i).getName());
                text.setTag(i);
                text.setOnClickListener(this);

                linearTabs.addView(addView);

                arraylistview.add(addView);


            }
        }

    }


    private void findviewbyid() {
        spinnerModel = (Spinner) view.findViewById(R.id.spinnermodel);
        spinnerYear = (Spinner) view.findViewById(R.id.spinneryear);
        spinnerVehicle = (Spinner) view.findViewById(R.id.spinnervehicle);
        txtcar = (TextView) view.findViewById(R.id.txtcar);
        txtjeep = (TextView) view.findViewById(R.id.txtjeep);
        txtvan = (TextView) view.findViewById(R.id.txtvan);
        truck = (TextView) view.findViewById(R.id.txttruck);
        linearModel = (LinearLayout) view.findViewById(R.id.linearModel);
        linearYear = (LinearLayout) view.findViewById(R.id.linearYear);
        linearVehicle = (LinearLayout) view.findViewById(R.id.linearVehicle);
        kms = (EditText) view.findViewById(R.id.kms);
        month = (EditText) view.findViewById(R.id.month);
        price = (TextView) view.findViewById(R.id.price);
        description = (TextView) view.findViewById(R.id.description);
        layoutdescandprice = (LinearLayout) view.findViewById(R.id.layoutdescandprice);
        layoutkmsandmonth = (LinearLayout) view.findViewById(R.id.layoutkmsandmonth);
        linearTabs = (LinearLayout) view.findViewById(R.id.linearTabs);
        searchkmsandmonth = (TextView) view.findViewById(R.id.searchkmsandmonth);
        pdfbutton = (Button) view.findViewById(R.id.pdfbutton);
        serachInfo = (TextView) view.findViewById(R.id.searchInfo);
        textadd = (TextView) view.findViewById(R.id.addText);
        seletcategory = (TextView) view.findViewById(R.id.selectCategory);
    }

    public void setModelDataSpinner(String makename) {


        linearModel.setVisibility(View.VISIBLE);
        linearVehicle.setVisibility(View.GONE);
        linearYear.setVisibility(View.GONE);
        modelDetail = new ArrayList<ModelDetail>();
        modelDetail = ModelDetail.getAll();
        Log.e("Rakhi", "modelnameissize" + modelDetail.size());


        modelDetail = ModelDetail.getDetailsbyTitle(makename);
        Log.e("Rakhi", "modelnameis" + modelDetail);

        selectModel = new ArrayList<>();
        selectModel.add("Select Model");

        for (int i = 0; i < modelDetail.size(); i++) {

            selectModel.add(modelDetail.get(i).getName());

        }


        sa = new SpinnermainAdapter(getActivity(), selectModel);
        spinnerModel.setAdapter(sa);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.textcategory:
                int gettag = (Integer) v.getTag();
                for (int i = 0; i < arraylistview.size(); i++) {
                    TextView textview = (TextView) arraylistview.get(i).findViewById(R.id.textcategory);
                    if (i == gettag) {
                        textview.setBackgroundResource(R.color.orange);
                    } else {
                        textview.setBackgroundResource(R.color.accordingto_appcolor);
                    }
                }
                setModelDataSpinner(maintenance.get(gettag).getName());
                layoutdescandprice.setVisibility(View.GONE);
                layoutkmsandmonth.setVisibility(View.GONE);
                serachInfo.setVisibility(View.GONE);
                pdfbutton.setVisibility(View.GONE);
                kms.setText("");
                month.setText("");

                break;

            case R.id.pdfbutton:

                String extStorageDirectory = Environment.getExternalStorageDirectory()
                        .toString();
                File folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/pdf");
                folder.mkdirs();
                File file = new File(folder, pdflink.hashCode() + ".pdf");
                PDFHelper.getIntance(getActivity()).openPdf(pdflink, file, vehicleName);
                break;

            case R.id.searchkmsandmonth:

                try {
                    InputMethodManager input = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    input.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getWindowToken(), 0);
                } catch (Exception e) {

                }

                //Key for Month and Kms
                selectsearchkms = kms.getText().toString();
                selectsearchmonth = month.getText().toString();
                if ((serviceDetail.size() <= 0 || serviceDetail == null)) {
                    Toast.makeText(getActivity(), "Price And Description Not Given..Please Select other year!!!", Toast.LENGTH_LONG).show();
                    layoutdescandprice.setVisibility(View.GONE);
                } else {
                    //condition for check both kms and month is empty or not
                    layoutdescandprice.setVisibility(View.VISIBLE);
                    if (selectsearchkms.equals("0") && selectsearchmonth.equals("0")) {
                        Toast.makeText(getActivity(), "Please Select Kms Or Month", Toast.LENGTH_LONG).show();
                        layoutdescandprice.setVisibility(View.GONE);
                    } else if (!selectsearchmonth.isEmpty() && selectsearchkms.equals("0")) {
                        kms.setText("");
                        searchMONTHOnCLickButton();
                    } else if (!selectsearchkms.isEmpty() && selectsearchmonth.equals("0")) {
                        month.setText("");
                        searchKMSOnclickButton();

                    }
                    //condition for both kms and month select
                    else if ((!selectsearchmonth.isEmpty() && !selectsearchkms.isEmpty())) {
                        try {
                            ArrayList<Integer> arraylist = new ArrayList<Integer>();

                            /// Get KMS PRICE
                            subtractkmslist = new ArrayList<Integer>();

                            selectPricebykms();
                            int poskms = subtractkmslist.size() - 1;
                            Log.d("Pawan", "Subtractlist" + subtractkmslist);
                            for (int i = 0; i < subtractkmslist.size(); i++) {
                                if (highKMS == i) {
                                    poskms = i;

                                    subtractkmslist.clear();
                                    break;

                                }
                            }

                            //Get MONTH PRICE
                            subtractmonthlist = new ArrayList<Integer>();

                            selectPricebymonth();
                            int posmonth = subtractmonthlist.size() - 1;
                            Log.d("Pawan", "Subtractlist" + subtractmonthlist);
                            for (int i = 0; i < subtractmonthlist.size(); i++) {
                                if (highMONTH == i) {
                                    posmonth = i;

                                    subtractmonthlist.clear();
                                    break;

                                }
                            }

                            String[] pricearray = {serviceDetail.get(poskms).getPrice(), serviceDetail.get(posmonth).getPrice()};
                            String[] desc = {serviceDetail.get(poskms).getDescription(), serviceDetail.get(posmonth).getDescription()};

                            Integer[] array = {highKMS, highMONTH};

                            for (int i = 0; i < array.length; i++) {
                                arraylist.add(array[i]);
                            }
                            getClosestMonthAndKMS(array, 0);
                            int pos = array.length - 1;
                            for (int i = 0; i < array.length; i++) {
                                if (highMONTHANDKMS == i) {
                                    pos = i;

                                    break;

                                }

                            }

                            Log.d("Pawan", "PRICE MONTH AND KMS==" + pricearray[pos]);
                            layoutdescandprice.setVisibility(View.VISIBLE);
                            price.setText(pricearray[pos]);
                            description.setText(desc[pos]);
                        }
                        catch (Exception e)
                        {
                            layoutdescandprice.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"Price And Description Not Given..Please Select other year!!!",Toast.LENGTH_LONG).show();
                        }

                    }

                    //condition for  kms select
                    else if (!selectsearchkms.isEmpty()) {


                        if (selectsearchkms.equals("0")) {
                            Toast.makeText(getActivity(), "Please Select Kms", Toast.LENGTH_LONG).show();
                            layoutdescandprice.setVisibility(View.GONE);
                        } else {
                            searchKMSOnclickButton();
                        }
                    }
                    //condition for  month select
                    else if (!selectsearchmonth.isEmpty()) {


                        if (selectsearchmonth.equals("0")) {
                            Toast.makeText(getActivity(), "Please Select Month", Toast.LENGTH_LONG).show();
                            layoutdescandprice.setVisibility(View.GONE);
                        } else {
                            searchMONTHOnCLickButton();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Select Kms Or Month", Toast.LENGTH_LONG).show();
                        layoutdescandprice.setVisibility(View.GONE);
                    }
                }
//                kms.setText("");
//                month.setText("");
                break;
        }


    }

    //Find Nearest Value Of Kms
    public static int getClosestKms(Integer[] a, int x) {

        int low = 0;
        highKMS = a.length ;
        Log.d("Pawan","Array kms=="+a);
        boolean notequalkms = true;
        if (highKMS < 0)
            throw new IllegalArgumentException("The array cannot be empty");

        for (int i = 0; i < a.length; i++) {
            if (x == a[i]) {
                highKMS = i;
                notequalkms = false;
                Log.d("Pawan", "equal VALUE  KMS when equal ==" + highKMS);
                break;
            }
        }
        if (notequalkms) {
            while (low < highKMS) {
                int mid = (low + highKMS) / 2;
                assert (mid < highKMS);
                int d1 = Math.abs(a[mid] - x);
                int d2 = Math.abs(a[mid + 1] - x);
                if (d2 <= d1) {
                    low = mid + 1;
                    Log.d("Pawan", "LOW VALUE  KMS ==" + low);
                } else {
                    highKMS = mid;
                    Log.d("Pawan", "HIGH VALUE  KMS ==" + highKMS);
                }
            }
            Log.d("Pawan", "a[high]  KMS " + a[highKMS]);
        }
        return a[highKMS];

    }

//

    //Find Nearest Value Of Month
    public static int getClosestMonth(Integer[] a, int x) {

        int low = 0;
        highMONTH = a.length ;
        boolean notequalmonth = true;

        if (highMONTH < 0)
            throw new IllegalArgumentException("The array cannot be empty");

        for (int i = 0; i < a.length; i++) {
            if (x == a[i]) {
                highMONTH = i;
                notequalmonth = false;
                break;
            }
        }
        if (notequalmonth) {
            while (low < highMONTH) {
                int mid = (low + highMONTH) / 2;
                assert (mid < highMONTH);
                int d1 = Math.abs(a[mid] - x);
                int d2 = Math.abs(a[mid + 1] - x);
                if (d2 <= d1) {
                    low = mid + 1;
                    Log.d("Pawan", "LOW VALUE   MONTH==" + low);
                } else {
                    highMONTH = mid;
                    Log.d("Pawan", "HIGH VALUE   MONTH==" + highMONTH);
                }
            }
        }
        Log.d("Pawan", "a[high]   MONTH" + a[highMONTH]);
        return a[highMONTH];

    }

    //Find Nearest Value Of Kms And Month
    public static int getClosestMonthAndKMS(Integer[] a, int x) {

        int low = 0;
        highMONTHANDKMS = a.length-1 ;
        boolean notequalMonthAndKMS = true;

        if (highMONTHANDKMS < 0)
            throw new IllegalArgumentException("The array cannot be empty");

        for (int i = 0; i < a.length; i++) {
            if (x == a[i]) {
                highMONTHANDKMS = i;
                notequalMonthAndKMS = false;
                break;
            }
        }
        if (notequalMonthAndKMS) {
            while (low < highMONTHANDKMS) {
                int mid = (low + highMONTHANDKMS) / 2;
                assert (mid < highMONTHANDKMS);
                int d1 = Math.abs(a[mid] - x);
                int d2 = Math.abs(a[mid + 1] - x);
                if (d2 <= d1) {
                    low = mid + 1;
                    Log.d("Pawan", "LOW VALUE  KMS MONTH==" + low);
                } else {
                    highMONTHANDKMS = mid;
                    Log.d("Pawan", "HIGH VALUE  KMS MONTH==" + highMONTHANDKMS);
                }
            }
        }
        Log.d("Pawan", "a[high] KMS MONTH" + a[highMONTHANDKMS]);
        return a[highMONTHANDKMS];

    }

    public void searchKMSOnclickButton() {
        try {
            subtractkmslist = new ArrayList<Integer>();

            selectPricebykms();
            int pos = subtractkmslist.size() - 1;
            for (int i = 0; i < subtractkmslist.size(); i++) {
                if (highKMS == i) {
                    pos = i;

                    subtractkmslist.clear();
                    break;

                }
            }
            layoutdescandprice.setVisibility(View.VISIBLE);
            price.setText(serviceDetail.get(pos).getPrice());
            description.setText(serviceDetail.get(pos).getDescription());
            Log.d("Pawan", "PRICE KMS==" + serviceDetail.get(pos).getPrice());
        }
        catch (Exception e)
        {
            layoutdescandprice.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Price And Description Not Given..Please Select other year!!!",Toast.LENGTH_LONG).show();
        }
    }

    public void searchMONTHOnCLickButton() {
        try {
            subtractmonthlist = new ArrayList<Integer>();

            selectPricebymonth();
            int pos = subtractmonthlist.size() - 1;
            Log.d("Pawan", "Subtractlist" + subtractmonthlist);
            for (int i = 0; i < subtractmonthlist.size(); i++) {
                if (highMONTH == i) {
                    pos = i;

                    subtractmonthlist.clear();
                    break;

                }
            }

            layoutdescandprice.setVisibility(View.VISIBLE);
            price.setText(serviceDetail.get(pos).getPrice());
            description.setText(serviceDetail.get(pos).getDescription());
            Log.d("Pawan", "PRICE MONTh==" + serviceDetail.get(pos).getPrice());
        } catch (Exception e) {
            layoutdescandprice.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Price And Description Not Given..Please Select other year!!!",Toast.LENGTH_LONG).show();
        }
    }
}
