package com.andro.jk.metisandroid1.Management;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andro.jk.metisandroid1.Models.CategoryModel;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.andro.jk.metisandroid1.WebUtils.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Saikiran on 8/10/2016.
 */
public class ChangeCategoryDialogFragment extends DialogFragment {

    RadioGroup categoryRadioGroup;
    Button changeCategory, addCategoryCancel;
    private int categoryNo, categoryId;
    final List<String> categories = new ArrayList<String>();
    final List<Integer> categoryIds = new ArrayList<Integer>();
    int receivedCatId;

    public interface ChangeCategoryDialogListener {
        void onFinishChangeCategory(int categoryId);
    }


    public ChangeCategoryDialogFragment() {
    }

    public static ChangeCategoryDialogFragment newInstance(String title,int receivedCatId) {
        ChangeCategoryDialogFragment frag = new ChangeCategoryDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("receivedCatId",receivedCatId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_category_dialog, container);


    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryRadioGroup = (RadioGroup) view.findViewById(R.id.categoryRadioGroup);


        final String token = "Token ";
        final String access_token = SaveSharedPreference.getAccessToken(getContext());
        String access_token_wo_quotes = access_token.replace("\"", "");
        final String s = token.concat(access_token_wo_quotes);
        receivedCatId = getArguments().getInt("receivedCatId");

        RestClient.get().getCategories(s, new Callback<List<CategoryModel>>() {
            @Override
            public void success(List<CategoryModel> categoryModels, Response response) {
                //categories.add("Select Category");
                for (CategoryModel categoryModel : categoryModels) {
                    categories.add(categoryModel.getCategory());
                    categoryIds.add(categoryModel.getCategoryId());

                }
                    int index = categoryIds.indexOf(receivedCatId);

                    categories.remove(index);
                    categoryIds.remove(index);

                    Log.d("msg1", categories.toString());
                    Log.d("msg2", categoryIds.toString());
                    categoryNo = categories.size();



                for (int i = 1; i <= categoryNo; i++) {
                    Log.d("key23",Integer.toString(categoryNo));
                    Log.d("key22","fired");
                    RadioButton rdbtn = new RadioButton(getContext());
                    int rbdid = 14110055 + i;
                    rdbtn.setId(rbdid);
                    //rprms= new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    rdbtn.setText(categories.get(i-1));

                    categoryRadioGroup.addView(rdbtn);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("key11", error.toString());
                dismiss();
                Toast.makeText(getContext(),"Failed to fetch categories, Try after some time",Toast.LENGTH_SHORT).show();
            }
        });

        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int key = checkedId - 14110055;
                categoryId = categoryIds.get(key - 1);
                Log.d("key23",Integer.toString(categoryId));
            }
        });




        changeCategory = (Button) view.findViewById(R.id.changeCategory);
        addCategoryCancel = (Button) view.findViewById(R.id.addCategoryCancel);
        changeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBackResult();
            }
        });

        addCategoryCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        String title = getArguments().getString("title", "Confirm Change Category");
        getDialog().setTitle(title);
    }

    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        ChangeCategoryDialogListener listener = (ChangeCategoryDialogListener) getTargetFragment();
        listener.onFinishChangeCategory(categoryId);
        dismiss();
    }

}
