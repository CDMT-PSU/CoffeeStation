using CoffeeStation.Database;
using System.Data.Entity;
using System.Diagnostics;
using System.Windows;

namespace CoffeeStation
{
    /// <summary>
    /// Логика взаимодействия для LoginWindow.xaml
    /// </summary>
    public partial class LoginWindow : Window
    {
        public LoginWindow()
        {
            DataContext = this;
            InitializeComponent();
        }

        private void LogIn(object sender, RoutedEventArgs e)
        {
            var ctx = new ApplicationContext();
            var username = usernameTextBox.Text;
            var password = passwordBox.Password;
            if (ctx.AuthenticateUser(username, password))
            {
                MessageBox.Show("OK");
            }
            else
            {
                MessageBox.Show("Invalid credentials");
            }
        }

        private void ShowSignupWindow(object sender, RoutedEventArgs e)
        {
            var signupWindow = new SignupWindow();
            signupWindow.Show();
            Close();
        }
    }
}
