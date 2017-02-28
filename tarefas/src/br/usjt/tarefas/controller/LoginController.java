package br.usjt.tarefas.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.usjt.tarefas.dao.UsuarioDao;
import br.usjt.tarefas.modelo.Usuario;

@Controller
public class LoginController {
	private final UsuarioDao dao;
	
	@Autowired
	public LoginController(UsuarioDao dao){
		this.dao = dao;
	}

	@RequestMapping("loginForm")
	public String loginForm(){
		return "formulario-login";
	}
	
	@RequestMapping("efetuaLogin")
	public String efetuaLogin(Usuario usuario, HttpSession session){

		if(dao.existeUsuario(usuario)){
			session.setAttribute("usuarioLogado", usuario);
			return "menu";
		}
		return "redirect:loginForm";
	}
}
