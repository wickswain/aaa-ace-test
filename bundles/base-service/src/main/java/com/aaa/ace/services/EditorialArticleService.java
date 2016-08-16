/**
 * 
 */
package com.aaa.ace.services;

import java.util.List;

import com.aaa.ace.beans.EditorialCardBean;

/**
 * @author yogesh.mahajan
 *
 */
public interface EditorialArticleService {
	
	List<EditorialCardBean> getEditorialArticles(String path, List<String> searchTagList, boolean usePopularity);
	
}
