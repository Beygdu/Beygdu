package is.arnastofnun.beygdu;


import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.UiSettings;

import org.w3c.dom.Text;

import java.util.logging.Handler;

public class MapsActivity extends NavDrawer {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_maps, frameLayout);
        setTitle(R.string.title_activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.map_info_window, null);
                        TextView info1 = (TextView) v.findViewById(R.id.info1);
                        TextView info2 = (TextView) v.findViewById(R.id.info2);
                        TextView info3 = (TextView) v.findViewById(R.id.info3);
                        TextView info4 = (TextView) v.findViewById(R.id.info4);
                        info1.setText("Árnastofnun");
                        info2.setText("Sími. 525-4010");
                        info3.setText("Netfang: arnastofnun@hi.is");
                        info4.setText("Opnunartími: 9-12 og 13-15");
                        return v;
                    }
                });
            }
        }


    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Árnastofnun.
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(64.14330015, -21.96268916))
                        .title("Árnastofnun")
        );
        mMap.animateCamera(CameraUpdateFactory.zoomBy(13));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 20.0f ) );

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);


        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(64.14330015, -21.96268916));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 4000, null);
        mMap.moveCamera(center);

    }

}
