package br.usjt.tarefas.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import br.usjt.tarefas.dao.TarefaDao;
import br.usjt.tarefas.modelo.Tarefa;

@Controller
public class TarefasController {
	private final TarefaDao dao;
	
	@Autowired
	public TarefasController(TarefaDao dao){
		this.dao = dao;
	}
	
	
	@RequestMapping("adicionaTarefa")
	public String adicionaTarefa(@Valid Tarefa tarefa, BindingResult result){
		
		if(result.hasFieldErrors("descricao")){
			return "tarefa/formulario";
		}
		System.out.println("descricao: "+tarefa.getDescricao());
		dao.adiciona(tarefa);
		return "redirect:listaTarefas";
	}
	
	@RequestMapping("novaTarefa")
	public String form(){
		return "tarefa/formulario";
	}
	
	@RequestMapping("listaTarefas")
	public String lista(Model model){
		model.addAttribute("tarefas",dao.listaTarefas());
		return "tarefa/lista";
	}
	
	@RequestMapping("removeTarefa")
	public String removeTarefa(Tarefa tarefa){
		dao.remove(tarefa);
		return "redirect:listaTarefas";
	}
	
	@RequestMapping("mostraTarefa")
	public String mostraTarefa(Long id, Model model){
		model.addAttribute("tarefa",dao.buscaPorId(id));
		return "tarefa/mostra";
	}
	
	@RequestMapping("alteraTarefa")
	public String altera(Tarefa tarefa){
		dao.altera(tarefa);
		return "redirect:listaTarefas";
	}
	
	@RequestMapping("finalizaTarefa")
	public String finaliza(Long id, Model model){
		model.addAttribute("tarefa",dao.finaliza(id));
		return "tarefa/finalizada";
	}
}
