package com.aaa.ace.services;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * Page suffix resolver service is used to add the suffix for the internal paths
 * selected by path browser widget in the dialog.
 *
 */
public interface PageSuffixResolverService {

	/**
	 * Gets the valid link URL.
	 *
	 * @param resolver
	 *            the resourceResolver
	 * @param link
	 *            the link
	 * @return String
	 */
	String resolveLinkURL(final ResourceResolver resolver, final String link);

	/**
	 * Gets the valid link map URL using the etc map configuration.
	 *
	 * @param resolver
	 *            the resourceResolver
	 * @param link
	 *            the link
	 * @return String
	 */
	String resolveLinkMapURL(final ResourceResolver resolver, final String link);
}
