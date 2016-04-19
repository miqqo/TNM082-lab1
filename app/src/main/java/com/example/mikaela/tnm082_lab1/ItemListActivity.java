package com.example.mikaela.tnm082_lab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mikaela.tnm082_lab1.database.Datasource;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details
 * (if present) is a {@link ItemDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class
        ItemListActivity extends AppCompatActivity
        implements ItemListFragment.Callbacks {



    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    int currentItem = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_app_bar);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());



        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        //inflate: add items to the action
        getMenuInflater().inflate(R.menu.menu_items, menu);

        return true;
    }

    /**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id, String title, int rating, String description) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            getSupportFragmentManager().popBackStack();

            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
            arguments.putString(ItemDetailFragment.ARG_ITEM_TITLE, title);
            arguments.putInt(ItemDetailFragment.ARG_ITEM_RATING, rating);
            arguments.putString(ItemDetailFragment.ARG_ITEM_DESCRIPTION, description);
            
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);


            getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).addToBackStack(null).commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_TITLE, title);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_RATING, rating);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_DESCRIPTION, description);
           // startActivity(detailIntent);
            startActivityForResult(detailIntent, 1);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra(ItemDetailFragment.ARG_ITEM_ID);
                int id = (int) Long.parseLong(result);
                ((ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.item_list)).removeItemFromMobile(id);


            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void itemSort(int currentItem){
        ((ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.item_list)).sort(currentItem);

    }
}
