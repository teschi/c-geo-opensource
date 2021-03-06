package cgeo.geocaching.maps.google;

import cgeo.geocaching.IWaypoint;
import cgeo.geocaching.R;
import cgeo.geocaching.cgeoapplication;
import cgeo.geocaching.enumerations.CacheType;
import cgeo.geocaching.geopoint.Geopoint;
import cgeo.geocaching.go4cache.Go4CacheUser;
import cgeo.geocaching.maps.MapProviderFactory;
import cgeo.geocaching.maps.interfaces.CachesOverlayItemImpl;
import cgeo.geocaching.maps.interfaces.GeoPointImpl;
import cgeo.geocaching.maps.interfaces.MapProvider;
import cgeo.geocaching.maps.interfaces.OtherCachersOverlayItemImpl;

import com.google.android.maps.MapActivity;

import android.content.Context;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

public class GoogleMapProvider implements MapProvider {

    public final static int MAP = 1;
    public final static int SATELLITE = 2;

    private final Map<Integer, String> mapSources;

    private int baseId;

    public GoogleMapProvider(int _baseId) {
        baseId = _baseId;
        final Resources resources = cgeoapplication.getInstance().getResources();

        mapSources = new HashMap<Integer, String>();
        mapSources.put(baseId + MAP, resources.getString(R.string.map_source_google_map));
        mapSources.put(baseId + SATELLITE, resources.getString(R.string.map_source_google_satellite));
    }

    @Override
    public Map<Integer, String> getMapSources() {

        return mapSources;
    }

    @Override
    public boolean isMySource(int sourceId) {
        return sourceId >= baseId + MAP && sourceId <= baseId + SATELLITE;
    }

    public static boolean isSatelliteSource(int sourceId) {
        MapProvider mp = MapProviderFactory.getMapProvider(sourceId);
        if (mp instanceof GoogleMapProvider) {
            GoogleMapProvider gp = (GoogleMapProvider) mp;
            if (gp.baseId + SATELLITE == sourceId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Class<? extends MapActivity> getMapClass() {
        return GoogleMapActivity.class;
    }

    @Override
    public int getMapViewId() {
        return R.id.map;
    }

    @Override
    public int getMapLayoutId() {
        return R.layout.map_google;
    }

    @Override
    public GeoPointImpl getGeoPointBase(final Geopoint coords) {
        return new GoogleGeoPoint(coords.getLatitudeE6(), coords.getLongitudeE6());
    }

    @Override
    public CachesOverlayItemImpl getCachesOverlayItem(final IWaypoint coordinate, final CacheType type) {
        return new GoogleCacheOverlayItem(coordinate, type);
    }

    @Override
    public OtherCachersOverlayItemImpl getOtherCachersOverlayItemBase(Context context, Go4CacheUser userOne) {
        return new GoogleOtherCachersOverlayItem(context, userOne);
    }

}
