# DatabaseActions

@**since**: 1.0 > @**api-version**: 2.0 > **Dependencies**: Hibernate | Commons DBCP2

Files:

* **API Interface** org.europa.together.business.[DatabaseActions](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/DatabaseActions.java)
* **Domain Object**  org.europa.together.domain.[JdbcConnection](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/domain/JdbcConnection.java)
* **Implementation** org.europa.together.application.[JdbcActions](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/JdbcActions.java)
* **Configuration** org.europa.together.configuration.[jdbc.properties](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/resources-filtered/org/europa/together/configuration/jdbc.properties)

---

DatabaseActions implements a direct JDBC access to the configured DBMS. This allows to send direct SQL statements out of the DAO. The connection pool DBCP2 holds the database connection alive and optimize the performance of the whole application.  Commons DBCP2 is the same connection pool which is used in the configuration for the DAO.

Configuration Items: [see here for all configuration values](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/filters/database.properties)
* jdbc.schema = together
* jdbc.user = together
* jdbc.password = together
* hibernate.timezone = UTC
* hibernate.hbm2ddl.auto = update
* hibernate.show_sql = false
* dbcp.initialSize = 10
* hibernate.dialect.database = org.hibernate.dialect.PostgreSQL95Dialect
* jdbc.driverClassName = org.postgresql.Driver
* jdbc.url = jdbc:postgresql://172.18.0.2:5432/together-test

**Sample:**

```java
DatabaseActions dbms = new JdbcActions();
// establish connection
dbms.connect("jdbc.properties");

// execute a simple SQL statement
dbms.executeQuery("DROP TABLE IF EXISTS test;");

// execute a SQL file from classpath
dbms.executeSqlFromClasspath("path/to/file/import.sql");

// get DBMS connection information
JdbcConnection metaData = dbms.getJdbcMetaData();
System.out.println(metaData.toString());
```

The DatabaseActions are very helpful by dealing with test cases. It is possible to create a connection and execute SQL scripts to populate the database tables with test values. Inside the package is placed a file called [jdbc.properties](https://github.com/ElmarDott/TP-CORE/blob/master/src/main/resources-filtered/org/europa/together/configuration/jdbc.properties) which is configured as fall back when the property file for connect("jdbc.properties") is an empty String or set to test. The pre configured Database (PostgreSQL) connection is: **user**=together, **password**=together and **schema**=togehter-test.

**Usage in test cases**:

```java
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class JUnit5Test {

    private static DatabaseActions jdbcActions = new JdbcActions();

    private final String SQL_FILE
            = "org/europa/together/sql/test.sql";
    private final String sql_drop = "DROP TABLE IF EXISTS test;";

    @BeforeAll
    static void setUp() {
        //only when a JDBC connection is established the test suite will executed
        Assumptions.assumeTrue(jdbcActions.connect("test"));
    }

    @AfterEach
    void testCaseTermination() {
        //clean up database after test done
        jdbcActions.executeQuery(FLUSH_TABLE);
    }

    @Test
    void foo() {
        // populate database with sql script
        jdbcActions.executeSqlFromClasspath(SQL_FILE);

        do something ...
    }
}
```

