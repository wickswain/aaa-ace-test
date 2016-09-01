package com.aaa.ace.sightly.providers;

import com.aaa.ace.services.PageSuffixResolverService;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider class is used to get a valid link URL appended with a
 * valid extension based on the selection through path browser granite widget.
 *
 */
public class PathBrowserPropertyProvider extends WCMUsePojo {

    /**
     * Valid Link URL.
     */
    private String linkURL;

    @Override
    public final void activate() throws Exception {
        final String link = get("link", String.class);

        if (link != null && link.startsWith("/content")) {
            PageSuffixResolverService pageService = getSlingScriptHelper()
                    .getService(PageSuffixResolverService.class);
            linkURL = pageService.resolveLinkURL(getResourceResolver(), link);
        } else {
            linkURL = link;
        }
    }

    /**
     * gets the valid link URL.
     *
     * @return String
     */
    public final String getLinkURL() {
        return linkURL;
    }

}
