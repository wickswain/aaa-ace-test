package com.aaa.ace.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface PageSuffixResolverService {
	public String resolveLinkURL(final ResourceResolver resolver, final String link);
}
