package models;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import views.formdata.UserFormData;

import java.io.File;
import java.util.*;

/**
 * Represent a student's hobbies.
 * This class includes:
 * <ul>
 * <li> The model structure (fields, plus getters and setters).
 * <li> Some methods to facilitate form display and manipulation (makeHobbyMap, etc.).
 * <li> Some fields and methods to "fake" a database of Hobbies.
 * </ul>
 */
public class Equipment {
  private IRI iri;
  private String name;

  public Equipment(IRI iri, String name) {
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
  public static Map<String, Boolean> makeEquipmentMap(OWLReasoner reasoner, OWLDataFactory factory) {
    Map<String, Boolean> equipmentMap = new TreeMap<String, Boolean>();

    IRI iri = IRI
            .create("http://www.semanticweb.org/larakellett/ontologies/2015/1/exercise#Equipment");


    OWLClass equipment = factory.getOWLClass(iri);
    NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(
            equipment, true);

    Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();


    for (OWLNamedIndividual ind : individuals) {

      String fragment = ind.getIRI().getFragment();
      String name = fragment.replace("_", " ");

      if(!name.equals("None")) {
        Equipment equipment1 = new Equipment(ind.getIRI(), name);
        allEquipment.add(equipment1);

        equipmentMap.put(name, false);
      }
    }
    return equipmentMap;
  }

  /**
   *
   */
  public static Equipment findEquipment(String equipmentName) {
    for (Equipment equipment : allEquipment) {
      if (equipmentName.equals(equipment.getName())) {
        return equipment;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return String.format("[Equipment %s]", this.name);
  }

  private static List<Equipment> allEquipment = new ArrayList<Equipment>();

}
