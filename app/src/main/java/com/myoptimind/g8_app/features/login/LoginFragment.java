package com.myoptimind.g8_app.features.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.dashboard.DashboardFragment;
import com.myoptimind.g8_app.features.dashboard.MainActivity;
import com.myoptimind.g8_app.features.shared.SharedPref;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPref.getInstance(getActivity()).loggedInExists()) {
            Intent intent = MainActivity.newIntent(getActivity());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.from(getActivity()).inflate(R.layout.fragment_login, container, false);
        }

        initLogin();

        return view;
    }

    private void initLogin() {

        EditText etEmployeeNumber = view.findViewById(R.id.et_employee_number);
        EditText etPassword       = view.findViewById(R.id.et_password);
        Button btnLogin         = view.findViewById(R.id.btn_login);



        final LoginViewModel loginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);




        btnLogin.setOnClickListener(
                v -> {
                    loginViewModel.authenticateUser(
                            etEmployeeNumber.getText().toString(),
                            etPassword.getText().toString()
                    );
                }
        );

        loginViewModel.getAuthResponse().observe(this.getViewLifecycleOwner(), authResponse -> {
            if(authResponse.getMeta().getStatus().equals("200")){
                Toast.makeText(getActivity(),authResponse.getMeta().getMessage(),Toast.LENGTH_LONG).show();
                Intent mainActivityIntent = MainActivity.newIntent(getActivity());
                startActivity(mainActivityIntent);
            }
        });

        loginViewModel.getErrorString().observe(this.getViewLifecycleOwner(),s -> {
            Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
        });

        loginViewModel.getIsRequesting().observe(getViewLifecycleOwner(), isRequesting -> {
            btnLogin.setEnabled(!isRequesting);
        });


    }
}
