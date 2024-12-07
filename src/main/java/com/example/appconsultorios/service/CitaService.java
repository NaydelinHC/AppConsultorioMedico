package com.example.appconsultorios.service;

import com.example.appconsultorios.model.Cita;
import com.example.appconsultorios.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Logger;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = Logger.getLogger(CitaService.class.getName());

    public String agendarCita(Cita cita) {
        try {
            // Verificar reglas de negocio
            if (!verificarConsultorioDisponible(cita)) {
                return "No se puede agendar la cita porque ya existe una cita en ese mismo horario en el consultorio.";
            }
            if (!verificarDoctorDisponible(cita)) {
                return "No se puede agendar la cita porque el doctor ya tiene una cita en ese mismo horario.";
            }
            if (!verificarPacienteDisponible(cita)) {
                return "No se puede agendar la cita porque el paciente ya tiene una cita en ese mismo horario o con menos de 2 horas de diferencia.";
            }
            if (!verificarMaximoCitasDoctor(cita)) {
                return "No se puede agendar la cita porque el doctor ya tiene 8 citas en el día.";
            }

            // Insertar la cita en la base de datos
            insertarCita(cita);

            logger.info("Cita agendada exitosamente para el doctor con ID: " + cita.getDoctorId());
            return "Cita agendada exitosamente";

        } catch (Exception e) {
            logger.severe("Error al agendar la cita: " + e.getMessage());
            return "Error al agendar la cita. Por favor, inténtelo de nuevo.";
        }
    }

    private boolean verificarConsultorioDisponible(Cita cita) {
        String sql = "SELECT COUNT(*) FROM CITAS WHERE CONSULTORIO_ID = ? AND HORARIO = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, cita.getConsultorioId(), cita.getHorario());
        return count == 0;
    }

    private boolean verificarDoctorDisponible(Cita cita) {
        String sql = "SELECT COUNT(*) FROM CITAS WHERE DOCTOR_ID = ? AND HORARIO = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, cita.getDoctorId(), cita.getHorario());
        return count == 0;
    }

    private boolean verificarPacienteDisponible(Cita cita) {
        LocalDateTime twoHoursBefore = cita.getHorario().minusHours(2);
        LocalDateTime twoHoursAfter = cita.getHorario().plusHours(2);

        String sql = "SELECT COUNT(*) FROM CITAS WHERE NOMBRE_PACIENTE = ? AND (HORARIO BETWEEN ? AND ?)";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, cita.getNombrePaciente(), twoHoursBefore, twoHoursAfter);
        return count == 0;
    }

    private boolean verificarMaximoCitasDoctor(Cita cita) {
        LocalDateTime startOfDay = cita.getHorario().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = cita.getHorario().toLocalDate().atTime(LocalTime.MAX);

        String sql = "SELECT COUNT(*) FROM CITAS WHERE DOCTOR_ID = ? AND HORARIO BETWEEN ? AND ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, cita.getDoctorId(), startOfDay, endOfDay);
        return count < 8;
    }

    private void insertarCita(Cita cita) {
        String sql = "INSERT INTO CITAS (DOCTOR_ID, CONSULTORIO_ID, HORARIO, NOMBRE_PACIENTE) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, cita.getDoctorId(), cita.getConsultorioId(), cita.getHorario(), cita.getNombrePaciente());
    }

    public String editarCita(Cita cita) {
        try {
            // Verificar reglas de negocio
            if (!verificarConsultorioDisponible(cita)) {
                return "No se puede editar la cita porque ya existe una cita en ese mismo horario en el consultorio.";
            }
            if (!verificarDoctorDisponible(cita)) {
                return "No se puede editar la cita porque el doctor ya tiene una cita en ese mismo horario.";
            }
            if (!verificarPacienteDisponible(cita)) {
                return "No se puede editar la cita porque el paciente ya tiene una cita en ese mismo horario o con menos de 2 horas de diferencia.";
            }
            if (!verificarMaximoCitasDoctor(cita)) {
                return "No se puede editar la cita porque el doctor ya tiene 8 citas en el día.";
            }

            // Actualizar la cita en la base de datos
            actualizarCita(cita);

            logger.info("Cita editada exitosamente para el doctor con ID: " + cita.getDoctorId());
            return "Cita editada exitosamente";

        } catch (Exception e) {
            logger.severe("Error al editar la cita: " + e.getMessage());
            return "Error al editar la cita. Por favor, inténtelo de nuevo.";
        }
    }

    private void actualizarCita(Cita cita) {
        String sql = "UPDATE CITAS SET DOCTOR_ID = ?, CONSULTORIO_ID = ?, HORARIO = ?, NOMBRE_PACIENTE = ? WHERE ID = ?";
        jdbcTemplate.update(sql, cita.getDoctorId(), cita.getConsultorioId(), cita.getHorario(), cita.getNombrePaciente(), cita.getId());
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
}