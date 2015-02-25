package models;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleRenderer;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.util.*;

/**
 * Created by larakellett on 2/22/15.
 */
public class Exercise {

    public String name;
    public String description;
    public Integer calories;
    public String injuries;
    public String equipment;
    public Integer time;
    public String id;
    public Float met;
    public String intensity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replace("_", " ");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getInjuries() {
        return injuries;
    }

    public void setInjuries(String injuries) {
        this.injuries = injuries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Float getMet() {
        return met;
    }

    public void setMet(Float met) {
        this.met = met;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public static List<Exercise> parseOwlResults(Set<OWLNamedIndividual> individuals, OWLOntology ontology, User user) {

        List<Exercise> exercises = new ArrayList<Exercise>();
        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

        if (individuals != null && !individuals.isEmpty()) {
            for (OWLNamedIndividual individual : individuals) {

                Exercise exercise = new Exercise();
                exercise.setId(individual.toStringID());
                exercise.setName(shortFormProvider.getShortForm(individual).replace("_", " "));
                exercise.setIntensity(user.getIntensity().getName());

                String ns = "http://www.semanticweb.org/larakellett/ontologies/2015/1/exercise#";
                OWLDataFactory df = ontology.getOWLOntologyManager().getOWLDataFactory();
                OWLDataProperty description = df.getOWLDataProperty(IRI.create(ns + "description"));
                OWLDataProperty met = df.getOWLDataProperty(IRI.create(ns + "metabolicEquivalent"));
                OWLObjectProperty avoidIfHadInjury = df.getOWLObjectProperty(IRI.create(ns + "avoidIfHadInjury"));
                OWLObjectProperty requiresEquipment = df.getOWLObjectProperty(IRI.create(ns + "requiresEquipment"));


                for (OWLLiteral lit : EntitySearcher.getDataPropertyValues(individual, description, ontology)) {
                    exercise.setDescription(lit.getLiteral());
                }
                for (OWLLiteral lit : EntitySearcher.getDataPropertyValues(individual, met, ontology)) {

                    Float metVal = Float.parseFloat(lit.getLiteral());
                    exercise.setMet(metVal);

                    if(user.time != null) {
                        exercise.setCalories(exercise.getCalories(user, metVal));
                        exercise.setTime(user.time);
                    }
                    else if(user.calories != null) {
                        exercise.setCalories(user.calories);
                        exercise.setTimeCalc(user, metVal);
                    }
                }

                StringBuilder injuries = new StringBuilder();
                int i = 0;
                for (OWLIndividual related : EntitySearcher.getObjectPropertyValues(individual, avoidIfHadInjury, ontology)) {
                    if (i > 0) {
                        injuries.append(", ");
                    }
                    if (related.isNamed()) {
                        injuries.append(shortFormProvider.getShortForm((OWLNamedIndividual) related).replace("_", " "));
                        i++;
                    }
                }
                exercise.setInjuries(injuries.toString());

                StringBuilder equipment = new StringBuilder();
                int j = 0;
                for (OWLIndividual equip : EntitySearcher.getObjectPropertyValues(individual, requiresEquipment, ontology)) {
                    if (j > 0) {
                        equipment.append(", ");
                    }
                    if (equip.isNamed()) {
                        equipment.append(shortFormProvider.getShortForm((OWLNamedIndividual) equip).replace("_", " "));
                        j++;
                    }
                }
                exercise.setEquipment(equipment.toString());

                exercises.add(exercise);
            }
        }

        Collections.sort(exercises, new Comparator<Exercise>() {

            @Override
            public int compare(Exercise e1, Exercise e2) {
                return (e1.met > e2.met) ? -1 : 1;
            }

        });
        return exercises;
    }

    public Integer getCalories(User user, Float met) {

        Double calories = getBMR(user) * met * user.getTime();

        return calories.intValue();
    }

    public void setTimeCalc(User user, Float met) {

        Double timeVal = user.calories/(getBMR(user) * met);

        this.time = timeVal.intValue();
    }

    public Double getBMR(User user) {

        Double bmr = null;

        if (user.getGender().equals("M")) {
            bmr = ((13.75 * user.getWeight()) + (5 * user.getHeight()) - (6.76 * user.getAge()) + 66)/1440;
        } else if (user.getGender().equals("F")) {
            bmr = ((9.56 * user.getWeight()) + (1.85 * user.getHeight()) - (4.68 * user.getAge()) + 655)/1440;
        }
        return bmr;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", injuries='" + injuries + '\'' +
                ", equipment='" + equipment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exercise exercise = (Exercise) o;

        if (id != null ? !id.equals(exercise.id) : exercise.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
