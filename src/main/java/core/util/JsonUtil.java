package core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eel.kitchen.jsonschema.main.JsonValidationFailureException;
import org.eel.kitchen.jsonschema.main.JsonValidator;
import org.eel.kitchen.jsonschema.main.ValidationReport;
import org.eel.kitchen.util.JsonLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper methods for manipulation of JSON.
 * 
 * @author jani
 * 
 */
public class JsonUtil {
	private static Logger log = LoggerFactory.getLogger(JsonUtil.class);

	/**
	 * Read an input stream to an object.
	 * 
	 * @param <T>
	 *            Type of object.
	 * @param stream
	 *            Stream to read.
	 * @param clazz
	 *            {@link Class} of object.
	 * @return T Read object.
	 */
	public static <T> T readJsonFile(InputStream stream, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		T res = null;

		try {
			res = mapper.readValue(stream, clazz);
		} catch (JsonParseException e) {
			log.info("JsonUtils.readJsonFile(): Unable to parse stream");
		} catch (JsonMappingException e) {
			log.info("JsonUtils.readJsonFile(): Unable to map stream");
		} catch (IOException e) {
			log.info("JsonUtils.readJsonFile(): Unable to load stream");
		}

		return res;
	}

	/**
	 * Reads a JSON file to an object.
	 * 
	 * @param <T>
	 *            Type of object.
	 * @param file
	 *            File to read.
	 * @param clazz
	 *            {@link Class} of object
	 * @return T Read object.
	 */
	public static <T> T readJsonFile(File file, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		T res = null;

		try {
			res = mapper.readValue(file, clazz);
		} catch (JsonParseException e) {
			log.info(String.format(
					"JsonUtils.readJsonFile(): Unable to parse %s",
					file.getPath()));
		} catch (JsonMappingException e) {
			log.info(String.format(
					"JsonUtils.readJsonFile(): Unable to map %s",
					file.getPath()));
		} catch (IOException e) {
			log.info(String.format(
					"JsonUtils.readJsonFile(): Unable to load %s",
					file.getPath()));
		}

		return res;
	}

	/**
	 * Reads a JSON string to an object.
	 * 
	 * @param <T>
	 *            Type of object.
	 * @param json
	 *            JSON string.
	 * @param clazz
	 *            {@link Class} of object.
	 * @return T Read object.
	 */
	public static <T> T readJsonString(String json, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		T res = null;

		try {
			res = mapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			log.info("JsonUtils.readJsonString(): Unable to parse stream");
		} catch (JsonMappingException e) {
			log.info("JsonUtils.readJsonString(): Unable to map stream");
		} catch (IOException e) {
			log.info("JsonUtils.readJsonString(): Unable to load stream");
		}

		return res;
	}

	/**
	 * Writes an object as a JSON to a file
	 * 
	 * @param file
	 *            Destination file.
	 * @param o
	 *            Object to write.
	 * @return Whether successful.
	 */
	public static boolean writeJsonFile(File file, Object o) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(file, o);
		} catch (Exception e) {
			log.info(String.format(
					"JsonUtils.writeJsonFile(): Unable to write %s",
					file.getPath()));
			return false;
		}
		return true;
	}

	/**
	 * Converts object to JSON string.
	 * 
	 * @param o
	 *            object to convert.
	 * @return JSON string.
	 */
	public static String objectToJsonString(Object o) {
		return objectToJsonString(o, null);
	}

	/**
	 * Converts object to JSON string, using a view class for property
	 * filtering.
	 * 
	 * @param o
	 *            object to convert.
	 * @param view
	 *            {@link Class} used for filtering.
	 * @return JSON string.
	 */
	public static String objectToJsonString(Object o, Class<?> view) {
		ObjectMapper mapper = new ObjectMapper();

		// mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION,false);
		try {
			if (view != null) {
				return mapper.writerWithView(view).writeValueAsString(o);
			}
			return mapper.writeValueAsString(o);
		} catch (Exception e) {
			log.info(String
					.format("JsonUtils.objectToJsonString(): Unable to write to string"));
		}
		return null;
	}

	/**
	 * Converts object to JSON.
	 * 
	 * @param o
	 *            object to convert.
	 * @return {@link JsonNode}
	 */
	public static JsonNode objectToJson(Object o) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.valueToTree(o);
		} catch (Exception e) {
			log.error(String
					.format("JsonUtils.objectToJson(): Unable to write json"));
		}
		return null;
	}

	/**
	 * Validates object against a JSON schema.
	 * 
	 * @param o
	 *            object to validate.
	 * @param schemaResource
	 *            schema used for validation.
	 * @return {@link Validator}
	 */
	public static Validator validateJson(Object o, String schemaResource) {
		JsonNode json = objectToJson(o);

		JsonNode schema;
		try {
			schema = JsonLoader.fromResource(schemaResource);
			return validateJson(json, schema);
		} catch (IOException e) {
			log.info(String
					.format("JsonUtils.validateJson(): Unable to load schema json"));
			Validator res = new Validator();
			res.addError("jsonerror", "Unable to load schema");
			return res;
		}
	}

	/**
	 * Validates JSON node against a JSON schema.
	 * 
	 * @param json
	 *            {@link JsonNode} to validate.
	 * @param schema
	 *            {@link JsonNode} schema.
	 * @return {@link Validator}.
	 */
	public static Validator validateJson(JsonNode json, JsonNode schema) {
		Validator res = new Validator();

		JsonValidator validator;
		try {
			validator = new JsonValidator(schema);

			ValidationReport report;

			report = validator.validate(json);

			int i = 0;
			for (String s : report.getMessages()) {
				res.addError("json_error_" + i, s);
				i++;
			}
		} catch (JsonValidationFailureException e) {
			res.addError("jsonerror", "Unable to validate json");
			log.info(String
					.format("JsonUtils.validateJson(): Unable to validate json"));
		}
		return res;
	}

}
