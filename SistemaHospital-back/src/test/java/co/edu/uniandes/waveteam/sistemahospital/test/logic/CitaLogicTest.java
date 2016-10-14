/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.waveteam.sistemahospital.test.logic;

import co.edu.uniandes.waveteam.sistemahospital.api.ICitaLogic;
import co.edu.uniandes.waveteam.sistemahospital.entities.CitaEntity;
import co.edu.uniandes.waveteam.sistemahospital.logic.CitaLogic;
import co.edu.uniandes.waveteam.sistemahospital.persistence.CitaPersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author jm.lizarazo10
 */
@RunWith(Arquillian.class)
public class CitaLogicTest {
    
    
    private PodamFactory factory = new PodamFactoryImpl();

    /**
     * 
     */
    @Inject
    private ICitaLogic citaLogic;

    /**
     * 
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * 
     */
    @Inject
    private UserTransaction utx;
    
    
    /**
     * 
     */
    private List<CitaEntity> data = new ArrayList<CitaEntity>();
    
    /**
     * 
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(CitaEntity.class.getPackage())
                .addPackage(CitaLogic.class.getPackage())
                .addPackage(CitaLogic.class.getPackage())
                .addPackage(CitaPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }
    
    
    /**
     * Configuración inicial de la prueba.
     *
     * 
     */
    @Before
    public void setUp() {
        try {
            utx.begin();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    
    /**
     * Limpia las tablas que están implicadas en la prueba.
     *
     * 
     */
    private void clearData() {
        
        em.createQuery("delete from CitaEntity").executeUpdate();
    }
    
    
    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     *
     * 
     */
    private void insertData() {
        
        
        for (int i = 0; i < 3; i++) {
            CitaEntity entity = factory.manufacturePojo(CitaEntity.class);
            

            em.persist(entity);
            data.add(entity);
        }
    }
    
    
    
    @Test
    public void createCitaTest() {
        CitaEntity newCita = factory.manufacturePojo(CitaEntity.class);
        CitaEntity result = citaLogic.createCita(newCita);
        Assert.assertNotNull(result);
        CitaEntity entity = em.find(CitaEntity.class, result.getId());
        Assert.assertEquals(newCita.getName(), entity.getName());
        Assert.assertEquals(newCita.getId(), entity.getId());
    }
    
    
}
