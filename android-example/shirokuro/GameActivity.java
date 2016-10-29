package game.example.shirokuro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;



public class GameActivity extends Activity{
	public int mapcnt;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		BoxView Map = new BoxView(this);
		//画面サイズ読み取り
		Map.Screenwidth = ScreenSize().widthPixels;
		Map.Screenhight = ScreenSize().heightPixels;
		//マップ呼び出し
		String mapname = "test3";
		Map.Map = Getmap(mapname);
		Map.Maplength = mapcnt;
		setContentView(Map);
	}
	
	//画面サイズ取得関数
	public DisplayMetrics ScreenSize(){
		WindowManager windowM = getWindowManager();
		Display display = windowM.getDefaultDisplay();
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		return displayMetrics;
	}
	
	//マップデータ取得関数
	public int[] Getmap(String mapname){
		int cnt = 0;
		int Wordbuffer;
		AssetManager assetM = getResources().getAssets();
		int[] mapdata = new int[120];
		InputStream is;
		try {
			is = assetM.open("mapdata/"+mapname+".txt");
			BufferedReader reader= new BufferedReader(new InputStreamReader(is,"UTF-8"));
			while( (Wordbuffer = reader.read()) != -1 ){
				//文字コード調整
				int code = Wordbuffer - 48;
				//0,1,2,3,4,5以外排除
				if(code > 9 || code < 0)continue;
				mapdata[cnt] = code;
				cnt++;
				}
			reader.close();
			mapcnt = cnt;
		} catch (IOException e) {
		  e.printStackTrace();
		}
		return mapdata;
	}
}

class BoxView extends View{
	//画面サイズ
	public int Screenwidth;
	public int Screenhight;
	//タッチされた場所
	private int placeX;
	private int placeY;
	//マスの大きさ
	public int BoxSize;
	//マップデータ
	public int[] Map;
	public int Maplength;
	private int[][] BMap = new int[120][120];
	private int BMapcnt = 0;
	private int boxcnt;
	
	//画像データ
	private Bitmap mBlack;
	private Bitmap mWhite;
	private Bitmap mGray;
	private Bitmap mCanset;
	
	//フラグ管理
	private final int FARST = 1;
	private final int TURN = 2;
	private final int BACK = 3;
	private int flag = FARST;
	private int colorflag;
	
	//デバック用
	//private int test = 0;
	
	public BoxView(Context beta){
		super(beta);
		setFocusable(true);
		Resources res = this.getContext().getResources();
		//画像呼び出し
		mBlack = BitmapFactory.decodeResource(res, R.drawable.black);
		mWhite = BitmapFactory.decodeResource(res, R.drawable.white);
		mGray = BitmapFactory.decodeResource(res, R.drawable.gray);
		mCanset = BitmapFactory.decodeResource(res, R.drawable.canset);
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//マスサイズ決定
		BoxSize = Screenwidth/10;
		
		//画像リサイズ
		Bitmap ReBlack = Chenge(mBlack);
		Bitmap ReWhite = Chenge(mWhite);
		Bitmap ReGray = Chenge(mGray);
		Bitmap ReCanset = Chenge(mCanset);
		
		switch(flag){
		//初期化
		case FARST:
			colorflag = Map[Maplength-2];
			break;
		//マップデータ変更処理	
		case TURN:
			//タッチ位置検索
			 double boxX,boxY;
			boxX = Math.ceil((placeX) / (BoxSize) + 1.0);
			boxY = Math.ceil((placeY) / (BoxSize) + 1.0);
			boxcnt = (int)(boxX + (boxY - 1) * 10);
			if(Map[boxcnt - 1] == 3 && colorflag == 4 && Cansetplace(boxcnt-1)){
				for(int i=0;i < Map.length;i++){
					BMap[BMapcnt][i] = Map[i];
				}
				BMapcnt++;
				Reverse(boxcnt - 1);
				colorflag = 5;
			}
			if(Map[boxcnt - 1] == 3 && colorflag == 5 && Cansetplace(boxcnt-1)){
				for(int i=0;i < Map.length;i++){
					BMap[BMapcnt][i] = Map[i];
				}
				BMapcnt++;
				Reverse(boxcnt - 1);
				colorflag = 4;
			}
			break;
		case BACK:
			BMapcnt--;
			for(int i=0;i < Map.length;i++){
				Map[i] = BMap[BMapcnt][i];
			}
			if(colorflag == 5){
				colorflag = 4;
			}else{
				colorflag = 5;
			}
			break;
		}
		//背景色設定
		canvas.drawRGB(128,128,128);
		
		//デバック用マップデータ表示
		/*
		Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize( 35);
		textPaint.setColor( Color.WHITE);
		canvas.drawText(boxcnt+"",0,50,textPaint);
		canvas.drawText(flag+"",0,100,textPaint);
		//canvas.drawText(test+"",0,100,textPaint);
		*/
		//最初の色の決定
		if(colorflag == 4){
			canvas.drawBitmap(ReBlack,0,0,null);
		}
		if(colorflag == 5){
			canvas.drawBitmap(ReWhite,0,0,null);
		}
		//マップ描画
		for(int maxMap = Map.length,cntMap = 0
				,cntx = 0,cnty = 0;cntMap < maxMap;cnty++){
			for(cntx = 0;cntx < 10 && cntMap < maxMap;cntx++){
				if(Map[cntMap] == 1){
					canvas.drawBitmap(ReBlack,BoxSize*(cntx),BoxSize*(cnty),null);
				}
				if(Map[cntMap] == 2){
					canvas.drawBitmap(ReWhite,BoxSize*(cntx),BoxSize*(cnty),null);
				}
				if(Map[cntMap] == 3){
					if(Cansetplace(cntMap)){
						canvas.drawBitmap(ReCanset,BoxSize*(cntx),BoxSize*(cnty),null);
					}else{
						canvas.drawBitmap(ReGray,BoxSize*(cntx),BoxSize*(cnty),null);
					}
				}
				cntMap++;
			}
		}
		canvas.drawBitmap(ReCanset,BoxSize,BoxSize*9,null);
	}
	
