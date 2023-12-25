package controller;

import com.EmployeeData.Model.model;
import Repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    public ResponseEntity<List<model>> getAllEmployees(@RequestParam(required = false) String name) {
        try {
            List<model> employees = name == null ?
                    employeeRepository.findAll() :
                    employeeRepository.findByNameContaining(name);

            return employees.isEmpty() ?
                    new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                    new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<model> getEmployeeById(@PathVariable("id") int id) {
        Optional<model> employeeData = employeeRepository.findById((long) id);

        return employeeData.map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<model> createEmployee(@RequestBody model employee) {
        try {
            model _employee = employeeRepository.save(new model(employee.getName(), employee.getRole(), employee.getSalary()));
            return new ResponseEntity<>(_employee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<model> updateEmployee(@PathVariable("id") int id, @RequestBody model employee) {
        Optional<model> employeeData = employeeRepository.findById((long) id);

        if (employeeData.isPresent()) {
            model _employee = employeeData.get();
            _employee.setName(employee.getName());
            _employee.setRole(employee.getRole());
            _employee.setSalary(employee.getSalary());
            return new ResponseEntity<>(employeeRepository.save(_employee), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("id") int id) {
        try {
            employeeRepository.deleteById((long) id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllEmployees() {
        try {
            employeeRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
