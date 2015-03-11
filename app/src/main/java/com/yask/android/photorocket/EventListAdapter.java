package com.yask.android.photorocket;

import android.content.Context;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ylwu on 3/5/15.
 */
public class EventListAdapter extends ParseQueryAdapter<Event> {

    public EventListAdapter(Context context, ParseUser user){
        super (context,new QueryFactory<Event>() {
            @Override
            public ParseQuery<Event> create() {
                ParseQuery query = new ParseQuery("Event");
                query.whereEqualTo(Event.PARTICIPANTS_KEY, ParseUser.getCurrentUser());
                query.include(Event.PARTICIPANTS_KEY);
                //loading event from local database
                query.fromLocalDatastore();
                return query;
            }
        });
    }

    public EventListAdapter(Context context, ParseUser user, final boolean occuring){
        super (context,new QueryFactory<Event>() {
            @Override
            public ParseQuery<Event> create() {
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                if (occuring){
                    ParseQuery query = new ParseQuery("Event");
                    query.whereLessThanOrEqualTo(Event.STARTTIME_KEY,d);
                    query.whereGreaterThanOrEqualTo(Event.ENDTIME_KEY,d);
                    query.whereEqualTo(Event.PARTICIPANTS_KEY, ParseUser.getCurrentUser());
                    query.include(Event.PARTICIPANTS_KEY);
                    query.fromLocalDatastore();
                    return query;
                } else {
                    ParseQuery beforeQuery = new ParseQuery("Event");
                    beforeQuery.whereLessThanOrEqualTo(Event.ENDTIME_KEY,d);
                    ParseQuery afterQuery = new ParseQuery("Event");
                    afterQuery.whereGreaterThanOrEqualTo(Event.STARTTIME_KEY,d);
                    List<ParseQuery> queries = new ArrayList<ParseQuery>();
                    queries.add(beforeQuery);
                    queries.add(afterQuery);
                    ParseQuery joinedQuery = ParseQuery.or(queries);
                    joinedQuery.whereEqualTo(Event.PARTICIPANTS_KEY, ParseUser.getCurrentUser());
                    joinedQuery.include(Event.PARTICIPANTS_KEY);
                    joinedQuery.fromLocalDatastore();
                    return joinedQuery;
                }
            }
        });
    }
}
