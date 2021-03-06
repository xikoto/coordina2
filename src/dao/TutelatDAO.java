package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dao.dto.TutelatDTO;
import pojo.Tutelat;

public class TutelatDAO {

	Connection c;

	public TutelatDAO() {
		c = ManagerDAO.getInstancia().getCon();
	}

	/*
	 * nif nom cognoms correu_upv correu_personal grup_patu grup_matricula mobil
	 */
	public boolean afegir(Tutelat t) {
		try {
			String nif = t.getNif();
			String nom = t.getNom();
			String cog = t.getCognoms();
			String corr = t.getCorreu_upv();
			String corrPers = t.getCorreu_personal();
			String grupPatu = t.getGrup_patu().getNom();
			String grupMat = t.getGrup_matricula();
			String mobil = t.getMobil();

			String sql = "Insert into Tutelat values('" + nif + "','" + nom + "','" + cog + "','" + corr + "','"
					+ corrPers + "','" + grupPatu + "','" + grupMat + "','" + mobil + "')";
			Statement stmt = c.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public ArrayList<TutelatDTO> llistarTutelats() throws SQLException {
		ArrayList<TutelatDTO> llista = new ArrayList<>();

		String sql = "select * from tutelat";
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			llista.add(new TutelatDTO(rs.getString("nif"), rs.getString("nom"), rs.getString("cognoms"),
					rs.getString("correu_upv"), rs.getString("correu_personal"), rs.getString("grup_patu"),
					rs.getString("grup_matricula"), rs.getString("mobil")));

		}
		return llista;
	}

	public boolean esborrar(Tutelat t) {
		String sql = "delete from Tutelat where nif = '" + t.getNif() + "'";
		try {
			Statement stmt = c.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean editar(Tutelat t) {
		String sql = "update Tutelat set nom ='" + t.getNom() + "', cognoms ='" + t.getCognoms() + "', correu_upv ='"
				+ t.getCorreu_upv() + "', correu_personal ='" + t.getCorreu_personal() + "', grup_patu ='"
				+ t.getGrup_patu().getNom() + "', grup_matricula ='" + t.getGrup_matricula() + "', mobil = '"
				+ t.getMobil() + "' where nif = '" + t.getNif() + "'";
		
		try {
			Statement stmt = c.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
