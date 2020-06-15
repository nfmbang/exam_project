/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.Car;
import dto.CarDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import util.EMF_Creator;

/**
 *
 * @author jonab
 */
public class CarFacade {

    private static CarFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CarFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static CarFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CarFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void addCar(Car car) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(car);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List getAllCars() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Car> query = em.createQuery("Select c from Car c", Car.class);
//            List<CarDTO> cars = new ArrayList();
//            for (int i = 0; i < query.getResultList().size(); i++) {
//                cars.add(new CarDTO(query.getResultList().get(i)));
//            }
//            return cars;
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List convertCarsToDTO(List<Car> cars) {
        List<CarDTO> carsDTO = new ArrayList();
        for (int i = 0; i < cars.size(); i++) {
            carsDTO.add(new CarDTO(cars.get(i)));

        }
        return carsDTO;
    }

    public void populate() {
        CarFacade cf = getFacade(EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE));
        cf.addCar(new Car(2000, "VW", "Golf", 10000, "Angela Merkel", "am11111"));
        cf.addCar(new Car(2008, "Ford", "Ka", 15000, "Donald Trump", "dt22222"));
        cf.addCar(new Car(2017, "Audi", "RS7", 800000, "Kim Jung Un", "kj33333"));
    }

//    public static void main(String[] args) {
//        CarFacade cf = getFacade(EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE));
//        cf.addCar(new Car(2000, "VW", "Golf", 10000, "Angela Merkel", "am11111"));
//        cf.addCar(new Car(2008, "Ford", "Ka", 15000, "Donald Trump", "dt22222"));
//        cf.addCar(new Car(2017, "Audi", "RS7", 800000, "Kim Jung Un", "kj33333"));
//
////cf.addCar(new Car(1999, "Ford", "Ka", 5000, "Peter", "AK47566"));
//        //cf.addCar(new Car(2002, "Audi", "A6", 60000, "Ib", "SK44320"));
//    }
}
