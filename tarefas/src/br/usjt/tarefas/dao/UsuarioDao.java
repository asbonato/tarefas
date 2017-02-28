package br.usjt.tarefas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.usjt.tarefas.modelo.Usuario;
@Repository
public class UsuarioDao {
	private Connection conexao;
	
	@Autowired
	public UsuarioDao(DataSource dataSource){
		try{
			this.conexao = dataSource.getConnection();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	public boolean existeUsuario(Usuario usuario){
		String sql = "select * from usuario where login=? and senha=?";
		
		try (PreparedStatement pst = conexao.prepareStatement(sql);){
			pst.setString(1, usuario.getLogin());
			pst.setString(2, usuario.getSenha());
			try(ResultSet rs = pst.executeQuery();){
				return rs.next();
			}
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
}
