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
import com.awrtechnologies.carbudgetsales.adapter.ModelDetailAdapter;
import com.awrtechnologies.carbudgetsales.data.DocumentModelDetails;

import java.util.ArrayList;
import java.util.List;

public class ModelDetailFragment extends Fragment implements
        OnItemClickListener {

    ListView listview;

    String modelname, modelimage;
    ModelDetailAdapter madapter;
    String modelid;
    TextView textadd;

    private List<DocumentModelDetails> arraylist;

    public ModelDetailFragment(String makeid) {
        this.modelid = makeid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View modelview = inflater.inflate(R.layout.mode_fragment, null);
        listview = (ListView) modelview.findViewById(R.id.listview);
        textadd = (TextView) modelview.findViewById(R.id.addText);
        arraylist = DocumentModelDetails.getAllByModelId(modelid);

        if (arraylist == null || arraylist.size() <= 0) {
            arraylist = new ArrayList<DocumentModelDetails>();
            textadd.setVisibility(View.VISIBLE);
        } else {
            textadd.setVisibility(View.GONE);
            madapter = new ModelDetailAdapter(getActivity(), arraylist);
            listview.setAdapter(madapter);
        }
        listview.setOnItemClickListener(this);
        return modelview;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ((MainActivity) getActivity()).openNewFragment(new GalleryFragment(
                modelid));
    }

}
