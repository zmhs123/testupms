package cn.tech.wings.cloud.common.datasource.config;

import cn.tech.wings.cloud.common.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.PropertySource;

/**
 * @author cloud
 * @date 2022/8/8
 *
 * 注入SQL 格式化的插件
 */
@ConditionalOnClass(name = "cn.tech.wings.cloud.common.data.mybatis.DruidSqlLogFilter")
@PropertySource(value = "classpath:dynamic-ds-log.yaml", factory = YamlPropertySourceFactory.class)
public class DynamicLogConfiguration {

}
