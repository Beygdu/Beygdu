package is.arnastofnun.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Property;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

/**
 * Created by jfjclarke on 21.3.2015.
 *
 * A LongClickListener which with animation which highlights a TextView object
 * by setting background color to yellow and then reverting setting to Transparent.
 *
 */
public class XLongClickListener implements View.OnLongClickListener {

    private Context context;
    private TextView cell;


    /**
     *
     * @param context the context where used.
     * @param textView the Textview.
     */
    public XLongClickListener(Context context, TextView textView) {
        super();
        this.context = context;
        this.cell = textView;
    }

    @Override
    public boolean onLongClick(View v) {
        cell.setBackgroundColor(Color.TRANSPARENT);

        final Property<TextView, Integer> property = new Property<TextView, Integer>(int.class, "BackgroundColor") {
            @Override
            public Integer get(TextView object) {
                ColorDrawable cd = (ColorDrawable) object.getBackground();
                int colorCode = cd.getColor();
                return colorCode;
            }

            @Override
            public void set(TextView object, Integer value) {
                object.setBackgroundColor(value);
            }
        };

        final ObjectAnimator animator = ObjectAnimator.ofInt(cell, property, Color.parseColor("#f0ad4e"));
        animator.setDuration(500L);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setInterpolator(new DecelerateInterpolator(2));
        animator.start();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(cell.getText(), cell.getText());
        clipboard.setPrimaryClip(clip);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        return false;
    }
}
