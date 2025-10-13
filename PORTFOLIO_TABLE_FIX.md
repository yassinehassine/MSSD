# Portfolio Table Fix - Using `portfolio_items`

## ✅ Issue Identified

The backend was querying the wrong table!

### ❌ Before (Wrong):
```java
@Table(name = "portfolio")
```
Backend was looking for data in `portfolio` table.

### ✅ After (Correct):
```java
@Table(name = "portfolio_items")
```
Backend now queries `portfolio_items` table where your data actually is!

## 🔧 Change Made

**File:** `mssd-backend/src/main/java/com/mssd/model/Portfolio.java`

**Line 13:** Changed table name from `"portfolio"` to `"portfolio_items"`

This tells JPA/Hibernate to fetch data from the correct table in your database.

## 🚀 How to Apply the Fix

### Step 1: Restart Backend

You need to restart the backend for the change to take effect:

```powershell
# Navigate to backend directory
cd "c:\Users\yassi\Downloads\MSSD-Mssd\MSSD-Mssd\mssd-backend"

# Stop the current backend (Press Ctrl+C if running)

# Clean and rebuild
mvnw clean install -DskipTests

# Start backend
mvnw spring-boot:run
```

### Step 2: Wait for Backend to Start

Look for this message in the console:
```
Started MssdApplication in X.XXX seconds
Tomcat started on port(s): 8080
```

### Step 3: Test the API

Open browser or PowerShell:

```powershell
# Test API directly
Invoke-RestMethod -Uri "http://localhost:8080/api/portfolio" -Method Get

# Or open in browser:
http://localhost:8080/api/portfolio
```

**Expected Result:** Should now return **all portfolios from `portfolio_items` table** with `active = true`

### Step 4: Check Frontend

Open: `http://localhost:4200/portfolio`

**Console should show:**
```
📦 Portfolio Page: Loading portfolios from API...
✅ Portfolio Page: Received 2 portfolios
```

And both portfolios should display!

## 📊 What This Fixes

### Home Page (`/`)
- Will now fetch from `portfolio_items` table
- Shows correct active portfolios

### Portfolio Page (`/portfolio`)
- Will now fetch from `portfolio_items` table
- Shows all active portfolios with correct data
- Categories work correctly

### Admin Panel (`/admin/portfolio`)
- Already working correctly
- Reads and writes to `portfolio_items` table

## 🎯 SQL Query Used (Behind the Scenes)

After the fix, backend executes:
```sql
SELECT p.* 
FROM portfolio_items p 
JOIN formations f ON p.formation_id = f.id
WHERE p.active = true 
ORDER BY p.created_at DESC
```

**Before fix, it was trying:**
```sql
SELECT p.* 
FROM portfolio p  -- ❌ This table doesn't exist or has no data
...
```

## ✅ Verification Steps

### 1. Check Database Table Exists
```sql
USE mssd_db;
SHOW TABLES LIKE 'portfolio_items';
-- Should return: portfolio_items
```

### 2. Check Data in Table
```sql
SELECT id, title, active, created_at 
FROM portfolio_items 
WHERE active = true;
-- Should return your 2 portfolios
```

### 3. Test Backend API
```powershell
curl http://localhost:8080/api/portfolio
# Should return JSON array with both portfolios
```

### 4. Test Frontend
- Open: `http://localhost:4200/portfolio`
- Should see both portfolios
- Check console (F12) for success logs

## 📝 Quick Summary

| Component | Before | After | Status |
|-----------|--------|-------|--------|
| Backend Entity | `@Table(name = "portfolio")` | `@Table(name = "portfolio_items")` | ✅ Fixed |
| Admin Panel | ✅ Correct | ✅ Correct | ✅ Working |
| Home Page | ❌ Wrong table | ✅ Correct table | ✅ Will work after restart |
| Portfolio Page | ❌ Wrong table | ✅ Correct table | ✅ Will work after restart |

## 🎉 Expected Result

After restarting the backend:

1. **API returns all active portfolios from `portfolio_items`**
   - Should see your 2 portfolios (ffff and yyyyyyy)

2. **Home page shows all portfolios**
   - Console: `✅ Home Page: Loaded 2 portfolios for display`

3. **Portfolio page shows all portfolios**
   - Console: `✅ Portfolio Page: Received 2 portfolios`
   - Both portfolios display with images and details
   - Category filters work

4. **Admin panel continues working**
   - Already using correct table

## 🔄 Next Steps

1. **Stop current backend** (Ctrl+C)
2. **Run:**
   ```powershell
   cd "c:\Users\yassi\Downloads\MSSD-Mssd\MSSD-Mssd\mssd-backend"
   mvnw clean install -DskipTests
   mvnw spring-boot:run
   ```
3. **Wait for "Started MssdApplication..."**
4. **Test:** Open `http://localhost:8080/api/portfolio` in browser
5. **Verify:** Both portfolios should appear!
6. **Check frontend:** Open `http://localhost:4200/portfolio`

---

## 🐛 If Still Not Working After Restart

### Check Database Connection
```sql
-- Verify table exists
SHOW TABLES LIKE 'portfolio_items';

-- Verify data exists
SELECT COUNT(*) FROM portfolio_items WHERE active = true;
-- Should return: 2
```

### Check Backend Logs
Look for errors like:
- `Table 'mssd_db.portfolio_items' doesn't exist` ❌ (table name issue)
- `Table 'mssd_db.portfolio' doesn't exist` ✅ (This means fix is applied!)

### Check Application Properties
File: `mssd-backend/src/main/resources/application.properties`

Should have:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mssd_db
spring.jpa.hibernate.ddl-auto=update
```

---

**This single change fixes the entire portfolio fetching issue! The backend now correctly queries `portfolio_items` where your data actually exists.** 🎯
