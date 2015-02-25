package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import views.formdata.UserFormData;

/**
 *
 *
 */
public class User {
    public Integer time;
    private Integer height;
    private Integer weight;
    private Integer age;
    public Integer calories;
    private List<Equipment> equipment = new ArrayList<Equipment>();
    private String gender;
    private Intensity intensity;
    private List<Injury> injuries = new ArrayList<Injury>();
    private List<ExerciseRx> exerciseRx = new ArrayList<ExerciseRx>();
    private List<MedicalConditions> medicalCondition = new ArrayList<MedicalConditions>();

    /**
     * Model entities typically want to have a no-arg constructor.
     */
    public User(UserFormData formData) {
        super();
        this.age = formData.age;
        this.weight = formData.weight;
        this.gender = formData.gender;
        this.height = formData.height;
        this.time = formData.time;
        this.calories = formData.calories;

        if (formData.equipment != null) {
            for (String equipment : formData.equipment) {
                this.equipment.add(Equipment.findEquipment(equipment));
            }
        }

        this.intensity = Intensity.findIntensity(formData.intensity);

        if (formData.injuries != null) {
            for (String injury : formData.injuries) {
                this.injuries.add(Injury.findInjury(injury));
            }
        }

        if (formData.exerciseRx != null) {
            for (String exerciseRx : formData.exerciseRx) {
                this.exerciseRx.add(ExerciseRx.findExerciseRx(exerciseRx));
            }
        }

        if (formData.medicalCondition != null) {
            for (String condition : formData.medicalCondition) {
                this.medicalCondition.add(MedicalConditions.findMedicalCondition(condition));
            }
        }
    }

    public User(){

    }


    public void addEquipment(Equipment equipment) {
        this.equipment.add(equipment);
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the intensity
     */
    public Intensity getIntensity() {
        return intensity;
    }

    /**
     * @param intensity the intensity to set
     */
    public void setIntensity(Intensity intensity) {
        this.intensity = intensity;
    }

    /**
     * @return the injuries
     */
    public List<Injury> getInjuries() {
        return injuries;
    }

    /**
     * @param injuries the injuries to set
     */
    public void setInjuries(List<Injury> injuries) {
        this.injuries = injuries;
    }

    public void addInjury(Injury injury) {
        this.injuries.add(injury);
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public List<ExerciseRx> getExerciseRx() {
        return exerciseRx;
    }

    public void setExerciseRx(List<ExerciseRx> exerciseRx) {
        this.exerciseRx = exerciseRx;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public List<MedicalConditions> getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(List<MedicalConditions> medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    /**
     * @param formData The user form data.
     * @return A user instance.
     */
    public List<Exercise> findExercises(OWLOntology ontology) {

        DLQuery query = new DLQuery(ontology);

        StringBuilder exclusionString = new StringBuilder();
        int i = 0;
        for(Injury injury: injuries)
        {
            if(i > 0)
            {
                exclusionString.append(" or ");
            }
            exclusionString.append("avoidIfHadInjury value " + injury.getIRIName());
            i++;
        }
        Set<OWLNamedIndividual> exclusionInd = query.executeQuery(exclusionString.toString());

        List<Exercise> exclusions = Exercise.parseOwlResults(exclusionInd, ontology, this);

        StringBuilder queryString = new StringBuilder("(requiresEquipment value None");
        for(Equipment equip: equipment)
        {
            queryString.append(" or ");

            queryString.append("requiresEquipment value " + equip.getIRIName());
        }
        queryString.append(") ");

        if(exerciseRx.size() > 0) {
            queryString.append(" and (");
            i = 0;
            for (ExerciseRx exRx : exerciseRx) {
                if (i > 0) {
                    queryString.append(" or ");
                }
                queryString.append("isOfType value " + exRx.getIRIName());
                i++;
            }
            queryString.append(") ");
        }


        queryString.append(" and (metabolicEquivalent some xsd:double[> " + intensity.getMinimum() + "]) and (metabolicEquivalent some xsd:double[< " + intensity.getMaximum() + "])");

        System.out.println(queryString.toString());
        Set<OWLNamedIndividual> individuals = query.executeQuery(queryString.toString());

        List<Exercise> recommendations = Exercise.parseOwlResults(individuals, ontology, this);

        List<Exercise> finalRecommendations = new ArrayList<Exercise>();
        for(Exercise e: recommendations){
            if(!exclusions.contains(e)){
                finalRecommendations.add(e);
            }
        }

        return finalRecommendations;
    }

    @Override
    public String toString() {
        return "User{" +
                "time=" + time +
                ", height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                ", equipment=" + equipment +
                ", gender='" + gender + '\'' +
                ", intensity=" + intensity +
                ", injuries=" + injuries +
                ", exerciseRx=" + exerciseRx +
                '}';
    }
}
