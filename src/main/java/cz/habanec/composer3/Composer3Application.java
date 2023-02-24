package cz.habanec.composer3;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

@SpringBootApplication
@RequiredArgsConstructor
public class Composer3Application implements CommandLineRunner {


	private final DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(Composer3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Running CL-runner");
		execSql("db/migration/migrate-modi.sql");
		execSql("db/migration/migrate-quint-circle.sql");
	}

	private void execSql(String... sqlFile) {
		try (Connection connection = dataSource.getConnection()) {
			Arrays.stream(sqlFile).forEach(file -> {
				Resource resource = new ClassPathResource(file);
				try {
					ScriptUtils.executeSqlScript(connection, resource);
				} catch (ScriptException e) {
					throw new RuntimeException("Failed to execute script '" + resource.getFilename() + "'.", e);
				}
			});

		} catch (SQLException e) {
			throw new RuntimeException("No connection acquired.");
		}
	}
}
