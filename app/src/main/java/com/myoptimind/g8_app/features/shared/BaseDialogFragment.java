package com.myoptimind.g8_app.features.shared;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.myoptimind.g8_app.R;

public class BaseDialogFragment extends DialogFragment {

    private ImageView btnCloseDialog;

    public BaseDialogFragment(){
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_NoActionBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorBlackA50)));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setBtnCloseDialog(ImageView btnCloseDialog) {
        this.btnCloseDialog = btnCloseDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(btnCloseDialog == null){
            btnCloseDialog = view.findViewById(R.id.iv_close);
        }

        if(btnCloseDialog != null){
            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
        }
    }
}
