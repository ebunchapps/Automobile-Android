package com.awrtechnologies.carbudgetsales.fragements;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.DocumentModelDetails;
import com.awrtechnologies.carbudgetsales.data.ServiceDetail;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PDFHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

public class GalleryFragment extends Fragment {

	Point point;
	public ImageLoader mLoader;
	ImageView imageview;
	String type;
	String modelid;
	String attacmentname;
	String pdflink;

//	public GalleryFragment(String image,String type) {
//		this.image = image;
//		this.type=type;
//
//	}

	public GalleryFragment(String modelid) {
		this.modelid=modelid;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.galleryimage, null);
		imageview = (ImageView) view.findViewById(R.id.imageview);
		point = GeneralHelper.getInstance(getActivity()).getScreenSize();
		mLoader = ImageLoader.getInstance();
		type=DocumentModelDetails.getByModelId(modelid).getType();
		String url = DocumentModelDetails.getByModelId(modelid).getImageMain();
		pdflink=DocumentModelDetails.getByModelId(modelid).getLink();
		attacmentname=DocumentModelDetails.getByModelId(modelid).getAttachment_name();
		if(type.equals("PDF"))
		{
			String extStorageDirectory = Environment.getExternalStorageDirectory()
					.toString();
			File folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/pdf");
			folder.mkdirs();
			File file = new File(folder, pdflink.hashCode()+".pdf");
			PDFHelper.getIntance(getActivity()).openPdf(pdflink, file, attacmentname);
		}
	else {

			mLoader.displayImage(url, imageview);
		}
		return view;
	}

}
