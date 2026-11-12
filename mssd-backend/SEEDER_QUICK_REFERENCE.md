# 🚀 Database Seeder - Quick Reference Card

## ⚡ Quick Start (Choose One)

### Method 1: One Command (Windows)
```bash
cd mssd-backend
run-seeder.bat
```

### Method 2: Maven Command
```bash
mvnw spring-boot:run -Dspring-boot.run.profiles=seed
```

### Method 3: SQL Direct
```bash
mysql -u username -p database_name < database_seeder.sql
```

---

## 🔑 Login Credentials

| Username | Password | Role | Email |
|----------|----------|------|-------|
| `admin` | `admin123` | ADMIN | admin@mssd.com |
| `manager` | `manager123` | USER | manager@mssd.com |
| `instructor` | `instructor123` | USER | instructor@mssd.com |

---

## 📊 Data Count

```
Users:          3
Themes:         6  
Formations:     18 (3 per theme)
Reviews:        8
Portfolio:      8
Calendar:       8
Blog Posts:     10
Newsletters:    5
Highlights:     4
Company Info:   1
───────────────────
TOTAL:         75 records
```

---

## 🎯 Themes Seeded

1. ✅ Management & Leadership
2. ✅ Communication & Relation Client
3. ✅ Développement Personnel
4. ✅ Vente & Négociation
5. ✅ Ressources Humaines
6. ✅ Gestion de Projet

---

## 💰 Formation Prices

- **Minimum:** €900 (Gestion du Temps)
- **Maximum:** €3,500 (Certification PMP)
- **Average:** €1,700
- **Most Common:** €1,500-€2,000

---

## 🏢 Portfolio Companies

✅ Total Energies
✅ BNP Paribas
✅ Orange
✅ Renault Group
✅ Airbus
✅ SNCF
✅ L'Oréal
✅ Carrefour

---

## 📅 Calendar Locations

✅ Paris (4 sessions)
✅ Lyon (1 session)
✅ Marseille (1 session)
✅ Toulouse (1 session)
✅ Bordeaux (1 session)

---

## ⚙️ Application Configuration

### Enable Seeder
```properties
# In application.properties
spring.profiles.active=seed
```

### Disable Seeder (After Use)
```properties
# Remove or comment out
# spring.profiles.active=seed
```

---

## ✅ Verification Queries

### Check Record Counts
```sql
SELECT 
    'Users' as table_name, COUNT(*) as count FROM users
UNION ALL SELECT 'Themes', COUNT(*) FROM themes
UNION ALL SELECT 'Formations', COUNT(*) FROM formations
UNION ALL SELECT 'Reviews', COUNT(*) FROM reviews
UNION ALL SELECT 'Portfolio', COUNT(*) FROM portfolio
UNION ALL SELECT 'Calendar', COUNT(*) FROM calendars
UNION ALL SELECT 'Blogs', COUNT(*) FROM blog;
```

### Test Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Get All Formations
```bash
curl http://localhost:8080/api/formations
```

---

## 🔧 Troubleshooting

### ❌ Problem: Seeder doesn't run
```bash
# Check profile is active
✓ spring.profiles.active=seed
```

### ❌ Problem: Foreign key errors
```sql
-- Disable temporarily
SET FOREIGN_KEY_CHECKS = 0;
-- Run seeder
SET FOREIGN_KEY_CHECKS = 1;
```

### ❌ Problem: Duplicate entry
```sql
-- Clear tables first
TRUNCATE TABLE reviews;
TRUNCATE TABLE portfolio;
TRUNCATE TABLE formations;
TRUNCATE TABLE themes;
```

### ❌ Problem: Can't login
```java
// Regenerate password hash
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
System.out.println(encoder.encode("admin123"));
```

---

## 📁 File Locations

```
mssd-backend/
├── src/main/java/com/mssd/config/
│   └── DataSeeder.java              ← Java seeder
├── database_seeder.sql              ← SQL seeder
├── DATABASE_SEEDER_README.md        ← Full documentation
├── DATA_STRUCTURE.md                ← Data overview
├── SEEDER_QUICK_REFERENCE.md        ← This file
└── run-seeder.bat                   ← Windows script
```

---

## ⚠️ Important Warnings

| ⚠️ WARNING | Description |
|-----------|-------------|
| **Data Loss** | Seeder **DELETES ALL EXISTING DATA** |
| **Backup First** | Always backup before running |
| **Production** | **NEVER** run in production |
| **Profile** | Remove `seed` profile after use |
| **Passwords** | Change default passwords for security |

---

## 🎨 Customization Quick Tips

### Add Formation
```java
// In DataSeeder.java - seedFormations()
createFormation("Title", "slug", "description", 
    theme, new BigDecimal("1500.00"), "3 jours", 
    Formation.Level.INTERMEDIATE)
```

### Add Theme
```java
// In DataSeeder.java - seedThemes()
createTheme("Theme Name", "theme-slug", 
    "Description", "/assets/icons/icon.svg")
```

### Add User
```java
// In DataSeeder.java - seedUsers()
createUser("username", "email@domain.com", 
    "password", User.Role.ADMIN)
```

---

## 📱 API Endpoints to Test

```
GET  /api/themes                  → All themes
GET  /api/themes/{id}             → Theme details
GET  /api/formations              → All formations
GET  /api/formations/{id}         → Formation details
GET  /api/blog                    → All blog posts
GET  /api/portfolio               → All portfolio
GET  /api/calendar                → All events
POST /api/auth/login              → User login
```

---

## 🔄 Reset Database (Clean Start)

### Option 1: Drop and Recreate
```sql
DROP DATABASE mssd_database;
CREATE DATABASE mssd_database;
```

### Option 2: Truncate Tables
```sql
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE reviews;
TRUNCATE TABLE portfolio;
TRUNCATE TABLE calendars;
TRUNCATE TABLE formations;
TRUNCATE TABLE themes;
TRUNCATE TABLE blog;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;
```

### Then Run Seeder Again
```bash
run-seeder.bat
```

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `DATABASE_SEEDER_README.md` | Complete instructions |
| `DATABASE_SEEDER_SUMMARY.md` | Overview & benefits |
| `DATA_STRUCTURE.md` | Data relationships |
| `SEEDER_QUICK_REFERENCE.md` | This cheat sheet |

---

## 🎯 Success Checklist

After running seeder:

- [ ] Application starts without errors
- [ ] Login with admin/admin123 works
- [ ] 6 themes visible on homepage
- [ ] 18 formations listed
- [ ] Reviews show on formation pages
- [ ] Portfolio projects display
- [ ] Calendar events are listed
- [ ] Blog posts are accessible
- [ ] No console errors

---

## 🚨 Emergency Restore

If something goes wrong:

```bash
# Restore from backup
mysql -u username -p database_name < backup.sql

# Or re-run seeder
mvnw spring-boot:run -Dspring-boot.run.profiles=seed
```

---

## 📞 Quick Help

**Can't login?**
→ Check password encoder matches (BCrypt)

**No data showing?**
→ Verify `published=true` and `active=true`

**Foreign key errors?**
→ Check table clearing order

**Seeder not running?**
→ Verify `seed` profile is active

---

## 🎉 You're Done!

Your database now contains:
- ✅ Professional French content
- ✅ Complete relational data
- ✅ Realistic business information
- ✅ Ready-to-use test accounts

**Next:** Start your frontend and test!

---

## 📧 Test Newsletter Emails

```
jean.dupont@example.com
marie.martin@example.com
pierre.bernard@example.com
sophie.dubois@example.com
lucas.thomas@example.com
```

---

**Print this page for quick reference! 📄**

*Last Updated: 2024*
