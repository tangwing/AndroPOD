package org.andropod;

import java.io.UnsupportedEncodingException;

import org.bwa.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class HelloAndropodActivity extends Activity {
	
	
    /** 
     * Called when the activity is first created. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initComponents();
        
        //Set listeners:
        
        //The radio group for setting the speed
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            	public void onCheckedChanged(RadioGroup group, int checkedId) {  
                    if (checkedId == R.id.radio_vite) {  
                        vitesse = 1500;
                    } else if (checkedId == R.id.radio_moyen) {  
                    	vitesse = 1000;
                    } else if (checkedId == R.id.radio_lent) {  
                    	vitesse = 500;
                     } 
                }  
          });
     
//        buttonReset.setOnClickListener(new OnClickListener()
//	        {
//	        	public void onClick(View v)
//	        	{
//	        		tv0.setText("0");
//	        		tv1.setText("0");
//	        		tv2.setText("0");
//	        		tv3.setText("0");
//	        		tv4.setText("0");
//	        	}
//	        });
        //Groups of motor controller
        button0Left.setOnClickListener(new OnButtonClickedListener());
        button0Left.setOnTouchListener(new OnButtonTouchedListener());
        button0Left.setOnLongClickListener(new OnButtonLongClickListener());
        button0Right.setOnClickListener(new OnButtonClickedListener());
        button0Right.setOnTouchListener(new OnButtonTouchedListener());
        button0Right.setOnLongClickListener(new OnButtonLongClickListener());
        
        button1Left.setOnClickListener(new OnButtonClickedListener());
        button1Left.setOnTouchListener(new OnButtonTouchedListener());
        button1Left.setOnLongClickListener(new OnButtonLongClickListener());
        button1Right.setOnClickListener(new OnButtonClickedListener());
        button1Right.setOnTouchListener(new OnButtonTouchedListener());
        button1Right.setOnLongClickListener(new OnButtonLongClickListener());
        
        button2Left.setOnClickListener(new OnButtonClickedListener());
        button2Left.setOnTouchListener(new OnButtonTouchedListener());
        button2Left.setOnLongClickListener(new OnButtonLongClickListener());
        button2Right.setOnClickListener(new OnButtonClickedListener());
        button2Right.setOnTouchListener(new OnButtonTouchedListener());
        button2Right.setOnLongClickListener(new OnButtonLongClickListener());
        
        button3Left.setOnClickListener(new OnButtonClickedListener());
        button3Left.setOnTouchListener(new OnButtonTouchedListener());
        button3Left.setOnLongClickListener(new OnButtonLongClickListener());
        button3Right.setOnClickListener(new OnButtonClickedListener());
        button3Right.setOnTouchListener(new OnButtonTouchedListener());
        button3Right.setOnLongClickListener(new OnButtonLongClickListener());
        

        
        
          

        //Set the on click listener for the transmit button
        buttonTransmit.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v) {
				try {
					andropod.write((editInput.getText().toString().concat("\r")).getBytes("us-ascii"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        
        
        //Set up the reader thread that receives the data
        threadReader = new Thread(new Runnable() {
			public void run() {
				boolean wasConnected = false;
				try
				{
					while(andropod.isOpen()) //Loop as long as the interface is open
					{
						boolean isConnected = andropod.isConnected();

						if(isConnected != wasConnected) //Check connection status
						{
							appendText(isConnected?"-- Device connected --\r\n":"-- Device disconnected --\r\n");
							wasConnected = isConnected;
						}	
						
						if(isConnected)	//Receive data if connected
						{
							byte []buffer = new byte[1];
							if(andropod.read(buffer) > 0) 		//if data has been read
							{
								appendText(new String(buffer));	//append read data
							}
						}
						else			//Not connected
						{
							Thread.sleep(100); //Sleep some time to minimize CPU usage
						}
					}
				}catch (Exception e){}
			}
		});
        threadReader.start();
    }
    
    /**
     * Initialize components and set tags of buttons
     */
    private void initComponents()
    {
    	
        //The radio group for setting the speed
        group = (RadioGroup) findViewById(R.id.group);
         
       // buttonReset=(Button)findViewById(R.id.buttonreset);
        //Groups of motor controller
    	button0Left = (Button)findViewById(R.id.button0LeftArrow1);
        button0Right = (Button)findViewById(R.id.button0RightArrow1);
        tv0 = (TextView)findViewById(R.id.tvAngle0);
        button0Left.setTag(R.id.relativeTextView, tv0);//The first tag is the relative TextView
        button0Left.setTag(R.id.btnDirection, "Left");//The second tag indicate the fonction of this button
        button0Left.setTag(R.id.motorId,new Integer(0));
        button0Right.setTag(R.id.relativeTextView, tv0);
        button0Right.setTag(R.id.btnDirection, "Right");
        button0Right.setTag(R.id.motorId,new Integer(0));
        
        
        button1Right = (Button)findViewById(R.id.button1RightArrow1);
        button1Left = (Button)findViewById(R.id.button1LeftArrow1);
        tv1 = (TextView)findViewById(R.id.tvAngle1);
        button1Left.setTag(R.id.relativeTextView, tv1);
        button1Left.setTag(R.id.btnDirection, "Left");
        button1Left.setTag(R.id.motorId,new Integer(1));
        button1Right.setTag(R.id.relativeTextView, tv1);
        button1Right.setTag(R.id.btnDirection, "Right");
        button1Right.setTag(R.id.motorId,new Integer(1));
        
        button2Right = (Button)findViewById(R.id.button2RightArrow1);
        button2Left = (Button)findViewById(R.id.button2LeftArrow1);
        tv2 = (TextView)findViewById(R.id.tvAngle2);
        button2Left.setTag(R.id.relativeTextView, tv2);
        button2Left.setTag(R.id.btnDirection, "Left");
        button2Left.setTag(R.id.motorId,new Integer(2));
        button2Right.setTag(R.id.relativeTextView, tv2);
        button2Right.setTag(R.id.btnDirection, "Right");
        button2Right.setTag(R.id.motorId,new Integer(2));
        
        button3Right = (Button)findViewById(R.id.button3RightArrow1);
        button3Left = (Button)findViewById(R.id.button3LeftArrow1);
        tv3 = (TextView)findViewById(R.id.tvAngle3);
        button3Left.setTag(R.id.relativeTextView, tv3);
        button3Left.setTag(R.id.btnDirection, "Left");
        button3Left.setTag(R.id.motorId,new Integer(3));
        button3Right.setTag(R.id.relativeTextView, tv3);
        button3Right.setTag(R.id.btnDirection, "Right");
        button3Right.setTag(R.id.motorId,new Integer(3));
        
        
        editInput = (EditText)findViewById(R.id.editInput);
        buttonTransmit = (Button)findViewById(R.id.buttonTransmit); 
        textOutput = (TextView)findViewById(R.id.textOutput);
        scrollOutput = (ScrollView)findViewById(R.id.scrollOutput); 
    }
    

/**
 * Generate command according to the motor number and position in angle form.
 * */
public byte[] getOrder(int motorNumber,int angle,int vitesse){
	int position = 1500+1000*angle/90;
	String order = "#"+motorNumber+" P"+position+" S"+vitesse+"\r";
	return order.getBytes();
}

private class OnButtonClickedListener implements OnClickListener
{
	public void onClick(View v) 
	{
		Button btn=(Button)v;
		TextView tv=(TextView) btn.getTag(R.id.relativeTextView);
		String direction=(String)btn.getTag(R.id.btnDirection);
		int motorNumber=((Integer)btn.getTag(R.id.motorId)).intValue();
		
		int angle=Integer.parseInt(tv.getText().toString());
		if(direction.equals("Left"))
			angle-=ANGLE_DELTA;
		else if(direction.equals("Right"))
			angle+=ANGLE_DELTA;
		
		if((angle <= 90) && (angle >= -90))
		{
			tv.setText(String.valueOf(angle));
			byte [] buffer = getOrder(motorNumber,angle,vitesse);
			andropod.write(buffer);
		}
	}
}

/**
 * this Listener is designed to finish the LongPressed event
 * @author shang
 *
 */
private class OnButtonLongClickListener implements OnLongClickListener
{
	public boolean onLongClick(View v) 
	{
		//System.out.println("we are here"+stop);
		Button btn=(Button)v;
		TextView tv=(TextView) btn.getTag(R.id.relativeTextView);
		String direction=(String)btn.getTag(R.id.btnDirection);
		int motorNumber=((Integer)btn.getTag(R.id.motorId)).intValue();
		int delta;
		if(direction.equals("Left"))
			delta=-ANGLE_DELTA;
		else
			delta=ANGLE_DELTA;
		isLongClicked=true;
		mHandler.post(new CounterUpdater(tv,delta,motorNumber));
		return true;
	}
}

private class CounterUpdater implements Runnable
{
	private TextView tv=null;
	private int delta;
	private int motorNumber;
	public CounterUpdater(TextView tv, int delta, int motorNumber)
	{
		this.tv=tv;
		this.delta=delta;
		this.motorNumber=motorNumber;
	}
	public void run() 
	{
		if(stop==false)
		{
			int angle=Integer.parseInt(tv.getText().toString())+delta;
			if((angle <= 90) && (angle >= -90))
			{
				tv.setText(String.valueOf(angle));
				byte [] buffer = getOrder(motorNumber,angle,vitesse);
				andropod.write(buffer);
			}
			mHandler.postDelayed(this,200);
		}
		else
		{
			stop=false;
			isLongClicked=false;
		}
	}
	
}

/**
 * this Listener is designed to finish the LongPressed event
 * @author shang
 *
 */
private class OnButtonTouchedListener implements OnTouchListener
{
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			if(isLongClicked==true)
			{
				stop=true;
			}
		}
		return false;
	}
}




