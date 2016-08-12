package com.aaa.ace.services;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * Page suffix resolver service is used to add the suffix for the internal paths
 * selected by path browser widget in the dialog.
 *
 */
public interface PageSuffixResolverService {
    String resolveLinkURL(final ResourceResolver resolver, final String link);
}
