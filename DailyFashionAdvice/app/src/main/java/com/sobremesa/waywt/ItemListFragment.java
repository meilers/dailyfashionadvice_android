package com.sobremesa.waywt;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.sobremesa.waywt.adapters.ThreadAdapter;
import com.sobremesa.waywt.database.ThreadTable;
import com.sobremesa.waywt.models.RemoteThread;
import com.sobremesa.waywt.providers.DFAContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 */
public class ItemListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";


    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private boolean mActivateOnClick;

    private RecyclerView mThreadRv;
    private LinearLayoutManager mThreadLayoutManager;
    private ThreadAdapter mThreadAdapter;
    private List<RemoteThread> mThreads;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mThreads = new ArrayList<RemoteThread>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thread_list, null, false);

        mThreadRv = (RecyclerView) view.findViewById(R.id.fragment_thread_list_rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mThreadRv.setHasFixedSize(true);

        // use a linear layout manager
        mThreadLayoutManager = new LinearLayoutManager(getActivity());
        mThreadRv.setLayoutManager(mThreadLayoutManager);

        // specify an adapter (see also next example)

        mThreadAdapter = new ThreadAdapter(mThreads);
        mThreadRv.setAdapter(mThreadAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().getSupportLoaderManager().initLoader(DFAConstants.THREADS_LOADER_ID, null, this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        mActivateOnClick = activateOnItemClick;
    }


    private void setActivatedPosition(int position) {
        if( mActivateOnClick )
        {
            if (position == ListView.INVALID_POSITION) {
                mThreadAdapter.clearSelections();
            } else {
                mThreadAdapter.toggleSelection(position);
            }

            mActivatedPosition = position;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        switch (id)
        {
            case DFAConstants.THREADS_LOADER_ID:
                return new CursorLoader(getActivity(), DFAContentProvider.Uris.THREADS_URI, ThreadTable.ALL_COLUMNS, null, null, null);

        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        switch (cursorLoader.getId()) {

            case DFAConstants.THREADS_LOADER_ID:
                if (cursor != null) {
                    mThreads.clear();

                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        mThreads.add(new RemoteThread(cursor));
                    }

                    mThreadAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

}
