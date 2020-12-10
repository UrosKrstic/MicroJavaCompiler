package rs.ac.bg.etf.pp1.util;

import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

public class Log4JUtils {

	private static Log4JUtils logs = new Log4JUtils();
	
	public static Log4JUtils instance() {
		return logs;
	}
	
	public URL findLoggerConfigFile() {
		return Thread.currentThread().getContextClassLoader().getResource("config/log4j.xml");
	}
	
	public void prepareLogFile(Logger root) {
		Appender appender = root.getAppender("file");
		
		if (!(appender instanceof FileAppender))
			return;
		FileAppender fAppender = (FileAppender)appender;
		
		String logFileName = fAppender.getFile();
        //logFileName = logFileName.substring(0, logFileName.lastIndexOf('.')) + "-test.log";
        String timeDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toString();
        timeDate = timeDate.replace(':', '-');
        String logFileRenamed = logFileName.substring(0, logFileName.lastIndexOf('.')) + timeDate + ".log";
		
		File logFile = new File(logFileName);
		File renamedFile = new File(logFileRenamed); 
        
        if (logFile.exists() && logFile.length() > 0) {
			if (!logFile.renameTo(renamedFile))
				System.err.println("Could not rename log file!");
		}
		
		fAppender.setFile(logFile.getAbsolutePath());
		fAppender.activateOptions();
	}
	
	
	
}
