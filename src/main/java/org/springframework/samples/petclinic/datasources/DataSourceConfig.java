/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.datasources;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.util.List;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@Configuration
public class DataSourceConfig {

	@Value("${datasource.primary.platform}")
	String primaryDSPlatform;

	@Value("${datasource.primary.url}")
	String primaryDSUrl;

	@Value("${datasource.primary.driver}")
	String primaryDSDriver;

	@Value("${datasource.primary.username}")
	String primaryDSUsername;

	@Value("${datasource.primary.password}")
	String primaryDSPassword;

	@Value("${datasource.primary.data}")
	String primaryDSData;

	@Value("${datasource.primary.schema}")
	String primaryDSSchema;

	@Value("${datasource.secondary.platform}")
	String secondaryDSPlatform;

	@Value("${datasource.secondary.url}")
	String secondaryDSUrl;

	@Value("${datasource.secondary.driver}")
	String secondaryDSDriver;

	@Value("${datasource.secondary.data}")
	String secondaryDSData;

	@Value("${datasource.secondary.schema}")
	String secondaryDSSchema;

	@Primary
	@Bean
	public DataSource getDataSource(@Qualifier("first") DataSourceProperties first,
			@Qualifier("second") DataSourceProperties second) {
		final DataSource firstDataSource = first.initializeDataSourceBuilder().build();
		final DataSource secondDataSource = second.initializeDataSourceBuilder().build();
		try {
			System.out.println("Using MySQL DB");
			firstDataSource.getConnection();
			return firstDataSource;
		}
		catch (Exception e) {
			System.out.println("Failed to connect to MySQL, switching to H2 DB");
			return secondDataSource;
		}
	}

	@Primary
	@Bean("first")
	public DataSourceProperties primaryDataSource() {
		final DataSourceProperties dataSourceProperties = new DataSourceProperties();
		dataSourceProperties.setPlatform(primaryDSPlatform);
		dataSourceProperties.setUrl(primaryDSUrl);
		dataSourceProperties.setDriverClassName(primaryDSDriver);
		dataSourceProperties.setUsername(primaryDSUsername);
		dataSourceProperties.setPassword(primaryDSPassword);
		dataSourceProperties.setData(List.of(primaryDSData));
		dataSourceProperties.setSchema(List.of(primaryDSSchema));
		dataSourceProperties.setInitializationMode(DataSourceInitializationMode.ALWAYS);
		return dataSourceProperties;
	}

	@Bean("second")
	public DataSourceProperties secondaryDataSource() {
		final DataSourceProperties dataSourceProperties = new DataSourceProperties();
		dataSourceProperties.setPlatform(secondaryDSPlatform);
		dataSourceProperties.setUrl(secondaryDSUrl);
		dataSourceProperties.setDriverClassName(secondaryDSDriver);
		dataSourceProperties.setData(List.of(secondaryDSData));
		dataSourceProperties.setSchema(List.of(secondaryDSSchema));
		return dataSourceProperties;
	}

}
