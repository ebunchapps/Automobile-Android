package com.awrtechnologies.carbudgetsales.fragements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.util.Log;
import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.InventoryAdapter;
import com.awrtechnologies.carbudgetsales.data.Inventory;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Inventory_fragement extends Fragment implements
        OnItemClickListener, OnClickListener {

    ListView listview;

    InventoryAdapter adapter;
    EditText search;
    private Button loadMoreButton;
    private View footerLoadingView;
    int end = 0;
    TextView textadd;

    private List<Inventory> arraylist;
    BroadcastReceiver refreshReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            arraylist = Inventory.getAll();
            if (arraylist == null || arraylist.size() <=0) {
                arraylist = new ArrayList<Inventory>();
            }
            if (adapter != null) {
                adapter.setList(arraylist);
            } else {
                adapter = new InventoryAdapter(getActivity(), arraylist);
                listview.setAdapter(adapter);
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

    ;

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(refreshReciever);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inventoryview = inflater
                .inflate(R.layout.inventoryfragement, null);
        listview = (ListView) inventoryview.findViewById(R.id.listview);
        textadd=(TextView)inventoryview.findViewById(R.id.addText);
        GeneralHelper.getInstance(getActivity()).setIscheckfragment(false);
        search = (EditText) inventoryview.findViewById(R.id.edittextsearch);
//
        /**
         * List Footer
         */

        View footer = inflater.inflate(R.layout.inventoryfooter, null);
        listview.addFooterView(footer);
        buildFooterView(footer);
        footerLoadingView.setVisibility(View.INVISIBLE);

        arraylist = Inventory.getAll();
        if (arraylist != null && arraylist.size() > 0) {
            textadd.setVisibility(View.GONE);
            adapter = new InventoryAdapter(getActivity(), arraylist);
            listview.setAdapter(adapter);
        } else {
            textadd.setVisibility(View.VISIBLE);
            new Task().execute();
        }



        ((MainActivity) getActivity()).deals
                .setBackgroundResource(R.drawable.deals);
        ((MainActivity) getActivity()).inventory
                .setBackgroundResource(R.drawable.pressedinventory);
        ((MainActivity) getActivity()).news
                .setBackgroundResource(R.drawable.news);
        ((MainActivity) getActivity()).tools
                .setBackgroundResource(R.drawable.tools);
        ((MainActivity) getActivity()).info
                .setBackgroundResource(R.drawable.information);
        ((MainActivity) getActivity()).service
                .setBackgroundResource(R.drawable.services);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                adapter.getFilter().filter(s.toString());
                if(s.toString().equals(""))
                {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                InputMethodManager input = (InputMethodManager) getActivity()
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                                input.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                        .getWindowToken(), 0);
                            } catch (Exception e) {

                            }

                        }
                    }, 300);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {



                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            InputMethodManager input = (InputMethodManager) getActivity()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            input.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                    .getWindowToken(), 0);
                        } catch (Exception e) {

                        }

                    }
                }, 5000);


            }
        });

        adapter = new InventoryAdapter(getActivity(), arraylist);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        return inventoryview;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        int invid = Integer.parseInt(adapter.arraylist.get(position)
                .getInventoryId().trim());

        ((MainActivity) getActivity()).openNewFragment(new InventoryDetail(
                invid));

    }

    public class Task extends AsyncTask<Void, Void, String> {



        @Override
        protected String doInBackground(Void... params) {

            try {

                User user = User.getUser();
                HttpClient httpclient = new DefaultHttpClient();
                String url = Constants.BASEURL + "/api/get_inventories?appid="
                        + Constants.APPID + "&" + "server_key="
                        + Constants.SERVER_KEY + "&userid=" + user.getUserid() + "&limit=" + Constants.INVENTORYCOUNTLIMIT + "&start=" + end;
                System.out.println("USERID   " + user.getUserid());

                Log.e("Rakhi", "endinsidethetoast" + end);

//                String urll = "http://www.awrtechnologies.com/clients/paul/automobilenew/api/get_inventories?appid=7e0b57b8ec0cb5b2f49cb86911b1366a&server_key=T03moz10tfj5y3K0QFgh5F3AroyRJ60d&userid=1&limit=10&start=0";
                HttpGet httpget = new HttpGet(url);
                System.out.println("GET ALL CALLED");
                HttpResponse httpresponse = httpclient.execute(httpget);

                InputStream inputstream = httpresponse.getEntity().getContent();

                String result = convertStreamToString(inputstream);
                System.out.println("RESULT ARRIVED");

                System.out.println("RESULT PROCESSED");

                return result;
            } catch (Exception e) {
                System.out.println("GET ALL EXCEPTION");
                e.printStackTrace();
            }

            return "";
        }


        @Override
        protected void onPreExecute() {
            footerLoadingView.setVisibility(View.VISIBLE);
            loadMoreButton.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
//			mProgressDialog.dismiss();
            footerLoadingView.setVisibility(View.GONE);
            loadMoreButton.setVisibility(View.VISIBLE);

            try {

                JSONObject object1 = new JSONObject(result);
                int responseCode = object1.getInt("response_code");
                JSONObject responseObject = object1.getJSONObject("response");
                int inventoriesCount = Integer.valueOf(responseObject.getString("total"));
                PreferencesManager.setPreferenceIntByKey(getActivity(), PreferencesManager.INVENTORYCOUNT, inventoriesCount);
                // /////CAR NESWS
                // -----------------------------------------------------------


					ActiveAndroid.beginTransaction();

                JSONArray inventoryarray = responseObject
                        .getJSONArray("inventories");
                for (int i = 0; i < inventoryarray.length(); i++) {

                    JSONObject inventoryobj = inventoryarray
                            .getJSONObject(i);
                    Inventory inventory = new Inventory();
                    inventory.setAddescription(getString(inventoryobj
                            , "addescription"));
                    inventory.setAdid(getString(inventoryobj, "adid"));
                    inventory.setBody(getString(inventoryobj, "body"));
                    inventory.setCategory(getString(inventoryobj
                            , "category"));
                    inventory.setCompanyId(getString(inventoryobj
                            , "companyid"));
                    inventory.setCompanyName(getString(inventoryobj
                            , "companyname"));
                    inventory.setCreatedDate(getString(inventoryobj
                            , "createddate"));
                    inventory.setCylinder(getString(inventoryobj
                            , "cylinder"));
                    inventory.setDoors(getString(inventoryobj, "doors"));
                    inventory.setDrive(getString(inventoryobj, "drive"));
                    inventory.setEngineSize(getString(inventoryobj
                            , "enginesize"));
                    inventory.setExteriorColor(getString(inventoryobj
                            , "exteriorcolor"));
                    inventory.setInteriorColor(getString(inventoryobj
                            , "interiorcolor"));
                    inventory.setMfgExteriorColor(getString(inventoryobj
                            , "mfgexteriorcolor"));
                    inventory.setFinancingDescription(getString(inventoryobj
                            , "financingdescription"));
                    inventory.setFinancingDownPayment(getString(inventoryobj
                            , "financingdownpayment"));
                    inventory.setFinancingIsAvailable(getString(inventoryobj
                            , "financingisavailable"));
                    inventory.setFinancingNumberOfPayment(getString(inventoryobj
                            , "financingnumberofpayment"));
                    inventory.setFinancingOdometer(getString(inventoryobj
                            , "financingodometer"));
                    inventory.setFinancingPayment(getString(inventoryobj
                            , "financingpayment"));
                    inventory.setFinancingPaymenttype(getString(inventoryobj
                            , "financingpaymenttype"));
                    inventory.setFinancingSource(getString(inventoryobj
                            , "financingsource"));
                    inventory.setFinancingType(getString(inventoryobj
                            , "financingtype"));
                    inventory.setFuelType(getString(inventoryobj
                            , "fueltype"));
                    inventory.setHidePrice(getString(inventoryobj
                            , "hideprice"));
                    String imagesMain = "";
                    String imagesThumb = "";

                    try {
                        imagesMain = inventoryobj
                                .getJSONObject("mainphoto").getString(
                                        "main");
                        imagesThumb = inventoryobj.getJSONObject(
                                "mainphoto").getString("thumb");

                        JSONArray arry = inventoryobj
                                .getJSONArray("otherimages");
                        for (int j = 0; j < arry.length(); ++j) {
                            JSONObject obj = arry.getJSONObject(j);
                            imagesMain += "," + getString(obj, "main");
                            imagesThumb += "," + getString(obj, "thumb");
                        }
                        inventory.setImagesMain(imagesMain);
                        inventory.setImagesThumb(imagesThumb);
                    } catch (Exception e) {

                    }
                    inventory.setInteriorColor(getString(inventoryobj
                            , "interiorcolor"));
                    inventory.setInventoryId(getString(inventoryobj, "id"));
                    inventory.setKms(getString(inventoryobj, "kms"));
                    inventory.setMake(getString(inventoryobj, "make"));
                    inventory.setManufactureProgram(getString(inventoryobj
                            , "manufactureprogram"));
                    inventory.setMfgExteriorColor(getString(inventoryobj
                            , "mfgexteriorcolor"));
                    inventory.setModel(getString(inventoryobj, "model"));
                    inventory.setModifiedDate(getString(inventoryobj
                            , "modifieddate"));
                    inventory.setOptions(getString(inventoryobj, "options"));
                    inventory.setPassenger(getString(inventoryobj
                            , "passenger"));
                    inventory.setPrice(getString(inventoryobj, "price"));
                    inventory.setStatus(getString(inventoryobj, "status"));
                    inventory.setStockNumber(getString(inventoryobj
                            , "stocknumber"));
                    inventory.setTransmission(getString(inventoryobj
                            , "transmission"));
                    inventory.setTrim(getString(inventoryobj,"trim"));
//                    inventory.setUserId(inventoryobj.getString("userid"));
                    inventory.setVin(getString(inventoryobj, "vin"));
                    inventory.setWarranty(getString(inventoryobj
                            , "warranty"));
                    inventory.setWarrantyDescription(getString(inventoryobj
                            , "warrantydescription"));
                    inventory.setYear(getString(inventoryobj,"year"));
                    inventory.save();
                }


                ActiveAndroid.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
				ActiveAndroid.endTransaction();
                    if (adapter == null) {
                        adapter = new InventoryAdapter(getActivity(), arraylist);
                        listview.setAdapter(adapter);
                    } else {
                        textadd.setVisibility(View.GONE);
                        adapter = new InventoryAdapter(getActivity(), arraylist);
                        listview.setAdapter(adapter);
                        adapter.setList(arraylist);


                        adapter.refresh();
                    }
                }catch(Exception e)
                    {

                    }

            }
            try {
                int count = PreferencesManager.getPreferenceIntByKey(getActivity(), PreferencesManager.INVENTORYCOUNT);
                int total = Inventory.getAll().size();
                if (end >= count) {
                    loadMoreButton.setVisibility(View.GONE);
                    footerLoadingView.setVisibility(View.GONE);
                } else {
                    loadMoreButton.setVisibility(View.VISIBLE);
                    footerLoadingView.setVisibility(View.INVISIBLE);
                }
            }catch (Exception e)
            {

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

    private void buildFooterView(View footer) {
        loadMoreButton = (Button) footer.findViewById(R.id.loadMoreButton);
        loadMoreButton.setOnClickListener(this);
        footerLoadingView = footer.findViewById(R.id.foorerLoadingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadMoreButton:
                loadMoreButton.setVisibility(View.INVISIBLE);
                footerLoadingView.setVisibility(View.VISIBLE);
                int currentLevel = adapter.arraylist.size();

                int count = PreferencesManager.getPreferenceIntByKey(getActivity(), PreferencesManager.INVENTORYCOUNT);

                int start = currentLevel;
                end = start;

                if (end >= count) {

                    end = count;
                }
                new Task().execute();
                break;
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
