package modelo;

import java.sql.*;
import java.util.UUID;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import vista.frmPolicia;

public class tbPolicia {
    
    private String UUIDpolicia;
    private String nombrePolicia;
    private int edadPolicia;
    private int pesoPolicia;
    private String correoPolicia;
    
    public String getUUIDpolicia() {
        return UUIDpolicia;
    }

    public void setUUIDpolicia(String UUIDpolicia) {
        this.UUIDpolicia = UUIDpolicia;
    }

    public String getNombrePolicia() {
        return nombrePolicia;
    }

    public void setNombrePolicia(String nombrePolicia) {
        this.nombrePolicia = nombrePolicia;
    }

    public int getEdadPolicia() {
        return edadPolicia;
    }

    public void setEdadPolicia(int edadPolicia) {
        this.edadPolicia = edadPolicia;
    }

    public int getPesoPolicia() {
        return pesoPolicia;
    }

    public void setPesoPolicia(int pesoPolicia) {
        this.pesoPolicia = pesoPolicia;
    }

    public String getCorreoPolicia() {
        return correoPolicia;
    }

    public void setCorreoPolicia(String correoPolicia) {
        this.correoPolicia = correoPolicia;
    }
    
    // Método para insertar un nuevo policía en la base de datos
    public void insertarPolicia() {
        Connection conexion = ClaseConexion.getConexion();
        
        try {
            PreparedStatement addPolicia = conexion.prepareStatement("INSERT INTO tbPolicia (UUIDpolicia, nombrePolicia, edadPolicia, pesoPolicia, correoPolicia) VALUES (?, ?, ?, ?, ?)");
            addPolicia.setString(1, UUID.randomUUID().toString());
            addPolicia.setString(2, getNombrePolicia());
            addPolicia.setInt(3, getEdadPolicia());
            addPolicia.setInt(4, getPesoPolicia());
            addPolicia.setString(5, getCorreoPolicia());
            addPolicia.executeUpdate();
        } catch(SQLException ex) {
            System.out.println("Error en el modelo: Método insertarPolicia " + ex);
        }
    }
    
    // Método para eliminar un policía de la base de datos
    public void eliminarPolicia(JTable tabla) {
        Connection conexion = ClaseConexion.getConexion();
        int filaSeleccionada = tabla.getSelectedRow();
        String UUIDpoliciaSeleccionado = tabla.getValueAt(filaSeleccionada, 0).toString();
        
        try {
            PreparedStatement deletePolicia = conexion.prepareStatement("DELETE FROM tbPolicia WHERE UUIDpolicia = ?");
            deletePolicia.setString(1, UUIDpoliciaSeleccionado);
            deletePolicia.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al eliminar policía: " + e);
        }
    }
    
    // Método para actualizar la información de un policía en la base de datos
    public void actualizarPolicia(JTable tabla) {
        Connection conexion = ClaseConexion.getConexion();
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            String UUIDpolicia = tabla.getValueAt(filaSeleccionada, 0).toString();
            try { 
                PreparedStatement updatePolicia = conexion.prepareStatement("UPDATE tbPolicia SET nombrePolicia = ?, edadPolicia = ?, pesoPolicia = ?, correoPolicia = ? WHERE UUIDpolicia = ?");
                updatePolicia.setString(1, getNombrePolicia());
                updatePolicia.setInt(2, getEdadPolicia());
                updatePolicia.setInt(3, getPesoPolicia());
                updatePolicia.setString(4, getCorreoPolicia());
                updatePolicia.setString(5, UUIDpolicia);
                updatePolicia.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error en el método de actualizarPolicia: " + e);
            }
        } else {
            System.out.println("No se pudo actualizar");
        }
    }
    
    // Método para mostrar todos los policías en una tabla
    public void mostrarPoliciasTB(JTable tabla) {
        Connection conexion = ClaseConexion.getConexion();
        DefaultTableModel modeloPolicias = new DefaultTableModel();
        modeloPolicias.setColumnIdentifiers(new Object[]{"UUIDpolicia", "Nombre", "Edad", "Peso", "Correo"});
        try {
            PreparedStatement statement = conexion.prepareStatement("SELECT * FROM tbPolicia");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                modeloPolicias.addRow(new Object[]{
                    rs.getString("UUIDpolicia"), 
                    rs.getString("nombrePolicia"), 
                    rs.getInt("edadPolicia"), 
                    rs.getInt("pesoPolicia"),
                    rs.getString("correoPolicia")
                });
            }
            tabla.setModel(modeloPolicias);
        } catch (Exception e) {
            System.out.println("Error en el modelo, método mostrarPoliciasTB: " + e);
        }
    }
    
    // Método para cargar los datos de un policía seleccionado en la tabla a los campos de texto
    public void cargarDatosTabla(frmPolicia vista) {
        int filaSeleccionada = vista.jTBpolicia.getSelectedRow();
        if (filaSeleccionada != -1) {
            String UUIDpolicia = vista.jTBpolicia.getValueAt(filaSeleccionada, 0).toString();
            String nombreTB = vista.jTBpolicia.getValueAt(filaSeleccionada, 1).toString();
            int edadTB = Integer.parseInt(vista.jTBpolicia.getValueAt(filaSeleccionada, 2).toString());
            int pesoTB = Integer.parseInt(vista.jTBpolicia.getValueAt(filaSeleccionada, 3).toString());
            String correoTB = vista.jTBpolicia.getValueAt(filaSeleccionada, 4).toString();
            
            vista.txtNombre.setText(nombreTB);
            vista.txtEdad.setText(String.valueOf(edadTB));
            vista.txtPeso.setText(String.valueOf(pesoTB));
            vista.txtCorreo.setText(correoTB);
            
            // Guardamos el UUID para usarlo en actualizaciones
            setUUIDpolicia(UUIDpolicia);
        }
    }
}
