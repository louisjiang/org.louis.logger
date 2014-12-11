package org.louis.logger;

public abstract class LoggerFactory {

	private static volatile LoggerFactory defaultFactory = null;

	private static LoggerFactory newDefaultFactory(String name) {
		LoggerFactory f;
		try {
			f = new Slf4JLoggerFactory(true);
			f.newInstance(name).info("Using SLF4J as the default logging framework");
		} catch (Throwable t1) {
			try {
				f = new Log4JLoggerFactory();
				f.newInstance(name).info("Using Log4J as the default logging framework");
			} catch (Throwable t2) {
				f = new JdkLoggerFactory();
				f.newInstance(name).info("Using java.util.logging as the default logging framework");
			}
		}
		return f;
	}

	public static LoggerFactory getDefaultFactory() {
		if (defaultFactory == null) {
			defaultFactory = newDefaultFactory(LoggerFactory.class.getName());
		}
		return defaultFactory;
	}

	public static void setDefaultFactory(LoggerFactory defaultFactory) {
		if (defaultFactory == null) {
			throw new NullPointerException("defaultFactory");
		}
		LoggerFactory.defaultFactory = defaultFactory;
	}

	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	public static Logger getLogger(String name) {
		return getDefaultFactory().newInstance(name);
	}

	protected abstract Logger newInstance(String name);
}
