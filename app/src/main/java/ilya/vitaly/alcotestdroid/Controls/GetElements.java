package ilya.vitaly.alcotestdroid.Controls;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vital on 28/10/2017.
 */

public class GetElements {

    public static void getAllButtons(ViewGroup v, List<Button> buttonsInActivity) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);
            if (child instanceof ViewGroup)
                getAllButtons((ViewGroup) child, buttonsInActivity);
            else if (child instanceof Button)
                buttonsInActivity.add((Button) child);
        }
    }
}
