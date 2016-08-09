package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This class is a sightly provider used in page intro component.
 *
 * @author bharath.kambam
 */
public class PageIntroSightlyProvider extends WCMUsePojo {

    /**
     * Title variable.
     */
    private String title;

    /*
     * (non-Javadoc)
     *
     * @see com.adobe.cq.sightly.WCMUsePojo#activate()
     */
    @Override
    public final void activate() throws Exception {
        getTitle();
    }

    /**
     * Get the page intro title.
     *
     * @return the title
     */
    public final String getTitle() {

        if (getCurrentPage().getNavigationTitle() != null) {
            title = getCurrentPage().getNavigationTitle();
        } else {
            title = getCurrentPage().getTitle();
        }

        return title;
    }

}
