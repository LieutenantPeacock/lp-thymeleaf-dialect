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
 * 
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
		final IModelFactory modelFactory = context.getModelFactory();
		final IModel model = modelFactory.createModel();
		class Util {
			final int currentPage = ((Number) getRequiredAttribute("currentPage")).intValue();
			final int firstPage = ((Number) getOrDefault("firstPage", BigInteger.ONE)).intValue();
			final int lastPage = ((Number) getRequiredAttribute("lastPage")).intValue();
			final int maxPages = ((Number) getOrDefault("maxPages", BigInteger.TEN)).intValue();
			final PageGenerator pageGenerator = (PageGenerator) getOrDefault("pageGenerator",
					PaginationElementTagProcessor.this.pageGenerator);
			final boolean generatePrevLink = (boolean) getOrDefault("generatePrevLink", true);
			final boolean generateNextLink = (boolean) getOrDefault("generateNextLink", true);
			final boolean generateFirstLink = (boolean) getOrDefault("generateFirstLink", true);
			final boolean generateLastLink = (boolean) getOrDefault("generateLastLink", true);
			final boolean generateDisabledLinks = (boolean) getOrDefault("generateDisabledLinks", false);
			final String currentClass = getOrDefault("currentClass", "current").toString();
			final String pageClass = getOrDefault("pageClass", "page").toString();
			final String pageLinkClass = getOrDefault("pageLinkClass", "page-link").toString();
			final String rootClass = getOrDefault("rootClass", "pagination").toString();
			final String prevClass = getOrDefault("prevClass", "prev-page").toString();
			final String nextClass = getOrDefault("nextClass", "next-page").toString();
			final String firstClass = getOrDefault("firstClass", "first-page").toString();
			final String lastClass = getOrDefault("lastClass", "last-page").toString();
			final String disabledClass = getOrDefault("disabledClass", "disabled").toString();
			final String prevText = getOrDefault("prevText", "&laquo; Prev").toString();
			final String nextText = getOrDefault("nextText", "Next &raquo;").toString();
			final String firstText = getOrDefault("firstText", "First").toString();
			final String lastText = getOrDefault("lastText", "Last").toString();
			final String basePageLink = getOrDefault("basePageLink", "javascript:;").toString().replaceAll("/$", "");

			Object getRequiredAttribute(final String attribute) {
				final String value = tag.getAttributeValue(attribute);
				if (value == null)
					throw new TemplateProcessingException(
							"Required attribute [" + attribute + "] not present on " + TAG_NAME + " tag");
				return parser.parseExpression(context, value).execute(context);
			}

			Object getOrDefault(final String attribute, final Object defaultValue) {
				return Optional.ofNullable(tag.getAttributeValue(attribute))
						.map(val -> parser.parseExpression(context, val).execute(context)).orElse(defaultValue);
			}

			void addPage(final String pageClass, final String link, final String text) {
				model.add(modelFactory.createOpenElementTag("li", "class", pageClass.trim()));
				final Map<String, String> linkAttrs = new HashMap<>();
				linkAttrs.put("class", pageLinkClass);
				linkAttrs.put("href", basePageLink + "/" + link);
				model.add(modelFactory.createOpenElementTag("a", linkAttrs, null, false));
				model.add(modelFactory.createText(text));
				model.add(modelFactory.createCloseElementTag("a"));
				model.add(modelFactory.createCloseElementTag("li"));
			}

			void process() {
				final List<PageItem> pages = pageGenerator.generatePages(currentPage, firstPage, lastPage, maxPages);
				model.add(modelFactory.createOpenElementTag("ul", "class", rootClass));
				if (generateFirstLink && (firstPage != currentPage || generateDisabledLinks)) {
					addPage(pageClass + " " + firstClass + (currentPage == firstPage ? " " + disabledClass : ""),
							String.valueOf(firstPage), firstText);
				}
				if (generatePrevLink && (firstPage != currentPage || generateDisabledLinks)) {
					addPage(pageClass + " " + prevClass + (currentPage == firstPage ? " " + disabledClass : ""),
							String.valueOf(currentPage - 1), prevText);
				}
				for (final PageItem page : pages) {
					addPage(pageClass + (page.getNumber() == currentPage ? " " + currentClass : ""), page.getLink(),
							page.getText());
				}
				if (generateNextLink && (lastPage != currentPage || generateDisabledLinks)) {
					addPage(pageClass + " " + nextClass + (currentPage == lastPage ? " " + disabledClass : ""),
							String.valueOf(currentPage + 1), nextText);
				}
				if (generateLastLink && (lastPage != currentPage || generateDisabledLinks)) {
					addPage(pageClass + " " + lastClass + (currentPage == lastPage ? " " + disabledClass : ""),
							String.valueOf(lastPage), lastText);
				}
				model.add(modelFactory.createCloseElementTag("ul"));
				structureHandler.replaceWith(model, false);
			}
		}
		new Util().process();
	}
}
