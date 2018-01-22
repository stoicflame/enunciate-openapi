package dk.jyskebank.tools.enunciate.modules.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webcohesion.enunciate.EnunciateLogger;

public class OutputLogger implements EnunciateLogger {
	private static final Logger logger = LoggerFactory.getLogger(OutputLogger.class);

	@Override
	public void debug(String message, Object... formatArgs) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format(message, formatArgs));
		}
	}

	@Override
	public void info(String message, Object... formatArgs) {
		if (logger.isInfoEnabled()) {
			logger.info(String.format(message, formatArgs));
		}
	}

	@Override
	public void warn(String message, Object... formatArgs) {
		if (logger.isWarnEnabled()) {
			logger.warn(String.format(message, formatArgs));
		}
	}

	@Override
	public void error(String message, Object... formatArgs) {
		if (logger.isErrorEnabled()) {
			logger.error(String.format(message, formatArgs));
		}
	}
}
