package org.etd.generate.code.plugin.converter;

/**
 * 转换器
 */
public interface BaseConverter<T, R> {


    R convert(T entity);
}
