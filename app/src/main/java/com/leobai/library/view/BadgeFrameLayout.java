package com.leobai.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.leobai.library.utils.BadgeUtil;

import java.util.List;

/**
 * 支持Badge的RadioButton
 * Created by LeoMrBai on 2017/7/27.
 */

public class BadgeFrameLayout extends FrameLayout implements BadgeUtil.Badge {

    private int number;
    private BadgeUtil.BadgeDrawer drawer;
    private String id;

    public BadgeFrameLayout(Context context) {
        super(context);
    }

    public BadgeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BadgeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (drawer == null){
            drawer = drawer();
        }
        drawer.draw(canvas);
    }

    @Override
    public void update(int number) {
        this.number = number;
        invalidate();
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public BadgeUtil.BadgeDrawer drawer() {
        if (drawer == null) drawer = new BadgeUtil.BadgeDrawer(this);
        return drawer;
    }

    @Override
    public String getBadgeId() {
        return id;
    }

    @Override
    public void setBadgeId(String id) {
        this.id = id;
    }

    @Override
    public void updateBadge(List<BadgeUtil.BadgeNumber> badgeNumbers) {

    }
}
