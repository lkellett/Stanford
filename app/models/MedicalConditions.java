package models;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

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
    public String priority;

    public MedicalConditions(IRI iri, String name, String warning, String priority) {
        this.iri = iri;
        this.name = name;
        this.warning = warning;
        this.priority = priority;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
        OWLObjectProperty hasPriority = df.getOWLObjectProperty(IRI.create(ns + "hasPriority"));

        OWLClass intensity = factory.getOWLClass(iri);
        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(
                intensity, true);

        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();

        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();


        for (OWLNamedIndividual ind : individuals) {

            String fragment = ind.getIRI().getFragment();
            String name = fragment.replace("_", " ");
            String priorityVal = null;

            String warningText = null;
            for (OWLLiteral lit : EntitySearcher.getDataPropertyValues(ind, warning, ontology)) {
                warningText = lit.getLiteral();
            }
            for (OWLIndividual related : EntitySearcher.getObjectPropertyValues(ind, hasPriority, ontology)) {
                if (related.isNamed()) {
                    priorityVal = shortFormProvider.getShortForm((OWLNamedIndividual) related);
                }
            }
            MedicalConditions medical1 = new MedicalConditions(ind.getIRI(), name, warningText, priorityVal);
            allMedicalCondition.add(medical1);

            medicalConditionMap.put(name, false);


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
