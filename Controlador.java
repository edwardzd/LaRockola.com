package controlador;

import Vista.Ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Conexion;
import modelo.Producto;

//Para que los botones escuchen
public class Controlador implements ActionListener {

    //creamos un objto "conectar" de la clase conexion
    Conexion conectar = new Conexion();
    Connection connect = conectar.getConexion();

    private Producto mod;
    private Ventana vista;

    //Creamos un constructor de la clase Controlado que recibe como parametros un modelo y una vista
    public Controlador(Producto mod, Ventana vista) {
        this.mod = mod;
        this.vista = vista;
        //Inicializamos los botones
        this.vista.btnGuardarRegistro.addActionListener(this);
        this.vista.btnActualizarConsultar.addActionListener(this);
        this.vista.btnConsultarConsultar.addActionListener(this);
        this.vista.btnEliminarConsutar.addActionListener(this);
    }

    //Creamos un metodo para arrancar o iniciar la ventana que incluya: Titulo, visibilidad y ubicación
    public void iniciar() {
        vista.setTitle("Reto Semana 5");
        vista.setVisible(true);
        vista.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Declaramos una variable "model" del tipo defaultTableModel para mostrar la base de datos vinculada
        DefaultTableModel model = (DefaultTableModel) this.vista.tablaConsultar.getModel();

        //con un condicional vemos cual fue el evento y las acciones a realizar dependiendo de este
        //creamos una variable del la clase ResultSet para devolver una consulta a la base de datos mediante
        //el statement "pst"
        if (e.getSource() == vista.btnConsultarConsultar) {//Realizar una consulta de la base datos
            //Creamos la variable "resul" del tipo ResultSet para mostrar el resultado de la query
            ResultSet resul = null;
            //Capturamos los errores mediante el uso de try /catch
            try {
                model.setRowCount(0);//inicializamos nuestra tabla modelo con cero filas
                PreparedStatement pst = connect.prepareStatement("SELECT id,nombre,cantidad,categoria,precio FROM productos");//Estructura de busqueda Query
                resul = pst.executeQuery();//Ejecutamos la busqueda definida en pst

                //Usamos un while para decir que mientras hayan filas en la base datos las traiga al modelo de tabla en la vista
                //vamos llenando la tabla model con filas traidas de la base de datos
                while (resul.next()) {
                    model.addRow(new Object[]{resul.getInt("id"), resul.getString("nombre"), resul.getInt("cantidad"), resul.getString("categoria"), resul.getDouble("precio")});
                }
                JOptionPane.showMessageDialog(null, "Datos consultados");//Borrar es una prueba PARA VER SI SALE EL MSJE CUANDO SE ACTUALIZA LA CONSULTA
            } catch (Exception ee) {
                //A través de una caja de dialogo le decimos que nos muestre el error
                JOptionPane.showMessageDialog(null, ee.getMessage().toString());
            }
        } else if (e.getSource() == vista.btnGuardarRegistro) {//Guarda o crea un nuevo registro
            try {
                model.setRowCount(0);
                PreparedStatement pst = connect.prepareStatement("INSERT INTO productos (nombre,cantidad,categoria,precio) VALUES(?,?,?,?)");//Inserta los campos en la varaible pst
                pst.setString(1, vista.txtNombreRegistro.getText());//De lo digitado en la caja de texto nombre ubiquelo en la 1era posición
                pst.setString(2, vista.txtCantidadRegistro.getText());                
                pst.setString(3, (String) vista.cboxCategoriaRegistro.getSelectedItem());
                pst.setDouble(4, Double.parseDouble(vista.txtPrecioRegistro.getText()));
                pst.execute();
                JOptionPane.showMessageDialog(null, "Datos guardados");
            } catch (Exception ee) {
                JOptionPane.showMessageDialog(null, ee.getMessage().toString());
            }
        } else if (e.getSource() == vista.btnActualizarConsultar) {//Update un registro
            try {
                model.setRowCount(0);
                PreparedStatement pst = connect.prepareStatement("UPDATE productos SET nombre=?,cantidad=?,precio=?,categoria=? WHERE id=?");
                pst.setString(1, vista.txtNombreConsultar.getText());//De lo digitado en la caja de texto nombre ubiquelo en la 1era posición
                pst.setInt(2, Integer.parseInt(vista.txtCantidadConsultar.getText()));
                pst.setDouble(3, Double.parseDouble(vista.txtPrecioConsultar.getText()));
                pst.setString(4, (String) vista.cboxCategoriaConsultar.getSelectedItem());
                pst.setString(5, vista.txtIdConsultar.getText());
                pst.execute();
                JOptionPane.showMessageDialog(null, "Datos actualizados");
            } catch (Exception ee) {
                JOptionPane.showMessageDialog(null, ee.getMessage().toString());
            }
        } else if (e.getSource() == vista.btnEliminarConsutar) { //Eliminar un registro

            try {
                model.setRowCount(0);
                PreparedStatement pst = connect.prepareStatement("DELETE FROM productos WHERE id=?");
                pst.setInt(1, Integer.parseInt(vista.txtIdConsultar.getText()));
                pst.execute();
                JOptionPane.showMessageDialog(null, "Datos Eliminados");
            } catch (Exception ee) {
                JOptionPane.showMessageDialog(null, ee.getMessage().toString());
            }

        }

    }

}
