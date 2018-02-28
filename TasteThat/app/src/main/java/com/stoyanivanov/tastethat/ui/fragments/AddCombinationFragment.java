package com.stoyanivanov.tastethat.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.ui.activities.ImageActivity;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddCombinationFragment extends BaseFragment {

    @BindView(R.id.ll_add_fields_container) LinearLayout addFieldsContainer;
    @BindView(R.id.btn_add_combination) Button addCombination;
    @BindView(R.id.btn_add_discard) ImageView discardChanges;
    @BindView(R.id.view_first_ingredient) View firstIngredientField;
    @BindView(R.id.view_second_ingredient) View secondIngredientField;

    private ArrayList<View> allFields = new ArrayList<>();
    private ViewGroup container;

    public static AddCombinationFragment newInstance() {
        return new AddCombinationFragment();
    }

    @OnClick(R.id.btn_add_combination)
        void startNewImageActivity() {
            startImageActivity();
        }

    @OnClick(R.id.btn_add_discard)
        void discardChanges(View view) {
            discardUserChanges();
            TasteThatApplication.hideVirtualKeyboard(view);
        }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_add_combination, inflater, container);

        this.container = container;

        allFields.clear();
        configureFirstTwoFields();

        return view;
    }


    private void discardUserChanges() {
        for(View field : allFields) {
            EditText componentField =  field.findViewById(R.id.et_component);
            componentField.setText("");
        }

        allFields.get(0).requestFocus();
    }

    private void configureFirstTwoFields() {
        Button newFieldBtnFirst =  firstIngredientField.findViewById(R.id.btn_add_new_et);
        CustomTextView fieldCounterFirst = firstIngredientField.findViewById(R.id.ctv_et_counter);
        allFields.add(firstIngredientField);

        newFieldBtnFirst.setVisibility(View.INVISIBLE);
        fieldCounterFirst.setText(generateStringForFieldCounter());

        Button newFieldBtnSecond = secondIngredientField.findViewById(R.id.btn_add_new_et);
        CustomTextView fieldCounterSecond = secondIngredientField.findViewById(R.id.ctv_et_counter);
        allFields.add(secondIngredientField);

        newFieldBtnSecond.setOnClickListener(newFieldBtnOnclick);
        fieldCounterSecond.setText(generateStringForFieldCounter());
    }

    View.OnClickListener newFieldBtnOnclick =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setVisibility(View.INVISIBLE);
            inflateNewField();
        }
    };

    private void inflateNewField() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View newField = inflater.inflate(R.layout.add_field_inflatable, container,false);
        if (newField != null) {
            allFields.add(newField);
            configureNewField(newField);
            addFieldsContainer.addView(newField);
        }
    }

    private void configureNewField(View newField) {
        Button newFieldBtn = newField.findViewById(R.id.btn_add_new_et);
        CustomTextView fieldCounter = newField.findViewById(R.id.ctv_et_counter);

        newField.findViewById(R.id.et_component).requestFocus();

        if (allFields.size() >= Constants.MAX_ADD_FIELDS) {
            newFieldBtn.setVisibility(View.INVISIBLE);
        } else {
            newFieldBtn.setOnClickListener(newFieldBtnOnclick);
        }

        fieldCounter.setText(generateStringForFieldCounter());

        newField.requestFocus();
    }

    @NonNull
    private String generateStringForFieldCounter() {
        return Integer.toString(allFields.size()) + "/" + Integer.toString(Constants.MAX_ADD_FIELDS);
    }

    private void startImageActivity() {
        ArrayList<String> components = getAllComponents();

        if(components.size() >= Constants.MIN_REQUIRED_COMPONENTS) {
            startActivity(ImageActivity.getIntent(getActivity(),
                    FragmentTags.CHOOSE_IMAGE_FRAGMENT, components));
        } else {
            TasteThatApplication.showToast(getString(R.string.toast_invalid_input));
        }
    }

    private ArrayList<String> getAllComponents() {
        ArrayList<String> components = new ArrayList<>();

        for (View field: allFields) {
            EditText editText = field.findViewById(R.id.et_component);
            String component =  editText.getText().toString();

            if(!component.equals("")) {
                components.add(component);
            }
        }

        return components;
    }
}