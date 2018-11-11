package ch.emf.info.play.dao;

import ch.emf.dao.JpaDaoAPI;
import com.google.inject.ImplementedBy;

/**
 * Interface définissant la méthode pour récupérer la couche dao.
 * Permet l'injection par Guice de cette couche dao. 
 *
 * @author Jean-Claude Stritt 
 */
@ImplementedBy(DaoRepository.class) 
public interface DaoRepositoryItf {
  
  public JpaDaoAPI getDao();  

}
