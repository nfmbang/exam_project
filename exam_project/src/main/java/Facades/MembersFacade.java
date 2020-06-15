/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import Entities.Members;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Niels
 */
public class MembersFacade {

    private static MembersFacade instance;
    private static EntityManagerFactory emf;

    private MembersFacade() {

    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MembersFacade getMembersFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MembersFacade();
        }
        return instance;
    }

    public List<Members> getAll() {
        EntityManager em = emf.createEntityManager();
        return em.createNamedQuery("Members.getAll").getResultList();
    }

    public Members getMemberBysId(String sId) {
        EntityManager em = emf.createEntityManager();
        return em.createNamedQuery("Members.getBysId", Members.class).setParameter("sId", sId).getSingleResult();
    }

    private Boolean isPopulated() {
        EntityManager em = emf.createEntityManager();
        return em.find(Members.class, (long) 1) != null;
    }

    public void populate() {
        EntityManager em = emf.createEntityManager();
        if (isPopulated()) {
            return;
        }
        Members A, B, C;
        A = new Members("cph-nb168", "Niels B", "Rød");
        B = new Members("cph-mn521", "Martin W", "Rød");
        C = new Members("cph-jh409", "Jonatan H", "Gul-Rød");

        try {
            em.getTransaction().begin();
            em.persist(A);
            em.persist(B);
            em.persist(C);

            em.getTransaction().commit();

        } finally {
            em.close();
        }

    }

    public long getMembersCount() {
        EntityManager em = emf.createEntityManager();
        return em.createNamedQuery("Members.count", Long.class).getSingleResult();
    }
}
