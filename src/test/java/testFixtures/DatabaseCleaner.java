package testFixtures;

import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

/**
 * 데이터베이스 리셋
 *
 * @author paul
 * @since 2022-12-13
 */
@Component
public class DatabaseCleaner implements InitializingBean {

    private final static String REFERENTIAL_INTEGRITY_SQL = "SET REFERENTIAL_INTEGRITY";
    private final static String TRUNCATE_TABLE_SQL = "TRUNCATE TABLE";

    @PersistenceContext
    protected EntityManager entityManager;

    private Set<String> tableNames;

    @Override
    public void afterPropertiesSet() throws Exception {
        final var tableSet = new HashSet<String>();
        entityManager.unwrap(Session.class)
                .doWork(connection -> {
                    //테이블 꺼내기
                    final String[] types = {"TABLE"};
                    ResultSet tables = connection.getMetaData().getTables(connection.getCatalog(), null, "%", types);

                    while (tables.next()) {
                        tableSet.add(tables.getString("table_name"));
                    }

                });
        tableNames = tableSet;
    }

    @Transactional
    public void cleanUp() {
        entityManager.unwrap(Session.class)
                .doWork(connection -> {
                    entityManager.flush();
                    final var statement = connection.createStatement();
                    statement.executeUpdate(String.format("%s %s", REFERENTIAL_INTEGRITY_SQL, "FALSE"));
                    for (final String tableName : tableNames) {
                        statement.executeUpdate(String.format("%s %s", TRUNCATE_TABLE_SQL, tableName));
                    }
                    statement.executeUpdate(String.format("%s %s", REFERENTIAL_INTEGRITY_SQL, "TRUE"));
                });
    }
}
