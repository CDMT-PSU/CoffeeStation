using System;
using System.Data.Entity;
using System.Data.SQLite;
using System.Runtime.InteropServices;
using System.Security.Cryptography;

namespace CoffeeStation.Database
{
    public class ApplicationContext : DbContext
    {
        public ApplicationContext() : base("DefaultConnection")
        {
        }

        public DbSet<User> Users { get; set; }

        public bool UsernameExists(string username)
        {
            var ctx = new ApplicationContext();
            var query = ctx.Database.SqlQuery<int>("SELECT count(1) FROM user WHERE username = @username", new SQLiteParameter("@username", username));
            var enumerator = query.GetEnumerator();
            enumerator.MoveNext();
            return enumerator.Current > 0;
        }

        public bool AuthenticateUser(string username, string password)
        {
            var ctx = new ApplicationContext();
            var query = ctx.Database.SqlQuery<string>($"SELECT hash FROM user WHERE username = '{username}'", new SQLiteParameter("@username", username));
            var enumerator = query.GetEnumerator();
            enumerator.MoveNext();
            var storedHash = enumerator.Current;
            if (storedHash == null)
            {
                return false;
            }
            var storedHashBytes = Convert.FromBase64String(storedHash);
            var salt = new byte[16];
            Array.Copy(storedHashBytes, 0, salt, 0, 16);
            var hash = new Rfc2898DeriveBytes(password, salt, 10000).GetBytes(20);
            for (var i = 0; i < 20; i++)
            {
                if (storedHashBytes[i + 16] != hash[i])
                {
                    return false;
                }
            }
            return true;
        }
    }
}
