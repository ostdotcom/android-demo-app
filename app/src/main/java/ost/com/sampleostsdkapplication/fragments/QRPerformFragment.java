package ost.com.sampleostsdkapplication.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ost.mobilesdk.OstSdk;
import com.ost.mobilesdk.models.entities.OstDevice;
import com.ost.mobilesdk.workflows.OstContextEntity;
import com.ost.mobilesdk.workflows.OstWorkflowContext;
import com.ost.mobilesdk.workflows.interfaces.OstVerifyDataInterface;

import org.json.JSONObject;

import ost.com.sampleostsdkapplication.R;

/**
 * Fragment representing the Create user session screen for OstDemoApp.
 */
public class QRPerformFragment extends BaseFragment {
    private String mUserId;
    private String mTokenId;

    private LinearLayout mExternalView;
    private TextView mVerifyDataView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View childLayout = inflater.inflate(R.layout.qr_data_view, null);
        mExternalView = view.findViewById(R.id.external_view);
        mExternalView.addView(childLayout);
        mVerifyDataView = view.findViewById(R.id.verifyDataView);

        return view;
    }

    public String getPageTitle() {
        return getResources().getString(R.string.qr_perform);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserDetailsFragment.
     */
    public static QRPerformFragment newInstance(String tokenId, String userId) {
        QRPerformFragment fragment = new QRPerformFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.mTokenId = tokenId;
        fragment.mUserId = userId;
        return fragment;
    }

    @Override
    public void verifyData(OstWorkflowContext ostWorkflowContext, OstContextEntity ostContextEntity, OstVerifyDataInterface ostVerifyDataInterface) {
        super.verifyData(ostWorkflowContext, ostContextEntity, ostVerifyDataInterface);
        JSONObject jsonObject;
        if (OstSdk.DEVICE.equalsIgnoreCase(ostContextEntity.getEntityType())) {
            jsonObject = ((OstDevice) ostContextEntity.getEntity()).getData();
        } else {
            jsonObject = (JSONObject) ostContextEntity.getEntity();
        }

        mVerifyDataView.setText(jsonObject.toString());
        getNextButton().setText(getString(R.string.authorize));
        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ostVerifyDataInterface.dataVerified();
                getNextButton().setEnabled(false);
                showLoader();
            }
        });
    }
}