using System.Data.Entity;

namespace CoffeeStation.Database
{
    public class ApplicationContext : DbContext
    {
        public ApplicationContext() : base("DefaultConnection")
        {
        }

        public DbSet<User> Users { get; set; }
    }
}
