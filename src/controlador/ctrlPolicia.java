package controlador;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import modelo.tbPolicia;
import vista.frmPolicia;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ctrlPolicia implements MouseListener {
    
    frmPolicia Vista;
    tbPolicia Modelo;
    
    // Constructor de la clase
    public ctrlPolicia(frmPolicia Vista, tbPolicia Modelo) {
        this.Vista = Vista;
        this.Modelo = Modelo;
        
        // Agregar los eventos a los botones y componentes
        Vista.btnAgregar.addMouseListener(this);
        Vista.btnEliminar.addMouseListener(this);
        Vista.btnActualizar.addMouseListener(this);
        Vista.btnLimpiar.addMouseListener(this);
        Vista.jTBpolicia.addMouseListener(this);
        
        // Mostrar los policías en la tabla
        Modelo.mostrarPoliciasTB(Vista.jTBpolicia);
        
        // Aplicar filtros a los campos de texto
        aplicarFiltroNumerico(Vista.txtEdad);
        aplicarFiltroNumerico(Vista.txtPeso);
    }
    
    // Método para aplicar filtro numérico a los campos de edad y peso
    private void aplicarFiltroNumerico(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newText.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
    
    // Método para validar el formato del correo electrónico
    private boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }
    
    // Método para manejar eventos de clic
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == Vista.btnAgregar) {
            agregarPolicia();
        } else if (e.getSource() == Vista.btnEliminar) {
            eliminarPolicia();
        } else if (e.getSource() == Vista.btnActualizar) {
            actualizarPolicia();
        } else if (e.getSource() == Vista.btnLimpiar) {
            limpiarCampos();
        } else if (e.getSource() == Vista.jTBpolicia) {
            Modelo.cargarDatosTabla(Vista);
        }
    }
    
    // Método para agregar un nuevo policía
    private void agregarPolicia() {
        try {
            // Validar que no haya campos vacíos
            if (camposVacios()) {
                JOptionPane.showMessageDialog(Vista, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener los datos de los campos de texto
            String nombre = Vista.txtNombre.getText();
            int edad = Integer.parseInt(Vista.txtEdad.getText());
            int peso = Integer.parseInt(Vista.txtPeso.getText());
            String correo = Vista.txtCorreo.getText();
            
            // Validar el formato del correo
            if (!esCorreoValido(correo)) {
                JOptionPane.showMessageDialog(Vista, "El formato del correo electrónico no es válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Establecer los valores en el modelo tbPolicia
            Modelo.setNombrePolicia(nombre);
            Modelo.setEdadPolicia(edad);
            Modelo.setPesoPolicia(peso);
            Modelo.setCorreoPolicia(correo);
            
            // Insertar el nuevo policía
            Modelo.insertarPolicia();
            
            // Actualizar la tabla
            Modelo.mostrarPoliciasTB(Vista.jTBpolicia);
            
            // Limpiar los campos de texto
            limpiarCampos();
            
            JOptionPane.showMessageDialog(Vista, "Policía agregado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(Vista, "La edad y el peso deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Vista, "Error al agregar policía: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para eliminar un policía
    private void eliminarPolicia() {
        try {
            // Obtener la fila seleccionada
            int filaSeleccionada = Vista.jTBpolicia.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(Vista, "Seleccione un policía para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(Vista, "¿Está seguro de eliminar este policía?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Eliminar el policía
                Modelo.eliminarPolicia(Vista.jTBpolicia);
                
                // Actualizar la tabla
                Modelo.mostrarPoliciasTB(Vista.jTBpolicia);
                
                // Limpiar los campos de texto
                limpiarCampos();
                
                JOptionPane.showMessageDialog(Vista, "Policía eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Vista, "Error al eliminar policía: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para actualizar un policía existente
    private void actualizarPolicia() {
        try {
            // Validar que no haya campos vacíos
            if (camposVacios()) {
                JOptionPane.showMessageDialog(Vista, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener la fila seleccionada
            int filaSeleccionada = Vista.jTBpolicia.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(Vista, "Seleccione un policía para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener los datos de los campos de texto
            String nombre = Vista.txtNombre.getText();
            int edad = Integer.parseInt(Vista.txtEdad.getText());
            int peso = Integer.parseInt(Vista.txtPeso.getText());
            String correo = Vista.txtCorreo.getText();
            
            // Validar el formato del correo
            if (!esCorreoValido(correo)) {
                JOptionPane.showMessageDialog(Vista, "El formato del correo electrónico no es válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Establecer los valores en el modelo tbPolicia
            Modelo.setNombrePolicia(nombre);
            Modelo.setEdadPolicia(edad);
            Modelo.setPesoPolicia(peso);
            Modelo.setCorreoPolicia(correo);
            
            // Actualizar el policía
            Modelo.actualizarPolicia(Vista.jTBpolicia);
            
            // Actualizar la tabla
            Modelo.mostrarPoliciasTB(Vista.jTBpolicia);
            
            // Limpiar los campos de texto
            limpiarCampos();
            
            JOptionPane.showMessageDialog(Vista, "Policía actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(Vista, "La edad y el peso deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Vista, "Error al actualizar policía: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        Vista.txtNombre.setText("");
        Vista.txtEdad.setText("");
        Vista.txtPeso.setText("");
        Vista.txtCorreo.setText("");
    }
    
    // Método para verificar si hay campos vacíos en el formulario
    private boolean camposVacios() {
        return Vista.txtNombre.getText().isEmpty() ||
               Vista.txtEdad.getText().isEmpty() ||
               Vista.txtPeso.getText().isEmpty() ||
               Vista.txtCorreo.getText().isEmpty();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
}