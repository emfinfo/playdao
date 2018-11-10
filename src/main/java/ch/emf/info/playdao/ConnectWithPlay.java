package ch.emf.info.playdao;

import ch.emf.dao.conn.Connectable;
import ch.emf.dao.helpers.Logger;
import ch.emf.dao.transactions.Transaction;
import javax.persistence.EntityManager;
import play.db.jpa.JPAApi;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Http.Session;

/**
 * Connect with Play framework.
 *
 * @author jcstritt
 */
public class ConnectWithPlay implements Connectable {
  private final JPAApi jpaApi;
  private final DatabaseExecutionContext executionContext;

  public ConnectWithPlay(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
    this.jpaApi = jpaApi;
    this.executionContext = executionContext;
  }

  private int stringToInt(String sValue) {
    int value = 0;
    try {
      value = Integer.parseInt(sValue);
    } catch (NumberFormatException ex) {
    }
    return value;
  }

  private int[] getTenantsIds() {
    int t[] = {0, 0};
    try {
      Context ctx = Http.Context.current();
      Session session = ctx.session();
      if (session != null) {
        t[0] = stringToInt(session.get("user-id"));
        t[1] = stringToInt(session.get("db-id"));
      }
    } catch (Exception e) {
    }
    return t;
  }

  @Override
  public boolean isConnected() {
    return (jpaApi.em() != null) && jpaApi.em().isOpen();
  }

  @Override
  public EntityManager getEm() {
    EntityManager em = jpaApi.em();

    // support du multi-tenants
    int t[] = getTenantsIds();
    if (t[0] > 0 && t[1] > 0) {
      em.setProperty("eclipselink.session-name", "multitenant-session-" + t[0] + "-" + t[1]);
      em.setProperty("eclipselink.tenant-id1", "" + t[0]);
      em.setProperty("eclipselink.tenant-id2", "" + t[1]);
    }
    Logger.debug(this.getClass(), em);
    return em;
  }

  @Override
  public Transaction getTr() {
    Transaction transaction = null;
    if (jpaApi.em() != null) {
      transaction = new Transaction(jpaApi.em().getTransaction());
    }
    return transaction;
  }

  @Override
  public void disconnect() {
  }

}
