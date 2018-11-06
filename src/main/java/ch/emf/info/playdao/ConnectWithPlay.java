package ch.emf.info.playdao;

import ch.emf.dao.conn.Connectable;
import ch.emf.dao.transactions.Transaction;
import javax.persistence.EntityManager;
import play.db.jpa.JPAApi;

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

  @Override
  public boolean isConnected() {
    return (jpaApi.em() != null) && jpaApi.em().isOpen();
  }

  @Override
  public EntityManager getEm() {
    EntityManager em = jpaApi.em();
//    Logger.debug(this.getClass(), em);
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
