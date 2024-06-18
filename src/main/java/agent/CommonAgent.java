package agent;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class CommonAgent {
	static ClassPathXmlApplicationContext context = null; 
			
	abstract public void run();
	
	public static void loadContext() {
		context = new ClassPathXmlApplicationContext("springConfig.xml");
	}
	
	public static String[] getBeans() {
		return context.getBeanDefinitionNames();
	}
}
