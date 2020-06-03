using CoffeeStation.Database;
using System;
using System.Data.Entity;
using System.Windows;

namespace CoffeeStation
{
    /// <summary>
    /// Логика взаимодействия для SignupWindow.xaml
    /// </summary>
    public partial class SignupWindow : Window
    {
        public SignupWindow()
        {
            DataContext = this;
            InitializeComponent();
        }

        private void SignUp(object sender, RoutedEventArgs e)
        {
            var username = usernameTextBox.Text;
            var password = passwordBox.Password;
            var passwordConfirmation = passwordConfirmationBox.Password;
            if (username.Length < 4)
            {
                MessageBox.Show("Username length must be 4 to 15 characters");
                return;
            }
            if (password.Length < 4)
            {
                MessageBox.Show("Password length must be 4 to 15 characters");
                return;
            }
            if (password != passwordConfirmation)
            {
                MessageBox.Show("Password mismatch");
                return;
            }
            var user = new User();
            user.Username = username;
            user.HashPassword(password);
            var ctx = new ApplicationContext();
            ctx.Users.Add(user);
            ctx.SaveChanges();
            MessageBox.Show("Registration done");
        }

        private void ShowLoginWindow(object sender, RoutedEventArgs e)
        {
            var loginWindow = new LoginWindow();
            loginWindow.Show();
            Close();
        }
    }
}
