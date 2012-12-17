package core.util;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Helper reflection methods.
 * 
 * @author jani
 * 
 */
public class ReflectionUtil {

	/**
	 * Gets the value of a property.
	 * 
	 * @param <T>
	 * @param object
	 * @param propName
	 * @return Value of a property.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPropertyValue(Object object, String propName) {
		try {
			return (T) new PropertyDescriptor(propName, object.getClass())
					.getReadMethod().invoke(object);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gets the class of a property.
	 * 
	 * @param object
	 * @param propName
	 * @return {@link Class}
	 */
	public static Class<?> getPropertyClass(Object object, String propName) {
		try {
			return new PropertyDescriptor(propName, object.getClass())
					.getPropertyType();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Sets the value of a property.
	 * 
	 * @param object
	 * @param propName
	 * @param value
	 */
	public static void setPropertyValue(Object object, String propName,
			Object value) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(propName,
					object.getClass());
			pd.getWriteMethod().invoke(object, value);
		} catch (Exception e) {
			// nothing
		}
	}

	/**
	 * Sets a list of properties with a certain values. Compares the current
	 * value of the property with the corresponding value in oldValues and if
	 * different, adds an error.
	 * 
	 * @param object
	 * @param oldValues
	 * @param newValues
	 * @return {@link Validator}
	 */
	public static Validator updateObjectProps(Object object,
			Map<String, Object> oldValues, Map<String, Object> newValues) {
		Validator validator = new Validator();

		Object current = null;
		Object oldValue = null;
		Object newValue = null;
		Class<?> clazz = null;
		for (String key : newValues.keySet()) {
			boolean valid = true;
			if (oldValues.containsKey(key)) {
				clazz = getPropertyClass(object, key);

				current = getPropertyValue(object, key);
				oldValue = Parser.readObject(oldValues.get(key), clazz);
				if (current != null && oldValue != null
						&& !current.equals(oldValue)) {
					valid = false;
				}
			}
			if (valid) {
				newValue = Parser.readObject(newValues.get(key), clazz);
				setPropertyValue(object, key, newValue);
			} else {
				validator.addError(String.format("changed_%s", key),
						String.format("%s has been changed", key));
			}
		}

		return validator;
	}
}
