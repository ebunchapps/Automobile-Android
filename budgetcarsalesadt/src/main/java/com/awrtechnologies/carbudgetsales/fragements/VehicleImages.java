package com.awrtechnologies.carbudgetsales.fragements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import ogrelab.org.apache.http.HttpResponse;
import ogrelab.org.apache.http.client.HttpClient;
import ogrelab.org.apache.http.client.entity.UrlEncodedFormEntity;
import ogrelab.org.apache.http.client.methods.HttpPost;
import ogrelab.org.apache.http.entity.mime.MultipartEntity;
import ogrelab.org.apache.http.entity.mime.content.FileBody;
import ogrelab.org.apache.http.entity.mime.content.StringBody;
import ogrelab.org.apache.http.impl.client.DefaultHttpClient;
import ogrelab.org.apache.http.protocol.HTTP;

import org.json.JSONArray;
import org.json.JSONObject;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.VehicleGalleryadapter;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PDFHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class VehicleImages extends Fragment implements OnItemClickListener {

	View view;

	GridView gridview;
	public ImageLoader mLoader;

	VehicleInfo vehicleimage;
	String vehicleid;
	VehicleGalleryadapter adapter;
	Point screensize;
	User user;
	RelativeLayout  relativeprogress;

	public VehicleImages(String vehicleid) {
		this.vehicleid = vehicleid;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.vehicleimages, null);
		gridview = (GridView) view.findViewById(R.id.gridview);
		relativeprogress = (RelativeLayout)view.findViewById(R.id.relativelayoutprogressbarheader);
		screensize = GeneralHelper.getInstance(getActivity()).getScreenSize();
		user = User.getUser();

		GeneralHelper.getInstance(getActivity()).setIscheckfragment(true);
		mLoader = ImageLoader.getInstance();
		vehicleimage = VehicleInfo.getById(vehicleid);
		if (vehicleimage == null) {
			vehicleimage = new VehicleInfo();
		}
		adapter = new VehicleGalleryadapter(getActivity(), vehicleimage);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {


		String[] type = vehicleimage.getType().split(",");
		String[] main =  vehicleimage.getMain().split(",");
		if (type[position].equals("pdf")) {
			String extStorageDirectory = Environment.getExternalStorageDirectory()
					.toString();
			File folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/pdf");
			folder.mkdirs();
			File file = new File(folder, main[position].hashCode()+".pdf");
			PDFHelper.getIntance(getActivity()).openPdf(main[position],file,vehicleimage.getMake());
		} else {

			((MainActivity) getActivity())
					.openNewFragment(new FullScreenVehicleimage(position));
		}
	}

}
