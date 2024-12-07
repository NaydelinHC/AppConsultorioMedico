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

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCita(@PathVariable int id, Model model) {
        Cita cita = citaService.obtenerCitaPorId(id);
        model.addAttribute("cita", cita);
        return "editarCita";
    }

    @PostMapping("/editar")
    public String procesarFormularioEditarCita(@RequestParam int id,
                                               @RequestParam int doctorId,
                                               @RequestParam int consultorioId,
                                               @RequestParam String horario,
                                               @RequestParam String nombrePaciente,
                                               Model model) {
        Cita cita = new Cita();
        cita.setId(id);
        cita.setDoctorId(doctorId);
        cita.setConsultorioId(consultorioId);
        cita.setHorario(LocalDateTime.parse(horario));
        cita.setNombrePaciente(nombrePaciente);

        String mensaje = citaService.editarCita(cita);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("cita", cita); // Ensure cita is added to the model
        return "editarCita";
    }
}
