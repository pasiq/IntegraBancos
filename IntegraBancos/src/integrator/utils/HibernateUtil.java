package integrator.utils;

import integrator.entities.MercadoA;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;


public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
        
    }

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
            	AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration();
                annotationConfiguration.addAnnotatedClass(MercadoA.class);
                
                sessionFactory = annotationConfiguration.configure().buildSessionFactory();

            } catch (Throwable ex) {
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }

            return sessionFactory;

        } else {
            return sessionFactory;
        }
        
    }
}
