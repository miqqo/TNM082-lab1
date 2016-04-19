package com.example.mikaela.tnm082_lab1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.MenuItem;
import android.view.Menu;


import com.example.mikaela.tnm082_lab1.database.Datasource;
import com.example.mikaela.tnm082_lab1.database.Item;
import com.example.mikaela.tnm082_lab1.dummy.DummyContent;

import java.util.ArrayList;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {

    public Datasource datasource;
    public Item item;
    String title = "haj";
    int rating = 2;
    String description = "en läskig sak";
    int currentItem = 1;
    boolean ascending = true;
    public ArrayAdapter<Item> arrayAdapter;


    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id, String title, int rating, String description);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id, String title, int rating, String description) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datasource = new Datasource(getActivity());
        datasource.open();

        item = new Item();

        setHasOptionsMenu(true);

        currentItem = getActivity().getPreferences(Activity.MODE_PRIVATE).getInt("sort", 1);
        ascending = getActivity().getPreferences(Activity.MODE_PRIVATE).getBoolean("ascending", true);


        setListAdapter(new ArrayAdapter<Item>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                datasource.fetchAll(currentItem, ascending)
        ));
    }

    private ArrayAdapter castToArrayAdapter(ListAdapter adapter){
        return ((ArrayAdapter) adapter);
    }

    public void addItem(){

        datasource.insertItem(title, rating, description);
        arrayAdapter = castToArrayAdapter(getListAdapter());
        arrayAdapter.clear();
        arrayAdapter.addAll(datasource.fetchAll(currentItem, ascending));

    }

    public void removeItemFromMobile(int id){
        arrayAdapter = castToArrayAdapter(getListAdapter());
        datasource.deleteItem(id);

        for (int i = 0; i < arrayAdapter.getCount(); i++) {

            if (arrayAdapter.getItem(i).id == id) {
                arrayAdapter.remove(arrayAdapter.getItem(i));
                arrayAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void removeItem(int position) {
        arrayAdapter = castToArrayAdapter(getListAdapter());

        int newId = (int) arrayAdapter.getItem(position).id;
        arrayAdapter.remove(arrayAdapter.getItem(position));
        arrayAdapter.notifyDataSetChanged();
        datasource.deleteItem(newId);

        getActivity().getSupportFragmentManager().popBackStack();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addItem();
                return true;
            case R.id.sort:
                SortingDialog dialog = new SortingDialog();
                dialog.show(getActivity().getFragmentManager(), "dialog");
                return true;
            case R.id.acending:
                item.setChecked(!item.isChecked());
                if(item.isChecked()){

                    ascending = false;
                    item.setChecked(false);
                }
                else{
                    ascending = true;
                    item.setChecked(true);

                }
                SharedPreferences prefs = getActivity().getPreferences(Activity.MODE_PRIVATE);
                prefs.edit().putBoolean("ascending", ascending).commit();

                arrayAdapter = castToArrayAdapter(getListAdapter());
                arrayAdapter.clear();
                arrayAdapter.addAll(datasource.fetchAll(currentItem, ascending));
                arrayAdapter.notifyDataSetChanged();


                return true;
            case R.id.help:
                return true;
            case R.id.delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sort(int sortingItem){
        currentItem = sortingItem;

        ascending = getActivity().getPreferences(Activity.MODE_PRIVATE).getBoolean("ascending", true);

        datasource.getCursor(currentItem, true);
        ArrayAdapter arrayAdapter = castToArrayAdapter(getListAdapter());
        arrayAdapter.clear();
        arrayAdapter.addAll(datasource.fetchAll(sortingItem, ascending));
        arrayAdapter.notifyDataSetChanged();

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        datasource.close();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        setActivatedPosition(position);
        mCallbacks.onItemSelected(String.valueOf(datasource.fetchAll(currentItem, true).get(position).getId()),
                datasource.fetchAll(currentItem, true).get(position).getTitle(),
                datasource.fetchAll(currentItem, true).get(position).getRating(),
                datasource.fetchAll(currentItem, true).get(position).getDescription());

        ((AppCompatActivity)getActivity()).startSupportActionMode(ActionModeCallBack);




    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private final ActionMode.Callback ActionModeCallBack  = new ActionMode.Callback() {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // TODO Auto-generated method stub
            switch (item.getItemId()){
                case R.id.delete:
                    removeItem(mActivatedPosition);
                    mActivatedPosition = ListView.INVALID_POSITION;
                    return true;
                case R.id.edit:
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            mode.getMenuInflater().inflate(R.menu.menu_items2, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub

            mode.setTitle("CheckBox is Checked");
            return false;
        }

        private ArrayAdapter castToArrayAdapter(ListAdapter adapter){
            return ((ArrayAdapter) adapter);
        }





    };


}



