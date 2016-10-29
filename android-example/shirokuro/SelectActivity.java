package game.example.shirokuro;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;



public class SelectActivity extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		SelectView select = new SelectView(this);
		setContentView(select);
	}
	
	//画面サイズ取得関数
	public DisplayMetrics ScreenSize(){
		WindowManager windowM = getWindowManager();
		Display display = windowM.getDefaultDisplay();
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		return displayMetrics;
	}
}

class SelectView extends View{
	public SelectView(Context beta){
		super(beta);
		setFocusable(true);
		Resources res = this.getContext().getResources();
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
	}
}