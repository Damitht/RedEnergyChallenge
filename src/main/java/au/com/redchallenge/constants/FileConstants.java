package au.com.redchallenge.constants;

/***
 * Constants shared among all classes in the project
 */

import au.com.redchallenge.domain.Quality;

import java.util.HashMap;
import java.util.Map;

public class FileConstants {
    public interface FileRowIndicators {
        String START_OF_FILE = "100";
        String START_OF_DATA = "200";
        String METER_READINGS = "300";
        String END_OF_FILE = "900";
    }

    public interface recordColumnSize {
        int type200 = 3;
        int type300 = 4;
    }

    public static Map getQualityMap(){
        Map<String, String> qualityMap = new HashMap<String, String>();
        for(Quality temQuality : Quality.values()){
            qualityMap.put(temQuality.toString(), temQuality.toString());
        }
        return qualityMap;
    }
}
