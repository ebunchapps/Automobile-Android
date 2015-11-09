package com.awrtechnologies.carbudgetsales.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.awrtechnologies.carbudgetsales.adapter.ListViewAdapter;
import com.awrtechnologies.carbudgetsales.data.Deals;

import java.util.ArrayList;
import java.util.List;

public class Main extends Fragment implements OnItemClickListener {

	ListView list;
	List<Deals> arraylist;
	ListViewAdapter listadapter;

	private int id;
	private int catgid;
	TextView textadd;

	public Main(int catgid) {
		this.catgid = catgid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View mainview = inflater.inflate(R.layout.cardetailfragement, null);

		list = (ListView) mainview.findViewById(R.id.listview);
		textadd=(TextView)mainview.findViewById(R.id.addText);


		arraylist = Deals.getAllById(catgid);

		if (arraylist == null || arraylist.size() <=0) {
			arraylist = new ArrayList<Deals>();

			textadd.setVisibility(View.VISIBLE);
		}
		else {
			list.setVisibility(View.VISIBLE);
			textadd.setVisibility(View.GONE);
			listadapter = new ListViewAdapter(getActivity(), arraylist);
			list.setAdapter(listadapter);
		}

		list.setOnItemClickListener(this);
		return mainview;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		((MainActivity)getActivity()).openNewFragment(new CarDetailFragment(position,catgid,arraylist.get(position).getDealId()));

	}

}
