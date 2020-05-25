using CoffeeStation.Database;
using System.Data.Entity;
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
            InitializeComponent();

            ApplicationContext context = new ApplicationContext();
            context.Users.Load();
        }

        private void ShowSignupWindow(object sender, RoutedEventArgs e)
        {
            var signupWindow = new SignupWindow();
            signupWindow.Show();
            Close();
        }
    }
}
