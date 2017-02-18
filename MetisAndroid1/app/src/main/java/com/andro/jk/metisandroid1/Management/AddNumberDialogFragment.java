package com.andro.jk.metisandroid1.Management;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.andro.jk.metisandroid1.R;


public class AddNumberDialogFragment extends DialogFragment {

    EditText etNumber,etName;
    Button addWorker,cancel;

    public interface AddNumberDialogListener {
        void onFinishAddNumber(String name, String number);
    }


    public AddNumberDialogFragment() {
    }

    public static AddNumberDialogFragment newInstance(String title) {
        AddNumberDialogFragment frag = new AddNumberDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_number_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etNumber = (EditText) view.findViewById(R.id.etNumber);
        etName = (EditText) view.findViewById(R.id.etName);
        // Fetch arguments from bundle and set title
        addWorker = (Button) view.findViewById(R.id.addWorker);
        cancel = (Button) view.findViewById(R.id.cancel);
        addWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBackResult();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        String title = getArguments().getString("title", "Add Worker");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        AddNumberDialogListener listener = (AddNumberDialogListener) getTargetFragment();
        listener.onFinishAddNumber(etName.getText().toString(),etNumber.getText().toString());
        dismiss();
    }


}
