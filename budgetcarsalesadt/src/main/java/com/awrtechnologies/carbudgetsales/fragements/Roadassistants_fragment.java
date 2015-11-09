package com.awrtechnologies.carbudgetsales.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.MakedataAdapter;
import com.awrtechnologies.carbudgetsales.data.DocumentMake;

import java.util.ArrayList;
import java.util.List;

public class Roadassistants_fragment extends Fragment implements
		OnItemClickListener {

	ListView listview;
	List<DocumentMake> arraylist;

	TextView addtext;
	MakedataAdapter madapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.road_assistants, null);
		listview = (ListView) view.findViewById(R.id.listview);
		addtext=(TextView)view.findViewById(R.id.addText);
		arraylist = DocumentMake.getAll();
		if (arraylist == null || arraylist.size()<=0) {
			arraylist = new ArrayList<DocumentMake>();
			addtext.setVisibility(View.VISIBLE);
		}
		else {
			addtext.setVisibility(View.GONE);
			madapter = new MakedataAdapter(getActivity(), arraylist);
			listview.setAdapter(madapter);
		}

		listview.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int makeid = Integer.parseInt(arraylist.get(position).getMakeId());
		((MainActivity) getActivity()).openNewFragment(new Model_fragment(
				makeid));
	}

}
