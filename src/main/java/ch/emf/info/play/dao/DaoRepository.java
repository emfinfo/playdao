package ch.emf.info.play.dao;

import ch.emf.dao.JpaDaoAPI;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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
  private final DatabaseExecutionContext dbCtx;
  private final JpaDaoAPI dao;

  @Inject
  public DaoRepository(JPAApi jpaApi, DatabaseExecutionContext dbCtx, JpaDaoAPI dao) {
    this.jpaApi = jpaApi;
    this.dbCtx = dbCtx;
    this.dao = dao;

    // nécessaire pour créer un objet "connection" avec les données injectées de Play
    jpaApi.withTransaction(() -> {
      this.dao.setConnection(new ConnectWithPlay(jpaApi, dbCtx));
    });
  }

  @Override
  public JpaDaoAPI getDao() {
    return dao;
  }

}
