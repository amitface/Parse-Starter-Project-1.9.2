package com.parse.snapstar;

/**
 * Created by root on 27/1/16.
 */
import java.io.File;

abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
