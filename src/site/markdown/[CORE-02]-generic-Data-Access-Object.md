# GenericDAO

@**since**: 1.0 > @**api-version**: 1.4 > **Dependencies**:  Hibernate | Commons DBCP2 | Spring

Files:

* **API Interface** org.europa.together.business.[GenericDAO](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/GenericDAO.java)
* **DomainObject**  org.europa.together.domain.[JpaPagination](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/domain/JpaPagination.java)
* **Implementation** org.europa.together.application.[GenericHbmDAO](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/GenericHbmDAO.java)
* **Configuration** org.europa.together.configuration.[spring-dao.xml](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/resources-filtered/org/europa/together/configuration/spring-dao.xml)
* **Exception** org.europa.together.exceptions.[DAOException](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/exceptions/DAOException.java)

---

This implementation of a Data Access Object (DAO) is generic and is easy to extend by inheritance. The DAO organize all basic database operations CRUD (Create, Read, Update and Delete) for their Domain Objects (DO). Also serialization and deserialization as JSON Objects for interoperable data exchange are available.

![GenericDAO UML](https://github.com/ElmarDott/TP-CORE/wiki/images/genericDAO.png)

The concrete implementation of the GenericDAO is based on the Java Persistence API (JPA) provided by Hibernate (HBM). The GenericDAO is designed for transaction management (Spring JPA Transaction Manager). To reach a better performance, a connection pool is used. The connection pool library is Apache Commons DBCP2 and configured within the Spring context. Bean Validation is supported by the Hibernate Validator. JSON Processing is supported by Jackson. The following list gives a brief overview about Hibernate and JPA version compatibility.

        HBM 3.2 + | JPA 1.0 | JSR 220 - 05.2006
        HBM 3.5 + | JPA 2.0 | JSR 317 - 12.2009
        HBM 4.3 + | JPA 2.1 | JSR 338 - 04.2013
        HBM 5.3 + | JPA 2.2 | JSR 338 - 07.2017
        HBM 5.5 + | JPA 2.2 | Jakarta EE 8 - 06.2021
        HBM 6.0 + | JPA 3.0 | Jakarta EE 9 - 2021 (under development)

To use the DAO in your project you need to activate the Spring Context. In Web applications packaged as WAR, this will be done in the WEB-INF/web.xml file. In pure Java Applications you can set in your executable class the context:
```java
ApplicationContext context =
   new ClassPathXmlApplicationContext("/applicationContext.xml");
```
```xml
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>/WEB-INF/spring-dao.xml</param-value>
</context-param>
<listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```
The library comes with a included spring configuration spring-dao.xml for a PostgreSQL DBMS. The configured schemata is **together** with user **together** and the **password** together.

You can manipulate during the compile time the DBMS for your needs. Supported systems are: PostgreSQL, MariaDB, MySQL, Oracle, DB2, H2, SQLlite and Derby. The configuration to change the DB settings you will find in [database.properties](https://github.com/ElmarDott/TP-CORE/blob/master/src/main/filters/database.properties)

**Pagination**: If the listAll() call contains a huge amount of results, the application is in risk to slow down dramatically. This also applies to all methods in the DAOs that have List&lt;Object&gt; as a return value. To secure the application performance an pagination is mandatory. This will reduce the result set to a define amount of objects. The genericDAO use for pagination the high performant seek method.

```java
//Constants
JpaPagination.ORDER_ASC          //default
JpaPagination.ORDER_DESC
JpaPagination.PAGING_FOREWARD    //default
JpaPagination.PAGING_BACKWARD

JpaPagination<DomainObject> paging = new JpaPagination<>("primaryKey");
paging.setPageSize(15);
paging.setPageBreak(new DomainObject(primaryKey));
List<DomainObject> result = DataAccessObject.listAllElements(paging);

//changing the default settings
paging.setSorting(JpaPagination.ORDER_DESC):
paging.setPaging(PAGING_BACKWARD);
```

The result of the previous listing contains maximal 15 elements, with the Domain Object from the page break as first entry, ascending sorted. The sorting of the results is ascending.

**An example of a usage of the DAO can be found in the implementation of: [CORE-05] Application Configuration**

**Sample:**
This abstract sample demonstrate the basic concept how to deal with DAO objects. The DomainObject (DO) is a mapping to an equal database table. Domain Object are located in the package domain and can identified by the @Entity annotation above the Class definition. The DAO interface located in the business package is the API of your functionality supported by your Domain Object. The DAOImpl located in the application package is the implementation of the DAO interface.

```java
package org.europa.together.domain;

@Entity
@Table(name = "DOMAIN_OBJECT")
public class DomainObject implements Serializable {
  @Id
  @Column(name = "IDX")
  private String uuid;
}

package org.europa.together.business;

@Component
public interface ObjectDAO
    extends GenericDAO<DomainObject, String> {
  /** JavaDoc. */
  void daoOpeationA();

  /** JavaDoc. */
  void daoOpertionB();
}

package org.europa.together.application;

@Repository
public class ObjectDAOImpl
    extends GenericHbmDAO<DomainObject, String>
    implements ObjectDAO {

  @Override
  void daoOpeationA() {
    //foo ...
  }

  @Override
  void daoOpeationB() {
    //foo ...
  }
}
```
**Spring Configuration**
The whole configuration of Hibernate is managed by spring JPA. A extra (additional) Hibernate configuration is not needed.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!-- Package needed to be scanned for annotation for @autowired DI-->
    <context:component-scan base-package="org.europa.together">
        <context:include-filter type="annotation"
                                expression="org.springframework.stereotype.Component" />
        <context:include-filter type="annotation"
                                expression="org.springframework.stereotype.Service" />
        <context:include-filter type="annotation"
                                expression="org.springframework.stereotype.Repository" />
        <context:include-filter type="annotation"
                                expression="org.springframework.stereotype.Controller" />
    </context:component-scan>

    <bean id="mainSchemata" class="org.apache.commons.dbcp2.BasicDataSource">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.user}"/>
        <property name="password" value="${jdbc.password}"/>

        <!-- connection pool configuration -->
        <property name="initialSize" value="${dbcp.initialSize}" />
    </bean>

    <!-- ENTITY MANAGER for test Database Schemata -->
    <bean id="mainEntityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="persistenceUnitName" value="defaultPersistenceUnit"/>
        <property name="dataSource" ref="mainSchemata" />
        <property name="packagesToScan">
            <list>
                <value>org.europa.together.domain</value>
            </list>
        </property>

        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
                <property name="generateDdl" value="true" />
            </bean>
        </property>

        <property name="jpaProperties">
            <props>
                <prop key="javax.persistence.query.timeout">1000</prop>
                <prop key="hibernate.dialect">${hibernate.dialect.database}</prop>
                <prop key="hibernate.hbm2ddl.auto">${hibernate.hbm2ddl.auto}</prop>
                <prop key="hibernate.show_sql">${hibernate.show_sql}</prop>
            </props>
        </property>
    </bean>

    <!-- TRANSACTION MANAGER -->
    <bean id="mainTransactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" ref="mainEntityManagerFactory" />
    </bean>

    <bean id="persistenceExceptionTranslationPostProcessor"
          class="org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor"/>

    <tx:annotation-driven transaction-manager="mainTransactionManager"/>
</beans>
```