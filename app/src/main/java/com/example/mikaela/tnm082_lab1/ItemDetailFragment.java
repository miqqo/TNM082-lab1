package com.example.mikaela.tnm082_lab1;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mikaela.tnm082_lab1.dummy.DummyContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_TITLE = "item_title";
    public static final String ARG_ITEM_RATING = "item_rating";
    public static final String ARG_ITEM_DESCRIPTION = "item_description";




    /**
     * The dummy content this fragment is presenting.
     */
    private String id;
    private String title;
    private int rating;
    private String description;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            id = getArguments().getString(ARG_ITEM_ID);
        }
        if(getArguments().containsKey(ARG_ITEM_TITLE)){
            title = getArguments().getString(ARG_ITEM_TITLE);
        }
        if(getArguments().containsKey(ARG_ITEM_RATING)){
            rating = getArguments().getInt(ARG_ITEM_RATING);
        }
        if(getArguments().containsKey(ARG_ITEM_DESCRIPTION)){
            description = getArguments().getString(ARG_ITEM_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if(id != null){
            ((TextView) rootView.findViewById(R.id.item_id)).setText(id);

        }

        if(title != null){
            ((TextView) rootView.findViewById(R.id.item_title)).setText(title);
        }
        if(rating > -1){
            ((RatingBar) rootView.findViewById(R.id.item_rating)).setRating(rating);
        }
        if(description != null){
            ((TextView) rootView.findViewById(R.id.item_description)).setText(description);
        }


        return rootView;
    }
}
