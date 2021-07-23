/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Mark Paluch
 */
@Testcontainers
public class DummyTests {

	// will be started before and stopped after each test method
	@Container
	private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:10.17")
			.withDatabaseName("foo")
			.withUsername("foo")
			.withPassword("secret");

	@Test
	void name() {


		PostgresqlConnectionConfiguration config = PostgresqlConnectionConfiguration
				.builder().host(postgresqlContainer.getHost())
				.port(postgresqlContainer.getFirstMappedPort())
				.username(postgresqlContainer.getUsername())
				.password(postgresqlContainer.getPassword())
				.database(postgresqlContainer.getDatabaseName()).build();

		PostgresqlConnectionFactory pcf = new PostgresqlConnectionFactory(config);

		PostgresqlConnection connection = pcf.create().block();

		System.out.println("----------------------------------");


		System.out.println(connection.createStatement("SELECT 1").execute()
				.flatMap(it -> it.map((row, rowMetadata) -> row.get(0))).blockLast());


		System.out.println("----------------------------------");

		connection.close().block();

	}
}
