package com.example.appconsultorios.repository;

import com.example.appconsultorios.model.Consultorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ConsultorioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Consultorio> findAll() {
        return jdbcTemplate.query("SELECT * FROM CONSULTORIO", new ConsultorioRowMapper());
    }

    public Consultorio findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM CONSULTORIO WHERE ID = ?", new ConsultorioRowMapper(), id);
    }

    public void save(Consultorio consultorio) {
        String sql = "INSERT INTO CONSULTORIO (NUMERO_CONSULTORIO, PISO) VALUES (?, ?)";
        jdbcTemplate.update(sql, consultorio.getNumeroConsultorio(), consultorio.getPiso());
    }

    private static class ConsultorioRowMapper implements RowMapper<Consultorio> {
        @Override
        public Consultorio mapRow(ResultSet rs, int rowNum) throws SQLException {
            Consultorio consultorio = new Consultorio();
            consultorio.setId(rs.getLong("ID"));
            consultorio.setNumeroConsultorio(rs.getInt("NUMERO_CONSULTORIO"));
            consultorio.setPiso(rs.getInt("PISO"));
            return consultorio;
        }
    }
}
