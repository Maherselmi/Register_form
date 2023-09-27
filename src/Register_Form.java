import javax.lang.model.element.Name;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Register_Form extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAdress;
    private JPasswordField tfPassword;
    private JPasswordField tfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel RegisterPanel;


    public Register_Form(JFrame parent){
        super(parent);
        setTitle("creat a new account ");
        setContentPane(RegisterPanel);
        setSize(450 , 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });setVisible(true);
    }

    private void registerUser() {
        String Name = tfName.getText();
        String Email= tfEmail.getText();
        String Phone=tfPhone.getText();
        String Adress=tfAdress.getText();
        String Password=String.valueOf(tfPassword.getPassword());
        String ConfirmPassword=String.valueOf(tfConfirmPassword.getPassword());

        if (Name.isEmpty()|| Email.isEmpty()|| Phone.isEmpty()||Adress.isEmpty()||Password.isEmpty()){
            JOptionPane.showMessageDialog(this,"please enter all fields " ,
                    "try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!Password.equals(ConfirmPassword)){
            JOptionPane.showMessageDialog(this , "confirm password do not match" ,
                    "try again",JOptionPane.ERROR_MESSAGE);
        }
        user = addUsertodatabase(Name,Email,Adress,Phone,Password);
        if (user== null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user","try agin",JOptionPane.ERROR_MESSAGE);
        }

    }

    public User user;
    private User addUsertodatabase(String name, String email, String adress, String phone, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/register?serverTimezone=UTC";
        final String USERNAME ="root";
        final String PASSWORD ="";
        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement stmt = conn.createStatement();
            String sql ="INSERT INTO registre (Name, Email, Phone, Adress, Password)" +"VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, adress);
            preparedStatement.setString(5, password);


            int addedRows = preparedStatement.executeUpdate();
            if (addedRows>0){
                user = new User();
                user.Name=name;
                user.Email=email;
                user.Adress=adress;
                user.Phone=phone;
                user.Password=password;

            }
            stmt.close();
            conn.close();


            
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;

    }

    public static void main(String[] args) {
        Register_Form myform = new Register_Form(null);
        User user = myform.user;
        if (user == null){
            System.out.println("valid register");
        }
        else {
            System.out.println("invalid");

        }
    }
}
