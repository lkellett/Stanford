package views.formdata;


import models.MedicalConditions;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Backing class for the user data form.
 * Requirements:
 * <ul>
 * <li> All fields are public, 
 * <li> All fields are of type String or List[String].
 * <li> A public no-arg constructor.
 * <li> A validate() method that returns null or a List[ValidationError].
 * </ul>
 */
public class UserFormData {

  public Integer time;
  public Integer height;
  public Integer weight;
  public Integer age;
  public List<String> equipment = new ArrayList<String>();
  public String gender;
  public String intensity;
  public Integer calories;
  public List<String> exerciseRx;
  public List<String> injuries = new ArrayList<String>();
  public List<String> medicalCondition = new ArrayList<String>();

  /** Required for form instantiation. */
  public UserFormData() {
  }


  /**
   * Validates Form<StudentFormData>.
   * Called automatically in the controller by bindFromRequest().
   * 
   * Validation checks include:
   * <ul>
   * <li> Name must be non-empty.
   * <li> Password must be at least five characters.
   * <li> Hobbies (plural) are optional, but if specified, must exist in database.
   * <li> Grade Level is required and must exist in database.
   * <li> GPA is required and must exist in database.
   * <li> Majors (plural) are optional, but if specified, must exist in database.
   * </ul>
   *
   * @return Null if valid, or a List[ValidationError] if problems found.
   */
  public List<ValidationError> validate() {

    List<ValidationError> errors = new ArrayList<ValidationError>();

    if (age == null || age <= 0) {
        errors.add(new ValidationError("age", "Please fill in the age field"));
    }
    if (age < 18 || age > 65) {
      errors.add(new ValidationError("age", "Sorry in order to calculate calorie estimates this application can only be used for ages 18-65"));
    }
    if (height == null || height <= 0) {
      errors.add(new ValidationError("height", "Please fill in the height field"));
    }
    if (height < 50 || height > 260) {
      errors.add(new ValidationError("height", "Invalid value"));
    }
    if (gender == null || gender.length() == 0) {
      errors.add(new ValidationError("gender", "Please fill in the gender field"));
    }
    if (weight == null || weight <= 0) {
      errors.add(new ValidationError("weight", "Please fill in the weight field"));
    }
    if (intensity == null || intensity.length() == 0) {
      errors.add(new ValidationError("intensity", "Please select an intensity value"));
    }
    if ((time == null || time <= 0) && (calories == null || calories <= 0)) {
      errors.add(new ValidationError("time", "You need to complete one of either the time or calories field"));
      errors.add(new ValidationError("calories", ""));
    }
    if (!(time == null || time <= 0) && !(calories == null || calories <= 0)) {
      errors.add(new ValidationError("time", "Only one of either the time or calories fields can be completed"));
      errors.add(new ValidationError("calories", ""));
    }

    if(errors.size() > 0)
      return errors;

    return null;
  }
}
