package com.example.toDoList.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.toDoList.model.Task;
import com.example.toDoList.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/")
    public ModelAndView task(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("tasks");
        return modelAndView;
    }

    @GetMapping
    public String listarTarefas(Model model) {
        List<Task> tarefas = taskRepository.findAll();
        model.addAttribute("tarefas", tarefas);
        return "listarTarefas";  // Nome do template (view) que será renderizado
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping
    public String criarTarefa(@RequestBody Task tarefa) {
        tarefa.setDataCriacao(new Date());
        taskRepository.save(tarefa);
        return "redirect:/tarefas";  // Redireciona para a lista de tarefas após criar
    }

    @PutMapping("/{id}")
    public String atualizarTarefa(@PathVariable Long id, @RequestBody Task tarefaAtualizada) {
        Optional<Task> optionalTarefa = taskRepository.findById(id);
        if (optionalTarefa.isPresent()) {
            Task tarefa = optionalTarefa.get();
            tarefa.setTitulo(tarefaAtualizada.getTitulo());
            tarefa.setDescricao(tarefaAtualizada.getDescricao());
            taskRepository.save(tarefa);
            return "redirect:/tarefas";  // Redireciona para a lista de tarefas após atualizar
        } else {
            throw new RuntimeException("Tarefa não encontrada para o ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public String deletarTarefa(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tarefas";  // Redireciona para a lista de tarefas após deletar
    }
}
