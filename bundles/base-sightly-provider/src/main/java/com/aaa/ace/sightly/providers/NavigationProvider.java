package com.aaa.ace.sightly.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.cq.sightly.WCMUsePojo;
import com.aaa.ace.sightly.modals.NavigationVO;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

/**
 * Provides a generic utility class that can be used to draw a navigation. It
 * specifically does this by providing a list of navigation elements VO object.
 * 
 * A navigation element VO object reflects a page and can have its own child
 * pages as list of navigation elements VO objects.
 */

public class NavigationProvider extends WCMUsePojo {

	private static final Logger log = LoggerFactory.getLogger(NavigationProvider.class);

	private static final String NAVIGATION_HOME_PAGE = "/content/navigation-root";
	private static final String PAGE_TYPE_KEY = "pageType";
	private static final String PAGE_TYPE_VALUE = "tile";
	private static final int PAGE_TYPE_TILE_MAX_COUNT = 6;
	private static final int FIRST_LEVEL_NAVIGATION_CHILD_MAX_COUNT = 7;

	private List<NavigationVO> navigationVOs = new ArrayList<NavigationVO>();

	private Page homePage = null;

	@Override
	public void activate() throws Exception {
		log.info("Entering activate() method of NavigationHelper use class");
		setHomePage();
		getNavigationItems();
	}

	private Page setHomePage() {
		homePage = getPageManager().getPage(NAVIGATION_HOME_PAGE);
		return homePage;
	}

	/*
	 * This method iterate the parent node. For parent node iterate each child
	 * set currengtPage,hasChildren and list of children of that child in
	 * NavigationVO object based on node properties.
	 */
	private void getNavigationItems() {
		int firstLevelNavigationChildCount = 0;
		if(StringUtils.isNotBlank(homePage.getPath())){
			Iterator<Page> navigationNodes = homePage.listChildren();

			while (navigationNodes.hasNext()) {
				firstLevelNavigationChildCount+=1;
				if(firstLevelNavigationChildCount > FIRST_LEVEL_NAVIGATION_CHILD_MAX_COUNT)
					break;
				NavigationVO navigationVO = new NavigationVO();
				Page currentPage = navigationNodes.next();

				if (!currentPage.getProperties().containsKey(NameConstants.PN_HIDE_IN_NAV)) {
					navigationVO.setCurrentpage(currentPage);
					navigationVO.setUniqueId(UUID.randomUUID().toString());
					getChildPages(navigationVO, currentPage);
					navigationVOs.add(navigationVO);
				}
			}
		}
	}

	/*
	 * This method iterates over all the child nodes of the parent
	 * page. For current node iterate each child set currengtPage,hasChildren
	 * and list of children of that child in NavigationVO object based on node
	 * properties.
	 */

	private void getChildPages(NavigationVO navigationVO, Page currentPage) {
		Iterator<Page> childPages = currentPage.listChildren();

		List<NavigationVO> newChildPagesList = new ArrayList<NavigationVO>();
		NavigationVO navigationChildVo = null;
		int tilePageCount = 0;
		while (childPages.hasNext()) {
			navigationChildVo = new NavigationVO();
			Page childPage = childPages.next();
			if (isVisibleInNav(childPage)) {
				boolean isQualifiedNavPage = true;
				if(isTilePage(childPage)){
					tilePageCount++;
					isQualifiedNavPage = tilePageCount <= PAGE_TYPE_TILE_MAX_COUNT;
				}
				if(isQualifiedNavPage){
					navigationChildVo.setCurrentpage(childPage);
					navigationChildVo.setUniqueId(UUID.randomUUID().toString());
					newChildPagesList.add(navigationChildVo);
					
					if (childPage.listChildren() != null) {
						getChildPages(navigationChildVo, childPage);
					}
				}
			}
		}
		navigationVO.setHasChildren(newChildPagesList.size()>1);
		navigationVO.setNavigList(newChildPagesList);
	}
	
	private boolean isVisibleInNav(Page page){
		return !page.getProperties().containsKey(NameConstants.PN_HIDE_IN_NAV);
	}

	private boolean isTilePage(Page page){
		if(page.getProperties().containsKey(PAGE_TYPE_KEY) && page.getProperties().get(PAGE_TYPE_KEY).equals(PAGE_TYPE_VALUE)){
		   return true;
		}
		return false;
	}

	public Page getHomePage() {
		return homePage;
	}

	public List<NavigationVO> getNavigationVOs() {
		return navigationVOs;
	}

}
