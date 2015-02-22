package models;

import java.util.ArrayList;
import java.util.List;
import views.formdata.UserFormData;

/**
 *
 *
 */
public class User {
  private Integer time;
  private Integer height;
  private Integer weight;
  private Integer age;
  private List<Equipment> equipment = new ArrayList<Equipment>();
  private String gender;
  private Intensity intensity;
  private List<Injury> injuries = new ArrayList<Injury>();

  /** Model entities typically want to have a no-arg constructor. */
  public User() {
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



  /**
   *
   * @param formData The user form data.
   * @return A user instance.
   */
  public static List<Exercise> findExercise(UserFormData formData) {
    User user = new User();
    user.age = formData.age;
    user.weight = formData.weight;
    user.gender = formData.gender;

    for (String equipment : formData.equipment) {
      user.equipment.add(Equipment.findEquipment(equipment));
    }

    user.intensity = Intensity.findIntensity(formData.intensity);
    for (String injury : formData.injuries) {
      user.injuries.add(Injury.findInjury(injury));
    }

    List<Exercise> recommendations = new ArrayList<Exercise>();


    return recommendations;
  }

}
