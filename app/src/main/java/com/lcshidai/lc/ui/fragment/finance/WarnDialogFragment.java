package com.lcshidai.lc.ui.fragment.finance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lcshidai.lc.R;


/**
 * Created by huashigen on 2017-07-13.
 */

public class WarnDialogFragment extends DialogFragment {
    private OnActionBtnClickListener onActionBtnClickListener;
    private View rootView;

    public void setOnActionBtnClickListener(OnActionBtnClickListener onActionBtnClickListener) {
        this.onActionBtnClickListener = onActionBtnClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_fragment_warn, container, false);
        Button btnOk = rootView.findViewById(R.id.btnCharge);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes()
                .height);
    }

    public interface OnActionBtnClickListener {
        public void onLick(String content);
    }
}
