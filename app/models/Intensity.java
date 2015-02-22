package models;

import java.io.File;
import java.util.*;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import views.formdata.UserFormData;

/**
 * Represent a Grade Point Average. 
 * This class includes:
 * <ul>
 * <li> The model structure (fields, plus getters and setters).
 * <li> Some methods to facilitate form display and manipulation (makeGPAMap, etc.).
 * <li> Some fields and methods to "fake" a database of GPAs.
 * </ul>
 */
public class Intensity {

  private IRI iri;
  private String name;

  public Intensity(IRI iri, String name) {
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
  public static Map<String, Boolean> makeIntensityMap(OWLReasoner reasoner, OWLDataFactory factory) {
    Map<String, Boolean> intensityMap = new HashMap<String, Boolean>();

    IRI iri = IRI
            .create("http://www.semanticweb.org/larakellett/ontologies/2015/1/exercise#Intensity");


    OWLClass equipment = factory.getOWLClass(iri);
    NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(
            equipment, true);

    Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();


    for (OWLNamedIndividual ind : individuals) {

      String fragment = ind.getIRI().getFragment();
      String name = fragment.replace("_", " ");

      Intensity intensity1 = new Intensity(ind.getIRI(), name);
      allIntensity.add(intensity1);

      intensityMap.put(name, false);
    }
    return intensityMap;
  }

  /**
   *
   */
  public static Intensity findIntensity(String intensityName) {
    for (Intensity intensity : allIntensity) {
      if (intensityName.equals(intensity.getName())) {
        return intensity;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return String.format("[Intensity %s]", this.name);
  }

  private static List<Intensity> allIntensity = new ArrayList<Intensity>();

}
