package emad.youtube.Tools;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class AmurorFont extends android.support.v7.widget.AppCompatButton {
    public AmurorFont(Context context) {
        super(context);
    }

    public AmurorFont(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (Localization.getLocalization(context) == Localization.ARABIC_VALUE) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "AXTON.otf"));
//        } else {
//            setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/english_light.TTF"));
//        }

    }
}