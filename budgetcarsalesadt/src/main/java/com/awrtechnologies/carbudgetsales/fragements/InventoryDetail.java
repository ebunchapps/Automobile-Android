package com.awrtechnologies.carbudgetsales.fragements;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.FacebookSharing;
import com.awrtechnologies.carbudgetsales.GooglePlusSharing;
import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.Twittersharing;
import com.awrtechnologies.carbudgetsales.data.Inventory;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;
import com.awrtechnologies.carbudgetsales.hlistview.widget.AdapterView;
import com.awrtechnologies.carbudgetsales.hlistview.widget.AdapterView.OnItemClickListener;
import com.awrtechnologies.carbudgetsales.hlistview.widget.HListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class InventoryDetail extends Fragment implements OnClickListener {

    View view;
    TextView textmake;
    TextView textmodel;
    TextView textyear;
    TextView textprice;
    TextView textexterior;
    TextView textinterior;
    TextView textmfg;
    TextView textdoors;
    TextView textkms;
    TextView textdrive;
    TextView textcylinder;
    TextView textfuel_type;
    TextView texttransmission;
    TextView textenginesize;
    TextView textoption;
    TextView textdescription;
    TextView texttitleMake;
    TextView textviewprice;
    TextView textviewoption;
    TextView text_passenger;
    TextView text_body;
    TextView text_trim;
    TextView text_warrnty;
    TextView text_warrntydesc;

    TextView textviewdec;

    Button buttonfb;
    Button buttongplus;
    Button buttontwitter;

    LinearLayout linearexterior;
    LinearLayout linearinterior;
    LinearLayout linearmfg;
    LinearLayout lineardoors;
    LinearLayout linearkms;
    LinearLayout linearengine;
    LinearLayout lineardrive;
    LinearLayout linearcylinder;
    LinearLayout linearfuel;
    LinearLayout lineartransmission;
    LinearLayout linearlayouypassenger;
    LinearLayout linearlayouybody;
    LinearLayout linaertrim;
    LinearLayout linearwarrnty;
    LinearLayout linearwarrntydesc;

    HListView hlistview;
    Point screensize;

    ArrayList<String> phots_list = new ArrayList<String>();
    int invid;
    Inventory inventory;

    public ImageLoader mLoader;
    HListAdapter adapter;

    private static final int FBLOGIN = 1003;
    private static final int GPLUSLOGIN = 1004;
    private static final int TWITTERLOGIN = 1005;

    public InventoryDetail(int invid) {
        this.invid = invid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inventory = Inventory.getById(invid);
        view = inflater.inflate(R.layout.inventorydetail, null);

        oncreate();

        screensize = GeneralHelper.getInstance(getActivity()).getScreenSize();
        mLoader = ImageLoader.getInstance();
        adapter = new HListAdapter();
        hlistview.setAdapter(adapter);
        phots_list = new ArrayList<String>();
        for (String s : inventory.getImagesThumb().split(",")) {
            phots_list.add(s.trim());
        }
        adapter.notifyDataSetChanged();

        settext();

        hlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String inventoryId = inventory.getInventoryId();
                PreferencesManager.setPreferenceByKey(getActivity(),
                        "inventoryid", inventoryId);
                ((MainActivity) getActivity())
                        .openNewFragment(new FullScreenInventory(position));

            }
        });
        ;

        buttonfb.setOnClickListener(this);
        buttontwitter.setOnClickListener(this);
        buttongplus.setOnClickListener(this);
        return view;
    }

    class HListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return phots_list.size();
        }

        @Override
        public Object getItem(int position) {
            return phots_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return phots_list.get(position).toString().hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder h = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.photo_wall_pager, null);
                h = new Holder();
                h.iv = (ImageView) convertView
                        .findViewById(R.id.pager_photo_img);
                LayoutParams lpiv = h.iv.getLayoutParams();
                lpiv.width = Math.round(screensize.x * 0.320f);
                lpiv.height = Math.round(lpiv.width * 1.10f);
                h.iv.setLayoutParams(lpiv);
                convertView.setTag(h);
            } else {
                h = (Holder) convertView.getTag();
            }

            mLoader.displayImage(phots_list.get(position).replace("\"", "")
                    .trim(), h.iv);
            return convertView;
        }
    }

    class Holder {
        ImageView iv;
    }

    void settext() {
        textmake.setText(inventory.getMake());
        texttitleMake.setText(inventory.getMake() + " " + inventory.getModel());
        textmodel.setText(inventory.getModel());
        textyear.setText(inventory.getYear());
        try {
            textprice.setText(String.format("$%.2f",
                    Double.parseDouble(inventory.getPrice())));

        } catch (Exception e) {
            textviewprice.setVisibility(View.INVISIBLE);
            textprice.setVisibility(View.INVISIBLE);
        }
        if (inventory.getExteriorColor().equals(" ")) {
            linearexterior.setVisibility(View.GONE);
        } else {
            textexterior.setText(inventory.getExteriorColor());
        }
        if (inventory.getInteriorColor().equals(" ")) {
            linearinterior.setVisibility(View.GONE);
        } else {
            textinterior.setText(inventory.getInteriorColor());
        }
        if (inventory.getMfgExteriorColor().equals(" ")) {
            linearmfg.setVisibility(View.GONE);
        } else {
            textmfg.setText(inventory.getMfgExteriorColor());
        }
        if (inventory.getDoors().equals(" ")) {
            lineardoors.setVisibility(View.GONE);
        } else {
            textdoors.setText(inventory.getDoors());
        }

        if (inventory.getKms().equals(" ")) {
            linearkms.setVisibility(View.GONE);
        } else {
            textkms.setText(inventory.getKms());
        }


        if (inventory.getDrive().equals(" ")) {
            lineardrive.setVisibility(View.GONE);
        } else {
            textdrive.setText(inventory.getDrive());
        }
        if (inventory.getCylinder().equals(" ")) {
            linearcylinder.setVisibility(View.GONE);
        } else {
            textcylinder.setText(inventory.getCylinder());
        }
        if (inventory.getFuelType().equals(" ")) {
            linearfuel.setVisibility(View.GONE);
        } else {
            textfuel_type.setText(inventory.getFuelType());
        }
        if (inventory.getTransmission().equals(" ")) {
            lineartransmission.setVisibility(View.GONE);
        } else {
            texttransmission.setText(inventory.getTransmission());
        }
        if (inventory.getEngineSize().equals(" ")) {
            linearengine.setVisibility(View.GONE);
        } else {
            textenginesize.setText(inventory.getEngineSize());
        }

        if (inventory.getTrim().equals(" ")) {
            linaertrim.setVisibility(View.GONE);
        } else {
            text_trim.setText(inventory.getTrim());
        }

        if (inventory.getPassenger().equals(" ")) {
            linearlayouypassenger.setVisibility(View.GONE);
        } else {
            text_passenger.setText(inventory.getPassenger());
        }

        if (inventory.getBody().equals(" ")) {
            linearlayouybody.setVisibility(View.GONE);
        } else {
            text_body.setText(inventory.getBody());
        }
        if (inventory.getWarranty().equals(" ")) {
            linearwarrnty.setVisibility(View.GONE);
        } else {
            text_warrnty.setText(inventory.getWarranty());
        }
        if (inventory.getWarrantyDescription().equals(" ")) {
            linearwarrntydesc.setVisibility(View.GONE);
        } else {
            text_warrntydesc.setText(inventory.getWarrantyDescription());
        }

        if (inventory.getOptions().equals(" ")) {
            textviewoption.setVisibility(View.GONE);
            textoption.setVisibility(View.GONE);
        } else {
            textviewoption.setVisibility(View.VISIBLE);
            textoption.setVisibility(View.VISIBLE);
            textoption.setText(inventory.getOptions());
        }
        if (inventory.getAddescription().equals(" ")) {
            textdescription.setVisibility(View.GONE);
            textviewdec.setVisibility(View.GONE);

        } else {
            textdescription.setVisibility(View.VISIBLE);
            textviewdec.setVisibility(View.VISIBLE);
            textdescription.setText(inventory.getAddescription());
        }
    }

    void oncreate() {
        textmake = (TextView) view.findViewById(R.id.textmake);
        textmodel = (TextView) view.findViewById(R.id.textmodel);
        textyear = (TextView) view.findViewById(R.id.textyear);
        textviewprice = (TextView) view.findViewById(R.id.textviewprice);
        phots_list = new ArrayList<String>();
        textprice = (TextView) view.findViewById(R.id.text_price);
        textexterior = (TextView) view.findViewById(R.id.text_exterior);
        textinterior = (TextView) view.findViewById(R.id.text_interior);
        textmfg = (TextView) view.findViewById(R.id.text_mfg);
        textdoors = (TextView) view.findViewById(R.id.text_doors);
        textkms = (TextView) view.findViewById(R.id.text_kms);
        textdrive = (TextView) view.findViewById(R.id.text_drive);
        textcylinder = (TextView) view.findViewById(R.id.text_cylinder);
        textfuel_type = (TextView) view.findViewById(R.id.text_fueltype);
        texttransmission = (TextView) view.findViewById(R.id.text_transmisn);
        textenginesize = (TextView) view.findViewById(R.id.text_enginesize);
        textoption = (TextView) view.findViewById(R.id.text_option);
        texttitleMake = (TextView) view.findViewById(R.id.texttitleMake);
        textdescription = (TextView) view.findViewById(R.id.text_description);

         text_passenger= (TextView) view.findViewById(R.id.text_passenger);
         text_body= (TextView) view.findViewById(R.id.text_body);
         text_trim= (TextView) view.findViewById(R.id.text_trim);
         text_warrnty= (TextView) view.findViewById(R.id.text_warranty);
         text_warrntydesc= (TextView) view.findViewById(R.id.text_warrantydescription);

        buttonfb = (Button) view.findViewById(R.id.button_facebook);
        buttontwitter = (Button) view.findViewById(R.id.button_twitter);
        buttongplus = (Button) view.findViewById(R.id.button_google);

        linearcylinder = (LinearLayout) view
                .findViewById(R.id.linearlayouyhcylinder);
        lineardoors = (LinearLayout) view.findViewById(R.id.linearlayouyhdoors);
        lineardrive = (LinearLayout) view.findViewById(R.id.linearlayouyhdrive);
        linearengine = (LinearLayout) view.findViewById(R.id.linearlayouyhens);
        linearexterior = (LinearLayout) view.findViewById(R.id.linearlayouyhex);
        linearfuel = (LinearLayout) view
                .findViewById(R.id.linearlayouyhfueltype);
        linearinterior = (LinearLayout) view.findViewById(R.id.linearlayouyhin);
        linearkms = (LinearLayout) view.findViewById(R.id.linearlayouyhkms);
        linearmfg = (LinearLayout) view.findViewById(R.id.linearlayouyhmfg);
        lineartransmission = (LinearLayout) view
                .findViewById(R.id.linearlayouyhtr);


        linearlayouypassenger = (LinearLayout) view.findViewById(R.id.linearlayouypassenger);
        linearlayouybody = (LinearLayout) view.findViewById(R.id.linearlayouybody);
        linaertrim = (LinearLayout) view.findViewById(R.id.linearlayouytrim);
        linearwarrnty = (LinearLayout) view.findViewById(R.id.linearlayouywarranty);
        linearwarrntydesc = (LinearLayout) view.findViewById(R.id.linearlayouywarrantydescription);
        textviewoption = (TextView) view.findViewById(R.id.textviewoption);
        textviewdec = (TextView) view.findViewById(R.id.textviewdesc);

        hlistview = (HListView) view.findViewById(R.id.hlistview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_facebook:
                fbfunc();
                break;

            case R.id.button_google:

                Intent intentGooglePlus = new Intent(getActivity(),
                        GooglePlusSharing.class);
                intentGooglePlus.putExtra("dealId", invid);
                startActivityForResult(intentGooglePlus, GPLUSLOGIN);
                break;

            case R.id.button_twitter:
                Intent intentTwitter = new Intent(getActivity(),
                        Twittersharing.class);
                intentTwitter.putExtra("dealId", invid);
                startActivityForResult(intentTwitter, TWITTERLOGIN);
                break;

            default:
                break;
        }

    }

    private void fbfunc() {

        sendFBRequest();
    }

    private void sendFBRequest() {
        Intent intentFb = new Intent(getActivity(), FacebookSharing.class);

        intentFb.putExtra("dealId", invid);

        startActivityForResult(intentFb, FBLOGIN);
    }
}
