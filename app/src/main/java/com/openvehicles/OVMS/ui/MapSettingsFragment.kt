package com.openvehicles.OVMS.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.openvehicles.OVMS.utils.AppPrefs;
import com.openvehicles.OVMS.R;
import com.openvehicles.OVMS.utils.ConnectionList;

public class FragMapSettings extends Fragment implements ConnectionList.ConnectionsListener {
	private static final String TAG = "FragMapSettings(OCM)";

	View view;
	AppPrefs appPrefs;
	ConnectionList connectionList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.map_settings, null);
		appPrefs = new AppPrefs(getActivity(), "ovms");

		connectionList = new ConnectionList(getActivity(), this,false);

		setUpClusteringViews();

		return view;
	}

	public interface UpdateMap {
		public void updateClustering(int clusterSizeIndex, boolean enabled);
		public void clearCache();
		public void updateFilter(String connectionList);
	}

	@Override
	public void onConnectionChanged(String conId, String conName) {
		FragMap.updateMap.updateFilter(conId);
	}

	private void setUpClusteringViews() {
		final CheckBox ocmEnableCheckbox = (CheckBox) view.findViewById(R.id.checkbox_ocm_enable);
		final CheckBox clusterCheckbox = (CheckBox) view.findViewById(R.id.checkbox_cluster);
		final SeekBar clusterSizeSeekbar = (SeekBar) view.findViewById(R.id.seekbar_cluster_size);
		Spinner maxResultsSpinner = (Spinner) view.findViewById(R.id.ocm_maxresults);
		Button btnConnections = (Button) view.findViewById(R.id.btn_connections);
		Button btnClearCache = (Button) view.findViewById(R.id.btn_clearcache);

		boolean ocmEnabled = appPrefs.getData("option_ocm_enabled", "1").equals("1");
		ocmEnableCheckbox.setChecked(ocmEnabled);

		if (appPrefs.getData("check").equals("false")) {
			clusterCheckbox.setChecked(false);
			clusterSizeSeekbar.setEnabled(false);
		}

		try {
			// Note: probably due to cargo culting, the prefs name for the cluster size is "progress"
			clusterSizeSeekbar.setProgress(Integer.parseInt(appPrefs.getData("progress")));
		} catch (Exception e) {
			// TODO: handle exception
		}

		// set maxresults spinner to current value:
		try {
			String ocm_maxresults_value = appPrefs.getData("maxresults");
			String[] ocm_maxresults_options = getResources().getStringArray(R.array.ocm_options_maxresults);
			int ocm_maxresults_index = 0;
			for (int i=0; i<ocm_maxresults_options.length; i++) {
				if (ocm_maxresults_options[i].equals(ocm_maxresults_value)) {
					ocm_maxresults_index = i;
					break;
				}
			}
			maxResultsSpinner.setSelection(ocm_maxresults_index);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ocmEnableCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appPrefs.saveData("option_ocm_enabled", isChecked ? "1" : "0");
				if (!isChecked) {
					FragMap.updateMap.clearCache();
				}
			}
		});

		clusterCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				clusterSizeSeekbar.setEnabled(isChecked);
				appPrefs.saveData("check", "" + isChecked);
				FragMap.updateMap.updateClustering(clusterSizeSeekbar.getProgress(), isChecked);
			}
		});

		clusterSizeSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progress = seekBar.getProgress();
				boolean enabled = clusterCheckbox.isChecked();
				appPrefs.saveData("progress", "" + progress);
				FragMap.updateMap.updateClustering(progress, enabled);
			}
		});

		maxResultsSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				String selected = adapterView.getItemAtPosition(i).toString();
				if (!appPrefs.getData("maxresults").equals(selected)) {
					appPrefs.saveData("maxresults", "" + selected);
					FragMap.updateMap.clearCache();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) { }
		});

		btnConnections.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				connectionList.sublist();
			}
		});

		btnClearCache.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FragMap.updateMap.clearCache();
				Toast.makeText(getActivity(), getString(R.string.msg_cache_cleared),
						Toast.LENGTH_SHORT).show();
			}
		});

	}
}
