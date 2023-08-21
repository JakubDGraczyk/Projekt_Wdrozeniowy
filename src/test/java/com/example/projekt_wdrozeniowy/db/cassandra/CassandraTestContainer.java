package com.example.projekt_wdrozeniowy.db.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class CassandraTestContainer {
    private static final String KEYSPACE_NAME = "test";

    @Container
    private static final CassandraContainer<?> cassandra = new CassandraContainer<>("cassandra:4.1.3")
            .withExposedPorts(9042);

    @BeforeAll
    static void setupCassandraConnectionProperties() {
        System.setProperty("spring.data.cassandra.keyspace-name", KEYSPACE_NAME);
        System.setProperty("spring.data.cassandra.contact-points", cassandra.getContainerIpAddress());
        System.setProperty("spring.data.cassandra.port", String.valueOf(cassandra.getFirstMappedPort()));

        createKeyspace(cassandra.getCluster());
    }

    static void createKeyspace(Cluster cluster) {
        try (Session session = cluster.connect()) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE_NAME + " WITH replication = \n" +
                    "{'class':'SimpleStrategy','replication_factor':'1'};");
        }
    }

    @Test
    void givenCassandraContainer_whenSpringContextIsBootstrapped_thenContainerIsRunningWithNoExceptions() {
        Session session = cassandra.getCluster().connect();
        session.execute("CREATE TABLE " + KEYSPACE_NAME + ".article(" +
                "id int primary key," +
                "name text" +
                ");");
        session.execute("INSERT INTO test.article(id,name)values(1,'Kuba');");
        ResultSet result = session.execute("SELECT * FROM " + KEYSPACE_NAME + ".article");
        assertThat(result.iterator().next().getString("name")).isEqualTo("Kuba");
    }
}