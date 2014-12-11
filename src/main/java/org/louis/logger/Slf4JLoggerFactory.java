package org.louis.logger;

import org.slf4j.helpers.NOPLoggerFactory;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Slf4JLoggerFactory extends LoggerFactory {

	public Slf4JLoggerFactory() {
	}

	Slf4JLoggerFactory(boolean failIfNOP) {
		assert failIfNOP;
		final StringBuffer buf = new StringBuffer();
		final PrintStream err = System.err;
		try {
			System.setErr(new PrintStream(new OutputStream() {
				@Override
				public void write(int b) {
					buf.append((char) b);
				}
			}, true, "US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}

		try {
			if (org.slf4j.LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
				throw new NoClassDefFoundError(buf.toString());
			} else {
				err.print(buf);
				err.flush();
			}
		} finally {
			System.setErr(err);
		}
	}

	@Override
	public Logger newInstance(String name) {
		return new Slf4JLogger(org.slf4j.LoggerFactory.getLogger(name));
	}
}
