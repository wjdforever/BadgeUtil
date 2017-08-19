package com.leobai.library.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.leobai.library.R;
import com.leobai.library.utils.BadgeUtil;
import com.leobai.library.view.BadgeFrameLayout;
import com.leobai.library.view.BadgeRadioButton;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * Created by Leo on 2017/8/19.
 */

public class BadgeTestActivity extends AppCompatActivity {

    private BadgeRadioButton tab0,tab1,tab2,tab3;
    private BadgeFrameLayout frameBadge;
    private Button updateAllBtn;

    private BadgeUtil.Notifier badgeNotifier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_badge);

        findViews();
        registerBadges();
        setupViews();
    }

    private void findViews(){
        tab0 = (BadgeRadioButton) findViewById(R.id.tab0);
        tab1 = (BadgeRadioButton) findViewById(R.id.tab1);
        tab2 = (BadgeRadioButton) findViewById(R.id.tab2);
        tab3 = (BadgeRadioButton) findViewById(R.id.tab3);
        frameBadge = (BadgeFrameLayout) findViewById(R.id.frame_badge);
        updateAllBtn = (Button) findViewById(R.id.btn_update);
    }

    private void registerBadges(){
        badgeNotifier = new BadgeUtil.Notifier();
        badgeNotifier.register(tab0);
        badgeNotifier.register(tab1);
        badgeNotifier.register(tab2);
        badgeNotifier.register(tab3);
        badgeNotifier.register(frameBadge);
        tab0.setBadgeId("tab0");
        tab1.setBadgeId("tab1");
        tab2.setBadgeId("tab2");
        tab3.setBadgeId("tab3");
        frameBadge.setBadgeId("frame");
    }

    private void setupViews(){
        tab0.setOnClickListener(badgeOnClickListener);
        tab1.setOnClickListener(badgeOnClickListener);
        tab2.setOnClickListener(badgeOnClickListener);
        tab3.setOnClickListener(badgeOnClickListener);
        frameBadge.setOnClickListener(badgeOnClickListener);
        updateAllBtn.setOnClickListener(updateAllOnClickListener);

    }

    private View.OnClickListener badgeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof BadgeUtil.Badge){
                ((BadgeUtil.Badge) v).update((int) (Math.random() * 200));
            }
        }
    };

    private View.OnClickListener updateAllOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List<BadgeUtil.BadgeNumber> list = new ArrayList<>();

            list.add(new BadgeUtil.BadgeNumber((int) (Math.random() * 200),"tab0"));
            list.add(new BadgeUtil.BadgeNumber((int) (Math.random() * 200),"tab1"));
            list.add(new BadgeUtil.BadgeNumber((int) (Math.random() * 200),"tab2"));
            list.add(new BadgeUtil.BadgeNumber((int) (Math.random() * 200),"tab3"));
            list.add(new BadgeUtil.BadgeNumber((int) (Math.random() * 200),"frame"));

            badgeNotifier.notifyUpdate(list);
        }
    };
}
