package com.leobai.library.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * this is the util class to integrate badge and manage them.
 * Created by LeoMrBai on 2017/7/27.
 */

public class BadgeUtil {

    /**
     * The Badge
     */
    public interface Badge extends UpdateAble {

        int getNumber();

        BadgeDrawer drawer();

        String getBadgeId();

        void setBadgeId(String id);
    }

    public interface UpdateAble {
        void update(int number);
        void updateBadge(List<BadgeNumber> badgeNumbers);
    }

    /**
     * This do the draw job.
     * for View you should call {@link #draw(Canvas)} in onDraw(Canvas) method.
     * for ViewGroup you should call {@link #draw(Canvas)} in dispatchDraw(Canvas) method.
     */
    public static class BadgeDrawer {
        private Badge badge;
        private Paint paint;
        private int textSize = dpToPx(9);
        private int radius = dpToPx(8);
        private int paddingTop = dpToPx(4);
        private int paddingRight = dpToPx(8);
        private int minWidth = dpToPx(56);
        private int minHeight = dpToPx(56);
        private int textColor = Color.WHITE;
        private int backgroundColor = Color.RED;

        public BadgeDrawer(Badge badge) {
            this.badge = badge;

            if (((View) badge).getMinimumWidth() < minWidth) {
                ((View) badge).setMinimumWidth(minWidth);
            }

            if (((View) badge).getMeasuredHeight() < minHeight) {
                ((View) badge).setMinimumHeight(minHeight);
            }

            paint = new Paint();
            paint.setAntiAlias(true);
        }

        public void draw(Canvas canvas) {
            View target = (View) badge;
            if (target.getWidth() == 0 || target.getHeight() == 0) {
                return;
                //如果获取不到宽度和高度就不执行绘图流程
            }

            if (badge.getNumber() <= 0) {
                //如果小于0就不画
                return;
            }

            beforeDrawBackGround();

            int centerX = target.getWidth() - paddingRight - radius;
            int centerY = paddingTop + radius;

            canvas.drawCircle(centerX, centerY, radius, paint);

            beforeDrawText();

            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            canvas.drawText(
                    badge.getNumber() <= 99 ? String.valueOf(badge.getNumber()) : "99+"
                    , centerX, centerY - (fontMetrics.bottom + fontMetrics.top) / 2
                    , paint
            );
        }

        private void beforeDrawBackGround() {

            paint.setColor(backgroundColor);
            paint.setStyle(Paint.Style.FILL);

        }

        private void beforeDrawText() {

            paint.setColor(textColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStrokeWidth(ViewUtils.dpToPx(1));
            paint.setTextSize(textSize);

        }

        public BadgeDrawer setTextSize(int textSize, boolean inDp) {
            if (inDp) {
                textSize = dpToPx(textSize);
            }
            this.textSize = textSize;
            return this;
        }

        public BadgeDrawer setRadius(int radius, boolean inDp) {
            if (inDp) {
                radius = dpToPx(radius);
            }
            this.radius = radius;
            return this;
        }

        public BadgeDrawer setPaddingTop(int paddingTop, boolean inDp) {
            if (inDp) {
                paddingTop = dpToPx(paddingTop);
            }
            this.paddingTop = paddingTop;
            return this;
        }

        public BadgeDrawer setPaddingRight(int paddingRight, boolean inDp) {
            if (inDp) {
                paddingRight = dpToPx(paddingRight);
            }
            this.paddingRight = paddingRight;
            return this;
        }

        public BadgeDrawer setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public BadgeDrawer setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        private int dpToPx(int dp) {
            return ViewUtils.dpToPx(dp);
        }
    }

    /**
     *You may need this manager class if you are gonna refresh numbers by http request.
     */
    public static class BadgeManager extends Timer {

        private long delay = 100;
        private long period = 10000L;
        private Notifier notifier;
        private TimerTask task;
        private boolean enabled = false;

        public static BadgeManager create() {
            return new BadgeManager();
        }

        public BadgeManager delay(long delay) {
            this.delay = delay;
            return this;
        }

        public BadgeManager period(long period) {
            this.period = period;
            return this;
        }

        //You need a notifier to set numbers to registered badges.
        public BadgeManager notifier(Notifier notifier) {
            this.notifier = notifier;
            return this;
        }

        public BadgeManager start() {
            if (task == null) {
                task = new TimerTask() {
                    @Override
                    public void run() {
                        //Make your http request here.
                    }
                };
            }
            schedule(task, delay, period);
            enabled = true;
            return this;
        }

        /**
         * this method should be called on UI Thread.
         */
        public void onBadgeNumbersGet(List<BadgeNumber> badgeNumbers){
            if (notifier != null){
                notifier.notifyUpdate(badgeNumbers);
            }
        }

        public BadgeManager stop() {
            if (task != null){
                task.cancel();
                task = null;
            }
            enabled = false;
            return this;
        }

        public Notifier getNotifier() {
            return notifier;
        }
    }

    /**
     * This will tell all the badges that is registered it is time to update.
     * First you need to register your badges to a Notifier.
     */
    public static class Notifier {
        List<UpdateAble> notifyList = new ArrayList<>();
        private boolean enable = true;
        private List<BadgeNumber> cachedNumbers;

        public void notifyUpdate(List<BadgeNumber> badgeNumbers) {
            cachedNumbers = badgeNumbers;
            if (!enable) return;
            for (UpdateAble updateAble : notifyList) {
                if (updateAble instanceof Badge) {
                    BadgeNumber number = findNumberById(badgeNumbers, ((Badge) updateAble).getBadgeId());
                    if (number != null) {
                        updateAble.update(number.getNumber());
                    }
                } else {
                    updateAble.updateBadge(badgeNumbers);
                }
            }
        }

        public void register(UpdateAble updateAble) {
            if (updateAble != null && !notifyList.contains(updateAble)) {
                notifyList.add(updateAble);
                if (!(updateAble instanceof Badge)) {
                    updateAble.updateBadge(cachedNumbers);
                }
            }
        }

        public void unRegister(UpdateAble updateAble) {
            if (updateAble != null && notifyList.contains(updateAble)) {
                notifyList.remove(updateAble);
            }
        }

        @Nullable
        public BadgeNumber findNumberById(List<BadgeNumber> badgeNumbers, String id) {
            for (BadgeNumber number : badgeNumbers) {
                if (id.equals(number.getId())) {
                    return number;
                }
            }
            return null;
        }

        public void disable() {
            this.enable = false;
        }
    }

    public static class BadgeNumber {
        //Number tobe set to the badge.
        private int number;
        //Id of your certain badge.
        private String id;

        public BadgeNumber() {
        }

        public BadgeNumber(int number, String id) {
            this.number = number;
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
