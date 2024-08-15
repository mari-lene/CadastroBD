package cadastrobd.model;

/**
 *
 * @author mari-
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import cadastrobd.model.util.ConectorBD;

public class PessoaJuridicaDAO {

    private ConectorBD connector;
    
    public PessoaJuridicaDAO() {
        connector = new ConectorBD();
    }
    
    public PessoaJuridica getPessoa(Integer id) throws SQLException {
        String sql = "SELECT pj.FK_Pessoa_idPessoa, pj.cnpj, p.nome, p.endereco, p.cidade, p.estado, p.telefone, p.email "
            + "FROM PessoaJuridica pj "
            + "INNER JOIN Pessoa p ON pj.FK_Pessoa_idPessoa = p.idPessoa "
            + "WHERE pj.FK_Pessoa_idPessoa = ?";
        try (Connection con = connector.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new PessoaJuridica(
                        rs.getInt("FK_Pessoa_idPessoa"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cnpj")
                    );
                }
            }
        }
        return null;
    }

    public ArrayList<PessoaJuridica> getPessoas() throws SQLException {
        ArrayList<PessoaJuridica> list = new ArrayList<>();
        String sql = "SELECT pj.FK_Pessoa_idPessoa, pj.cnpj, p.nome, p.endereco, p.cidade, p.estado, p.telefone, p.email "
            + "FROM PessoaJuridica pj "
            + "INNER JOIN Pessoa p ON pj.FK_Pessoa_idPessoa = p.idPessoa";
        try (Connection con = connector.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new PessoaJuridica(
                    rs.getInt("FK_Pessoa_idPessoa"),
                    rs.getString("nome"),
                    rs.getString("endereco"),
                    rs.getString("cidade"),
                    rs.getString("estado"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("cnpj")));
            }
        }
        return list;
    }
    
    public int incluir(PessoaJuridica pj) throws SQLException {
        if (pj.getNome() == null || pj.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("'nome' cannot be empty or null.");
        }
        String sqlInsertPessoa = "INSERT INTO Pessoa(nome, endereco, cidade, estado, telefone, email) VALUES(?, ?, ?, ?, ?, ?)";
        String sqlInsertPessoaJuridica = "INSERT INTO PessoaJuridica(FK_Pessoa_idPessoa, cnpj) VALUES(?, ?)";

        try (Connection con = connector.getConnection();
            PreparedStatement stmtPessoa = con.prepareStatement(sqlInsertPessoa,Statement.RETURN_GENERATED_KEYS)) {
            String[] pfArray = {"", pj.getNome(), pj.getEndereco(), pj.getCidade(), pj.getEstado(), pj.getTelefone(), pj.getEmail()};
            for(int i = 1; i < 7; i++) {
                stmtPessoa.setString(i, pfArray[i]);
            }
            if (stmtPessoa.executeUpdate() != 0) {
                System.out.println("INSERT INTO PessoaJuridica success.");
            }
            else {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmtPessoa.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idNovaPessoa = generatedKeys.getInt(1);
                    pj.setId(idNovaPessoa);
                    try (PreparedStatement stmtPessoaFisica = con.prepareStatement(sqlInsertPessoaJuridica,Statement.RETURN_GENERATED_KEYS)) {
                        stmtPessoaFisica.setInt(1, idNovaPessoa);
                        stmtPessoaFisica.setString(2, pj.getCnpj());
                        stmtPessoaFisica.executeUpdate();
                    }
                    return idNovaPessoa;
                } else {
                    throw new SQLException("Creating user failed. No ID obtained.");
                }
            }
        }
    }
    
    public void alterar(PessoaJuridica pj) throws SQLException {
        String sqlUpdatePessoa = "UPDATE Pessoa SET nome = ?, endereco = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE idPessoa = ?;";
        String sqlUpdatePessoaJuridica = "UPDATE PessoaJuridica SET cnpj = ? WHERE FK_Pessoa_idPessoa = ?;";
        try (Connection con = connector.getConnection();
            PreparedStatement stmtPessoa = con.prepareStatement(sqlUpdatePessoa,Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stmtPessoaJuridica = con.prepareStatement(sqlUpdatePessoaJuridica,Statement.RETURN_GENERATED_KEYS)) {
            String[] pfArray = {"", pj.getNome(), pj.getEndereco(), pj.getCidade(), pj.getEstado(), pj.getTelefone(), pj.getEmail()};
            for(int i = 1; i < 7; i++) {
                stmtPessoa.setString(i, pfArray[i]);
            }
            stmtPessoa.setInt(7, pj.getId());
            stmtPessoa.executeUpdate();
            stmtPessoaJuridica.setString(1, pj.getCnpj());
            stmtPessoaJuridica.setInt(2, pj.getId());
            stmtPessoaJuridica.executeUpdate();
        }
    }
    
    public void excluir(PessoaJuridica pj) throws SQLException {
        String sqlDeletePessoaJuridica = "DELETE FROM PessoaJuridica WHERE FK_Pessoa_idPessoa = ?;";
        String sqlDeletePessoa = "DELETE FROM Pessoa WHERE idPessoa = ?;";
        try (Connection con = connector.getConnection();
             PreparedStatement stmtPessoaJuridica = con.prepareStatement(sqlDeletePessoaJuridica,Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtPessoa = con.prepareStatement(sqlDeletePessoa,Statement.RETURN_GENERATED_KEYS)) {
            stmtPessoaJuridica.setInt(1, pj.getId());
            stmtPessoaJuridica.executeUpdate();
            stmtPessoa.setInt(1, pj.getId());
            stmtPessoa.executeUpdate();
        }
    }

    public void close() throws SQLException {
        connector.close();
    }
    
}
