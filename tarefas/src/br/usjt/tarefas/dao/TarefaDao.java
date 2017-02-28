package br.usjt.tarefas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.usjt.tarefas.modelo.Tarefa;
@Repository
public class TarefaDao {
	private Connection conexao;
	
	@Autowired
	public TarefaDao(DataSource dataSource){
		try{
			this.conexao = dataSource.getConnection();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	public void adiciona(Tarefa tarefa){
		String sql = "insert into tarefa (descricao) values(?)";
		try (PreparedStatement pst = conexao.prepareStatement(sql);){
			pst.setString(1, tarefa.getDescricao());
			pst.execute();
		} catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	public void altera(Tarefa tarefa){
		String sql = "update tarefa set descricao=?, finalizada=?, data_fim=? where id=?";
		try (PreparedStatement pst = conexao.prepareStatement(sql);){
			pst.setString(1, tarefa.getDescricao());
			pst.setString(2, (tarefa.isFinalizado()?"S":"N"));
			if(tarefa.getDataFinalizacao() != null){
				pst.setDate(3, new java.sql.Date(tarefa.getDataFinalizacao().getTimeInMillis()));
			} else {
				pst.setNull(3, Types.DATE);
			}
			pst.setLong(4, tarefa.getId());
			pst.execute();
		} catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	public void remove(Tarefa tarefa){
		String sql = "delete from tarefa where id=?";
		try (PreparedStatement pst = conexao.prepareStatement(sql);){
			pst.setLong(1, tarefa.getId());
			pst.execute();
		} catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	public List<Tarefa> listaTarefas(){
		String sql = "select * from tarefa";
		List<Tarefa> tarefas = new ArrayList<Tarefa>();
		
		try (PreparedStatement pst = conexao.prepareStatement(sql);
			 ResultSet rs = pst.executeQuery();){
			while(rs.next()){
				Tarefa tarefa = new Tarefa();
				tarefa.setId(rs.getLong("id"));
				tarefa.setFinalizado(rs.getString("finalizada").equals("S"));
				tarefa.setDescricao(rs.getString("descricao"));
				java.sql.Date dataBanco = rs.getDate("data_fim");
				if(dataBanco!=null){
					Calendar data = Calendar.getInstance();
					data.setTime(dataBanco);
					tarefa.setDataFinalizacao(data);	
				}
				tarefas.add(tarefa);
			}
		} catch(SQLException e){
			throw new RuntimeException(e);
		}
		return tarefas;
	}
	
	public Tarefa buscaPorId(Long id){
		String sql = "select * from tarefa where id=?";
		Tarefa tarefa = null;
		try (PreparedStatement pst = conexao.prepareStatement(sql);){
			pst.setLong(1, id);
			try(ResultSet rs = pst.executeQuery();){
				if(rs.next()){
					tarefa = new Tarefa();
					tarefa.setId(rs.getLong("id"));
					tarefa.setFinalizado(rs.getString("finalizada").equals("S"));
					tarefa.setDescricao(rs.getString("descricao"));
					java.sql.Date dataBanco = rs.getDate("data_fim");
					if(dataBanco!=null){
						Calendar data = Calendar.getInstance();
						data.setTime(dataBanco);
						tarefa.setDataFinalizacao(data);	
					}
				}
			}
		} catch(SQLException e){
			throw new RuntimeException(e);
		}
		return tarefa;
	}

	public Tarefa finaliza(Long id) {
		Tarefa tarefa = buscaPorId(id);
		tarefa.setFinalizado(true);
		Calendar data = Calendar.getInstance();
		data.setTime(new Date(System.currentTimeMillis()));
		tarefa.setDataFinalizacao(data);
		altera(tarefa);
		return tarefa;
	}
}
