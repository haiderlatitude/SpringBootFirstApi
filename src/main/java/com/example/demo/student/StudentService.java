package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents () {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());

        if (studentOptional.isPresent()) {
            throw new IllegalStateException("The email is already taken");
        }

        studentRepository.save(student);
    }

    @Transactional
    public void updateStudent(Long id, String name, String email) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("The student with this id does not exist!"));

        if (name != null) {
            student.setName(name);
        }

        if (email != null && !email.equals(student.getEmail())) {
            if (studentRepository.findStudentByEmail(email).isPresent()) {
                throw new IllegalStateException("The email is already taken");
            }

            student.setEmail(email);
        }
    }

    public void deleteStudent(Long id) {
        boolean exists = studentRepository.existsById(id);

        if (!exists) {
            throw new IllegalStateException("The student with this id does not exist");
        }

        studentRepository.deleteById(id);
    }
}
