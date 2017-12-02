package club.itsociety.mmuone;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Timothy on 09/11/2017.
 * Class to override TextInputLayout to fix its bug
 * When error set and then hidden, the space will not be hidden
 * as the used View.INVISIBLE instead of View.GONE
 * Remember to use this in XML file as well
 */

//	Extending TextInputLayout
public class MyTextInputLayout extends TextInputLayout
{
	public MyTextInputLayout(Context context) {
		super(context);
	}

	public MyTextInputLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	//	Overriding the method with bug
	@Override
	public void setError(@Nullable CharSequence error) {
		super.setError(error);

		View layout = getChildAt(1);
		if (layout != null) {
			if (error != null && !"".equals(error.toString().trim())) {
				layout.setVisibility(VISIBLE);
			} else {
				layout.setVisibility(GONE);
			}
		}
	}
}
