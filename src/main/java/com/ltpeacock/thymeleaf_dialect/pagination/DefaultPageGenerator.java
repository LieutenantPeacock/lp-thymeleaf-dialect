package com.ltpeacock.thymeleaf_dialect.pagination;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The default {@link PageGenerator} that generates pages by
 * trying to place the current page in the middle of the list of pages
 * and use the maximum number of allowed pages.
 * @author LieutenantPeacock
 *
 */
public class DefaultPageGenerator implements PageGenerator {
	@Override
	public List<PageItem> generatePages(int currentPage, int firstPage, int lastPage, int maxPages) {
		int start = Math.max(currentPage - maxPages / 2, firstPage);
		int end = start + maxPages - 1;
		if(end > lastPage) {
			final int over = end - lastPage;
			start = Math.max(start - over, firstPage);
			end = lastPage;
		}
		return IntStream.rangeClosed(start, end).mapToObj(PageItem::new).collect(Collectors.toList());
	}
}
