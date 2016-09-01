/**
 * 
 */
package com.aaa.ace.services;

import com.aaa.ace.beans.PageMetaDataBean;
import com.day.cq.tagging.Tag;

/**
 * This service providers meta name and content based on tag passed in.
 * 
 * @author yogesh.mahajan
 *
 */
public interface MetaTagService {
    
    PageMetaDataBean getMetaData (Tag tag);

}
