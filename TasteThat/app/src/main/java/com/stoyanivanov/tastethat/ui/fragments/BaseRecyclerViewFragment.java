package com.stoyanivanov.tastethat.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;


public abstract class BaseRecyclerViewFragment extends BaseFragment {

    public static final int ORDER_TIMESTAMP = 0;
    public static final int ORDER_MOST_LIKED = 1;
    protected int currORDER = 0;

    protected void setupOptionsMenu(View view) {
        setHasOptionsMenu(true);

        ImageView optionsMenu = view.findViewById(R.id.iv_options_menu);
        optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().openOptionsMenu();
            }
        });

    }

    protected void configureSearchWidget(final EditText searchBar, final ImageView searchIcon,
                                         final ImageView cancelSearch, final CustomTextView selectedSectionHeader) {

        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    TasteThatApplication.showVirtualKeyboard();
                } else {
                    TasteThatApplication.hideVirtualKeyboard(v);
                }
            }
        });

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    startFilteringContent();
                    return true;
                }
                return false;
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBar.getVisibility() == View.VISIBLE) {
                    startFilteringContent();
                    TasteThatApplication.hideVirtualKeyboard(v);
                } else {
                    showAppBarSearch(searchBar,cancelSearch,selectedSectionHeader);
                }
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAdapterOnSearchCancel();
                searchBar.setText("");
                TasteThatApplication.hideVirtualKeyboard(v);
                showAppBarHeader(searchBar,cancelSearch,selectedSectionHeader);
            }
        });
    }


    private void showAppBarHeader(EditText searchBar, ImageView cancelSearch,
                                  CustomTextView selectedSectionHeader) {

        searchBar.setVisibility(View.INVISIBLE);
        cancelSearch.setVisibility(View.INVISIBLE);
        selectedSectionHeader.setVisibility(View.VISIBLE);
    }

    private void showAppBarSearch(EditText searchBar, ImageView cancelSearch,
                                  CustomTextView selectedSectionHeader) {

        selectedSectionHeader.setVisibility(View.INVISIBLE);
        searchBar.setVisibility(View.VISIBLE);
        cancelSearch.setVisibility(View.VISIBLE);
        searchBar.requestFocus();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        switch(currORDER) {
            case ORDER_TIMESTAMP:
                inflater.inflate(R.menu.actionbar_menu_timestamp, menu); break;

            case ORDER_MOST_LIKED:
                inflater.inflate(R.menu.actionbar_menu_likes, menu); break;
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    protected abstract void startFilteringContent();

    protected abstract void notifyAdapterOnSearchCancel();

    protected abstract void instantiateRV();
}
