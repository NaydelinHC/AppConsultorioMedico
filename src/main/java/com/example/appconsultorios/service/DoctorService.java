package com.example.appconsultorios.service;

import com.example.appconsultorios.model.Cita;
import com.example.appconsultorios.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Cita> consultarCitasPorDoctor(int doctorId) {
        String sql = "SELECT * FROM CITAS WHERE DOCTOR_ID = ?";
        return jdbcTemplate.query(sql, new Object[]{doctorId}, (rs, rowNum) -> {
            Cita cita = new Cita();
            cita.setId(rs.getInt("ID"));
            cita.setDoctorId(rs.getInt("DOCTOR_ID"));
            cita.setConsultorioId(rs.getInt("CONSULTORIO_ID"));
            cita.setHorario(rs.getTimestamp("HORARIO").toLocalDateTime());
            cita.setNombrePaciente(rs.getString("NOMBRE_PACIENTE"));
            return cita;
        });
    }

    public String cancelarCita(int citaId) {
        String sql = "DELETE FROM CITAS WHERE ID = ?";
        int rowsAffected = jdbcTemplate.update(sql, citaId);
        if (rowsAffected > 0) {
            return "Cita cancelada exitosamente.";
        } else {
            return "No se pudo cancelar la cita. Es posible que la cita ya haya pasado.";
        }
    }

    public Cita obtenerCitaPorId(int citaId) {
        String sql = "SELECT * FROM CITAS WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{citaId}, (rs, rowNum) -> {
            Cita cita = new Cita();
            cita.setId(rs.getInt("ID"));
            cita.setDoctorId(rs.getInt("DOCTOR_ID"));
            cita.setConsultorioId(rs.getInt("CONSULTORIO_ID"));
            cita.setHorario(rs.getTimestamp("HORARIO").toLocalDateTime());
            cita.setNombrePaciente(rs.getString("NOMBRE_PACIENTE"));
            return cita;
        });
    }

    private boolean verificarMaximoCitasDoctor(Cita cita) {
        LocalDateTime startOfDay = cita.getHorario().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = cita.getHorario().toLocalDate().atTime(LocalTime.MAX);

        String sql = "SELECT COUNT(*) FROM CITAS WHERE DOCTOR_ID = ? AND HORARIO BETWEEN ? AND ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, cita.getDoctorId(), startOfDay, endOfDay);
        return count < 8;
    }
}