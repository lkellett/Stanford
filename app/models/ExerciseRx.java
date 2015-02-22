package models;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.*;

/**
 * Represent a Grade Point Average. 
 * This class includes:
 * <ul>
 * <li> The model structure (fields, plus getters and setters).
 * <li> Some methods to facilitate form display and manipulation (makeGPAMap, etc.).
 * <li> Some fields and methods to "fake" a database of GPAs.
 * </ul>
 */
public class ExerciseRx {

  private IRI iri;
  private String name;

  public ExerciseRx(IRI iri, String name) {
    this.iri = iri;
    this.name = name;
  }

  public void setIri(IRI iri) {
    this.iri = iri;
  }

  public void setName(String name) {
    this.name = name;
  }

  public IRI getIri() {
    return iri;
  }

  public String getName() {
    return name;
  }

  /**
   *
   */
  public static Map<String, Boolean> makeExerciseRxMap(OWLReasoner reasoner, OWLDataFactory factory) {
    Map<String, Boolean> exerciseRxMap = new HashMap<String, Boolean>();

    IRI iri = IRI
            .create("http://www.semanticweb.org/larakellett/ontologies/2015/1/exercise#Exercise_Rx");


    OWLClass exerciseRx = factory.getOWLClass(iri);
    NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(
            exerciseRx, true);

    Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();


    for (OWLNamedIndividual ind : individuals) {

      String fragment = ind.getIRI().getFragment();
      String name = fragment.replace("_", " ");

      ExerciseRx exerciseRx1 = new ExerciseRx(ind.getIRI(), name);
      allExerciseRx.add(exerciseRx1);

      exerciseRxMap.put(name, false);
    }
    return exerciseRxMap;
  }

  /**
   *
   */
  public static ExerciseRx findExerciseRx(String exerciseRxName) {
    for (ExerciseRx exerciseRx : allExerciseRx) {
      if (exerciseRxName.equals(exerciseRx.getName())) {
        return exerciseRx;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return String.format("[ExerciseRx %s]", this.name);
  }

  private static List<ExerciseRx> allExerciseRx = new ArrayList<ExerciseRx>();

}
