package internship.service;

import internship.dao.StudentDAO;
import internship.entity.Student;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StudentService {

    @Inject
    private StudentDAO studentDAO;

    public Student createStudent(Student student) {
        if (studentDAO.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un étudiant avec cet email existe déjà");
        }
        return studentDAO.save(student);
    }

    public Optional<Student> getStudentById(Long id) {
        return studentDAO.findById(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Student existing = studentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable"));

        existing.setFirstName(updatedStudent.getFirstName());
        existing.setLastName(updatedStudent.getLastName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setPromotion(updatedStudent.getPromotion());

        return studentDAO.save(existing);
    }

    public void deleteStudent(Long id) {
        studentDAO.deleteById(id);
    }
}
