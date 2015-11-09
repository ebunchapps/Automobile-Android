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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.GalleryAdapter;
import com.awrtechnologies.carbudgetsales.data.Deals;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.PDFHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

import java.io.File;

public class Gallery extends Fragment implements OnItemClickListener {

	GridView gridview;
	GalleryAdapter gadapter;
	private int catid;
	private int position;
	Deals deal;

	public Gallery() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View galleryview = inflater.inflate(R.layout.gallery, null);
		gridview = (GridView) galleryview.findViewById(R.id.gridview);

		catid = PreferencesManager
				.getPreferenceIntByKey(getActivity(), "CATID");
		position = PreferencesManager.getPreferenceIntByKey(getActivity(),
				"position");

		deal = Deals.getAllByDealId(PreferencesManager.getPreferenceByKey(
				getActivity(), "id"));

		if (deal == null) {
			deal = new Deals();
		}

		gadapter = new GalleryAdapter(getActivity(), deal);

		gridview.setAdapter(gadapter);
		gadapter.notifyDataSetChanged();
		gridview.setOnItemClickListener(this);
		return galleryview;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		String[] type = deal.getImagesType().split(",");
		String[] main = deal.getImagesMain().split(",");
		if (type[pos].equals("pdf")) {
			String extStorageDirectory = Environment.getExternalStorageDirectory()
					.toString();
			File folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/pdf");
			folder.mkdirs();
			File file = new File(folder, main[pos].hashCode()+".pdf");
			PDFHelper.getIntance(getActivity()).openPdf(main[pos],file,deal.getCategoryName());
		} else {

			((MainActivity) getActivity())
					.openNewFragment(new GalleryViewpager(deal.getImagesMain(),
							deal.getImagesType(), pos));
		}

	}

}
