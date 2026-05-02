package com.ltpeacock.thymeleaf_dialect.pagination;

/**
 * The default {@link PageLinkGenerator} that generates links by concatenating
 * the base page link with a slash (/) and then the page number.
 * @author LieutenantPeacock
 *
 */
public class DefaultPageLinkGenerator implements PageLinkGenerator {
	@Override
	public String generateLink(final String basePageLink, final int page) {
		return basePageLink + "/" + page;
	}
}
