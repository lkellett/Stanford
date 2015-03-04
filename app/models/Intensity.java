package models;

import java.io.File;
import java.util.*;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;
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
  private Double minimum;
  private Double maximum;

  public Intensity(IRI iri, String name, Double minimum, Double maximum) {
    this.iri = iri;
    this.name = name;
    this.minimum = minimum;
    this.maximum = maximum;
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

  public Double getMinimum() {
    return minimum;
  }

  public void setMinimum(Double minimum) {
    this.minimum = minimum;
  }

  public Double getMaximum() {
    return maximum;
  }

  public void setMaximum(Double maximum) {
    this.maximum = maximum;
  }

  /**
   *
   */
  public static Map<String, Boolean> makeIntensityMap(OWLOntology ontology, OWLReasoner reasoner, OWLDataFactory factory) {
    Map<String, Boolean> intensityMap = new TreeMap<String, Boolean>();

    String ns = "http://www.semanticweb.org/larakellett/ontologies/2015/1/exercise#";
    IRI iri = IRI
            .create(ns + "Intensity");

    OWLDataFactory df = ontology.getOWLOntologyManager().getOWLDataFactory();
    OWLDataProperty min = df.getOWLDataProperty(IRI.create(ns + "minimumMetabolicEquivalent"));
    OWLDataProperty max = df.getOWLDataProperty(IRI.create(ns + "maximumMetabolicEquivalent"));

    OWLClass intensity = factory.getOWLClass(iri);
    NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(
            intensity, true);

    Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();


    for (OWLNamedIndividual ind : individuals) {

      String fragment = ind.getIRI().getFragment();
      String name = fragment.replace("_", " ");

      Double minVal = null;
      Double maxVal = null;
      for (OWLLiteral lit : EntitySearcher.getDataPropertyValues(ind, min, ontology)) {
        minVal = Double.parseDouble(lit.getLiteral());
      }
      for (OWLLiteral lit : EntitySearcher.getDataPropertyValues(ind, max, ontology)) {
        maxVal = Double.parseDouble(lit.getLiteral());
      }

      Intensity intensity1 = new Intensity(ind.getIRI(), name, minVal, maxVal);
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

  public static Intensity findIntensity(float metVal) {
    for (Intensity intensity : allIntensity) {
      if (intensity.getMinimum() <= metVal && intensity.getMaximum() >= metVal) {
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
