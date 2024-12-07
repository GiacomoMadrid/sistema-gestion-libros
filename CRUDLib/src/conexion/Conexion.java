package conexion;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author Giacomo
 */
public class Conexion {
    protected Connection conexion = null;
    
    public Conexion(){
    }
    
    
    protected void conectar()throws SQLException, ClassNotFoundException{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "1234");
    
    }
    
    protected void desconectar()throws SQLException{
        if(!conexion.isClosed()){
            conexion.close();
        }
    }
    
}
