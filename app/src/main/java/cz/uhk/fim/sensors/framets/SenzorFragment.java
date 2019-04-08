package cz.uhk.fim.sensors.framets;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.uhk.fim.sensors.R;


public class SenzorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.getSensorList(Sensor.TYPE_ALL);

        textView = getView().findViewById(R.id.fragment_sensor_text1);
        LinearLayout linearLayout = getView().findViewById(R.id.fragment_layout);

        for (final Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            Button button = new Button(getContext());
            button.setText(sensor.getName());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sensorManager.registerListener(SenzorFragment.this, sensor, SensorManager.SENSOR_DELAY_UI);
                }
            });
            linearLayout.addView(button);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String helper = "";

        for (float v : event.values) {
            helper = helper + ", " + v;
        }


        textView.setText("Sensor say" + helper);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        textView.setText("Sensor say" + sensor.getResolution());
    }
}
