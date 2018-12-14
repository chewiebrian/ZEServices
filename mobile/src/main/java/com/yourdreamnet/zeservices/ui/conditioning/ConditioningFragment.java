package com.yourdreamnet.zeservices.ui.conditioning;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yourdreamnet.zeservices.MainActivity;
import com.yourdreamnet.zeservices.QueueSingleton;
import com.yourdreamnet.zeservices.R;
import com.yourdreamnet.zeservices.ZEServicesAPI;

import java.util.Objects;

public class ConditioningFragment extends Fragment {

    private ConditioningViewModel mViewModel;

    public static ConditioningFragment newInstance() {
        return new ConditioningFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.conditioning_fragment, container, false);
    }

    private ZEServicesAPI.AuthenticatedAPI getApi() {
        return ((MainActivity) getActivity()).getApi();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ConditioningViewModel.class);

        Button condition = getView().findViewById(R.id.start_conditioning);
        ZEServicesAPI.AuthenticatedAPI api = getApi();
        condition.setOnClickListener(
            view -> api.startPrecondition(QueueSingleton.getQueue(), api.getCurrentVin()).
                subscribe(result -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    TextView status = getView().findViewById(R.id.status);
                    status.setText(R.string.started_condition);
                }), error -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    Log.e("Conditioning", "Unable to pre-condition car", error);
                    TextView status = getView().findViewById(R.id.status);
                    status.setText(R.string.error_conditioning);
                })
            )
        );
    }

}
