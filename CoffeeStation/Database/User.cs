using CoffeeStation.Database;
using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.Infrastructure;
using System.Data.SqlClient;
using System.Runtime.CompilerServices;
using System.Security.Cryptography;

namespace CoffeeStation
{
    [Table("user")]
    public class User/* : INotifyPropertyChanged*/
    {
        public User()
        {
        }

        [Column("id")]
        public int Id { get; set; }

        [Column("username")]
        public string Username { get; set; }

        [Column("hash")]
        public string Hash { get; set; }

        [Column("role")]
        public int Role { get; set; }

        //public event PropertyChangedEventHandler PropertyChanged;

        //public void OnPropertyChanged([CallerMemberName]string prop = "")
        //{
        //    PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(prop));
        //}

        public void HashPassword(string password)
        {
            var salt = new byte[16];
            new RNGCryptoServiceProvider().GetBytes(salt);
            var hash = new Rfc2898DeriveBytes(password, salt, 10000).GetBytes(20);
            var saltedHash = new byte[36];
            Array.Copy(salt, 0, saltedHash, 0, 16);
            Array.Copy(hash, 0, saltedHash, 16, 20);
            Hash = Convert.ToBase64String(saltedHash);
        }
    }
}
