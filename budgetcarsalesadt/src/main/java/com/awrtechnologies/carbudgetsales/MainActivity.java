package com.awrtechnologies.carbudgetsales;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.awrtechnologies.carbudgetsales.data.DealCategory;
import com.awrtechnologies.carbudgetsales.data.DealerInfo;
import com.awrtechnologies.carbudgetsales.data.Deals;
import com.awrtechnologies.carbudgetsales.data.DocumentMake;
import com.awrtechnologies.carbudgetsales.data.DocumentModel;
import com.awrtechnologies.carbudgetsales.data.DocumentModelDetails;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.data.ModelDetail;
import com.awrtechnologies.carbudgetsales.data.News;
import com.awrtechnologies.carbudgetsales.data.ServiceData;
import com.awrtechnologies.carbudgetsales.data.ServiceDetail;
import com.awrtechnologies.carbudgetsales.data.Social;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;
import com.awrtechnologies.carbudgetsales.data.VehiclesDetail;
import com.awrtechnologies.carbudgetsales.data.YearDetail;
import com.awrtechnologies.carbudgetsales.fragements.DealerInfo_fragment;
import com.awrtechnologies.carbudgetsales.fragements.Deals_fragemnet;
import com.awrtechnologies.carbudgetsales.fragements.Inventory_fragement;
import com.awrtechnologies.carbudgetsales.fragements.News_fragment;
import com.awrtechnologies.carbudgetsales.fragements.ServiceFragment;
import com.awrtechnologies.carbudgetsales.fragements.Signin_fragment;
import com.awrtechnologies.carbudgetsales.fragements.Tools_fragment;
import com.awrtechnologies.carbudgetsales.fragements.VehicleReceiptImages;
import com.awrtechnologies.carbudgetsales.helper.ConnectionDetector;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

public class MainActivity extends FragmentActivity implements OnClickListener {

    public Fragment currentfragment;
    Stack<Fragment> fragmentstack;
    public ImageView news;
    public ImageView deals;
    public ImageView tools;
    public ImageView inventory;
    public ImageView info;
    public ImageView service;
    //      Inventory_fragement inf;
    Button facebook, twitter, google, digg, youtube;
    String imagethumb, type;
    String news_thumb;
    int dealid, vehicledriven;
    RelativeLayout rl_buttons, relativeprogress;
    String thumb;
    Social social;
    public File imageFilePath;
    public Uri imageFileUri;
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;


