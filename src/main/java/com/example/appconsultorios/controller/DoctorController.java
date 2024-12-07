package com.example.appconsultorios.controller;

import com.example.appconsultorios.model.Cita;
import com.example.appconsultorios.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;


@Controller
@RequestMapping("/api/doctores")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/consultarCitas")
    public String consultarCitas(@RequestParam int doctorId, Model model) {
        try {
            List<Cita> citas = doctorService.consultarCitasPorDoctor(doctorId);
            model.addAttribute("citas", citas);
            return "consultarCitas";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al consultar las citas: " + e.getMessage());
            return "consultarCitas";
        }
    }


    @PostMapping("/cancelarCita")
    public String cancelarCita(@RequestParam int citaId, Model model) {
        String mensaje = doctorService.cancelarCita(citaId);
        if (mensaje != null && !mensaje.isEmpty()) {
            model.addAttribute("mensaje", mensaje);
        }
        return "cancelarCita";
    }
}