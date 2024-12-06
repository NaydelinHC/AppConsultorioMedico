package com.example.appconsultorios.repository;

import com.example.appconsultorios.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class DoctorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = Logger.getLogger(DoctorRepository.class.getName());

    public Doctor findById(int id) {
        String sql = "SELECT * FROM DOCTOR WHERE ID = ?";
        logger.info("Executing query: " + sql + " with ID: " + id);

        List<Doctor> doctors = jdbcTemplate.query(sql, new Object[]{id}, doctorRowMapper());
        if (doctors.isEmpty()) {
            logger.warning("No doctor found with ID: " + id);
            return null;
        } else {
            logger.info("Doctor found: " + doctors.get(0));
            return doctors.get(0);
        }
    }

    private RowMapper<Doctor> doctorRowMapper() {
        return new RowMapper<Doctor>() {
            @Override
            public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("ID"));
                doctor.setNombre(rs.getString("NOMBRE"));
                doctor.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
                doctor.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
                doctor.setEspecialidad(rs.getString("ESPECIALIDAD"));
                return doctor;
            }
        };
    }
}