    public void loadData() {

        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            try {
                new MaintenanceTask().execute();
                DealsTask dealtask = new DealsTask();
                dealtask.execute();

            } catch (Exception e) {

            }
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(MainActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentstack = new Stack<Fragment>();
        // creating connection detector class instance
        cd = new ConnectionDetector(MainActivity.this);
        rl_buttons = (RelativeLayout) findViewById(R.id.relative_layout_buttons);
        relativeprogress = (RelativeLayout) findViewById(R.id.relativelayoutprogressbarheader);
        news = (ImageView) findViewById(R.id.button_news);
        deals = (ImageView) findViewById(R.id.button_deals);
        tools = (ImageView) findViewById(R.id.button_tools);
        inventory = (ImageView) findViewById(R.id.button_inventroy);
        info = (ImageView) findViewById(R.id.button_information);
        service = (ImageView) findViewById(R.id.button_services);
        facebook = (Button) findViewById(R.id.button_facebook);
        twitter = (Button) findViewById(R.id.button_twitter);
        google = (Button) findViewById(R.id.button_google);
        digg = (Button) findViewById(R.id.button_digg);
        youtube = (Button) findViewById(R.id.button_youtube);

        java.io.File imageFile1 = new File(
                (Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        + "/." + Constants.APPNAME + ""));
        imageFile1.mkdirs();
        User user = User.getUser();
        if (user != null) {
            if (GeneralHelper.getInstance(MainActivity.this).isIscheck() == true
                    || GeneralHelper.getInstance(MainActivity.this)
                    .isIscheckdonetime() == true) {

//				openfragment();
//				GeneralHelper.getInstance(com.awrtechnologies.carbudgetsales.MainActivity.this).setIscheckfragment(false);
                loadData();

            } else if (GeneralHelper.getInstance(MainActivity.this).isIscheck() == false
                    || GeneralHelper.getInstance(MainActivity.this)
                    .isIscheckdonetime() == false) {

                loadData();
            }
        } else {
//             openNewFragment(new ServiceFragment());
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            ft.replace(R.id.contanier, new Signin_fragment());

            ft.commit();
        }

        deals.setOnClickListener(this);
        inventory.setOnClickListener(this);
        news.setOnClickListener(this);
        tools.setOnClickListener(this);
        info.setOnClickListener(this);
        service.setOnClickListener(this);
        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        google.setOnClickListener(this);
        digg.setOnClickListener(this);
        youtube.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_deals) {
            fragmentstack.clear();
            openNewFragment(new Deals_fragemnet());
        } else if (id == R.id.button_news) {
            fragmentstack.clear();
            openNewFragment(new News_fragment());
        } else if (id == R.id.button_inventroy) {
            fragmentstack.clear();
//			new inf.Task().execute();
            openFragment(new Inventory_fragement());
        } else if (id == R.id.button_tools) {
            fragmentstack.clear();
            openFragment(new Tools_fragment());
        } else if (id == R.id.button_information) {
            fragmentstack.clear();
            openFragment(new DealerInfo_fragment());
        } else if (id == R.id.button_facebook) {
            Uri uri = Uri.parse("http://www.facebook.com");
        } else if (id == R.id.button_google) {
            Uri uri = Uri.parse("http://www.google.com");
            Intent webintent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(webintent);
        } else if (id == R.id.button_digg) {
            Uri uri = Uri.parse("http://www.google.com");
            Intent webintent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(webintent);
        } else if (id == R.id.button_twitter) {
            Uri uri = Uri.parse("http://www.com.awrtechnologies.carbudgetsales.twitter.com");
            Intent webintent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(webintent);
        } else if (id == R.id.button_youtube) {
            Uri uri = Uri.parse("http://www.youtube.com");
            Intent webintent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(webintent);
        } else if (id == R.id.button_services) {
            fragmentstack.clear();
            openFragment(new ServiceFragment());
        }

    }

