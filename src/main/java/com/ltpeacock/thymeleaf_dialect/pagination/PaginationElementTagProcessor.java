package com.ltpeacock.thymeleaf_dialect.pagination;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * The tag processor for the {@code lp:pagination} tag.
 * @author LieutenantPeacock
 *
 */
public class PaginationElementTagProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "pagination";
	private static final int PRECEDENCE = 1000;
	private final PageGenerator pageGenerator;
	
	public PaginationElementTagProcessor(final String dialectPrefix, final PageGenerator pageGenerator) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
		this.pageGenerator = pageGenerator;
	}

	@Override
	protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag,
			final IElementTagStructureHandler structureHandler) {
		final IEngineConfiguration configuration = context.getConfiguration();
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		int currentPage = ((Number)getRequiredAttribute(context, parser, tag, "currentPage")).intValue();
		int firstPage = getOrDefault(context, parser, tag, "firstPage", BigInteger.ONE).intValue();
		int lastPage = ((Number)getRequiredAttribute(context, parser, tag, "lastPage")).intValue();
		int maxPages = getOrDefault(context, parser, tag, "maxPages", BigInteger.TEN).intValue();
		PageGenerator pageGenerator = getOrDefault(context, parser, tag, "pageGenerator", this.pageGenerator);
		boolean generatePrevLink = getOrDefault(context, parser, tag, "generatePrevLink", true);
		boolean generateNextLink = getOrDefault(context, parser, tag, "generateNextLink", true);
		boolean generateFirstLink = getOrDefault(context, parser, tag, "generateFirstLink", true);
		boolean generateLastLink = getOrDefault(context, parser, tag, "generateLastLink", true);
		boolean generateDisabledLinks = getOrDefault(context, parser, tag, "generateDisabledLinks", false);
		String currentClass = getOrDefault(context, parser, tag, "currentClass", "current");
		String pageClass = getOrDefault(context, parser, tag, "pageClass", "page");
		String pageLinkClass = getOrDefault(context, parser, tag, "pageLinkClass", "page-link");
		String rootClass = getOrDefault(context, parser, tag, "rootClass", "pagination");
		String prevClass = getOrDefault(context, parser, tag, "prevClass", "prev-page");
		String nextClass = getOrDefault(context, parser, tag, "nextClass", "next-page");
		String firstClass = getOrDefault(context, parser, tag, "firstClass", "first-page");
		String lastClass = getOrDefault(context, parser, tag, "lastClass", "last-page");
		String disabledClass = getOrDefault(context, parser, tag, "disabledClass", "disabled");
		String prevText = getOrDefault(context, parser, tag, "prevText", "&laquo; Prev");
		String nextText = getOrDefault(context, parser, tag, "nextText", "Next &raquo;");
		String firstText = getOrDefault(context, parser, tag, "firstText", "First");
		String lastText = getOrDefault(context, parser, tag, "lastText", "Last");
		String basePageLink = getOrDefault(context, parser, tag, "basePageLink", "javascript:;");
		if (basePageLink.endsWith("/"))
			basePageLink = basePageLink.substring(0, basePageLink.length() - 1);
		final List<PageItem> pages = pageGenerator.generatePages(currentPage, firstPage, lastPage, maxPages);
		final IModelFactory modelFactory = context.getModelFactory();
		final IModel model = modelFactory.createModel();
		model.add(modelFactory.createOpenElementTag("ul", "class", rootClass));
		if (generateFirstLink && (firstPage != currentPage || generateDisabledLinks)) {
			addPage(modelFactory, model, pageClass + " " + firstClass + (currentPage == firstPage ? " " + disabledClass : ""),
					pageLinkClass, basePageLink,
					String.valueOf(firstPage), firstText);
		}
		if (generatePrevLink && (firstPage != currentPage || generateDisabledLinks)) {
			addPage(modelFactory, model, pageClass + " " + prevClass + (currentPage == firstPage ? " " + disabledClass : ""),
					pageLinkClass, basePageLink,
					String.valueOf(currentPage - 1), prevText);
		}
		for (final PageItem page : pages) {
			addPage(modelFactory, model, pageClass + (page.getNumber() == currentPage ? " " + currentClass : ""),
					pageLinkClass, basePageLink,
					page.getLink(), page.getText());
		}
		if (generateNextLink && (lastPage != currentPage || generateDisabledLinks)) {
			addPage(modelFactory, model, pageClass + " " + nextClass + (currentPage == lastPage ? " " + disabledClass : ""),
					pageLinkClass, basePageLink,
					String.valueOf(currentPage + 1), nextText);
		}
		if (generateLastLink && (lastPage != currentPage || generateDisabledLinks)) {
			addPage(modelFactory, model, pageClass + " " + lastClass + (currentPage == lastPage ? " " + disabledClass : ""),
					pageLinkClass, basePageLink,
					String.valueOf(lastPage), lastText);
		}
		model.add(modelFactory.createCloseElementTag("ul"));
		structureHandler.replaceWith(model, false);
	}

	private void addPage(final IModelFactory modelFactory, final IModel model, final String pageClass, final String pageLinkClass, final String basePageLink, final String link, final String text) {
		model.add(modelFactory.createOpenElementTag("li", "class", pageClass.trim()));
		final Map<String, String> linkAttrs = new HashMap<>();
		linkAttrs.put("class", pageLinkClass);
		linkAttrs.put("href", basePageLink + "/" + link);
		model.add(modelFactory.createOpenElementTag("a", linkAttrs, null, false));
		model.add(modelFactory.createText(text));
		model.add(modelFactory.createCloseElementTag("a"));
		model.add(modelFactory.createCloseElementTag("li"));
	}

	@SuppressWarnings("unchecked")
	private static <T> T getRequiredAttribute(final ITemplateContext context, final IStandardExpressionParser parser,final IProcessableElementTag tag, final String attribute) {
		final String value = tag.getAttributeValue(attribute);
		if (value == null)
			throw new TemplateProcessingException("Required attribute [" + attribute + "] not present on tag");
		return (T) parser.parseExpression(context, value).execute(context);
	}

	@SuppressWarnings("unchecked")
	private static <T> T getOrDefault(final ITemplateContext context, final IStandardExpressionParser parser, final IProcessableElementTag tag, final String attribute,
			final T defaultValue) {
		return Optional.ofNullable(tag.getAttributeValue(attribute)).map(val -> 
				(T) parser.parseExpression(context, val).execute(context)).orElse(defaultValue);
	}
}
