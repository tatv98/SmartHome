package nuce.tatv.smarthome.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;

import nuce.tatv.smarthome.Activities.MainActivity;
import nuce.tatv.smarthome.R;

import static nuce.tatv.smarthome.Activities.MainActivity.checkConnect;
import static nuce.tatv.smarthome.Activities.MainActivity.series;
import static nuce.tatv.smarthome.Activities.MainActivity.subcribe;

public class DashboardFragment extends Fragment{
    View vDashboard;
    public static GraphView gvRainSenser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vDashboard = inflater.inflate(R.layout.dashboard_fragment, container, false);
        init();
        return vDashboard;
    }
    private void init() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Dashboard");

        // we get graph view instance
        gvRainSenser = vDashboard.findViewById(R.id.gvRainSenser);
        gvRainSenser.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = gvRainSenser.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setScrollable(true);


        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Dashboard");
        if (checkConnect){
            subcribe("CAMBIENMUA");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            // we're going to simulate real time with thread that append data to the graph
            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 100; i++) {
                    if (getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                gvRainSenser.onDataChanged(false, false);
                            }
                        });
                    }
                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
    }
}
