package com.ltpeacock.thymeleaf_dialect.pagination;

/**
 * Class for generating the actual links to be used for each page
 * (set as the href attribute of the created anchor elements).
 * The default generator used is {@link DefaultPageLinkGenerator}.
 * @author LieutenantPeacock
 *
 */
@FunctionalInterface
public interface PageLinkGenerator {
	/**
	 * Generate the link for the specified page.
	 * @param basePageLink The base URL specified on the {@code lp:pagination} tag.
	 * @param page The number of the current page.
	 * @return The link for the current page.
	 */
	String generateLink(final String basePageLink, final int page);
}
