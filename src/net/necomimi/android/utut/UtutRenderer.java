package net.necomimi.android.utut;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class UtutRenderer /*extends View*/ implements Renderer {

	private Activity activity;
	private ImageView mainImage;
	private TextView calValue;
	
	public void init(Activity activity) {
		this.activity = activity;
        Display display = ((WindowManager)activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        this.mainImage = (ImageView)this.activity.findViewById(R.id.main_image);
        this.mainImage.setScaleType(ScaleType.FIT_CENTER);
        this.calValue = (TextView)this.activity.findViewById(R.id.cal_value);
        this.calValue.setPadding((int)(display.getWidth() * 0.62),
        		                 (int)(display.getHeight() * 0.07),
        		                 0,
        		                 0);
	}

	public void changeCalText(String text) throws RenderException {
        this.calValue.setText(text);
	}

	public void changeImage(int res) throws RenderException {
		this.mainImage.setImageResource(res);
	}

}
