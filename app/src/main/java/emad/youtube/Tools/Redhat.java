package emad.youtube.Tools;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class Redhat extends android.support.v7.widget.AppCompatTextView {
    public Redhat(Context context) {
        super(context);
    }

    public Redhat(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (Localization.getLocalization(context) == Localization.ARABIC_VALUE) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "redhat.ttf"));
//        } else {
//            setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/english_light.TTF"));
//        }

    }
}
