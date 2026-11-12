# 🌱 MSSD Database Seeder Guide

This guide explains how to populate your MSSD database with clean, professional data for development and testing.

## 📋 Table of Contents
- [Overview](#overview)
- [What Data is Included](#what-data-is-included)
- [Method 1: Java Seeder (Recommended)](#method-1-java-seeder-recommended)
- [Method 2: SQL Script](#method-2-sql-script)
- [Verification](#verification)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

Two seeding methods are available:

1. **Java Seeder** (`DataSeeder.java`) - Automatic, Spring Boot integrated
2. **SQL Script** (`database_seeder.sql`) - Manual, database-direct

Both methods populate identical data and clear existing records before seeding.

---

## 📦 What Data is Included

### Users (3 accounts)
- **admin** / admin@mssd.com / password: `admin123` (ADMIN role)
- **manager** / manager@mssd.com / password: `manager123` (USER role)
- **instructor** / instructor@mssd.com / password: `instructor123` (USER role)

### Company Information (1)
- MSSD Training & Consulting with full details

### Highlights (4)
- Homepage feature highlights

### Themes (6)
- Management & Leadership
- Communication & Relation Client
- Développement Personnel
- Vente & Négociation
- Ressources Humaines
- Gestion de Projet

### Formations (18)
- 3 formations per theme
- Mix of BEGINNER, INTERMEDIATE, and EXPERT levels
- Realistic prices (€900 - €3,500)
- Complete descriptions in French

### Reviews (8)
- Authentic-looking reviews for various formations
- 4-5 star ratings with detailed comments

### Portfolio Projects (8)
- Real company references (Total, BNP Paribas, Orange, etc.)
- Project dates and descriptions
- Formation associations

### Calendar Events (8)
- Upcoming training sessions
- Various locations (Paris, Lyon, Marseille, etc.)
- Available capacity tracking

### Blog Posts (10)
- Professional development topics
- Some with YouTube video links
- Published at different dates

### Newsletter Subscriptions (5)
- Sample newsletter subscribers

---

## 🔧 Method 1: Java Seeder (Recommended)

### Prerequisites
- Spring Boot application configured
- Database connection established
- All repositories available

### Step 1: Activate the Seeder Profile

Add the `seed` profile when starting your application:

**Option A: Command Line**
```bash
cd mssd-backend
mvnw spring-boot:run -Dspring-boot.run.profiles=seed
```

**Option B: IntelliJ IDEA**
1. Open `Run/Debug Configurations`
2. Select your Spring Boot configuration
3. In `Active profiles`, add: `seed`
4. Run the application

**Option C: application.properties**
```properties
spring.profiles.active=seed
```

### Step 2: Run and Verify

The seeder runs automatically on startup when the `seed` profile is active:

```
🌱 Starting database seeding...
🗑️  Clearing existing data...
✓ Database cleared
👤 Seeding users...
✓ Created 3 users
🏢 Seeding company information...
✓ Company information created
⭐ Seeding highlights...
✓ Created 4 highlights
🎨 Seeding themes...
✓ Created 6 themes
📚 Seeding formations...
✓ Created 18 formations
⭐ Seeding reviews...
✓ Created 8 reviews
💼 Seeding portfolio...
✓ Created 8 portfolio items
📅 Seeding calendar events...
✓ Created 8 calendar events
📝 Seeding blog posts...
✓ Created 10 blog posts
📧 Seeding newsletter subscriptions...
✓ Created 5 newsletter subscriptions
✅ Database seeding completed successfully!
```

### Step 3: Remove Seed Profile (Important!)

**After seeding, disable the profile to prevent re-seeding on every restart:**

- Remove `-Dspring-boot.run.profiles=seed` from command
- Or remove `seed` from IntelliJ configuration
- Or remove from `application.properties`

### Customization

To modify the seeding behavior:

**Preserve existing data:**
```java
// In DataSeeder.java, comment out this line:
// clearDatabase(...);
```

**Add more data:**
Edit the seed methods in `DataSeeder.java`:
- `seedFormations()` - Add more formations
- `seedBlogs()` - Add more blog posts
- `seedUsers()` - Add more users
- etc.

---

## 📄 Method 2: SQL Script

### Prerequisites
- MySQL/MariaDB database
- Database client (MySQL Workbench, phpMyAdmin, DBeaver, etc.)
- Database credentials

### Step 1: Backup Your Database (Important!)

Before running the script:

```bash
mysqldump -u your_username -p mssd_database > backup_before_seeding.sql
```

### Step 2: Execute the SQL Script

**Option A: MySQL Command Line**
```bash
mysql -u your_username -p mssd_database < database_seeder.sql
```

**Option B: MySQL Workbench**
1. Open MySQL Workbench
2. Connect to your database
3. Open `database_seeder.sql`
4. Execute the script (⚡ Execute button)

**Option C: phpMyAdmin**
1. Login to phpMyAdmin
2. Select your database
3. Go to "Import" tab
4. Choose `database_seeder.sql`
5. Click "Go"

**Option D: DBeaver**
1. Connect to your database
2. Open SQL Editor
3. Load `database_seeder.sql`
4. Execute (Ctrl+Enter)

### Step 3: Verify Data

Run the verification queries at the end of the SQL file:

```sql
SELECT 'Users' as Table_Name, COUNT(*) as Count FROM users
UNION ALL
SELECT 'Themes', COUNT(*) FROM themes
UNION ALL
SELECT 'Formations', COUNT(*) FROM formations
UNION ALL
SELECT 'Reviews', COUNT(*) FROM reviews
UNION ALL
SELECT 'Portfolio', COUNT(*) FROM portfolio
UNION ALL
SELECT 'Calendar', COUNT(*) FROM calendars
UNION ALL
SELECT 'Blog', COUNT(*) FROM blog
UNION ALL
SELECT 'Newsletters', COUNT(*) FROM newsletters
UNION ALL
SELECT 'Highlights', COUNT(*) FROM highlights
UNION ALL
SELECT 'Companies', COUNT(*) FROM companies;
```

**Expected Results:**
| Table_Name | Count |
|------------|-------|
| Users | 3 |
| Themes | 6 |
| Formations | 18 |
| Reviews | 8 |
| Portfolio | 8 |
| Calendar | 8 |
| Blog | 10 |
| Newsletters | 5 |
| Highlights | 4 |
| Companies | 1 |

---

## ✅ Verification

### Check via Application API

Test the endpoints to verify data:

```bash
# Get all themes
curl http://localhost:8080/api/themes

# Get all formations
curl http://localhost:8080/api/formations

# Get all blog posts
curl http://localhost:8080/api/blog

# Login as admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Check via Database

```sql
-- Check theme-formation relationships
SELECT 
    t.name as theme,
    COUNT(f.id) as formation_count
FROM themes t
LEFT JOIN formations f ON t.id = f.theme_id
GROUP BY t.id, t.name;

-- Check reviews per formation
SELECT 
    f.title,
    COUNT(r.id) as review_count,
    AVG(r.rating) as avg_rating
FROM formations f
LEFT JOIN reviews r ON f.id = r.formation_id
GROUP BY f.id, f.title;

-- Check upcoming calendar events
SELECT 
    title,
    start_time,
    location,
    max_capacity,
    current_capacity
FROM calendars
WHERE start_time > NOW()
ORDER BY start_time;
```

---

## 🔧 Troubleshooting

### Issue: Foreign Key Constraint Errors

**Cause:** Tables are being cleared/populated in the wrong order

**Solution:**
```sql
-- Temporarily disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;
-- Run your operations
SET FOREIGN_KEY_CHECKS = 1;
```

The SQL script already handles this.

### Issue: Duplicate Entry Errors

**Cause:** Data already exists in the database

**Solutions:**
1. Clear database first:
   ```sql
   -- The seeder scripts do this automatically
   TRUNCATE TABLE reviews;
   TRUNCATE TABLE portfolio;
   -- etc.
   ```

2. Or restore from backup and try again

### Issue: Java Seeder Not Running

**Cause:** Profile not activated or repositories missing

**Solutions:**
1. Verify profile is active:
   ```
   spring.profiles.active=seed
   ```

2. Check console for errors about missing repositories

3. Ensure all entities have corresponding repositories

### Issue: BCrypt Password Errors (SQL)

**Cause:** Password hashes may not match your BCrypt implementation

**Solution:** 
If login fails, generate new password hashes:

```java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
System.out.println(encoder.encode("admin123"));
```

Then update the SQL script with new hashes.

### Issue: Date/Time Issues

**Cause:** Time zone differences or date format issues

**Solution:**
The seeders use relative dates (NOW(), DATE_ADD, etc.) which should work across time zones.

For SQL, ensure your database time zone is correct:
```sql
SELECT @@global.time_zone, @@session.time_zone;
```

---

## 🎨 Customization Tips

### Change Formation Prices
```java
// In DataSeeder.java
createFormation("Title", "slug", "description", 
    theme, new BigDecimal("YOUR_PRICE"), "duration", Level.BEGINNER)
```

### Add More Themes
```java
// In seedThemes() method
createTheme("New Theme Name", "new-theme-slug",
    "Theme description", "/assets/icons/icon.svg")
```

### Modify Calendar Dates
```java
// Adjust the interval
now.plusDays(15)  // 15 days from now
now.plusMonths(2) // 2 months from now
```

### Add Real Images
Replace placeholder image URLs with actual images:
```java
formation.setImageUrl("/uploads/actual-image.jpg");
```

---

## 📚 Next Steps

After seeding:

1. **Test the Frontend:** Visit your Angular application and verify all data displays correctly
2. **Test Authentication:** Login with the seeded users
3. **Test Forms:** Create new formations, blog posts, etc.
4. **Check Relationships:** Verify theme-formation, formation-review relationships
5. **Test Search/Filter:** Ensure all filtering and search features work

---

## 🆘 Need Help?

Common issues and solutions:
- **Can't login:** Verify password encoder matches (BCrypt)
- **Missing images:** Replace placeholder URLs with actual image paths
- **Foreign key errors:** Check table clearing order
- **Data not showing:** Verify `published=true` and `active=true` flags

For database-specific issues, check:
- Database connection settings in `application.properties`
- User permissions (need CREATE, INSERT, DELETE, UPDATE)
- Table structure matches entity definitions

---

## ✨ Features of This Seeder

- ✅ Complete relational data (themes ↔ formations ↔ reviews)
- ✅ Realistic French business data
- ✅ Proper date ranges (past projects, future events)
- ✅ BCrypt-encrypted passwords
- ✅ Published/active flags properly set
- ✅ Image URL placeholders for all visual content
- ✅ Safe clearing of existing data
- ✅ Verification queries included
- ✅ Professional descriptions and content

---

**Happy Seeding! 🌱**
