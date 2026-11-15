package org.example.util;

import org.example.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory factory = new Configuration()
            .addAnnotatedClass(User.class)
            .buildSessionFactory();

    public static SessionFactory getFactory() {
        return factory;
    }
}
