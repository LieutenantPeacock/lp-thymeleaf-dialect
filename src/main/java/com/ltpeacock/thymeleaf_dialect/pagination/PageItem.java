package com.ltpeacock.thymeleaf_dialect.pagination;

/**
 * Value object for a page, containing the link to it, the text to be displayed, 
 * and the page number.
 * @author LieutenantPeacock
 *
 */
public class PageItem {
	private final String link;
	private final String text;
	private final int number;

	/**
	 * Create a {@code PageItem} using the page number as the link and text.
	 * @param pageNum The page number.
	 */
	public PageItem(final int pageNum) {
		this(String.valueOf(pageNum), String.valueOf(pageNum), pageNum);
	}
	
	/**
	 * Create a {@code PageItem} with the specified link and text. 
	 * The page number is set to {@code -1}, indicating that it is not specified.
	 * @param link The link for the page.
	 * @param text The text to be shown for the page.
	 */
	public PageItem(final String link, final String text) {
		this(link, text, -1);
	}

	/**
	 * Construct a {@code PageItem} with the specified link, text to be displayed, 
	 * and the page number.
	 * @param link The link for the page.
	 * @param text The text to be shown for the page.
	 * @param number The page number.
	 */
	public PageItem(final String link, final String text, final int number) {
		this.link = link;
		this.text = text;
		this.number = number;
	}

	/**
	 * Return the link for the page.
	 * @return The link for the page.
	 */
	public String getLink() {
		return link;
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