//*************************************************************************
    /**
     * Overwrite the default onResume to resume the andropod connection
     */
    @Override
    public void onResume()
    {
    	super.onResume();		//Resume activity
    	andropod.onResume();	//Resume andropod 
    }
    
    /**
     * Overwrite the default onPause to pause the andropod connection
     */
    @Override
    public void onPause()
    {
    	super.onPause();		//Pause activity
    	andropod.onPause();		//Pause andropod
    }
    
    /**
     * Append text and scroll to the botton on the output field
     * thread safe
     * @param text	New text to be appended to the textView
     */
    public static void appendText(String text)
    {
    	//final String newText = text;
    	
		handlerGui.post(new Runnable() {									
			public void run() {
//				textOutput.append(newText);	
//				scrollOutput.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
    }
    
    
	private AndropodConnection 	andropod = new AndropodConnection();
	private static Handler handlerGui = new Handler();
	private Button buttonTransmit = null;
	private EditText editInput = null;
	private static TextView textOutput 		= null;
	private static ScrollView scrollOutput	= null;
	private Thread	threadReader 	= null;
	
	private TextView tv0 = null;
	private TextView tv1 = null;
	private TextView tv2 = null;
	private TextView tv3 = null;
	private TextView tv4 = null;
	
	//private Button buttonReset=null;
	private Button button0Left = null;
	private Button button0Right = null;
	private Button button1Left = null;
	private Button button1Right = null;
	private Button button2Left = null;
	private Button button2Right = null;
	private Button button3Left = null;
	private Button button3Right = null;
	private Button button4Left = null;
	private Button button4Right = null;
	private RadioGroup group = null;
	
	private Handler mHandler = new Handler();
	//private RadioButton radioButton = null;
	private int vitesse=1000;
	public static boolean stop=false;
	public static boolean isLongClicked=false;
	final private int ANGLE_DELTA=6;
	
}