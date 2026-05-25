package com.riwi.librotech.controller.ui;

import com.riwi.librotech.model.Publisher;
import com.riwi.librotech.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/publishers")
public class PublisherUIController {

    private final PublisherService publisherService;

    public PublisherUIController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public String listPublishers(Model model) {
        model.addAttribute("publishers", publisherService.findAll());
        model.addAttribute("screenTitle", "Publisher Catalog - Dashboard");
        return "publishers/list";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        model.addAttribute("publisher", new Publisher());
        model.addAttribute("screenTitle", "Register New Publisher");
        return "publishers/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return publisherService.findById(id).map(publisher -> {
            model.addAttribute("publisher", publisher);
            model.addAttribute("screenTitle", "Edit Publisher");
            return "publishers/form";
        }).orElse("redirect:/admin/publishers");
    }

    @PostMapping("/save")
    public String savePublisher(@ModelAttribute("publisher") Publisher publisher, Model model) {
        int currentYear = LocalDate.now().getYear();
        if (publisher.getName() == null || publisher.getName().isBlank()) {
            model.addAttribute("nameError", "Name cannot be empty.");
            model.addAttribute("screenTitle", publisher.getId() == null ? "Register New Publisher" : "Edit Publisher");
            return "publishers/form";
        }
        if (publisher.getFoundedIn() != null && publisher.getFoundedIn() > currentYear) {
            model.addAttribute("foundedInError", "Founded year cannot be greater than " + currentYear + ".");
            model.addAttribute("screenTitle", publisher.getId() == null ? "Register New Publisher" : "Edit Publisher");
            return "publishers/form";
        }
        if (publisher.getId() != null) {
            publisherService.update(publisher.getId(), publisher);
        } else {
            publisherService.save(publisher);
        }
        return "redirect:/admin/publishers";
    }

    @PostMapping("/delete/{id}")
    public String deletePublisher(@PathVariable Long id) {
        publisherService.deleteById(id);
        return "redirect:/admin/publishers";
    }
}