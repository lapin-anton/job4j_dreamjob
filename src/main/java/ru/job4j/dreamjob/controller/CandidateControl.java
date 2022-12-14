package ru.job4j.dreamjob.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import net.jcip.annotations.ThreadSafe;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.servise.CandidateService;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@ThreadSafe
public class CandidateControl {

    private final CandidateService service;

    public CandidateControl(CandidateService service) {
        this.service = service;
    }

    @GetMapping("/candidates")
    public String candidates(Model model, HttpSession session) {
        model.addAttribute("candidates", service.findAll());
        User user = getUser(session);
        model.addAttribute("user", user);
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model, HttpSession session) {
        User user = getUser(session);
        model.addAttribute("user", user);
        model.addAttribute("candidate", new Candidate(0, "Укажите имя соискателя",
                "Укажите желаемую должность", null));
        return "addCandidate";
    }

    @PostMapping("/createCandidate")
    public String createCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        candidate.setPhoto(file.getBytes());
        service.add(candidate);
        return "redirect:/candidates";
    }

    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        candidate.setPhoto(file.getBytes());
        service.update(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String formUpdateCandidate(Model model, @PathVariable("candidateId") int id, HttpSession session) {
        User user = getUser(session);
        model.addAttribute("user", user);
        model.addAttribute("candidate", service.findById(id));
        return "updateCandidate";
    }

    @GetMapping("/photoCandidate/{candidateId}")
    public ResponseEntity<Resource> download(@PathVariable("candidateId") Integer candidateId) {
        Candidate candidate = service.findById(candidateId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(candidate.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(candidate.getPhoto()));
    }

    private User getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setEmail("Гость");
        }
        return user;
    }

}
