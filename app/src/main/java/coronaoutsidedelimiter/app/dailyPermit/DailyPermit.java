package coronaoutsidedelimiter.app.dailyPermit;


import android.location.Location;
import android.location.LocationManager;

import java.util.Date;

public class DailyPermit {
    Location theAddress ;
    Date theDate ;


    public DailyPermit(Location newAddress) {
        this.theAddress = newAddress ;
        this.theDate = new Date() ;
    }

    public Location getTheAddress() {
        return theAddress;
    }

    public void setTheAddress(Location theAddress) {
        this.theAddress = theAddress;
    }

    public Date getTheDate() {
        return theDate;
    }

    public void setTheDate(Date theDate) {
        this.theDate = theDate;
    }
}
