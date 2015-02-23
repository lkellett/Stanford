package models;

import java.io.File;
import java.util.*;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import views.formdata.UserFormData;

/**
 * Represent a student's majors.
 * This class includes:
 * <ul>
 * <li> The model structure (fields, plus getters and setters).
 * <li> Some methods to facilitate form display and manipulation (makeMajorMap, etc.).
 * <li> Some fields and methods to "fake" a database of Majors.
 * </ul>
 */
public class Injury {
  private IRI iri;
  private String name;

  public Injury(IRI iri, String name) {
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

  public String getIRIName() { return name.replace(" ", "_");}

  /**
   *
   */
  public static Map<String, Boolean> makeInjuriesMap(OWLReasoner reasoner, OWLDataFactory factory) {
    Map<String, Boolean> injuriesMap = new HashMap<String, Boolean>();

    IRI iri = IRI
            .create("http://www.semanticweb.org/larakellett/ontologies/2015/1/exercise#Injuries");

    OWLClass equipment = factory.getOWLClass(iri);
    NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(
            equipment, true);

    Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();


    for (OWLNamedIndividual ind : individuals) {

      String fragment = ind.getIRI().getFragment();
      String name = fragment.replace("_", " ");

      Injury injury1 = new Injury(ind.getIRI(), name);
      allInjuries.add(injury1);

      injuriesMap.put(name, false);
    }
    return injuriesMap;
  }

  /**
   *
   */
  public static Injury findInjury(String injuryName) {
    for (Injury injury : allInjuries) {
      if (injuryName.equals(injury.getName())) {
        return injury;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return String.format("[Injury %s]", this.name);
  }

  private static List<Injury> allInjuries = new ArrayList<Injury>();

}

