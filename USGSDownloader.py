#!/usr/bin/env python3
import urllib.request
import pathlib
import os

class MapData:
    def __init__(self, new_cell_id, new_state, new_cell_name, new_north_latitude, new_west_longitude, new_south_latitude, new_east_longitude, new_download_url, new_gda_item_id):
        self.cellId = new_cell_id
        self.state = new_state
        self.cellName = new_cell_name
        self.northLatitude = new_north_latitude
        self.westLongitude = new_west_longitude
        self.southLatitude = new_south_latitude
        self.eastLongitude = new_east_longitude
        self.downloadURL = new_download_url
        self.gdaItemID = new_gda_item_id
mapDataList = []
downloadedFileIDs = []

with open("mapDetails", "r") as mapDetails:
    mapDetailsText = mapDetails.readlines()

for mapDetailsLine in mapDetailsText:
    mapDetailsLineData = mapDetailsLine.split(" ")
    cellID = mapDetailsLineData[0]
    state = mapDetailsLineData[1]
    cellName = mapDetailsLineData[2]
    northLatitude = mapDetailsLineData[3]
    westLongitude = mapDetailsLineData[4]
    southLatitude = mapDetailsLineData[5]
    eastLongitude = mapDetailsLineData[6]
    downloadURL = mapDetailsLineData[7]
    gdaItemID = mapDetailsLineData[8]
    mapData = MapData(cellID, state, cellName, northLatitude, westLongitude, southLatitude, eastLongitude, downloadURL, gdaItemID)
    mapDataList.append(mapData)

with open("downloaded", "r") as downloaded:
    downloadedFileIDs = set([downloadedFileID.strip() for downloadedFileID in downloaded.readlines()])

for i, mapData in enumerate(mapDataList):
    if not mapData.cellId in downloadedFileIDs:
        mapFileDirectoryPath = pathlib.Path.home().joinpath("Downloads").joinpath("USGSMaps")
        if not os.path.exists(str(mapFileDirectoryPath)):
            os.makedirs(str(mapFileDirectoryPath))
        mapFileName = str(mapFileDirectoryPath.joinpath(mapData.cellId + "_" + mapData.cellName + ".pdf"))
        print("Downloading " + mapFileName + "... ", end="", flush=True)
        urllib.request.urlretrieve(mapData.downloadURL, mapFileName)
        with open("downloaded", "a") as downloaded:
            downloaded.write(mapData.cellId + "\n")
        print("done ({}/{})".format(i + 1, len(mapDataList)))
