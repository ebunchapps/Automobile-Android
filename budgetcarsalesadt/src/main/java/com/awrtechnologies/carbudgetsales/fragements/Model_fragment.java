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
import com.awrtechnologies.carbudgetsales.adapter.ModeldataAdapter;
import com.awrtechnologies.carbudgetsales.data.DocumentModel;

import java.util.ArrayList;
import java.util.List;

public class Model_fragment extends Fragment implements OnItemClickListener {

    ListView listview;

    ModeldataAdapter madapter;
    int makeid;
    TextView addText;

    private List<DocumentModel> arraylist;

    public Model_fragment(int makeid) {
        this.makeid = makeid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View modelview = inflater.inflate(R.layout.mode_fragment, null);
        listview = (ListView) modelview.findViewById(R.id.listview);
        addText = (TextView) modelview.findViewById(R.id.addText);
//		arraylist = DocumentModel.getAll();
        arraylist = DocumentModel.getAllByMakeId(makeid);
        if (arraylist == null || arraylist.size()<=0) {
            addText.setVisibility(View.VISIBLE);
            arraylist = new ArrayList<DocumentModel>();
        } else {
            addText.setVisibility(View.GONE);
            madapter = new ModeldataAdapter(getActivity(), arraylist);
            listview.setAdapter(madapter);
        }
        listview.setOnItemClickListener(this);
        return modelview;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        ((MainActivity) getActivity()).openNewFragment(new ModelDetailFragment(
                arraylist.get(position).getModelId()));
    }

}
