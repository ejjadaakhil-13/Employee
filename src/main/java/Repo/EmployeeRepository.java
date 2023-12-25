package Repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EmployeeData.Model.model;


@Repository
public interface EmployeeRepository extends JpaRepository<model, Long>{
	
	List<model> findByNameContaining(String name);
	}

