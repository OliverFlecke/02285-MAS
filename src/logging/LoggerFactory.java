package logging;

import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerFactory 
{
	private static Logger logger = null;
	
	public static Logger getLogger(String name)
	{		
		if (logger == null)
		{			
			LogManager.getLogManager().reset();
		}
						
		logger = Logger.getLogger(name);
		
		logger.setUseParentHandlers(false);
		
		Handler handler = new LoggingHandler();
		
		logger.addHandler(handler);
	
		return logger;
	}
}
