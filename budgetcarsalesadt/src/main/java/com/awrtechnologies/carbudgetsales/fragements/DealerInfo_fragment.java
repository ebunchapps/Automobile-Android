package com.awrtechnologies.carbudgetsales.fragements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.DealerInfo;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;

public class DealerInfo_fragment extends Fragment implements OnClickListener {

	View dealerinfo;
	TextView signup;
	TextView textcall;
	TextView textemailurl;
	TextView textfb;
	TextView textgoogleplus;
	TextView textdigg;
	TextView textyoutube;
	TextView texttwitter;
	TextView name;
	TextView email;
	TextView phone;
	TextView address;

	LinearLayout llcall;
	LinearLayout llemail;
	LinearLayout llfb;
	LinearLayout llgoogle;
	LinearLayout lldigg;
	LinearLayout llyoutube;
	LinearLayout lltwittr;

	BroadcastReceiver refreshReciever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			fillData();
		}
	};

	@Override
	public void onResume() {
		getActivity().registerReceiver(
				refreshReciever,
				new IntentFilter(getActivity().getApplicationInfo().packageName
						+ "refresh"));
		super.onResume();
	};

	@Override
	public void onPause() {
		getActivity().unregisterReceiver(refreshReciever);
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dealerinfo = inflater.inflate(R.layout.dealerinfo, null);
		GeneralHelper.getInstance(getActivity()).setIscheckfragment(false);
		oncreate();
		((MainActivity) getActivity()).deals
				.setBackgroundResource(R.drawable.deals);
		((MainActivity) getActivity()).inventory
				.setBackgroundResource(R.drawable.inventory);
		((MainActivity) getActivity()).news
				.setBackgroundResource(R.drawable.news);
		((MainActivity) getActivity()).tools
				.setBackgroundResource(R.drawable.tools);
		((MainActivity) getActivity()).info
				.setBackgroundResource(R.drawable.pressedinformation);
		((MainActivity) getActivity()).service
				.setBackgroundResource(R.drawable.services);
		fillData();
		textcall.setOnClickListener(this);
		textemailurl.setOnClickListener(this);
		textdigg.setOnClickListener(this);
		textfb.setOnClickListener(this);
		textgoogleplus.setOnClickListener(this);
		texttwitter.setOnClickListener(this);
		textyoutube.setOnClickListener(this);
		return dealerinfo;
	}

	void fillData() {
		DealerInfo di = DealerInfo.getDealer();
		if (di != null) {
			if (di.getEmail().trim().equals("")) {
				llemail.setVisibility(View.GONE);
			} else {
				llemail.setVisibility(View.VISIBLE);
			}
			if (di.getFbUrl().trim().equals("")) {
				llfb.setVisibility(View.GONE);
			} else {
				llfb.setVisibility(View.VISIBLE);
			}
			if (di.getGplUsUrl().trim().equals("")) {
				llgoogle.setVisibility(View.GONE);
			} else {
				llgoogle.setVisibility(View.VISIBLE);
			}
			if (di.getDiggUrl().trim().equals("")) {
				lldigg.setVisibility(View.GONE);
			} else {
				lldigg.setVisibility(View.VISIBLE);
			}
			if (di.getYoutubeUrl().trim().equals("")) {
				llyoutube.setVisibility(View.GONE);
			} else {
				llyoutube.setVisibility(View.VISIBLE);
			}
			if (di.getTwitterurl().trim().equals("")) {
				lltwittr.setVisibility(View.GONE);
			} else {
				lltwittr.setVisibility(View.VISIBLE);
			}
			if (di.getPhone().trim().equals("")) {
				llcall.setVisibility(View.GONE);
			} else {
				llcall.setVisibility(View.VISIBLE);
			}
			signup.setText(di.getSignUpSource());
			textcall.setText(di.getPhone() + "");
			textemailurl.setText(di.getEmail());
			textfb.setText(di.getFbUrl());
			textgoogleplus.setText(di.getGplUsUrl());
			textdigg.setText(di.getDiggUrl());
			texttwitter.setText(di.getTwitterurl());
			textyoutube.setText(di.getYoutubeUrl());
			signup.setText(di.getDealershipname());
			name.setText(di.getFirstName());
			email.setText(di.getEmail());
			phone.setText(di.getPhone() + "");
			address.setText(di.getAddress());
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		DealerInfo di = DealerInfo.getDealer();
		di = DealerInfo.getDealer();
		if (id == R.id.textviewcall) {
			String phno = textcall.getText().toString().replace("-", "");
			Intent phoneIntent = new Intent(Intent.ACTION_CALL);
			phoneIntent.setData(Uri.parse("tel:" + phno));
			try {
				startActivity(phoneIntent);

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), "Call failed,please try again later",
						Toast.LENGTH_SHORT).show();
			}
		}
		if (id == R.id.textviewemail) {

			textemailurl.setText(Html.fromHtml(di.getEmail()));
		}
		if(id==R.id.textviewfb)
		{
//			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//			startActivity(intent);
		}
		if(id==R.id.textviewgoogle)
		{
//			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//			startActivity(intent);
		}
		if(id==R.id.textviewdigg)
		{
//			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//			startActivity(intent);
		}
		if(id==R.id.textviewtwitter)
		{
//			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//			startActivity(intent);
		}if(id==R.id.textviewyoutube)
		{
//			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//			startActivity(intent);
		}


	}

	public void oncreate() {
		signup = (TextView) dealerinfo.findViewById(R.id.textviewSignUp);
		textcall = (TextView) dealerinfo.findViewById(R.id.textviewcall);
		textemailurl = (TextView) dealerinfo.findViewById(R.id.textviewemail);
		textfb = (TextView) dealerinfo.findViewById(R.id.textviewfb);
		textgoogleplus = (TextView) dealerinfo
				.findViewById(R.id.textviewgoogle);
		textdigg = (TextView) dealerinfo.findViewById(R.id.textviewdigg);
		textyoutube = (TextView) dealerinfo.findViewById(R.id.textviewyoutube);
		texttwitter = (TextView) dealerinfo.findViewById(R.id.textviewtwitter);
		name = (TextView) dealerinfo.findViewById(R.id.edittext_Name);
		email = (TextView) dealerinfo.findViewById(R.id.edittext_Email);
		phone = (TextView) dealerinfo.findViewById(R.id.edittext_Phonenum);
		address = (TextView) dealerinfo.findViewById(R.id.edittext_Address);
		llcall = (LinearLayout) dealerinfo.findViewById(R.id.linearlayoutcall);
		llemail = (LinearLayout) dealerinfo
				.findViewById(R.id.linearlayoutemail);
		llfb = (LinearLayout) dealerinfo.findViewById(R.id.linearlayoutfb);
		llgoogle = (LinearLayout) dealerinfo
				.findViewById(R.id.linearlayoutgoogle);
		lldigg = (LinearLayout) dealerinfo.findViewById(R.id.linearlayoutdigg);
		llyoutube = (LinearLayout) dealerinfo
				.findViewById(R.id.linearlayoutyoutube);
		lltwittr = (LinearLayout) dealerinfo
				.findViewById(R.id.linearlayouttwitter);
	}

}
