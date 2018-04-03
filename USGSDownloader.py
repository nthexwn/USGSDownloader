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

mapDataList = list(map(lambda mapDetailsLine: MapData(*mapDetailsLine.split()), mapDetailsText))

with open("downloaded", "r") as downloaded:
    downloadedFileIDs = set([downloadedFileID.strip() for downloadedFileID in downloaded.readlines()])

for i, mapData in enumerate(mapDataList):
    if not mapData.cellId in downloadedFileIDs:
        mapFileDirectoryPath = pathlib.Path.home().joinpath("Downloads").joinpath("USGSMaps")
        if not os.path.exists(str(mapFileDirectoryPath)):
            os.makedirs(str(mapFileDirectoryPath))
        mapFileName = str(mapFileDirectoryPath.joinpath(mapData.cellId + "_" + mapData.cellName + ".pdf"))
        print("Downloading {}...".format(mapFileName), end="", flush=True)
        urllib.request.urlretrieve(mapData.downloadURL, mapFileName)
        with open("downloaded", "a") as downloaded:
            downloaded.write(mapData.cellId + "\n")
        print("done ({}/{})".format(i + 1, len(mapDataList)))
