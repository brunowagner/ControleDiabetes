package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.Event;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {
    private ArrayList<Event> mEvents;

    private SQLiteDatabase mDb;
    CalculoDeBolusDBHelper mDbHelper;
    private final EventAdapter.EventAdapterOnClickHandler mClickHandler;

    public EventAdapter(CalculoDeBolusDBHelper calculoDeBolusDBHelper, EventAdapter.EventAdapterOnClickHandler clickHandler) {
        mDbHelper = calculoDeBolusDBHelper;
        mDb = mDbHelper.getWritableDatabase();
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public EventAdapter.EventAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdFromListItem = R.layout.events_list_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
        return new EventAdapter.EventAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventAdapterViewHolder eventAdapterViewHolder, int position) {
        Event event = mEvents.get(position);

        eventAdapterViewHolder.itemView.setTag(event.getId());
        eventAdapterViewHolder.mEventTextView.setText(event.getText());
        eventAdapterViewHolder.mSourceTextView.setText(String.valueOf(event.getSource()));
    }

    @Override
    public int getItemCount() {
        if (mEvents == null) return 0;
        return mEvents.size();
    }


    // - Methods ------------------------------------------------------------------------------------------

    private Cursor getAllData() {
        return mDb.query(CalculoDeBolusContract.EventEntry.TABLE_NAME,
                null, null, null, null,null,
                CalculoDeBolusContract.EventEntry.COLUMN_EVENT_NAME);
    }

    public void refreshData(){

        Cursor cursor = getAllData();
        Log.d("bwvm", "refreshData: tamanho do cursor" + cursor.getCount());
        if (cursor.getCount() <= 0) return;
        ArrayList<Event> events = new ArrayList<Event>();
        if (cursor.moveToFirst()){
            do {
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.EventEntry._ID)));
                event.setText(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.EventEntry.COLUMN_EVENT_NAME)));
                event.setSource(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.EventEntry.COLUMN_SOURCE_NAME)));

                events.add(event);

            }while(cursor.moveToNext());

        }
        cursor.close();
        setEvents(events);
    }

    public void setEvents (ArrayList<Event> events){
        mEvents = events;
        notifyDataSetChanged();
    }

    // - Inner Class ------------------------------------------------------------------------------------------
    public class EventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mEventTextView;
        public final TextView mSourceTextView;


        public EventAdapterViewHolder(View itemView){
            super(itemView);
            mEventTextView = itemView.findViewById(R.id.tv_event);
            mSourceTextView = itemView.findViewById(R.id.tv_event_source);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Event event = mEvents.get(position);
            mClickHandler.onClick(event);
        }
    }

    // - Interfaces ----------------------------------------------------------------------------------------
    public interface EventAdapterOnClickHandler{
        void onClick(Event event);
    }
}
