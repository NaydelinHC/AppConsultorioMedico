package com.example.appconsultorios.repository;

import com.example.appconsultorios.model.Cita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CitaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Cita> findAll() {
        return jdbcTemplate.query("SELECT * FROM CITA", new CitaRowMapper());
    }

    public Cita findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM CITA WHERE ID = ?", new CitaRowMapper(), id);
    }

    public void save(Cita cita) {
        String sql = "INSERT INTO CITA (DOCTOR_ID, CONSULTORIO_ID, HORARIO, NOMBRE_PACIENTE) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, cita.getDoctorId(), cita.getConsultorioId(), cita.getHorario(), cita.getNombrePaciente());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM CITA WHERE ID = ?";
        jdbcTemplate.update(sql, id);
    }

    public void update(Cita cita) {
        String sql = "UPDATE CITA SET DOCTOR_ID = ?, CONSULTORIO_ID = ?, HORARIO = ?, NOMBRE_PACIENTE = ? WHERE ID = ?";
        jdbcTemplate.update(sql, cita.getDoctorId(), cita.getConsultorioId(), cita.getHorario(), cita.getNombrePaciente(), cita.getId());
    }

    private static class CitaRowMapper implements RowMapper<Cita> {
        @Override
        public Cita mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cita cita = new Cita();
            cita.setId(rs.getInt("ID"));
            cita.setDoctorId(rs.getInt("DOCTOR_ID"));
            cita.setConsultorioId(rs.getInt("CONSULTORIO_ID"));
            cita.setHorario(rs.getTimestamp("HORARIO").toLocalDateTime());
            cita.setNombrePaciente(rs.getString("NOMBRE_PACIENTE"));
            return cita;
        }
    }
}
