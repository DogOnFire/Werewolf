package com.dogonfire.werewolf;

import java.lang.reflect.Field;

public class FieldSetter
{
	public static Object a(Class<? extends Object> c, Object object, String fieldName)
	{
		try
		{
			Field field = c.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Object a(Object object, String fieldName)
	{
		return a(object.getClass(), object, fieldName);
	}

	public static void a(Class<? extends Object> c, Object object, String fieldName, Object value)
	{
		try
		{
			Field field = c.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void a(Object object, String fieldName, Object value)
	{
		a(object.getClass(), object, fieldName, value);
	}

	public static void b(Object object, String fieldName, Object value)
	{
		try
		{
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);

			field.set(object, value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void b(Class<? extends Object> c, Object object, String fieldName, Object value)
	{
		try
		{
			Field field = c.getDeclaredField(fieldName);
			field.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);

			field.set(object, value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
