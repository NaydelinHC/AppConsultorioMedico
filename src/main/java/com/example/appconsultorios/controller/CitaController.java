package com.example.appconsultorios.controller;

import com.example.appconsultorios.model.Cita;
import com.example.appconsultorios.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @PostMapping
    @ResponseBody
    public String agendarCita(@RequestBody Cita cita) {
        return citaService.agendarCita(cita);
    }

    @GetMapping("/agendar")
    public String mostrarFormularioAgendarCita(Model model) {
        model.addAttribute("cita", new Cita());
        return "agendarCita";
    }

    @PostMapping("/agendar")
    public String procesarFormularioAgendarCita(@RequestParam int doctorId,
                                                @RequestParam int consultorioId,
                                                @RequestParam String horario,
                                                @RequestParam String nombrePaciente,
                                                Model model) {
        Cita cita = new Cita();
        cita.setDoctorId(doctorId);
        cita.setConsultorioId(consultorioId);
        cita.setHorario(LocalDateTime.parse(horario));
        cita.setNombrePaciente(nombrePaciente);

        String mensaje = citaService.agendarCita(cita);
        model.addAttribute("mensaje", mensaje);
        return "agendarCita";
    }

//    @GetMapping
//    public List<Cita> obtenerCitas() {
//        return citaService.obtenerTodas();
//    }
//
//    @DeleteMapping("/{id}")
//    public String cancelarCita(@PathVariable Long id) {
//        citaService.eliminarCita(id);
//        return "Cita cancelada exitosamente";
//    }
//
//    @PutMapping
//    public String actualizarCita(@RequestBody Cita cita) {
//        citaService.actualizarCita(cita);
//        return "Cita actualizada exitosamente";
//    }
}
