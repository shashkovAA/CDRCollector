package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.*;
import java.net.Socket;

import controller.HomeBarController;
//import controller.mainController;
import objects.Paths;


public class PbxCollector implements Runnable
{

	private volatile boolean stopMe;

	private Socket myConnect = null;
	private File cdrFile;

	private BufferedReader bufferIn = null;
	
	private FileWriter out = null;
	
	private BufferedWriter bufferOut = null;

	private InputStreamReader in = null;

	private HomeBarController mMainController;

	public PbxCollector()
	{} //=== онструктор без параметров.

	public PbxCollector(Socket mySocket, File cCdrFile) //=== онструктор, принимает переменные извне и присваивает их значени€ переменным класса.
	{
		this.myConnect = mySocket;
		this.cdrFile = cCdrFile;
	}
	
	
//************************************************************************************************************
//===ќсновной метод потока
	
	@Override
	public void run()
	{
		stopMe = false;

		try 
		{
			bufferIn = new BufferedReader(new InputStreamReader(myConnect.getInputStream()));

			String getStr;

			while (stopMe == false)
			{
								
				if (bufferIn.ready())	//---buffer is not empty?

				{
					getStr = getStringBuffer(bufferIn);

					try 
					{
						out = new FileWriter(cdrFile, true);
						
						bufferOut = new BufferedWriter(out);
						
						bufferOut.write(getStr + Paths.lineSeparator);

						bufferOut.flush();
						
						bufferOut.close();
					
					}
					catch (IOException except)
					{
						
						Debug.log.error(except.getMessage());
					}
									
					mMainController.getRecord(getStr);

				}
				
				try
				{
					Thread.currentThread().sleep(2000);	//—топим поток на 2 секунды
				}
				catch (InterruptedException except)
				{
					
					Debug.log.error(except.getMessage());
				}
			}

			bufferIn.close();	//===¬р€д ли когда выполнитс€, но напишу.
		}

		catch (java.net.SocketException e)
		{
			Debug.log.error(e.getMessage());
			try
			{
				bufferIn.close();
			}
			catch (IOException except)
			{
				Debug.log.error(except.getMessage());
			}

		}
		catch (IOException e)
		{
			Debug.log.error(e.getMessage());
		}

		finally
		{

			try
			{
				bufferIn.close();
				myConnect.close();
				if (myConnect.isClosed()) Debug.log.info("Client connection is closed.");
				mMainController.numSockets = mMainController.numSockets + 1;
				Debug.log.info("numSockets = " + mMainController.numSockets);

			}
			catch (IOException e)
			{
				Debug.log.error(e.getMessage());
			}
		}

	}
//************************************************************************************************************
//===ћетод дл€ останова текущего потока 
	
	public void stopMe()
	{
		stopMe = true;
										
	}
//************************************************************************************************************
//===ћетод инициализации дл€ св€зи с основным контроллером	

	public void init(HomeBarController mMainController)
	{
		
		this.mMainController = mMainController;
				
	}
	
	
	private String getStringBuffer(BufferedReader buffer)
	{
		
		char[] inputChars = new char[1024];
		int numChars = 0;
		String str = null;
		
		try
		{
			numChars = buffer.read(inputChars);
			str = String.copyValueOf(inputChars);

		}
		catch (IOException except)
		{
			except.printStackTrace();
		}
				          
		return str;
	}
			
	
}
