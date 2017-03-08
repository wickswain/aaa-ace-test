package com.aaa.ace.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

public final class CORSConfig {
	private static final CORSConfig INSTANCE = new CORSConfig();

	private boolean enabled = false;
	private String[] allowedDomains;
	private String[] allowedMethods;
	
	private int maxage;

	// Private constructor prevents instantiation from other classes
	private CORSConfig() {
	}

	/**
	 * Get the CORSConfig instance.
	 * 
	 * @return the CORSConfig singleton
	 */
	public static CORSConfig getInstance() {
		return INSTANCE;
	}

	public synchronized boolean isEnabled() {
		return enabled;
	}

	public synchronized void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public synchronized String[] getAllowedMethods() {
		return allowedMethods;
	}
	
	public synchronized void setAllowedMethods(String[] allowedMethods) {
		this.allowedMethods = allowedMethods;
	}

	public synchronized String[] getAllowedDomains() {
		return allowedDomains;
	}

	public synchronized void setAllowedDomains(String[] allowedDomains) {
		this.allowedDomains = allowedDomains;
	}

	public int getMaxage() {
		return maxage;
	}

	public void setMaxage(int maxage) {
		this.maxage = maxage;
	}

	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}