package emad.youtube.Tools;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RegularFont extends android.support.v7.widget.AppCompatTextView {
    public RegularFont(Context context) {
        super(context);
    }

    public RegularFont(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (Localization.getLocalization(context) == Localization.ARABIC_VALUE) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "Amurg-Regular.otf"));
//        } else {
//            setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/english_light.TTF"));
//        }

    }
}
