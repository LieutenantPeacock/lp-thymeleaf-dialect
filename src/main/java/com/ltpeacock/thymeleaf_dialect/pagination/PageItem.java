package com.ltpeacock.thymeleaf_dialect.pagination;

/**
 * Value object for a page, containing the text to be displayed
 * and the page number.
 * @author LieutenantPeacock
 *
 */
public class PageItem {
	private final String text;
	private final int number;

	/**
	 * Create a {@code PageItem} using the page number as the text.
	 * @param pageNum The page number.
	 */
	public PageItem(final int pageNum) {
		this(String.valueOf(pageNum), pageNum);
	}

	/**
	 * Construct a {@code PageItem} with the specified text to be displayed
	 * and the page number.
	 * @param text The text to be shown for the page.
	 * @param number The page number.
	 */
	public PageItem(final String text, final int number) {
		this.text = text;
		this.number = number;
	}

	/**
	 * Return the text for the page.
	 * @return The text to be displayed for the page.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Return the page number.
	 * @return The page number.
	 */
	public int getNumber() {
		return number;
	}
}
