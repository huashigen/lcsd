package com.lcshidai.lc.ui;

import android.os.Bundle;
import android.view.View;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PublicImageViewActivity extends TRJActivity {

    @Bind(R.id.pv_scale)
    PhotoView pvScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_image_view);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("IMG_URL");
        Glide.with(mContext).load(url).into(pvScale);
        pvScale.enable();
        pvScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }
}
