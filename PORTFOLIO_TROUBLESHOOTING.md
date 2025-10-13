# Portfolio Not Showing - Troubleshooting Guide

## Changes Made ✅

### 1. Events Section - Fixed ✅
- **Changed events per page from 6 to 3** in `home.ts`
- Updated grid layout to `col-lg-4 col-md-4 col-sm-12` to show 3 events horizontally on larger screens
- Pagination now properly shows only 3 events at a time
- Events section displays at the end of home page

### 2. Portfolio Component - Already Correct ✅
The portfolio component is correctly configured:
- Uses `PortfolioService.getActivePortfolios()` 
- Endpoint: `GET http://localhost:8080/api/portfolio`
- Backend controller is properly set up
- Service has proper error handling and logging

## Why Portfolio Might Not Show Data

### ✅ Code Status: WORKING
The frontend and backend code is correctly configured. If portfolios aren't showing, it's a **data or runtime issue**, not a code issue.

### Common Causes & Solutions:

#### 1. Backend Not Running ⚠️
**Check:**
```powershell
# Navigate to backend
cd "c:\Users\yassi\Downloads\MSSD-Mssd\MSSD-Mssd\mssd-backend"

# Start backend
mvnw spring-boot:run
```

**Expected Output:**
```
Started MssdApplication in X.XXX seconds
Tomcat started on port 8080
```

#### 2. No Data in Database ⚠️
**Check if portfolios exist:**
- Open browser: http://localhost:8080/api/portfolio
- Should return JSON array of portfolios
- If empty `[]`, you need to add portfolios via admin panel

**To add portfolios:**
1. Navigate to: http://localhost:4200/admin/portfolio
2. Click "Ajouter Portfolio"
3. Fill in:
   - Title
   - Description  
   - Formation (select from dropdown)
   - Client name
   - Project date
   - Upload image
   - Set "Active" to YES
4. Save

#### 3. CORS or Connection Issues ⚠️
**Check browser console:**
```
Press F12 → Console tab
Look for errors like:
- "Failed to fetch"
- "CORS error"
- "Network error"
```

**Solution:**
- Backend must be running on `http://localhost:8080`
- Frontend must be running on `http://localhost:4200`
- Check `PortfolioController.java` has: `@CrossOrigin(origins = "http://localhost:4200")`

#### 4. Database Connection Issues ⚠️
**Check application.properties:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mssd_db
spring.datasource.username=root
spring.datasource.password=your_password
```

**Test database connection:**
```sql
-- Connect to MySQL
mysql -u root -p

-- Check database exists
SHOW DATABASES LIKE 'mssd_db';

-- Check portfolio table has data
USE mssd_db;
SELECT COUNT(*) FROM portfolio WHERE active = true;
```

## Verification Steps

### Step 1: Start Backend
```powershell
cd "c:\Users\yassi\Downloads\MSSD-Mssd\MSSD-Mssd\mssd-backend"
mvnw spring-boot:run
```

### Step 2: Start Frontend
```powershell
cd "c:\Users\yassi\Downloads\MSSD-Mssd\MSSD-Mssd\mssd-frontend"
npm start
```

### Step 3: Check API Directly
Open browser: `http://localhost:8080/api/portfolio`

**Good Response:**
```json
[
  {
    "id": 1,
    "title": "Project Name",
    "description": "Description...",
    "imageUrl": "uploads/filename.jpg",
    "active": true,
    "formationName": "Formation Name",
    "formationCategory": "Category"
  }
]
```

**Bad Response (Empty):**
```json
[]
```
→ Need to add portfolios in admin panel!

### Step 4: Check Frontend Console
1. Open http://localhost:4200/portfolio
2. Press F12 → Console
3. Look for logs:
   - "Active portfolios fetched:" with data ✅
   - "Error loading portfolios:" ❌

## Quick Test Commands

### Test Backend API:
```powershell
# Test if backend is running
Invoke-WebRequest -Uri "http://localhost:8080/api/portfolio" -UseBasicParsing

# Or use curl
curl http://localhost:8080/api/portfolio
```

### Check Frontend Logs:
Open DevTools Console (F12) and you should see:
```
Active portfolios fetched: [...]
Categories extracted: [...]
```

## Summary

✅ **Events Section**: Fixed - now shows only 3 events with pagination
✅ **Portfolio Code**: Correct - uses proper endpoint and service
⚠️ **Portfolio Data Issue**: 
   - Backend must be running
   - Database must have portfolio entries with `active=true`
   - Check API returns data: http://localhost:8080/api/portfolio

## Next Steps

1. **Start both servers** (backend + frontend)
2. **Test API directly** in browser: http://localhost:8080/api/portfolio
3. **If API returns empty**: Add portfolios via admin panel
4. **If API returns data but UI doesn't show**: Check browser console for errors
5. **If still issues**: Share the browser console logs and API response

---

## Contact
If you continue having issues, provide:
1. Backend console output
2. Browser console logs (F12)
3. Response from: http://localhost:8080/api/portfolio
