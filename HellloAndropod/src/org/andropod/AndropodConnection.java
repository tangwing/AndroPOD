/**
 * Connection class to handle connections with Andropod Interfaces
 * Make sure you overwrite the onPause and onResume events
 * in your main activity and call the according AndropodConnection
 * functions to ensure proper opening and closing of the connection
 * 
 * @author 	Bernhard Wörndl-Aichriedler <bwa@xdevelop.at>
 * @data	11.2011
 */
package org.andropod;
import java.io.*;
import java.net.*;
import android.util.Log;

public class AndropodConnection 
extends Thread
{
	private ServerSocket 			AndropodSocket 		= null;
	private Socket					AndropodInterface 	= null;
	private BufferedInputStream		AndropodReader 		= null;
	private BufferedOutputStream	AndropodWriter 		= null;

	/**
	 * Constructor 
	 */
	public AndropodConnection()
	{
		//this.onResume();
	}

	public void run()
	{
		while(true)
		{
			while(this.isOpen())
			{
				try
				{
					//Use only latest connection
					this.AndropodInterface = this.AndropodSocket.accept();
					this.AndropodReader = new BufferedInputStream(this.AndropodInterface.getInputStream());
					this.AndropodWriter = new BufferedOutputStream(this.AndropodInterface.getOutputStream());
				} catch(Exception e){}
			}
			try
			{
				this.AndropodReader.close();
				this.AndropodWriter.close();
				this.AndropodInterface.close();				
			} catch(Exception e){}
			
			synchronized (this) {
				try
				{
					this.wait();
				} catch(Exception e){Log.e("info", e.toString());}
			}
		}		
	}
	
	/**
	 * Suspends the Andropod Interface
	 */
	public void onPause()
	{
		try
		{
			this.AndropodSocket.close();
		} catch(Exception e){}
	}
	
	/**
	 * Resumes the Andropod Interface from suspend
	 */
	public void onResume()
	{
		if(!this.isOpen())
		{
			try
			{
				this.AndropodSocket = new ServerSocket(1337);
				
				if(this.getState() == Thread.State.NEW)
				{
					this.start();
				}
				else
				{
					synchronized (this) {
						this.notify();
					}	
				}
				
			} catch(Exception e){Log.e("info", e.toString());}
		}
	}

	/**
	 * Checks if the AndropodInterface is connected
	 * @return Interface is connected?
	 */
	public boolean isConnected()
	{		
		if(this.AndropodInterface != null && !this.AndropodInterface.isClosed())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the port to the Andropod Interface is open
	 * @return Port is open?
	 */
	public boolean isOpen()
	{
		if(this.AndropodSocket != null && !this.AndropodSocket.isClosed())
		{
			//Log.d("info", "Server is open");
			return true;
		}
		return false;		
	}
	
	
	
	/**
	 * Transmit a data to the AndrodpodInterface, autoflushing
	 * @param	onByte	One byte to transmit
	 */
	public void write(int oneByte)
	{
		byte buffer[] = {(byte)oneByte};
		this.write(buffer, 0, buffer.length);
	}
	
	/**
	 * Transmit a data to the AndrodpodInterface, autoflushing
	 * @param	buffer	Buffer to write to the interface, full length of the array will be transmitted
	 */
	public void write(byte []buffer)
	{
		this.write(buffer, 0, buffer.length);
		
		//String tmp=new String(buffer);
//		System.out.println(buffer.length+tmp);
//		for(int i=0;i<buffer.length;i++)
//		{
//			System.out.print(Integer.toHexString(buffer[i])+" ");
//		}
//		
//		System.out.println(tmp);
		//HelloAndropodActivity.appendText(tmp);
	}
	
	
	/**
	 * Transmit a data to the AndrodpodInterface, autoflushing
	 * @param	buffer	Buffer to write to the interface
	 * @param	offset	Offset to start writing
	 * @param	length	Length to write
	 */
	public void write(byte []buffer, int offset, int length)
	{
		if(this.isConnected())
		{
			try
			{
				this.AndropodWriter.write(buffer, offset, length);		
				this.AndropodWriter.flush();
			}
			catch(Exception e){reset();}
		}
	}
	
	
	/**
	 * Receive data from the Andropod Interface
	 * @param	buffer	Buffer to write to (the entire array will be filled)
	 * @return Received packet
	 */
	public int read(byte []buffer)
	{
		return this.read(buffer, 0, buffer.length);
	}
	
	
	/**
	 * Receive data from the Andropod Interface
	 * @param	buffer	Buffer to write to
	 * @param	offset	offset to start reading
	 * @param	length	Length to read
	 * @return Received packet
	 */
	public int read(byte []buffer, int offset, int length)
	{
		int read = -1;
		
		if(this.isConnected())
		{		
			try
			{
				read = this.AndropodReader.read(buffer, offset, length);
				
				if(read < 0){reset();}
			}
			catch(Exception e) {reset();}		
		}
		return read;
	}
	
	/**
	 * Reset - close the current connection
	 */
	private void reset()
	{
		try
		{
			this.AndropodInterface.close();
		} catch(Exception e) {}
		this.AndropodInterface = null;
		this.AndropodReader = null;
		this.AndropodWriter = null;
		
		Log.d("info", "Connection closed!");	
	}
	
}
