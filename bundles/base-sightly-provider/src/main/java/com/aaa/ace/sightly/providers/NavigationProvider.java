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

	//private static final String STYLE_PROPERTY_ABSOLUTE_PARENT = "absParent";

	//private static final String STYLE_PROPERTY_NAVIGATION_HOME_PAGE = "rootPagePath";

	private List<NavigationVO> navigationVOs = new ArrayList<NavigationVO>();

	private Page homePage = null;

	@Override
	public void activate() throws Exception {
		log.info("Entering activate() method of NavigationHelper use class");
		setHomePage();
		getNavigationItems();
	}

	private Page setHomePage() {
		// homePage = getPageManager().getPage(getCurrentStyle().get(STYLE_PROPERTY_NAVIGATION_HOME_PAGE, ""));
		homePage = getPageManager().getPage("/content/navigation-root");
		/*if (homePage == null) {
			homePage = getRootPage(getCurrentPage(), getCurrentStyle().get(STYLE_PROPERTY_ABSOLUTE_PARENT, 2));
		}*/

		return homePage;
	}

	/*
	 * This method iterate the parent node. For parent node iterate each child
	 * set currengtPage,hasChildren and list of children of that child in
	 * NavigationVO object based on node properties.
	 */
	private void getNavigationItems() {
		//Page navRootPage = getRootPage(getCurrentPage(), getCurrentStyle().get(STYLE_PROPERTY_ABSOLUTE_PARENT, 2));
		
		if(StringUtils.isNotBlank(homePage.getPath())){
			Iterator<Page> navigationNodes = homePage.listChildren();

			while (navigationNodes.hasNext()) {
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
	 * This method recursively iterates over all the child nodes of the parent
	 * page. For current node iterate each child set currengtPage,hasChildren
	 * and list of children of that child in NavigationVO object based on node
	 * properties.
	 */

	private void getChildPages(NavigationVO navigationVO, Page currentPage) {
		Iterator<Page> childNodes = currentPage.listChildren();
		Iterator<Page> tmp = currentPage.listChildren();
		navigationVO.setHasChildren(hasMoreThnOneChild(tmp));

		List<NavigationVO> childPages = new ArrayList<NavigationVO>();
		NavigationVO navigationChildVo = null;

		while (childNodes.hasNext()) {
			navigationChildVo = new NavigationVO();
			Page childPage = childNodes.next();
			if (!childPage.getProperties().containsKey(NameConstants.PN_HIDE_IN_NAV)) {
				navigationChildVo.setCurrentpage(childPage);
				navigationChildVo.setUniqueId(UUID.randomUUID().toString());
				childPages.add(navigationChildVo);

				if (childPage.listChildren() != null) {
					getChildPages(navigationChildVo, childPage);
				}

			}

		}

		navigationVO.setNavigList(childPages);
	}

	private boolean hasMoreThnOneChild(Iterator<Page> iterator) {
		boolean isHideInNav = false, isNotHideInNav = true;

		while (iterator.hasNext()) {
			Page tmpPage = iterator.next();
			isHideInNav = tmpPage.getProperties().containsKey(NameConstants.PN_HIDE_IN_NAV);
			if (!isHideInNav) {
				isNotHideInNav = tmpPage.getProperties().containsKey(NameConstants.PN_HIDE_IN_NAV);
			}
		}

		if (!isNotHideInNav) {
			isNotHideInNav = true;
		} else {
			isNotHideInNav = false;
		}

		return isNotHideInNav;
	}

	/*
	 * Gets the root page where the navigation will start from.
	 * 
	 * @param currentPage The current page.
	 * 
	 * @param absParent The absolute level of the navigation root.
	 * 
	 * @return The root page.
	 */
	/*private Page getRootPage(final Page currentPage, final int absParent) {
		final Page parentPage = currentPage.getAbsoluteParent(absParent);
		if (parentPage == null) {
			return currentPage;
		}
		return parentPage;
	}*/

	public Page getHomePage() {
		return homePage;
	}

	public List<NavigationVO> getNavigationVOs() {
		return navigationVOs;
	}

}
