package ch.emf.info.playdao;

import ch.emf.dao.JpaDaoAPI;
import ch.emf.dao.conn.Connectable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javax.persistence.EntityManager;
import play.db.jpa.JPAApi;

/**
 * Dépôt pour mémoriser un objet JPAAPi de Play injecté en même temps que la
 * couche dao. Un objet de connexion est ensuite créé et passé à la couche dao.
 * 
 * @author jcstritt
 */
@Singleton
public class DaoRepository implements DaoRepositoryItf {
  private final JPAApi jpaApi;
  private final DatabaseExecutionContext executionContext;
  private final JpaDaoAPI dao;
  
  @Inject
  public DaoRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext, JpaDaoAPI dao) {
    this.jpaApi = jpaApi;
    this.executionContext = executionContext;
    this.dao = dao;
    
    // nécessaire pour créer un objet "connection" avec les données injectées de Play
    jpaApi.withTransaction(() -> {
      Connectable conn = new ConnectWithPlay(jpaApi, executionContext);
      EntityManager em = jpaApi.em();
      dao.setConnection(conn);
    });
  }
  
  @Override
  public JpaDaoAPI getDao() {
    return dao;
  }

}