    public void imageOpen(Fragment f, final int REQUEST_CAMERA,
                          final int SELECT_FILE) {

        GeneralHelper.getInstance(MainActivity.this).setTempFragment(f);
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    java.io.File imageFile = new File(
                            (Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                                    + "/." + Constants.APPNAME
                                    + "/"
                                    + System.currentTimeMillis() + ".jpeg"));

                    PreferencesManager.setPreferenceByKey(MainActivity.this,
                            "IMAGEWWC", imageFile.getAbsolutePath());
                    //
                    imageFilePath = imageFile;
                    imageFileUri = Uri.fromFile(imageFile);
                    Intent i = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                            imageFileUri);
                    startActivityForResult(i, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    if (Build.VERSION.SDK_INT < 19) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE);
                    } else {
                        Intent intent = new Intent(
                                Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE);
                    }


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("OnActivityResult");
        Fragment tempFragment = GeneralHelper.getInstance(this)
                .getTempFragment();
        if (requestCode % 10 == 0 && resultCode == RESULT_OK) {
            String pathName = "";
            Uri path = data.getData();
            if (Build.VERSION.SDK_INT < 19) {
                pathName = getRealPathFromURI(this, path);

            } else {
                pathName = getRealPathFromURIUpdated(this, path);
            }

            System.out.println("selected file");
            System.out.println("path " + pathName);

            if (requestCode == 10) {
                ((VehicleReceiptImages) tempFragment).onImageSelect(pathName);
            }
            // } else if (requestCode == 20) {
            // ((UpdateAccount) tempFragment).onImageSelect(pathName);
            // }
        } else if (requestCode % 10 != 0 && resultCode == RESULT_OK) {

            System.out.println("IMAGEPATH "
                    + PreferencesManager.getPreferenceByKey(MainActivity.this,
                    "IMAGEWWC"));

            imageFilePath = new File(PreferencesManager.getPreferenceByKey(
                    MainActivity.this, "IMAGEWWC"));

            String pathName = imageFilePath.getAbsolutePath();

            if (requestCode == 15) {

                ((VehicleReceiptImages) tempFragment).onImageSelect(pathName);

            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getRealPathFromURIUpdated(Context context, Uri contentUri) {

        ParcelFileDescriptor parcelFileDescriptor;
        String path = "";
        try {
            parcelFileDescriptor = getContentResolver().openFileDescriptor(
                    contentUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor
                    .getFileDescriptor();
            path = getImagePath(contentUri);
            parcelFileDescriptor.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    public String getImagePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ",
                new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor
                .getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public boolean emailValidator(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public void openNewFragment(Fragment f) {

        try {
            InputMethodManager input = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), 0);
        } catch (Exception e) {
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentstack.push(currentfragment);
        ft.replace(R.id.contanier, f);
        currentfragment = f;
        ft.commitAllowingStateLoss();
    }

    public void back() {
        currentfragment = fragmentstack.pop();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.contanier, currentfragment);
        ft.commit();
    }


    public void openFragment(Fragment f) {

        try {
            InputMethodManager input = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), 0);
        } catch (Exception e) {
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.contanier, f);
        currentfragment = f;
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        try {
            if (fragmentstack.size() <= 0) {
                finish();
            } else {
                back();
            }

        } catch (Exception e) {
            finish();
        }


    }

    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     */
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.transparentlogo
                : R.drawable.transparentlogo);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
                dialog.dismiss();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public class DealsTask extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;

        @Override
        protected String doInBackground(Void... params) {

            try {
                User user = User.getUser();
                HttpClient httpclient = new DefaultHttpClient();
                String url = Constants.BASEURL + "/api/get_all?appid="
                        + Constants.APPID + "&" + "server_key="
                        + Constants.SERVER_KEY + "&userid=" + user.getUserid();
                System.out.println("USERID   " + user.getUserid());
//                String urll="http://awrtechnologies.com/automobile/api/get_all?appid=7e0b57b8ec0cb5b2f49cb86911b1366a&server_key=T03moz10tfj5y3K0QFgh5F3AroyRJ60d&userid=1";
//                String urll="http://www.awrtechnologies.com/automobile/api/get_all?appid=uyDuIhAk9PvK1xk2nS9pfzoLg96fF3169OgeXM6pWh4gZUty1IInI7jglPBc6Qil10kJE0hIbIxIIGj3s9k2qbeVKXWVRihhUEeXW68vPTr4ToX9727SRL3FAZ8Z0eBx&server_key=T03moz10tfj5y3K0QFgh5F3AroyRJ60d&userid=37";
                HttpGet httpget = new HttpGet(url);
                System.out.println("GET ALL CALLED");
                HttpResponse httpresponse = httpclient.execute(httpget);

                InputStream inputstream = httpresponse.getEntity().getContent();

                String result = convertStreamToString(inputstream);
                System.out.println("RESULT ARRIVED");
                processData(result);
                System.out.println("RESULT PROCESSED");
            } catch (Exception e) {
                System.out.println("GET ALL EXCEPTION");
//				showMessage("Please Connect to Internet");
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPreExecute() {
            try {
                if (GeneralHelper.getInstance(MainActivity.this).isIscheckfragment() == false) {
                    openNewFragment(new Deals_fragemnet());
                    relativeprogress.setVisibility(View.VISIBLE);
                } else {
                    mProgressDialog = new ProgressDialog(MainActivity.this);
                    mProgressDialog.setMessage("Loading..");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                }
            } catch (Exception e) {

            }


            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("GET ALL POST");
            try {
                if (GeneralHelper.getInstance(MainActivity.this).isIscheckfragment() == false) {
                    relativeprogress.setVisibility(View.GONE);
                } else {

                    mProgressDialog.dismiss();
                }
            } catch (Exception e) {

            }


            // sending broadcast to fragments to refresh com.awrtechnologies.carbudgetsales.data because new com.awrtechnologies.carbudgetsales.data
            // is
            // available

            // Fragment registered with this broadcast will recieve the
            // broadcast packagename+refresh
            Intent i = new Intent();
            i.setAction(getApplicationInfo().packageName + "refresh");
            sendBroadcast(i);
            socialbutton();
            if (GeneralHelper.getInstance(MainActivity.this).isIscheckfragment() == true) {
                openNewFragment(new VehicleReceiptImages());
//				openNewFragment(new FullScreenVehicleReceiptImage(0));
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

    public void processData(String result) {
        try {
            JSONObject object1 = new JSONObject(result);
            int responseCode = object1.getInt("response_code");
            JSONObject responseObject = object1.getJSONObject("response");

            // /////CAR NESWS
            // -----------------------------------------------------------

            try {
                ActiveAndroid.beginTransaction();
                JSONArray newsArray = responseObject.getJSONArray("news");
                new Delete().from(News.class).execute();
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject obj = newsArray.getJSONObject(i);

                    News news = new News();
                    news.setContactNo(getString(obj, "contactno"));
                    news.setCreateDate(getString(obj, "createdate"));
                    news.setDescription(getString(obj, "description"));
                    news.setEmail(getString(obj, "email"));
                    news.setPicture(getString(obj, "picture"));
                    try {
                        news.setImageMain(obj.getJSONObject("images")
                                .getString("main"));
                    } catch (Exception e) {

                    }
                    try {
                        news.setImageThumb(obj.getJSONObject("images")
                                .getString("thumb"));
                    } catch (Exception e) {

                    }

//                    try {
//                        news.setSubTitle(getString(obj,"subtitle"));
//                    } catch (Exception e) {
//
//                    }
                    news.setNewsType(getString(obj, "newstype"));
                    news.setTitle(getString(obj, "title"));
                    news.setUserId(getString(obj, "userid"));
                    news.save();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
////Services
            try {
                new Delete().from(ServiceData.class).execute();

                JSONArray services = responseObject.getJSONArray("services");

                for (int i = 0; i < services.length(); i++) {
                    JSONObject serviceobj = services.getJSONObject(i);
                    ServiceData service = new ServiceData();
                    service.setAppointmentDate(getString(serviceobj, "appointmentdate"));
                    service.setCreateDate(getString(serviceobj, "createdate"));
                    service.setDealerId(getString(serviceobj, "dealerid"));
                    service.setDealerUserId(getString(serviceobj, "dealeruserid"));
                    service.setDescription(getString(serviceobj, "description"));
                    service.setLogComment(getString(serviceobj, "logcomment"));
                    service.setLogDate(getString(serviceobj, "logdate"));
                    service.setLogId(getString(serviceobj, "logid"));
                    service.setLogStatus(getString(serviceobj, "logstatus"));
                    service.setLogString(getString(serviceobj, "logstring"));
                    service.setMake(getString(serviceobj, "make"));
                    service.setModel(getString(serviceobj, "model"));
                    service.setSid(getString(serviceobj, "id"));
                    service.setStatus(getString(serviceobj, "status"));
                    service.setUserEmail(getString(serviceobj, "useremail"));
                    service.setUserName(getString(serviceobj, "username"));
                    service.setUserPhone(getString(serviceobj, "userphone"));
                    service.setYear(getString(serviceobj, "year"));
                    service.save();
                    System.out.println("Service Saved!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ////Categories
            try {
                new Delete().from(DealCategory.class).execute();
                JSONArray services = responseObject.getJSONArray("categories");
                for (int i = 0; i < services.length(); i++) {
                    JSONObject categoryObj = services.getJSONObject(i);
                    DealCategory dc = new DealCategory();
                    dc.setCreateDate(getString(categoryObj, "createdate"));
                    dc.setDealCount(getString(categoryObj, "deal_count"));
                    dc.setDealId(getString(categoryObj, "id"));
                    dc.setImage(getString(categoryObj, "image"));
                    dc.setMainImage(getString(categoryObj.getJSONObject("images")
                            , "main"));
                    dc.setName(getString(categoryObj, "name"));
                    dc.setThumbImage(getString(categoryObj.getJSONObject("images")
                            , "thumb"));
                    dc.setUserid(getString(categoryObj, "userid"));
                    dc.setAppid(getString(categoryObj, "appid"));
                    dc.save();
                }
            } catch (Exception e) {

            }


            /////Deals
            try {
                new Delete().from(Deals.class).execute();
                JSONArray dealsArray = responseObject.getJSONArray("deals");
                Log.d("Pawan", "Api Deals" + dealsArray);
                for (int i = 0; i < dealsArray.length(); i++) {

                    JSONObject dealsobj = dealsArray.getJSONObject(i);
                    Deals deal = new Deals();
                    deal.setCategoryId(getString(dealsobj, "category"));
                    deal.setAppid(getString(dealsobj, "appid"));
                    deal.setCategoryName(getString(dealsobj, "name"));
                    deal.setCreateDate(getString(dealsobj, "createdate"));
                    deal.setDealId(getString(dealsobj, "id"));
                    deal.setDescription(getString(dealsobj, "description"));
                    JSONArray arry = dealsobj.getJSONArray("images");
                    String mainImages = "";
                    String thumbImages = "";
                    String types = "";
                    for (int j = 0; j < arry.length(); ++j) {
                        JSONObject obj = arry.getJSONObject(j);
                        if (mainImages.equals("")) {
                            mainImages = getString(obj, "main");
                        } else {
                            mainImages += "," + getString(obj, "main");
                        }
                        if (thumbImages.equals("")) {
                            thumbImages = getString(obj, "thumb");
                        } else {
                            thumbImages += "," + getString(obj, "thumb");
                        }
                        if (types.equals("")) {
                            types = getString(obj, "type");
                        } else {
                            types += "," + getString(obj, "type");
                        }
                    }
                    deal.setImagesMain(mainImages);
                    deal.setImagesThumb(thumbImages);
                    deal.setImagesType(types);
                    System.out.println("MAINACTIVITY===================================>" + types);
                    deal.setName(getString(dealsobj, "name"));
                    deal.setPrice(getString(dealsobj, "price"));
                    deal.setUserId(getString(dealsobj, "userid"));
                    deal.setValidFrom(getString(dealsobj, "validfrom"));
                    deal.setValidTo(getString(dealsobj, "validto"));
                    deal.setVehicleDriven(getString(dealsobj, "isvehicle"));
                    deal.setKms(getString(dealsobj, "kms"));
                    deal.save();

                }
            } catch (Exception e) {

            }

            ////Documents
            try {
                new Delete().from(DocumentMake.class).execute();
                new Delete().from(DocumentModel.class).execute();
                new Delete().from(DocumentModelDetails.class).execute();
                JSONArray makesarry = responseObject.getJSONArray("documents");
                for (int i = 0; i < makesarry.length(); i++) {
                    JSONObject makeobj = makesarry.getJSONObject(i);
                    DocumentMake dm = new DocumentMake();
//                    dm.setCreateDate(makeobj.getString("createdate"));
//                    dm.setImage(makeobj.getString("image"));
                    dm.setMakeId(getString(makeobj, "id"));
                    dm.setName(getString(makeobj, "name"));
                    dm.setAppId(getString(makeobj, "appid"));
//                    dm.setUserId(makeobj.getString("userid"));
                    dm.save();
                    try {
                        JSONArray modelsarry = makeobj.getJSONArray("models");
                        for (int j = 0; j < modelsarry.length(); j++) {
                            JSONObject modelobj = modelsarry.getJSONObject(j);
                            DocumentModel dmodel = new DocumentModel();

//                            dmodel.setCreateDate(modelobj.getString("createdate"));
//                            dmodel.setDetails(modelobj.getString("createdate"));
                            dmodel.setMakeId(getString(modelobj, "make_id"));
                            dmodel.setModelId(getString(modelobj, "id"));
//                            dmodel.setParentId(modelobj.getString("makeid"));
                            dmodel.setTitle(getString(modelobj, "name"));
//                            dmodel.setUserId(modelobj.getString("userid"));

                            dmodel.save();

                            try {
                                JSONArray attachmentsarry = modelobj.getJSONArray("attachments");
                                for (int k = 0; k < attachmentsarry.length(); k++) {
                                    JSONObject attachments = attachmentsarry.getJSONObject(k);
                                    DocumentModelDetails dmd = new DocumentModelDetails();
                                    dmd.setTitle(getString(attachments, "name"));
//                                    dmd.setImageMain(attachments.getJSONObject("image")
//                                            .getString("main"));
//                                    dmd.setImageThumb(attachments.getJSONObject("image")
//                                            .getString("thumb"));
                                    dmd.setType(getString(attachments, "type"));
                                    dmd.setModelDetailId(getString(attachments, "id"));
                                    dmd.setModelid(getString(attachments, "model_id"));
                                    dmd.setAttachment_name(getString(attachments, "attachment_name"));
                                    dmd.setLink(getString(attachments, "link"));
                                    dmd.save();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            //////////OwnVehicles
            try {
                new Delete().from(VehicleInfo.class).execute();
                JSONArray ownvehiclearray = responseObject
                        .getJSONArray("ownvehicle");
                System.out.println("OWN VEHICLE");
                Log.d("Pawan", "ownvehicle" + ownvehiclearray);
                for (int i = 0; i < ownvehiclearray.length(); i++) {
                    JSONObject ownvehicle = ownvehiclearray
                            .getJSONObject(i);
                    System.out.println("ARRAY=="
                            + ownvehicle.getString("license"));
                    VehicleInfo vehicleinfo = new VehicleInfo();
                    vehicleinfo.setVehicleid(getString(ownvehicle, "id"));
                    vehicleinfo.setLicense(getString(ownvehicle, "license"));
                    vehicleinfo.setHashId(getString(ownvehicle, "hashId"));
                    vehicleinfo.setMake(getString(ownvehicle, "make"));
                    vehicleinfo.setModel(getString(ownvehicle, "model"));
                    vehicleinfo.setUserId(getString(ownvehicle, "userId"));
                    vehicleinfo.setYear(getString(ownvehicle, "year"));
                    JSONArray imagearry = ownvehicle.getJSONArray("images");
                    String mainimage = "";
                    String thumbimage = "";
                    String types = "";
                    String usertypes = "";
                    for (int j = 0; j < imagearry.length(); j++) {
                        JSONObject imageobj = imagearry.getJSONObject(j);


                        if (usertypes.equals("")) {
                            usertypes = getString(imageobj, "userType");
                        } else {
                            usertypes += "," + getString(imageobj, "userType");
                        }

                        if (types.equals("")) {
                            types = getString(imageobj, "type");
                        } else {
                            types += "," + getString(imageobj, "type");
                        }

                        if (mainimage.equals("")) {
                            mainimage = getString(imageobj, "main");
                        } else {
                            mainimage += "," + getString(imageobj, "main");
                        }

                        if (thumbimage.equals("")) {
                            thumbimage = getString(imageobj, "thumb");
                        } else {
                            thumbimage += "," + getString(imageobj, "thumb");
                        }
                    }
                    vehicleinfo.setThumb(thumbimage);
                    vehicleinfo.setType(types);
                    vehicleinfo.setMain(mainimage);
                    vehicleinfo.setUsertype(usertypes);

                    vehicleinfo.save();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                new Delete().from(Garage.class).execute();
                JSONArray dealsArray = responseObject
                        .getJSONArray("garage");

                for (int i = 0; i < dealsArray.length(); i++) {

                    JSONObject dealsobj = dealsArray.getJSONObject(i);
                    Garage deal = new Garage();
                    deal.setCategoryId(getString(dealsobj, "categoryid"));
                    deal.setCategoryName(getString(dealsobj, "name"));
                    deal.setCreateDate(getString(dealsobj, "createdate"));
                    deal.setDealId(getString(dealsobj, "id"));
                    deal.setDescription(getString(dealsobj, "description"));
                    JSONArray arry = dealsobj.getJSONArray("images");
                    String mainImages = "";
                    String thumbImages = "";
                    String types = "";
                    for (int j = 0; j < arry.length(); ++j) {
                        JSONObject obj = arry.getJSONObject(j);
                        if (mainImages.equals("")) {
                            mainImages = getString(obj, "main");
                        } else {
                            mainImages += "," + getString(obj, "main");
                        }
                        if (thumbImages.equals("")) {
                            thumbImages = getString(obj, "thumb");
                        } else {
                            thumbImages += "," + getString(obj, "thumb");
                        }
                        if (types.equals("")) {
                            types = getString(obj, "type");
                        } else {
                            types += "," + getString(obj, "type");
                        }
                    }
                    deal.setImagesMain(mainImages);
                    deal.setImagesThumb(thumbImages);
                    deal.setImagesType(types);
                    deal.setName(getString(dealsobj, "name"));
                    deal.setPrice(getString(dealsobj, "price"));
                    deal.setUserId(getString(dealsobj, "userid"));
                    deal.setKms(getString(dealsobj, "kms"));
                    deal.setValidFrom(getString(dealsobj, "validfrom"));
                    deal.setValidTo(getString(dealsobj, "validto"));
                    deal.setVehicleDriven(getString(dealsobj
                            , "isvehicle"));
                    deal.save();

                }
            } catch (Exception e) {

            }

/////Dealer Info
            new Delete().from(DealerInfo.class).execute();
            try {
                JSONObject obj = responseObject.getJSONObject("dealer_info");
                DealerInfo di = new DealerInfo();
                di.setAddress(getString(obj, "address"));
                di.setAppId(getString(obj, "appid"));
                di.setDiggUrl(getString(obj, "diggurl"));
                di.setEmail(getString(obj, "email"));
                di.setFbUrl(getString(obj, "fburl"));
                di.setFirstName(getString(obj, "contactname"));
                di.setGplUsUrl(getString(obj, "gplusurl"));
                di.setImage(getString(obj, "image"));
                di.setDealershipname(getString(obj, "dealershipname"));
                di.setPhone(getString(obj, "phone"));
                di.setSignUpSource(getString(obj, "singup_source"));
                di.setTwitterurl(getString(obj, "twitterurl"));
                di.setYoutubeUrl(getString(obj, "youtubeurl"));
                di.setZipcode(getString(obj, "zipcode"));
                di.setCreatedate(getString(obj, "createdate"));
                di.setHashId(getString(obj, "hashId"));
                di.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                new Delete().from(Social.class).execute();

                JSONObject obj = responseObject.getJSONObject("social");
                Social social = new Social();
                social.setDiggUrl(getString(obj, "diggurl"));
                social.setFacebook(getString(obj, "facebook"));
                social.setgPlus(getString(obj, "gplus"));
                social.setTwitter(getString(obj, "com/awrtechnologies/carbudgetsales/twitter"));
                social.setYoutube(getString(obj, "youtube"));
                social.save();
            } catch (Exception e) {

            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        } finally

        {
            ActiveAndroid.endTransaction();
        }

    }

    public void layoutShown() {
        rl_buttons.setVisibility(View.VISIBLE);
    }

    public void layoutNotShown() {
        rl_buttons.setVisibility(View.GONE);
    }

    public void socialbutton() {
        try {
            social = Social.getSocial();
            if (social.getFacebook().equals("")) {
                facebook.setVisibility(View.GONE);
            } else {
                facebook.setVisibility(View.VISIBLE);

            }
            if (social.getTwitter().equals("")) {
                twitter.setVisibility(View.GONE);
            } else {
                twitter.setVisibility(View.VISIBLE);

            }
            if (social.getgPlus().equals("")) {
                google.setVisibility(View.GONE);
            } else {
                google.setVisibility(View.VISIBLE);

            }
            if (social.getDiggUrl().equals("")) {
                digg.setVisibility(View.GONE);
            } else {
                digg.setVisibility(View.VISIBLE);
            }
            if (social.getYoutube().equals("")) {
                youtube.setVisibility(View.GONE);
            } else {
                youtube.setVisibility(View.VISIBLE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(JSONObject obj, String key) {
        try {
            return obj.getString(key);
        } catch (Exception e) {
            return "";
        }
    }


    public class MaintenanceTask extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;

        @Override
        protected String doInBackground(Void... params) {

            {

                try {

                    User user = User.getUser();
                    HttpClient httpclient = new DefaultHttpClient();
                    String url = Constants.BASEURL + "/api/maintenance_get_meta";

                    HttpPost httppost = new HttpPost(url);
                    HttpResponse httpresponse = httpclient.execute(httppost);

                    ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
//                    BasicNameValuePair pasrd = new BasicNameValuePair("appid","7e0b57b8ec0cb5b2f49cb86911b1366a");
                    BasicNameValuePair pasrd = new BasicNameValuePair("appid", Constants.APPID);
                    param.add(pasrd);
                    BasicNameValuePair pasrdd = new BasicNameValuePair(
                            "server_key", Constants.SERVER_KEY);
                    param.add(pasrdd);


                    httppost.setEntity(new UrlEncodedFormEntity(param));
                    HttpResponse httpresponses = httpclient.execute(httppost);

                    InputStream inputstream = httpresponses.getEntity().getContent();

                    String result = convertStreamToString(inputstream);
                    Log.d("Pawan", "MAINTENANC RESULT" + result);


                    return result;
                } catch (Exception e) {
                    System.out.println("GET ALL EXCEPTION");
                    e.printStackTrace();
                }

                return "";
            }
        }


        @Override
        protected void onPreExecute() {
            try {
                if (GeneralHelper.getInstance(MainActivity.this).isIscheckfragment() == false) {
                    relativeprogress.setVisibility(View.VISIBLE);
                } else {
                    mProgressDialog = new ProgressDialog(MainActivity.this);
                    mProgressDialog.setMessage("Loading..");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                }
            } catch (Exception e) {

            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (GeneralHelper.getInstance(MainActivity.this).isIscheckfragment() == false) {
                    relativeprogress.setVisibility(View.GONE);
                } else {

                    mProgressDialog.dismiss();
                }
            } catch (Exception e) {

            }

            try {
                new Delete().from(com.awrtechnologies.carbudgetsales.data.Maintenance.class).execute();
                new Delete().from(ServiceDetail.class).execute();
                new Delete().from(YearDetail.class).execute();
                new Delete().from(ModelDetail.class).execute();
                new Delete().from(VehiclesDetail.class).execute();
                Log.e("Pawan", "Result is" + result);
                JSONObject jsonobj = new JSONObject(result);
                int responseCode = jsonobj.getInt("response_code");
                String message = jsonobj.getString("message");
                try {
                    JSONObject obj = jsonobj.getJSONObject("response");
                    try {
                        JSONArray jsonAry = obj.getJSONArray("categories");
                        for (int p = 0; p < jsonAry.length(); p++) {
                            JSONObject objcategory = jsonAry.getJSONObject(p);

                            com.awrtechnologies.carbudgetsales.data.Maintenance maintenance = new com.awrtechnologies.carbudgetsales.data.Maintenance();
                            maintenance.setMainId(getString(objcategory, "id"));
                            maintenance.setName(getString(objcategory, "name"));
                            maintenance.setAppId(getString(objcategory, "appid"));
                            maintenance.save();

                            try {
                                JSONArray arrayModel = objcategory.getJSONArray("models");

                                for (int j = 0; j < arrayModel.length(); j++) {
                                    JSONObject objmodel = arrayModel.getJSONObject(j);

                                    ModelDetail md = new ModelDetail();
                                    md.setMainId(getString(objmodel, "id"));
                                    md.setName(getString(objmodel, "name"));
                                    md.setCategoryid(getString(objmodel, "cat_id"));
                                    md.setVname(getString(objcategory, "name"));

                                    md.save();
                                    try {
                                        JSONArray arrayYear = objmodel.getJSONArray("years");
                                        for (int k = 0; k < arrayYear.length(); k++) {

                                            JSONObject objYear = arrayYear.getJSONObject(k);
                                            YearDetail yd = new YearDetail();
                                            yd.setMainId(getString(objYear, "id"));
                                            yd.setName(getString(objYear, "name"));
                                            yd.setModelId(getString(objYear, "model_id"));
                                            yd.save();
                                            JSONArray arrayVehicle = objYear.getJSONArray("vehicles");
                                            for (int l = 0; l < arrayVehicle.length(); l++) {
                                                try {
                                                    JSONObject objVehicle = arrayVehicle.getJSONObject(l);
                                                    VehiclesDetail vd = new VehiclesDetail();
                                                    vd.setMainId(getString(objVehicle, "id"));
                                                    vd.setName(getString(objVehicle, "name"));
                                                    vd.setPdf(getString(objVehicle, "pdf"));
                                                    vd.save();
                                                    try {
                                                        JSONArray aryService = objVehicle.getJSONArray("service_details");
                                                        for (int q = 0; q < aryService.length(); q++) {
                                                            JSONObject objService = aryService.getJSONObject(q);

                                                            ServiceDetail sd = new ServiceDetail();
                                                            sd.setsId(getString(objService, "id"));
                                                            sd.setKms(getString(objService, "kms"));
                                                            sd.setMonth(getString(objService, "months"));
//                                                    sd.setPdf(objService.getString("pdf"));
//                                                    sd.setType(objService.getString("type"));
                                                            sd.setVehicleId(getString(objService, "vehicle_id"));
                                                            sd.setDescription(getString(objService, "description"));
                                                            sd.setPrice(getString(objService, "price"));
                                                            sd.save();

                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();

                                                    }


                                                } catch (Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
            super.onPostExecute(result);
        }
    }


    @Override
    protected void onResume() {

        System.out.println("ON RESUME");
        super.onResume();
    }

    @Override
    protected void onPause() {

        System.out.println("ON PAUSE");
        super.onPause();
    }


}
