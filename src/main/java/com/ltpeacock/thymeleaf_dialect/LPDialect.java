package com.ltpeacock.thymeleaf_dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import com.ltpeacock.thymeleaf_dialect.pagination.DefaultPageGenerator;
import com.ltpeacock.thymeleaf_dialect.pagination.PageGenerator;
import com.ltpeacock.thymeleaf_dialect.pagination.PaginationElementTagProcessor;

/**
 * Lt. Peacock Dialect. Must be added to the {@link org.thymeleaf.TemplateEngine}
 * to use tags with the {@code lp} prefix.
 * @author LieutenantPeacock
 *
 */
public class LPDialect extends AbstractProcessorDialect {
	private static final String DIALECT_NAME = "Lt. Peacock Dialect";
	private PageGenerator pageGenerator = new DefaultPageGenerator();

	public LPDialect() {
		super(DIALECT_NAME, "lp", StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(final String dialectPrefix) {
		final Set<IProcessor> processors = new HashSet<>();
		processors.add(new PaginationElementTagProcessor(dialectPrefix, pageGenerator));
		processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
		return processors;
	}

	/**
	 * Set the {@link PageGenerator} used by default for the {@code lp:pagination} tag.
	 * @param pageGenerator The PageGenerator to use when it is not specified 
	 * as an attribute on the {@code lp:pagination} tag. 
	 * The default is {@link DefaultPageGenerator}.
	 */
	public void setPageGenerator(PageGenerator pageGenerator) {
		this.pageGenerator = pageGenerator;
	}
}
