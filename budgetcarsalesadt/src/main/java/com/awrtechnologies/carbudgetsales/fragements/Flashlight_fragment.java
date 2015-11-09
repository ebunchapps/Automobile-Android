package com.awrtechnologies.carbudgetsales.fragements;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.awrtechnologies.carbudgetsales.R;

public class Flashlight_fragment extends Fragment {

	private Camera camera;
	ImageButton imagebtn;
	private boolean isFlashOn;
	private boolean hasFlash;
	Parameters params;
	MediaPlayer mp;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View flashlightview = inflater.inflate(R.layout.flashlight, null);
		imagebtn = (ImageButton) flashlightview.findViewById(R.id.btnSwitch);
		
		// displaying button image
	
		imagebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hasFlash = getActivity().getPackageManager().hasSystemFeature(
						PackageManager.FEATURE_CAMERA_FLASH);
				
				if (!hasFlash) {
					// device doesn't support flash
					// Show alert message and close the application
					AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
					alert.setTitle("Error");
					alert.setMessage("Sorry, your device doesn't support flash light!");
					alert.setButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// closing the application
							getActivity().finish();
						}
					});
					alert.show();

				}
				toggleButtonImage();
				if (isFlashOn) {
					// turn off flash
					turnOffFlash();
				} else {
					// turn on flash
					turnOnFlash();
				}

			}
		});
		return flashlightview;
	}

	private void getCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();
				params = camera.getParameters();
			} catch (RuntimeException e) {
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}
		}
	}
	private void turnOnFlash() {
		if (!isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			playSound();
			
			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview();
			isFlashOn = true;
			
			toggleButtonImage();
		}

	}
	private void turnOffFlash() {
		if (isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			playSound();
			
			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(params);
			camera.stopPreview();
			isFlashOn = false;
			
			// changing button/switch image
			toggleButtonImage();
		}
	}
	private void playSound(){
		if(isFlashOn){
			mp = MediaPlayer.create(getActivity(), R.raw.light_switch_off);
		}else{
			mp = MediaPlayer.create(getActivity(), R.raw.light_switch_on);
		}
		mp.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        }); 
		mp.start();
	}
	private void toggleButtonImage(){
		if(isFlashOn){
			imagebtn.setImageResource(R.drawable.on);
		}else{
			imagebtn.setImageResource(R.drawable.off);
		}
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		
		// on pause turn off the flash
		turnOffFlash();
	}



	@Override
	public void onResume() {
		super.onResume();
		
		// on resume turn on the flash
		if(hasFlash)
			turnOnFlash();
	}

	@Override
	public void onStart() {
		super.onStart();
		
		getCamera();
		
	}

	@Override
	public void onStop() {
		super.onStop();
		
		// on stop release the camera
		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

}