package coronaoutsidedelimiter.app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import coronaoutsidedelimiter.app.dailyPermit.DailyPermit;

public class MainActivity extends AppCompatActivity {
    DailyPermit theDailyPermit;
    LocationManager theLM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.theDailyPermit = new DailyPermit( new Location(LocationManager.GPS_PROVIDER) ) ;


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();

        // This verification should be done during onStart() because the system calls
        // this method when the user returns to the activity, which ensures the desired
        // location provider is enabled each time the activity resumes from the stopped state.
        this.theLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = this.theLM.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.allowGPSTitle))
                    .setMessage(R.string.allowGPS)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enableLocationSettings();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    });
        } else {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            theLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (
                            Math.sqrt(
                                    ((location.getLatitude() - theDailyPermit.getTheAddress().getLatitude()) * (location.getLatitude() - theDailyPermit.getTheAddress().getLatitude()))
                                            +
                                            ((location.getLongitude() - theDailyPermit.getTheAddress().getLongitude()) * (location.getLongitude() - theDailyPermit.getTheAddress().getLongitude()))
                            )
                                    > 1000
                    ) {
                        new AlertDialog.Builder(MainActivity.this).setMessage(R.string.outOfAllowedRange);
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
    }


    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

}
