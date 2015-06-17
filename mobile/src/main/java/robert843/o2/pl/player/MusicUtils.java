/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package robert843.o2.pl.player;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class MusicUtils {

    private static final String TAG = "MusicUtils";

    public static void createPlaylist(ContentResolver resolver, String pName) {
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.NAME, pName);
        Uri newPlaylistUri = resolver.insert(uri, values);
        Log.d(TAG, "newPlaylistUri:" + newPlaylistUri);
    }

    public static void addToPlaylist(ContentResolver resolver, String audioId) {

        String audio_id = MediaStore.Audio.Playlists.Members.AUDIO_ID;
        // String playorder = MediaStore.Audio.Playlists.Members.PLAY_ORDER;
        String[] columns = { audio_id};
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", 10755);
        Cursor cur = resolver.query(uri, columns, null, null, null);
       // cur.moveToFirst();
       // final int base = cur.getInt(0);
      //  cur.close();
        ContentValues values = new ContentValues();
     //   values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, Integer.valueOf(base + audioId));
        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audioId);
        resolver.insert(uri, values);
    }

    public static void removeFromPlaylist(ContentResolver resolver, String audioId)
    {
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", 10755);
        resolver.delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID +" = "+audioId, null);
    }
    public static Cursor getPlaylistTracks(Context context, int playlist_id) {
        Uri newuri = MediaStore.Audio.Playlists.Members.getContentUri(
                "external", playlist_id);
        ContentResolver resolver = context.getContentResolver();
        String audio_id = MediaStore.Audio.Playlists.Members.AUDIO_ID;
       // String playorder = MediaStore.Audio.Playlists.Members.PLAY_ORDER;
        String[] columns = { audio_id};
        Cursor cursor = resolver.query(newuri, columns, null, null, null);
        return cursor;
    }
}