	//タッチ入力処理
	public boolean onTouchEvent(MotionEvent me){
		//タッチされた時
		if(me.getAction() == MotionEvent.ACTION_DOWN && me.getDownTime() > 50){
			//タッチ受付範囲指定
			if(me.getX() >= BoxSize && me.getX() <= BoxSize * 9
					&& me.getY() >= BoxSize && me.getY() <= BoxSize * 10){
				placeX = (int)me.getX();
				placeY = (int)me.getY();
				flag = TURN;
				invalidate();
			}
			if(me.getX() >= BoxSize && me.getX() <= BoxSize * 2
					&& me.getY() >= BoxSize * 9&& me.getY() <= BoxSize * 10 && BMapcnt > 0){
				flag = BACK;
				invalidate();
			}
		}
		return true;
	}
	
	//置けるマスの判断
	public boolean Cansetplace(int cnt){
		boolean pass = false;
		if(Map[cnt] == 3){
			//マスの周りのデータ
			int direct[] = {-11,-10,-9,-1,1,9,10,11};
			int box[] = new int[8];
			for(int num = 0;num < 8;num++){
				box[num] = Map[cnt + direct[num]];
			}
			int flag[] = {1,1,1,1,1,1,1,1};
			//次の色が黒の時
			if(colorflag == 4){
				for(int bcnt=0;bcnt < 8;bcnt++){
					if(box[bcnt] != 2){
						flag[bcnt] = 0;
					}
				}
				for(int Direct = 0;Direct < 8;Direct++){
					for(int Distance = 0;Distance < 8;Distance++){
						if(flag[Direct] == 1){
							int para = cnt + direct[Direct] * (Distance + 2);
							if(Map[para] == 0 || Map[para] == 3){
								flag[Direct] = 0;
							}
							if(Map[para] == 1){
								pass = true;
								break;
							}
						}
					}
				}
			}
			//次の色が白の時
			if(colorflag == 5){
				for(int wcnt=0;wcnt < 8;wcnt++){
					if(box[wcnt] != 1){
						flag[wcnt] = 0;
					}
				}
				for(int Direct = 0;Direct < 8;Direct++){
					for(int Distance = 0;Distance < 8;Distance++){
						if(flag[Direct] == 1){
							int para = cnt + direct[Direct] * (Distance + 2);
							if(Map[para] == 0 || Map[para] == 3){
								flag[Direct] = 0;
							}
							if(Map[para] == 2){
								pass = true;
								break;
							}
						}
					}
				}
			}
		}
		return pass;
	}
	
	//裏返し関数
	public void Reverse(int cnt){
		if(Map[cnt] == 3){
			//マスの周りのデータ
			int direct[] = {-11,-10,-9,-1,1,9,10,11};
			int box[] = new int[8];
			for(int num = 0;num < 8;num++){
				box[num] = Map[cnt + direct[num]];
			}
			int flag[] = {1,1,1,1,1,1,1,1};
			//次の色が黒の時
			if(colorflag == 4){
				for(int bcnt=0;bcnt < 8;bcnt++){
					if(box[bcnt] != 2){
						flag[bcnt] = 0;
					}
				}
				for(int Direct = 0;Direct < 8;Direct++){
					for(int Distance = 0;Distance < 8;Distance++){
						if(flag[Direct] == 1){
							int para = cnt + direct[Direct] * (Distance + 2);
							if(Map[para] == 0 || Map[para] == 3){
								flag[Direct] = 0;
							}
							if(Map[para] == 1){
								for(int alpha = 0;alpha < Distance + 2;alpha++){
									int beta = cnt + direct[Direct] * alpha;
									Map[beta] = 1;
								}
							}
						}
					}
				}
			}
			//次の色が白の時
			if(colorflag == 5){
				for(int wcnt=0;wcnt < 8;wcnt++){
					if(box[wcnt] != 1){
						flag[wcnt] = 0;
					}
				}
				for(int Direct = 0;Direct < 8;Direct++){
					for(int Distance = 0;Distance < 8;Distance++){
						if(flag[Direct] == 1){
							int para = cnt + direct[Direct] * (Distance + 2);
							if(Map[para] == 0 || Map[para] == 3){
								flag[Direct] = 0;
							}
							if(Map[para] == 2){
								for(int alpha = 0;alpha < Distance + 2;alpha++){
									int beta = cnt + direct[Direct] * alpha;
									Map[beta] = 2;
								}
							}
						}
					}
				}
			}
		}
	}
	
	//画像リサイズ関数
	public Bitmap Chenge(Bitmap gazou){
		Bitmap Cgazou = Bitmap.createScaledBitmap(gazou,BoxSize,BoxSize,false);
		return Cgazou;
	}
}