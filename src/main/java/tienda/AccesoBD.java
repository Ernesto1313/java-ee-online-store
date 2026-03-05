package tienda;

import java.sql.*;
import java.util.*;

public final class AccesoBD {

    private static AccesoBD instanciaUnica = null;
    private Connection conexionBD = null;

    public static AccesoBD getInstance(){
        if (instanciaUnica == null){
            instanciaUnica = new AccesoBD();
        }
        return instanciaUnica;
    }

    private AccesoBD() {
        abrirConexionBD();
    }

    public void abrirConexionBD() {
        if (conexionBD == null)
        {
            String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
            String DB_URL = "jdbc:mariadb://localhost:3306/daw?allowPublicKeyRetrieval=true&useSSL=false";
            String USER = "root";
            String PASS = "DawLab";
            try {
                Class.forName(JDBC_DRIVER);
                conexionBD = DriverManager.getConnection(DB_URL, USER, PASS);
            }
            catch(Exception e) {
                System.err.println("No se ha podido conectar a la base de datos");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean comprobarAcceso() {
        abrirConexionBD();
        return (conexionBD != null);
    }

    public List<ProductoBD> obtenerProductosBD(int categoria) {
        abrirConexionBD();
        ArrayList<ProductoBD> productos = new ArrayList<>();

        try {
            String query = "SELECT codigo, descripcion, precio, existencias, imagen, categoria FROM productos WHERE categoria = ?";
            PreparedStatement s = conexionBD.prepareStatement(query);
            s.setInt(1, categoria);
            ResultSet resultado = s.executeQuery();
            while(resultado.next()){
                ProductoBD producto = new ProductoBD();
                producto.setCodigo(resultado.getInt("codigo"));
                producto.setDescripcion(resultado.getString("descripcion"));
                producto.setPrecio(resultado.getFloat("precio"));
                producto.setExistencias(resultado.getInt("existencias"));
                producto.setImagen(resultado.getString("imagen"));
                producto.setCategoria(resultado.getInt("categoria"));
                productos.add(producto);
            }
        } catch(Exception e) {
            System.err.println("Error ejecutando la consulta a la base de datos");
            System.err.println(e.getMessage());
        }
        return productos;
    }

    public int comprobarUsuarioBD(String usuario, String clave) {
        abrirConexionBD();
        int codigo = -1;
    
        try {
            String con = "SELECT codigo FROM usuarios WHERE usuario=? AND clave=?";
            PreparedStatement s = conexionBD.prepareStatement(con);
            s.setString(1, usuario);
            s.setString(2, clave);
            ResultSet resultado = s.executeQuery();
    
            if (resultado.next()) {
                codigo = resultado.getInt("codigo");
            }
        } catch (Exception e) {
            System.err.println("Error verificando usuario/clave");
            e.printStackTrace();
        }
    
        return codigo;
    }

    public boolean insertarUsuario(String usuario, String clave, String nombre, String apellidos, String direccion, String telefono) {
        abrirConexionBD();
        try {
            String sql = "INSERT INTO usuarios (usuario, clave, nombre, apellidos, direccion, telefono) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement s = conexionBD.prepareStatement(sql);
            s.setString(1, usuario);
            s.setString(2, clave);
            s.setString(3, nombre);
            s.setString(4, apellidos);
            s.setString(5, direccion);
            s.setString(6, telefono);
            s.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }
    public Connection getConexion() {
        return conexionBD;
    }
    public int obtenerExistencias(int codigoProducto) {
        int existencias = 0;
        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement("SELECT existencias FROM productos WHERE codigo = ?");
            ps.setInt(1, codigoProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existencias = rs.getInt("existencias");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existencias;
    }
    
    
    
}
