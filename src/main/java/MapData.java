public class MapData {
    public int cellID;
    public String state;
    public String cellName;
    public double northLatitude;
    public double westLongitude;
    public double southLatitude;
    public double eastLongitude;
    public String downloadURL;
    public int gdaItemID;

    public MapData(int cellID, String state, String cellName, double northLatitude, double westLongitude,
                   double SouthLatitude, double eastLongitude, String downloadURL, int gdaItemID)
    {
        this.cellID = cellID;
        this.state = state;
        this.cellName = cellName;
        this.northLatitude = northLatitude;
        this.westLongitude = westLongitude;
        this.southLatitude = southLatitude;
        this.eastLongitude = eastLongitude;
        this.downloadURL = downloadURL;
        this.gdaItemID = gdaItemID;
    }
}
