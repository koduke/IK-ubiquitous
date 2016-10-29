package game.example.shirokuro;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.content.Intent;
 
 
public class TitleActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Titleバー非表示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TitleView Title = new TitleView(this);
		Title.Screenwidth = ScreenSize().widthPixels;
		Title.Screenhight = ScreenSize().heightPixels;
		setContentView(Title);
	}
	
	//画面サイズ取得関数
	public DisplayMetrics ScreenSize(){
		WindowManager windowM = getWindowManager();
		Display display = windowM.getDefaultDisplay();
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		return displayMetrics;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		//�^�b�`�������Ɏ��s
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
		//GameActivity
			Intent triger = new Intent(this,GameActivity.class);
		//MainActivity
			//Intent triger = new Intent(this,MainActivity.class);
			startActivity(triger);
			this.finish();
		}
		return true;
	}
}

//描画用関数
class TitleView extends View{
	//画面サイズデータ
	public int Screenwidth;
	public int Screenhight;
	//画像データ
	private Bitmap mTitle;
	private Bitmap mStart;
	
	public TitleView(Context c){
		super(c);
		setFocusable(true);
		// Resource呼び出し
		Resources res = this.getContext().getResources();
		//タイトル画像(res/drawable/sirokurotitle.png)
		mTitle = BitmapFactory.decodeResource(res, R.drawable.sirokurotitle);
		//スタート画像(res/drawable/start.png)
		mStart = BitmapFactory.decodeResource(res, R.drawable.start);
	}
	
	//タイトル描画
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int Twidth,Thight,Swidth,Shight;
		//タイトルの表示位置
		Twidth = Screenwidth/2 - mTitle.getWidth()/2;
		Thight = Screenhight/2 - mTitle.getHeight();
		//スタートの表示位置
		Swidth = Screenwidth/2 - mStart.getWidth()/2;
		Shight = Screenhight - mStart.getHeight()*2;
		//背景色設定
		canvas.drawRGB(128,128,128);
		//画像表示
		canvas.drawBitmap(mTitle, Twidth, Thight, null);
		canvas.drawBitmap(mStart, Swidth, Shight, null);
	}
}