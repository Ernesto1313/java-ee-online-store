package tienda;

import java.sql.Date;
import java.util.ArrayList;

public class PedidoConDetalles {
    private int codigo;
    private Date fecha;
    private float importe;
    private int estado;
    private ArrayList<ProductoBD> productos;

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public float getImporte() { return importe; }
    public void setImporte(float importe) { this.importe = importe; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public ArrayList<ProductoBD> getProductos() { return productos; }
    public void setProductos(ArrayList<ProductoBD> productos) { this.productos = productos; }
}
