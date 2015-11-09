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
import com.awrtechnologies.carbudgetsales.adapter.NewsAdapter;
import com.awrtechnologies.carbudgetsales.data.News;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;

import java.util.List;

public class News_fragment extends Fragment implements OnItemClickListener {

	ListView listview;
	NewsAdapter newsadapter;
	private List<News> newsList;
	TextView textadd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View news = inflater.inflate(R.layout.news, null);
		listview = (ListView) news.findViewById(R.id.listview);
		textadd=(TextView)news.findViewById(R.id.addText);
		newsList = News.getAll();
		if(newsList==null || newsList.size()<=0)
		{
			textadd.setVisibility(View.VISIBLE);
		}
		else {
			textadd.setVisibility(View.GONE);
			newsadapter = new NewsAdapter(getActivity(), newsList);
			listview.setAdapter(newsadapter);
		}

		GeneralHelper.getInstance(getActivity()).setIscheckfragment(false);
		((MainActivity) getActivity()).deals
				.setBackgroundResource(R.drawable.deals);
		((MainActivity) getActivity()).inventory
				.setBackgroundResource(R.drawable.inventory);
		((MainActivity) getActivity()).news
				.setBackgroundResource(R.drawable.pressednews);
		((MainActivity) getActivity()).tools
				.setBackgroundResource(R.drawable.tools);
		((MainActivity) getActivity()).info
				.setBackgroundResource(R.drawable.information);
		((MainActivity) getActivity()).service
				.setBackgroundResource(R.drawable.services);
		listview.setOnItemClickListener(this);
		return news;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String newstilte1 = newsList.get(position).getTitle();
		String newsdate1 = newsList.get(position).getCreateDate();
		String newsdesc1 = newsList.get(position).getDescription();
		String newstype1 = newsList.get(position).getNewsType();
		String newsimage1 = newsList.get(position).getPicture();
		String newssubtitle1 = newsList.get(position).getSubTitle();
		String newscontactno1 = newsList.get(position).getContactNo();
		String newsemail1 = newsList.get(position).getEmail();
		((MainActivity) getActivity()).openNewFragment(new NewsFB_fragment(
				newstilte1, newsdate1, newsdesc1, newstype1, newsimage1,
				newssubtitle1, newscontactno1, newsemail1));

	}

}
