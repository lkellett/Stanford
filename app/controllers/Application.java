package controllers;

import models.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.formdata.UserFormData;
import views.html.index;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The controller for the single page of this application.
 *
 * @author Philip Johnson
 */
public class Application extends Controller {

  /**
   *
   */
  public static Result getIndex() {

    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology ontology = loadOntology(manager);
    OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
    OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
    OWLDataFactory factory = manager.getOWLDataFactory();


    UserFormData userData =  new UserFormData();
    Form<UserFormData> formData = Form.form(UserFormData.class).fill(userData);
    return ok(index.render(
            formData,
            Equipment.makeEquipmentMap(reasoner, factory),
            Intensity.makeIntensityMap(reasoner, factory),
            Injury.makeInjuriesMap(reasoner, factory),
            ExerciseRx.makeExerciseRxMap(reasoner, factory),
            getGenders()
    ));
  }

  /**
   * Process a form submission.
   * First we bind the HTTP POST data to an instance of StudentFormData.
   * The binding process will invoke the StudentFormData.validate() method.
   * If errors are found, re-render the page, displaying the error data. 
   * If errors not found, render the page with the good data. 
   * @return The index page with the results of validation. 
   */
  public static Result postIndex() {

    // Get the submitted form data from the request object, and run validation.
    Form<UserFormData> formData = Form.form(UserFormData.class).bindFromRequest();

    if (formData.hasErrors()) {
      // Don't call formData.get() when there are errors, pass 'null' to helpers instead. 
      flash("error", "Please correct errors above.");
      return badRequest(index.render(formData,
              null,
              null,
              null,
              null,
              null
      ));
    }
    else {
      // Convert the formData into a Student model instance.
      List<Exercise> exercises = User.findExercise(formData.get());
      flash("success", "Student instance created/edited: " + exercises);
      return ok(index.render(formData,
              null,
              null,
              null,
              null,
              null
      ));
    }
  }

  static OWLOntology loadOntology(OWLOntologyManager manager) {

    File file = new File("public/files/exercise.owl");

    try {
      return manager.loadOntologyFromOntologyDocument(file);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return null;
  }

  static List<String> getGenders()
  {
    List<String> gender = new ArrayList<String>();
    gender.add("M");
    gender.add("F");
    return gender;
  }

}
