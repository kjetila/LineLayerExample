package com.example.kjetil.linelayerexample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MainActivity extends AppCompatActivity
         {

    private MapView mapView;
    private List<Point> routeCoordinates;
    private MapboxMap mMap;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);
        testButton = findViewById(R.id.btn_test);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "onMapReady: setting style url..");
                mMap.setStyleUrl("https://www.mapbox.com/android-docs/files/mapbox-raster-v8.json");
                for (Layer layer : mMap.getLayers()) {
                    Log.i("Layer", layer.getId() + " " + layer.getVisibility().toString());
                }
            }
        });

        // Create a list to store our line coordinates.
        routeCoordinates = new ArrayList<Point>();
        routeCoordinates.add(Point.fromLngLat(5.734, 58.979999999999997));
        routeCoordinates.add(Point.fromLngLat(6.620277100000067, 58.0630377));

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                //mapboxMap.setStyleUrl();
                // Create the LineString from the list of coordinates and then make a GeoJSON
                // FeatureCollection so we can add the line to our map as a layer.
                LineString lineString = LineString.fromLngLats(routeCoordinates);

                FeatureCollection featureCollection =
                        FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});

                Source geoJsonSource = new GeoJsonSource("line-source", featureCollection);

                mapboxMap.addSource(geoJsonSource);
                LineLayer lineLayer = new LineLayer("linelayer", "line-source");

                // The layer properties for our line. This is where we make the line dotted, set the
                // color, etc.
                lineLayer.setProperties(
                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineWidth(8f),
                        PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
                );

                mapboxMap.addLayer(lineLayer);
                mMap = mapboxMap;
            }
        });
        //mapView.setStyleUrl("https://lznorth-test.maplytic.no/style/style-v2.json");
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
