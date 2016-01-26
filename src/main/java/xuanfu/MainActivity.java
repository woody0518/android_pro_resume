package xuanfu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.start).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShowFloatWindowService.showSuspend(MainActivity.this);
				
			}
		});
	}
	
	
	public void end(View v){
		ShowFloatWindowService.hideSuspend(this);
	}
}
