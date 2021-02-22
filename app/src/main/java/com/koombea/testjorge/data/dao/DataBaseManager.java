package com.koombea.testjorge.data.dao;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.ListenerToken;


public class DataBaseManager {

    private static Database mDatabase;
    private static DataBaseManager mInstance = null;
    private ListenerToken mListenerToken;
    public  String mCurrentUser = null;
    private static final String dbName = "koombeadb";
    private static final String username = "koombeauser";

    protected DataBaseManager() {

    }

    public static DataBaseManager getSharedInstance() {
        if (mInstance == null) {
            mInstance = new DataBaseManager();
        }

        return mInstance;
    }

    public static Database getDatabase() {
        return mDatabase;
    }

    // tag::initCouchbaseLite[]
    public void initCouchbaseLite(Context context) {
        CouchbaseLite.init(context);
    }
    // end::initCouchbaseLite[]

    // tag::userProfileDocId[]
    public String getCurrentUserDocId() {
        return "user::" + mCurrentUser;
    }
    // end::userProfileDocId[]

    public String getCurrentUser() {
        return mCurrentUser;
    }

    // tag::openOrCreateDatabase[]
    public void openOrCreateDatabaseForUser(Context context)
    // end::openOrCreateDatabase[]
    {
        // tag::databaseConfiguration[]
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDirectory(String.format("%s/%s", context.getFilesDir(), username));
        // end::databaseConfiguration[]

        mCurrentUser = username;

        try {
            // tag::createDatabase[]
            mDatabase = new Database(dbName, config);
            // end::createDatabase[]
            registerForDatabaseChanges();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    // tag::registerForDatabaseChanges[]
    private void registerForDatabaseChanges()
    // end::registerForDatabaseChanges[]
    {
        // tag::addDatabaseChangelistener[]
        // Add database change listener
        mListenerToken = mDatabase.addChangeListener(change -> {

            for(String docId : change.getDocumentIDs()) {
                Document doc = mDatabase.getDocument(docId);
                if (doc != null) {
                    Log.i("DatabaseChangeEvent", "Document was added/updated");
                } else {

                    Log.i("DatabaseChangeEvent", "Document was deleted");
                }
            }

        });
        // end::addDatabaseChangelistener[]
    }

    // tag::closeDatabaseForUser[]
    public void closeDatabaseForUser()
    // end::closeDatabaseForUser[]
    {
        try {
            if (mDatabase != null) {
                deregisterForDatabaseChanges();
                // tag::closeDatabase[]
                mDatabase.close();
                // end::closeDatabase[]
                mDatabase = null;
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    // tag::deregisterForDatabaseChanges[]
    private void deregisterForDatabaseChanges()
    // end::deregisterForDatabaseChanges[]
    {
        if (mListenerToken != null) {
            // tag::removedbchangelistener[]
            mDatabase.removeChangeListener(mListenerToken);
            // end::removedbchangelistener[]
        }
    }
}
