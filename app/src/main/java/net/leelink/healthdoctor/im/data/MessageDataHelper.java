package net.leelink.healthdoctor.im.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDataHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String name = "MessageDataBase";


    public MessageDataHelper(Context context) {
        super(context, name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+name+"(content text , time text,isMeSend integer,isRead integer,sendId text,receiveId text,type integer,RecorderTime real );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
