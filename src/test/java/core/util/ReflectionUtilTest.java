package core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for {@link ReflectionUtil}.
 * 
 * @author jani
 * 
 */
public class ReflectionUtilTest extends TestBase {
	private Person person;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		person = new Person("john", 30);
	}

	/**
	 * Tests {@link ReflectionUtil#getPropertyValue(Object, String)}.
	 */
	public void testGetPropertyValue() {
		String name = ReflectionUtil.getPropertyValue(person, "wrong");
		assertNull(name);

		name = ReflectionUtil.getPropertyValue(person, "name");
		assertEquals(person.getName(), name);

		Integer age = ReflectionUtil.getPropertyValue(person, "age");
		assertEquals(person.getAge(), age);

	}

	/**
	 * Tests {@link ReflectionUtil#getPropertyClass(Object, String)}.
	 */
	public void testGetPropertyClass() {
		Class<?> clazz;
		clazz = ReflectionUtil.getPropertyClass(person, "wrong");
		assertNull(clazz);

		clazz = ReflectionUtil.getPropertyClass(person, "name");
		assertEquals(String.class, clazz);

		clazz = ReflectionUtil.getPropertyClass(person, "age");
		assertEquals(Integer.class, clazz);

	}

	
	/**
	 * Tests {@link ReflectionUtil#setPropertyValue(Object, String, Object)}.
	 */
	public void testSetPropertyValue() {
		ReflectionUtil.setPropertyValue(person, "name", "mike");
		assertEquals("mike", person.getName());
		
		ReflectionUtil.setPropertyValue(person, "age", 40);
		assertSame(40, person.getAge());
	}
	
	/**
	 * Tests {@link ReflectionUtil#updateObjectProps(Object, Map, Map)}.
	 */
	public void testUpdateObjectPropsInvalid() {
		Validator res;
		
		Map<String, Object> oldValues = new HashMap<String, Object>();
		Map<String, Object> newValues = new HashMap<String, Object>();
		
		oldValues.put("name", "peter");
		oldValues.put("age", 30);
		
		newValues.put("name", "mike");
		newValues.put("age", 40);
		
		res = ReflectionUtil.updateObjectProps(person, oldValues, newValues);
		
		assertFalse(res.isValid());
		assertEquals("john", person.getName());
		assertEquals(newValues.get("age"), person.getAge());
	}
	
	/**
	 * Tests {@link ReflectionUtil#updateObjectProps(Object, Map, Map)}.
	 */
	public void testUpdateObjectPropsValid() {
		Validator res;
		
		Map<String, Object> oldValues = new HashMap<String, Object>();
		Map<String, Object> newValues = new HashMap<String, Object>();

		oldValues.put("name", "john");
		oldValues.put("age", 30);
		
		newValues.put("name", "mike");
		newValues.put("age", 40);
		
		res = ReflectionUtil.updateObjectProps(person, oldValues, newValues);
	
		assertTrue(res.isValid());
		assertEquals(newValues.get("name"), person.getName());
		assertEquals(newValues.get("age"), person.getAge());
	}
	class Person {

		private String name;
		private Integer age;

		public Person(String name, Integer age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
	}
}
