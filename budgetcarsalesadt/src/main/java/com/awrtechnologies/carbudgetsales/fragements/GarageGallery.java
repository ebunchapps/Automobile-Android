package com.awrtechnologies.carbudgetsales.fragements;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.GarageGalleryAdapter;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.PDFHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

import java.io.File;

public class GarageGallery extends Fragment implements OnItemClickListener {

	GridView gridview;
	Garage garage;
	GarageGalleryAdapter gadapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View galleryview = inflater.inflate(R.layout.gallery, null);
		gridview = (GridView) galleryview.findViewById(R.id.gridview);
		garage = Garage.getAllByDealId(PreferencesManager.getPreferenceByKey(
				getActivity(), "id"));
		//
		if (garage == null) {
			garage = new Garage();
		}
		gadapter = new GarageGalleryAdapter(getActivity(), garage);
		gridview.setAdapter(gadapter);
		gridview.setOnItemClickListener(this);
		return galleryview;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String[] type = garage.getImagesType().split(",");
		String[] main = garage.getImagesMain().split(",");
		if (type[position].equals("pdf")) {
			String extStorageDirectory = Environment.getExternalStorageDirectory()
					.toString();
			File folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/pdf");
			folder.mkdirs();
			File file = new File(folder, main[position].hashCode()+".pdf");
			PDFHelper.getIntance(getActivity()).openPdf(main[position], file,garage.getCategoryName());
//			Uri path = Uri.parse(main[pos]);
		} else {

			((MainActivity) getActivity())
					.openNewFragment(new GalleryViewpager(garage.getImagesMain(),
							garage.getImagesType(), position));
		}

	}
	

}
