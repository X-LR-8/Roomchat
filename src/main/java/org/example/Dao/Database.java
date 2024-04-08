package org.example.Dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.Addclasses.Room;

import java.util.List;

public class Database {
    private static Database instance;
    public static Database getInstance(){
        if(instance == null){
            instance=new Database();
        }
        return instance;
    }
    public Database(){
    }
    public List<Room> read() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persis");
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Room> criteriaQuery = criteriaBuilder.createQuery(Room.class);
        Root<Room> root = criteriaQuery.from(Room.class);
        criteriaQuery.select(root);
        TypedQuery<Room> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Room> list = typedQuery.getResultList();
        return list;
    }

    public void write(Room room) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persis");
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(room);
        entityManager.getTransaction().commit();
        entityManager.close();
        factory.close();
    }
}
