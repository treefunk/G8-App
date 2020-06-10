package com.myoptimind.g8_app.features.announcementsmemos.bulletin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myoptimind.g8_app.R;

public class SingleBulletinFragment extends Fragment {

    private static final String ARGS_TITLE = "s_args_title";
    private static final String ARGS_DATE = "s_args_date";
    private static final String ARGS_BODY  = "s_args_body";
    private static final String ARGS_IMAGE = "s_args_image";

    private TextView tvTitle,tvDate,tvBody;
    private ImageView ivImage;

    public static SingleBulletinFragment newInstance(String title, String date, String body, String image) {

        Bundle args = new Bundle();

        SingleBulletinFragment fragment = new SingleBulletinFragment();
        args.putString(ARGS_TITLE,title);
        args.putString(ARGS_DATE,date);
        args.putString(ARGS_BODY,body);
        args.putString(ARGS_IMAGE,image);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_bulletin,container,false);

        Bundle bundle = getArguments();


        tvTitle = v.findViewById(R.id.tv_single_title);
        tvDate  = v.findViewById(R.id.tv_single_date);
        tvBody  = v.findViewById(R.id.tv_single_body);
        ivImage = v.findViewById(R.id.iv_single_image);


        tvTitle.setText(bundle.getString(ARGS_TITLE));
        tvDate.setText(bundle.getString(ARGS_DATE));
        tvBody.setText(bundle.getString(ARGS_BODY));
        ivImage.setImageResource(R.drawable.sample);


        return v;
    }
}
