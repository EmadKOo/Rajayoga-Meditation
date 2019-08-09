package emad.youtube.Tools;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
public class MontsFont extends android.support.v7.widget.AppCompatButton {
    public MontsFont(Context context) {
        super(context);
    }

    public MontsFont(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (Localization.getLocalization(context) == Localization.ARABIC_VALUE) {
                 setTypeface(Typeface.createFromAsset(context.getAssets(), "monts.ttf"));
//        } else {
//            setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/english_light.TTF"));
//        }

    }
}
