package mediaservice.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.SystemUtils;

public class ConfigPropertiesUtil implements ServletContextListener{
	private static Properties prop=new Properties();
	private static String fileName="/opt/dev/apps/mon/configs/vt.properties";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			if(SystemUtils.IS_OS_WINDOWS) {
				fileName="D:\\DevApps\\_Workspace\\Java\\Eclipse\\monitor\\.mydata\\mon-service\\config.properties";
			}
			
			System.out.println("Initializing properties config......");
			
			InputStream inputStream= new FileInputStream(fileName);
			prop.load(inputStream);
			inputStream.close();
			
			System.out.println("Initialize properties config success!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	public static String getProperty(String key){
		return prop.getProperty(key);
	}
}
