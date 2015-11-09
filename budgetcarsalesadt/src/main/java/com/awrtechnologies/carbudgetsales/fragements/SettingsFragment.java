package com.awrtechnologies.carbudgetsales.fragements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.activeandroid.query.Delete;
import com.awrtechnologies.carbudgetsales.FirstActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.ServiceData;
import com.awrtechnologies.carbudgetsales.data.DealCategory;
import com.awrtechnologies.carbudgetsales.data.DealerInfo;
import com.awrtechnologies.carbudgetsales.data.Deals;
import com.awrtechnologies.carbudgetsales.data.DocumentMake;
import com.awrtechnologies.carbudgetsales.data.DocumentModel;
import com.awrtechnologies.carbudgetsales.data.DocumentModelDetails;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.data.Inventory;
import com.awrtechnologies.carbudgetsales.data.ModelDetails;
import com.awrtechnologies.carbudgetsales.data.News;
import com.awrtechnologies.carbudgetsales.data.Social;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

public class SettingsFragment extends Fragment {

	Button logout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.settings_fragment, null);
		logout = (Button) v.findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteAll();

			}
		});
		return v;
	}

	protected void deleteAll() {
		new Delete().from(ServiceData.class).execute();
		new Delete().from(DealCategory.class).execute();
		new Delete().from(DealerInfo.class).execute();
		new Delete().from(Deals.class).execute();
		new Delete().from(DocumentMake.class).execute();
		new Delete().from(DocumentModel.class).execute();
		new Delete().from(DocumentModelDetails.class).execute();
		new Delete().from(User.class).execute();
		new Delete().from(Garage.class).execute();
		new Delete().from(Inventory.class).execute();
		new Delete().from(ModelDetails.class).execute();
		new Delete().from(News.class).execute();
		new Delete().from(Social.class).execute();
		PreferencesManager.clearAllPreferences(getActivity());
		Intent i = new Intent(getActivity(), FirstActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
}
