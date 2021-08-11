package com.ltpeacock.thymeleaf_dialect.pagination;

import java.util.List;

/**
 * Class for generating pages. This is used by the {@code lp:pagination} tag.
 * The default generator used is {@link DefaultPageGenerator}.
 * @author LieutenantPeacock
 *
 */
public interface PageGenerator {
	/**
	 * Generate the {@link List} of {@link PageItem} objects to be used for pagination.
	 * @param currentPage The current page number.
	 * @param firstPage The first page number.
	 * @param lastPage The last page number.
	 * @param maxPages The maximum number of pages to generate.
	 * @return The PageItems to be used for the current {@code lp:pagination} tag.
	 */
	List<PageItem> generatePages(int currentPage, int firstPage, int lastPage, int maxPages);
}
