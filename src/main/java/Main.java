import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static final String MAP_DETAILS_FILE_NAME = "mapDetails";
    public static final String MAP_DOWNLOAD_DIRECTORY_PATH = System.getProperty("user.home") + "/Downloads/USGSMaps/";

    public static void barf(Exception e)
    {
        System.out.println("Barf: " + e.getMessage());
        throw new RuntimeException(e);
    }

    public static List<MapData> processMapDetails(String fileName)
    {
        List<MapData> mapDataList = new ArrayList<>();
        Thread currentThread = Thread.currentThread();
        ClassLoader classLoader = currentThread.getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader mapDetailsReader = new BufferedReader(inputStreamReader);
        String mapDetailsLine = null;
        out:while(true)
        {
            try
            {
                mapDetailsLine = mapDetailsReader.readLine();
            }
            catch(IOException e) { barf(e); }
            if(mapDetailsLine == null || mapDetailsLine.isEmpty()) { break out; }
            String[] mapDetailsLineItems = mapDetailsLine.split(" ");
            int cellID = Integer.parseInt(mapDetailsLineItems[0]);
            String state = mapDetailsLineItems[1];
            String cellName = mapDetailsLineItems[2].replaceAll("_", " ");
            double northLatitude = Double.parseDouble(mapDetailsLineItems[3]);
            double westLongitude = Double.parseDouble(mapDetailsLineItems[4]);
            double southLatitude = Double.parseDouble(mapDetailsLineItems[5]);
            double eastLongitude = Double.parseDouble(mapDetailsLineItems[6]);
            String downloadURL = mapDetailsLineItems[7];
            int gdaItemID = Integer.parseInt(mapDetailsLineItems[8]);
            MapData mapData = new MapData(cellID, state, cellName, northLatitude, westLongitude, southLatitude, eastLongitude, downloadURL, gdaItemID);
            mapDataList.add(mapData);
        }
        return mapDataList;
    }

    public static void downloadMaps(List<MapData> mapDataList)
    {
        for(int i = 0; i < mapDataList.size(); i++) {
            MapData mapData = mapDataList.get(i);

            // Generate map URL
            URL mapURL = null;
            try {
                mapURL = new URL(mapData.downloadURL);
            } catch (MalformedURLException e) {
                barf(e);
            }

            // Open channel to map URL
            ReadableByteChannel mapChannel = null;
            try {
                mapChannel = Channels.newChannel(mapURL.openStream());
            } catch (IOException e) {
                barf(e);
            }

            // Open stream for map file
            String streamPath = String.format("%s%s_%s.pdf", MAP_DOWNLOAD_DIRECTORY_PATH, mapData.cellID,
                    mapData.cellName);
            FileOutputStream mapStream = null;
            try
            {
                mapStream = new FileOutputStream(streamPath);
            }
            catch(FileNotFoundException e) { barf(e); }

            // Stream map from channel to file
            try
            {
                mapStream.getChannel().transferFrom(mapChannel, 0, Long.MAX_VALUE);
            }
            catch(IOException e) { barf(e); }
        }
    }

    public static void main(String[] args)
    {
        List<MapData> mapDataList = processMapDetails(MAP_DETAILS_FILE_NAME);
        downloadMaps(mapDataList);
    }
}