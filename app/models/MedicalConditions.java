package models;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;

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
public class MedicalConditions {

    private IRI iri;
    private String name;
    public String warning;

    public MedicalConditions(IRI iri, String name, String warning) {
        this.iri = iri;
        this.name = name;
        this.warning = warning;
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

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    /**
     *
     */
    public static Map<String, Boolean> makeMedicalConditionMap(OWLOntology ontology, OWLReasoner reasoner, OWLDataFactory factory) {
        Map<String, Boolean> medicalConditionMap = new HashMap<String, Boolean>();

        String ns = "http://www.semanticweb.org/larakellett/ontologies/2015/1/exercise#";
        IRI iri = IRI
                .create(ns + "Medical_Conditions");

        OWLDataFactory df = ontology.getOWLOntologyManager().getOWLDataFactory();
        OWLDataProperty warning = df.getOWLDataProperty(IRI.create(ns + "warning"));

        OWLClass intensity = factory.getOWLClass(iri);
        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(
                intensity, true);

        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();


        for (OWLNamedIndividual ind : individuals) {

            String fragment = ind.getIRI().getFragment();
            String name = fragment.replace("_", " ");

            String warningText = null;
            for (OWLLiteral lit : EntitySearcher.getDataPropertyValues(ind, warning, ontology)) {
                warningText = lit.getLiteral();

                MedicalConditions medical1 = new MedicalConditions(ind.getIRI(), name, warningText);
                allMedicalCondition.add(medical1);

                medicalConditionMap.put(name, false);
            }
        }
        return medicalConditionMap;
    }

    /**
     *
     */

    public static MedicalConditions findMedicalCondition(String condition) {
        for (MedicalConditions medical : allMedicalCondition) {
            if (condition.equals(medical.getName())) {
                return medical;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("[Medical Condition %s]", this.name);
    }

    private static List<MedicalConditions> allMedicalCondition = new ArrayList<MedicalConditions>();

}
