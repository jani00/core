package core.web.view;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import core.config.WebConfig;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * A view of a web page. Contains an instance of Freemarker and a model which
 * feeds it.
 * 
 * @author jani
 * 
 */
public class View {

	private Configuration config;

	private Map<String, Object> model;

	private String masterTemplate;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            Servlet which owns the view.
	 */
	public View(ServletContext context) {
		config = new Configuration();
		config.setServletContextForTemplateLoading(context,
				WebConfig.TEMPLATES_LOCATION);
		config.setLocale(WebConfig.FTL_LOCALE);
		config.setTemplateUpdateDelay(0);
		config.setEncoding(WebConfig.FTL_LOCALE,
				WebConfig.FTL_TEMPLATE_ENCODING);
		config.setOutputEncoding(WebConfig.FTL_OUTPUT_ENCODING);
		config.setLocalizedLookup(false);

		model = new HashMap<String, Object>();

		masterTemplate = WebConfig.DEFAULT_MASTER_TEMPLATE;
	}

	/**
	 * Tries to parse the view to a string.
	 * 
	 * @return Result of parsing.
	 * @throws ViewException
	 *             Error while parsing
	 */
	public String parse() throws ViewException {
		return parse(masterTemplate);
	}

	/**
	 * Tries to parse the view to a string.
	 * 
	 * @param templateFile
	 *            Template to parse.
	 * @return Result of parsing.
	 * @throws ViewException
	 *             Error while parsing
	 */
	public String parse(String templateFile) throws ViewException {
		Template template = null;
		try {
			template = config.getTemplate(templateFile);
		} catch (ParseException e) {
			throw new ViewException(e.getMessage(), e);
		} catch (IOException e) {
			throw new ViewException(String.format(
					"Error opening template \"%s\"", templateFile), e);
		}
		StringWriter writer = new StringWriter();

		try {
			template.process(model, writer);
		} catch (IOException e) {
			throw new ViewException(String.format(
					"Error writing template \"%s\" to writer", templateFile), e);
		} catch (TemplateException e) {
			throw new ViewException(e.getMessage(), e);
		}

		return writer.toString();
	}

	/**
	 * Puts a value to the model.
	 * 
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		model.put(key, value);
	}

	/**
	 * Puts all values to the model.
	 * 
	 * @param values
	 */
	public void put(Map<String, Object> values) {
		model.putAll(values);
	}

	/**
	 * Puts the result of a template parsing to the model.
	 * 
	 * @param key
	 * @param templateFile
	 * @throws ViewException
	 */
	public void putTemplate(String key, String templateFile)
			throws ViewException {
		put(key, parse(templateFile));
	}

	/**
	 * Sets the master template of the view.
	 * 
	 * @param masterTemplate
	 */
	public void setMaster(String masterTemplate) {
		this.masterTemplate = masterTemplate;
	}
}
