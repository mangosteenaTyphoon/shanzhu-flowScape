package com.shanzhu.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Jackson JSON 序列化配置
 * 解决 LocalDateTime 格式问题，支持多种日期时间格式
 */
@Configuration
public class JacksonConfig {

    /**
     * 优先使用的日期时间格式（用于序列化输出）
     */
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期时间格式化器
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    /**
     * ISO 8601 格式化器（用于兼容旧数据）
     */
    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * 配置全局的 ObjectMapper
     * @Primary 注解确保这个 ObjectMapper 是主要的 Bean
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 注册 Java 8 时间模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 配置 LocalDateTime 的序列化器（统一输出格式）
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));

        // 配置支持多种格式的反序列化器
        javaTimeModule.addDeserializer(LocalDateTime.class, new MultiFormatLocalDateTimeDeserializer());

        objectMapper.registerModule(javaTimeModule);

        // 禁用时间戳格式，使用字符串格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 配置反序列化选项
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        return objectMapper;
    }

    /**
     * 自定义 LocalDateTime 反序列化器
     * 支持多种日期时间格式：
     * 1. yyyy-MM-dd HH:mm:ss (空格分隔，新格式)
     * 2. yyyy-MM-dd'T'HH:mm:ss (T分隔，ISO 8601格式，兼容旧数据)
     */
    public static class MultiFormatLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

        public MultiFormatLocalDateTimeDeserializer() {
            super(DATE_TIME_FORMATTER);
        }

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            String dateTimeString = parser.getText();

            if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
                return null;
            }

            // 尝试多种格式解析
            try {
                // 1. 尝试空格分隔格式 yyyy-MM-dd HH:mm:ss
                return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e1) {
                try {
                    // 2. 尝试ISO 8601格式 yyyy-MM-dd'T'HH:mm:ss
                    return LocalDateTime.parse(dateTimeString, ISO_DATE_TIME_FORMATTER);
                } catch (DateTimeParseException e2) {
                    try {
                        // 3. 尝试标准ISO格式（带毫秒）
                        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } catch (DateTimeParseException e3) {
                        // 如果都失败了，抛出原始异常
                        throw new RuntimeException(String.format(
                            "无法解析日期时间字符串: '%s'。支持的格式: '%s', '%s', ISO_LOCAL_DATE_TIME",
                            dateTimeString, DATE_TIME_PATTERN, "yyyy-MM-dd'T'HH:mm:ss"), e1);
                    }
                }
            }
        }
    }
}
