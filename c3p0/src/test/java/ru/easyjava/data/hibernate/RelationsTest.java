package ru.easyjava.data.hibernate.entity;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RelationsTest {
    private SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        try {
            MetadataSources metadataSources = new MetadataSources(registry);
            sessionFactory = metadataSources.buildMetadata().buildSessionFactory();

            //metadataSources.addAnnotatedClass(ru.easyjava.data.hibernate.entity.Address.class);
            //metadataSources.addAnnotatedClassName("ru.easyjava.data.hibernate.entity.Address.class");
            //metadataSources.addResource("classpath:/Address.hbm.xml");

        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        //Transient op
        Operation op = new Operation();

        op.setId(1L);
        op.setAccountId(100500);
        op.setAmount(BigDecimal.TEN);
        op.setTimestamp(ZonedDateTime.now());
        op.setDescription("Test operation");
        op.setOpCode(9000);

        Long opRowId = (Long)session.save(op);
        System.out.println("Saved entity and assigned id: " + opRowId);

        op.setAccountId(9000);

        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void testGreeter() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("from Operation ")
                .list()
                .forEach(System.out::println);
        session.getTransaction().commit();

        session.beginTransaction();
        Operation op = session.get(Operation.class, 1L);
        op.setAccountId(9000);
        session.update(op);
        session.getTransaction().commit();

        session.beginTransaction();
        session.createQuery("from Operation ")
                .list()
                .forEach(System.out::println);
        session.getTransaction().commit();

        session.beginTransaction();
        session.load(Operation.class, 2L);
        System.out.println(session.load(Operation.class, 1L));
        System.out.println(session.get(Operation.class, 1L));
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        Operation opD = session.load(Operation.class, 1L);
        session.getTransaction().commit();
        session.close();

        //Throws exception
        //System.out.println(opD.getDescription());

        session = sessionFactory.openSession();
        session.beginTransaction();
        session.merge(opD);
        session.lock(opD, LockMode.NONE);
        System.out.println(opD.getDescription());
        session.getTransaction().commit();
        session.close();
    }
}
